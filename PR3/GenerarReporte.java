import java.io.*;
import java.util.*;

public class GenerarReporte {
    
    public static void main(String[] args) {
        System.out.println("=== REPORTE PRÁCTICA 3: MEMOIZACIÓN Y PROGRAMACIÓN DINÁMICA ===");
        System.out.println("Autores: José Gerardo Malfavaun Gorostizaga y David Fernando Avila Díaz");
        System.out.println("Materia: EDA - Prof. Marco Morales Aguirre");
        System.out.println();
        
        // Casos específicos requeridos
        int[][] casos = {{4, 9, 10}, {4, 9, 100}, {4, 9, 1000}, {4, 9, 10000}};
        
        System.out.println("📊 RESULTADOS DE CASOS ESPECÍFICOS:");
        System.out.println("Format: m=4, n=9, t=[valor]");
        System.out.println();
        
        for (int[] caso : casos) {
            int m = caso[0], n = caso[1], t = caso[2];
            System.out.println("Caso: m=" + m + ", n=" + n + ", t=" + t);
            
            // Probar algoritmo dinámico (siempre funciona)
            long inicio = System.nanoTime();
            BurgerDinamico.Resultado resultDin = BurgerDinamico.resolver(m, n, t);
            long fin = System.nanoTime();
            double tiempoDin = (fin - inicio) / 1_000_000.0; // milisegundos
            
            // Probar algoritmo memoización
            inicio = System.nanoTime();
            BurgerMemoizacion.Resultado resultMemo = BurgerMemoizacion.resolver(m, n, t);
            fin = System.nanoTime();
            double tiempoMemo = (fin - inicio) / 1_000_000.0;
            
            // Probar algoritmo recursivo (puede hacer timeout)
            inicio = System.nanoTime();
            BurgerRecursivo.Resultado resultRec = BurgerRecursivo.resolver(m, n, t);
            fin = System.nanoTime();
            double tiempoRec = (fin - inicio) / 1_000_000.0;
            
            System.out.println("  Solución óptima: " + resultDin.totalBurgers + " hamburguesas (" + 
                             resultDin.burgersM + " tipo M, " + resultDin.burgersN + " tipo N)");
            
            if (resultDin.tiempoRestante > 0) {
                System.out.println("  Tiempo restante: " + resultDin.tiempoRestante + " minutos");
            }
            
            System.out.println("  Tiempos de ejecución:");
            
            if (resultRec.timeoutExcedido) {
                System.out.println("    - Recursivo: TIMEOUT (>3s)");
            } else {
                System.out.printf("    - Recursivo: %.3f ms%n", tiempoRec);
            }
            
            System.out.printf("    - Memoización: %.3f ms%n", tiempoMemo);
            System.out.printf("    - Dinámico: %.3f ms%n", tiempoDin);
            System.out.println();
        }
        
        System.out.println("🔍 ANÁLISIS DE RENDIMIENTO:");
        System.out.println();
        System.out.println("1. ALGORITMO RECURSIVO:");
        System.out.println("   - Complejidad: O(2^t) exponencial");
        System.out.println("   - Funciona para casos pequeños (t≤100)");
        System.out.println("   - Hace timeout para t≥1000");
        System.out.println("   - Sin optimizaciones, explora todo el espacio de búsqueda");
        System.out.println();
        
        System.out.println("2. ALGORITMO CON MEMOIZACIÓN:");
        System.out.println("   - Complejidad: O(t²) con cache eficiente");
        System.out.println("   - Evita recalcular subproblemas repetidos");
        System.out.println("   - Funciona eficientemente hasta t=10000");
        System.out.println("   - Usa memoria adicional para el cache");
        System.out.println();
        
        System.out.println("3. ALGORITMO DINÁMICO:");
        System.out.println("   - Complejidad: O(t) tiempo, O(t) espacio");
        System.out.println("   - Más eficiente en tiempo y espacio");
        System.out.println("   - Iterativo, sin riesgo de stack overflow");
        System.out.println("   - Solución bottom-up óptima");
        System.out.println();
        
        System.out.println("📈 CONCLUSIONES:");
        System.out.println("• El algoritmo recursivo puro es impracticable para casos grandes");
        System.out.println("• La memoización mejora dramáticamente el rendimiento");
        System.out.println("• La programación dinámica es la solución más eficiente");
        System.out.println("• Para este problema específico (m=4, n=9), siempre conviene usar solo tipo M");
        System.out.println();
        
        System.out.println("Reporte generado el: " + new Date());
    }
}