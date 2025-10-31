import java.io.*;
import java.util.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * Comparador de algoritmos para el problema de hamburguesas
 * Ejecuta y compara rendimiento de las tres implementaciones
 * 
 * @author José y David
 */
public class AlgorithmComparator {
    
    private static final long LIMITE_TIEMPO_NANO = 3_000_000_000L; // 3 segundos
    private MemoryMXBean memoryBean;
    
    public AlgorithmComparator() {
        this.memoryBean = ManagementFactory.getMemoryMXBean();
    }
    
    /**
     * Ejecuta y compara todos los algoritmos para un caso específico
     */
    public List<PerformanceResult> compararAlgoritmos(int m, int n, int t) {
        List<PerformanceResult> resultados = new ArrayList<>();
        
        // Forzar recolección de basura antes de las pruebas
        System.gc();
        
        // Ejecutar algoritmo recursivo
        resultados.add(ejecutarRecursivo(m, n, t));
        
        System.gc();
        
        // Ejecutar algoritmo de memoización
        resultados.add(ejecutarMemoizacion(m, n, t));
        
        System.gc();
        
        // Ejecutar algoritmo dinámico
        resultados.add(ejecutarDinamico(m, n, t));
        
        return resultados;
    }
    
    /**
     * Ejecuta y mide el algoritmo recursivo
     */
    private PerformanceResult ejecutarRecursivo(int m, int n, int t) {
        PerformanceResult resultado = new PerformanceResult("Recursivo", m, n, t);
        
        long memoriaInicial = getMemoriaUsada();
        long inicioTiempo = System.nanoTime();
        
        try {
            BurgerRecursivo.Resultado res = BurgerRecursivo.resolver(m, n, t);
            
            long finTiempo = System.nanoTime();
            long memoriaFinal = getMemoriaUsada();
            
            resultado.setTiempoEjecucion(finTiempo - inicioTiempo);
            resultado.setMemoriaUsada(Math.max(0, memoriaFinal - memoriaInicial));
            
            if (res.timeoutExcedido) {
                resultado.setTimeoutExcedido(true);
            } else {
                resultado.setResultadoProblema(res.totalBurgers, res.burgersM, res.burgersN, res.tiempoRestante);
                resultado.setSolucionCorrecta(validarSolucion(m, n, t, res.totalBurgers, res.burgersM, res.burgersN, res.tiempoRestante));
            }
            
        } catch (Exception e) {
            resultado.setSolucionCorrecta(false);
            resultado.setTiempoEjecucion(System.nanoTime() - inicioTiempo);
        }
        
        return resultado;
    }
    
    /**
     * Ejecuta y mide el algoritmo de memoización
     */
    private PerformanceResult ejecutarMemoizacion(int m, int n, int t) {
        PerformanceResult resultado = new PerformanceResult("Memoizacion", m, n, t);
        
        long memoriaInicial = getMemoriaUsada();
        long inicioTiempo = System.nanoTime();
        
        try {
            BurgerMemoizacion.Resultado res = BurgerMemoizacion.resolver(m, n, t);
            
            long finTiempo = System.nanoTime();
            long memoriaFinal = getMemoriaUsada();
            
            resultado.setTiempoEjecucion(finTiempo - inicioTiempo);
            resultado.setMemoriaUsada(Math.max(0, memoriaFinal - memoriaInicial));
            
            if (res.timeoutExcedido) {
                resultado.setTimeoutExcedido(true);
            } else {
                resultado.setResultadoProblema(res.totalBurgers, res.burgersM, res.burgersN, res.tiempoRestante);
                resultado.setSolucionCorrecta(validarSolucion(m, n, t, res.totalBurgers, res.burgersM, res.burgersN, res.tiempoRestante));
            }
            
        } catch (Exception e) {
            resultado.setSolucionCorrecta(false);
            resultado.setTiempoEjecucion(System.nanoTime() - inicioTiempo);
        }
        
        return resultado;
    }
    
    /**
     * Ejecuta y mide el algoritmo dinámico
     */
    private PerformanceResult ejecutarDinamico(int m, int n, int t) {
        PerformanceResult resultado = new PerformanceResult("Dinamico", m, n, t);
        
        long memoriaInicial = getMemoriaUsada();
        long inicioTiempo = System.nanoTime();
        
        try {
            BurgerDinamico.Resultado res = BurgerDinamico.resolver(m, n, t);
            
            long finTiempo = System.nanoTime();
            long memoriaFinal = getMemoriaUsada();
            
            resultado.setTiempoEjecucion(finTiempo - inicioTiempo);
            resultado.setMemoriaUsada(Math.max(0, memoriaFinal - memoriaInicial));
            resultado.setTamanoTablaDP(t <= 100000 ? t : 1); // Aproximación del espacio usado
            
            if (res.timeoutExcedido) {
                resultado.setTimeoutExcedido(true);
            } else {
                resultado.setResultadoProblema(res.totalBurgers, res.burgersM, res.burgersN, res.tiempoRestante);
                resultado.setSolucionCorrecta(validarSolucion(m, n, t, res.totalBurgers, res.burgersM, res.burgersN, res.tiempoRestante));
            }
            
        } catch (Exception e) {
            resultado.setSolucionCorrecta(false);
            resultado.setTiempoEjecucion(System.nanoTime() - inicioTiempo);
        }
        
        return resultado;
    }
    
