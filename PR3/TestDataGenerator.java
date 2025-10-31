import java.io.*;
import java.util.*;

/**
 * Generador de casos de prueba para evaluar algoritmos de hamburguesas
 * Genera datos escalables para análisis de rendimiento
 * 
 * @author José y David
 */
public class TestDataGenerator {
    
    private Random random;
    
    public TestDataGenerator() {
        this.random = new Random(42); // Seed fijo para reproducibilidad
    }
    
    public TestDataGenerator(long seed) {
        this.random = new Random(seed);
    }
    
    /**
     * Caso de prueba individual
     */
    public static class CasoPrueba {
        public final int m, n, t;
        public final String descripcion;
        public final TipoCaso tipo;
        
        public CasoPrueba(int m, int n, int t, String descripcion, TipoCaso tipo) {
            this.m = m;
            this.n = n;
            this.t = t;
            this.descripcion = descripcion;
            this.tipo = tipo;
        }
        
        @Override
        public String toString() {
            return String.format("%d %d %d // %s (%s)", m, n, t, descripcion, tipo);
        }
        
        public String toTestFormat() {
            return String.format("%d %d %d", m, n, t);
        }
    }
    
    public enum TipoCaso {
        TRIVIAL,        // Casos muy pequeños
        PEQUENO,        // Casos pequeños para recursivo
        MEDIANO,        // Casos medianos para memoización
        GRANDE,         // Casos grandes para programación dinámica
        ESTRES,         // Casos de estrés para timeout
        BORDE,          // Casos extremos
        EQUILIBRADO,    // m ≈ n
        DESBALANCEADO   // m << n o m >> n
    }
    
    /**
     * Genera conjunto completo de casos de prueba
     */
    public List<CasoPrueba> generarConjuntoCompleto() {
        List<CasoPrueba> casos = new ArrayList<>();
        
        // Casos triviales (para verificar correctitud)
        casos.addAll(generarCasosTriviales());
        
        // Casos pequeños (recursivo debería funcionar)
        casos.addAll(generarCasosPequenos());
        
        // Casos medianos (para memoización)
        casos.addAll(generarCasosMedianos());
        
        // Casos grandes (para programación dinámica)
        casos.addAll(generarCasosGrandes());
        
        // Casos de estrés (para testear timeout)
        casos.addAll(generarCasosEstres());
        
        // Casos de borde
        casos.addAll(generarCasosBorde());
        
        return casos;
    }
    
    /**
     * Casos triviales - resultados conocidos
     */
    public List<CasoPrueba> generarCasosTriviales() {
        List<CasoPrueba> casos = new ArrayList<>();
        
        casos.add(new CasoPrueba(1, 1, 5, "5 hamburguesas iguales", TipoCaso.TRIVIAL));
        casos.add(new CasoPrueba(2, 3, 10, "Caso ejemplo básico", TipoCaso.TRIVIAL));
        casos.add(new CasoPrueba(4, 9, 10, "Del archivo original", TipoCaso.TRIVIAL));
        casos.add(new CasoPrueba(3, 5, 7, "Resultado parcial", TipoCaso.TRIVIAL));
        casos.add(new CasoPrueba(1, 2, 0, "Sin tiempo", TipoCaso.TRIVIAL));
        casos.add(new CasoPrueba(5, 7, 3, "Tiempo insuficiente", TipoCaso.TRIVIAL));
        
        return casos;
    }
    
    /**
     * Casos pequeños - para algoritmo recursivo
     */
    public List<CasoPrueba> generarCasosPequenos() {
        List<CasoPrueba> casos = new ArrayList<>();
        
        for (int i = 1; i <= 10; i++) {
            int m = 1 + random.nextInt(5);
            int n = 1 + random.nextInt(5);
            int t = 5 + random.nextInt(20);
            casos.add(new CasoPrueba(m, n, t, "Pequeño aleatorio " + i, TipoCaso.PEQUENO));
        }
        
        // Casos específicos pequeños
        casos.add(new CasoPrueba(1, 10, 15, "m muy pequeño", TipoCaso.PEQUENO));
        casos.add(new CasoPrueba(10, 1, 15, "n muy pequeño", TipoCaso.PEQUENO));
        casos.add(new CasoPrueba(3, 3, 18, "m = n pequeño", TipoCaso.PEQUENO));
        
        return casos;
    }
    
