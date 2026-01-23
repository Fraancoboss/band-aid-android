# IMPLEMENTATION.md — Integración CIMA (AEMPS) para Band-AId (diseño + pruebas con Postman)

## 0. Objetivo de este documento
Definir, de forma **extremadamente práctica**, cómo:
1) **probar en Postman** los endpoints de CIMA (AEMPS) con **URLs exactas**  
2) establecer el **flujo de datos** mínimo para, en una iteración posterior, integrar búsqueda/selección de medicamentos y extracción de secciones de prospecto/ficha técnica **sin scraping** (preferentemente)

Este documento **NO** implementa código y **NO** cambia el dominio.

---

## 1. Alcance y límites (pies de plomo)

### 1.1 Alcance
- Buscar medicamentos en CIMA.
- Seleccionar un medicamento por `nregistro` (o por `cn`).
- Obtener detalle del medicamento.
- Obtener **prospecto/ficha técnica segmentados por secciones** (ideal para “keyword → solo lo necesario → descartar”).

### 1.2 Fuera de alcance (por seguridad/ética)
- Prescripción o recomendación médica.
- “Dosis correcta para ti”.
- Interpretación clínica automática.
- Diagnóstico por síntomas.

> CIMA devuelve información oficial; Band-AId debe usarla de forma **informativa** y para **organizar** una rutina confirmada por el usuario (p. ej. “me lo ha indicado un profesional”).

## 1.3 Terminología usada en este documento
- **Buscar medicamento**: consultar listas en `/medicamentos`.
- **Seleccionar medicamento**: elegir un único resultado identificado por `nregistro` o `cn`.
- **Organizar rutina**: transformar una indicación ya confirmada en horarios/calendario.
- **Documento segmentado**: prospecto o ficha técnica devueltos por secciones vía `docSegmentado/*`.
- **Keyword**: palabra introducida por el usuario que se mapea a una sección concreta del documento.

---

## 2. Base URL oficial
La API usa esta base (todas las rutas cuelgan de aquí):  
`https://cima.aemps.es/cima/rest/[METODO]`

Ejemplo oficial en la guía:  
`https://cima.aemps.es/cima/rest/medicamento?nregistro=51347`

---

## 3. Postman — configuración recomendada

### 3.1 Crear Environment (opcional pero recomendado)
Crea un Environment llamado `CIMA` y añade:
- `baseUrl` = `https://cima.aemps.es/cima/rest`

Así, tus requests quedarán más limpias:  
`{{baseUrl}}/medicamentos?nombre=ibuprofeno&pagina=1`

### 3.2 Headers
En general CIMA devuelve JSON UTF-8.  
Para endpoints de documento segmentado, **recomendado**:
- `Accept: application/json` (para obtener secciones con contenido en JSON)

---

## 4. Endpoints críticos (los que necesitas sí o sí)

### 4.1 Listar medicamentos (búsqueda)
**Endpoint**  
`GET {{baseUrl}}/medicamentos?{condiciones}`

**Parámetros útiles**
- `nombre` = Nombre del medicamento
- `laboratorio` = Laboratorio
- `practiv1` / `practiv2` = Principio activo (texto)
- `cn` = Código nacional
- `atc` = Código ATC o descripción
- `nregistro` = Nº registro
- `pagina` = página de resultados (paginación)
- filtros booleanos útiles:
  - `receta` (1 con receta, 0 sin receta)
  - `comerc` (1 comercializados, 0 no)
  - además de otros (triángulo, huérfano, etc.)

**Requests listas para Postman**
1) Buscar por nombre  
`GET {{baseUrl}}/medicamentos?nombre=ibuprofeno&pagina=1`

2) Buscar por principio activo  
`GET {{baseUrl}}/medicamentos?practiv1=ibuprofeno&pagina=1`

3) Filtrar por receta (ejemplo)  
`GET {{baseUrl}}/medicamentos?nombre=ibuprofeno&receta=1&pagina=1`

**Resultado esperado**  
JSON con `totalFilas`, `pagina`, `tamanioPagina` y `resultados[]` (cada item con `nregistro`, `nombre`, etc.).

> Nota: este endpoint puede devolver mucho. En UI real: siempre obligar a seleccionar un item antes de pedir detalles.

---

### 4.2 Obtener detalle de 1 medicamento (cuando el usuario ya eligió)
**Endpoint**  
`GET {{baseUrl}}/medicamento?{condiciones}`

**Parámetros**
- `nregistro` = Nº registro
- `cn` = Código nacional

**Requests listas para Postman**
1) Por nregistro  
`GET {{baseUrl}}/medicamento?nregistro=80298`

2) Por código nacional  
`GET {{baseUrl}}/medicamento?cn=974931`

**Resultado esperado**  
JSON amplio del medicamento con `docs[]`, `viasAdministracion[]`, `formaFarmaceutica`, `dosis`, etc.

**Regla de oro**
- `/medicamentos` = lista / búsqueda  
- `/medicamento` = 1 seleccionado

