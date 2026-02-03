# 3. Conceptos básicos de LangChain

Cómo se modelan los flujos y los bloques esenciales antes de entrar a integraciones o RAG.

## 3.1 Runnables y Chains

- **Runnable** es la interfaz unificada para componer pasos (`invoke`, `batch`, `stream`).
- Composición:
  - **Secuencial**: `prompt | llm | parser`.
  - **Paralelo**: `RunnableParallel` para fan-out (ej.: varias consultas a la vez).
- Ventajas: tipado de entradas/salidas, facilidad de prueba, reutilización en CLI o web.

## 3.2 PromptTemplates y formatos

- Text vs Chat templates (`PromptTemplate` vs `ChatPromptTemplate`).
- Placeholders comunes: `{context}`, `{question}`, `{history}`.
- Buenas prácticas:
  - Separar instrucciones del input del usuario.
  - Añadir ejemplos breves (few-shot) si el modelo lo requiere.
  - Controlar longitud (trimming) antes de enviar al modelo.

## 3.3 Output parsers

- **StructuredOutputParser / PydanticOutputParser**: generan validaciones de esquema.
- **Enum / Retry parsers**: permiten reintentar con mensajes de corrección al modelo.
- Útiles para:
  - **Agents**: validar acciones y argumentos.
  - **RAG**: asegurar JSON bien formado antes de post-procesar.

## 3.4 Memory

- Tipos comunes:
  - **Buffer**: historial completo (corta pronto el contexto).
  - **BufferWindow**: últimas `k` interacciones.
  - **Summary**: condensación progresiva.
  - **VectorStoreRetrieverMemory**: guarda mensajes en un store vectorial.
- Decide en función de:
  - Límite de tokens del modelo.
  - Necesidad de precisión vs. costo.

## 3.5 Callbacks y tracing

- `stdout`, `langchain.debug`, o callbacks personalizados para logs.
- **LangSmith**: panel para trazar runs, métricas, y comparar prompts.
- Consejo: habilitar trazas desde el inicio del curso para iterar más rápido.
