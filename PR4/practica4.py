#!/usr/bin/env python3
"""
Práctica 4: Programación Dinámica
Cálculo de Distancia de Edición entre Documentos

Estudiante: David Fernando Avila Díaz - Clave Única: 197851
Estudiante: José Gerardo Malfavaun Gorostizaga - Clave Única: 213398
Instituto Tecnológico Autónomo de México (ITAM)
Departamento Académico de Computación
Prof. Marco Morales Aguirre

Descripción:
Programa que calcula la distancia de edición entre dos documentos utilizando
tres implementaciones diferentes:
1. Versión recursiva sin memoización
2. Versión recursiva con memoización  
3. Versión iterativa con programación dinámica

Costos:
- Insertar un carácter: 1
- Reemplazar un carácter: 2
- Eliminar un carácter: 1

Especificaciones:
- Archivos de hasta 50,000 caracteres
- Timeout de 10 segundos por cálculo
- Formato UTF-8
"""

import sys
import time
import signal
import os

# Constantes de costo
COSTO_INSERCION = 1
COSTO_ELIMINACION = 1  
COSTO_REEMPLAZO = 2

class TimeoutException(Exception):
    """Excepción para manejar timeouts."""
    pass

def timeout_handler(signum, frame):
    """Manejador de timeout."""
    raise TimeoutException("Tiempo límite excedido")

def distancia_edicion_recursiva_sin_memo(s1, s2, i, j):
    """
    Implementación recursiva SIN memoización.
    
    Args:
        s1: Primera cadena
        s2: Segunda cadena
        i: Índice actual en s1
        j: Índice actual en s2
    
    Returns:
        int: Distancia de edición mínima
    """
    # Casos base
    if i == 0:
        return j * COSTO_INSERCION
    if j == 0:
        return i * COSTO_ELIMINACION
    
    # Si los caracteres son iguales, no hay costo
    if s1[i-1] == s2[j-1]:
        return distancia_edicion_recursiva_sin_memo(s1, s2, i-1, j-1)
    
    # Calcular el mínimo de las tres operaciones
    insercion = distancia_edicion_recursiva_sin_memo(s1, s2, i, j-1) + COSTO_INSERCION
    eliminacion = distancia_edicion_recursiva_sin_memo(s1, s2, i-1, j) + COSTO_ELIMINACION
    reemplazo = distancia_edicion_recursiva_sin_memo(s1, s2, i-1, j-1) + COSTO_REEMPLAZO
    
    return min(insercion, eliminacion, reemplazo)

def distancia_edicion_recursiva_con_memo(s1, s2, i, j, memo):
    """
    Implementación recursiva CON memoización.
    
    Args:
        s1: Primera cadena
        s2: Segunda cadena
        i: Índice actual en s1
        j: Índice actual en s2
        memo: Diccionario de memoización
    
    Returns:
        int: Distancia de edición mínima
    """
    # Verificar si ya calculamos este subproblema
    if (i, j) in memo:
        return memo[(i, j)]
    
    # Casos base
    if i == 0:
        resultado = j * COSTO_INSERCION
    elif j == 0:
        resultado = i * COSTO_ELIMINACION
    elif s1[i-1] == s2[j-1]:
        # Si los caracteres son iguales, no hay costo
        resultado = distancia_edicion_recursiva_con_memo(s1, s2, i-1, j-1, memo)
    else:
        # Calcular el mínimo de las tres operaciones
        insercion = distancia_edicion_recursiva_con_memo(s1, s2, i, j-1, memo) + COSTO_INSERCION
        eliminacion = distancia_edicion_recursiva_con_memo(s1, s2, i-1, j, memo) + COSTO_ELIMINACION
        reemplazo = distancia_edicion_recursiva_con_memo(s1, s2, i-1, j-1, memo) + COSTO_REEMPLAZO
        
        resultado = min(insercion, eliminacion, reemplazo)
    
    memo[(i, j)] = resultado
    return resultado

