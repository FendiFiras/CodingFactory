import os
import logging
import requests
import whisper
import ffmpeg
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from transformers import pipeline
import torch

app = FastAPI()

# Middleware CORS pour autoriser Angular
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4200"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Initialisation des modèles
logging.info("Chargement du modèle Whisper...")
transcription_model = whisper.load_model("base")
logging.info("Chargement du modèle de résumé...")
summarizer = pipeline("summarization", model="facebook/bart-large-cnn")

class VideoRequest(BaseModel):
    filename: str

@app.post("/analyze")
async def analyze_video(request: VideoRequest):
    filename = request.filename
    video_url = f"http://localhost:8087/event/{filename}"
    local_video_path = os.path.abspath("temp_video.mp4")
    local_audio_path = os.path.abspath("temp_audio.wav")

    try:
        # Téléchargement de la vidéo
        logging.info(f"Téléchargement depuis : {video_url}")
        with requests.get(video_url, stream=True) as r:
            r.raise_for_status()
            with open(local_video_path, 'wb') as f:
                for chunk in r.iter_content(chunk_size=8192):
                    f.write(chunk)

        if not os.path.exists(local_video_path) or os.path.getsize(local_video_path) == 0:
            return JSONResponse(content={"error": "Fichier vidéo non valide."}, status_code=400)

        # Extraction audio
        try:
            ffmpeg.input(local_video_path).output(local_audio_path, format='wav').run(overwrite_output=True)
            logging.info("Audio extrait avec succès.")
        except ffmpeg.Error as e:
            logging.error(f"Erreur ffmpeg : {e.stderr.decode()}")
            return JSONResponse(content={"error": "Erreur lors de la conversion de la vidéo."}, status_code=500)

        # Transcription avec Whisper
        logging.info("Début de la transcription...")
        result = transcription_model.transcribe(local_audio_path)
        transcription = result["text"]

        # Résumé avec Transformers
        chunks = [transcription[i:i+1000] for i in range(0, len(transcription), 1000)]
        summary_parts = []
        for chunk in chunks:
            summary_parts.append(summarizer(chunk, max_length=150, min_length=40, do_sample=False)[0]["summary_text"])
        summary = " ".join(summary_parts)

        return JSONResponse(content={"transcription": transcription, "summary": summary})

    except requests.RequestException as e:
        logging.error(f"Erreur téléchargement : {e}")
        return JSONResponse(content={"error": "Erreur de téléchargement de la vidéo."}, status_code=400)

    except Exception as e:
        logging.exception("Erreur inattendue.")
        return JSONResponse(content={"error": str(e)}, status_code=500)

    finally:
        # Nettoyage des fichiers temporaires
        for file_path in [local_video_path, local_audio_path]:
            try:
                if os.path.exists(file_path):
                    os.remove(file_path)
                    logging.info(f"Fichier supprimé : {file_path}")
            except Exception as e:
                logging.warning(f"Erreur suppression fichier : {e}")