    /**
     * Valida que una solución sea correcta
     */
    private boolean validarSolucion(int m, int n, int t, int total, int burgersM, int burgersN, int tiempoRestante) {
        // Verificar que los contadores sean no negativos
        if (burgersM < 0 || burgersN < 0 || tiempoRestante < 0) return false;
        
        // Verificar que el total sea correcto
        if (total != burgersM + burgersN) return false;
        
        // Verificar que el tiempo usado más el restante sea el total
        int tiempoUsado = burgersM * m + burgersN * n;
        if (tiempoUsado + tiempoRestante != t) return false;
        
        // Verificar que no se pueda hacer más hamburguesas con el tiempo restante
        if (tiempoRestante >= m || tiempoRestante >= n) {
            // Debería poder hacer al menos una hamburguesa más
            return false;
        }
        
        return true;
    }
    
    /**
     * Obtiene memoria usada actualmente
     */
    private long getMemoriaUsada() {
        return memoryBean.getHeapMemoryUsage().getUsed();
    }
    
    /**
     * Ejecuta una batería completa de pruebas
     */
    public List<PerformanceResult> ejecutarBateriaPruebas(List<TestDataGenerator.CasoPrueba> casos) {
        List<PerformanceResult> todosResultados = new ArrayList<>();
        int total = casos.size();
        int actual = 0;
        
        System.out.println("Ejecutando batería de " + total + " casos de prueba...");
        System.out.println("Progreso: [" + "=".repeat(50) + "]");
        System.out.print("          [");
        
        for (TestDataGenerator.CasoPrueba caso : casos) {
            actual++;
            
            // Mostrar progreso
            int porcentaje = (actual * 50) / total;
            if (actual % (total / 50 + 1) == 0) {
                System.out.print("=");
                System.out.flush();
            }
            
            List<PerformanceResult> resultadosCaso = compararAlgoritmos(caso.m, caso.n, caso.t);
            for (PerformanceResult res : resultadosCaso) {
                // Agregar información del tipo de caso
                todosResultados.add(res);
            }
            
            // Pausa pequeña para evitar sobrecarga del sistema
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("] Completado!");
        return todosResultados;
    }
    
    /**
     * Genera reporte de comparación
     */
    public void generarReporte(List<PerformanceResult> resultados, String nombreArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("=== REPORTE DE COMPARACIÓN DE ALGORITMOS ===");
            writer.println("Fecha: " + new Date());
            writer.println();
            
            // Estadísticas generales
            Map<String, List<PerformanceResult>> porAlgoritmo = agruparPorAlgoritmo(resultados);
            
            writer.println("=== RESUMEN GENERAL ===");
            for (String algoritmo : porAlgoritmo.keySet()) {
                List<PerformanceResult> resultadosAlg = porAlgoritmo.get(algoritmo);
                long exitosos = resultadosAlg.stream().mapToLong(r -> r.isSolucionCorrecta() && !r.isTimeoutExcedido() ? 1 : 0).sum();
                long timeouts = resultadosAlg.stream().mapToLong(r -> r.isTimeoutExcedido() ? 1 : 0).sum();
                double tiempoPromedio = resultadosAlg.stream()
                    .filter(r -> !r.isTimeoutExcedido())
                    .mapToDouble(PerformanceResult::getTiempoEjecucionSegundos)
                    .average().orElse(0.0);
                
                writer.printf("%s: %d/%d exitosos, %d timeouts, %.6fs promedio%n", 
                    algoritmo, exitosos, resultadosAlg.size(), timeouts, tiempoPromedio);
            }
            writer.println();
            
            // Tabla detallada
            writer.println("=== RESULTADOS DETALLADOS ===");
            writer.println("Algoritmo    | Tiempo    | Memoria    | Resultado       | Parámetros");
            writer.println("-------------|-----------|------------|-----------------|------------");
            
            for (PerformanceResult resultado : resultados) {
                writer.println(resultado.toTableRow());
            }
            
            writer.println();
            writer.println("=== ANÁLISIS DE ESCALABILIDAD ===");
            analizarEscalabilidad(porAlgoritmo, writer);
        }
    }
    
