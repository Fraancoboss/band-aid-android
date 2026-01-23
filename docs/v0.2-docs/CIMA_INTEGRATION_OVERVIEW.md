# Panorama de Integración CIMA

## Propósito
Proveer un flujo local-only e informativo para consultar medicamentos con la API CIMA (AEMPS). La integración apoya la organización de un tratamiento confirmado por el usuario, sin recomendación médica.

## Alcance (Implementable)
- Buscar y seleccionar un medicamento por `nregistro` o `cn`.
- Obtener detalles estructurados para referencia del usuario.
- Recuperar secciones segmentadas de prospecto/ficha técnica para mostrar solo texto relevante.

## Fuera de Alcance
- Recomendación o prescripción médica.
- Diagnóstico o selección por síntomas.
- Persistencia de contenido externo.
- Sincronización en segundo plano o actualizaciones automáticas.

## Uso de Datos y Finalidad
- Los datos externos se usan solo con fines informativos y para organizar una rutina confirmada por el usuario.
- No se realiza inferencia clínica ni automatización.

## Flujo Conceptual de Datos (Sin Código)
1. El usuario busca por nombre o principio activo.
2. El usuario selecciona un único resultado (`nregistro` o `cn`).
3. La app consulta detalles y secciones segmentadas si aplica.
4. Se muestra el texto necesario para confirmación informativa.
5. El contenido externo se descarta al terminar la sesión.

## Estado del documento
- Versión: v0.2 (planificación)
- Tipo: conceptual
- Impacto en código: ninguno
