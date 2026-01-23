# Flujo de App (v0.1)

```mermaid
flowchart TD
    A[Inicio de la app] --> B[Lista de medicamentos activos]
    B --> C[Detalle de medicamento]
    C --> D[Registro manual de toma]
    B --> E[Calendario pasivo (read-only)]
```

Este flujo refleja el comportamiento implementado en v0.1 sin automatizacion ni procesos en segundo plano.
