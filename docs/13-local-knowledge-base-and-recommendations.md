# Base de Conocimiento Local y Recomendaciones

## 1. Proposito de la Base de Conocimiento
- Derivar observaciones y recomendaciones simples a partir de datos ya registrados.
- Ayudar al usuario a identificar patrones de cumplimiento y habitos.
- Mantener un sistema local, determinista y explicable.

## 2. Alcance y Limitaciones
- Solo utiliza datos existentes del dominio: planes, entradas esperadas, registros reales y notas subjetivas.
- No modifica datos del usuario ni toma decisiones por el usuario.
- No se presenta como consejo medico ni reemplaza criterio profesional.

## 3. Senales de Entrada y Fuentes de Contexto
- DoseSchedule: horarios y frecuencia planificados.
- CalendarEntry: dosis esperadas por fecha y hora.
- DoseLog: tomas reales, incluidas omisiones registradas.
- Notas subjetivas diarias.
- Patrones temporales deterministas: puntualidad, retrasos, omisiones, consistencia.

## 4. Reglas y Heuristicas de Conocimiento
- Deteccion de retrasos: si la toma se registra fuera de la ventana prevista, se marca como tardia.
- Deteccion de omisiones: si una entrada expira sin registro, se marca como omitida.
- Consistencia semanal: si se cumplen todas las dosis planificadas en una semana, se considera consistente.
- Patrones de dias especificos: si hay omisiones recurrentes en un dia de la semana, se registra como patron.
- Relacion con notas: si existe nota subjetiva negativa en un dia con omisiones, se registra una posible correlacion descriptiva.

## 5. Flujo de Generacion de Recomendaciones
- Recolectar datos recientes dentro de un rango temporal definido.
- Evaluar reglas deterministas sobre CalendarEntry y DoseLog.
- Generar una lista corta de observaciones y sugerencias accionables.
- Priorizar recomendaciones basadas en frecuencia y repeticion de patrones.

## 6. Explicabilidad y Transparencia para el Usuario
- Cada recomendacion debe incluir la regla aplicada y el periodo observado.
- El usuario puede revisar las entradas que originaron la recomendacion.
- El lenguaje debe ser descriptivo y no prescriptivo.

## 7. Integridad de Datos y Privacidad
- Todo el procesamiento ocurre en el dispositivo.
- Ningun dato sale del dispositivo ni se comparte con terceros.
- Las recomendaciones no modifican ni sobrescriben registros existentes.

## 8. Modos de Falla y Escenarios sin Accion
- Si no hay datos suficientes, no se generan recomendaciones.
- Si los registros son incompletos, se muestran solo observaciones basadas en datos verificables.
- Si el usuario desactiva el registro de notas, las recomendaciones no usan ese contexto.

## 9. Fuera de Alcance
- Integraciones con servicios externos o sincronizacion en la nube.
- Modelos predictivos o probabilisticos.
- Diagnosticos, evaluaciones clinicas o sugerencias medicas.
