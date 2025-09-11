import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // --modo lineal|hash  --entrada <ruta>  [--hash polinomial|suma|xor] [--cubetas <int>]
        String modo = "hash";
        String entrada = null;
        String nombreHash = "polinomial";
        int cubetas = 131071; // primo grande recomendado

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--modo":     modo = args[++i]; break;
                case "--entrada":  entrada = args[++i]; break;
                case "--hash":     nombreHash = args[++i]; break;
                case "--cubetas":  cubetas = Integer.parseInt(args[++i]); break;
            }
        }
        if (entrada == null) {
            System.err.println("Uso: java Principal --modo lineal|hash --entrada <archivo> [--hash polinomial|suma|xor] [--cubetas N]");
            System.exit(2);
        }

        List<Registro> recs = Lector.leerRegistros(new File(entrada));

        int m;
        if ("lineal".equalsIgnoreCase(modo)) {
            m = BuscadorLineal.contarPacientesDuplicadosLineal(recs);
        } else {
            TablaHash.Metodo metodo = parsearMetodo(nombreHash);
            m = BuscadorHash.contarPacientesDuplicadosHash(recs, cubetas, metodo);
        }

        if (m == 0) {
            System.out.println("no hay dos pacientes con registros idénticos");
        } else {
            System.out.println("se encontraron " + m + " pacientes idénticos");
        }
    }

    private static TablaHash.Metodo parsearMetodo(String s) {
        switch (s.toLowerCase()) {
            case "suma": return TablaHash.Metodo.SUMA;
            case "xor":  return TablaHash.Metodo.XOR;
            default:     return TablaHash.Metodo.POLINOMIAL;
        }
    }
}
