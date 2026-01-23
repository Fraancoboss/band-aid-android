# Límites de Datos

## Entradas Externas
- Resultados de búsqueda en `/medicamentos`.
- Detalle de medicamento en `/medicamento`.
- Secciones segmentadas en `/docSegmentado/*`.
- Búsqueda textual en `/buscarEnFichaTecnica`.

## Transformaciones Permitidas
- Filtrar resultados hasta un único seleccionado.
- Extraer secciones específicas por ID.
- Eliminar HTML y mostrar texto plano.

## Datos que se Descartan
- Cuerpos completos no solicitados.
- Resultados no seleccionados tras la elección del usuario.
- Cualquier dato no requerido por la pantalla actual.

## Datos que NUNCA se Guardan
- Contenido de prospecto o ficha técnica (completo o parcial).
- HTML o texto derivado de fuentes externas.
- Identificadores externos fuera del contexto de sesión.

## Separación Dominio vs DTOs
- Los modelos de dominio no se modifican ni se rellenan con contenido externo.
- Los datos externos se mapean a estructuras transitorias en memoria.

## Estado del documento
- Versión: v0.2 (planificación)
- Tipo: implementable
- Impacto en código: ninguno
