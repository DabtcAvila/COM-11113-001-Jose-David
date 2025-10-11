import java.io.*;
import java.util.*;

public class BurgerRecursivo {
    
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
            this.totalBurgers = -1;
            this.burgersM = -1;
            this.burgersN = -1;
            this.tiempoRestante = -1;
        }
    }
    
    private static long inicioTiempo;
    private static final long TIMEOUT_NANO = 3_000_000_000L;
    
    public static Resultado resolver(int m, int n, int t) {
        inicioTiempo = System.nanoTime();
        return resolverRecursivo(m, n, t, 0, 0);
    }
    
    private static Resultado resolverRecursivo(int m, int n, int tiempoRestante, int burgersM, int burgersN) {
        if (System.nanoTime() - inicioTiempo > TIMEOUT_NANO) {
            return new Resultado(true);
        }
        if (tiempoRestante < 0) {
            return new Resultado(-1, -1, -1, -1);
        }
        
        if (tiempoRestante == 0) {
            return new Resultado(burgersM + burgersN, burgersM, burgersN, 0);
        }
        
        if (tiempoRestante < m && tiempoRestante < n) {
            return new Resultado(burgersM + burgersN, burgersM, burgersN, tiempoRestante);
        }
        
        Resultado mejorResultado = new Resultado(burgersM + burgersN, burgersM, burgersN, tiempoRestante);
        
        if (tiempoRestante >= m) {
            Resultado resultadoM = resolverRecursivo(m, n, tiempoRestante - m, burgersM + 1, burgersN);
            if (resultadoM.timeoutExcedido) return resultadoM;
            if (resultadoM.totalBurgers > mejorResultado.totalBurgers) {
                mejorResultado = resultadoM;
            }
        }
        
        if (tiempoRestante >= n) {
            Resultado resultadoN = resolverRecursivo(m, n, tiempoRestante - n, burgersM, burgersN + 1);
            if (resultadoN.timeoutExcedido) return resultadoN;
            if (resultadoN.totalBurgers > mejorResultado.totalBurgers) {
                mejorResultado = resultadoN;
            }
        }
        
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