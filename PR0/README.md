# COM-11113: Análisis de Algoritmos y Complejidad Computacional

**Semestre:** Otoño 2025
**Profesor:** Marco Morales Aguirre


## Práctica 0

**Estudiante:** David Fernando Avila Díaz
**Clave Única:** 197851

**Estudiante:** José Gerardo Malfavaun Gorostizaga
**Clave Única:** 213398


## Descripción de Archivos

**Patient.java**: Clase que representa un paciente con id, prioridad (1-10), tiempo de llegada y nombre.
**TriageSystem.java**: Interfaz que define las operaciones básicas: insert(), extractMin(), search(), isEmpty() y size().
**MinHeapTriage.java**: Implementación usando un Min Heap con array. Mantiene el paciente más urgente en la raíz.
**SortedLinkedListTriage.java**: Implementación usando lista ligada ordenada por prioridad. Mantiene todos los elementos ordenados.
**Main.java**: Programa principal que prueba ambas estructuras con 1000 pacientes y mide tiempos de ejecución.
**graficas.html**: Visualización de resultados comparativos en gráficas de barras.


## Instrucciones de Compilación y Ejecución

### Opción 1: Desde Terminal

**Navegar a la carpeta del proyecto**
cd PR0

**Compilar todos los archivos Java**
javac -d bin src/*.java

**Ejecutar el programa principal**
java -cp bin Main

**Para limpiar archivos compilados**
rm -rf bin/*

### Opción 2: Desde VS Code (Recomendado)
1. Abrir la carpeta del proyecto en VS Code
2. Abrir el archivo `Main.java`
3. Hacer click en el botón "Run" que aparece sobre `public static void main`
   - Alternativa: Click derecho → "Run Java"
   - Atajo: Ctrl+F5 (Windows/Linux) o Cmd+F5 (Mac)
4. Los resultados aparecerán en la terminal integrada de VS Code


## Ver gráficas
Abrir graficas.html en cualquier navegador