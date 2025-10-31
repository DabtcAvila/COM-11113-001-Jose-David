#!/usr/bin/env python3
"""
Sistema de Benchmarking Oficial para Práctica 4
Gráficas de Desempeño según Especificaciones del Profesor

Estudiante: David Fernando Avila Díaz - Clave Única: 197851
Estudiante: José Gerardo Malfavaun Gorostizaga - Clave Única: 213398
Instituto Tecnológico Autónomo de México (ITAM)
Departamento Académico de Computación
Prof. Marco Morales Aguirre

Descripción:
Genera gráficas de desempeño para las tres versiones del algoritmo
con las longitudes especificadas: [5, 50, 500, 5000, 50000]
"""

import time
import random
import string
import signal
import sys
import os
from practica4 import (
    distancia_edicion_recursiva_sin_memo,
    distancia_edicion_recursiva_con_memo, 
    distancia_edicion_iterativa,
    TimeoutException,
    timeout_handler
)

# Intentar importar matplotlib
try:
    import matplotlib.pyplot as plt
    import numpy as np
    GRAFICAS_DISPONIBLES = True
except ImportError:
    GRAFICAS_DISPONIBLES = False
    print("Advertencia: matplotlib no disponible. Se generarán solo datos numéricos.")

class BenchmarkOficial:
    """Clase para realizar benchmarks según especificaciones oficiales."""
    
    def __init__(self):
        self.longitudes = [5, 50, 500, 5000, 50000]
        self.resultados = {
            'recursiva_sin_memo': [],
            'recursiva_con_memo': [],
            'iterativa': []
        }
        self.timeouts = {
            'recursiva_sin_memo': [],
            'recursiva_con_memo': [],
            'iterativa': []
        }
    
    def generar_texto_aleatorio(self, longitud):
        """
        Genera un texto aleatorio de longitud específica.
        Simula texto real con espacios y puntuación.
        """
        # Usar caracteres más realistas para texto
        caracteres = string.ascii_letters + string.digits + ' .,;:!?-\n'
        texto = ''.join(random.choice(caracteres) for _ in range(longitud))
        return texto
    
    def medir_tiempo_con_timeout(self, func, *args, timeout_seconds=10):
        """
        Mide tiempo de ejecución con timeout.
        
        Returns:
            tuple: (tiempo, timeout_ocurrido, resultado)
        """
        signal.signal(signal.SIGALRM, timeout_handler)
        signal.alarm(timeout_seconds)
        
        try:
            start_time = time.perf_counter()
            resultado = func(*args)
            end_time = time.perf_counter()
            tiempo = end_time - start_time
            
            signal.alarm(0)
            return tiempo, False, resultado
            
        except TimeoutException:
            signal.alarm(0)
            return timeout_seconds, True, None
        except Exception as e:
            signal.alarm(0)
            print(f"Error durante medición: {e}")
            return 0, False, None
    
    def ejecutar_benchmark_longitud(self, longitud):
        """Ejecuta benchmark para una longitud específica."""
        print(f"\nTesting longitud: {longitud} caracteres")
        print("-" * 50)
        
        # Generar textos de prueba
        texto1 = self.generar_texto_aleatorio(longitud)
        texto2 = self.generar_texto_aleatorio(longitud)
        
        # 1. Versión recursiva SIN memoización
        print("Probando versión recursiva sin memoización...")
        tiempo1, timeout1, resultado1 = self.medir_tiempo_con_timeout(
            distancia_edicion_recursiva_sin_memo, texto1, texto2, len(texto1), len(texto2)
        )
        
        if timeout1:
            print(f"  ✗ Timeout ({tiempo1}s)")
            self.resultados['recursiva_sin_memo'].append(None)
            self.timeouts['recursiva_sin_memo'].append(True)
        else:
            print(f"  ✓ Completado en {tiempo1:.6f}s, resultado: {resultado1}")
            self.resultados['recursiva_sin_memo'].append(tiempo1)
            self.timeouts['recursiva_sin_memo'].append(False)
        
        # 2. Versión recursiva CON memoización
        print("Probando versión recursiva con memoización...")
        memo = {}
        tiempo2, timeout2, resultado2 = self.medir_tiempo_con_timeout(
            distancia_edicion_recursiva_con_memo, texto1, texto2, len(texto1), len(texto2), memo
        )
        
        if timeout2:
            print(f"  ✗ Timeout ({tiempo2}s)")
            self.resultados['recursiva_con_memo'].append(None)
            self.timeouts['recursiva_con_memo'].append(True)
        else:
            print(f"  ✓ Completado en {tiempo2:.6f}s, resultado: {resultado2}")
            self.resultados['recursiva_con_memo'].append(tiempo2)
            self.timeouts['recursiva_con_memo'].append(False)
        
        # 3. Versión iterativa
        print("Probando versión iterativa...")
        tiempo3, timeout3, resultado3 = self.medir_tiempo_con_timeout(
            distancia_edicion_iterativa, texto1, texto2
        )
        
        if timeout3:
            print(f"  ✗ Timeout ({tiempo3}s)")
            self.resultados['iterativa'].append(None)
            self.timeouts['iterativa'].append(True)
        else:
            print(f"  ✓ Completado en {tiempo3:.6f}s, resultado: {resultado3}")
            self.resultados['iterativa'].append(tiempo3)
            self.timeouts['iterativa'].append(False)
        
        # Verificar consistencia de resultados (solo si no hubo timeouts)
        if not timeout1 and not timeout2 and not timeout3:
            if resultado1 == resultado2 == resultado3:
                print(f"  ✓ Resultados consistentes: {resultado1}")
            else:
                print(f"  ✗ Resultados inconsistentes: {resultado1}, {resultado2}, {resultado3}")
    
    def ejecutar_benchmark_completo(self):
        """Ejecuta el benchmark completo para todas las longitudes."""
        print("BENCHMARK OFICIAL - PRÁCTICA 4")
        print("="*60)
        print("Prof. Marco Morales Aguirre")
        print("Longitudes a probar:", self.longitudes)
        print("Timeout por prueba: 10 segundos")
        print()
        
        random.seed(42)  # Para reproducibilidad
        
        for longitud in self.longitudes:
            self.ejecutar_benchmark_longitud(longitud)
        
        self.generar_reporte()
        if GRAFICAS_DISPONIBLES:
            self.generar_graficas()
        else:
            print("\nPara generar gráficas, instale matplotlib:")
            print("pip install matplotlib numpy")
    
    def generar_reporte(self):
        """Genera reporte textual de resultados."""
        print("\n" + "="*60)
        print("REPORTE DE RESULTADOS")
        print("="*60)
        print()
        
        print(f"{'Longitud':<10} {'Sin Memo':<12} {'Con Memo':<12} {'Iterativa':<12}")
        print("-" * 50)
        
        for i, longitud in enumerate(self.longitudes):
            sin_memo = "TIMEOUT" if self.timeouts['recursiva_sin_memo'][i] else f"{self.resultados['recursiva_sin_memo'][i]:.6f}s"
            con_memo = "TIMEOUT" if self.timeouts['recursiva_con_memo'][i] else f"{self.resultados['recursiva_con_memo'][i]:.6f}s"
            iterativa = "TIMEOUT" if self.timeouts['iterativa'][i] else f"{self.resultados['iterativa'][i]:.6f}s"
            
            print(f"{longitud:<10} {sin_memo:<12} {con_memo:<12} {iterativa:<12}")
        
        # Guardar reporte
        self.guardar_reporte()
    
    def guardar_reporte(self):
        """Guarda el reporte en archivo."""
        try:
            os.makedirs("resultados", exist_ok=True)
            
            with open("resultados/benchmark_oficial.txt", "w", encoding="utf-8") as f:
                f.write("BENCHMARK OFICIAL - PRÁCTICA 4\n")
                f.write("="*50 + "\n")
                f.write("Prof. Marco Morales Aguirre\n")
                f.write(f"Fecha: {time.strftime('%Y-%m-%d %H:%M:%S')}\n\n")
                
                f.write("Longitud\tSin_Memo(s)\tCon_Memo(s)\tIterativa(s)\tTimeout_Sin\tTimeout_Con\tTimeout_Iter\n")
                
                for i, longitud in enumerate(self.longitudes):
                    sin_memo = self.resultados['recursiva_sin_memo'][i] if self.resultados['recursiva_sin_memo'][i] else 10.0
                    con_memo = self.resultados['recursiva_con_memo'][i] if self.resultados['recursiva_con_memo'][i] else 10.0
                    iterativa = self.resultados['iterativa'][i] if self.resultados['iterativa'][i] else 10.0
                    
                    f.write(f"{longitud}\t{sin_memo:.6f}\t{con_memo:.6f}\t{iterativa:.6f}\t")
                    f.write(f"{self.timeouts['recursiva_sin_memo'][i]}\t")
                    f.write(f"{self.timeouts['recursiva_con_memo'][i]}\t")
                    f.write(f"{self.timeouts['iterativa'][i]}\n")
            
            print("✓ Reporte guardado en: resultados/benchmark_oficial.txt")
            
        except Exception as e:
            print(f"✗ Error guardando reporte: {e}")
    
    def generar_graficas(self):
        """Genera gráficas según especificaciones."""
        if not GRAFICAS_DISPONIBLES:
            return
        
        try:
            plt.figure(figsize=(15, 10))
            
            # Preparar datos para gráficas
            longitudes_validas = []
            tiempos_sin_memo = []
            tiempos_con_memo = []
            tiempos_iterativa = []
            
            for i, longitud in enumerate(self.longitudes):
                longitudes_validas.append(longitud)
                
                # Usar tiempo máximo (10s) para timeouts
                sin_memo = self.resultados['recursiva_sin_memo'][i] if not self.timeouts['recursiva_sin_memo'][i] else 10.0
                con_memo = self.resultados['recursiva_con_memo'][i] if not self.timeouts['recursiva_con_memo'][i] else 10.0
                iterativa = self.resultados['iterativa'][i] if not self.timeouts['iterativa'][i] else 10.0
                
                tiempos_sin_memo.append(sin_memo)
                tiempos_con_memo.append(con_memo)
                tiempos_iterativa.append(iterativa)
            
            # Gráfica 1: Comparación de tiempos (escala lineal)
            plt.subplot(2, 2, 1)
            plt.plot(longitudes_validas, tiempos_sin_memo, 'ro-', label='Recursiva sin memoización', linewidth=2)
            plt.plot(longitudes_validas, tiempos_con_memo, 'go-', label='Recursiva con memoización', linewidth=2)
            plt.plot(longitudes_validas, tiempos_iterativa, 'bo-', label='Iterativa (Prog. Dinámica)', linewidth=2)
            plt.xlabel('Longitud de texto (caracteres)')
            plt.ylabel('Tiempo (segundos)')
            plt.title('Comparación de Tiempos de Ejecución')
            plt.legend()
            plt.grid(True)
            plt.axhline(y=10, color='r', linestyle='--', alpha=0.5, label='Límite timeout')
            
            # Gráfica 2: Escala logarítmica
            plt.subplot(2, 2, 2)
            plt.loglog(longitudes_validas, tiempos_sin_memo, 'ro-', label='Recursiva sin memoización', linewidth=2)
            plt.loglog(longitudes_validas, tiempos_con_memo, 'go-', label='Recursiva con memoización', linewidth=2)
            plt.loglog(longitudes_validas, tiempos_iterativa, 'bo-', label='Iterativa (Prog. Dinámica)', linewidth=2)
            plt.xlabel('Longitud de texto (caracteres)')
            plt.ylabel('Tiempo (segundos)')
            plt.title('Comparación en Escala Logarítmica')
            plt.legend()
            plt.grid(True)
            
            # Gráfica 3: Solo versiones eficientes (sin recursiva pura)
            plt.subplot(2, 2, 3)
            plt.plot(longitudes_validas, tiempos_con_memo, 'go-', label='Recursiva con memoización', linewidth=2)
            plt.plot(longitudes_validas, tiempos_iterativa, 'bo-', label='Iterativa (Prog. Dinámica)', linewidth=2)
            plt.xlabel('Longitud de texto (caracteres)')
            plt.ylabel('Tiempo (segundos)')
            plt.title('Algoritmos Eficientes (sin recursiva pura)')
            plt.legend()
            plt.grid(True)
            
            # Gráfica 4: Eficiencia relativa
            plt.subplot(2, 2, 4)
            eficiencia = [t_memo/t_iter if t_iter > 0 else 1 for t_memo, t_iter in zip(tiempos_con_memo, tiempos_iterativa)]
            plt.plot(longitudes_validas, eficiencia, 'mo-', linewidth=2)
            plt.xlabel('Longitud de texto (caracteres)')
            plt.ylabel('Factor (Tiempo Memo / Tiempo Iterativo)')
            plt.title('Eficiencia Relativa: Memoización vs Iterativa')
            plt.grid(True)
            plt.axhline(y=1, color='r', linestyle='--', alpha=0.5)
            
            plt.tight_layout()
            
            # Guardar gráficas
            os.makedirs("resultados", exist_ok=True)
            plt.savefig("resultados/graficas_desempeno.png", dpi=300, bbox_inches='tight')
            plt.savefig("resultados/graficas_desempeno.pdf", bbox_inches='tight')
            
            print("✓ Gráficas guardadas en:")
            print("  - resultados/graficas_desempeno.png")
            print("  - resultados/graficas_desempeno.pdf")
            
            # Mostrar gráficas
            plt.show()
            
        except Exception as e:
            print(f"✗ Error generando gráficas: {e}")

def main():
    """Función principal del benchmark."""
    benchmark = BenchmarkOficial()
    benchmark.ejecutar_benchmark_completo()

if __name__ == "__main__":
    main()