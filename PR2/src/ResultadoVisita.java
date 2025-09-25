
import java.util.List;
import java.util.StringJoiner;

public class ResultadoVisita {
    private final List<Integer> ruta;
    private final int callesRecorridas;
    private final int pesoTotal;

    public ResultadoVisita(List<Integer> ruta, int callesRecorridas, int pesoTotal) {
        this.ruta = ruta;
        this.callesRecorridas = callesRecorridas;
        this.pesoTotal = pesoTotal;
    }

    public List<Integer> getRuta() {
        return ruta;
    }

    public int getCallesRecorridas() {
        return callesRecorridas;
    }

    public int getPesoTotal() {
        return pesoTotal;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("->");
        for (Integer p : ruta) sj.add(String.valueOf(p));
        return "Ruta: " + sj.toString() +
               ", Calles: " + callesRecorridas +
               ", Peso: " + pesoTotal;
    }
}
