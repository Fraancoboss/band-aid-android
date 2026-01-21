# Build, Pruebas y Despliegue

## 1. Vision General del Proceso de Build
- El build se ejecuta de forma local mediante Gradle Wrapper.
- El proyecto requiere el SDK de Android configurado para compilar.
- El resultado esperado es un APK de depuracion o release segun el tipo de build.

## 2. Builds de Desarrollo vs Release
- Desarrollo: build de depuracion para validacion local y pruebas manuales.
- Release: build sin ofuscacion ni minificacion en el estado actual.
- No existen firmas o configuraciones de publicacion definidas.

## 3. Estrategia de Pruebas (Alcance Actual)
- No hay estrategia de pruebas automatizadas definida.
- No se promete cobertura ni suites de validacion.
- Las validaciones actuales son manuales.

## 4. Validacion Manual y Pruebas de Usuario
- Verificar flujo basico de recordatorios y registro de tomas.
- Confirmar estados en calendario y consistencia de historicos.
- Revisar la entrada de datos de medicaciones y ediciones basicas.

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
