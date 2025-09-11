import java.io.*;
import java.util.*;

public class Lector {
    // Lee: primera línea n (1..100000), luego n líneas con 10 enteros (0..10_000_000)
    public static List<Registro> leerRegistros(File f) throws IOException {
        List<Registro> registros = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea = br.readLine();
            if (linea == null) throw new IOException("Archivo vacío");
            int n = Integer.parseInt(linea.trim());
            for (int i = 0; i < n; i++) {
                String li = br.readLine();
                if (li == null) throw new IOException("Faltan líneas respecto a n");
                registros.add(Registro.parsearLinea(li));
            }
        }
        return registros;
    }
}
