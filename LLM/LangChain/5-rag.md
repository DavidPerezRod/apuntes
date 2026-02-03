# 5. Retrieval Augmented Generation (RAG)

Cómo enriquecer prompts con contexto externo usando embeddings y recuperación.

## 5.1 Flujo general

1) Ingesta: parsear fuentes (PDF/HTML/MD), limpiar y chunkear.  
2) Indexado: generar embeddings y guardar en un vector store.  
3) Retrieval: buscar k chunks relevantes (similaridad, MMR, filtros).  
4) Composición: `context` → prompt → modelo → parser.  
5) Evaluación: verificar grounding y relevancia.

## 5.2 Chunks y embeddings

- Chunking:
  - Tamaño base 400–800 tokens; solapamiento 10–20%.
  - Ajustar según el modelo y el dominio (código vs. prosa).
- Embeddings recomendados:
  - OpenAI `text-embedding-3-large/small`.
  - Alternativas open-source: `all-MiniLM-*`, `nomic-embed-*` (via `langchain-community`).

## 5.3 Vector stores comunes

| Store | Uso rápido | Cuándo elegirlo |
| ----- | ---------- | ---------------- |
| Chroma | Local, sencillo, zero-config | Prototipo o demos en local |
| Pinecone | SaaS escalable, filtros metadata | Producción con SLA |
| FAISS | Local embebido, alto rendimiento | Offline o entornos on-prem |
| Weaviate / Qdrant | API gRPC/HTTP, buenas features de filtros | Necesitas filtros avanzados y despliegue propio |

## 5.4 Patrones de retrieval

- **Similarity**: KNN clásico; rápido y suficiente en muchos casos.
- **MMR (Maximal Marginal Relevance)**: balancea relevancia y diversidad (evita duplicados).
- **Contextual compression**: re-ranking o resumen de chunks antes de mandarlos al modelo.
- **Ensembles**: combinar BM25 + embeddings para dominios con vocabulario específico.

## 5.5 Evaluación mínima

- Métricas automáticas: precisión@k sobre un set pequeño con preguntas etiquetadas.
- Observabilidad: log de consultas, `retriever` stats, y tracing para ver qué contextos lee el modelo.
- Guardrails: remover PII en ingesta, limitar tamaño de archivos y validar tipos.
