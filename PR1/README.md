
# COM-11113: Análisis de Algoritmos y Complejidad Computacional

**Semestre:** Otoño 2025
**Profesor:** Marco Morales Aguirre

---

## Práctica 1: Tablas de Hash

**Estudiante:** David Fernando Avila Díaz
**Clave Única:** 197851

**Estudiante:** José Gerardo Malfavaun Gorostizaga
**Clave Única:** 213398

---

## Descripción del Problema

En un centro de salud se tiene un conjunto de **datos de estudios de laboratorio** de los pacientes que son atendidos.

* El número de estudios distintos es **10**.
* Cada estudio se representa con un **número entero**.
* La información de los pacientes se almacena en un archivo de texto.

### **Formato de Entrada**

* La **primera línea** contiene un entero `n`, con `1 ≤ n ≤ 100000`.
* Las siguientes `n` líneas contienen exactamente **10 enteros** separados por espacio o tabulador.
* Cada entero está en el rango `[0, 10,000,000]`.

### **Formato de Salida**

* Si **no existen pacientes con registros idénticos**:

  ```
  no hay dos pacientes con registros idénticos
  ```

* Si **sí existen duplicados**:

  ```
  se encontraron m pacientes idénticos
  ```

  donde `m` es el **total de pacientes que pertenecen a grupos duplicados**.

  * Ejemplo: si hay dos pares duplicados, `m = 4`.
  * Ejemplo: si hay un grupo de tres idénticos, `m = 3`.

---

## Versiones Implementadas

La práctica requiere **dos soluciones al mismo problema**, utilizando diferentes estructuras de datos:

1. **Versión Lineal (Arreglo simple)**

   * Revisa todos los registros uno por uno.
   * Complejidad **O(n²)** en el peor caso.
   * No usa estructuras adicionales de búsqueda.
   * Poco eficiente para valores grandes de `n`.

2. **Versión con Tabla Hash**

   * Usa una tabla hash implementada con **encadenamiento separado**.
   * Ofrece un desempeño **O(n)** amortizado si la función de hash y el número de cubetas son adecuados.
   * Se implementaron tres funciones de hash para comparar:

     * **Polinomial**
     * **Suma**
     * **XOR**
   * El número de cubetas puede configurarse; se recomienda un **primo grande** para reducir colisiones.

---

## Descripción de Archivos

**Registro.java**
Clase que representa un registro de 10 enteros. Incluye métodos para crear el registro desde una línea de texto y compararlo con otros registros.

**Lector.java**
Clase encargada de leer el archivo de entrada, validar el formato y devolver una lista de objetos `Registro`.

**BuscadorLineal.java**
Implementación de la búsqueda con un arreglo lineal. Compara cada registro con todos los demás para detectar duplicados. Complejidad **O(n²)**.

**TablaHash.java**
Clase que implementa la **tabla de hash** con encadenamiento. Incluye tres funciones de hash (polinomial, suma, XOR) y permite almacenar los registros junto con su número de apariciones.

**BuscadorHash.java**
Clase que utiliza `TablaHash` para detectar registros duplicados de manera eficiente. Devuelve el total de pacientes duplicados.

**Principal.java**
Programa principal.

* Recibe parámetros por línea de comandos.
* Puede ejecutarse en modo **lineal** o **hash**.
* Imprime exactamente los textos solicitados por la práctica.

**BancoPruebas.java**
Generador de datos sintéticos y medidor de tiempos de ejecución.

* Crea archivos de prueba de diferentes tamaños.
* Mide los tiempos de las dos implementaciones.
* Genera el archivo `resultados/tiempos.csv` con los resultados.

**Graficador.java**
Lee el archivo CSV de resultados y genera la gráfica `resultados/tiempos.png`, comparando el desempeño de la versión lineal vs la versión hash.

**datos/ejemplo\_pequeno.txt**
Archivo de ejemplo con pocos registros (incluye algunos duplicados) para probar el funcionamiento del programa.

**resultados/tiempos.csv**
Archivo generado automáticamente con los tiempos de ejecución en milisegundos para distintos tamaños de entrada.

**resultados/tiempos.png**
Gráfica comparativa generada a partir de los resultados del benchmark.

---

## Instrucciones de Compilación y Ejecución

### Opción 1: Desde Terminal

**1. Navegar a la carpeta del proyecto**

```bash
cd PR1
```

**2. Compilar todos los archivos Java**

```bash
javac -d bin src/*.java
```

**3. Ejecutar el programa principal**

```bash
# Modo lineal
java -cp bin Principal --modo lineal --entrada datos/ejemplo_pequeno.txt

# Modo hash (función polinomial, 131071 cubetas)
java -cp bin Principal --modo hash --entrada datos/ejemplo_pequeno.txt --hash polinomial --cubetas 131071
```

**4. Limpiar archivos compilados**

```bash
rm -rf bin/*
```

---

### Opción 2: Desde VS Code (Recomendado)

1. Abrir la carpeta del proyecto en **VS Code**.
2. Abrir el archivo `Principal.java`.
3. Hacer click en el botón **Run** que aparece sobre `public static void main`.

   * Alternativa: Click derecho → **Run Java**.
   * Atajo: `Ctrl+F5` (Windows/Linux) o `Cmd+F5` (Mac).
4. Los resultados aparecerán en la terminal integrada de VS Code.

---

## Ver Gráficas

**1. Generar datasets y medir tiempos**

```bash
java -cp bin BancoPruebas
```

Esto crea `resultados/tiempos.csv`.

**2. Generar la gráfica de resultados**

```bash
java -cp bin Graficador
```

Esto crea `resultados/tiempos.png`.

**3. Visualizar la gráfica**
Abrir `resultados/tiempos.png` en cualquier visor de imágenes.

---

## Discusión de Resultados

* **Versión Lineal**

  * El tiempo de ejecución crece de forma cuadrática (**O(n²)**).
  * Esto la hace poco práctica para `n` grandes.

* **Versión Hash**

  * El tiempo de ejecución crece aproximadamente de forma lineal (**O(n)** amortizado).
  * La calidad del desempeño depende de la **función de hash** y del número de **cubetas** elegidas.
  * La función polinomial con un número primo grande de cubetas mostró el mejor comportamiento.

* **Comparación Teórica vs Práctica**

  * El análisis teórico predice que el algoritmo lineal es cuadrático y el hash es lineal.
  * Las gráficas (`resultados/tiempos.png`) confirman esta diferencia:

    * La curva lineal crece mucho más rápido.
    * La curva hash se mantiene proporcional a `n`.
  * Pequeñas desviaciones se explican por:

    * Colisiones en la tabla hash.
    * Variaciones en el hardware y manejo de memoria en Java.