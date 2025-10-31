
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class Principal {

    private static boolean esComentarioOVacía(String s) {
        String t = s.trim();
        return t.isEmpty() || t.startsWith("#") || t.startsWith("//") || t.startsWith(";");
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso:");
            System.out.println("  java -cp bin practica3.Principal <archivo_entrada>");
            System.out.println();
            System.out.println("Notas:");
            System.out.println("  - Cada línea representa un árbol (p. ej. [10, 20, [5, 7]])");
            System.out.println("  - Se ignoran líneas vacías o comentarios (#, //, ;).");
            return;
        }

        Path archivo = Paths.get(args[0]);
        if (!Files.exists(archivo)) {
            System.err.println("Error: no se encontró el archivo: " + archivo.toAbsolutePath());
            System.exit(1);
        }

        int dia = 0;
        try (BufferedReader br = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (esComentarioOVacía(linea)) continue;
                dia++;

                try {
                    Nodo raiz = AnalizadorTexto.parsearLinea(linea);
                    ArbolRuta arbol = new ArbolRuta(raiz);

                    long t1 = System.nanoTime();
                    ResultadoVisita rRec = RecolectorRecursivo.recolectar(arbol);
                    long t2 = System.nanoTime();

                    long t3 = System.nanoTime();
                    ResultadoVisita rIt = RecolectorIterativo.recolectar(arbol);
                    long t4 = System.nanoTime();

                    System.out.println("Día " + dia + ": " + linea.trim());
                    System.out.println("Recursivo: " + rRec + " Tiempo(ns): " + (t2 - t1));
                    System.out.println("Iterativo: " + rIt + " Tiempo(ns): " + (t4 - t3));
                    System.out.println("------------------------------------");
                } catch (IllegalArgumentException ex) {
                    System.err.println("Línea inválida (día " + dia + "): " + ex.getMessage());
                    System.err.println("Contenido: " + linea.trim());
                    System.err.println("------------------------------------");
                }
            }
        } catch (IOException e) {
            System.err.println("Error de E/S leyendo " + archivo.toAbsolutePath());
        }
    }
}
