
import java.io.*;
import java.nio.file.*;

public class Principal {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Principal <archivo_entrada>");
            return;
        }
        String archivo = args[0];

        try {
            for (String linea : Files.readAllLines(Paths.get(archivo))) {
                if (linea.trim().isEmpty()) continue;

                Nodo raiz = AnalizadorTexto.parsearLinea(linea);
                ArbolRuta arbol = new ArbolRuta(raiz);

                long t1 = System.nanoTime();
                ResultadoVisita rRec = RecolectorRecursivo.recolectar(arbol);
                long t2 = System.nanoTime();

                long t3 = System.nanoTime();
                ResultadoVisita rIt = RecolectorIterativo.recolectar(arbol);
                long t4 = System.nanoTime();

                System.out.println("Recursivo: " + rRec + " Tiempo(ns): " + (t2-t1));
                System.out.println("Iterativo: " + rIt + " Tiempo(ns): " + (t4-t3));
                System.out.println("------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
