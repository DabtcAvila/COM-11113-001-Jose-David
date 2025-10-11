import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Analizador estadístico avanzado para resultados de rendimiento
 * Genera análisis detallado de complejidad, eficiencia y escalabilidad
 * 
 * @author José y David
 */
public class AnalizadorRendimiento {
    
    /**
     * Clase para almacenar estadísticas descriptivas
     */
    public static class EstadisticasDescriptivas {
        private double media;
        private double mediana;
        private double desviacionEstandar;
        private double minimo;
        private double maximo;
        private double q1; // Primer cuartil
        private double q3; // Tercer cuartil
        private int n; // Tamaño de muestra
        
        public EstadisticasDescriptivas(List<Double> datos) {
            if (datos.isEmpty()) {
                throw new IllegalArgumentException("Lista de datos vacía");
            }
            
            this.n = datos.size();
            List<Double> datosOrdenados = datos.stream().sorted().collect(Collectors.toList());
            
            // Media
            this.media = datos.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            
            // Mediana
            if (n % 2 == 0) {
                this.mediana = (datosOrdenados.get(n/2 - 1) + datosOrdenados.get(n/2)) / 2.0;
            } else {
                this.mediana = datosOrdenados.get(n/2);
            }
            
            // Cuartiles
            this.q1 = calcularCuartil(datosOrdenados, 0.25);
            this.q3 = calcularCuartil(datosOrdenados, 0.75);
            
            // Mínimo y máximo
            this.minimo = datosOrdenados.get(0);
            this.maximo = datosOrdenados.get(n - 1);
            
            // Desviación estándar
            double sumaCuadrados = datos.stream()
                .mapToDouble(x -> Math.pow(x - media, 2))
                .sum();
            this.desviacionEstandar = Math.sqrt(sumaCuadrados / (n - 1));
        }
        
        private double calcularCuartil(List<Double> datosOrdenados, double percentil) {
            double pos = percentil * (datosOrdenados.size() - 1);
            int indiceInferior = (int) Math.floor(pos);
            int indiceSuperior = (int) Math.ceil(pos);
            
            if (indiceInferior == indiceSuperior) {
                return datosOrdenados.get(indiceInferior);
            } else {
                double peso = pos - indiceInferior;
                return datosOrdenados.get(indiceInferior) * (1 - peso) + 
                       datosOrdenados.get(indiceSuperior) * peso;
            }
        }
        
        // Getters
        public double getMedia() { return media; }
        public double getMediana() { return mediana; }
        public double getDesviacionEstandar() { return desviacionEstandar; }
        public double getMinimo() { return minimo; }
        public double getMaximo() { return maximo; }
        public double getQ1() { return q1; }
        public double getQ3() { return q3; }
        public int getN() { return n; }
        
        public double getCoeficienteVariacion() {
            return media != 0 ? (desviacionEstandar / media) * 100 : 0;
        }
        
        public double getRangoIntercuartil() {
            return q3 - q1;
        }
    }
    
    /**
     * Clase para análisis de complejidad algorítmica
     */
    public static class AnalisisComplejidad {
        private String algoritmo;
        private double factorCrecimiento;
        private double coeficienteCorrelacion;
        private String complejidadEstimada;
        private double bondadAjuste;
        
        public AnalisisComplejidad(String algoritmo, List<PerformanceResult> resultados) {
            this.algoritmo = algoritmo;
            analizarComplejidad(resultados);
        }
        
        private void analizarComplejidad(List<PerformanceResult> resultados) {
            if (resultados.size() < 3) {
                this.complejidadEstimada = "Datos insuficientes";
                return;
            }
            
            // Filtrar resultados válidos y ordenar por t
            List<PerformanceResult> validos = resultados.stream()
                .filter(r -> !r.isTimeoutExcedido() && r.getTiempoEjecucionSegundos() > 0)
                .sorted(Comparator.comparingInt(PerformanceResult::getT))
                .collect(Collectors.toList());
            
            if (validos.size() < 3) {
                this.complejidadEstimada = "Datos insuficientes (timeouts)";
                return;
            }
            
            // Probar diferentes modelos de complejidad
            double mejorAjuste = -1;
            String mejorModelo = "Desconocido";
            
            // Modelo lineal: O(t)
            double ajusteLineal = calcularAjusteLineal(validos);
            if (ajusteLineal > mejorAjuste) {
                mejorAjuste = ajusteLineal;
                mejorModelo = "O(t) - Lineal";
            }
            
            // Modelo cuadrático: O(t²)
            double ajusteCuadratico = calcularAjusteCuadratico(validos);
            if (ajusteCuadratico > mejorAjuste) {
                mejorAjuste = ajusteCuadratico;
                mejorModelo = "O(t²) - Cuadrático";
            }
            
            // Modelo exponencial: O(2^t)
            double ajusteExponencial = calcularAjusteExponencial(validos);
            if (ajusteExponencial > mejorAjuste) {
                mejorAjuste = ajusteExponencial;
                mejorModelo = "O(2^t) - Exponencial";
            }
            
            this.complejidadEstimada = mejorModelo;
            this.bondadAjuste = mejorAjuste;
            
            // Calcular factor de crecimiento promedio
            calcularFactorCrecimiento(validos);
        }
        
