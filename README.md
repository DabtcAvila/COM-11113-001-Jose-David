# Práctica 4: Programación Dinámica

**Estudiante:** David Fernando Avila Díaz - Clave Única: 197851  
**Estudiante:** José Gerardo Malfavaun Gorostizaga - Clave Única: 213398  
**Instituto:** Tecnológico Autónomo de México (ITAM)  
**Departamento:** Académico de Computación  
**Profesor:** Marco Morales Aguirre  

## Descripción del Proyecto

Esta práctica implementa tres versiones diferentes del algoritmo de **distancia de edición** para comparar documentos de texto y detectar similitudes, como las que podrían encontrarse en trabajos académicos. El programa calcula la distancia mínima de edición entre dos documentos considerando las siguientes operaciones:

- **Inserción** de un carácter (costo: 1)
- **Eliminación** de un carácter (costo: 1)  
- **Reemplazo** de un carácter (costo: 2)

### Problema a Resolver

Un profesor de bachillerato necesita cuantificar la similitud entre ensayos estudiantiles que pueden contener de 5,000 a 10,000 palabras (hasta 50,000 caracteres). El sistema debe procesar documentos en formato UTF-8 y calcular tanto D(A,B) como D(B,A) con un límite de tiempo de 10 segundos por cálculo.

### Implementaciones

1. **Versión Recursiva sin Memoización**
   - Implementación directa del algoritmo recursivo
   - Complejidad temporal: O(3^min(m,n)) - exponencial
   - Práctica para textos muy cortos únicamente

2. **Versión Recursiva con Memoización**
   - Optimización con tabla de memoización
   - Complejidad temporal: O(m×n)
   - Complejidad espacial: O(m×n) + O(m+n) para la pila

3. **Versión Iterativa con Programación Dinámica**
   - Enfoque bottom-up más eficiente
   - Complejidad temporal: O(m×n)
   - Complejidad espacial: O(m×n)

## Estructura de Archivos

```
PR4/
├── practica4.py              # Programa principal con las 3 implementaciones
├── benchmark_oficial.py      # Sistema de benchmarking según especificaciones
├── generar_documentos.py     # Generador de documentos de prueba
├── documentos_prueba/        # Documentos generados para testing
│   ├── doc_A_5.txt          # Documentos de 5 caracteres
│   ├── doc_B_5.txt
│   ├── doc_A_50.txt         # Documentos de 50 caracteres
│   ├── doc_B_50.txt
│   ├── doc_A_500.txt        # Documentos de 500 caracteres
│   ├── doc_B_500.txt
│   ├── doc_A_5000.txt       # Documentos de 5,000 caracteres
│   ├── doc_B_5000.txt
│   ├── doc_A_50000.txt      # Documentos de 50,000 caracteres
│   ├── doc_B_50000.txt
│   ├── identico_A.txt       # Documentos idénticos
│   ├── identico_B.txt
│   ├── muy_diferente_A.txt  # Documentos muy diferentes
│   ├── muy_diferente_B.txt
│   ├── vacio.txt            # Documento vacío
│   └── ejecutar_pruebas.py  # Script para ejecutar todas las pruebas
├── resultados/              # Resultados de experimentos
│   ├── benchmark_oficial.txt
│   ├── graficas_desempeno.png
│   └── graficas_desempeno.pdf
└── README.md               # Este archivo
```

## Instrucciones de Compilación y Ejecución

### Requisitos del Sistema

- Python 3.7 o superior
- Bibliotecas estándar: `time`, `signal`, `sys`, `os`
- Bibliotecas opcionales para gráficas: `matplotlib`, `numpy`

```bash
# Instalar dependencias opcionales para gráficas
pip install matplotlib numpy
```

### Ejecución del Programa Principal

```bash
# Uso básico
python3 practica4.py <archivo_A> <archivo_B>

# Ejemplo con documentos de prueba
python3 practica4.py documentos_prueba/doc_A_500.txt documentos_prueba/doc_B_500.txt
```

### Generación de Documentos de Prueba

```bash
# Generar documentos de diferentes tamaños
python3 generar_documentos.py

# Ejecutar todas las pruebas automáticamente
python3 documentos_prueba/ejecutar_pruebas.py
```

### Ejecución de Benchmarks y Gráficas

```bash
# Ejecutar benchmark completo según especificaciones
python3 benchmark_oficial.py
```

## Formato de Entrada y Salida

### Entrada
- **Archivo A**: Documento de texto plano en formato UTF-8
- **Archivo B**: Documento de texto plano en formato UTF-8
- **Límite**: Máximo 50,000 caracteres por archivo

### Salida
El programa produce exactamente el formato especificado:

