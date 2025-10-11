import java.io.*;
import java.util.*;

public class BurgerMemoizacion {
    
    private static Map<Long, Resultado> memo = new HashMap<>();
    private static long inicioTiempo;
    private static final long LIMITE_TIEMPO_NANO = 3_000_000_000L; // 3 segundos
    
    public static class Resultado {
        int totalBurgers;
        int burgersM;
        int burgersN;
        int tiempoRestante;
        boolean timeoutExcedido;
        
        public Resultado(int total, int m, int n, int resto) {
            this.totalBurgers = total;
            this.burgersM = m;
            this.burgersN = n;
            this.tiempoRestante = resto;
            this.timeoutExcedido = false;
        }
        
        public Resultado(boolean timeout) {
            this.timeoutExcedido = timeout;
        }
    }
    
    public static Resultado resolver(int m, int n, int t) {
        memo.clear();
        inicioTiempo = System.nanoTime();
        
        // Normalizar parámetros para mejor cache hit rate
        if (m > n) {
            Resultado resultado = resolverMemoizado(n, m, t);
            if (!resultado.timeoutExcedido) {
                // Intercambiar resultados para mantener consistencia
                int temp = resultado.burgersM;
                resultado.burgersM = resultado.burgersN;
                resultado.burgersN = temp;
            }
            return resultado;
        }
        
        return resolverMemoizado(m, n, t);
    }
    
    // Usar hash más eficiente con long en lugar de String
    private static long generarClave(int tiempoRestante, int contadorM, int contadorN, int m, int n) {
        // Solo usar tiempo restante como clave principal - más eficiente
        // Los contadores se pueden calcular a partir del tiempo usado
        return ((long) tiempoRestante << 32) | ((long) m << 16) | n;
    }
    
    // Método simplificado que usa solo tiempo restante para memoización
    private static Resultado resolverMemoizado(int m, int n, int tiempoRestante) {
        // Verificar timeout periódicamente
        if (System.nanoTime() - inicioTiempo > LIMITE_TIEMPO_NANO) {
            return new Resultado(true);
        }
        
        // Caso base: no se puede hacer más hamburguesas
        if (tiempoRestante < Math.min(m, n)) {
            return new Resultado(0, 0, 0, tiempoRestante);
        }
        
        long clave = generarClave(tiempoRestante, 0, 0, m, n);
        if (memo.containsKey(clave)) {
            return memo.get(clave);
        }
        
        Resultado mejorResultado = new Resultado(0, 0, 0, tiempoRestante);
        
        // Probar hacer hamburguesa tipo M
        if (tiempoRestante >= m) {
            Resultado resultadoM = resolverMemoizado(m, n, tiempoRestante - m);
            if (resultadoM.timeoutExcedido) {
                return resultadoM;
            }
            int totalM = 1 + resultadoM.totalBurgers;
            if (totalM > mejorResultado.totalBurgers) {
                mejorResultado = new Resultado(totalM, 1 + resultadoM.burgersM, 
                                             resultadoM.burgersN, resultadoM.tiempoRestante);
            }
        }
        
        // Probar hacer hamburguesa tipo N
        if (tiempoRestante >= n) {
            Resultado resultadoN = resolverMemoizado(m, n, tiempoRestante - n);
            if (resultadoN.timeoutExcedido) {
                return resultadoN;
            }
            int totalN = 1 + resultadoN.totalBurgers;
            if (totalN > mejorResultado.totalBurgers) {
                mejorResultado = new Resultado(totalN, resultadoN.burgersM, 
                                             1 + resultadoN.burgersN, resultadoN.tiempoRestante);
            }
        }
        
        memo.put(clave, mejorResultado);
        return mejorResultado;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) break;
            
            String[] partes = linea.split("\\s+");
            int m = Integer.parseInt(partes[0]);
            int n = Integer.parseInt(partes[1]);
            int t = Integer.parseInt(partes[2]);
            
            long inicio = System.nanoTime();
            Resultado resultado = resolver(m, n, t);
            long fin = System.nanoTime();
            
            double tiempoEjecucion = (fin - inicio) / 1_000_000_000.0;
            
            if (resultado.timeoutExcedido || tiempoEjecucion > 3.0) {
                System.out.println("tiempo límite para resolver el problema excedido");
            } else {
                if (resultado.tiempoRestante == 0) {
                    System.out.println(resultado.totalBurgers + " " + resultado.burgersM + " " + resultado.burgersN);
                } else {
                    System.out.println(resultado.totalBurgers + " " + resultado.burgersM + " " + resultado.burgersN + " " + resultado.tiempoRestante);
                }
            }
        }
        
        scanner.close();
    }
}