#!/usr/bin/env python3
"""
Sistema de Benchmarking para Algoritmos de Distancia de Edición

Estudiante: David Fernando Avila Díaz - Clave Única: 197851
Instituto Tecnológico Autónomo de México (ITAM)
Estructuras de Datos y Algoritmos

Descripción:
Sistema automatizado para evaluar el rendimiento de los algoritmos recursivo
y de programación dinámica en diferentes casos de prueba y tamaños de entrada.
"""

import time
import random
import string
import os
import sys
from distancia_edicion import distancia_edicion_recursiva, distancia_edicion_dinamica

# Configuración de benchmarks
DELTA = 1
ALFA = 2
REPETICIONES = 5  # Número de repeticiones por caso para obtener promedios

def generar_cadena_aleatoria(longitud, alfabeto=None):
    """
    Genera una cadena aleatoria de longitud específica.
    
    Args:
        longitud: Longitud de la cadena
        alfabeto: Alfabeto a usar (por defecto: letras mayúsculas)
    
    Returns:
        str: Cadena aleatoria generada
    """
    if alfabeto is None:
        alfabeto = string.ascii_uppercase
    return ''.join(random.choice(alfabeto) for _ in range(longitud))

def medir_tiempo_algoritmo(func, s1, s2, *args):
    """
    Mide el tiempo de ejecución de un algoritmo.
    
    Args:
        func: Función del algoritmo a medir
        s1, s2: Cadenas de entrada
        *args: Argumentos adicionales para la función
    
    Returns:
        tuple: (resultado, tiempo_promedio)
    """
    tiempos = []
    resultado = None
    
    for _ in range(REPETICIONES):
        start_time = time.perf_counter()
        if func == distancia_edicion_recursiva:
            resultado = func(s1, s2)
        else:  # distancia_edicion_dinamica
            resultado, _ = func(s1, s2)
        end_time = time.perf_counter()
        tiempos.append(end_time - start_time)
    
    tiempo_promedio = sum(tiempos) / len(tiempos)
    return resultado, tiempo_promedio

def benchmark_por_longitud():
    """Benchmark variando la longitud de las cadenas."""
    print("="*70)
    print("BENCHMARK POR LONGITUD DE CADENAS")
    print("="*70)
    print()
    
    longitudes = [3, 5, 8, 10, 12, 15]
    resultados = []
    
    print(f"{'Longitud':<10} {'Recursivo (ms)':<15} {'Prog.Din (ms)':<15} {'Mejora':<10} {'Distancia':<10}")
    print("-" * 70)
    
    for longitud in longitudes:
        # Generar cadenas de prueba
        s1 = generar_cadena_aleatoria(longitud)
        s2 = generar_cadena_aleatoria(longitud)
        
        # Medir algoritmo recursivo
        dist_rec, tiempo_rec = medir_tiempo_algoritmo(distancia_edicion_recursiva, s1, s2)
        
        # Medir programación dinámica
        dist_dp, tiempo_dp = medir_tiempo_algoritmo(distancia_edicion_dinamica, s1, s2)
        
        # Calcular mejora
        mejora = tiempo_rec / tiempo_dp if tiempo_dp > 0 else float('inf')
        
        # Mostrar resultados
        print(f"{longitud:<10} {tiempo_rec*1000:<15.3f} {tiempo_dp*1000:<15.3f} {mejora:<10.2f}x {dist_rec:<10}")
        
        resultados.append({
            'longitud': longitud,
            'tiempo_recursivo': tiempo_rec,
            'tiempo_dinamica': tiempo_dp,
            'mejora': mejora,
            'distancia': dist_rec,
            's1': s1,
            's2': s2
        })
    
    return resultados

def benchmark_casos_especiales():
    """Benchmark para casos especiales conocidos."""
    print("\n" + "="*70)
    print("BENCHMARK CASOS ESPECIALES")
    print("="*70)
    print()
    
    casos = [
        ("ALGORITMO", "ALTRUISMO", "Caso original"),
        ("GATO", "PERRO", "Palabras diferentes"),
        ("CASA", "MASA", "Una sustitución"),
        ("", "ABC", "Cadena vacía vs texto"),
        ("XYZ", "", "Texto vs cadena vacía"),
        ("ABCD", "ABCD", "Cadenas idénticas"),
        ("ABCDEF", "FEDCBA", "Cadenas inversas"),
        ("A", "B", "Caracteres simples"),
        ("AAA", "BBB", "Repeticiones"),
        ("ABCDE", "ACE", "Eliminaciones múltiples")
    ]
    
    print(f"{'Caso':<25} {'T1':<10} {'T2':<10} {'Dist':<6} {'Rec(ms)':<10} {'DP(ms)':<10} {'Mejora':<8}")
    print("-" * 85)
    
    for s1, s2, descripcion in casos:
        # Medir ambos algoritmos
        dist_rec, tiempo_rec = medir_tiempo_algoritmo(distancia_edicion_recursiva, s1, s2)
        dist_dp, tiempo_dp = medir_tiempo_algoritmo(distancia_edicion_dinamica, s1, s2)
        
        mejora = tiempo_rec / tiempo_dp if tiempo_dp > 0 else float('inf')
        
        print(f"{descripcion:<25} {s1:<10} {s2:<10} {dist_rec:<6} {tiempo_rec*1000:<10.3f} {tiempo_dp*1000:<10.3f} {mejora:<8.2f}x")