    /**
     * Casos medianos - para memoización
     */
    public List<CasoPrueba> generarCasosMedianos() {
        List<CasoPrueba> casos = new ArrayList<>();
        
        for (int i = 1; i <= 15; i++) {
            int m = 2 + random.nextInt(20);
            int n = 2 + random.nextInt(20);
            int t = 50 + random.nextInt(500);
            casos.add(new CasoPrueba(m, n, t, "Mediano aleatorio " + i, TipoCaso.MEDIANO));
        }
        
        // Casos específicos medianos
        casos.add(new CasoPrueba(7, 11, 100, "Primos medianos", TipoCaso.MEDIANO));
        casos.add(new CasoPrueba(5, 25, 200, "Factor 5", TipoCaso.MEDIANO));
        casos.add(new CasoPrueba(12, 18, 300, "Múltiplos de 6", TipoCaso.MEDIANO));
        
        return casos;
    }
    
    /**
     * Casos grandes - para programación dinámica
     */
    public List<CasoPrueba> generarCasosGrandes() {
        List<CasoPrueba> casos = new ArrayList<>();
        
        for (int i = 1; i <= 20; i++) {
            int m = 10 + random.nextInt(100);
            int n = 10 + random.nextInt(100);
            int t = 1000 + random.nextInt(10000);
            casos.add(new CasoPrueba(m, n, t, "Grande aleatorio " + i, TipoCaso.GRANDE));
        }
        
        // Casos específicos grandes
        casos.add(new CasoPrueba(13, 17, 5000, "Primos grandes", TipoCaso.GRANDE));
        casos.add(new CasoPrueba(50, 75, 10000, "Múltiplos de 25", TipoCaso.GRANDE));
        casos.add(new CasoPrueba(33, 44, 15000, "Múltiplos de 11", TipoCaso.GRANDE));
        
        return casos;
    }
    
    /**
     * Casos de estrés - para probar timeout
     */
    public List<CasoPrueba> generarCasosEstres() {
        List<CasoPrueba> casos = new ArrayList<>();
        
        // Casos que deberían causar timeout en recursivo
        casos.add(new CasoPrueba(2, 3, 50, "Timeout recursivo probable", TipoCaso.ESTRES));
        casos.add(new CasoPrueba(1, 1, 45, "Máximo recursivo", TipoCaso.ESTRES));
        
        // Casos muy grandes
        casos.add(new CasoPrueba(100, 200, 100000, "Muy grande 1", TipoCaso.ESTRES));
        casos.add(new CasoPrueba(300, 500, 1000000, "Extremo grande", TipoCaso.ESTRES));
        casos.add(new CasoPrueba(1, 2, 50000, "Linear grande", TipoCaso.ESTRES));
        
        return casos;
    }
    
    /**
     * Casos de borde - valores extremos
     */
    public List<CasoPrueba> generarCasosBorde() {
        List<CasoPrueba> casos = new ArrayList<>();
        
        casos.add(new CasoPrueba(1, 1000000, 1000000, "m mínimo, n máximo", TipoCaso.BORDE));
        casos.add(new CasoPrueba(1000000, 1, 1000000, "m máximo, n mínimo", TipoCaso.BORDE));
        casos.add(new CasoPrueba(500000, 500000, 1000000, "Valores máximos", TipoCaso.BORDE));
        casos.add(new CasoPrueba(1, 1, 1000000, "Tiempos mínimos", TipoCaso.BORDE));
        casos.add(new CasoPrueba(999999, 1000000, 1000000, "Consecutivos grandes", TipoCaso.BORDE));
        
        return casos;
    }
    
