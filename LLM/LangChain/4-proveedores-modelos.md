# 4. Proveedores y modelos

Guía corta para elegir proveedores y ajustar configuración de modelos en LangChain.

## 4.1 Mapa rápido de proveedores

| Proveedor | Librería LangChain | Notas prácticas |
| --------- | ------------------ | --------------- |
| OpenAI / Azure OpenAI | `langchain-openai` | Chat (`gpt-4.1`, `gpt-3.5-turbo`), embeddings (`text-embedding-3-*`). En Azure, definir `openai_api_type`, `azure_deployment`, `azure_endpoint`. |
| Google Generative AI | `langchain-google-genai` | Modelos `gemini-1.5-*`. Requiere `GOOGLE_API_KEY`. |
| Anthropic | `langchain-anthropic` | `claude-3-*`. Muy bueno en instrucciones largas; tokens caros. |
| Hugging Face / local | `langchain-huggingface`, `langchain-community` | Modelos open-source; vigilar VRAM y cuantización. Puede usarse con `TextGenerationInference` o `Ollama`. |

## 4.2 Selección rápida de modelo

- **Prototipo**: `gpt-4o-mini` o `gpt-3.5-turbo` (coste bajo).
- **Producción / calidad**: `gpt-4.1` / `claude-3.5-sonnet` (según disponibilidad).
- **Contexto largo**: variantes `*-1.5` o modelos con 100k+ tokens (ej.: `gpt-4.1`).
- **Local**: usa Ollama para pruebas offline; ajustar temperatura baja para estabilidad.

## 4.3 Configuración típica (OpenAI)

- Variables:
  - `OPENAI_API_KEY` o `AZURE_OPENAI_API_KEY`.
  - `OPENAI_BASE_URL` si se usa proxy/self-hosted.
- Parámetros comunes:
  - `model`, `temperature`, `max_tokens`, `seed` (para reproducibilidad en tests).
- Reintentos:
  - Configurar `max_retries` y backoff en el `Client` o vía `Runnable.retry()`.

## 4.4 Cost control y cuotas

- Definir límites por entorno (ej.: `MAX_PROMPT_TOKENS`, `MAX_OUTPUT_TOKENS`).
- Registrar uso con callbacks y métricas (LangSmith u OTEL).
- Implementar guardrails básicos (ej.: truncar entradas, rechazar archivos enormes).
