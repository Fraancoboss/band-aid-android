# API Testing Guide (CIMA AEMPS)

## Objetivo
Script local para probar endpoints de CIMA sin integrar el SDK en la app. Este flujo es de prueba manual y no persiste datos.

## Requisitos
- Python 3.10+ instalado
- Conectividad a Internet

## Crear entorno virtual (Windows)
1. Desde la carpeta del proyecto, ejecuta:
   - `python -m venv outdoor_testing\api_testing\.venv`
2. Activa el entorno:
   - `outdoor_testing\api_testing\.venv\Scripts\activate`

## Instalar dependencias
- `pip install requests`

## Ejecutar el script
- `python outdoor_testing\api_testing\api_testing.py`

## Uso
El script muestra un menu numerico y permite seleccionar:
1. Listar medicamentos (busqueda)
2. Detalle por nregistro
3. Detalle por cn
4. Listar secciones de documento segmentado
5. Obtener contenido de documento segmentado
6. Buscar texto en ficha tecnica

## Notas
- Este script es un stub de pruebas y no forma parte de la app.
- No se guarda ni se cachea contenido: todo es temporal en memoria.
