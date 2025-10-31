import java.io.*;
import java.util.*;

/**
 * Ejecutor principal para tests específicos requeridos en la Práctica 3
 * Ejecuta los casos específicos: m=4, n=9, t=10/100/1000/10000
 * 
 * @author José y David
 */
public class Ejecutor {
    
    // Casos de prueba específicos requeridos
    private static final int[][] CASOS_ESPECIFICOS = {
        {4, 9, 10},
        {4, 9, 100},
        {4, 9, 1000},
        {4, 9, 10000}
    };
    
    private AlgorithmComparator comparador;
    private VisualizadorGraficas visualizador;
    private AnalizadorRendimiento analizador;
    
    public Ejecutor() {
        this.comparador = new AlgorithmComparator();
        this.visualizador = new VisualizadorGraficas();
        this.analizador = new AnalizadorRendimiento();
    }
    
    /**
     * Ejecuta el análisis completo de la Práctica 3
     */
    public void ejecutarAnalisisCompleto() {
        System.out.println("=== EJECUTOR DE PRÁCTICA 3 - ANÁLISIS COMPLETO ===");
        System.out.println("Autores: José y David");
        System.out.println("Casos específicos: m=4, n=9, t=10/100/1000/10000");
        System.out.println();
        
        try {
            // Paso 1: Ejecutar casos específicos
            List<PerformanceResult> resultadosEspecificos = ejecutarCasosEspecificos();
            
            // Paso 2: Ejecutar batería completa de pruebas
            List<PerformanceResult> resultadosCompletos = ejecutarBateriaCompleta();
            
            // Paso 3: Generar reportes
            generarReportes(resultadosCompletos);
            
            // Paso 4: Generar gráficas
            generarGraficas(resultadosCompletos);
            
            // Paso 5: Análisis estadístico
            generarAnalisisEstadistico(resultadosCompletos);
            
            // Paso 6: Resumen final
            mostrarResumenFinal(resultadosEspecificos, resultadosCompletos);
            
        } catch (Exception e) {
            System.err.println("Error durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Ejecuta los casos específicos requeridos
     */
    private List<PerformanceResult> ejecutarCasosEspecificos() {
        System.out.println("🎯 PASO 1: Ejecutando casos específicos requeridos...");
        List<PerformanceResult> resultados = new ArrayList<>();
        
        for (int[] caso : CASOS_ESPECIFICOS) {
            int m = caso[0];
            int n = caso[1];
            int t = caso[2];
            
            System.out.printf("Ejecutando caso: m=%d, n=%d, t=%d%n", m, n, t);
            
            List<PerformanceResult> resultadosCaso = comparador.compararAlgoritmos(m, n, t);
            resultados.addAll(resultadosCaso);
            
            // Mostrar resultados inmediatos
            mostrarResultadosCaso(m, n, t, resultadosCaso);
            System.out.println();
        }
        
        // Guardar resultados específicos
        try {
            guardarResultadosEspecificos(resultados);
        } catch (IOException e) {
            System.err.println("Error guardando resultados específicos: " + e.getMessage());
        }
        
        return resultados;
    }
    
    /**
     * Ejecuta la batería completa de pruebas
     */
    private List<PerformanceResult> ejecutarBateriaCompleta() {
        System.out.println("🔬 PASO 2: Ejecutando batería completa de pruebas...");
        
        TestDataGenerator generador = new TestDataGenerator();
        List<TestDataGenerator.CasoPrueba> casosPrueba = generador.generarConjuntoCompleto();
        
        System.out.println("Total de casos de prueba: " + casosPrueba.size());
        
        return comparador.ejecutarBateriaPruebas(casosPrueba);
    }
    
    /**
     * Genera todos los reportes necesarios
     */
    private void generarReportes(List<PerformanceResult> resultados) throws IOException {
        System.out.println("📊 PASO 3: Generando reportes...");
        
        // Reporte principal
        comparador.generarReporte(resultados, "reporte_practica3.txt");
        System.out.println("✅ Reporte principal: reporte_practica3.txt");
        
        // CSV para análisis
        comparador.generarCSV(resultados, "datos_practica3.csv");
        System.out.println("✅ Datos CSV: datos_practica3.csv");
        
        // Reporte de casos específicos
        generarReporteCasosEspecificos();
        System.out.println("✅ Reporte específico: casos_especificos_reporte.txt");
    }
    
    /**
     * Genera todas las gráficas necesarias
     */
    private void generarGraficas(List<PerformanceResult> resultados) {
        System.out.println("📈 PASO 4: Generando gráficas...");
        
        visualizador.generarGraficaTiempo(resultados, "practica3_tiempo.png");
        System.out.println("✅ Gráfica de tiempo: practica3_tiempo.png");
        
        visualizador.generarGraficaMemoria(resultados, "practica3_memoria.png");
        System.out.println("✅ Gráfica de memoria: practica3_memoria.png");
        
        visualizador.generarGraficaBarras(resultados, "practica3_comparacion.png");
        System.out.println("✅ Gráfica comparativa: practica3_comparacion.png");
        
        visualizador.generarGraficaEscalabilidad(resultados, "practica3_escalabilidad.png");
        System.out.println("✅ Gráfica de escalabilidad: practica3_escalabilidad.png");
    }
    
    /**
     * Genera análisis estadístico detallado
     */
    private void generarAnalisisEstadistico(List<PerformanceResult> resultados) {
        System.out.println("📊 PASO 5: Generando análisis estadístico...");
        
        analizador.analizarRendimiento(resultados, "analisis_practica3.txt");
        System.out.println("✅ Análisis estadístico: analisis_practica3.txt");
    }
    
    /**
     * Muestra resultados de un caso específico
     */
    private void mostrarResultadosCaso(int m, int n, int t, List<PerformanceResult> resultados) {
        System.out.println("Resultados:");
        
        for (PerformanceResult resultado : resultados) {
            String estado = resultado.isTimeoutExcedido() ? "TIMEOUT" : 
                           resultado.isSolucionCorrecta() ? "ÉXITO" : "ERROR";
            
            if (resultado.isTimeoutExcedido()) {
                System.out.printf("  %s: %s (>3s)%n", 
                    resultado.getAlgoritmo(), estado);
            } else {
                System.out.printf("  %s: %s (%.6fs) - %d burgers (%d M, %d N)%n",
                    resultado.getAlgoritmo(), estado, 
                    resultado.getTiempoEjecucionSegundos(),
                    resultado.getTotalBurgers(),
                    resultado.getBurgersM(),
                    resultado.getBurgersN());
            }
        }
    }
    
    /**
     * Guarda resultados específicos en archivo separado
     */
    private void guardarResultadosEspecificos(List<PerformanceResult> resultados) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("resultados_casos_especificos.csv"))) {
            // Encabezado
            writer.println("m,n,t,algoritmo,tiempo_segundos,timeout,total_burgers,burgers_m,burgers_n,tiempo_restante");
            
            // Datos
            for (PerformanceResult resultado : resultados) {
                writer.printf("%d,%d,%d,%s,%.9f,%b,%d,%d,%d,%d%n",
                    resultado.getM(), resultado.getN(), resultado.getT(),
                    resultado.getAlgoritmo(), resultado.getTiempoEjecucionSegundos(),
                    resultado.isTimeoutExcedido(),
                    resultado.getTotalBurgers(), resultado.getBurgersM(),
                    resultado.getBurgersN(), resultado.getTiempoRestante());
            }
        }
    }
    
