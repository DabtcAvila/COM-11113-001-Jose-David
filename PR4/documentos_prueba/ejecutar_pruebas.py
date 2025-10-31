#!/usr/bin/env python3
"""
Script para ejecutar todas las pruebas de documentos.
"""

import os
import sys
import glob
import subprocess

def ejecutar_prueba(archivo_a, archivo_b):
    """Ejecuta una prueba específica."""
    print(f"\nProbando: {archivo_a} vs {archivo_b}")
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
    
    print("\n" + "="*60)
    print("PRUEBAS ESPECIALES")
    print("="*60)
    
    for archivo_a, archivo_b in pruebas_especiales:
        if os.path.exists(archivo_a) and os.path.exists(archivo_b):
            ejecutar_prueba(archivo_a, archivo_b)

if __name__ == "__main__":
    main()