def distancia_edicion_iterativa(s1, s2):
    """
    Implementación iterativa con programación dinámica.
    
    Args:
        s1: Primera cadena
        s2: Segunda cadena
    
    Returns:
        int: Distancia de edición mínima
    """
    m = len(s1)
    n = len(s2)
    
    # Crear matriz DP
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # Inicializar casos base
    for i in range(m + 1):
        dp[i][0] = i * COSTO_ELIMINACION
    for j in range(n + 1):
        dp[0][j] = j * COSTO_INSERCION
    
    # Llenar la matriz DP
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if s1[i-1] == s2[j-1]:
                dp[i][j] = dp[i-1][j-1]  # Sin costo si son iguales
            else:
                insercion = dp[i][j-1] + COSTO_INSERCION
                eliminacion = dp[i-1][j] + COSTO_ELIMINACION
                reemplazo = dp[i-1][j-1] + COSTO_REEMPLAZO
                
                dp[i][j] = min(insercion, eliminacion, reemplazo)
    
    return dp[m][n]

def leer_archivo(nombre_archivo):
    """
    Lee el contenido de un archivo UTF-8.
    
    Args:
        nombre_archivo: Nombre del archivo a leer
    
    Returns:
        str: Contenido del archivo o None si hay error
    """
    try:
        with open(nombre_archivo, 'r', encoding='utf-8') as file:
            contenido = file.read()
            return contenido
    except FileNotFoundError:
        print(f"Error: No se pudo encontrar el archivo {nombre_archivo}")
        return None
    except Exception as e:
        print(f"Error al leer {nombre_archivo}: {e}")
        return None

def calcular_con_timeout(func, *args, timeout_seconds=10):
    """
    Ejecuta una función con timeout.
    
    Args:
        func: Función a ejecutar
        *args: Argumentos de la función
        timeout_seconds: Tiempo límite en segundos
    
    Returns:
        tuple: (resultado, tiempo_ejecucion, timeout_excedido)
    """
    # Configurar el timeout
    signal.signal(signal.SIGALRM, timeout_handler)
    signal.alarm(timeout_seconds)
    
    try:
        start_time = time.perf_counter()
        resultado = func(*args)
        end_time = time.perf_counter()
        tiempo_ejecucion = end_time - start_time
        
        signal.alarm(0)  # Cancelar timeout
        return resultado, tiempo_ejecucion, False
        
    except TimeoutException:
        signal.alarm(0)  # Cancelar timeout
        return None, timeout_seconds, True
    except Exception as e:
        signal.alarm(0)  # Cancelar timeout
        print(f"Error durante el cálculo: {e}")
        return None, 0, False

def main():
    """Función principal del programa."""
    if len(sys.argv) != 3:
        print("Uso: python3 practica4.py <archivo_A> <archivo_B>")
        sys.exit(1)
    
    archivo_a = sys.argv[1]
    archivo_b = sys.argv[2]
    
    # Leer archivos
    texto_a = leer_archivo(archivo_a)
    texto_b = leer_archivo(archivo_b)
    
    if texto_a is None or texto_b is None:
        sys.exit(1)
    
    # Obtener longitudes
    len_a = len(texto_a)
    len_b = len(texto_b)
    
    # Verificar límites de tamaño
    if len_a > 50000 or len_b > 50000:
        print("Error: Los archivos exceden el límite de 50,000 caracteres")
        sys.exit(1)
    
    print(f"Documento A: {len_a} caracteres")
    print(f"Documento B: {len_b} caracteres")
    
    # Calcular D(A, B) usando programación dinámica (más eficiente)
    print("\nCalculando D(A, B)...")
    resultado_ab, tiempo_ab, timeout_ab = calcular_con_timeout(
        distancia_edicion_iterativa, texto_a, texto_b
    )
    
    if timeout_ab:
        print("D(A, B): tiempo límite para resolver el problema excedido")
    else:
        print(f"D(A, B): {resultado_ab}")
        print(f"Tiempo: {tiempo_ab:.6f} segundos")
    
    # Calcular D(B, A) usando programación dinámica
    print("\nCalculando D(B, A)...")
    resultado_ba, tiempo_ba, timeout_ba = calcular_con_timeout(
        distancia_edicion_iterativa, texto_b, texto_a
    )
    
    if timeout_ba:
        print("D(B, A): tiempo límite para resolver el problema excedido")
    else:
        print(f"D(B, A): {resultado_ba}")
        print(f"Tiempo: {tiempo_ba:.6f} segundos")
    
    # Verificar simetría
    if not timeout_ab and not timeout_ba:
        if resultado_ab == resultado_ba:
            print(f"\n✓ Verificación: D(A,B) = D(B,A) = {resultado_ab}")
        else:
            print(f"\n✗ Error: D(A,B) = {resultado_ab} ≠ D(B,A) = {resultado_ba}")

if __name__ == "__main__":
    main()