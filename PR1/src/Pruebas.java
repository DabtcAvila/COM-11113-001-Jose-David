import java.io.*;
import java.util.*;

public class Pruebas {

    private static final int[] TAMANOS = {1_000, 5_000, 10_000, 20_000, 40_000};
    private static final Random RNG = new Random(12345);

    public static void main(String[] args) throws Exception {
        File dirDatos = new File("datos");
        File dirRes = new File("resultados");
        dirDatos.mkdirs();
        dirRes.mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(dirRes, "tiempos.csv")))) {
            pw.println("n,modo,hash,cubetas,milisegundos");

            for (int n : TAMANOS) {
                File f = new File(dirDatos, "sintetico_" + n + ".txt");
                generarDatos(f, n, /*duplicarCada*/ 500);
                List<Registro> recs = Lector.leerRegistros(f);

                // Lineal
                long t0 = System.nanoTime();
                int mLin = BuscadorLineal.contarPacientesDuplicadosLineal(recs);
                long t1 = System.nanoTime();
                pw.printf(Locale.US, "%d,lineal,NA,NA,%.3f%n", n, (t1 - t0) / 1e6);

                // Hash (3 variantes)
                for (TablaHash.Metodo metodo : TablaHash.Metodo.values()) {
                    long h0 = System.nanoTime();
                    int mHash = BuscadorHash.contarPacientesDuplicadosHash(recs, 131071, metodo);
                    long h1 = System.nanoTime();
                    pw.printf(Locale.US, "%d,hash,%s,%d,%.3f%n",
                            n, metodo.name().toLowerCase(), 131071, (h1 - h0) / 1e6);

                    if (mHash != mLin) {
                        System.err.println("Aviso: m difiere entre lineal y hash en n=" + n +
                                " (" + mLin + " vs " + mHash + ")");
                    }
                }
                System.out.println("OK n=" + n + " (m=" + mLin + ")");
            }
        }
        System.out.println("Listo: resultados/tiempos.csv");
    }

    private static void generarDatos(File salida, int n, int duplicarCada) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(salida))) {
            pw.println(n);
            List<int[]> base = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int[] arr = new int[10];
                for (int j = 0; j < 10; j++) arr[j] = RNG.nextInt(10_000_001);
                base.add(arr);
            }
            // Inyectar duplicados para asegurar m>0
            for (int i = 0; i < n; i++) {
                if (i % duplicarCada == 0 && i + 1 < n) {
                    base.set(i + 1, Arrays.copyOf(base.get(i), 10));
                }
            }
            for (int[] rec : base) {
                for (int j = 0; j < 10; j++) {
                    if (j > 0) pw.print(' ');
                    pw.print(rec[j]);
                }
                pw.println();
            }
        }
    }
}
