#!/usr/bin/env python3
"""
Script automático para ejecutar todos los casos de prueba.
"""

import os
import sys
import glob

# Agregar el directorio padre al path
current_dir = os.path.dirname(os.path.abspath(__file__))
parent_dir = os.path.dirname(current_dir)
sys.path.insert(0, parent_dir)

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
    # Ya estamos en el directorio datos_prueba
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
