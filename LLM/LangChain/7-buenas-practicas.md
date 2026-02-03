# 7. Buenas prácticas y operación

Lineamientos rápidos para mantener costes bajo control, proteger credenciales y observar el comportamiento de las cadenas.

## 7.1 Manejo de credenciales

- Usar `.env` o gestor de secretos (no hardcodear claves).
- Variables separadas por entorno: `dev`, `staging`, `prod`.
- Rotar claves y limitar permisos en el dashboard del proveedor.

## 7.2 Costes y límites

- Definir `max_tokens` y `temperature` por defecto; revisar consumos con callbacks.
- Implementar truncado de input y filtros de tamaño de archivos.
- Añadir cuotas por usuario/servicio si se expone como API.

## 7.3 Observabilidad

- Activar `langchain.debug` en desarrollo para ver prompts y respuestas.
- Integrar con **LangSmith** o logs estructurados para runs (input, output, latencia, coste estimado).
- Alertas básicas: latencia elevada, tasa de reintentos, errores de tools.

## 7.4 Calidad y pruebas

- Tests con fixtures de prompts + respuestas esperadas (golden tests).
- Evaluaciones sintéticas para RAG: grounding y relevancia de contexto.
- Revisión manual de muestras cuando cambies de modelo o prompt (para detectar regresiones).

## 7.5 Seguridad

- Filtrar contenido ofensivo o PII si la app lo requiere (moderación previa).
- Validar URLs y queries generadas antes de ejecutarlas.
- Controlar qué tools puede invocar cada agente y con qué argumentos.
