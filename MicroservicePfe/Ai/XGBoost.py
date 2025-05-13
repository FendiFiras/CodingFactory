from flask import Flask, request, jsonify
import joblib
import numpy as np
import pandas as pd

app = Flask(__name__)

# 🔁 Chargement des modèles
xgb_model = joblib.load('xgb_model.pkl')        # Modèle XGBoost
kmeans = joblib.load("kmeans_model.pkl")        # Modèle KMeans
scaler = joblib.load("scaler.pkl")              # StandardScaler
pca = joblib.load("pca.pkl")                    # PCA à 2 composantes

# 🧩 Features attendues par les deux modèles
expected_features = [
    "Level",
    "University_GPA",
    "Field_of_Study",
    "Internships_Completed",
    "Projects_Completed",
    "Certifications",
    "Soft_Skills_Score",
    "Career_Satisfaction"
]

# 🔮 Route 1 : Prédiction du modèle XGBoost
@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.json
        df = pd.DataFrame([data])[expected_features]
        prediction = xgb_model.predict(df)[0]
        return jsonify({'prediction': int(prediction)})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

# 📊 Route 2 : Clustering avec KMeans
@app.route('/cluster', methods=['POST'])
def predict_cluster():
    try:
        data = request.json
        df = pd.DataFrame([data])[expected_features]

        # Standardisation + PCA
        scaled = scaler.transform(df)
        reduced = pca.transform(scaled)

        # Prédiction du cluster
        cluster = int(kmeans.predict(reduced)[0])
        return jsonify({'cluster': cluster})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

# 🚀 Lancement de l'API Flask
if __name__ == '__main__':
    app.run(port=5000)