    /**
     * Genera archivo CSV para análisis en Excel/Python
     */
    public void generarCSV(List<PerformanceResult> resultados, String nombreArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            // Encabezado CSV
            writer.println("algoritmo,m,n,t,tiempo_segundos,memoria_bytes,timeout,correcto," +
                          "total_burgers,burgers_m,burgers_n,tiempo_restante," +
                          "llamadas_recursivas,hits_cache,tamano_tabla_dp");
            
            // Datos
            for (PerformanceResult resultado : resultados) {
                writer.println(resultado.toCSV());
            }
        }
    }
    
    /**
     * Agrupa resultados por algoritmo
     */
    private Map<String, List<PerformanceResult>> agruparPorAlgoritmo(List<PerformanceResult> resultados) {
        Map<String, List<PerformanceResult>> mapa = new HashMap<>();
        for (PerformanceResult resultado : resultados) {
            mapa.computeIfAbsent(resultado.getAlgoritmo(), k -> new ArrayList<>()).add(resultado);
        }
        return mapa;
    }
    
    /**
     * Analiza escalabilidad de los algoritmos
     */
    private void analizarEscalabilidad(Map<String, List<PerformanceResult>> porAlgoritmo, PrintWriter writer) {
        writer.println("Análisis basado en tamaño del problema (t):");
        
        for (String algoritmo : porAlgoritmo.keySet()) {
            List<PerformanceResult> resultados = porAlgoritmo.get(algoritmo);
            
            // Agrupar por rangos de t
            Map<String, List<PerformanceResult>> porRango = new HashMap<>();
            for (PerformanceResult res : resultados) {
                String rango = determinarRango(res.getT());
                porRango.computeIfAbsent(rango, k -> new ArrayList<>()).add(res);
            }
            
            writer.println("\n" + algoritmo + ":");
            for (String rango : Arrays.asList("Pequeño", "Mediano", "Grande", "Muy Grande")) {
                if (porRango.containsKey(rango)) {
                    List<PerformanceResult> enRango = porRango.get(rango);
                    double tiempoPromedio = enRango.stream()
                        .filter(r -> !r.isTimeoutExcedido())
                        .mapToDouble(PerformanceResult::getTiempoEjecucionSegundos)
                        .average().orElse(0.0);
                    long timeouts = enRango.stream().mapToLong(r -> r.isTimeoutExcedido() ? 1 : 0).sum();
                    
                    writer.printf("  %s (t<%s): %.6fs promedio, %d timeouts de %d casos%n",
                        rango, getRangoMaximo(rango), tiempoPromedio, timeouts, enRango.size());
                }
            }
        }
    }
    
    private String determinarRango(int t) {
        if (t <= 100) return "Pequeño";
        if (t <= 1000) return "Mediano";
        if (t <= 10000) return "Grande";
        return "Muy Grande";
    }
    
    private String getRangoMaximo(String rango) {
        switch (rango) {
            case "Pequeño": return "100";
            case "Mediano": return "1K";
            case "Grande": return "10K";
            case "Muy Grande": return "∞";
            default: return "?";
        }
    }
    
    /**
     * Programa principal para ejecutar comparación completa
     */
    public static void main(String[] args) {
        AlgorithmComparator comparador = new AlgorithmComparator();
        TestDataGenerator generador = new TestDataGenerator();
        
        try {
            System.out.println("=== COMPARADOR DE ALGORITMOS DE HAMBURGUESAS ===");
            
            // Generar casos de prueba
            System.out.println("Generando casos de prueba...");
            List<TestDataGenerator.CasoPrueba> casos = generador.generarConjuntoCompleto();
            
            // Filtrar casos para ejecución rápida (opcional)
            if (args.length > 0 && args[0].equals("--rapido")) {
                casos = generador.filtrarPorTipo(casos, 
                    TestDataGenerator.TipoCaso.TRIVIAL, 
                    TestDataGenerator.TipoCaso.PEQUENO);
                System.out.println("Modo rápido: usando solo " + casos.size() + " casos pequeños");
            }
            
            // Ejecutar comparación
            List<PerformanceResult> resultados = comparador.ejecutarBateriaPruebas(casos);
            
            // Generar reportes
            System.out.println("\nGenerando reportes...");
            comparador.generarReporte(resultados, "reporte_comparacion.txt");
            comparador.generarCSV(resultados, "resultados_comparacion.csv");
            
            System.out.println("Archivos generados:");
            System.out.println("- reporte_comparacion.txt");
            System.out.println("- resultados_comparacion.csv");
            
            // Mostrar resumen rápido
            System.out.println("\n=== RESUMEN RÁPIDO ===");
            Map<String, List<PerformanceResult>> porAlg = comparador.agruparPorAlgoritmo(resultados);
            for (String alg : porAlg.keySet()) {
                List<PerformanceResult> res = porAlg.get(alg);
                long exitosos = res.stream().mapToLong(r -> r.isSolucionCorrecta() && !r.isTimeoutExcedido() ? 1 : 0).sum();
                System.out.printf("%s: %d/%d casos exitosos%n", alg, exitosos, res.size());
            }
            
        } catch (Exception e) {
            System.err.println("Error en la comparación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}