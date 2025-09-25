

# COM-11113: Análisis de Algoritmos y Complejidad Computacional

**Semestre:** Otoño 2025
**Profesor:** Marco Morales Aguirre

---

## Práctica: Recursión

**Estudiante:** David Fernando Avila Díaz
**Clave Única:** 197851

**Estudiante:** José Gerardo Malfavaun Gorostizaga
**Clave Única:** 213398

---

## Descripción del Problema

Un **centro de distribución de Correos de México** requiere planificar la **recolección de paquetes** en diferentes oficinas postales, distribuidas en forma de árbol.

* El **nodo raíz** representa el **centro de distribución**.
* Cada **hoja** representa una **oficina** con un peso asociado (el total de paquetes a recoger).
* Se recibe un archivo de texto con varios árboles (uno por cada día de la semana).

El programa debe calcular, para cada día:

1. **Ruta seguida** (recorrido de oficinas).
2. **Número de calles visitadas** (aristas recorridas).
3. **Peso total** recolectado.

Se deben implementar **dos versiones del algoritmo**:

1. **Versión Recursiva** (DFS con llamadas recursivas).
2. **Versión Iterativa** (DFS simulada con una pila explícita).

---

## Formato de Entrada

* Archivo de texto donde **cada línea representa un árbol**.
* Los árboles se escriben en **notación con corchetes**.
* Cada hoja contiene un **entero** (peso a recolectar).

Ejemplo de archivo:

```
[10, 20, [5, 7]]
[15, [8, [4, 6]], 12]
[9]
```

---

## Formato de Salida

Para cada línea (día de la semana) se imprime:

```
Recursivo: Ruta: 10->20->5->7, Calles: 6, Peso: 42 Tiempo(ns): 12345
Iterativo: Ruta: 10->20->5->7, Calles: 6, Peso: 42 Tiempo(ns): 14567
------------------------------------
```

---

## Versiones Implementadas

### **Versión Recursiva**

* Usa un **DFS recursivo** para recorrer el árbol.
* En cada llamada baja a un hijo, incrementa calles, recolecta peso y regresa.
* Código en `RecolectorRecursivo.java`.

### **Versión Iterativa**

* Usa una **pila explícita (Stack/Deque)** para simular la recursión.
* Reproduce el mismo orden de visita que la versión recursiva.
* Código en `RecolectorIterativo.java`.

---

## Descripción de Archivos

**Nodo.java**
Clase que representa un nodo del árbol. Puede ser interno (sin peso) o hoja (con peso).

**ArbolRuta.java**
Clase que encapsula un árbol de recolección, con métodos para contar calles y sumar pesos.

**ResultadoVisita.java**
Clase que guarda los resultados de un recorrido: ruta, calles recorridas y peso total.

**AnalizadorTexto.java**
Convierte una línea del archivo de entrada en un `Nodo` raíz. Implementa un parser simple para corchetes y números.

**RecolectorRecursivo.java**
Implementación recursiva del recorrido. Usa DFS con llamadas anidadas.

**RecolectorIterativo.java**
Implementación iterativa del recorrido. Usa una pila para simular DFS.

**Principal.java**
Programa principal.

* Lee el archivo de entrada.
* Ejecuta ambas versiones (recursiva e iterativa).
* Mide tiempos de ejecución con `System.nanoTime()`.
* Imprime resultados en consola.

**Benchmark.java** *(opcional)*
Clase auxiliar para generar árboles sintéticos y medir tiempos de ejecución de ambas versiones con diferentes tamaños.

**datos/ejemplo.txt**
Archivo de ejemplo con varios árboles de prueba.

**resultados/tiempos.csv**
Archivo generado con resultados de benchmark.

**resultados/tiempos.png**
Gráfica comparativa de tiempos recursivo vs iterativo.

---

## Instrucciones de Compilación y Ejecución

### Opción 1: Desde Terminal

**1. Navegar a la carpeta del proyecto**

```bash
cd PR3
```

**2. Compilar todos los archivos**

```bash
javac -d bin src/practica3/*.java
```

**3. Ejecutar el programa principal**

```bash
java -cp bin practica3.Principal datos/ejemplo.txt
```

**4. Limpiar archivos compilados**

```bash
rm -rf bin/*
```

---

### Opción 2: Desde VS Code (Recomendado)

1. Abrir la carpeta del proyecto en **VS Code**.
2. Abrir `Principal.java`.
3. Ejecutar con el botón **Run** o clic derecho → **Run Java**.
4. Ver los resultados en la terminal integrada.

---

## Ver Gráficas

**1. Generar datasets y medir tiempos**

```bash
java -cp bin practica3.Benchmark
```

Esto crea `resultados/tiempos.csv`.

**2. Graficar resultados**

*Puede hacerse en Python, Excel, R o con la clase `Graficador.java` (si se implementa).*

**3. Visualizar**
Abrir `resultados/tiempos.png` en cualquier visor.

---

## Discusión de Resultados

* **Versión Recursiva**

  * Más sencilla de implementar y entender.
  * El tiempo depende de la profundidad del árbol.

* **Versión Iterativa**

  * Usa una pila explícita, evita desbordamiento de pila de Java en árboles muy grandes.
  * Desempeño equivalente a la recursiva.

* **Comparación Teórica vs Práctica**

  * Ambas versiones tienen complejidad **O(n)** en número de nodos.
  * En la práctica, los tiempos son similares.
  * La versión recursiva es más clara, la iterativa es más robusta para árboles grandes.
