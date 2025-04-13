import pytesseract
from pdf2image import convert_from_path
import re
import sys
from fuzzywuzzy import fuzz
print(f"Received arguments: {sys.argv}")

# Configure pytesseract to use the installed tesseract executable
pytesseract.pytesseract.tesseract_cmd = r"C:\Users\ali\python\tesseract.exe"  # Ensure correct path

# Function to extract text from a PDF
def extract_text(pdf_path):
    try:
        images = convert_from_path(pdf_path)
        text = ""
        # Extract text from each image using pytesseract
        for img in images:
            text += pytesseract.image_to_string(img)
        return text
    except Exception as e:
        print(f"Error extracting text from PDF: {e}")
        return ""

# Function to match skills from CV to required skills
def match_skills(cv_text, required_skills):
    matched_skills = 0
    cleaned_text = re.sub(r'[^\w\s]', '', cv_text.lower())
    for skill in required_skills:
        for word in cleaned_text.split():
            if fuzz.partial_ratio(skill.lower(), word) > 90:
                matched_skills += 1
                break
    return matched_skills

# Function to calculate match score
def calculate_match_score(pdf_path, required_skills):
    cv_text = extract_text(pdf_path)
    if cv_text:
        matched_count = match_skills(cv_text, required_skills)
        total_skills = len(required_skills)
        score = (matched_count / total_skills) * 100 if total_skills > 0 else 0
        return score
    else:
        print("No text extracted from the CV.")
        return 0

# Main function to accept command line arguments
if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python ai_score.py <cv_path> <required_skills>")
        sys.exit(1)

    cv_path = sys.argv[1]
    required_skills = sys.argv[2].split(',')

    match_score = calculate_match_score(cv_path, required_skills)

print(f"{match_score:.2f}")
