# Apuntes del curso LLM con LangChain

Estructura base para documentar el curso `llm-py-6709587/langchain` (Udemy), alineada al formato usado en `RabbitMQ/` (capítulos numerados y temas separados).

## Índice / capítulos

- `1-introduccion.md`: visión general de LangChain y contexto.
- `2-entorno.md`: prerrequisitos, venv y dependencias.
- `3-conceptos-basicos.md`: runnables, prompts, parsers, memoria.
- `4-proveedores-modelos.md`: elección de modelos y configuración.
- `5-rag.md`: flujo de RAG, stores y evaluación.
- `6-agentes.md`: tipos de agentes y definición de tools.
- `7-buenas-practicas.md`: costes, seguridad y observabilidad.
- `notas/01-previo.md`: registro inicial del setup (se mantiene como histórico).

## Convención de carpetas (sugerida)

```
.
├── README.md
├── 1-introduccion.md
├── 2-entorno.md
├── 3-conceptos-basicos.md
├── 4-proveedores-modelos.md
├── 5-rag.md
├── 6-agentes.md
├── 7-buenas-practicas.md
├── notas/                 # Notas legacy/extra
│   └── 01-previo.md
├── ejemplos/              # Código por tema
│   ├── 01-hello-langchain/
│   ├── 02-prompts/
│   ├── 03-memory/
│   ├── 04-rag/
│   ├── 05-agente-tools/
│   └── 06-streamlit-app/
└── venv/                  # Entorno virtual
```

## Checklist rápido de entorno

- [ ] `python3 --version` muestra 3.12.x
- [ ] `python -m venv venv` creado (o ya existe)
- [ ] `source venv/bin/activate` para activar
- [ ] `pip install langchain langchain-openai langchain-google-genai streamlit` (ya instalado)
- [ ] Variables de entorno para APIs:
  - `OPENAI_API_KEY`
  - `GOOGLE_API_KEY` (si usas Google GenAI)
  - Otros proveedores que uses

## Notas de progreso

- Día 1: entorno creado y paquetes instalados (`langchain==1.1.0`, `langchain-openai`, `langchain-google-genai`, `streamlit`).
- Capítulos 1–7 creados con la misma estructura que `RabbitMQ/` para ir completando.
- Pendiente: crear ejemplo “hello world” en `ejemplos/01-hello-langchain/` y completar códigos por tema.
