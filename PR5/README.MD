# COM-11113: Análisis de Algoritmos y Complejidad Computacional

**Semestre:** Otoño 2025  
**Profesor:** Marco Morales Aguirre

---

## Práctica 5: Algoritmos Codiciosos — Codificación de Huffman

**Estudiante:** David Fernando Avila Díaz — *Clave Única:* 197851  
**Estudiante:** José Gerardo Malfavaun Gorostizaga — *Clave Única:* 213398  

---

## Descripción del Problema

Esta práctica consiste en implementar un sistema de **compresión de texto alfanumérico** utilizando el **algoritmo de Huffman**, un algoritmo codicioso clásico que construye un **código prefijo óptimo** basado en las frecuencias de un alfabeto.

El objetivo es comprender cómo un algoritmo codicioso puede generar soluciones **globalmente óptimas** a partir de decisiones **locales**, utilizando estructuras de datos como **árboles binarios** y **colas de prioridad**.

El programa debe:

- Construir el **árbol y código de Huffman** a partir de un alfabeto y sus frecuencias.
- **Codificar** un texto utilizando el código generado.
- **Decodificar** el texto codificado para verificar su exactitud.
- Medir:
  - **Tiempo de construcción del árbol de Huffman**.
  - **Tiempo de codificación** del texto.
- Generar **estadísticas de compresión**, comparando los bits generados vs los usados por UTF-8.

La práctica permite visualizar cómo la teoría de algoritmos y las estructuras de datos influyen directamente en la eficiencia de la compresión.

---

## Formato de Entrada

El programa recibe un archivo de texto con **tres líneas**:

1. **Línea 1: Alfabeto** (símbolos separados por espacio)

   ```text
   A B C D E

	2.	Línea 2: Frecuencias asociadas a cada símbolo, en el mismo orden

45 13 12 16 9


	3.	Línea 3: Texto a codificar

ABCADEEDCBA



⸻

Formato de Salida

El programa imprime:
	1.	El alfabeto leído.
	2.	Los códigos de Huffman generados, en el mismo orden del alfabeto.
	3.	Estadísticas de compresión:
	•	Número de símbolos del texto original.
	•	Bits generados por la codificación.
	•	Bits utilizados por UTF-8 (8 bits × longitud).
	4.	Tiempo del algoritmo de Huffman (ns).
	5.	Tiempo de codificación (ns).
	6.	El texto codificado (cadena de bits).
	7.	El texto decodificado, que debe coincidir exactamente con el original.

Ejemplo:

    Alfabeto: A B C D E
    Codigos: A=0 B=101 C=100 D=111 E=110
    Simbolos_originales=11 Bits_codificados=23 Bits_UTF8=88
    Tiempo_Huffman(ns)=12345 Tiempo_Codificacion(ns)=6789
    Texto_codificado: 0101110011110110...
    Texto_decodificado: ABCADEEDCBA

⸻

Algoritmo de Huffman

    El algoritmo de Huffman es codicioso porque en cada iteración selecciona los dos nodos con menor frecuencia y los combina. Esta decisión local garantiza, por las propiedades del método, que el resultado final sea un código óptimo, es decir, el que minimiza la longitud promedio de los códigos generados.

¿Por qué funciona la estrategia codiciosa?
	•	Los símbolos más frecuentes deben tener códigos más cortos.
	•	Los símbolos más raros deben alejarse de la raíz (códigos más largos).
	•	Las decisiones locales (combinar los dos mínimos) generan un árbol con mínimo costo global.
	•	El árbol generado es un código prefijo, lo cual evita cualquier ambigüedad durante la decodificación.

    Este comportamiento ha sido probado matemáticamente mediante argumentos de intercambio y demostración de subestructura óptima.

⸻

Pasos del Algoritmo
	1.	Crear un nodo hoja por cada símbolo del alfabeto.
	2.	Insertar todos los nodos en una cola de prioridad (min-heap) ordenada por frecuencia.
	3.	Mientras haya más de un nodo:
	•	Extraer los dos nodos de menor frecuencia.
	•	Crear un nodo interno cuya frecuencia es la suma de ambas.
	•	Insertarlo nuevamente en el heap.
	4.	El único nodo restante corresponde a la raíz del árbol de Huffman.
	5.	Para generar los códigos:
	•	Recorrer el árbol asignando 0 a cada transición hacia el hijo izquierdo.
	•	Asignar 1 a cada transición hacia el hijo derecho.
	•	Al llegar a una hoja, registrar el código.

Ejemplo de códigos obtenidos

    A = 0
    C = 100
    B = 101
    E = 110
    D = 111

⸻

Codificación

    La codificación consiste en reemplazar cada símbolo del texto por su código de Huffman.

Ejemplo:

    ABCADEEDCBA
    → 0 101 100 0 111 ...

    El resultado final es una cadena continua de bits, sin espacios, que representa la versión comprimida del texto.

    Este proceso tiene un costo O(L), donde L es la longitud del texto.

⸻

Decodificación

El proceso de decodificación consiste en recorrer el árbol de Huffman guiado por los bits:
	•	0 → moverse al hijo izquierdo.
	•	1 → moverse al hijo derecho.
	•	Al llegar a una hoja, se obtiene un símbolo y se regresa a la raíz.

    Este procedimiento garantiza que el texto decodificado siempre coincida con el original, ya que los códigos de Huffman son libres de prefijos.

⸻

Complejidad Computacional

    Proceso	Complejidad
    Construcción del árbol	O(n log n)
    Codificación	O(L)
    Decodificación	O(B)
    Espacio utilizado	O(n)

Donde:
	•	n = número de símbolos distintos en el alfabeto.
	•	L = longitud del texto original.
	•	B = número de bits generados tras la codificación.

⸻

Archivos del Proyecto
	•	NodoHuffman.java — Representa los nodos del árbol.
	•	ArbolHuffman.java — Construcción del árbol y generación de códigos.
	•	AnalizadorEntrada.java — Lectura y validación del archivo de entrada.
	•	Codificador.java — Codifica el texto a una cadena de bits.
	•	Decodificador.java — Reconstruye el texto original.
	•	ResultadoHuffman.java — Almacena todo el resultado del proceso.
	•	Principal.java — Control del flujo del programa.
	•	Benchmark.java — Mide tiempos y genera resultados/tiempos.csv.

⸻

Discusión de Resultados
	•	El uso del min-heap permite construir el árbol de manera eficiente.
	•	La codificación crece linealmente con el tamaño del texto.
	•	La compresión lograda suele ser entre 40% y 60% mejor que UTF-8.
	•	La decodificación es exacta debido al uso de un código prefijo.
	•	La práctica evidencia cómo los algoritmos codiciosos pueden garantizar soluciones óptimas cuando están correctamente diseñados.

⸻

Conclusión

    El algoritmo de Huffman demuestra la potencia de los algoritmos codiciosos y la importancia de las estructuras de datos en la construcción de soluciones óptimas.
    Es un método fundamental en compresión real utilizado en PNG, JPEG, MP3, ZIP, GZIP, Deflate y muchos sistemas modernos.

    Esta práctica muestra de forma clara cómo la teoría algorítmica se traduce en aplicaciones reales altamente eficientes y utilizadas globalmente.

---
