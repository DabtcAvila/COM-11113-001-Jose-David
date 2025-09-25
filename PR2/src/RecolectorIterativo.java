
import java.util.*;

public class RecolectorIterativo {

    public static ResultadoVisita recolectar(ArbolRuta arbol) {
        List<Integer> ruta = new ArrayList<>();
        int calles = 0;

        Deque<Nodo> pila = new ArrayDeque<>();
        Deque<Integer> idx = new ArrayDeque<>();
        pila.push(arbol.getRaiz());
        idx.push(0);

        while (!pila.isEmpty()) {
            Nodo actual = pila.peek();
            int i = idx.pop();

            if (actual.esHoja()) {
                ruta.add(actual.getPeso());
                pila.pop();
                if (!pila.isEmpty()) calles++; // regresar
                continue;
            }

            if (i < actual.getHijos().size()) {
                idx.push(i+1);
                Nodo hijo = actual.getHijos().get(i);
                calles++; // bajar
                pila.push(hijo);
                idx.push(0);
            } else {
                pila.pop();
                if (!pila.isEmpty()) calles++; // regresar
            }
        }

        int pesoTotal = 0;
        for (int p : ruta) pesoTotal += p;

        return new ResultadoVisita(ruta, calles, pesoTotal);
    }
}
