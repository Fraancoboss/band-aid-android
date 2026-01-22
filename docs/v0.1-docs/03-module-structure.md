# Estructura de Modulos

- Unico modulo: :app
- Punto de entrada: app/src/main/java/com/bandaid/app/MainActivity.kt (implementado en v0.1)
- UI: XML layouts con ViewBinding (implementado en v0.1)
- Archivos principales:
  - app/src/main/AndroidManifest.xml
  - app/src/main/res/layout/activity_main.xml
  - app/src/main/res/values/strings.xml

Pantallas implementadas en v0.1:
- app/src/main/java/com/bandaid/app/ui/medicine/MedicineDetailActivity.kt (detalle read-only)
- app/src/main/java/com/bandaid/app/ui/calendar/CalendarActivity.kt (calendario pasivo)

Paquetes logicos existentes:
- com.bandaid.app.domain (modelos y contratos)
- com.bandaid.app.data.local (repositorios in-memory)
- com.bandaid.app.di (AppContainer manual)
- com.bandaid.app.ui (pantallas y adaptadores)

Actualmente no se ha definido una separacion formal por capas (data/domain/ui); la estructura es minima y funcional.
