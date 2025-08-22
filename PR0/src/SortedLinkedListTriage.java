public class SortedLinkedListTriage implements TriageSystem {
    private Node head;
    private int size;
    
    private class Node {
        Patient patient;
        Node next;
        
        Node(Patient patient) {
            this.patient = patient;
            this.next = null;
        }
    }
    
    public SortedLinkedListTriage() {
        this.head = null;
        this.size = 0;
    }
    
    @Override
    public void insert(Patient patient) {
        Node newNode = new Node(patient);
        
        if (head == null || comparePatients(patient, head.patient) < 0) {
            newNode.next = head;
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null && comparePatients(patient, current.next.patient) >= 0) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }
    
    @Override
    public Patient extractMin() {
        if (isEmpty()) {
            return null;
        }
        
        Patient min = head.patient;
        head = head.next;
        size--;
        return min;
    }
    
    @Override
    public Patient search(int patientId) {
        Node current = head;
        while (current != null) {
            if (current.patient.getId() == patientId) {
                return current.patient;
            }
            current = current.next;
        }
        return null;
    }
    
    @Override
    public boolean isEmpty() {
        return head == null;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    private int comparePatients(Patient a, Patient b) {
        if (a.getPriority() != b.getPriority()) {
            return Integer.compare(a.getPriority(), b.getPriority());
        }
        return Long.compare(a.getArrivalTime(), b.getArrivalTime());
    }
}