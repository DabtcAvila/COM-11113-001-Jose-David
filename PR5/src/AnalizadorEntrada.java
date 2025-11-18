package practica5;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class AnalizadorEntrada {

    public static class DatosEntrada {
        private final char[] alfabeto;
        private final int[] frecuencias;
        private final String texto;

        public DatosEntrada(char[] alfabeto, int[] frecuencias, String texto) {
            this.alfabeto = alfabeto;
            this.frecuencias = frecuencias;
            this.texto = texto;
        }

        public char[] getAlfabeto() { return alfabeto; }
        public int[] getFrecuencias() { return frecuencias; }
        public String getTexto() { return texto; }
    }

    public static DatosEntrada leerArchivo(String ruta) throws IOException {
        Path p = Paths.get(ruta);
        List<String> lineas = Files.readAllLines(p, StandardCharsets.UTF_8);
        if (lineas.size() < 3) {
            throw new IllegalArgumentException("El archivo debe tener al menos 3 líneas");
        }

        String lineaAlfabeto = lineas.get(0).trim();
        String lineaFrecuencias = lineas.get(1).trim();
        String texto = lineas.get(2); // puede contener espacios

        String[] tokensSimbolos = lineaAlfabeto.split("\\s+");
        String[] tokensFreq = lineaFrecuencias.split("\\s+");

        if (tokensSimbolos.length != tokensFreq.length) {
            throw new IllegalArgumentException("Número de símbolos y frecuencias no coincide");
        }

        char[] alfabeto = new char[tokensSimbolos.length];
        int[] frecuencias = new int[tokensFreq.length];

        for (int i = 0; i < tokensSimbolos.length; i++) {
            String s = tokensSimbolos[i];
            if (s.length() != 1) {
                throw new IllegalArgumentException("Cada símbolo debe ser un solo carácter: " + s);
            }
            alfabeto[i] = s.charAt(0);
            frecuencias[i] = Integer.parseInt(tokensFreq[i]);
        }

        return new DatosEntrada(alfabeto, frecuencias, texto);
    }
}