package practica5;

import java.io.IOException;
import java.util.Map;

public class Principal {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso:");
            System.out.println("  java -cp bin practica5.Principal datos/entrada_ejemplo.txt");
            return;
        }

        String rutaArchivo = args[0];

        try {
            AnalizadorEntrada.DatosEntrada de = AnalizadorEntrada.leerArchivo(rutaArchivo);
            char[] alfabeto = de.getAlfabeto();
            int[] frecuencias = de.getFrecuencias();
            String texto = de.getTexto();

            // Construcción del código de Huffman
            long t1 = System.nanoTime();
            ArbolHuffman arbol = new ArbolHuffman(alfabeto, frecuencias);
            Map<Character, String> codigos = arbol.generarCodigos();
            long t2 = System.nanoTime();

            // Codificación
            long t3 = System.nanoTime();
            String textoCodificado = Codificador.codificar(texto, codigos);
            long t4 = System.nanoTime();

            String textoDecodificado = Decodificador.decodificar(textoCodificado, arbol.getRaiz());

            int numSimbolos = texto.length();
            int bitsCod = Codificador.bitsCodificados(textoCodificado);
            int bitsUtf8 = Codificador.bitsUtf8(texto);

            ResultadoHuffman resultado = new ResultadoHuffman(
                    alfabeto,
                    codigos,
                    numSimbolos,
                    bitsCod,
                    bitsUtf8,
                    (t2 - t1),
                    (t4 - t3),
                    textoCodificado,
                    textoDecodificado
            );

            imprimirResultado(resultado);

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error en la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void imprimirResultado(ResultadoHuffman r) {
        // Línea con el alfabeto
        System.out.print("Alfabeto:");
        for (char c : r.getAlfabeto()) {
            System.out.print(" " + c);
        }
        System.out.println();

        // Línea con los códigos
        System.out.print("Codigos:");
        for (char c : r.getAlfabeto()) {
            String codigo = r.getCodigos().get(c);
            System.out.print(" " + c + "=" + codigo);
        }
        System.out.println();

        // Línea con números de símbolos y bits
        System.out.println(
                "Simbolos_originales=" + r.getNumSimbolosOriginales()
                        + " Bits_codificados=" + r.getBitsCodificados()
                        + " Bits_UTF8=" + r.getBitsUtf8()
        );

        // Tiempos
        System.out.println("Tiempo_Huffman(ns)=" + r.getTiempoConstruccionNs()
                + " Tiempo_Codificacion(ns)=" + r.getTiempoCodificacionNs());

        // Texto codificado y decodificado
        System.out.println("Texto_codificado: " + r.getTextoCodificado());
        System.out.println("Texto_decodificado: " + r.getTextoDecodificado());
    }
}