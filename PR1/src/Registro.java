import java.util.Arrays;

public class Registro {
    // 10 enteros por especificación
    private final int[] v = new int[10];

    public Registro(int[] arr) {
        if (arr == null || arr.length != 10)
            throw new IllegalArgumentException("Se esperan 10 enteros por registro");
        System.arraycopy(arr, 0, this.v, 0, 10);
    }

    public static Registro parsearLinea(String linea) {
        String[] partes = linea.trim().split("\\s+");
        if (partes.length != 10)
            throw new IllegalArgumentException("Cada línea debe tener exactamente 10 enteros");
        int[] arr = new int[10];
        for (int i = 0; i < 10; i++) arr[i] = Integer.parseInt(partes[i]);
        return new Registro(arr);
    }

    public int[] datos() { return v; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Registro)) return false;
        Registro r = (Registro) o;
        return Arrays.equals(this.v, r.v);
    }

    @Override public int hashCode() { return Arrays.hashCode(v); }
}
