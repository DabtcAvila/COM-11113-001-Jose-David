
public class AnalizadorTexto {

    // === API ===
    // Acepta líneas tipo: [10, 20, [5, 7]]   o   [15, [8, [4, 6]], 12]   o   9
    public static Nodo parsearLinea(String linea) {
        if (linea == null) throw error("Línea nula");
        Cursor c = new Cursor(linea);
        c.saltarEspacios();
        Nodo raiz = parsearNodo(c);
        c.saltarEspacios();
        if (!c.fin()) throw error("Sobran caracteres después del árbol en pos " + c.pos);
        return raiz;
    }

    // === Parser recursivo ===
    private static Nodo parsearNodo(Cursor c) {
        c.saltarEspacios();
        if (c.fin()) throw error("Fin inesperado de línea");
        char ch = c.peek();

        if (ch == '[') {
            c.get(); // consume '['
            Nodo n = new Nodo(); // interno
            c.saltarEspacios();
            if (c.peek() == ']') { // lista vacía: []
                c.get();
                return n;
            }
            // uno o más elementos separados opcionalmente por comas
            while (true) {
                Nodo hijo = parsearNodo(c);
                n.agregarHijo(hijo);
                c.saltarEspacios();
                if (c.fin()) throw error("Falta ']' para cerrar lista");
                if (c.peek() == ',') { c.get(); c.saltarEspacios(); continue; }
                if (c.peek() == ']') { c.get(); break; }
                // si no hay coma ni ']', también permitimos separación por espacios
            }
            return n;
        }

        // número (hoja)
        if (Character.isDigit(ch)) {
            int val = parsearEntero(c);
            if (val <= 0) throw error("Peso debe ser positivo");
            return new Nodo(val);
        }

        throw error("Token inesperado '" + ch + "' en pos " + c.pos);
    }

    private static int parsearEntero(Cursor c) {
        int start = c.pos;
        while (!c.fin() && Character.isDigit(c.peek())) c.get();
        return Integer.parseInt(c.text.substring(start, c.pos));
    }

    // === Utilidades ===
    private static IllegalArgumentException error(String msg) {
        return new IllegalArgumentException("[AnalizadorTexto] " + msg);
    }

    private static final class Cursor {
        final String text;
        int pos;
        Cursor(String t) { this.text = t; this.pos = 0; }
        boolean fin() { return pos >= text.length(); }
        char peek() { return text.charAt(pos); }
        char get() { return text.charAt(pos++); }
        void saltarEspacios() {
            while (!fin()) {
                char ch = text.charAt(pos);
                if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n') pos++;
                else break;
            }
        }
    }
}
