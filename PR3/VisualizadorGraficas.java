import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Visualizador de gráficas para comparación de algoritmos
 * Genera gráficas de rendimiento usando bibliotecas estándar de Java
 * 
 * @author José y David
 */
public class VisualizadorGraficas {
    
    private static final int ANCHO_GRAFICA = 800;
    private static final int ALTO_GRAFICA = 600;
    private static final int MARGEN = 80;
    private static final int MARGEN_SUPERIOR = 60;
    
    // Colores para cada algoritmo
    private static final Color COLOR_RECURSIVO = new Color(220, 20, 60);     // Crimson
    private static final Color COLOR_MEMOIZACION = new Color(30, 144, 255);  // DodgerBlue
    private static final Color COLOR_DINAMICO = new Color(50, 205, 50);      // LimeGreen
    
    /**
     * Genera gráfica de tiempo de ejecución vs tamaño del problema
     */
    public void generarGraficaTiempo(List<PerformanceResult> resultados, String nombreArchivo) {
        try {
            BufferedImage imagen = new BufferedImage(ANCHO_GRAFICA, ALTO_GRAFICA, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = imagen.createGraphics();
            
            // Configurar renderizado de alta calidad
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Fondo blanco
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, ANCHO_GRAFICA, ALTO_GRAFICA);
            
            // Título
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String titulo = "Tiempo de Ejecución vs Tamaño del Problema (t)";
            FontMetrics fm = g2d.getFontMetrics();
            int anchoTitulo = fm.stringWidth(titulo);
            g2d.drawString(titulo, (ANCHO_GRAFICA - anchoTitulo) / 2, 30);
            
            // Agrupar por algoritmo
            Map<String, List<PerformanceResult>> porAlgoritmo = agruparPorAlgoritmo(resultados);
            
            // Calcular rangos
            int maxT = resultados.stream().mapToInt(PerformanceResult::getT).max().orElse(1);
            double maxTiempo = resultados.stream()
                .filter(r -> !r.isTimeoutExcedido())
                .mapToDouble(PerformanceResult::getTiempoEjecucionSegundos)
                .max().orElse(1.0);
            
            // Dibujar ejes
            dibujarEjes(g2d, maxT, maxTiempo, "Tiempo (t)", "Tiempo de Ejecución (seg)");
            
            // Dibujar datos para cada algoritmo
            for (Map.Entry<String, List<PerformanceResult>> entry : porAlgoritmo.entrySet()) {
                String algoritmo = entry.getKey();
                List<PerformanceResult> datos = entry.getValue();
                
                Color color = getColorAlgoritmo(algoritmo);
                dibujarSerieLineal(g2d, datos, maxT, maxTiempo, color, true);
            }
            
            // Leyenda
            dibujarLeyenda(g2d, porAlgoritmo.keySet());
            
            g2d.dispose();
            
            // Guardar imagen
            ImageIO.write(imagen, "PNG", new File(nombreArchivo));
            System.out.println("Gráfica de tiempo guardada: " + nombreArchivo);
            
        } catch (IOException e) {
            System.err.println("Error generando gráfica de tiempo: " + e.getMessage());
        }
    }
    
    /**
     * Genera gráfica de uso de memoria vs tamaño del problema
     */
    public void generarGraficaMemoria(List<PerformanceResult> resultados, String nombreArchivo) {
        try {
            BufferedImage imagen = new BufferedImage(ANCHO_GRAFICA, ALTO_GRAFICA, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = imagen.createGraphics();
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, ANCHO_GRAFICA, ALTO_GRAFICA);
            
            // Título
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String titulo = "Uso de Memoria vs Tamaño del Problema (t)";
            FontMetrics fm = g2d.getFontMetrics();
            int anchoTitulo = fm.stringWidth(titulo);
            g2d.drawString(titulo, (ANCHO_GRAFICA - anchoTitulo) / 2, 30);
            
            Map<String, List<PerformanceResult>> porAlgoritmo = agruparPorAlgoritmo(resultados);
            
            int maxT = resultados.stream().mapToInt(PerformanceResult::getT).max().orElse(1);
            double maxMemoria = resultados.stream()
                .mapToDouble(r -> r.getMemoriaUsada() / 1024.0) // KB
                .max().orElse(1.0);
            
            dibujarEjes(g2d, maxT, maxMemoria, "Tiempo (t)", "Memoria (KB)");
            
            for (Map.Entry<String, List<PerformanceResult>> entry : porAlgoritmo.entrySet()) {
                String algoritmo = entry.getKey();
                List<PerformanceResult> datos = entry.getValue();
                
                Color color = getColorAlgoritmo(algoritmo);
                dibujarSerieMemoria(g2d, datos, maxT, maxMemoria, color);
            }
            
            dibujarLeyenda(g2d, porAlgoritmo.keySet());
            
            g2d.dispose();
            ImageIO.write(imagen, "PNG", new File(nombreArchivo));
            System.out.println("Gráfica de memoria guardada: " + nombreArchivo);
            
        } catch (IOException e) {
            System.err.println("Error generando gráfica de memoria: " + e.getMessage());
        }
    }
    
