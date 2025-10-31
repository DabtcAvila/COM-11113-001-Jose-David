#!/usr/bin/env python3
"""
Generador de Casos de Prueba para Distancia de Edición

Estudiante: David Fernando Avila Díaz - Clave Única: 197851
Instituto Tecnológico Autónomo de México (ITAM)
Estructuras de Datos y Algoritmos

Descripción:
Genera casos de prueba diversos para evaluar los algoritmos de distancia de edición,
incluyendo casos extremos, casos aleatorios y casos con patrones específicos.
"""

import os
import random
import string

def crear_directorio_datos():
    """Crea el directorio de datos de prueba si no existe."""
    os.makedirs("datos_prueba", exist_ok=True)

def generar_caso(nombre, contenido1, contenido2, descripcion=""):
    """
    Genera un caso de prueba específico.
    
    Args:
        nombre: Nombre base del caso (ej: "caso1")
        contenido1: Contenido del primer archivo
        contenido2: Contenido del segundo archivo
        descripcion: Descripción del caso
    """
    try:
        with open(f"datos_prueba/{nombre}_a.txt", "w", encoding="utf-8") as f:
            f.write(contenido1)
        
        with open(f"datos_prueba/{nombre}_b.txt", "w", encoding="utf-8") as f:
            f.write(contenido2)
        
        # Crear archivo de metadatos
        with open(f"datos_prueba/{nombre}_info.txt", "w", encoding="utf-8") as f:
            f.write(f"Caso: {nombre}\n")
            f.write(f"Descripción: {descripcion}\n")
            f.write(f"Cadena A: '{contenido1}'\n")
            f.write(f"Cadena B: '{contenido2}'\n")
            f.write(f"Longitud A: {len(contenido1)}\n")
            f.write(f"Longitud B: {len(contenido2)}\n")
        
        print(f"✓ Generado: {nombre} - {descripcion}")
        
    except Exception as e:
        print(f"✗ Error generando {nombre}: {e}")

def generar_cadena_aleatoria(longitud, alfabeto=None):
    """Genera una cadena aleatoria."""
    if alfabeto is None:
        alfabeto = string.ascii_uppercase
    return ''.join(random.choice(alfabeto) for _ in range(longitud))

def generar_casos_basicos():
    """Genera casos básicos y fundamentales."""
    print("Generando casos básicos...")
    
    # Caso original de la práctica
    generar_caso("original", "ALGORITMO", "ALTRUISMO", 
                "Caso original de la práctica")
    
    # Casos de un solo carácter
    generar_caso("char_simple", "A", "B", 
                "Sustitución de un carácter")
    
    # Cadenas idénticas
    generar_caso("identicas", "ABCDE", "ABCDE", 
                "Cadenas idénticas (distancia = 0)")
    
    # Cadena vacía vs texto
    generar_caso("vacia_texto", "", "HOLA", 
                "Cadena vacía vs texto (solo inserciones)")
    
    # Texto vs cadena vacía
    generar_caso("texto_vacia", "MUNDO", "", 
                "Texto vs cadena vacía (solo eliminaciones)")
    
    # Ambas vacías
    generar_caso("ambas_vacias", "", "", 
                "Ambas cadenas vacías")

def generar_casos_extremos():
    """Genera casos extremos y edge cases."""
    print("Generando casos extremos...")
    
    # Cadenas muy diferentes
    generar_caso("muy_diferentes", "AAAA", "BBBB", 
                "Cadenas completamente diferentes")
    
    # Una inserción
    generar_caso("una_insercion", "ABC", "ABCD", 
                "Una inserción al final")
    
    # Una eliminación
    generar_caso("una_eliminacion", "ABCD", "ABC", 
                "Una eliminación al final")
    
    # Una sustitución
    generar_caso("una_sustitucion", "ABC", "ABD", 
                "Una sustitución al final")
    
    # Cadenas inversas
    generar_caso("inversas", "ABCDE", "EDCBA", 
                "Cadenas inversas")
    
    # Repeticiones
    generar_caso("repeticiones", "AAA", "BBB", 
                "Caracteres repetidos")
    
    # Subcadena
    generar_caso("subcadena", "ABCDE", "ACE", 
                "Una es subcadena de la otra")

def generar_casos_aleatorios():
    """Genera casos aleatorios para testing extensivo."""
    print("Generando casos aleatorios...")
    
    random.seed(42)  # Para reproducibilidad
    
    longitudes = [3, 5, 8, 10, 15]
    
    for i, longitud in enumerate(longitudes, 1):
        # Casos aleatorios normales
        s1 = generar_cadena_aleatoria(longitud)
        s2 = generar_cadena_aleatoria(longitud)
        generar_caso(f"aleatorio_{i}", s1, s2, 
                    f"Caso aleatorio longitud {longitud}")
        
        # Casos con alfabeto reducido
        alfabeto_pequeno = "ABC"
        s1_pequeno = generar_cadena_aleatoria(longitud, alfabeto_pequeno)
        s2_pequeno = generar_cadena_aleatoria(longitud, alfabeto_pequeno)
        generar_caso(f"alfabeto_pequeno_{i}", s1_pequeno, s2_pequeno, 
                    f"Alfabeto reducido, longitud {longitud}")

def generar_casos_similitud():
    """Genera casos con diferentes niveles de similitud."""
    print("Generando casos por similitud...")
    
    base = "ABCDEFGHIJ"
    similitudes = [0.0, 0.3, 0.5, 0.7, 0.9]
    
    for i, similitud in enumerate(similitudes, 1):
        longitud = len(base)
        caracteres_iguales = int(longitud * similitud)
        
        # Crear cadena con similitud específica
        s2_chars = list(base[:caracteres_iguales])
        caracteres_diferentes = longitud - caracteres_iguales
        s2_chars.extend(generar_cadena_aleatoria(caracteres_diferentes))
        random.shuffle(s2_chars)
        s2 = ''.join(s2_chars)
        
        generar_caso(f"similitud_{int(similitud*100)}", base, s2, 
                    f"Similitud aproximada {similitud:.0%}")

