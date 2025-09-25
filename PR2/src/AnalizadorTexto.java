    
import java.util.*;

public class AnalizadorTexto {

    // Ejemplo de formato aceptado: [10,20,[5,7]]
    public static Nodo parsearLinea(String linea) {
        linea = linea.trim();
        if (linea.startsWith("[") && linea.endsWith("]")) {
            return parsearLista(new StringTokenizer(linea, "[], ", true));
        } else {
            // línea simple: un número
            return new Nodo(Integer.parseInt(linea));
        }
    }

    private static Nodo parsearLista(StringTokenizer st) {
        Nodo nodo = new Nodo();
        while (st.hasMoreTokens()) {
            String tok = st.nextToken().trim();
            if (tok.equals("[") || tok.equals(",")) {
                continue;
            } else if (tok.equals("]")) {
                break;
            } else {
                if (tok.startsWith("[")) {
                    nodo.agregarHijo(parsearLista(new StringTokenizer(tok, "[], ", true)));
                } else {
                    try {
                        int valor = Integer.parseInt(tok);
                        nodo.agregarHijo(new Nodo(valor));
                    } catch (NumberFormatException e) {
                        // ignorar
                    }
                }
            }
        }
        return nodo;
    }
}
