# Build, Pruebas y Despliegue

## 1. Vision General del Proceso de Build
- El build se ejecuta de forma local mediante Gradle Wrapper.
- El proyecto requiere el SDK de Android configurado para compilar.
- El resultado esperado es un APK de depuracion o release segun el tipo de build.
Estado actual (v0.1): con el SDK configurado, el build compila y se ejecuta localmente.

## 2. Builds de Desarrollo vs Release
- Desarrollo: build de depuracion para validacion local y pruebas manuales.
- Release: build sin ofuscacion ni minificacion en el estado actual.
- No existen firmas o configuraciones de publicacion definidas.

## 3. Estrategia de Pruebas (Alcance Actual)
- No hay estrategia de pruebas automatizadas definida.
- No se promete cobertura ni suites de validacion.
- Las validaciones actuales son manuales.

## 4. Validacion Manual y Pruebas de Usuario
- Verificar flujo basico de lista y detalle de medicaciones.
- Confirmar registro manual de tomas y visualizacion read-only en detalle.
- Confirmar que la vista de calendario pasivo muestra entries solo si existen.

## 5. Expectativas de Despliegue
- Despliegue local en dispositivos durante desarrollo.
- No hay proceso de distribucion automatizada.
- No hay canales definidos para distribucion externa.

## 6. Limitaciones Operativas
- La aplicacion depende de configuracion local del SDK para compilar.
- La validacion depende de pruebas manuales del usuario o equipo.
- No hay mecanismos definidos para monitoreo o telemetria.

## 7. No Objetivos Explicitos
- No hay CI/CD ni pipelines automatizados.
- No hay cobertura de pruebas garantizada.
- No hay estrategia de publicacion definida.