    /**
     * Genera gráfica de barras para comparación directa
     */
    public void generarGraficaBarras(List<PerformanceResult> resultados, String nombreArchivo) {
        try {
            BufferedImage imagen = new BufferedImage(ANCHO_GRAFICA, ALTO_GRAFICA, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = imagen.createGraphics();
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, ANCHO_GRAFICA, ALTO_GRAFICA);
            
            // Título
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String titulo = "Comparación de Rendimiento por Algoritmo";
            FontMetrics fm = g2d.getFontMetrics();
            int anchoTitulo = fm.stringWidth(titulo);
            g2d.drawString(titulo, (ANCHO_GRAFICA - anchoTitulo) / 2, 30);
            
            // Agrupar y calcular promedios
            Map<String, List<PerformanceResult>> porAlgoritmo = agruparPorAlgoritmo(resultados);
            Map<String, Double> tiempoPromedio = new HashMap<>();
            
            for (Map.Entry<String, List<PerformanceResult>> entry : porAlgoritmo.entrySet()) {
                String algoritmo = entry.getKey();
                List<PerformanceResult> datos = entry.getValue();
                
                double promedio = datos.stream()
                    .filter(r -> !r.isTimeoutExcedido())
                    .mapToDouble(PerformanceResult::getTiempoEjecucionSegundos)
                    .average().orElse(0.0);
                
                tiempoPromedio.put(algoritmo, promedio);
            }
            
            // Dibujar barras
            dibujarBarras(g2d, tiempoPromedio);
            
            g2d.dispose();
            ImageIO.write(imagen, "PNG", new File(nombreArchivo));
            System.out.println("Gráfica de barras guardada: " + nombreArchivo);
            
        } catch (IOException e) {
            System.err.println("Error generando gráfica de barras: " + e.getMessage());
        }
    }
    
    /**
     * Genera gráfica de escalabilidad específica para casos de prueba
     */
    public void generarGraficaEscalabilidad(List<PerformanceResult> resultados, String nombreArchivo) {
        try {
            BufferedImage imagen = new BufferedImage(ANCHO_GRAFICA, ALTO_GRAFICA, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = imagen.createGraphics();
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, ANCHO_GRAFICA, ALTO_GRAFICA);
            
            // Título
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String titulo = "Escalabilidad: m=4, n=9, t=10/100/1000/10000";
            FontMetrics fm = g2d.getFontMetrics();
            int anchoTitulo = fm.stringWidth(titulo);
            g2d.drawString(titulo, (ANCHO_GRAFICA - anchoTitulo) / 2, 30);
            
            // Filtrar solo casos específicos
            int[] valores_t = {10, 100, 1000, 10000};
            Map<String, double[]> tiemposPorAlgoritmo = new HashMap<>();
            
            for (String algoritmo : Arrays.asList("Recursivo", "Memoizacion", "Dinamico")) {
                double[] tiempos = new double[4];
                
                for (int i = 0; i < valores_t.length; i++) {
                    int t = valores_t[i];
                    double tiempo = resultados.stream()
                        .filter(r -> r.getAlgoritmo().equals(algoritmo) && 
                                   r.getM() == 4 && r.getN() == 9 && r.getT() == t)
                        .filter(r -> !r.isTimeoutExcedido())
                        .mapToDouble(PerformanceResult::getTiempoEjecucionSegundos)
                        .average().orElse(3.0); // 3 segundos si timeout
                    
                    tiempos[i] = tiempo;
                }
                
                tiemposPorAlgoritmo.put(algoritmo, tiempos);
            }
            
            dibujarGraficaEscalabilidad(g2d, tiemposPorAlgoritmo, valores_t);
            
            g2d.dispose();
            ImageIO.write(imagen, "PNG", new File(nombreArchivo));
            System.out.println("Gráfica de escalabilidad guardada: " + nombreArchivo);
            
        } catch (IOException e) {
            System.err.println("Error generando gráfica de escalabilidad: " + e.getMessage());
        }
    }
    
