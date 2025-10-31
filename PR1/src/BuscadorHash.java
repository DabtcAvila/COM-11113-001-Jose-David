import java.util.*;

public class BuscadorHash {

    /**
     * Inserta todos los registros en TablaHash
     * y devuelve m = suma de tamaños de grupos con tamaño >= 2.
     */
    public static int contarPacientesDuplicadosHash(List<Registro> registros,
                                                    int cubetas,
                                                    TablaHash.Metodo metodo) {
        TablaHash th = new TablaHash(cubetas, metodo);
        for (Registro r : registros) th.agregar(r);
        return th.sumarConteosAlMenos(2);
    }
}
