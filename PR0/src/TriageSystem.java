public interface TriageSystem {
    void insert(Patient patient);
    Patient extractMin();
    Patient search(int patientId);
    boolean isEmpty();
    int size();
}