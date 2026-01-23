# Referencia de Endpoints CIMA

## Base URL
`https://cima.aemps.es/cima/rest`

## Tabla de Endpoints

| Endpoint | Método | Parámetros usados | Caso de uso | Ejemplo |
|---|---|---|---|---|
| `/medicamentos` | GET | `nombre`, `practiv1`, `pagina` | Búsqueda de medicamentos | `https://cima.aemps.es/cima/rest/medicamentos?nombre=ibuprofeno&pagina=1` |
| `/medicamento` | GET | `nregistro` o `cn` | Detalle del medicamento seleccionado | `https://cima.aemps.es/cima/rest/medicamento?nregistro=80298` |
| `/docSegmentado/secciones/{tipoDoc}` | GET | `nregistro` | Listar secciones disponibles | `https://cima.aemps.es/cima/rest/docSegmentado/secciones/2?nregistro=80298` |
| `/docSegmentado/contenido/{tipoDoc}` | GET | `nregistro`, `seccion` (opcional) | Obtener contenido completo o por sección | `https://cima.aemps.es/cima/rest/docSegmentado/contenido/2?nregistro=80298&seccion=3` |
| `/buscarEnFichaTecnica` | POST | JSON con `seccion`, `texto`, `contiene` | Búsqueda textual en ficha técnica | `https://cima.aemps.es/cima/rest/buscarEnFichaTecnica` |

## Notas
- `tipoDoc`: `1` = ficha técnica, `2` = prospecto.
- Uso informativo, no prescriptivo.
- No se guarda contenido externo.

## Estado del documento
- Versión: v0.2 (planificación)
- Tipo: implementable
- Impacto en código: ninguno
