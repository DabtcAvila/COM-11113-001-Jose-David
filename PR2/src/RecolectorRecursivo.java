
import java.util.ArrayList;
import java.util.List;

public class RecolectorRecursivo {

    public static ResultadoVisita recolectar(ArbolRuta arbol) {
        List<Integer> ruta = new ArrayList<>();
        int[] calles = {0};

        dfs(arbol.getRaiz(), ruta, calles);

        int pesoTotal = 0;
        for (int p : ruta) pesoTotal += p;

        return new ResultadoVisita(ruta, calles[0], pesoTotal);
    }

    private static void dfs(Nodo nodo, List<Integer> ruta, int[] calles) {
        if (nodo.esHoja()) {
            ruta.add(nodo.getPeso());
            return;
        }
        for (Nodo h : nodo.getHijos()) {
            calles[0]++;     // ir hacia abajo
            dfs(h, ruta, calles);
            calles[0]++;     // regresar
        }
    }
}
