# Diseno de Seguridad y Privacidad

## 1. Clasificacion de Datos
- Datos de salud personales: medicaciones, planes de dosis, registros de toma.
- Datos sensibles de contexto: notas subjetivas diarias.
- Metadatos operativos: fechas, horas y estados de cumplimiento.

## 2. Principios de Privacidad por Diseno
- Todo el procesamiento es local al dispositivo.
- No existe comunicacion de datos hacia servicios externos.
- Las funciones se diseñan para no requerir identidad ni cuentas.
- La aplicacion evita recolectar datos no necesarios.

## 3. Estrategia de Minimizacion de Datos
- Se registran solo los campos necesarios para recordatorios y seguimiento.
- Las notas subjetivas son opcionales.
- No se almacenan imagenes crudas de escaneos por defecto.

## 4. Consideraciones de Almacenamiento Local y Acceso
- Los datos residen en el dispositivo del usuario.
- No hay sincronizacion ni copia automatica fuera del dispositivo.
- El acceso esta limitado al propio usuario del dispositivo.

## 5. Control y Transparencia para el Usuario
- El usuario crea, edita y elimina sus medicaciones y registros.
- Las recomendaciones son explicables y no modifican datos.
- Se explicita que el sistema no reemplaza indicaciones medicas.

## 6. Escenarios de Amenaza y Mitigaciones
- Acceso fisico no autorizado al dispositivo: mitigado por controles del sistema operativo.
- Pérdida o daño del dispositivo: no hay recuperacion automatica.
- Errores de entrada de datos: mitigados por confirmacion manual.

## 7. No Objetivos Explicitos
- No se ofrece autenticacion interna.
- No se realizan respaldos en la nube.
- No se declara cumplimiento normativo especifico.