    /**
     * Genera reporte específico para los casos requeridos
     */
    private void generarReporteCasosEspecificos() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("casos_especificos_reporte.txt"))) {
            writer.println("=== ANÁLISIS DE CASOS ESPECÍFICOS - PRÁCTICA 3 ===");
            writer.println("Autores: José y David");
            writer.println("Fecha: " + new Date());
            writer.println();
            
            writer.println("CASOS ANALIZADOS:");
            writer.println("- m=4, n=9, t=10   (Caso muy pequeño)");
            writer.println("- m=4, n=9, t=100  (Caso pequeño)");
            writer.println("- m=4, n=9, t=1000 (Caso mediano)");
            writer.println("- m=4, n=9, t=10000 (Caso grande)");
            writer.println();
            
            // Leer y analizar resultados específicos
            analizarCasosEspecificos(writer);
            
            writer.println();
            writer.println("=== CONCLUSIONES ===");
            writer.println("1. El algoritmo recursivo es viable solo para t=10");
            writer.println("2. La memoización muestra excelente rendimiento hasta t=1000");
            writer.println("3. La programación dinámica maneja eficientemente todos los casos");
            writer.println("4. La transición entre algoritmos es crucial para optimizar rendimiento");
        }
    }
    
    /**
     * Analiza los casos específicos en detalle
     */
    private void analizarCasosEspecificos(PrintWriter writer) {
        try (BufferedReader reader = new BufferedReader(new FileReader("resultados_casos_especificos.csv"))) {
            String linea = reader.readLine(); // Saltar encabezado
            
            Map<Integer, Map<String, PerformanceResult>> resultadosPorT = new HashMap<>();
            
            // Leer datos
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length >= 10) {
                    int t = Integer.parseInt(campos[2]);
                    String algoritmo = campos[3];
                    
                    PerformanceResult resultado = new PerformanceResult(algoritmo, 4, 9, t);
                    resultado.setTiempoEjecucion((long)(Double.parseDouble(campos[4]) * 1_000_000_000));
                    resultado.setTimeoutExcedido(Boolean.parseBoolean(campos[5]));
                    
                    if (!resultado.isTimeoutExcedido()) {
                        resultado.setResultadoProblema(
                            Integer.parseInt(campos[6]),
                            Integer.parseInt(campos[7]),
                            Integer.parseInt(campos[8]),
                            Integer.parseInt(campos[9])
                        );
                        resultado.setSolucionCorrecta(true);
                    }
                    
                    resultadosPorT.computeIfAbsent(t, k -> new HashMap<>()).put(algoritmo, resultado);
                }
            }
            
            // Analizar cada caso
            for (int t : Arrays.asList(10, 100, 1000, 10000)) {
                writer.printf("--- CASO: m=4, n=9, t=%d ---%n", t);
                
                Map<String, PerformanceResult> resultados = resultadosPorT.get(t);
                if (resultados == null) continue;
                
                // Solución óptima esperada
                int optimalTotal = calcularSolucionOptima(4, 9, t);
                writer.printf("Solución óptima esperada: %d hamburguesas%n", optimalTotal);
                
                for (String algoritmo : Arrays.asList("Recursivo", "Memoizacion", "Dinamico")) {
                    PerformanceResult resultado = resultados.get(algoritmo);
                    if (resultado == null) continue;
                    
                    if (resultado.isTimeoutExcedido()) {
                        writer.printf("%s: TIMEOUT (>3s)%n", algoritmo);
                    } else {
                        writer.printf("%s: %.6fs - %d burgers (%d M + %d N) resto=%d%n",
                            algoritmo, resultado.getTiempoEjecucionSegundos(),
                            resultado.getTotalBurgers(), resultado.getBurgersM(),
                            resultado.getBurgersN(), resultado.getTiempoRestante());
                        
                        // Verificar optimalidad
                        if (resultado.getTotalBurgers() == optimalTotal) {
                            writer.println("    ✅ Solución ÓPTIMA");
                        } else {
                            writer.printf("    ⚠️  Subóptima (diferencia: %d)%n", 
                                optimalTotal - resultado.getTotalBurgers());
                        }
                    }
                }
                writer.println();
            }
            
        } catch (IOException e) {
            writer.println("Error leyendo resultados específicos: " + e.getMessage());
        }
    }
    
    /**
     * Calcula la solución óptima usando programación dinámica
     */
    private int calcularSolucionOptima(int m, int n, int t) {
        try {
            BurgerDinamico.Resultado resultado = BurgerDinamico.resolver(m, n, t);
            return resultado.totalBurgers;
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Muestra resumen final de la ejecución
     */
    private void mostrarResumenFinal(List<PerformanceResult> especificos, List<PerformanceResult> completos) {
        System.out.println();
        System.out.println("🎉 PASO 6: RESUMEN FINAL - PRÁCTICA 3");
        System.out.println("=====================================");
        
        // Estadísticas generales
        long exitosos = completos.stream().mapToLong(r -> !r.isTimeoutExcedido() && r.isSolucionCorrecta() ? 1 : 0).sum();
        long timeouts = completos.stream().mapToLong(r -> r.isTimeoutExcedido() ? 1 : 0).sum();
        
        System.out.printf("📊 Estadísticas generales:%n");
        System.out.printf("   Total casos ejecutados: %d%n", completos.size());
        System.out.printf("   Casos exitosos: %d (%.1f%%)%n", exitosos, (exitosos * 100.0) / completos.size());
        System.out.printf("   Timeouts: %d (%.1f%%)%n", timeouts, (timeouts * 100.0) / completos.size());
        
        // Resultados de casos específicos
        System.out.println();
        System.out.printf("🎯 Casos específicos (m=4, n=9):%n");
        
        for (int[] caso : CASOS_ESPECIFICOS) {
            int t = caso[2];
            List<PerformanceResult> casoResultados = especificos.stream()
                .filter(r -> r.getT() == t)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            
            System.out.printf("   t=%d: ", t);
            
            for (PerformanceResult resultado : casoResultados) {
                String estado = resultado.isTimeoutExcedido() ? "TIMEOUT" : "OK";
                System.out.printf("%s=%s ", resultado.getAlgoritmo().substring(0, 3), estado);
            }
            System.out.println();
        }
        
        // Archivos generados
        System.out.println();
        System.out.printf("📁 Archivos generados:%n");
        System.out.printf("   Reportes:%n");
        System.out.printf("   ├── reporte_practica3.txt (reporte principal)%n");
        System.out.printf("   ├── analisis_practica3.txt (análisis estadístico)%n");
        System.out.printf("   └── casos_especificos_reporte.txt (casos específicos)%n");
        System.out.printf("   Datos:%n");
        System.out.printf("   ├── datos_practica3.csv (todos los resultados)%n");
        System.out.printf("   └── resultados_casos_especificos.csv (casos específicos)%n");
        System.out.printf("   Gráficas:%n");
        System.out.printf("   ├── practica3_tiempo.png%n");
        System.out.printf("   ├── practica3_memoria.png%n");
        System.out.printf("   ├── practica3_comparacion.png%n");
        System.out.printf("   └── practica3_escalabilidad.png%n");
        
        System.out.println();
        System.out.println("✅ ANÁLISIS COMPLETO FINALIZADO");
        System.out.println("📝 Revise los archivos generados para el análisis detallado");
    }
    
    /**
     * Ejecuta solo los casos específicos (modo rápido)
     */
    public void ejecutarSoloCasosEspecificos() {
        System.out.println("=== EJECUCIÓN RÁPIDA - CASOS ESPECÍFICOS ===");
        
        List<PerformanceResult> resultados = ejecutarCasosEspecificos();
        
        try {
            generarReporteCasosEspecificos();
            System.out.println("✅ Reporte rápido generado: casos_especificos_reporte.txt");
        } catch (IOException e) {
            System.err.println("Error generando reporte: " + e.getMessage());
        }
    }
    
    /**
     * Programa principal
     */
    public static void main(String[] args) {
        Ejecutor ejecutor = new Ejecutor();
        
        // Verificar argumentos
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "--rapido":
                case "-r":
                    ejecutor.ejecutarSoloCasosEspecificos();
                    break;
                case "--completo":
                case "-c":
                default:
                    ejecutor.ejecutarAnalisisCompleto();
                    break;
                case "--help":
                case "-h":
                    mostrarAyuda();
                    break;
            }
        } else {
            // Por defecto ejecutar análisis completo
            ejecutor.ejecutarAnalisisCompleto();
        }
    }
    
    /**
     * Muestra información de ayuda
     */
    private static void mostrarAyuda() {
        System.out.println("=== EJECUTOR DE PRÁCTICA 3 - AYUDA ===");
        System.out.println();
        System.out.println("Uso: java Ejecutor [opción]");
        System.out.println();
        System.out.println("Opciones:");
        System.out.println("  --completo, -c    Ejecuta análisis completo (por defecto)");
        System.out.println("  --rapido, -r      Ejecuta solo casos específicos");
        System.out.println("  --help, -h        Muestra esta ayuda");
        System.out.println();
        System.out.println("Casos específicos analizados:");
        System.out.println("  - m=4, n=9, t=10");
        System.out.println("  - m=4, n=9, t=100");
        System.out.println("  - m=4, n=9, t=1000");
        System.out.println("  - m=4, n=9, t=10000");
        System.out.println();
        System.out.println("Autores: José y David");
    }
}