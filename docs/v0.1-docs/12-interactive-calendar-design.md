# Diseno del Calendario Interactivo

Estado: diseno conceptual. En v0.1 solo existe una vista pasiva que lista CalendarEntry si existen.

## 1. Proposito del Calendario
- Proveer una vista por fecha de las dosis esperadas y su estado real.
- Permitir al usuario revisar el cumplimiento diario de medicacion.
- Capturar el estado subjetivo diario sin afectar registros de dosis.

## 2. Alcance y Responsabilidades del Calendario
- Presentar dias con cero o mas CalendarEntry asociados.
- Derivar estados de los dias a partir de CalendarEntry y DoseLog.
- Permitir la seleccion de un dia para inspeccion y registro de estado subjetivo.

## 3. Tipos de Entradas del Calendario
- Entrada esperada: CalendarEntry programado sin confirmacion.
- Entrada completada: CalendarEntry con DoseLog asociado.
- Entrada omitida: CalendarEntry marcado como no tomado por accion del usuario.
- Entrada perdida: CalendarEntry que expiro sin confirmacion.

## 4. Definiciones de Estado Visual (conceptual)
- Dia sin dosis: no existen CalendarEntry para esa fecha.
- Dia pendiente: al menos un CalendarEntry esperado sin resolver.
- Dia completado: todas las entradas del dia tienen DoseLog asociado.
- Dia mixto: combinacion de completadas y no completadas.
- Dia omitido: todas las entradas del dia marcadas como omitidas.
- Dia perdido: todas las entradas del dia expiradas sin confirmacion.

## 5. Modelo de Interaccion a Nivel de Dia
- El usuario selecciona un dia para ver sus CalendarEntry.
- El sistema muestra estado de cada entrada: completada, pendiente, omitida o perdida.
- El usuario puede registrar un estado subjetivo diario para esa fecha.

## 6. Relacion con CalendarEntry y DoseLog
- El calendario se alimenta de CalendarEntry como fuente de verdad.
- Los DoseLog determinan si una entrada esta completada.
- No se crea estado paralelo para el calendario; se deriva de entidades existentes.

## 7. Registro de Estado Subjetivo Diario
- Cada fecha puede tener una nota subjetiva independiente de las dosis.
- La nota no modifica ni invalida DoseLog.
- La nota puede editarse sin afectar el historico de medicacion.

## 8. Reglas de Consistencia e Integridad de Datos
- Las vistas del calendario se derivan de CalendarEntry y DoseLog.
- No se permite mutar datos historicos de dosis desde el calendario.
- Los cambios en planes futuros regeneran entradas futuras sin alterar logs existentes.

## 9. Casos Borde y Escenarios de Falla
- Dias sin entries deben mostrarse como vacios sin estado de dosis.
- Cambios de horario o zona deben recalcular la asignacion de entradas por fecha.
- Eliminacion de una medicina debe remover entradas futuras pero conservar logs historicos.
- Notas subjetivas deben persistir aunque se edite el plan de dosis.

## 10. Fuera de Alcance
- Mecanismos de renderizado o componentes visuales.
- Persistencia especifica de notas o entradas.
- Sincronizacion externa o multi-dispositivo.
