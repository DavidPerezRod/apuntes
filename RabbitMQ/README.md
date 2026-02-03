# Apuntes del curso LLM con LangChain

Estructura base para ir documentando el curso `llm-py-6709587/langchain` (Udemy). Adapta los puntos según avance el temario.

## Índice propuesto

- Introducción y objetivos del curso
- Instalación y entorno
  - Requisitos (Python 3.12+, `venv`)
  - Dependencias (`langchain`, `langchain-openai`, `langchain-google-genai`, `streamlit`, etc.)
  - Configuración de variables de entorno (API keys)
- Conceptos clave de LLMs y LangChain
  - Chains y Runnables
  - PromptTemplates y parsers
  - Memory
  - Tools / Agents
  - Output parsers y schemas (Pydantic)
- Proveedores y modelos
  - OpenAI / Azure OpenAI
  - Google Generative AI
  - Modelos locales (si aplica)
- Integraciones comunes
  - Vector stores y embeddings
  - Retrieval Augmented Generation (RAG)
  - Callbacks / tracing (LangSmith)
- Aplicaciones de ejemplo
  - CLI mínima
  - App web con Streamlit
  - Ejemplo de agente con herramientas
- Buenas prácticas y seguridad
  - Manejo de credenciales
  - Cost control / rate limits
  - Observabilidad y logging
- Problemas comunes y solución
- Recursos y enlaces

## Convención de carpetas (sugerida)

```
.
├── README.md            # Este índice y notas de progreso
├── notas/               # Apuntes por módulo
│   ├── 01-intro.md
│   ├── 02-entorno.md
│   ├── 03-conceptos.md
│   ├── 04-proveedores.md
│   ├── 05-rag.md
│   ├── 06-agentes.md
│   └── 99-cheatsheet.md
├── ejemplos/            # Código de ejemplo por tema
│   ├── 01-hello-langchain/
│   ├── 02-prompts/
│   ├── 03-memory/
│   ├── 04-rag/
│   ├── 05-agente-tools/
│   └── 06-streamlit-app/
└── venv/                # Entorno virtual
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
- Pendiente: crear primeras notas en `notas/` y un ejemplo “hello world” en `ejemplos/01-hello-langchain/`.
