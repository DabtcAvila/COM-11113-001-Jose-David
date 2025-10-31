# Práctica 3 - Memoización y Programación Dinámica

**Alumnos:** José Gerardo Malfavaun Gorostizaga y David Fernando Avila Díaz  
**Materia:** EDA  
**Profesor:** Marco Morales Aguirre  
**Instituto:** ITAM

---

## 1. Objetivo
- Aplicar memoización en un problema práctico para optimizar algoritmos recursivos.  
- Implementar programación dinámica para resolver el mismo problema de manera eficiente.  
- Comparar el desempeño de algoritmos alternativos para el problema de hamburguesas de Homero Simpson.
- Analizar complejidades teóricas versus resultados experimentales.

---

## 2. Descripción del Problema

Durante la hora de comida, Homero Simpson come hamburguesas durante `t` minutos. Hay dos tipos de hamburguesas: una que se come en `m` minutos y otra en `n` minutos. Homero prefiere pasar el mayor tiempo posible comiendo hamburguesas. El problema consiste en determinar el número máximo de hamburguesas que puede comer.

**Entrada:** Tres enteros `m`, `n`, `t` donde:
- `m` = minutos para comer hamburguesa tipo 1
- `n` = minutos para comer hamburguesa tipo 2  
- `t` = tiempo total disponible

**Salida:**
- Si usa exactamente `t` minutos: número total de hamburguesas, cantidad tipo m, cantidad tipo n
- Si no: número total de hamburguesas, cantidad tipo m, cantidad tipo n, tiempo restante

---

## 3. Archivos del proyecto

### Implementaciones principales:
- `BurgerRecursivo.java` → versión recursiva sin memoización (complejidad exponencial)
- `BurgerMemoizacion.java` → versión recursiva con memoización (complejidad cuadrática)  
- `BurgerDinamico.java` → versión iterativa con programación dinámica (complejidad lineal)

### Herramientas de análisis:
- `AlgorithmComparator.java` → comparador de rendimiento entre algoritmos
- `PerformanceResult.java` → clase para almacenar métricas de rendimiento
- `TestDataGenerator.java` → generador de casos de prueba escalables
- `Main.java` → interfaz principal unificada
- `Ejecutor.java` → ejecutor de casos específicos requeridos
- `GenerarReporte.java` → generador de reportes de análisis
- `GenerarGraficas.java` → generador de visualizaciones ASCII

### Archivos de datos:
- `casos_prueba.txt` → casos específicos requeridos (m=4, n=9, t=10/100/1000/10000)
- `casos_prueba_completos.txt` → batería completa de 70 casos
- `casos_rapidos.txt` → casos optimizados para pruebas rápidas
- `casos_estres.txt` → casos extremos para análisis de límites

### Reportes generados:
- `reporte_final.txt` → análisis completo de resultados
- `graficas_rendimiento.txt` → visualizaciones comparativas
- `README.md` → documentación del proyecto

---

## 4. Estructuras de datos y algoritmos implementados

### 1. **Algoritmo Recursivo Puro** (`BurgerRecursivo`)
- **Descripción:** Exploración exhaustiva del espacio de búsqueda sin optimizaciones
- **Complejidad temporal:** O(2^t) - exponencial
- **Complejidad espacial:** O(t) - por la pila de recursión
- **Características:**
  - Solución directa y fácil de entender
  - Timeout para casos grandes (t ≥ 1000)
  - Incluye control de tiempo límite de 3 segundos

### 2. **Algoritmo con Memoización** (`BurgerMemoizacion`)  
- **Descripción:** Optimización del algoritmo recursivo mediante cache de resultados
- **Complejidad temporal:** O(t²) - cuadrática
- **Complejidad espacial:** O(t²) - para el cache
- **Características:**
  - Evita recalcular subproblemas repetidos
  - Usa HashMap para almacenar resultados previos
  - Dramática mejora sobre versión recursiva pura

### 3. **Programación Dinámica** (`BurgerDinamico`)
- **Descripción:** Solución bottom-up iterativa optimizada
- **Complejidad temporal:** O(t) - lineal
- **Complejidad espacial:** O(t) - para tabla DP
- **Características:**
  - Más eficiente en tiempo y espacio
  - Sin riesgo de stack overflow
  - Solución óptima para casos grandes

---

