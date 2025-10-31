#!/usr/bin/env python3
"""
Generador de Documentos de Prueba para Práctica 4

Estudiante: David Fernando Avila Díaz - Clave Única: 197851
Estudiante: José Gerardo Malfavaun Gorostizaga - Clave Única: 213398
Instituto Tecnológico Autónomo de México (ITAM)
Departamento Académico de Computación
Prof. Marco Morales Aguirre

Descripción:
Genera documentos de prueba de diferentes tamaños para evaluar
el desempeño de los algoritmos de distancia de edición.
"""

import random
import string
import os

def generar_texto_realista(longitud, tema="tecnologia"):
    """
    Genera un texto realista de longitud específica.
    
    Args:
        longitud: Número de caracteres deseado
        tema: Tema del texto
    
    Returns:
        str: Texto generado
    """
    
    # Vocabularios por tema
    vocabularios = {
        "tecnologia": [
            "computadora", "algoritmo", "programacion", "software", "hardware",
            "internet", "datos", "informacion", "sistema", "aplicacion",
            "codigo", "desarrollo", "tecnologia", "digital", "virtual",
            "red", "servidor", "base", "seguridad", "usuario"
        ],
        "ciencia": [
            "investigacion", "experimento", "hipotesis", "teoria", "metodo",
            "analisis", "resultado", "conclusion", "cientifico", "laboratorio",
            "medicion", "observacion", "variable", "control", "muestra",
            "estadistica", "probabilidad", "evidencia", "validacion", "replica"
        ],
        "educacion": [
            "estudiante", "profesor", "clase", "leccion", "tarea",
            "examen", "calificacion", "aprendizaje", "conocimiento", "habilidad",
            "materia", "curso", "semestre", "universidad", "colegio",
            "biblioteca", "libro", "investigacion", "proyecto", "practica"
        ]
    }
    
    palabras = vocabularios.get(tema, vocabularios["tecnologia"])
    
    # Conectores y palabras comunes
    conectores = [
        "el", "la", "los", "las", "un", "una", "de", "del", "en", "con",
        "por", "para", "que", "se", "es", "son", "y", "o", "pero", "sin",
        "sobre", "entre", "durante", "mediante", "a", "al", "desde", "hasta"
    ]
    
    # Generar texto
    texto = ""
    palabras_totales = palabras + conectores
    
    while len(texto) < longitud:
        # Elegir una palabra aleatoria
        palabra = random.choice(palabras_totales)
        
        # Añadir la palabra
        if len(texto) == 0:
            texto = palabra.capitalize()
        else:
            texto += " " + palabra
        
        # Ocasionalmente añadir puntuación
        if random.random() < 0.1:  # 10% de probabilidad
            if random.random() < 0.7:
                texto += ","
            elif random.random() < 0.8:
                texto += "."
            else:
                texto += ";"
        
        # Ocasionalmente crear nuevo párrafo
        if random.random() < 0.05 and len(texto) > 100:  # 5% de probabilidad
            texto += ".\n\n"
            # Próxima palabra será capitalizada
    
    # Ajustar a la longitud exacta
    if len(texto) > longitud:
        texto = texto[:longitud]
    elif len(texto) < longitud:
        # Rellenar con espacios y puntos
        while len(texto) < longitud:
            texto += " ."
    
    return texto

def generar_variante(texto_base, porcentaje_cambio=0.1):
    """
    Genera una variante de un texto cambiando un porcentaje de caracteres.
    
    Args:
        texto_base: Texto original
        porcentaje_cambio: Porcentaje de caracteres a cambiar (0.0 a 1.0)
    
    Returns:
        str: Texto modificado
    """
    texto_lista = list(texto_base)
    longitud = len(texto_lista)
    num_cambios = int(longitud * porcentaje_cambio)
    
    # Caracteres posibles para reemplazo
    caracteres = string.ascii_letters + string.digits + ' .,;:!?-'
    
    # Realizar cambios aleatorios
    posiciones_cambio = random.sample(range(longitud), min(num_cambios, longitud))
    
    for pos in sorted(posiciones_cambio, reverse=True):  # Procesar de atrás hacia adelante
        # Tipo de cambio aleatorio
        tipo_cambio = random.choice(['sustituir', 'insertar', 'eliminar'])
        
        if pos < len(texto_lista):  # Verificar que la posición sea válida
            if tipo_cambio == 'sustituir':
                texto_lista[pos] = random.choice(caracteres)
            elif tipo_cambio == 'insertar' and len(texto_lista) < 50000:
                texto_lista.insert(pos, random.choice(caracteres))
            elif tipo_cambio == 'eliminar' and len(texto_lista) > 1:
                del texto_lista[pos]
    
    return ''.join(texto_lista)