    /**
     * Genera casos balanceados vs desbalanceados
     */
    public List<CasoPrueba> generarCasosEquilibrio() {
        List<CasoPrueba> casos = new ArrayList<>();
        
        // Equilibrados (m ≈ n)
        for (int base = 10; base <= 1000; base *= 10) {
            casos.add(new CasoPrueba(base, base, base * 10, "Equilibrado " + base, TipoCaso.EQUILIBRADO));
            casos.add(new CasoPrueba(base, base + 1, base * 10, "Casi equilibrado " + base, TipoCaso.EQUILIBRADO));
        }
        
        // Desbalanceados
        for (int factor = 2; factor <= 100; factor *= 2) {
            int base = 10;
            casos.add(new CasoPrueba(base, base * factor, base * factor * 5, 
                     "Desbalanceado 1:" + factor, TipoCaso.DESBALANCEADO));
            casos.add(new CasoPrueba(base * factor, base, base * factor * 5, 
                     "Desbalanceado " + factor + ":1", TipoCaso.DESBALANCEADO));
        }
        
        return casos;
    }
    
    /**
     * Guarda casos de prueba en archivo
     */
    public void guardarCasos(List<CasoPrueba> casos, String nombreArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("# Casos de prueba generados para análisis de rendimiento");
            writer.println("# Formato: m n t // descripcion (tipo)");
            writer.println();
            
            String tipoActual = "";
            for (CasoPrueba caso : casos) {
                if (!tipoActual.equals(caso.tipo.toString())) {
                    tipoActual = caso.tipo.toString();
                    writer.println("# === " + tipoActual + " ===");
                }
                writer.println(caso.toString());
            }
        }
    }
    
    /**
     * Guarda solo datos para testing (sin comentarios)
     */
    public void guardarSoloTesting(List<CasoPrueba> casos, String nombreArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            for (CasoPrueba caso : casos) {
                writer.println(caso.toTestFormat());
            }
        }
    }
    
    /**
     * Filtra casos por tipo
     */
    public List<CasoPrueba> filtrarPorTipo(List<CasoPrueba> casos, TipoCaso... tipos) {
        Set<TipoCaso> tiposPermitidos = new HashSet<>(Arrays.asList(tipos));
        return casos.stream()
                   .filter(caso -> tiposPermitidos.contains(caso.tipo))
                   .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Programa principal para generar archivos de prueba
     */
    public static void main(String[] args) {
        TestDataGenerator generator = new TestDataGenerator();
        
        try {
            // Generar conjunto completo
            List<CasoPrueba> todosLosCasos = generator.generarConjuntoCompleto();
            generator.guardarCasos(todosLosCasos, "casos_prueba_completos.txt");
            
            // Generar subconjuntos específicos
            List<CasoPrueba> casosRapidos = generator.filtrarPorTipo(todosLosCasos, 
                TipoCaso.TRIVIAL, TipoCaso.PEQUENO, TipoCaso.MEDIANO);
            generator.guardarSoloTesting(casosRapidos, "casos_rapidos.txt");
            
            List<CasoPrueba> casosEstres = generator.filtrarPorTipo(todosLosCasos, 
                TipoCaso.ESTRES, TipoCaso.BORDE);
            generator.guardarSoloTesting(casosEstres, "casos_estres.txt");
            
            // Equilibrio
            List<CasoPrueba> casosEquilibrio = generator.generarCasosEquilibrio();
            generator.guardarCasos(casosEquilibrio, "casos_equilibrio.txt");
            
            System.out.println("Archivos de casos de prueba generados:");
            System.out.println("- casos_prueba_completos.txt (" + todosLosCasos.size() + " casos)");
            System.out.println("- casos_rapidos.txt (" + casosRapidos.size() + " casos)");
            System.out.println("- casos_estres.txt (" + casosEstres.size() + " casos)");
            System.out.println("- casos_equilibrio.txt (" + casosEquilibrio.size() + " casos)");
            
        } catch (IOException e) {
            System.err.println("Error al generar archivos: " + e.getMessage());
        }
    }
}