def benchmark_similitud():
    """Benchmark variando el nivel de similitud entre cadenas."""
    print("\n" + "="*70)
    print("BENCHMARK POR NIVEL DE SIMILITUD")
    print("="*70)
    print()
    
    longitud_base = 10
    niveles_similitud = [0.0, 0.2, 0.4, 0.6, 0.8, 1.0]
    
    print(f"{'Similitud':<12} {'Caracteres':<12} {'Dist':<6} {'Rec(ms)':<10} {'DP(ms)':<10} {'Mejora':<8}")
    print("-" * 65)
    
    for similitud in niveles_similitud:
        # Generar cadena base
        s1 = generar_cadena_aleatoria(longitud_base)
        
        # Generar s2 con nivel de similitud específico
        caracteres_iguales = int(longitud_base * similitud)
        caracteres_diferentes = longitud_base - caracteres_iguales
        
        s2_chars = list(s1[:caracteres_iguales])
        s2_chars.extend(generar_cadena_aleatoria(caracteres_diferentes))
        random.shuffle(s2_chars)
        s2 = ''.join(s2_chars)
        
        # Medir algoritmos
        dist_rec, tiempo_rec = medir_tiempo_algoritmo(distancia_edicion_recursiva, s1, s2)
        dist_dp, tiempo_dp = medir_tiempo_algoritmo(distancia_edicion_dinamica, s1, s2)
        
        mejora = tiempo_rec / tiempo_dp if tiempo_dp > 0 else float('inf')
        
        print(f"{similitud:<12.1%} {caracteres_iguales}/{longitud_base:<12} {dist_rec:<6} {tiempo_rec*1000:<10.3f} {tiempo_dp*1000:<10.3f} {mejora:<8.2f}x")

def generar_reporte_completo():
    """Genera un reporte completo de benchmarks."""
    print("SISTEMA DE BENCHMARKING - DISTANCIA DE EDICIÓN")
    print(f"Parámetros: Delta = {DELTA}, Alfa = {ALFA}")
    print(f"Repeticiones por caso: {REPETICIONES}")
    print(f"Fecha: {time.strftime('%Y-%m-%d %H:%M:%S')}")
    
    # Ejecutar todos los benchmarks
    resultados_longitud = benchmark_por_longitud()
    benchmark_casos_especiales()
    benchmark_similitud()
    
    # Análisis estadístico
    print("\n" + "="*70)
    print("ANÁLISIS ESTADÍSTICO")
    print("="*70)
    
    if resultados_longitud:
        mejoras = [r['mejora'] for r in resultados_longitud if r['mejora'] != float('inf')]
        if mejoras:
            print(f"Mejora promedio de Prog. Dinámica: {sum(mejoras)/len(mejoras):.2f}x")
            print(f"Mejora mínima: {min(mejoras):.2f}x")
            print(f"Mejora máxima: {max(mejoras):.2f}x")
    
    # Guardar resultados
    guardar_resultados(resultados_longitud)

def guardar_resultados(resultados):
    """Guarda los resultados en un archivo."""
    try:
        os.makedirs("resultados", exist_ok=True)
        
        with open("resultados/tiempos_ejecucion.txt", "w", encoding="utf-8") as f:
            f.write("Resultados de Benchmarking - Distancia de Edición\n")
            f.write("="*50 + "\n\n")
            f.write(f"Fecha: {time.strftime('%Y-%m-%d %H:%M:%S')}\n")
            f.write(f"Parámetros: Delta = {DELTA}, Alfa = {ALFA}\n")
            f.write(f"Repeticiones: {REPETICIONES}\n\n")
            
            f.write("Longitud\tT_Recursivo(s)\tT_Dinamica(s)\tMejora\tDistancia\tS1\tS2\n")
            for r in resultados:
                f.write(f"{r['longitud']}\t{r['tiempo_recursivo']:.6f}\t{r['tiempo_dinamica']:.6f}\t")
                f.write(f"{r['mejora']:.2f}\t{r['distancia']}\t{r['s1']}\t{r['s2']}\n")
        
        print(f"\n✓ Resultados guardados en: resultados/tiempos_ejecucion.txt")
        
    except Exception as e:
        print(f"✗ Error al guardar resultados: {e}")

def main():
    """Función principal del sistema de benchmarking."""
    random.seed(42)  # Para resultados reproducibles
    
    try:
        generar_reporte_completo()
    except KeyboardInterrupt:
        print("\n\nBenchmark interrumpido por el usuario.")
    except Exception as e:
        print(f"\nError durante el benchmarking: {e}")

if __name__ == "__main__":
    main()