import java.io.*;
import java.util.*;

public class GenerarGraficas {
    
    public static void main(String[] args) {
        System.out.println("=== GRÁFICAS DE RENDIMIENTO - PRÁCTICA 3 ===");
        System.out.println("Comparación de algoritmos para el problema de hamburguesas");
        System.out.println();
        
        // Datos de los casos específicos (en milisegundos)
        String[] casos = {"t=10", "t=100", "t=1000", "t=10000"};
        double[] tiemposRecursivo = {0.152, 2.243, -1, -1}; // -1 = timeout
        double[] tiemposMemoizacion = {0.220, 0.094, 0.615, 1.322};
        double[] tiemposDinamico = {0.221, 0.004, 0.031, 0.287};
        
        System.out.println("📊 GRÁFICA 1: TIEMPO DE EJECUCIÓN (milisegundos)");
        System.out.println("Escala logarítmica aproximada - cada * = factor de ~10x");
        System.out.println();
        
        for (int i = 0; i < casos.length; i++) {
            System.out.printf("%-8s ", casos[i]);
            
            // Recursivo
            System.out.print("Recursivo:   ");
            if (tiemposRecursivo[i] == -1) {
                System.out.print("TIMEOUT (>3000ms) ");
                System.out.println("█████████████████████████████████████████████");
            } else {
                int barras = (int)(Math.log10(tiemposRecursivo[i] + 1) * 5);
                for (int j = 0; j < barras; j++) System.out.print("█");
                System.out.printf(" %.3fms%n", tiemposRecursivo[i]);
            }
            
            // Memoización
            System.out.print("         Memoización: ");
            int barrasMemo = (int)(Math.log10(tiemposMemoizacion[i] + 1) * 5);
            for (int j = 0; j < barrasMemo; j++) System.out.print("█");
            System.out.printf(" %.3fms%n", tiemposMemoizacion[i]);
            
            // Dinámico
            System.out.print("         Dinámico:    ");
            int barrasDin = (int)(Math.log10(tiemposDinamico[i] + 1) * 5);
            for (int j = 0; j < barrasDin; j++) System.out.print("█");
            System.out.printf(" %.3fms%n", tiemposDinamico[i]);
            
            System.out.println();
        }
        
        System.out.println("📈 GRÁFICA 2: ESCALABILIDAD");
        System.out.println("Crecimiento del tiempo con el tamaño del problema");
        System.out.println();
        
        System.out.println("Algoritmo Recursivo:");
        System.out.println("t=10    ▫");
        System.out.println("t=100   ████");
        System.out.println("t=1000  ████████████████████████ (TIMEOUT)");
        System.out.println("t=10000 ████████████████████████ (TIMEOUT)");
        System.out.println("Complejidad: O(2^t) - Crecimiento exponencial");
        System.out.println();
        
        System.out.println("Algoritmo Memoización:");
        System.out.println("t=10    ▫");
        System.out.println("t=100   ▫");
        System.out.println("t=1000  ██");
        System.out.println("t=10000 ████");
        System.out.println("Complejidad: O(t²) - Crecimiento cuadrático");
        System.out.println();
        
        System.out.println("Algoritmo Dinámico:");
        System.out.println("t=10    ▫");
        System.out.println("t=100   ▫");
        System.out.println("t=1000  ▫");
        System.out.println("t=10000 ▫");
        System.out.println("Complejidad: O(t) - Crecimiento lineal");
        System.out.println();
        
        System.out.println("🏆 COMPARACIÓN DE EFICIENCIA:");
        System.out.println();
        System.out.println("Para t=10:");
        System.out.println("  Recursivo ≈ Memoización ≈ Dinámico");
        System.out.println("  (Casos pequeños, diferencias mínimas)");
        System.out.println();
        
        System.out.println("Para t=100:");
        System.out.println("  Dinámico > Memoización > Recursivo");
        System.out.println("  (Dinámico 560x más rápido que Recursivo)");
        System.out.println();
        
        System.out.println("Para t≥1000:");
        System.out.println("  Dinámico > Memoización >> Recursivo (TIMEOUT)");
        System.out.println("  (Solo los optimizados son viables)");
        System.out.println();
        
        System.out.println("📝 OBSERVACIONES CLAVE:");
        System.out.println("• El algoritmo recursivo puro se vuelve impracticable rápidamente");
        System.out.println("• La memoización reduce la complejidad de O(2^t) a O(t²)");
        System.out.println("• La programación dinámica logra complejidad óptima O(t)");
        System.out.println("• Para casos grandes (t>1000), solo DP y memoización son viables");
        System.out.println("• La diferencia de rendimiento es dramática: factores de 100x-1000x");
        System.out.println();
        
        System.out.println("Gráficas generadas el: " + new Date());
    }
}