def generar_casos_palabras():
    """Genera casos con palabras reales en español."""
    print("Generando casos con palabras...")
    
    palabras = [
        ("GATO", "PATO", "Palabras similares"),
        ("CASA", "MASA", "Una diferencia"),
        ("PERRO", "GATO", "Animales diferentes"),
        ("AMOR", "ROMA", "Anagramas"),
        ("COMPUTADORA", "ORDENADOR", "Sinónimos largos"),
        ("RATON", "RATONES", "Singular vs plural"),
        ("FELIZ", "FELIX", "Nombres similares"),
        ("MADRID", "PARIS", "Ciudades"),
        ("LUNES", "MARTES", "Días de la semana"),
        ("ROJO", "AZUL", "Colores")
    ]
    
    for i, (palabra1, palabra2, desc) in enumerate(palabras, 1):
        generar_caso(f"palabra_{i}", palabra1, palabra2, desc)

def crear_archivo_resumen():
    """Crea un archivo resumen de todos los casos generados."""
    try:
        archivos = [f for f in os.listdir("datos_prueba") if f.endswith("_info.txt")]
        archivos.sort()
        
        with open("datos_prueba/RESUMEN_CASOS.txt", "w", encoding="utf-8") as f:
            f.write("RESUMEN DE CASOS DE PRUEBA\n")
            f.write("="*50 + "\n\n")
            f.write(f"Total de casos generados: {len(archivos)}\n\n")
            
            for archivo in archivos:
                with open(f"datos_prueba/{archivo}", "r", encoding="utf-8") as info:
                    contenido = info.read()
                    f.write(contenido + "\n" + "-"*30 + "\n")
        
        print(f"✓ Resumen creado: datos_prueba/RESUMEN_CASOS.txt")
        
    except Exception as e:
        print(f"✗ Error creando resumen: {e}")

def crear_script_testing():
    """Crea un script para ejecutar todos los casos de prueba."""
    script_content = '''#!/usr/bin/env python3
"""
Script automático para ejecutar todos los casos de prueba.
"""

import os
import sys
import glob
sys.path.append('..')
from distancia_edicion import distancia_edicion_recursiva, distancia_edicion_dinamica

def ejecutar_caso(archivo_a, archivo_b):
    """Ejecuta un caso de prueba específico."""
    try:
        with open(archivo_a, 'r', encoding='utf-8') as f:
            s1 = f.read().strip()
        with open(archivo_b, 'r', encoding='utf-8') as f:
            s2 = f.read().strip()
        
        # Calcular distancias
        dist_rec = distancia_edicion_recursiva(s1, s2)
        dist_dp, _ = distancia_edicion_dinamica(s1, s2)
        
        return s1, s2, dist_rec, dist_dp
        
    except Exception as e:
        print(f"Error en {archivo_a}: {e}")
        return None, None, None, None

def main():
    """Ejecuta todos los casos de prueba."""
    os.chdir('datos_prueba')
    
    archivos_a = sorted(glob.glob("*_a.txt"))
    
    print("Ejecutando todos los casos de prueba...")
    print("="*60)
    print(f"{'Caso':<20} {'S1':<10} {'S2':<10} {'Rec':<5} {'DP':<5} {'OK':<5}")
    print("-"*60)
    
    errores = 0
    for archivo_a in archivos_a:
        caso = archivo_a.replace("_a.txt", "")
        archivo_b = archivo_a.replace("_a.txt", "_b.txt")
        
        s1, s2, dist_rec, dist_dp = ejecutar_caso(archivo_a, archivo_b)
        
        if s1 is not None:
            ok = "✓" if dist_rec == dist_dp else "✗"
            if dist_rec != dist_dp:
                errores += 1
            
            print(f"{caso:<20} {s1[:8]:<10} {s2[:8]:<10} {dist_rec:<5} {dist_dp:<5} {ok:<5}")
    
    print("-"*60)
    print(f"Total casos: {len(archivos_a)}")
    print(f"Errores: {errores}")
    
    if errores == 0:
        print("✓ Todos los casos pasaron correctamente")
    else:
        print(f"✗ {errores} casos fallaron")

if __name__ == "__main__":
    main()
'''
    
    try:
        with open("datos_prueba/ejecutar_casos.py", "w", encoding="utf-8") as f:
            f.write(script_content)
        
        # Hacer ejecutable en sistemas Unix
        os.chmod("datos_prueba/ejecutar_casos.py", 0o755)
        print("✓ Script de testing creado: datos_prueba/ejecutar_casos.py")
        
    except Exception as e:
        print(f"✗ Error creando script: {e}")

def main():
    """Función principal del generador."""
    print("GENERADOR DE CASOS DE PRUEBA - DISTANCIA DE EDICIÓN")
    print("="*55)
    print()
    
    # Crear directorio
    crear_directorio_datos()
    
    # Generar todos los tipos de casos
    generar_casos_basicos()
    generar_casos_extremos()
    generar_casos_aleatorios()
    generar_casos_similitud()
    generar_casos_palabras()
    
    # Crear archivos de soporte
    crear_archivo_resumen()
    crear_script_testing()
    
    print()
    print("✓ Generación de casos completada")
    print(f"✓ Casos guardados en: datos_prueba/")
    print("✓ Para ejecutar todos los casos: python3 datos_prueba/ejecutar_casos.py")

if __name__ == "__main__":
    main()