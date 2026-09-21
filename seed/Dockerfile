FROM python:3.12-slim

WORKDIR /seed

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY generar_seed.py .

ENTRYPOINT ["python", "generar_seed.py"]
