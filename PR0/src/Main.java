public class Main {
    public static void main(String[] args) {
        System.out.println("=== Sistema de Triage Hospitalario ===\n");
        
        testBasicFunctionality();
        testPerformanceComparison();
    }
    
    private static void testBasicFunctionality() {
        System.out.println("--- Prueba de Funcionalidad Básica ---");
        
        MinHeapTriage heap = new MinHeapTriage();
        SortedLinkedListTriage list = new SortedLinkedListTriage();
        
        Patient p1 = new Patient(1, 3, 1000, "Juan Pérez");
        Patient p2 = new Patient(2, 1, 1500, "María García");
        Patient p3 = new Patient(3, 5, 2000, "Pedro López");
        Patient p4 = new Patient(4, 1, 1200, "Ana Martínez");
        
        System.out.println("\nInsertando pacientes...");
        heap.insert(p1); list.insert(p1);
        heap.insert(p2); list.insert(p2);
        heap.insert(p3); list.insert(p3);
        heap.insert(p4); list.insert(p4);
        
        System.out.println("Heap size: " + heap.size() + ", List size: " + list.size());
        
        System.out.println("\nBúsqueda de paciente ID 3:");
        System.out.println("Heap: " + heap.search(3));
        System.out.println("List: " + list.search(3));
        
        System.out.println("\nExtrayendo pacientes por prioridad:");
        System.out.println("Heap - Siguiente: " + heap.extractMin());
        System.out.println("List - Siguiente: " + list.extractMin());
        
        System.out.println("Heap - Siguiente: " + heap.extractMin());
        System.out.println("List - Siguiente: " + list.extractMin());

        System.out.println("Heap - Siguiente: " + heap.extractMin());
        System.out.println("List - Siguiente: " + list.extractMin());

        System.out.println("Heap - Siguiente: " + heap.extractMin());
        System.out.println("List - Siguiente: " + list.extractMin());
    }
    
    private static void testPerformanceComparison() {
        System.out.println("\n\n--- Comparación de Rendimiento ---");
        
        for (int numPatients = 100; numPatients <= 1000; numPatients += 100) {
            MinHeapTriage heap = new MinHeapTriage();
            SortedLinkedListTriage list = new SortedLinkedListTriage();
            
            Patient[] patients = generateRandomPatients(numPatients);
            
            System.out.println("\nPrueba con " + numPatients + " pacientes:");
            
            long startTime, endTime;
            
            startTime = System.nanoTime();
            for (Patient p : patients) {
                heap.insert(p);
            }
            endTime = System.nanoTime();
            System.out.println("Heap Insert: " + (endTime - startTime) / 1000000.0 + " ms");
            
            startTime = System.nanoTime();
            for (Patient p : patients) {
                list.insert(p);
            }
            endTime = System.nanoTime();
            System.out.println("List Insert: " + (endTime - startTime) / 1000000.0 + " ms");
            
            startTime = System.nanoTime();
            for (int i = 1; i <= 10; i++) {
                heap.search(i);
            }
            endTime = System.nanoTime();
            System.out.println("Heap Search (10 búsquedas): " + (endTime - startTime) / 1000000.0 + " ms");
            
            startTime = System.nanoTime();
            for (int i = 1; i <= 10; i++) {
                list.search(i);
            }
            endTime = System.nanoTime();
            System.out.println("List Search (10 búsquedas): " + (endTime - startTime) / 1000000.0 + " ms");
            
            startTime = System.nanoTime();
            for (int i = 0; i < 10 && !heap.isEmpty(); i++) {
                heap.extractMin();
            }
            endTime = System.nanoTime();
            System.out.println("Heap ExtractMin (10 extracciones): " + (endTime - startTime) / 1000000.0 + " ms");
            
            startTime = System.nanoTime();
            for (int i = 0; i < 10 && !list.isEmpty(); i++) {
                list.extractMin();
            }
            endTime = System.nanoTime();
            System.out.println("List ExtractMin (10 extracciones): " + (endTime - startTime) / 1000000.0 + " ms");
        }
    }

    
    private static Patient[] generateRandomPatients(int count) {
        Patient[] patients = new Patient[count];
        for (int i = 0; i < count; i++) {
            int priority = (int)(Math.random() * 10) + 1;
            long arrivalTime = System.currentTimeMillis() + (long)(Math.random() * 60000);
            patients[i] = new Patient(i + 1, priority, arrivalTime, "Paciente" + (i + 1));
        }
        return patients;
    }
}