# 6. Agentes y herramientas

Resumen de cómo estructurar agentes en LangChain y buenas prácticas básicas.

## 6.1 Cuándo usar un agente

- Necesitas orquestar llamadas dinámicas a APIs/DBs en función de la pregunta del usuario.
- El número de pasos no es fijo o depende de resultados intermedios.
- No uses un agente si el flujo es estático y predecible: una chain secuencial es más barata y robusta.

## 6.2 Tipos comunes

- **ReAct / Tool-Calling**: el modelo decide la herramienta y argumentos; retorna “observaciones”.
- **Plan-and-execute**: primero genera un plan de pasos, luego los ejecuta (útil si hay latencia alta).
- **Router**: selecciona cadena o agente según la intención (classification → ruta A, preguntas → ruta B).

## 6.3 Definición de tools

- Interface básica: `{name, description, args_schema/opcional}`.
- Validar argumentos con Pydantic para evitar llamadas inválidas.
- Limitar el set de tools por caso de uso (menos confusión para el modelo).
- Añadir ejemplos en el prompt de agente con formato claro (ej.: JSON de acciones).

## 6.4 Observabilidad y seguridad

- Registrar cada llamada de tool (inputs/outputs) para depurar.
- Establecer límites de pasos o de llamadas para evitar loops.
- Sanitizar respuestas de tools antes de devolverlas al usuario (SQL, HTML).
- Evaluar el agente con escenarios de “comportamiento malicioso” y entradas adversarias.

## 6.5 Plantilla mínima de agente ReAct

```text
You are an assistant with access to tools.
Use the following format:

Thought: what to do
Action: one of {tool_names}
Action Input: arguments
Observation: result
... (repeat)
Final Answer: respuesta al usuario
```
