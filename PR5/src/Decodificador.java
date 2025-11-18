package practica5;

public class Decodificador {

    public static String decodificar(String bits, NodoHuffman raiz) {
        StringBuilder sb = new StringBuilder();
        NodoHuffman actual = raiz;

        for (int i = 0; i < bits.length(); i++) {
            char b = bits.charAt(i);
            if (b == '0') {
                actual = actual.getIzquierdo();
            } else if (b == '1') {
                actual = actual.getDerecho();
            } else {
                throw new IllegalArgumentException("Bit inválido en cadena: " + b);
            }

            if (actual == null) {
                throw new IllegalStateException("Ruta inválida en el árbol de Huffman");
            }

            if (actual.esHoja()) {
                sb.append(actual.getSimbolo());
                actual = raiz;
            }
        }

        return sb.toString();
    }
}