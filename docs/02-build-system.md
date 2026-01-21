# Sistema de Build

- El proyecto usa Gradle Wrapper 8.14.3 para asegurar builds reproducibles.
- El catalogo de versiones esta en gradle/libs.versions.toml y centraliza versiones de plugins y dependencias.
- El modulo :app aplica el Android Application Plugin mediante el catalogo de versiones.
- La compatibilidad con Android Studio se asegura al usar Gradle Wrapper y el plugin oficial de Android.
- La opcion android.overridePathCheck=true esta activada para permitir rutas con caracteres no ASCII en Windows.
