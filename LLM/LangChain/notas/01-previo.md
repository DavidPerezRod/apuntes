# 01. Previo: entorno Python y dependencias

Resumen de los pasos realizados para preparar el entorno del curso en `~/repos/udemy/llm-py-6709587/langchain`.

## Pasos ejecutados

- Verificar versiones: `python3 --version` → 3.12.3, `pip3 --version` → 24.0.
- Alias para usar `python`: se añadió `alias python=python3` en `~/.zshrc`.
- Crear `venv`: se intentó `python -m venv venv` y falló por falta de `ensurepip` → se instaló el paquete `python3.12-venv` (`sudo apt install python3.12-venv`).
- Recrear el entorno: `python -m venv venv`.
- Activar entorno: `source venv/bin/activate`.
- Instalar dependencias principales: `pip install langchain==1.1.0 langchain-openai langchain-google-genai streamlit`.
- Verificación: `pip show langchain` y `import langchain; print(langchain.__version__)` → 1.1.0.

## Siguientes pasos sugeridos

- Exportar claves antes de probar ejemplos:
  - `export OPENAI_API_KEY=...`
  - `export GOOGLE_API_KEY=...` (si usas GenAI)
- Crear `ejemplos/01-hello-langchain/` con un “hola mundo” que llame a un LLM y muestre respuesta por consola o Streamlit.
- Añadir notas por módulo en `notas/` (intro, conceptos, RAG, agentes, etc.).