        private double calcularAjusteLineal(List<PerformanceResult> resultados) {
            return calcularCorrelacion(
                resultados.stream().mapToDouble(r -> (double) r.getT()).toArray(),
                resultados.stream().mapToDouble(PerformanceResult::getTiempoEjecucionSegundos).toArray()
            );
        }
        
        private double calcularAjusteCuadratico(List<PerformanceResult> resultados) {
            return calcularCorrelacion(
                resultados.stream().mapToDouble(r -> Math.pow(r.getT(), 2)).toArray(),
                resultados.stream().mapToDouble(PerformanceResult::getTiempoEjecucionSegundos).toArray()
            );
        }
        
        private double calcularAjusteExponencial(List<PerformanceResult> resultados) {
            try {
                return calcularCorrelacion(
                    resultados.stream().mapToDouble(r -> (double) r.getT()).toArray(),
                    resultados.stream().mapToDouble(r -> Math.log(r.getTiempoEjecucionSegundos())).toArray()
                );
            } catch (Exception e) {
                return -1; // Error en logaritmo
            }
        }
        
        private double calcularCorrelacion(double[] x, double[] y) {
            if (x.length != y.length || x.length < 2) return -1;
            
            double mediaX = Arrays.stream(x).average().orElse(0);
            double mediaY = Arrays.stream(y).average().orElse(0);
            
            double numerador = 0;
            double denominadorX = 0;
            double denominadorY = 0;
            
            for (int i = 0; i < x.length; i++) {
                double diffX = x[i] - mediaX;
                double diffY = y[i] - mediaY;
                
                numerador += diffX * diffY;
                denominadorX += diffX * diffX;
                denominadorY += diffY * diffY;
            }
            
            if (denominadorX == 0 || denominadorY == 0) return 0;
            
            return Math.abs(numerador / Math.sqrt(denominadorX * denominadorY));
        }
        
        private void calcularFactorCrecimiento(List<PerformanceResult> resultados) {
            if (resultados.size() < 2) {
                this.factorCrecimiento = 1.0;
                return;
            }
            
            List<Double> ratios = new ArrayList<>();
            
            for (int i = 1; i < resultados.size(); i++) {
                PerformanceResult anterior = resultados.get(i - 1);
                PerformanceResult actual = resultados.get(i);
                
                if (anterior.getTiempoEjecucionSegundos() > 0) {
                    double ratio = actual.getTiempoEjecucionSegundos() / anterior.getTiempoEjecucionSegundos();
                    ratios.add(ratio);
                }
            }
            
            this.factorCrecimiento = ratios.stream().mapToDouble(Double::doubleValue).average().orElse(1.0);
        }
        
        // Getters
        public String getAlgoritmo() { return algoritmo; }
        public double getFactorCrecimiento() { return factorCrecimiento; }
        public String getComplejidadEstimada() { return complejidadEstimada; }
        public double getBondadAjuste() { return bondadAjuste; }
    }
    
