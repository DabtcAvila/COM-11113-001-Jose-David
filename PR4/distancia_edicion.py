#!/usr/bin/env python3
"""
Práctica 4: Distancia de Edición
Algoritmos Recursivos vs Programación Dinámica para calcular similitudes entre textos

Estudiante: David Fernando Avila Díaz - Clave Única: 197851
Instituto Tecnológico Autónomo de México (ITAM)
Estructuras de Datos y Algoritmos

Descripción:
Implementación y comparación de dos algoritmos para calcular la distancia de edición
entre dos cadenas de texto:
1. Algoritmo recursivo con memoización D(T1,T2)
2. Algoritmo de programación dinámica bottom-up D(T2,T1)

Parámetros de costo:
- Delta = 1 (costo de inserción/eliminación)
- Alfa = 2 (costo de sustitución)
"""

import time
import sys

# Parámetros globales
DELTA = 1  # Costo de inserción/eliminación
ALFA = 2   # Costo de sustitución

def distancia_edicion_recursiva(s1, s2, i=None, j=None, memo=None):
    """
    Implementación recursiva de la distancia de edición D(T1,T2)
    
    Args:
        s1: String 1
        s2: String 2
        i: Índice actual en s1
        j: Índice actual en s2
        memo: Diccionario para memoización (opcional)
    
    Returns:
        int: Distancia de edición mínima
    """
    if i is None:
        i = len(s1)
    if j is None:
        j = len(s2)
    if memo is None:
        memo = {}
    
    # Caso base: una cadena vacía
    if i == 0:
        return j * DELTA  # Insertar todos los caracteres de s2
    if j == 0:
        return i * DELTA  # Eliminar todos los caracteres de s1
    
    # Verificar si ya calculamos este subproblema
    if (i, j) in memo:
        return memo[(i, j)]
    
    # Si los caracteres son iguales, no hay costo
    if s1[i-1] == s2[j-1]:
        resultado = distancia_edicion_recursiva(s1, s2, i-1, j-1, memo)
    else:
        # Calcular el mínimo de las tres operaciones
        insercion = distancia_edicion_recursiva(s1, s2, i, j-1, memo) + DELTA
        eliminacion = distancia_edicion_recursiva(s1, s2, i-1, j, memo) + DELTA
        sustitucion = distancia_edicion_recursiva(s1, s2, i-1, j-1, memo) + ALFA
        
        resultado = min(insercion, eliminacion, sustitucion)
    
    memo[(i, j)] = resultado
    return resultado

def distancia_edicion_dinamica(s1, s2):
    """
    Implementación con programación dinámica D(T2,T1)
    
    Args:
        s1: String 1
        s2: String 2
    
    Returns:
        tuple: (distancia, matriz DP)
    """
    m = len(s1)
    n = len(s2)
    
    # Crear matriz DP
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # Inicializar casos base
    for i in range(m + 1):
        dp[i][0] = i * DELTA  # Eliminar todos los caracteres de s1
    for j in range(n + 1):
        dp[0][j] = j * DELTA  # Insertar todos los caracteres de s2
    
    # Llenar la matriz DP
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if s1[i-1] == s2[j-1]:
                dp[i][j] = dp[i-1][j-1]  # Sin costo si son iguales
            else:
                insercion = dp[i][j-1] + DELTA
                eliminacion = dp[i-1][j] + DELTA
                sustitucion = dp[i-1][j-1] + ALFA
                
                dp[i][j] = min(insercion, eliminacion, sustitucion)
    
    return dp[m][n], dp

def imprimir_matriz(matriz, s1, s2):
    """Imprime la matriz DP de forma legible"""
    print("\nMatriz de Programación Dinámica:")
    print("    ", end="")
    print("ε".rjust(3), end="")
    for char in s2:
        print(char.rjust(3), end="")
    print()
    
    for i in range(len(matriz)):
        if i == 0:
            print("ε".rjust(3), end=" ")
        else:
            print(s1[i-1].rjust(3), end=" ")
        
        for j in range(len(matriz[i])):
            print(str(matriz[i][j]).rjust(3), end="")
        print()

