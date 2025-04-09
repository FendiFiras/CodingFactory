import pytesseract
from pdf2image import convert_from_path
import re
import os
from fuzzywuzzy import fuzz

# Configure pytesseract to use the installed tesseract executable
pytesseract.pytesseract.tesseract_cmd = r"C:\Users\ali\python\tesseract.exe"  # Make sure to use the correct path to your tesseract executable
# Function to extract text from a PDF
def extract_text(pdf_path):
    # Convert PDF to images
    images = convert_from_path(pdf_path)
    text = ""

    # Extract text from each image using pytesseract
    for img in images:
        text += pytesseract.image_to_string(img)

    return text

# Function to match skills from CV to required skills
def match_skills(cv_text, required_skills):
    matched_skills = 0

    # Clean the CV text by removing special characters
    cleaned_text = re.sub(r'[^\w\s]', '', cv_text.lower())

    # Loop over each required skill and check if it's present in the CV
    for skill in required_skills:
        # Set a higher threshold (e.g., 90 or higher) to avoid false matches
        for word in cleaned_text.split():
            if fuzz.partial_ratio(skill.lower(), word) > 90:  # Strict match threshold
                matched_skills += 1
                break

    return matched_skills

# Function to calculate match score
def calculate_match_score(cv_path, required_skills):
    # Extract text from CV
    cv_text = extract_text(cv_path)
    print("CV Text Extracted:")
    print(cv_text)  # Debug: Print CV text for verification

    # Match skills
    matched_count = match_skills(cv_text, required_skills)
    print(f"Matched Skills Count: {matched_count}")

    # Calculate match score based on the number of matched skills
    total_skills = len(required_skills)
    score = matched_count / total_skills if total_skills > 0 else 0

    return score

# Example usage
path_to_cv = r"../uploads/cvs/cv anglais (1) (2).pdf"
required_skills = ["Java", "Spring Boot", "doker", "HTML"]  # Modify this list as needed

match_score = calculate_match_score(path_to_cv, required_skills)
print(f"Match Score: {match_score:.2f}")