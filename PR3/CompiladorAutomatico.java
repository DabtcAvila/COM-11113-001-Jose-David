import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Compilador y ejecutor automático para la Práctica 3
 * Automatiza todo el proceso: compilación, ejecución, análisis y limpieza
 * 
 * @author José y David
 */
public class CompiladorAutomatico {
    
    private static final String[] ARCHIVOS_JAVA = {
        "BurgerRecursivo.java",
        "BurgerMemoizacion.java", 
        "BurgerDinamico.java",
        "PerformanceResult.java",
        "TestDataGenerator.java",
        "AlgorithmComparator.java",
        "VisualizadorGraficas.java",
        "AnalizadorRendimiento.java",
        "Ejecutor.java",
        "Main.java"
    };
    
    private static final String[] ARCHIVOS_PRINCIPALES = {
        "Ejecutor",
        "VisualizadorGraficas", 
        "AnalizadorRendimiento",
        "AlgorithmComparator",
        "Main"
    };
    
    private boolean verbose;
    private String directorioTrabajo;
    
    public CompiladorAutomatico(boolean verbose) {
        this.verbose = verbose;
        this.directorioTrabajo = System.getProperty("user.dir");
    }
    
    /**
     * Ejecuta el proceso completo de compilación y análisis
     */
    public boolean ejecutarProcesoCompleto() {
        log("=== COMPILADOR AUTOMÁTICO - PRÁCTICA 3 ===");
        log("Autores: José y David");
        log("Directorio: " + directorioTrabajo);
        log("");
        
        try {
            // Paso 1: Verificar archivos
            if (!verificarArchivos()) {
                error("❌ Verificación de archivos falló");
                return false;
            }
            
            // Paso 2: Limpiar archivos anteriores
            limpiarArchivosAnteriores();
            
            // Paso 3: Compilar
            if (!compilarArchivos()) {
                error("❌ Compilación falló");
                return false;
            }
            
            // Paso 4: Ejecutar análisis principal
            if (!ejecutarAnalisisPrincipal()) {
                error("❌ Ejecución del análisis falló");
                return false;
            }
            
            // Paso 5: Generar gráficas
            if (!generarGraficas()) {
                error("❌ Generación de gráficas falló");
                return false;
            }
            
            // Paso 6: Generar análisis estadístico
            if (!generarAnalisisEstadistico()) {
                error("❌ Análisis estadístico falló");
                return false;
            }
            
            // Paso 7: Verificar salidas
            if (!verificarArchivosGenerados()) {
                error("❌ Verificación de archivos generados falló");
                return false;
            }
            
            // Paso 8: Generar resumen
            generarResumenFinal();
            
            log("✅ PROCESO COMPLETO EXITOSO");
            return true;
            
        } catch (Exception e) {
            error("❌ Error durante el proceso: " + e.getMessage());
            if (verbose) {
                e.printStackTrace();
            }
            return false;
        }
    }
    
    /**
     * Verifica que todos los archivos necesarios existan
     */
    private boolean verificarArchivos() {
        log("🔍 Verificando archivos fuente...");
        
        boolean todosExisten = true;
        for (String archivo : ARCHIVOS_JAVA) {
            Path path = Paths.get(directorioTrabajo, archivo);
            if (!Files.exists(path)) {
                error("   ❌ Falta: " + archivo);
                todosExisten = false;
            } else {
                log("   ✅ " + archivo);
            }
        }
        
        if (todosExisten) {
            log("✅ Todos los archivos fuente encontrados");
        }
        
        return todosExisten;
    }
    
