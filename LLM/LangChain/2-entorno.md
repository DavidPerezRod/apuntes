# 2. Entorno y dependencias

Resumen de cómo se configuró el entorno para el curso (`langchain` Udemy) y checklist para replicarlo.

## 2.1 Prerrequisitos

- Python 3.12.x (`python3 --version` → 3.12.3).
- `pip` 24.x.
- Paquete `python3.12-venv` instalado (necesario para `python -m venv` en Debian/Ubuntu).

## 2.2 Pasos ejecutados

- Crear/activar entorno virtual:
  - `python -m venv venv`
  - `source venv/bin/activate`
- Instalar dependencias principales:
  - `pip install langchain==1.1.0 langchain-openai langchain-google-genai streamlit`
- Verificación rápida:
  - `pip show langchain` o `python - <<'PY'\nimport langchain; print(langchain.__version__)\nPY` → 1.1.0.
- Alias opcional en `~/.zshrc`: `alias python=python3` (comodidad).

## 2.3 Variables de entorno necesarias

- `OPENAI_API_KEY` (o `AZURE_OPENAI_*` si se usa Azure).
- `GOOGLE_API_KEY` para Google Generative AI.
- Claves adicionales según otros proveedores o stores (ej.: `PINECONE_API_KEY`).

## 2.4 Problemas comunes y solución rápida

- `python -m venv venv` falla por `ensurepip`: instalar `python3.12-venv`.
- Conflicto de versiones de `pip`: actualizar con `python -m pip install --upgrade pip`.
- Streamlit en WSL: exportar `BROWSER=wslview` o ejecutar con `--server.enableCORS false` si hay bloqueos.

## 2.5 Siguientes pasos

- Crear `ejemplos/01-hello-langchain/` con un “hola mundo” de consola o Streamlit.
- Añadir notas específicas por tema en los siguientes archivos (3–7) para seguir el mismo esquema que los apuntes de RabbitMQ.

> Nota: El archivo original `notas/01-previo.md` se mantuvo como registro inicial; este capítulo consolida esa información con el formato común.
