package practica5;

import java.util.*;

public class ArbolHuffman {

    private final NodoHuffman raiz;

    public ArbolHuffman(char[] simbolos, int[] frecuencias) {
        if (simbolos.length != frecuencias.length) {
            throw new IllegalArgumentException("Longitudes de alfabeto y frecuencias no coinciden");
        }
        this.raiz = construirArbol(simbolos, frecuencias);
    }

    public NodoHuffman getRaiz() {
        return raiz;
    }

    private NodoHuffman construirArbol(char[] simbolos, int[] frecuencias) {
        PriorityQueue<NodoHuffman> cola = new PriorityQueue<>();

        for (int i = 0; i < simbolos.length; i++) {
            if (frecuencias[i] > 0) {
                cola.add(new NodoHuffman(simbolos[i], frecuencias[i], null, null));
            }
        }

        if (cola.isEmpty()) {
            throw new IllegalArgumentException("No hay símbolos con frecuencia positiva");
        }

        // Caso especial: solo un símbolo
        if (cola.size() == 1) {
            NodoHuffman unico = cola.poll();
            // Creamos un nodo interno ficticio para garantizar un código de al menos 1 bit
            return new NodoHuffman(null, unico.getFrecuencia(), unico, null);
        }

        while (cola.size() > 1) {
            NodoHuffman n1 = cola.poll();
            NodoHuffman n2 = cola.poll();
            NodoHuffman padre = new NodoHuffman(
                    null,
                    n1.getFrecuencia() + n2.getFrecuencia(),
                    n1,
                    n2
            );
            cola.add(padre);
        }

        return cola.poll();
    }

    public Map<Character, String> generarCodigos() {
        Map<Character, String> codigos = new LinkedHashMap<>();
        if (raiz.esHoja()) {
            // Solo un símbolo: asignamos código "0"
            codigos.put(raiz.getSimbolo(), "0");
        } else {
            generarCodigosRec(raiz, "", codigos);
        }
        return codigos;
    }

    private void generarCodigosRec(NodoHuffman nodo, String prefijo,
                                   Map<Character, String> codigos) {
        if (nodo == null) return;
        if (nodo.esHoja()) {
            codigos.put(nodo.getSimbolo(), prefijo);
            return;
        }
        generarCodigosRec(nodo.getIzquierdo(), prefijo + "0", codigos);
        generarCodigosRec(nodo.getDerecho(), prefijo + "1", codigos);
    }
}