    // Métodos auxiliares
    
    private Map<String, List<PerformanceResult>> agruparPorAlgoritmo(List<PerformanceResult> resultados) {
        Map<String, List<PerformanceResult>> mapa = new HashMap<>();
        for (PerformanceResult resultado : resultados) {
            mapa.computeIfAbsent(resultado.getAlgoritmo(), k -> new ArrayList<>()).add(resultado);
        }
        return mapa;
    }
    
    private Color getColorAlgoritmo(String algoritmo) {
        switch (algoritmo.toLowerCase()) {
            case "recursivo": return COLOR_RECURSIVO;
            case "memoizacion": return COLOR_MEMOIZACION;
            case "dinamico": return COLOR_DINAMICO;
            default: return Color.BLACK;
        }
    }
    
    private void dibujarEjes(Graphics2D g2d, int maxX, double maxY, String etiquetaX, String etiquetaY) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        
        // Eje X
        g2d.drawLine(MARGEN, ALTO_GRAFICA - MARGEN, ANCHO_GRAFICA - MARGEN, ALTO_GRAFICA - MARGEN);
        
        // Eje Y
        g2d.drawLine(MARGEN, MARGEN_SUPERIOR, MARGEN, ALTO_GRAFICA - MARGEN);
        
        // Etiquetas
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        FontMetrics fm = g2d.getFontMetrics();
        
        // Etiqueta eje X
        int anchoEtiquetaX = fm.stringWidth(etiquetaX);
        g2d.drawString(etiquetaX, (ANCHO_GRAFICA - anchoEtiquetaX) / 2, ALTO_GRAFICA - 20);
        
        // Etiqueta eje Y (rotada)
        Graphics2D g2dRotado = (Graphics2D) g2d.create();
        g2dRotado.rotate(-Math.PI / 2, 20, ALTO_GRAFICA / 2);
        int anchoEtiquetaY = fm.stringWidth(etiquetaY);
        g2dRotado.drawString(etiquetaY, 20 - anchoEtiquetaY / 2, ALTO_GRAFICA / 2);
        g2dRotado.dispose();
        
