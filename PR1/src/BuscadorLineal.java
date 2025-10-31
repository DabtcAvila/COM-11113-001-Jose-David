import java.util.*;

public class BuscadorLineal {

    /**
     * Devuelve m = total de pacientes que pertenecen a algún grupo de registros idénticos
     * (dos pares → m=4; un trío → m=3; sin duplicados → m=0).
     * Solo comparaciones sobre la lista (sin hash, sin ordenar).
     */
    public static int contarPacientesDuplicadosLineal(List<Registro> registros) {
        int n = registros.size();
        if (n <= 1) return 0;
        boolean[] contado = new boolean[n];
        int total = 0;

        for (int i = 0; i < n; i++) {
            if (contado[i]) continue;
            List<Integer> grupo = new ArrayList<>();
            grupo.add(i);
            for (int j = i + 1; j < n; j++) {
                if (!contado[j] && iguales(registros.get(i), registros.get(j))) {
                    grupo.add(j);
                }
            }
            if (grupo.size() >= 2) {
                for (int idx : grupo) contado[idx] = true;
                total += grupo.size();
            }
        }
        return total;
    }

    private static boolean iguales(Registro a, Registro b) {
        int[] x = a.datos(), y = b.datos();
        for (int i = 0; i < 10; i++) if (x[i] != y[i]) return false;
        return true;
    }
}