def crear_casos_prueba():
    """Crea casos de prueba para diferentes longitudes."""
    print("Generando documentos de prueba...")
    
    # Crear directorio
    os.makedirs("documentos_prueba", exist_ok=True)
    
    # Longitudes a generar
    longitudes = [5, 50, 500, 5000, 50000]
    
    for longitud in longitudes:
        print(f"Generando documentos de {longitud} caracteres...")
        
        # Documento A (original)
        texto_a = generar_texto_realista(longitud, "tecnologia")
        nombre_a = f"documentos_prueba/doc_A_{longitud}.txt"
        
        with open(nombre_a, 'w', encoding='utf-8') as f:
            f.write(texto_a)
        
        # Documento B (variante con 10% de cambios)
        texto_b = generar_variante(texto_a, 0.1)
        nombre_b = f"documentos_prueba/doc_B_{longitud}.txt"
        
        with open(nombre_b, 'w', encoding='utf-8') as f:
            f.write(texto_b)
        
        # Documento C (más diferente, 30% de cambios)
        texto_c = generar_variante(texto_a, 0.3)
        nombre_c = f"documentos_prueba/doc_C_{longitud}.txt"
        
        with open(nombre_c, 'w', encoding='utf-8') as f:
            f.write(texto_c)
        
        print(f"  ✓ Generados: doc_A_{longitud}.txt, doc_B_{longitud}.txt, doc_C_{longitud}.txt")
    
    # Casos especiales
    print("\nGenerando casos especiales...")
    
    # Documentos idénticos
    texto_identico = generar_texto_realista(1000, "educacion")
    with open("documentos_prueba/identico_A.txt", 'w', encoding='utf-8') as f:
        f.write(texto_identico)
    with open("documentos_prueba/identico_B.txt", 'w', encoding='utf-8') as f:
        f.write(texto_identico)
    
    # Documentos muy diferentes
    texto_muy_dif_a = generar_texto_realista(1000, "tecnologia")
    texto_muy_dif_b = generar_texto_realista(1000, "ciencia")
    with open("documentos_prueba/muy_diferente_A.txt", 'w', encoding='utf-8') as f:
        f.write(texto_muy_dif_a)
    with open("documentos_prueba/muy_diferente_B.txt", 'w', encoding='utf-8') as f:
        f.write(texto_muy_dif_b)
    
    # Documento vacío
    with open("documentos_prueba/vacio.txt", 'w', encoding='utf-8') as f:
        f.write("")
    
    print("  ✓ Casos especiales generados")
    
    # Crear script de pruebas
    crear_script_pruebas()
    
    print("\n✓ Todos los documentos generados en: documentos_prueba/")
    print("✓ Para probar: python3 documentos_prueba/ejecutar_pruebas.py")

def crear_script_pruebas():
    """Crea un script para ejecutar todas las pruebas."""
    
    script_content = '''#!/usr/bin/env python3
"""
Script para ejecutar todas las pruebas de documentos.
"""

import os
import sys
import glob
import subprocess

def ejecutar_prueba(archivo_a, archivo_b):
    """Ejecuta una prueba específica."""
    print(f"\\nProbando: {archivo_a} vs {archivo_b}")
    print("-" * 60)
    
    try:
        # Ejecutar el programa principal
        resultado = subprocess.run([
            sys.executable, "../practica4.py", archivo_a, archivo_b
        ], capture_output=True, text=True, timeout=15)
        
        if resultado.returncode == 0:
            print(resultado.stdout)
        else:
            print(f"Error: {resultado.stderr}")
            
    except subprocess.TimeoutExpired:
        print("TIMEOUT: La prueba excedió 15 segundos")
    except Exception as e:
        print(f"Error ejecutando prueba: {e}")

def main():
    """Ejecuta todas las pruebas."""
    os.chdir(os.path.dirname(os.path.abspath(__file__)))
    
    print("EJECUTANDO PRUEBAS DE DOCUMENTOS")
    print("="*60)
    
    # Pruebas por longitud
    longitudes = [5, 50, 500, 5000, 50000]
    
    for longitud in longitudes:
        archivo_a = f"doc_A_{longitud}.txt"
        archivo_b = f"doc_B_{longitud}.txt"
        
        if os.path.exists(archivo_a) and os.path.exists(archivo_b):
            ejecutar_prueba(archivo_a, archivo_b)
    
    # Pruebas especiales
    pruebas_especiales = [
        ("identico_A.txt", "identico_B.txt"),
        ("muy_diferente_A.txt", "muy_diferente_B.txt"),
        ("doc_A_500.txt", "vacio.txt"),
    ]
    
    print("\\n" + "="*60)
    print("PRUEBAS ESPECIALES")
    print("="*60)
    
    for archivo_a, archivo_b in pruebas_especiales:
        if os.path.exists(archivo_a) and os.path.exists(archivo_b):
            ejecutar_prueba(archivo_a, archivo_b)

if __name__ == "__main__":
    main()
'''
    
    with open("documentos_prueba/ejecutar_pruebas.py", 'w', encoding='utf-8') as f:
        f.write(script_content)
    
    # Hacer ejecutable
    os.chmod("documentos_prueba/ejecutar_pruebas.py", 0o755)

def main():
    """Función principal."""
    random.seed(42)  # Para reproducibilidad
    crear_casos_prueba()

if __name__ == "__main__":
    main()