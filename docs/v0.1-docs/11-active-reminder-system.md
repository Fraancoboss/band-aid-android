# Sistema de Recordatorios Activos

Estado: diseno conceptual. No implementado en v0.1.

## 1. Definicion de un Recordatorio Activo
- Un recordatorio activo es un evento de dosis esperada que requiere confirmacion explicita del usuario.
- Su objetivo es registrar si la dosis fue tomada o no tomada, no solo avisar.
- No se considera completado hasta que exista una accion del usuario.

## 2. Ciclo de Vida del Recordatorio
- Creacion: se genera a partir de un CalendarEntry planificado.
- Pendiente: espera confirmacion del usuario dentro de la ventana esperada.
- Confirmado: el usuario registra una toma y se crea un DoseLog.
- Omitido: el usuario indica que no tomo la dosis, quedando registrado.
- Expirado: pasa la ventana sin confirmacion y se marca como no completado.

## 3. Condiciones de Disparo
- Se activa cuando la hora actual alcanza el momento esperado del CalendarEntry.
- Puede activarse dentro de una ventana de tolerancia configurable.
- Debe evitar duplicados para un mismo CalendarEntry.

## 4. Requisitos de Interaccion del Usuario
- El usuario debe poder confirmar "tomado" o "no tomado".
- Debe existir un mecanismo para agregar una nota opcional.
- La confirmacion debe reflejarse inmediatamente en el historial.

## 5. Relacion con CalendarEntry y DoseLog
- Cada recordatorio activo corresponde a un CalendarEntry.
- La confirmacion crea un DoseLog asociado al CalendarEntry.
- Si se omite o expira, el CalendarEntry queda marcado como no completado.
- Un DoseLog no debe existir sin un CalendarEntry asociado en este flujo.
- La asociacion se infiere de forma logica usando medicineId + scheduledTime.

## 6. Fallos y Casos Borde
- Si el usuario confirma tarde, el registro debe conservar la hora real de toma.
- Si hay cambios en el DoseSchedule, las entradas futuras deben regenerarse sin afectar logs existentes.
- Si el usuario elimina una medicina, los recordatorios futuros se cancelan y los logs historicos se conservan.
- Si la aplicacion no esta abierta, el recordatorio no puede completarse hasta que el usuario interactue.

## 7. Lo que No esta Decidido Aun
- Mecanismo exacto de activacion y entrega de recordatorios.
- Persistencia de estados pendientes y expirados.
- Politica exacta de ventana de tolerancia y expiracion.