    /**
     * Realiza análisis completo de rendimiento
     */
    public void analizarRendimiento(List<PerformanceResult> resultados, String archivoSalida) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivoSalida))) {
            writer.println("=== ANÁLISIS ESTADÍSTICO DETALLADO DE RENDIMIENTO ===");
            writer.println("Fecha: " + new Date());
            writer.println("Resultados analizados: " + resultados.size());
            writer.println();
            
            // Agrupar por algoritmo
            Map<String, List<PerformanceResult>> porAlgoritmo = resultados.stream()
                .collect(Collectors.groupingBy(PerformanceResult::getAlgoritmo));
            
            // Análisis por algoritmo
            for (Map.Entry<String, List<PerformanceResult>> entry : porAlgoritmo.entrySet()) {
                String algoritmo = entry.getKey();
                List<PerformanceResult> datos = entry.getValue();
                
                writer.println("=== ALGORITMO: " + algoritmo.toUpperCase() + " ===");
                
                analizarAlgoritmo(writer, algoritmo, datos);
                writer.println();
            }
            
            // Análisis comparativo
            writer.println("=== ANÁLISIS COMPARATIVO ===");
            analizarComparativo(writer, porAlgoritmo);
            
            // Recomendaciones
            writer.println("=== RECOMENDACIONES DE USO ===");
            generarRecomendaciones(writer, porAlgoritmo);
            
            System.out.println("Análisis estadístico guardado en: " + archivoSalida);
            
        } catch (IOException e) {
            System.err.println("Error generando análisis: " + e.getMessage());
        }
    }
    
    private void analizarAlgoritmo(PrintWriter writer, String algoritmo, List<PerformanceResult> datos) {
        // Filtrar datos válidos
        List<PerformanceResult> validos = datos.stream()
            .filter(r -> !r.isTimeoutExcedido())
            .collect(Collectors.toList());
        
        List<PerformanceResult> timeouts = datos.stream()
            .filter(PerformanceResult::isTimeoutExcedido)
            .collect(Collectors.toList());
        
        writer.printf("Casos totales: %d | Exitosos: %d | Timeouts: %d%n", 
                     datos.size(), validos.size(), timeouts.size());
        
        if (validos.isEmpty()) {
            writer.println("⚠️  Todos los casos resultaron en timeout");
            return;
        }
        
        // Estadísticas de tiempo
        List<Double> tiempos = validos.stream()
            .map(PerformanceResult::getTiempoEjecucionSegundos)
            .collect(Collectors.toList());
        
        EstadisticasDescriptivas estatTiempo = new EstadisticasDescriptivas(tiempos);
        
        writer.println("\n--- Estadísticas de Tiempo de Ejecución ---");
        writer.printf("Media: %.6f segundos%n", estatTiempo.getMedia());
        writer.printf("Mediana: %.6f segundos%n", estatTiempo.getMediana());
        writer.printf("Desv. Estándar: %.6f segundos%n", estatTiempo.getDesviacionEstandar());
        writer.printf("Mínimo: %.6f segundos%n", estatTiempo.getMinimo());
        writer.printf("Máximo: %.6f segundos%n", estatTiempo.getMaximo());
        writer.printf("Coef. Variación: %.2f%%%n", estatTiempo.getCoeficienteVariacion());
        writer.printf("Rango Intercuartil: %.6f segundos%n", estatTiempo.getRangoIntercuartil());
        
        // Estadísticas de memoria
        List<Double> memorias = validos.stream()
            .map(r -> r.getMemoriaUsada() / 1024.0) // KB
            .collect(Collectors.toList());
        
        if (!memorias.isEmpty() && memorias.stream().anyMatch(m -> m > 0)) {
            EstadisticasDescriptivas estatMemoria = new EstadisticasDescriptivas(memorias);
            
            writer.println("\n--- Estadísticas de Uso de Memoria ---");
            writer.printf("Media: %.2f KB%n", estatMemoria.getMedia());
            writer.printf("Mediana: %.2f KB%n", estatMemoria.getMediana());
            writer.printf("Mínimo: %.2f KB%n", estatMemoria.getMinimo());
            writer.printf("Máximo: %.2f KB%n", estatMemoria.getMaximo());
        }
        
        // Análisis de complejidad
        AnalisisComplejidad complejidad = new AnalisisComplejidad(algoritmo, validos);
        
        writer.println("\n--- Análisis de Complejidad ---");
        writer.printf("Complejidad estimada: %s%n", complejidad.getComplejidadEstimada());
        writer.printf("Factor de crecimiento promedio: %.2fx%n", complejidad.getFactorCrecimiento());
        writer.printf("Bondad de ajuste: %.3f%n", complejidad.getBondadAjuste());
        
        // Análisis por rangos de tamaño
        analizarPorRangos(writer, validos);
        
        // Casos problemáticos
        if (!timeouts.isEmpty()) {
            writer.println("\n--- Casos con Timeout ---");
            writer.printf("Total timeouts: %d%n", timeouts.size());
            
            int minT = timeouts.stream().mapToInt(PerformanceResult::getT).min().orElse(0);
            int maxT = timeouts.stream().mapToInt(PerformanceResult::getT).max().orElse(0);
            
            writer.printf("Rango de t con timeout: %d - %d%n", minT, maxT);
            
            if (minT > 0) {
                writer.printf("Límite práctico estimado: t < %d%n", minT);
            }
        }
    }
    
    private void analizarPorRangos(PrintWriter writer, List<PerformanceResult> datos) {
        writer.println("\n--- Rendimiento por Rangos ---");
        
        Map<String, List<PerformanceResult>> porRango = datos.stream()
            .collect(Collectors.groupingBy(this::determinarRango));
        
        for (String rango : Arrays.asList("Muy Pequeño", "Pequeño", "Mediano", "Grande", "Muy Grande")) {
            if (porRango.containsKey(rango)) {
                List<PerformanceResult> enRango = porRango.get(rango);
                
                double tiempoPromedio = enRango.stream()
                    .mapToDouble(PerformanceResult::getTiempoEjecucionSegundos)
                    .average().orElse(0.0);
                
                int minT = enRango.stream().mapToInt(PerformanceResult::getT).min().orElse(0);
                int maxT = enRango.stream().mapToInt(PerformanceResult::getT).max().orElse(0);
                
                writer.printf("%s (t: %d-%d): %.6fs promedio (%d casos)%n",
                             rango, minT, maxT, tiempoPromedio, enRango.size());
            }
        }
    }
    
    private String determinarRango(PerformanceResult resultado) {
        int t = resultado.getT();
        if (t <= 10) return "Muy Pequeño";
        if (t <= 50) return "Pequeño";
        if (t <= 500) return "Mediano";
        if (t <= 5000) return "Grande";
        return "Muy Grande";
    }
    
    private void analizarComparativo(PrintWriter writer, Map<String, List<PerformanceResult>> porAlgoritmo) {
        writer.println("\n--- Comparación de Eficiencia ---");
        
        // Calcular eficiencia relativa
        Map<String, Double> eficienciaRelativa = new HashMap<>();
        double mejorTiempoPromedio = Double.MAX_VALUE;
        
        for (Map.Entry<String, List<PerformanceResult>> entry : porAlgoritmo.entrySet()) {
            String algoritmo = entry.getKey();
            List<PerformanceResult> datos = entry.getValue();
            
            double tiempoPromedio = datos.stream()
                .filter(r -> !r.isTimeoutExcedido())
                .mapToDouble(PerformanceResult::getTiempoEjecucionSegundos)
                .average().orElse(Double.MAX_VALUE);
            
            eficienciaRelativa.put(algoritmo, tiempoPromedio);
            mejorTiempoPromedio = Math.min(mejorTiempoPromedio, tiempoPromedio);
        }
        
        // Mostrar ranking
        List<Map.Entry<String, Double>> ranking = eficienciaRelativa.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(Collectors.toList());
        
        writer.println("Ranking de eficiencia (menor tiempo promedio):");
        for (int i = 0; i < ranking.size(); i++) {
            Map.Entry<String, Double> entry = ranking.get(i);
            double factor = entry.getValue() / mejorTiempoPromedio;
            
            writer.printf("%d. %s: %.6fs (%.2fx más lento que el mejor)%n",
                         i + 1, entry.getKey(), entry.getValue(), factor);
        }
        
        // Análisis de dominancia por rango
        writer.println("\n--- Dominancia por Rango de Problema ---");
        analizarDominancia(writer, porAlgoritmo);
    }
    
    private void analizarDominancia(PrintWriter writer, Map<String, List<PerformanceResult>> porAlgoritmo) {
        String[] rangos = {"Muy Pequeño", "Pequeño", "Mediano", "Grande", "Muy Grande"};
        
        for (String rango : rangos) {
            Map<String, Double> tiemposPorAlgoritmo = new HashMap<>();
            
            for (Map.Entry<String, List<PerformanceResult>> entry : porAlgoritmo.entrySet()) {
                String algoritmo = entry.getKey();
                List<PerformanceResult> datos = entry.getValue();
                
                double tiempoPromedio = datos.stream()
                    .filter(r -> !r.isTimeoutExcedido())
                    .filter(r -> determinarRango(r).equals(rango))
                    .mapToDouble(PerformanceResult::getTiempoEjecucionSegundos)
                    .average().orElse(Double.MAX_VALUE);
                
                if (tiempoPromedio < Double.MAX_VALUE) {
                    tiemposPorAlgoritmo.put(algoritmo, tiempoPromedio);
                }
            }
            
            if (!tiemposPorAlgoritmo.isEmpty()) {
                String mejor = tiemposPorAlgoritmo.entrySet().stream()
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("N/A");
                
                writer.printf("%s: Mejor algoritmo = %s%n", rango, mejor);
            }
        }
    }
    
    private void generarRecomendaciones(PrintWriter writer, Map<String, List<PerformanceResult>> porAlgoritmo) {
        writer.println();
        writer.println("Basado en el análisis estadístico:");
        writer.println();
        
        // Recomendaciones generales
        writer.println("🔍 RECOMENDACIONES DE USO:");
        writer.println();
        
        // Analizar cada algoritmo
        for (String algoritmo : Arrays.asList("Recursivo", "Memoizacion", "Dinamico")) {
            if (!porAlgoritmo.containsKey(algoritmo)) continue;
            
            List<PerformanceResult> datos = porAlgoritmo.get(algoritmo);
            List<PerformanceResult> exitosos = datos.stream()
                .filter(r -> !r.isTimeoutExcedido())
                .collect(Collectors.toList());
            
            writer.printf("• %s:%n", algoritmo);
            
            if (exitosos.isEmpty()) {
                writer.println("  ❌ No recomendado - Todos los casos resultan en timeout");
            } else {
                int maxT = exitosos.stream().mapToInt(PerformanceResult::getT).max().orElse(0);
                double tiempoPromedio = exitosos.stream()
                    .mapToDouble(PerformanceResult::getTiempoEjecucionSegundos)
                    .average().orElse(0.0);
                
                if (algoritmo.equals("Recursivo")) {
                    writer.printf("  ✅ Recomendado para t ≤ %d (tiempo promedio: %.6fs)%n", 
                                 Math.min(maxT, 30), tiempoPromedio);
                    writer.println("  📝 Ideal para casos pequeños y pruebas de concepto");
                } else if (algoritmo.equals("Memoizacion")) {
                    writer.printf("  ✅ Recomendado para 30 < t ≤ %d (tiempo promedio: %.6fs)%n", 
                                 Math.min(maxT, 10000), tiempoPromedio);
                    writer.println("  📝 Excelente balance entre eficiencia y simplicidad");
                } else if (algoritmo.equals("Dinamico")) {
                    writer.printf("  ✅ Recomendado para t > 1000 (tiempo promedio: %.6fs)%n", tiempoPromedio);
                    writer.println("  📝 Mejor opción para casos grandes y producción");
                }
            }
            writer.println();
        }
        
        writer.println("📊 CONCLUSIONES ESTADÍSTICAS:");
        writer.println("• La transición entre algoritmos es crucial para optimizar rendimiento");
        writer.println("• Los timeouts indican límites prácticos claros para cada enfoque");
        writer.println("• La variabilidad del tiempo sugiere optimizaciones específicas por rango");
        writer.println("• El uso de memoria es consistente con la complejidad teórica esperada");
    }
    
    /**
     * Programa principal para análisis estadístico
     */
    public static void main(String[] args) {
        try {
            System.out.println("=== ANALIZADOR ESTADÍSTICO DE RENDIMIENTO ===");
            
            // Leer datos
            List<PerformanceResult> resultados = leerCSV("resultados_comparacion.csv");
            
            if (resultados.isEmpty()) {
                System.out.println("No se encontraron datos. Ejecute primero AlgorithmComparator.");
                return;
            }
            
            System.out.println("Datos cargados: " + resultados.size() + " resultados");
            
            // Realizar análisis
            AnalizadorRendimiento analizador = new AnalizadorRendimiento();
            analizador.analizarRendimiento(resultados, "analisis_estadistico.txt");
            
            System.out.println("\n=== ANÁLISIS COMPLETADO ===");
            System.out.println("Archivo generado: analisis_estadistico.txt");
            
        } catch (Exception e) {
            System.err.println("Error en análisis: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Lee resultados desde archivo CSV
     */
    private static List<PerformanceResult> leerCSV(String nombreArchivo) {
        List<PerformanceResult> resultados = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea = reader.readLine(); // Saltar encabezado
            
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length >= 15) {
                    PerformanceResult resultado = new PerformanceResult(
                        campos[0], // algoritmo
                        Integer.parseInt(campos[1]), // m
                        Integer.parseInt(campos[2]), // n
                        Integer.parseInt(campos[3])  // t
                    );
                    
                    resultado.setTiempoEjecucion((long)(Double.parseDouble(campos[4]) * 1_000_000_000));
                    resultado.setMemoriaUsada(Long.parseLong(campos[5]));
                    resultado.setTimeoutExcedido(Boolean.parseBoolean(campos[6]));
                    resultado.setSolucionCorrecta(Boolean.parseBoolean(campos[7]));
                    
                    if (!resultado.isTimeoutExcedido()) {
                        resultado.setResultadoProblema(
                            Integer.parseInt(campos[8]),  // total_burgers
                            Integer.parseInt(campos[9]),  // burgers_m
                            Integer.parseInt(campos[10]), // burgers_n
                            Integer.parseInt(campos[11])  // tiempo_restante
                        );
                    }
                    
                    resultados.add(resultado);
                }
            }
            
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error leyendo CSV: " + e.getMessage());
        }
        
        return resultados;
    }
}