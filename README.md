# Band-AId

Band-AId es una aplicacion Android local-only para recordatorios de medicacion y registro de tomas. El proyecto es un unico modulo de aplicacion Android con layouts XML y ViewBinding, pensado para ejecutarse completamente en el dispositivo.

## Estado Actual (v0.1)
- Implementado: lista de medicinas, detalle de medicina (solo lectura), registro manual de tomas, vista pasiva de calendario.
- No implementado: trabajo en segundo plano, notificaciones, persistencia, sincronizacion en la nube o recordatorios automaticos.
- Build: compila y se ejecuta localmente cuando el SDK de Android esta configurado.

## Alcance y No Objetivos
Alcance:
- Planes de medicacion locales y registro de tomas
- Vista de calendario en solo lectura ligada a entradas existentes
- Registro manual de tomas sin automatizacion en segundo plano

No objetivos:
- Sincronizacion en la nube o servicios externos
- Recordatorios automaticos o ejecucion en segundo plano
- Consejos medicos o decisiones clinicas

## Referencia de Arquitectura
- El diseno y las decisiones del sistema estan documentados en `docs/v0.1-docs/` con archivos numerados.
- Los diagramas Mermaid viven en `docs/diagrams/`.

## Requisitos de Build
- JDK 17 instalado
- Android SDK instalado localmente
- Ruta del SDK configurada via `ANDROID_HOME` o `local.properties`
- Windows 10/11 es el objetivo principal de desarrollo
- Gradle Wrapper incluido y requerido

## Build y Despliegue Local
1) Abre el proyecto en Android Studio o usa una terminal en la raiz del proyecto.
2) Asegura que el SDK de Android este configurado localmente.
3) Compila el APK debug:
   - `.\gradlew assembleDebug`
4) Ejecuta localmente en un dispositivo/emulador usando Android Studio o tu flujo local.

Notas:
- `local.properties` no se versiona y debe crearse localmente cuando sea necesario.
- El proyecto esta diseniado para ejecucion local; no hay pipeline de distribucion definido.

## Checklist de Commit
- No hay secretos en el repositorio
- No hay rutas de SDK committeadas
- No hay servicios externos configurados
- El build requiere un Android SDK local

## Aviso
Este proyecto no ofrece consejo medico ni reemplaza orientacion profesional.
