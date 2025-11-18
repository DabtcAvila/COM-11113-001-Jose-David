package practica5;

public class NodoHuffman implements Comparable<NodoHuffman> {

    private final Character simbolo; // null si es nodo interno
    private final int frecuencia;
    private final NodoHuffman izquierdo;
    private final NodoHuffman derecho;

    public NodoHuffman(Character simbolo, int frecuencia,
                       NodoHuffman izquierdo, NodoHuffman derecho) {
        this.simbolo = simbolo;
        this.frecuencia = frecuencia;
        this.izquierdo = izquierdo;
        this.derecho = derecho;
    }

    public Character getSimbolo() {
        return simbolo;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public NodoHuffman getIzquierdo() {
        return izquierdo;
    }

    public NodoHuffman getDerecho() {
        return derecho;
    }

    public boolean esHoja() {
        return simbolo != null && izquierdo == null && derecho == null;
    }

    @Override
    public int compareTo(NodoHuffman o) {
        return Integer.compare(this.frecuencia, o.frecuencia);
    }
}