---

## 5. Prospecto / ficha técnica SIN scraping: documentos segmentados (recomendado)
CIMA expone endpoints para obtener documentos **segmentados** por secciones:
- `tipoDoc = 1` (Ficha técnica)
- `tipoDoc = 2` (Prospecto)

Esto encaja con:  
“palabra clave → pedir solo la sección → extraer → descartar”.

---

### 5.1 Listar secciones existentes del documento (sin contenido)
**Endpoint**  
`GET {{baseUrl}}/docSegmentado/secciones/:tipoDoc?{condiciones}`

**Parámetros**
- `nregistro` (obligatorio)

**Requests listas para Postman**
1) Secciones del prospecto (tipoDoc=2)  
`GET {{baseUrl}}/docSegmentado/secciones/2?nregistro=80298`

2) Secciones de ficha técnica (tipoDoc=1)  
`GET {{baseUrl}}/docSegmentado/secciones/1?nregistro=80298`

**Resultado esperado**  
Lista de secciones con IDs. **Guardar el ID** de la sección “Cómo tomar / Posología”.

---

### 5.2 Obtener contenido del documento segmentado (todo o una sección)
**Endpoint**  
`GET {{baseUrl}}/docSegmentado/contenido/:tipoDoc?{condiciones}`

**Parámetros**
- `nregistro` (obligatorio)
- `seccion` (opcional)
  - si no lo envías: devuelve todas las secciones
  - si lo envías: devuelve solo esa sección

**Header importante**  
Este método depende de `Accept`:
- `Accept: application/json` devuelve JSON por secciones con número, título y contenido.

**Requests listas para Postman**
1) Todo el prospecto (JSON por secciones)  
`GET {{baseUrl}}/docSegmentado/contenido/2?nregistro=80298`  
`Accept: application/json`

2) Solo una sección del prospecto  
`GET {{baseUrl}}/docSegmentado/contenido/2?nregistro=80298&seccion=3`  
`Accept: application/json`

3) Toda la ficha técnica (JSON por secciones)  
`GET {{baseUrl}}/docSegmentado/contenido/1?nregistro=80298`  
`Accept: application/json`

**Resultado esperado**  
Estructura JSON con el contenido textual segmentado.

**Este enfoque evita**
- scraping de HTML
- parseo de PDF
- inconsistencias de layout

**Nota importante**  
El contenido obtenido de `docSegmentado/*`:
- se consume de forma puntual,
- se procesa en memoria,
- NO se persiste,
- NO se cachea,
- NO se almacena completo ni parcial.

---

## 6. Alternativa (menos recomendada): HTML dochtml (si segmentado no sirve)
La guía también documenta URLs HTML directas para prospecto y ficha técnica.

### 6.1 Ficha técnica completa (HTML)
`GET https://cima.aemps.es/cima/dochtml/ft/{nregistro}/FichaTecnica.html`

### 6.2 Ficha técnica por sección (HTML)
`GET https://cima.aemps.es/cima/dochtml/ft/{nregistro}/{seccion}/FichaTecnica.html`

### 6.3 Prospecto completo (HTML)
`GET https://cima.aemps.es/cima/dochtml/p/{nregistro}/Prospecto.html`

### 6.4 Prospecto por sección (HTML)
`GET https://cima.aemps.es/cima/dochtml/p/{nregistro}/{seccion}/Prospecto.html`

> Usar solo si `docSegmentado/*` no trae la información necesaria.

---

## 7. Endpoint extra: buscar texto en la ficha técnica (NO “diagnóstico”)
**Endpoint**  
`POST {{baseUrl}}/buscarEnFichaTecnica`

**Body (JSON)**  
```json
[
  {
    "seccion": "4.1",
    "texto": "cáncer",
    "contiene": 1
  }
]
```

**Ejemplo 1** (buscar palabra en sección 4.1)  
```
POST {{baseUrl}}/buscarEnFichaTecnica
Content-Type: application/json
Body (raw JSON):
[
  { "seccion": "4.1", "texto": "acidez", "contiene": 1 }
]
```

**Ejemplo 2** (contiene “acidez” pero NO “estomago” en 4.1)  
```
[
  { "seccion": "4.1", "texto": "acidez", "contiene": 1 },
  { "seccion": "4.1", "texto": "estomago", "contiene": 0 }
]
```

Este endpoint permite **búsqueda textual**, no inferencia clínica.  
Cualquier uso debe considerarse exploratorio e informativo.

---

## 8. Flujo recomendado de pruebas en Postman
1. Buscar medicamentos por nombre (`/medicamentos`).
2. Seleccionar un resultado y copiar su `nregistro`.
3. Obtener el detalle del medicamento (`/medicamento`).
4. Listar secciones del prospecto (`/docSegmentado/secciones/2`).
5. Identificar la sección relevante (p. ej. posología).
6. Obtener solo esa sección (`/docSegmentado/contenido/2` con `seccion`).
7. Verificar que el contenido es suficiente para extracción orientativa.
