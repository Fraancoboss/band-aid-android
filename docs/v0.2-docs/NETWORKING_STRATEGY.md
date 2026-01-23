# Estrategia de Red

## Objetivos
- Mantener acceso a red explícito y bajo demanda.
- Evitar trabajo en segundo plano y automatismos.
- Respetar el comportamiento local-only y sin persistencia.

## Estrategia (Implementable)
- Usar un cliente HTTP simple con requests bajo demanda.
- Aplicar timeouts conservadores para conexión y lectura.
- Tratar todas las respuestas como transitorias.

## Manejo de Errores
- Mostrar errores de red de forma legible.
- No reintentar automáticamente sin acción del usuario.
- Fallar de forma segura sin cambios en dominio.

## Timeouts
- Conexión: corto para evitar bloqueos.
- Lectura: moderado para respuestas lentas.
- Valores exactos configurables, manteniendo enfoque conservador.

## Offline / Fallbacks
- Si no hay red, mostrar mensaje claro y permitir reintento manual.
- No usar datos externos guardados como fallback.
- Volver al flujo local-only sin enriquecimiento externo.

## Racional (Por qué esta estrategia)
- Alinea con restricciones local-only y sin background.
- Evita sincronización oculta o cacheo implícito.
- Mantiene la integración auditable y reversible.

## Estado del documento
- Versión: v0.2 (planificación)
- Tipo: implementable
- Impacto en código: ninguno