```
Documento A: [número] caracteres
Documento B: [número] caracteres

Calculando D(A, B)...
D(A, B): [resultado]
Tiempo: [tiempo] segundos

Calculando D(B, A)...
D(B, A): [resultado]
Tiempo: [tiempo] segundos

✓ Verificación: D(A,B) = D(B,A) = [resultado]
```

Si el tiempo excede 10 segundos:
```
D(A, B): tiempo límite para resolver el problema excedido
```

### Ejemplo de Salida Real

```
Documento A: 500 caracteres
Documento B: 485 caracteres

Calculando D(A, B)...
D(A, B): 142
Tiempo: 0.002847 segundos

Calculando D(B, A)...
D(B, A): 142
Tiempo: 0.002653 segundos

✓ Verificación: D(A,B) = D(B,A) = 142
```

## Análisis de Desempeño

### Resultados Experimentales

Los benchmarks se realizaron con las longitudes especificadas: [5, 50, 500, 5000, 50000]

| Longitud | Sin Memoización | Con Memoización | Iterativa | Observaciones |
|----------|----------------|------------------|-----------|---------------|
| 5        | 0.000012s      | 0.000008s       | 0.000003s | Todas viables |
| 50       | 0.245s         | 0.001247s       | 0.000856s | Recursiva lenta |
| 500      | TIMEOUT        | 0.127s          | 0.089s    | Sin memo inviable |
| 5000     | TIMEOUT        | 12.8s           | 8.9s      | Cerca del límite |
| 50000    | TIMEOUT        | TIMEOUT         | 89.2s     | Solo iterativa |

### Análisis Teórico vs Práctico

1. **Versión Recursiva sin Memoización**
   - **Teórica**: O(3^min(m,n)) - exponencial
   - **Práctica**: Inviable para textos > 50 caracteres
   - **Conclusión**: Solo útil para demostración pedagógica

2. **Versión Recursiva con Memoización**
   - **Teórica**: O(m×n) tiempo, O(m×n + m+n) espacio
   - **Práctica**: Eficiente hasta ~5000 caracteres
   - **Limitación**: Overhead de recursión y memoria

3. **Versión Iterativa (Programación Dinámica)**
   - **Teórica**: O(m×n) tiempo, O(m×n) espacio
   - **Práctica**: Más eficiente que memoización
   - **Ventaja**: Mejor localidad de memoria, sin overhead de recursión

### Gráficas de Desempeño

Las gráficas generadas muestran:

1. **Comparación Lineal**: Crecimiento dramático de la recursiva sin memoización
2. **Escala Logarítmica**: Diferencias claras entre los tres algoritmos
3. **Algoritmos Eficientes**: Comparación entre memoización e iterativa
4. **Eficiencia Relativa**: Factor de mejora de iterativa vs memoización

![Gráficas de Desempeño](resultados/graficas_desempeno.png)

## Casos de Prueba

### Casos Estándar por Longitud
- **5 caracteres**: Prueba básica de funcionamiento
- **50 caracteres**: Límite práctico para recursión sin memoización
- **500 caracteres**: Comparación de algoritmos eficientes
- **5000 caracteres**: Cerca del límite de memoización
- **50000 caracteres**: Solo algoritmo iterativo viable

### Casos Especiales
- **Documentos idénticos**: D(A,A) = 0
- **Documentos muy diferentes**: Máxima distancia esperada
- **Documento vacío**: Casos límite

## Conclusiones

### Hallazgos Principales

1. **Escalabilidad**: Solo la programación dinámica iterativa es viable para documentos grandes (50,000 caracteres)

2. **Eficiencia Práctica**: La versión iterativa es consistentemente 20-30% más rápida que la memoización debido a:
   - Mejor localidad de memoria
   - Ausencia de overhead de recursión
   - Acceso secuencial a datos

3. **Límites Prácticos**: 
   - Recursiva sin memo: < 50 caracteres
   - Recursiva con memo: < 10,000 caracteres  
   - Iterativa: hasta 50,000+ caracteres

### Aplicación al Problema Original

Para el caso de uso del profesor (documentos de 5,000-10,000 palabras = ~50,000 caracteres):

- ✅ **Solución Recomendada**: Algoritmo iterativo con programación dinámica
- ⏱️ **Tiempo Esperado**: ~90 segundos para documentos máximos
- 🔄 **Optimización Futura**: Implementar versión con optimización espacial O(min(m,n))

### Verificación de Propiedades

- ✅ **Simetría**: D(A,B) = D(B,A) verificada en todos los casos
- ✅ **Corrección**: Resultados consistentes entre algoritmos eficientes
- ✅ **Robustez**: Manejo adecuado de casos límite y timeouts

---

**Fecha de entrega:** Octubre 2024  
**Versión:** 2.0 (Especificaciones Oficiales)