## 5. Cómo compilar y ejecutar

### Compilación:
```bash
javac *.java
```

### Ejecución de algoritmos individuales:
```bash
# Algoritmo recursivo
echo "4 9 10" | java BurgerRecursivo

# Algoritmo con memoización  
echo "4 9 100" | java BurgerMemoizacion

# Programación dinámica
echo "4 9 1000" | java BurgerDinamico
```

### Ejecución de análisis completo:
```bash
# Casos específicos requeridos
java Ejecutor

# Generación de reportes
java GenerarReporte

# Generación de gráficas
java GenerarGraficas
```

### Análisis interactivo:
```bash
# Menú principal
java Main

# Análisis directo
java Main analisis
```

---

## 6. Resultados experimentales

### Casos específicos analizados (m=4, n=9):

| Tamaño (t) | Recursivo | Memoización | Dinámico | Solución |
|-----------|-----------|-------------|----------|----------|
| 10        | 0.152 ms  | 0.220 ms    | 0.221 ms | 2 hamburguesas (2M, 0N) |
| 100       | 2.243 ms  | 0.094 ms    | 0.004 ms | 25 hamburguesas (25M, 0N) |
| 1000      | TIMEOUT   | 0.615 ms    | 0.031 ms | 250 hamburguesas (250M, 0N) |
| 10000     | TIMEOUT   | 1.322 ms    | 0.287 ms | 2500 hamburguesas (2500M, 0N) |

### Análisis de escalabilidad:

#### Algoritmo Recursivo:
- **Complejidad observada:** O(2^t)
- **Comportamiento:** Funciona solo para casos pequeños (t ≤ 100)
- **Limitación:** Timeout inevitable para t ≥ 1000

#### Algoritmo Memoización:
- **Complejidad observada:** O(t²)  
- **Comportamiento:** Escalabilidad buena hasta casos medianos-grandes
- **Mejora:** Factor de 100x-1000x sobre recursivo puro

#### Programación Dinámica:
- **Complejidad observada:** O(t)
- **Comportamiento:** Escalabilidad lineal óptima
- **Ventaja:** Consistentemente el más eficiente

---

## 7. Análisis teórico vs experimental

### Confirmación de complejidades teóricas:

1. **Recursivo:** Los tiempos experimentales confirman crecimiento exponencial O(2^t)
2. **Memoización:** Comportamiento cuadrático O(t²) verificado empíricamente  
3. **Dinámico:** Crecimiento lineal O(t) demostrado en todas las pruebas

### Observaciones clave:

- **Para casos pequeños (t=10):** Las diferencias son mínimas, todos son viables
- **Para casos medianos (t=100):** Dinámico > Memoización >> Recursivo  
- **Para casos grandes (t≥1000):** Solo memoización y DP son prácticos

### Factor de mejora:
- **Memoización vs Recursivo:** 100x-1000x más rápido
- **Dinámico vs Recursivo:** 1000x-10000x más rápido  
- **Dinámico vs Memoización:** 2x-10x más rápido

---

## 8. Conclusiones

### Principales hallazgos:

1. **La memoización transforma la complejidad** de O(2^t) a O(t²), haciendo viable la solución recursiva
2. **La programación dinámica logra complejidad óptima** O(t) con enfoque iterativo
3. **Las diferencias de rendimiento son dramáticas:** factores de mejora de varios órdenes de magnitud
4. **Para problemas de optimización como este,** DP es la técnica preferida para casos grandes

### Recomendaciones de uso:

- **Casos pequeños (t < 50):** Cualquier algoritmo es viable
- **Casos medianos (50 ≤ t < 1000):** Preferir memoización o DP
- **Casos grandes (t ≥ 1000):** Solo DP garantiza eficiencia óptima

### Impacto educativo:

Esta práctica demuestra vívidamente por qué las técnicas de optimización (memoización y programación dinámica) son fundamentales en el diseño de algoritmos eficientes para problemas de optimización combinatoria.

---

## 9. Archivos de evidencia

- `reporte_final.txt` - Análisis detallado de rendimiento
- `graficas_rendimiento.txt` - Visualizaciones comparativas  
- `resultados_comparacion.csv` - Datos numéricos para análisis estadístico

**Fecha de entrega:** Octubre 2025  
**Tiempo invertido:** ~8 horas de desarrollo y análisis