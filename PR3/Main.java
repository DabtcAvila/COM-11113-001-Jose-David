import java.io.*;
import java.util.*;

/**
 * Programa principal unificado para la Práctica 3
 * Implementación completa del problema de hamburguesas con análisis de rendimiento
 * 
 * Opciones disponibles:
 * 1. Ejecutar algoritmo específico con entrada estándar
 * 2. Comparar rendimiento de todos los algoritmos
 * 3. Generar casos de prueba
 * 4. Ejecutar análisis completo
 * 
 * @author José y David
 */
public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                mostrarMenuInteractivo();
            } else {
                procesarArgumentosLinea(args);
            }
        } catch (Exception e) {
            System.err.println("Error en la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra menú interactivo
     */
    private static void mostrarMenuInteractivo() {
        System.out.println("=".repeat(60));
        System.out.println("    PRÁCTICA 3 - PROBLEMA DE HAMBURGUESAS");
        System.out.println("         Análisis de Algoritmos y Rendimiento");
        System.out.println("              Por José y David");
        System.out.println("=".repeat(60));
        System.out.println();
        
        while (true) {
            System.out.println("Opciones disponibles:");
            System.out.println("1. Ejecutar algoritmo específico");
            System.out.println("2. Comparar rendimiento de algoritmos");
            System.out.println("3. Generar casos de prueba");
            System.out.println("4. Ejecutar análisis completo");
            System.out.println("5. Probar caso individual");
            System.out.println("6. Mostrar información de algoritmos");
            System.out.println("0. Salir");
            System.out.println();
            System.out.print("Seleccione una opción (0-6): ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine().trim());
                System.out.println();
                
                switch (opcion) {
                    case 1:
                        ejecutarAlgoritmoEspecifico();
                        break;
                    case 2:
                        compararRendimiento();
                        break;
                    case 3:
                        generarCasosPrueba();
                        break;
                    case 4:
                        ejecutarAnalisisCompleto();
                        break;
                    case 5:
                        probarCasoIndividual();
                        break;
                    case 6:
                        mostrarInformacionAlgoritmos();
                        break;
                    case 0:
                        System.out.println("¡Hasta luego!");
                        return;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
                
                System.out.println("\\nPresione Enter para continuar...");
                scanner.nextLine();
                System.out.println();
                
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Procesa argumentos de línea de comandos
     */
    private static void procesarArgumentosLinea(String[] args) throws Exception {
        String comando = args[0].toLowerCase();
        
        switch (comando) {
            case "recursivo":
            case "-r":
                ejecutarDesdeEntradaEstandar("recursivo");
                break;
                
            case "memoizacion":
            case "-m":
                ejecutarDesdeEntradaEstandar("memoizacion");
                break;
                
            case "dinamico":
            case "-d":
                ejecutarDesdeEntradaEstandar("dinamico");
                break;
                
            case "comparar":
            case "-c":
                AlgorithmComparator comparador = new AlgorithmComparator();
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                comparador.main(subArgs);
                break;
                
            case "generar":
            case "-g":
                TestDataGenerator.main(new String[0]);
                break;
                
            case "analisis":
            case "-a":
                ejecutarAnalisisCompletoCmd();
                break;
                
            case "help":
            case "-h":
            case "--help":
                mostrarAyuda();
                break;
                
            default:
                System.out.println("Comando no reconocido: " + comando);
                mostrarAyuda();
        }
    }
    
    /**
     * Ejecuta algoritmo específico con entrada estándar
     */
    private static void ejecutarAlgoritmoEspecifico() {
        System.out.println("Seleccione el algoritmo a ejecutar:");
        System.out.println("1. Recursivo (fuerza bruta con optimizaciones)");
        System.out.println("2. Memoización (programación dinámica top-down)");
        System.out.println("3. Dinámico (programación dinámica bottom-up)");
        System.out.print("Opción (1-3): ");
        
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            String algoritmo;
            
            switch (opcion) {
                case 1: algoritmo = "recursivo"; break;
                case 2: algoritmo = "memoizacion"; break;
                case 3: algoritmo = "dinamico"; break;
                default:
                    System.out.println("Opción no válida.");
                    return;
            }
            
            System.out.println("\\nIngrese los casos de prueba (formato: m n t)");
            System.out.println("Línea vacía para terminar:");
            ejecutarDesdeEntradaEstandar(algoritmo);
            
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese un número válido.");
        }
    }
    
    /**
     * Ejecuta algoritmo desde entrada estándar
     */
    private static void ejecutarDesdeEntradaEstandar(String algoritmo) {
        try {
            switch (algoritmo.toLowerCase()) {
                case "recursivo":
                    BurgerRecursivo.main(new String[0]);
                    break;
                case "memoizacion":
                    BurgerMemoizacion.main(new String[0]);
                    break;
                case "dinamico":
                    BurgerDinamico.main(new String[0]);
                    break;
                default:
                    System.out.println("Algoritmo no reconocido: " + algoritmo);
            }
        } catch (Exception e) {
            System.out.println("Error ejecutando algoritmo: " + e.getMessage());
        }
    }
    
    /**
     * Compara rendimiento de algoritmos
     */
    private static void compararRendimiento() {
        System.out.println("=== COMPARACIÓN DE RENDIMIENTO ===");
        System.out.println("Seleccione el tipo de análisis:");
        System.out.println("1. Análisis rápido (casos pequeños)");
        System.out.println("2. Análisis completo (todos los casos)");
        System.out.println("3. Análisis personalizado");
        System.out.print("Opción (1-3): ");
        
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            
            AlgorithmComparator comparador = new AlgorithmComparator();
            TestDataGenerator generador = new TestDataGenerator();
            List<TestDataGenerator.CasoPrueba> casos;
            
            switch (opcion) {
                case 1:
                    casos = generador.filtrarPorTipo(generador.generarConjuntoCompleto(),
                        TestDataGenerator.TipoCaso.TRIVIAL, TestDataGenerator.TipoCaso.PEQUENO);
                    break;
                case 2:
                    casos = generador.generarConjuntoCompleto();
                    break;
                case 3:
                    casos = seleccionarCasosPersonalizados(generador);
                    break;
                default:
                    System.out.println("Opción no válida.");
                    return;
            }
            
            System.out.println("\\nEjecutando comparación con " + casos.size() + " casos...");
            List<PerformanceResult> resultados = comparador.ejecutarBateriaPruebas(casos);
            
            String timestamp = String.valueOf(System.currentTimeMillis());
            String archivoReporte = "reporte_" + timestamp + ".txt";
            String archivoCSV = "resultados_" + timestamp + ".csv";
            
            comparador.generarReporte(resultados, archivoReporte);
            comparador.generarCSV(resultados, archivoCSV);
            
            System.out.println("\\nArchivos generados:");
            System.out.println("- " + archivoReporte);
            System.out.println("- " + archivoCSV);
            
            mostrarResumenResultados(resultados);
            
        } catch (Exception e) {
            System.out.println("Error en la comparación: " + e.getMessage());
        }
    }
    
    /**
     * Permite seleccionar casos personalizados
     */
    private static List<TestDataGenerator.CasoPrueba> seleccionarCasosPersonalizados(TestDataGenerator generador) {
        System.out.println("\\nSeleccione los tipos de casos a incluir:");
        System.out.println("1. Triviales");
        System.out.println("2. Pequeños");
        System.out.println("3. Medianos");
        System.out.println("4. Grandes");
        System.out.println("5. Estrés");
        System.out.println("6. Borde");
        System.out.print("Ingrese los números separados por comas (ej: 1,2,3): ");
        
        String seleccion = scanner.nextLine().trim();
        List<TestDataGenerator.TipoCaso> tipos = new ArrayList<>();
        
        for (String numero : seleccion.split(",")) {
            try {
                int tipo = Integer.parseInt(numero.trim());
                switch (tipo) {
                    case 1: tipos.add(TestDataGenerator.TipoCaso.TRIVIAL); break;
                    case 2: tipos.add(TestDataGenerator.TipoCaso.PEQUENO); break;
                    case 3: tipos.add(TestDataGenerator.TipoCaso.MEDIANO); break;
                    case 4: tipos.add(TestDataGenerator.TipoCaso.GRANDE); break;
                    case 5: tipos.add(TestDataGenerator.TipoCaso.ESTRES); break;
                    case 6: tipos.add(TestDataGenerator.TipoCaso.BORDE); break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Número no válido ignorado: " + numero);
            }
        }
        
        if (tipos.isEmpty()) {
            tipos.add(TestDataGenerator.TipoCaso.TRIVIAL);
        }
        
        return generador.filtrarPorTipo(generador.generarConjuntoCompleto(), 
                                       tipos.toArray(new TestDataGenerator.TipoCaso[0]));
    }
    
    /**
     * Genera casos de prueba
     */
    private static void generarCasosPrueba() {
        System.out.println("=== GENERACIÓN DE CASOS DE PRUEBA ===");
        try {
            TestDataGenerator.main(new String[0]);
            System.out.println("Casos de prueba generados exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al generar casos de prueba: " + e.getMessage());
        }
    }
    
    /**
     * Ejecuta análisis completo
     */
    private static void ejecutarAnalisisCompleto() {
        System.out.println("=== ANÁLISIS COMPLETO ===");
        System.out.println("Este proceso puede tomar varios minutos...");
        System.out.print("¿Desea continuar? (s/n): ");
        
        if (!scanner.nextLine().trim().toLowerCase().startsWith("s")) {
            System.out.println("Análisis cancelado.");
            return;
        }
        
        try {
            ejecutarAnalisisCompletoCmd();
        } catch (Exception e) {
            System.out.println("Error en el análisis: " + e.getMessage());
        }
    }
    
    /**
     * Ejecuta análisis completo desde línea de comandos
     */
    private static void ejecutarAnalisisCompletoCmd() throws Exception {
        System.out.println("1. Generando casos de prueba...");
        TestDataGenerator.main(new String[0]);
        
        System.out.println("2. Ejecutando comparación completa...");
        AlgorithmComparator.main(new String[0]);
        
        System.out.println("\\n=== ANÁLISIS COMPLETO FINALIZADO ===");
        System.out.println("Archivos generados:");
        System.out.println("- casos_prueba_completos.txt");
        System.out.println("- casos_rapidos.txt");
        System.out.println("- casos_estres.txt");
        System.out.println("- casos_equilibrio.txt");
        System.out.println("- reporte_comparacion.txt");
        System.out.println("- resultados_comparacion.csv");
    }
    
    /**
     * Prueba un caso individual
     */
    private static void probarCasoIndividual() {
        System.out.println("=== PRUEBA DE CASO INDIVIDUAL ===");
        System.out.print("Ingrese m (tiempo hamburguesa M): ");
        int m = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.print("Ingrese n (tiempo hamburguesa N): ");
        int n = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.print("Ingrese t (tiempo total disponible): ");
        int t = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.println("\\nProbando caso: m=" + m + ", n=" + n + ", t=" + t);
        System.out.println("-".repeat(50));
        
        AlgorithmComparator comparador = new AlgorithmComparator();
        List<PerformanceResult> resultados = comparador.compararAlgoritmos(m, n, t);
        
        for (PerformanceResult resultado : resultados) {
            System.out.println(resultado);
        }
    }
    
    /**
     * Muestra información de algoritmos
     */
    private static void mostrarInformacionAlgoritmos() {
        System.out.println("=== INFORMACIÓN DE ALGORITMOS ===");
        System.out.println();
        
        System.out.println("1. ALGORITMO RECURSIVO (Fuerza Bruta Optimizada)");
        System.out.println("   - Complejidad temporal: O(2^(t/min(m,n)))");
        System.out.println("   - Complejidad espacial: O(t/min(m,n)) [stack de recursión]");
        System.out.println("   - Optimizaciones: poda por límite superior, ordenamiento de opciones");
        System.out.println("   - Mejor para: casos pequeños (t < 30)");
        System.out.println();
        
        System.out.println("2. ALGORITMO DE MEMOIZACIÓN (Programación Dinámica Top-Down)");
        System.out.println("   - Complejidad temporal: O(t)");
        System.out.println("   - Complejidad espacial: O(t)");
        System.out.println("   - Optimizaciones: cache con hash eficiente, normalización de parámetros");
        System.out.println("   - Mejor para: casos medianos (30 < t < 10000)");
        System.out.println();
        
        System.out.println("3. ALGORITMO DINÁMICO (Programación Dinámica Bottom-Up)");
        System.out.println("   - Complejidad temporal: O(t)");
        System.out.println("   - Complejidad espacial: O(t) o O(1) para casos grandes");
        System.out.println("   - Optimizaciones: array rolling para casos grandes, detección de timeout");
        System.out.println("   - Mejor para: casos grandes (t > 10000)");
        System.out.println();
        
        System.out.println("PROBLEMA:");
        System.out.println("Maximizar el número de hamburguesas que se pueden preparar en t minutos,");
        System.out.println("donde las hamburguesas tipo M toman m minutos y tipo N toman n minutos.");
        System.out.println();
        
        System.out.println("FORMATO DE ENTRADA: m n t");
        System.out.println("FORMATO DE SALIDA: total_burgers burgers_m burgers_n [tiempo_restante]");
        System.out.println("Si tiempo_restante = 0, no se muestra.");
        System.out.println("Si se excede el tiempo límite: 'tiempo límite para resolver el problema excedido'");
    }
    
    /**
     * Muestra resumen de resultados
     */
    private static void mostrarResumenResultados(List<PerformanceResult> resultados) {
        Map<String, List<PerformanceResult>> porAlgoritmo = new HashMap<>();
        for (PerformanceResult resultado : resultados) {
            porAlgoritmo.computeIfAbsent(resultado.getAlgoritmo(), k -> new ArrayList<>()).add(resultado);
        }
        
        System.out.println("\\n=== RESUMEN DE RESULTADOS ===");
        for (String algoritmo : porAlgoritmo.keySet()) {
            List<PerformanceResult> res = porAlgoritmo.get(algoritmo);
            long exitosos = res.stream().mapToLong(r -> r.isSolucionCorrecta() && !r.isTimeoutExcedido() ? 1 : 0).sum();
            long timeouts = res.stream().mapToLong(r -> r.isTimeoutExcedido() ? 1 : 0).sum();
            double tiempoPromedio = res.stream()
                .filter(r -> !r.isTimeoutExcedido())
                .mapToDouble(PerformanceResult::getTiempoEjecucionSegundos)
                .average().orElse(0.0);
            
            System.out.printf("%-12s: %3d/%3d exitosos, %3d timeouts, %8.6fs promedio%n", 
                algoritmo, exitosos, res.size(), timeouts, tiempoPromedio);
        }
    }
    
    /**
     * Muestra ayuda
     */
    private static void mostrarAyuda() {
        System.out.println("=== AYUDA - PRÁCTICA 3 HAMBURGUESAS ===");
        System.out.println();
        System.out.println("USO:");
        System.out.println("  java Main                    - Menú interactivo");
        System.out.println("  java Main <comando> [args]   - Ejecución directa");
        System.out.println();
        System.out.println("COMANDOS:");
        System.out.println("  recursivo, -r      - Ejecuta algoritmo recursivo con entrada estándar");
        System.out.println("  memoizacion, -m    - Ejecuta algoritmo de memoización");
        System.out.println("  dinamico, -d       - Ejecuta algoritmo dinámico");
        System.out.println("  comparar, -c       - Compara rendimiento de algoritmos");
        System.out.println("  generar, -g        - Genera casos de prueba");
        System.out.println("  analisis, -a       - Ejecuta análisis completo");
        System.out.println("  help, -h, --help  - Muestra esta ayuda");
        System.out.println();
        System.out.println("EJEMPLOS:");
        System.out.println("  java Main recursivo < casos_prueba.txt");
        System.out.println("  java Main comparar --rapido");
        System.out.println("  java Main analisis");
        System.out.println();
        System.out.println("ARCHIVOS GENERADOS:");
        System.out.println("  - casos_prueba_*.txt    - Casos de prueba generados");
        System.out.println("  - reporte_*.txt         - Reportes de comparación");
        System.out.println("  - resultados_*.csv      - Datos para gráficas");
    }
}