    /**
     * Limpia archivos de compilaciones y ejecuciones anteriores
     */
    private void limpiarArchivosAnteriores() {
        log("🧹 Limpiando archivos anteriores...");
        
        // Archivos class
        try {
            Files.walk(Paths.get(directorioTrabajo))
                .filter(p -> p.toString().endsWith(".class"))
                .forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                        log("   🗑️  " + p.getFileName());
                    } catch (IOException e) {
                        // Ignorar errores de eliminación
                    }
                });
        } catch (IOException e) {
            log("   ⚠️  Error limpiando archivos .class: " + e.getMessage());
        }
        
        // Archivos de salida anteriores
        String[] archivosLimpiar = {
            "reporte_practica3.txt",
            "datos_practica3.csv",
            "analisis_practica3.txt",
            "casos_especificos_reporte.txt",
            "resultados_casos_especificos.csv",
            "practica3_tiempo.png",
            "practica3_memoria.png",
            "practica3_comparacion.png",
            "practica3_escalabilidad.png",
            "resumen_compilacion.txt"
        };
        
        for (String archivo : archivosLimpiar) {
            try {
                if (Files.deleteIfExists(Paths.get(directorioTrabajo, archivo))) {
                    log("   🗑️  " + archivo);
                }
            } catch (IOException e) {
                // Ignorar errores
            }
        }
        
        log("✅ Limpieza completada");
    }
    
    /**
     * Compila todos los archivos Java
     */
    private boolean compilarArchivos() {
        log("⚙️  Compilando archivos Java...");
        
        try {
            // Crear comando de compilación
            List<String> comando = new ArrayList<>();
            comando.add("javac");
            comando.addAll(Arrays.asList(ARCHIVOS_JAVA));
            
            // Ejecutar compilación
            ProcessBuilder pb = new ProcessBuilder(comando);
            pb.directory(new File(directorioTrabajo));
            pb.redirectErrorStream(true);
            
            Process proceso = pb.start();
            
            // Capturar salida
            StringBuilder salida = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    salida.append(linea).append("\n");
                    if (verbose) {
                        log("   " + linea);
                    }
                }
            }
            
            boolean exito = proceso.waitFor(30, TimeUnit.SECONDS) && proceso.exitValue() == 0;
            
            if (exito) {
                log("✅ Compilación exitosa");
                
                // Verificar archivos .class generados
                int classGenerados = 0;
                for (String archivo : ARCHIVOS_JAVA) {
                    String nombreClass = archivo.replace(".java", ".class");
                    if (Files.exists(Paths.get(directorioTrabajo, nombreClass))) {
                        classGenerados++;
                    }
                }
                log("   📦 Archivos .class generados: " + classGenerados);
                
            } else {
                error("❌ Error de compilación:");
                error(salida.toString());
            }
            
            return exito;
            
        } catch (Exception e) {
            error("❌ Error ejecutando compilación: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Ejecuta el análisis principal usando Ejecutor
     */
    private boolean ejecutarAnalisisPrincipal() {
        log("🚀 Ejecutando análisis principal...");
        
        return ejecutarClase("Ejecutor", Arrays.asList("--completo"), 300); // 5 minutos timeout
    }
    
    /**
     * Genera las gráficas usando VisualizadorGraficas
     */
    private boolean generarGraficas() {
        log("📈 Generando gráficas...");
        
        // Verificar que existan los datos
        if (!Files.exists(Paths.get(directorioTrabajo, "resultados_comparacion.csv"))) {
            log("   ⚠️  No se encontró resultados_comparacion.csv, intentando con datos_practica3.csv");
            
            // Copiar archivo si existe con nombre diferente
            try {
                Path origen = Paths.get(directorioTrabajo, "datos_practica3.csv");
                Path destino = Paths.get(directorioTrabajo, "resultados_comparacion.csv");
                
                if (Files.exists(origen)) {
                    Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
                    log("   📋 Copiado datos_practica3.csv -> resultados_comparacion.csv");
                } else {
                    error("   ❌ No se encontraron datos para gráficas");
                    return false;
                }
                
            } catch (IOException e) {
                error("   ❌ Error copiando archivo de datos: " + e.getMessage());
                return false;
            }
        }
        
        return ejecutarClase("VisualizadorGraficas", Collections.emptyList(), 120); // 2 minutos timeout
    }
    
    /**
     * Genera el análisis estadístico
     */
    private boolean generarAnalisisEstadistico() {
        log("📊 Generando análisis estadístico...");
        
        // Verificar que existan los datos
        if (!Files.exists(Paths.get(directorioTrabajo, "resultados_comparacion.csv"))) {
            error("   ❌ No se encontraron datos para análisis estadístico");
            return false;
        }
        
        return ejecutarClase("AnalizadorRendimiento", Collections.emptyList(), 60); // 1 minuto timeout
    }
    
    /**
     * Ejecuta una clase Java específica
     */
    private boolean ejecutarClase(String nombreClase, List<String> argumentos, int timeoutSegundos) {
        try {
            List<String> comando = new ArrayList<>();
            comando.add("java");
            comando.add(nombreClase);
            comando.addAll(argumentos);
            
            ProcessBuilder pb = new ProcessBuilder(comando);
            pb.directory(new File(directorioTrabajo));
            pb.redirectErrorStream(true);
            
            log("   💻 Ejecutando: " + String.join(" ", comando));
            
            Process proceso = pb.start();
            
            // Capturar salida en tiempo real
            StringBuilder salida = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    salida.append(linea).append("\n");
                    if (verbose) {
                        log("     " + linea);
                    } else if (linea.contains("===") || linea.contains("✅") || linea.contains("❌")) {
                        log("     " + linea);
                    }
                }
            }
            
            boolean termino = proceso.waitFor(timeoutSegundos, TimeUnit.SECONDS);
            
            if (!termino) {
                proceso.destroyForcibly();
                error("   ⏰ Timeout ejecutando " + nombreClase);
                return false;
            }
            
            boolean exito = proceso.exitValue() == 0;
            
            if (exito) {
                log("   ✅ " + nombreClase + " ejecutado exitosamente");
            } else {
                error("   ❌ Error ejecutando " + nombreClase + " (código: " + proceso.exitValue() + ")");
                if (!verbose) {
                    error("     Salida del error:");
                    error(salida.toString());
                }
            }
            
            return exito;
            
        } catch (Exception e) {
            error("   ❌ Error ejecutando " + nombreClase + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica que se hayan generado todos los archivos esperados
     */
    private boolean verificarArchivosGenerados() {
        log("🔍 Verificando archivos generados...");
        
        String[] archivosEsperados = {
            "reporte_practica3.txt",
            "datos_practica3.csv", 
            "analisis_practica3.txt",
            "casos_especificos_reporte.txt",
            "resultados_casos_especificos.csv",
            "practica3_tiempo.png",
            "practica3_memoria.png",
            "practica3_comparacion.png",
            "practica3_escalabilidad.png"
        };
        
        boolean todosGenerados = true;
        Map<String, Boolean> estadoArchivos = new HashMap<>();
        
        for (String archivo : archivosEsperados) {
            Path path = Paths.get(directorioTrabajo, archivo);
            boolean existe = Files.exists(path);
            estadoArchivos.put(archivo, existe);
            
            if (existe) {
                try {
                    long tamano = Files.size(path);
                    log("   ✅ " + archivo + " (" + formatearTamano(tamano) + ")");
                } catch (IOException e) {
                    log("   ✅ " + archivo + " (tamaño desconocido)");
                }
            } else {
                error("   ❌ " + archivo + " NO GENERADO");
                todosGenerados = false;
            }
        }
        
        // Verificar archivos críticos
        if (!estadoArchivos.getOrDefault("reporte_practica3.txt", false) ||
            !estadoArchivos.getOrDefault("datos_practica3.csv", false)) {
            error("❌ Faltan archivos críticos del análisis");
            return false;
        }
        
        return todosGenerados;
    }
    
    /**
     * Genera un resumen final del proceso
     */
    private void generarResumenFinal() {
        log("📝 Generando resumen final...");
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("resumen_compilacion.txt"))) {
            writer.println("=== RESUMEN DE COMPILACIÓN Y EJECUCIÓN ===");
            writer.println("Fecha: " + new Date());
            writer.println("Autores: José y David");
            writer.println("Directorio: " + directorioTrabajo);
            writer.println();
            
            writer.println("ARCHIVOS FUENTE PROCESADOS:");
            for (String archivo : ARCHIVOS_JAVA) {
                Path path = Paths.get(directorioTrabajo, archivo);
                if (Files.exists(path)) {
                    try {
                        long tamano = Files.size(path);
                        writer.printf("  ✅ %s (%s)%n", archivo, formatearTamano(tamano));
                    } catch (IOException e) {
                        writer.printf("  ✅ %s%n", archivo);
                    }
                } else {
                    writer.printf("  ❌ %s (no encontrado)%n", archivo);
                }
            }
            
            writer.println();
            writer.println("ARCHIVOS GENERADOS:");
            
            String[] archivosGenerados = {
                "reporte_practica3.txt",
                "datos_practica3.csv",
                "analisis_practica3.txt", 
                "casos_especificos_reporte.txt",
                "resultados_casos_especificos.csv",
                "practica3_tiempo.png",
                "practica3_memoria.png",
                "practica3_comparacion.png",
                "practica3_escalabilidad.png"
            };
            
            for (String archivo : archivosGenerados) {
                Path path = Paths.get(directorioTrabajo, archivo);
                if (Files.exists(path)) {
                    try {
                        long tamano = Files.size(path);
                        writer.printf("  ✅ %s (%s)%n", archivo, formatearTamano(tamano));
                    } catch (IOException e) {
                        writer.printf("  ✅ %s%n", archivo);
                    }
                } else {
                    writer.printf("  ❌ %s (no generado)%n", archivo);
                }
            }
            
            writer.println();
            writer.println("INSTRUCCIONES DE REVISIÓN:");
            writer.println("1. Revise reporte_practica3.txt para el análisis principal");
            writer.println("2. Revise analisis_practica3.txt para estadísticas detalladas");
            writer.println("3. Revise casos_especificos_reporte.txt para casos requeridos");
            writer.println("4. Abra las gráficas PNG para visualización de resultados");
            writer.println("5. Use los archivos CSV para análisis adicional si es necesario");
            
            writer.println();
            writer.println("ESTADO: COMPILACIÓN Y ANÁLISIS COMPLETADOS");
            
            log("✅ Resumen guardado en: resumen_compilacion.txt");
            
        } catch (IOException e) {
            error("❌ Error generando resumen: " + e.getMessage());
        }
    }
    
    /**
     * Formatea el tamaño de archivo en formato legible
     */
    private String formatearTamano(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
    
    /**
     * Ejecuta solo la compilación (útil para desarrollo)
     */
    public boolean soloCompilar() {
        log("=== SOLO COMPILACIÓN ===");
        
        return verificarArchivos() && compilarArchivos();
    }
    
    /**
     * Ejecuta solo el análisis rápido
     */
    public boolean ejecutarAnalisisRapido() {
        log("=== ANÁLISIS RÁPIDO ===");
        
        try {
            if (!verificarArchivos() || !compilarArchivos()) {
                return false;
            }
            
            return ejecutarClase("Ejecutor", Arrays.asList("--rapido"), 120);
            
        } catch (Exception e) {
            error("❌ Error en análisis rápido: " + e.getMessage());
            return false;
        }
    }
    
    // Métodos de utilidad para logging
    private void log(String mensaje) {
        System.out.println(mensaje);
    }
    
    private void error(String mensaje) {
        System.err.println(mensaje);
    }
    
    /**
     * Programa principal
     */
    public static void main(String[] args) {
        boolean verbose = false;
        String modo = "completo";
        
        // Procesar argumentos
        for (String arg : args) {
            switch (arg.toLowerCase()) {
                case "--verbose":
                case "-v":
                    verbose = true;
                    break;
                case "--rapido":
                case "-r":
                    modo = "rapido";
                    break;
                case "--compilar":
                case "-c":
                    modo = "compilar";
                    break;
                case "--completo":
                    modo = "completo";
                    break;
                case "--help":
                case "-h":
                    mostrarAyuda();
                    return;
            }
        }
        
        CompiladorAutomatico compilador = new CompiladorAutomatico(verbose);
        boolean exito = false;
        
        switch (modo) {
            case "compilar":
                exito = compilador.soloCompilar();
                break;
            case "rapido":
                exito = compilador.ejecutarAnalisisRapido();
                break;
            case "completo":
            default:
                exito = compilador.ejecutarProcesoCompleto();
                break;
        }
        
        System.exit(exito ? 0 : 1);
    }
    
    /**
     * Muestra información de ayuda
     */
    private static void mostrarAyuda() {
        System.out.println("=== COMPILADOR AUTOMÁTICO - PRÁCTICA 3 ===");
        System.out.println();
        System.out.println("Uso: java CompiladorAutomatico [opciones]");
        System.out.println();
        System.out.println("Opciones:");
        System.out.println("  --completo        Proceso completo: compilar + análisis + gráficas (por defecto)");
        System.out.println("  --rapido, -r      Compilar + análisis rápido (solo casos específicos)");
        System.out.println("  --compilar, -c    Solo compilar archivos Java");
        System.out.println("  --verbose, -v     Mostrar salida detallada");
        System.out.println("  --help, -h        Mostrar esta ayuda");
        System.out.println();
        System.out.println("Ejemplos:");
        System.out.println("  java CompiladorAutomatico                    # Proceso completo");
        System.out.println("  java CompiladorAutomatico --rapido           # Análisis rápido");
        System.out.println("  java CompiladorAutomatico --compilar --verbose # Solo compilar con detalles");
        System.out.println();
        System.out.println("Archivos generados:");
        System.out.println("  - Reportes: reporte_practica3.txt, analisis_practica3.txt");
        System.out.println("  - Datos: datos_practica3.csv, resultados_casos_especificos.csv");
        System.out.println("  - Gráficas: practica3_*.png");
        System.out.println("  - Resumen: resumen_compilacion.txt");
        System.out.println();
        System.out.println("Autores: José y David");
    }
}