        // Marcas en los ejes
        dibujarMarcasEjes(g2d, maxX, maxY);
    }
    
    private void dibujarMarcasEjes(Graphics2D g2d, int maxX, double maxY) {
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.setStroke(new BasicStroke(1));
        
        int areaGrafica = ANCHO_GRAFICA - 2 * MARGEN;
        int alturaGrafica = ALTO_GRAFICA - MARGEN - MARGEN_SUPERIOR;
        
        // Marcas eje X
        for (int i = 0; i <= 10; i++) {
            int valor = (maxX * i) / 10;
            int x = MARGEN + (areaGrafica * i) / 10;
            
            g2d.drawLine(x, ALTO_GRAFICA - MARGEN - 5, x, ALTO_GRAFICA - MARGEN + 5);
            g2d.drawString(String.valueOf(valor), x - 10, ALTO_GRAFICA - MARGEN + 20);
        }
        
        // Marcas eje Y
        for (int i = 0; i <= 10; i++) {
            double valor = (maxY * i) / 10;
            int y = ALTO_GRAFICA - MARGEN - (alturaGrafica * i) / 10;
            
            g2d.drawLine(MARGEN - 5, y, MARGEN + 5, y);
            g2d.drawString(String.format("%.3f", valor), 10, y + 5);
        }
    }
    
    private void dibujarSerieLineal(Graphics2D g2d, List<PerformanceResult> datos, 
                                   int maxX, double maxY, Color color, boolean incluirTimeout) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2));
        
        // Ordenar por t
        datos.sort(Comparator.comparingInt(PerformanceResult::getT));
        
        int areaGrafica = ANCHO_GRAFICA - 2 * MARGEN;
        int alturaGrafica = ALTO_GRAFICA - MARGEN - MARGEN_SUPERIOR;
        
        Point2D puntoAnterior = null;
        
        for (PerformanceResult resultado : datos) {
            if (resultado.isTimeoutExcedido() && !incluirTimeout) continue;
            
            int x = MARGEN + (areaGrafica * resultado.getT()) / maxX;
            double tiempo = resultado.isTimeoutExcedido() ? 3.0 : resultado.getTiempoEjecucionSegundos();
            int y = ALTO_GRAFICA - MARGEN - (int)((alturaGrafica * tiempo) / maxY);
            
            Point2D puntoActual = new Point2D.Double(x, y);
            
            // Dibujar punto
            g2d.fillOval(x - 3, y - 3, 6, 6);
            
            // Dibujar línea al punto anterior
            if (puntoAnterior != null) {
                g2d.drawLine((int)puntoAnterior.getX(), (int)puntoAnterior.getY(), x, y);
            }
            
            puntoAnterior = puntoActual;
        }
    }
    
    private void dibujarSerieMemoria(Graphics2D g2d, List<PerformanceResult> datos, 
                                    int maxX, double maxY, Color color) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2));
        
        datos.sort(Comparator.comparingInt(PerformanceResult::getT));
        
        int areaGrafica = ANCHO_GRAFICA - 2 * MARGEN;
        int alturaGrafica = ALTO_GRAFICA - MARGEN - MARGEN_SUPERIOR;
        
        Point2D puntoAnterior = null;
        
        for (PerformanceResult resultado : datos) {
            int x = MARGEN + (areaGrafica * resultado.getT()) / maxX;
            double memoria = resultado.getMemoriaUsada() / 1024.0; // KB
            int y = ALTO_GRAFICA - MARGEN - (int)((alturaGrafica * memoria) / maxY);
            
            Point2D puntoActual = new Point2D.Double(x, y);
            
            g2d.fillOval(x - 3, y - 3, 6, 6);
            
            if (puntoAnterior != null) {
                g2d.drawLine((int)puntoAnterior.getX(), (int)puntoAnterior.getY(), x, y);
            }
            
            puntoAnterior = puntoActual;
        }
    }
    
    private void dibujarBarras(Graphics2D g2d, Map<String, Double> datos) {
        int areaGrafica = ANCHO_GRAFICA - 2 * MARGEN;
        int alturaGrafica = ALTO_GRAFICA - MARGEN - MARGEN_SUPERIOR;
        
        double maxTiempo = datos.values().stream().mapToDouble(Double::doubleValue).max().orElse(1.0);
        
        String[] algoritmos = {"Recursivo", "Memoizacion", "Dinamico"};
        int anchoBarra = areaGrafica / (algoritmos.length * 2);
        
        for (int i = 0; i < algoritmos.length; i++) {
            String algoritmo = algoritmos[i];
            if (!datos.containsKey(algoritmo)) continue;
            
            double tiempo = datos.get(algoritmo);
            Color color = getColorAlgoritmo(algoritmo);
            
            int x = MARGEN + (areaGrafica * (i + 1)) / (algoritmos.length + 1) - anchoBarra / 2;
            int altura = (int)((alturaGrafica * tiempo) / maxTiempo);
            int y = ALTO_GRAFICA - MARGEN - altura;
            
            g2d.setColor(color);
            g2d.fillRect(x, y, anchoBarra, altura);
            
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, anchoBarra, altura);
            
            // Etiqueta
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            FontMetrics fm = g2d.getFontMetrics();
            int anchoTexto = fm.stringWidth(algoritmo);
            g2d.drawString(algoritmo, x + (anchoBarra - anchoTexto) / 2, ALTO_GRAFICA - MARGEN + 15);
            
            // Valor
            String valor = String.format("%.3fs", tiempo);
            int anchoValor = fm.stringWidth(valor);
            g2d.drawString(valor, x + (anchoBarra - anchoValor) / 2, y - 5);
        }
    }
    
    private void dibujarGraficaEscalabilidad(Graphics2D g2d, Map<String, double[]> datos, int[] valoresT) {
        int areaGrafica = ANCHO_GRAFICA - 2 * MARGEN;
        int alturaGrafica = ALTO_GRAFICA - MARGEN - MARGEN_SUPERIOR;
        
        double maxTiempo = 3.0; // Máximo 3 segundos (timeout)
        
        // Dibujar ejes con escala logarítmica para t
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(MARGEN, ALTO_GRAFICA - MARGEN, ANCHO_GRAFICA - MARGEN, ALTO_GRAFICA - MARGEN);
        g2d.drawLine(MARGEN, MARGEN_SUPERIOR, MARGEN, ALTO_GRAFICA - MARGEN);
        
        // Etiquetas de los valores de t
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i < valoresT.length; i++) {
            int x = MARGEN + (areaGrafica * (i + 1)) / (valoresT.length + 1);
            g2d.drawLine(x, ALTO_GRAFICA - MARGEN - 5, x, ALTO_GRAFICA - MARGEN + 5);
            String etiqueta = "t=" + valoresT[i];
            FontMetrics fm = g2d.getFontMetrics();
            int anchoEtiqueta = fm.stringWidth(etiqueta);
            g2d.drawString(etiqueta, x - anchoEtiqueta / 2, ALTO_GRAFICA - MARGEN + 20);
        }
        
        // Dibujar líneas para cada algoritmo
        for (Map.Entry<String, double[]> entry : datos.entrySet()) {
            String algoritmo = entry.getKey();
            double[] tiempos = entry.getValue();
            Color color = getColorAlgoritmo(algoritmo);
            
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(3));
            
            for (int i = 0; i < tiempos.length - 1; i++) {
                int x1 = MARGEN + (areaGrafica * (i + 1)) / (valoresT.length + 1);
                int x2 = MARGEN + (areaGrafica * (i + 2)) / (valoresT.length + 1);
                
                int y1 = ALTO_GRAFICA - MARGEN - (int)((alturaGrafica * tiempos[i]) / maxTiempo);
                int y2 = ALTO_GRAFICA - MARGEN - (int)((alturaGrafica * tiempos[i + 1]) / maxTiempo);
                
                g2d.drawLine(x1, y1, x2, y2);
                g2d.fillOval(x1 - 4, y1 - 4, 8, 8);
            }
            
            // Último punto
            int xFinal = MARGEN + (areaGrafica * tiempos.length) / (valoresT.length + 1);
            int yFinal = ALTO_GRAFICA - MARGEN - (int)((alturaGrafica * tiempos[tiempos.length - 1]) / maxTiempo);
            g2d.fillOval(xFinal - 4, yFinal - 4, 8, 8);
        }
        
        // Leyenda específica
        dibujarLeyenda(g2d, datos.keySet());
    }
    
    private void dibujarLeyenda(Graphics2D g2d, Set<String> algoritmos) {
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        FontMetrics fm = g2d.getFontMetrics();
        
        int x = ANCHO_GRAFICA - 200;
        int y = MARGEN_SUPERIOR + 20;
        
        // Fondo de la leyenda
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillRect(x - 10, y - 20, 180, algoritmos.size() * 25 + 20);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - 10, y - 20, 180, algoritmos.size() * 25 + 20);
        
        for (String algoritmo : algoritmos) {
            Color color = getColorAlgoritmo(algoritmo);
            
            // Línea de color
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(x, y, x + 20, y);
            g2d.fillOval(x + 10 - 3, y - 3, 6, 6);
            
            // Texto
            g2d.setColor(Color.BLACK);
            g2d.drawString(algoritmo, x + 30, y + 5);
            
            y += 25;
        }
    }
    
    /**
     * Programa principal para generar todas las gráficas
     */
    public static void main(String[] args) {
        try {
            System.out.println("=== GENERADOR DE GRÁFICAS DE RENDIMIENTO ===");
            
            // Leer datos del CSV
            VisualizadorGraficas visualizador = new VisualizadorGraficas();
            List<PerformanceResult> resultados = leerCSV("resultados_comparacion.csv");
            
            if (resultados.isEmpty()) {
                System.out.println("No se encontraron datos. Ejecute primero AlgorithmComparator.");
                return;
            }
            
            System.out.println("Datos cargados: " + resultados.size() + " resultados");
            
            // Generar todas las gráficas
            visualizador.generarGraficaTiempo(resultados, "grafica_tiempo.png");
            visualizador.generarGraficaMemoria(resultados, "grafica_memoria.png");
            visualizador.generarGraficaBarras(resultados, "grafica_barras.png");
            visualizador.generarGraficaEscalabilidad(resultados, "grafica_escalabilidad.png");
            
            System.out.println("\n=== GRÁFICAS GENERADAS ===");
            System.out.println("- grafica_tiempo.png: Tiempo vs tamaño del problema");
            System.out.println("- grafica_memoria.png: Memoria vs tamaño del problema");
            System.out.println("- grafica_barras.png: Comparación promedio");
            System.out.println("- grafica_escalabilidad.png: Casos específicos m=4, n=9");
            
        } catch (Exception e) {
            System.err.println("Error generando gráficas: " + e.getMessage());
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