def reconstruir_operaciones(matriz, s1, s2):
    """Reconstruye la secuencia de operaciones óptima"""
    operaciones = []
    i, j = len(s1), len(s2)
    
    while i > 0 or j > 0:
        if i > 0 and j > 0 and s1[i-1] == s2[j-1]:
            # Caracteres iguales, no hay operación
            i -= 1
            j -= 1
        elif i > 0 and j > 0 and matriz[i][j] == matriz[i-1][j-1] + ALFA:
            # Sustitución
            operaciones.append(f"Sustituir '{s1[i-1]}' por '{s2[j-1]}' en posición {i}")
            i -= 1
            j -= 1
        elif i > 0 and matriz[i][j] == matriz[i-1][j] + DELTA:
            # Eliminación
            operaciones.append(f"Eliminar '{s1[i-1]}' en posición {i}")
            i -= 1
        elif j > 0 and matriz[i][j] == matriz[i][j-1] + DELTA:
            # Inserción
            operaciones.append(f"Insertar '{s2[j-1]}' en posición {i+1}")
            j -= 1
    
    return list(reversed(operaciones))

def leer_archivo(nombre_archivo):
    """Lee el contenido de un archivo"""
    try:
        with open(nombre_archivo, 'r', encoding='utf-8') as file:
            return file.read().strip()
    except FileNotFoundError:
        print(f"Error: No se pudo encontrar el archivo {nombre_archivo}")
        return None
    except Exception as e:
        print(f"Error al leer {nombre_archivo}: {e}")
        return None

def main():
    """Función principal"""
    print("="*60)
    print("PRÁCTICA 4: DISTANCIA DE EDICIÓN")
    print("="*60)
    print(f"Parámetros: Delta = {DELTA}, Alfa = {ALFA}")
    print()
    
    # Leer archivos
    t1 = leer_archivo("t1.txt")
    t2 = leer_archivo("t2.txt")
    
    if t1 is None or t2 is None:
        return
    
    print(f"T1 (t1.txt): '{t1}'")
    print(f"T2 (t2.txt): '{t2}'")
    print()
    
    # Algoritmo recursivo D(T1,T2)
    print("1. ALGORITMO RECURSIVO D(T1,T2)")
    print("-" * 40)
    start_time = time.time()
    distancia_rec = distancia_edicion_recursiva(t1, t2)
    tiempo_rec = time.time() - start_time
    print(f"Distancia de edición D(T1,T2): {distancia_rec}")
    print(f"Tiempo de ejecución: {tiempo_rec:.6f} segundos")
    print()
    
    # Programación dinámica D(T2,T1)
    print("2. PROGRAMACIÓN DINÁMICA D(T2,T1)")
    print("-" * 40)
    start_time = time.time()
    distancia_dp, matriz = distancia_edicion_dinamica(t2, t1)
    tiempo_dp = time.time() - start_time
    print(f"Distancia de edición D(T2,T1): {distancia_dp}")
    print(f"Tiempo de ejecución: {tiempo_dp:.6f} segundos")
    
    # Mostrar matriz
    imprimir_matriz(matriz, t2, t1)
    
    # Reconstruir operaciones
    print("\n3. SECUENCIA DE OPERACIONES ÓPTIMA")
    print("-" * 40)
    operaciones = reconstruir_operaciones(matriz, t2, t1)
    if operaciones:
        for i, op in enumerate(operaciones, 1):
            print(f"{i}. {op}")
    else:
        print("No se requieren operaciones (cadenas idénticas)")
    
    # Comparación de algoritmos
    print("\n4. COMPARACIÓN DE ALGORITMOS")
    print("-" * 40)
    print(f"Recursivo D(T1,T2):        {distancia_rec} ({tiempo_rec:.6f}s)")
    print(f"Prog. Dinámica D(T2,T1):   {distancia_dp} ({tiempo_dp:.6f}s)")
    print(f"Mejora de rendimiento:     {tiempo_rec/tiempo_dp:.2f}x más rápido")
    
    # Verificar simetría
    print("\n5. VERIFICACIÓN DE SIMETRÍA")
    print("-" * 40)
    distancia_simetrica, _ = distancia_edicion_dinamica(t1, t2)
    print(f"D(T1,T2) = {distancia_simetrica}")
    print(f"D(T2,T1) = {distancia_dp}")
    if distancia_simetrica == distancia_dp:
        print("✓ La distancia de edición es simétrica")
    else:
        print("✗ Error: La distancia no es simétrica")

if __name__ == "__main__":
    main()