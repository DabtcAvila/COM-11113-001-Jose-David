public class MinHeapTriage implements TriageSystem {
    private Patient[] heap;
    private int size;
    private int capacity;
    
    public MinHeapTriage() {
        this.capacity = 1000;
        this.heap = new Patient[capacity];
        this.size = 0;
    }
    
    @Override
    public void insert(Patient patient) {
        if (size >= capacity) {
            throw new RuntimeException("Heap está lleno");
        }
        
        heap[size] = patient;
        heapifyUp(size);
        size++;
    }
    
    @Override
    public Patient extractMin() {
        if (isEmpty()) {
            return null;
        }
        
        Patient min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        
        if (size > 0) {
            heapifyDown(0);
        }
        
        return min;
    }
    
    @Override
    public Patient search(int patientId) {
        for (int i = 0; i < size; i++) {
            if (heap[i].getId() == patientId) {
                return heap[i];
            }
        }
        return null;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    private void heapifyUp(int index) {
        if (index == 0) return;
        
        int parentIndex = (index - 1) / 2;
        
        if (heap[index].getPriority() < heap[parentIndex].getPriority() ||
            (heap[index].getPriority() == heap[parentIndex].getPriority() && 
             heap[index].getArrivalTime() < heap[parentIndex].getArrivalTime())) {
            
            swap(index, parentIndex);
            heapifyUp(parentIndex);
        }
    }
    
    private void heapifyDown(int index) {
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;
        int smallest = index;
        
        if (leftChild < size && comparePatients(heap[leftChild], heap[smallest]) < 0) {
            smallest = leftChild;
        }
        
        if (rightChild < size && comparePatients(heap[rightChild], heap[smallest]) < 0) {
            smallest = rightChild;
        }
        
        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }
    
    private int comparePatients(Patient a, Patient b) {
        if (a.getPriority() != b.getPriority()) {
            return Integer.compare(a.getPriority(), b.getPriority());
        }
        return Long.compare(a.getArrivalTime(), b.getArrivalTime());
    }
    
    private void swap(int i, int j) {
        Patient temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}