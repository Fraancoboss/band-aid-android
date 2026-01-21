# Diseno de Arquitectura Funcional y de Dominio

## 1. Objetivos Arquitectonicos
- Mantener una estructura simple, explicita y facil de implementar.
- Separar responsabilidades entre UI, logica de dominio y manejo de datos sin introducir patrones complejos.
- Permitir evolucion gradual hacia persistencia y trabajo en segundo plano sin reescrituras.
- Garantizar que el flujo principal sea claro para el usuario: ver medicaciones, registrar tomas y revisar historico.

## 2. Flujo de Aplicacion de Alto Nivel
- Pantalla principal con lista de medicaciones activas y proximas dosis.
- Vista de detalle de una medicacion con su plan de dosis.
- Accion de registrar una toma en el momento que ocurre.
- Consulta de historial de tomas realizadas por fecha.

## 3. Vision General del Modelo de Dominio
- El usuario administra medicaciones y planes de dosis.
- Cada plan de dosis genera eventos esperados en el calendario.
- Las tomas reales se registran como eventos historicos independientes del plan.

## 4. Entidades Principales (campos)

### User
- Proposito: representar al usuario que gestiona su medicacion.
- Campos clave:
  - id: String
  - name: String
  - timezone: String
- Persistencia futura: se espera almacenamiento local para preferencias y datos propios del usuario.

### Medicine
- Proposito: representar un medicamento y sus datos base.
- Campos clave:
  - id: String
  - name: String
  - dosage: String
  - instructions: String
  - isActive: Boolean
- Persistencia futura: se espera almacenamiento local para mantener el catalogo personal de medicinas.

### DoseSchedule
- Proposito: definir un plan de dosis recurrente para una medicina.
- Campos clave:
  - id: String
  - medicineId: String
  - startDate: LocalDate
  - endDate: LocalDate?
  - timesOfDay: List<LocalTime>
  - frequencyDays: Int
- Persistencia futura: se espera almacenamiento local para generar eventos de calendario y validar recordatorios.

### DoseLog
- Proposito: registrar una toma real realizada por el usuario.
- Campos clave:
  - id: String
  - medicineId: String
  - scheduledTime: LocalDateTime?
  - takenAt: LocalDateTime
  - status: String
  - notes: String?
- Persistencia futura: se espera almacenamiento local para historico y analitica personal.

### CalendarEntry
- Proposito: representar una dosis esperada en el tiempo a partir del plan.
- Campos clave:
  - id: String
  - medicineId: String
  - scheduleId: String
  - expectedAt: LocalDateTime
  - isCompleted: Boolean
- Persistencia futura: se espera almacenamiento local para visualizar agenda y facilitar recordatorios.

## 5. Relaciones Entre Entidades
- User 1..N Medicine.
- Medicine 1..N DoseSchedule.
- DoseSchedule 1..N CalendarEntry.
- Medicine 1..N DoseLog.
- CalendarEntry 0..1 DoseLog (una dosis esperada puede o no estar registrada como tomada).

## 6. Limites de Responsabilidad de UI
- UI muestra listas y detalles; no decide reglas de negocio.
- UI inicia acciones de crear, editar o registrar tomas.
- La logica de validacion y transformacion de datos vive fuera de la UI.
- La UI refleja estados actuales de medicinas, planes, calendario e historial.

## 7. Propiedad de Datos y Ciclo de Vida
- La fuente de verdad es local en el dispositivo del usuario.
- Las entidades se crean, actualizan y desactivan desde la UI.
- Los planes de dosis generan entradas de calendario esperadas.
- Los registros de toma se crean al confirmar una toma real.
- No se define aun sincronizacion externa ni multi-dispositivo.

## 8. Fuera de Alcance (por ahora)
- Persistencia con una tecnologia especifica.
- Trabajo en segundo plano y notificaciones.
- Escaneo de medicamentos.
- Integraciones externas o servicios en la nube.
