FROM python:3.11-slim
WORKDIR /app
# Install system dependencies for mysql-connector if needed
# RUN apt-get update && apt-get install -y default-libmysqlclient-dev build-essential && rm -rf /var/lib/apt/lists/*

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .
EXPOSE 8083
CMD ["python", "main.py"]
