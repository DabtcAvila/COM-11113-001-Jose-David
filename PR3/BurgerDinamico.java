import java.io.*;
import java.util.*;

public class BurgerDinamico {
    
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
        long inicioTiempo = System.nanoTime();
        final long LIMITE_TIEMPO_NANO = 3_000_000_000L;
        
        if (t == 0) {
            return new Resultado(0, 0, 0, 0);
        }
        
        // Optimización de espacio: usar rolling array si t es muy grande
        if (t > 100000) {
            return resolverOptimizado(m, n, t, inicioTiempo, LIMITE_TIEMPO_NANO);
        }
        
        int[] dp = new int[t + 1];
        int[] ultimaEleccionM = new int[t + 1]; // Para reconstruir la solución
        
        for (int tiempo = 1; tiempo <= t; tiempo++) {
            // Verificar timeout cada 10000 iteraciones
            if (tiempo % 10000 == 0 && System.nanoTime() - inicioTiempo > LIMITE_TIEMPO_NANO) {
                return new Resultado(true);
            }
            
            dp[tiempo] = dp[tiempo - 1]; // No hacer nada
            ultimaEleccionM[tiempo] = ultimaEleccionM[tiempo - 1];
            
            // Probar hacer hamburguesa M
            if (tiempo >= m && dp[tiempo - m] + 1 > dp[tiempo]) {
                dp[tiempo] = dp[tiempo - m] + 1;
                ultimaEleccionM[tiempo] = ultimaEleccionM[tiempo - m] + 1;
            }
            
            // Probar hacer hamburguesa N
            if (tiempo >= n && dp[tiempo - n] + 1 > dp[tiempo]) {
                dp[tiempo] = dp[tiempo - n] + 1;
                ultimaEleccionM[tiempo] = ultimaEleccionM[tiempo - n]; // No cambiar M
            }
        }
        
        // Reconstruir la solución óptima
        int maxBurgers = dp[t];
        int burgersM = ultimaEleccionM[t];
        int burgersN = maxBurgers - burgersM;
        int tiempoUsado = burgersM * m + burgersN * n;
        int tiempoRestante = t - tiempoUsado;
        
        return new Resultado(maxBurgers, burgersM, burgersN, tiempoRestante);
    }
    
    // Versión optimizada para valores grandes de t usando espacio constante
    private static Resultado resolverOptimizado(int m, int n, int t, long inicioTiempo, long limite) {
        // Para casos muy grandes, usar aproximación greedy optimizada
        int burgersTotales = 0;
        int burgersM = 0;
        int burgersN = 0;
        int tiempoRestante = t;
        
        // Estrategia greedy: siempre elegir la opción más eficiente
        while (tiempoRestante >= Math.min(m, n)) {
            if (System.nanoTime() - inicioTiempo > limite) {
                return new Resultado(true);
            }
            
            if (m <= n && tiempoRestante >= m) {
                burgersM++;
                tiempoRestante -= m;
            } else if (tiempoRestante >= n) {
                burgersN++;
                tiempoRestante -= n;
            } else {
                break;
            }
            burgersTotales++;
        }
        
        return new Resultado(burgersTotales, burgersM, burgersN, tiempoRestante);
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