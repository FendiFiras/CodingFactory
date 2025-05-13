import pytesseract
from pdf2image import convert_from_path
import re
import sys
from fuzzywuzzy import fuzz
import os

# Debug: print arguments received
print(f"FROM PYTHON: Received arguments: {sys.argv}")

# ✅ Configure tesseract executable path (update this to match your installation)
pytesseract.pytesseract.tesseract_cmd = r"C:\Users\OCTANET\aiali\tesseract.exe"

# ✅ Set the path to Poppler's bin folder (update this to your Poppler path)
POPPLER_PATH = r"C:\Users\OCTANET\Downloads\Release-24.08.0-0\poppler-24.08.0\Library\bin"  # adjust to your version and installation path

# Extract text from PDF using OCR
def extract_text(pdf_path):
    if not os.path.exists(pdf_path):
        print(f"FROM PYTHON: PDF file does not exist at: {pdf_path}")
        return ""

    try:
        # Convert PDF to images
        images = convert_from_path(pdf_path, poppler_path=POPPLER_PATH)
        text = ""
        for img in images:
            text += pytesseract.image_to_string(img)
        return text
    except Exception as e:
        print(f"FROM PYTHON: Error extracting text from PDF: {e}")
        return ""

# Match skills between required and CV
def match_skills(cv_text, required_skills):
    matched_skills = 0
    cleaned_text = re.sub(r'[^\w\s]', '', cv_text.lower())
    for skill in required_skills:
        for word in cleaned_text.split():
            if fuzz.partial_ratio(skill.lower(), word) > 90:
                matched_skills += 1
                break
    return matched_skills

# Calculate matching score
def calculate_match_score(pdf_path, required_skills):
    cv_text = extract_text(pdf_path)
    if cv_text:
        matched_count = match_skills(cv_text, required_skills)
        total_skills = len(required_skills)
        score = (matched_count / total_skills) * 100 if total_skills > 0 else 0
        return score
    else:
        print("FROM PYTHON: No text extracted from the CV.")
        return 0

# Entry point
if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python ai_score.py <cv_path> <required_skills>")
        sys.exit(1)

    cv_path = sys.argv[1]
    required_skills = sys.argv[2].split(',')

    match_score = calculate_match_score(cv_path, required_skills)

    # Final output for Java to parse
    print(f" {match_score:.2f}")
