public class Patient {
    private int id;
    private int priority;
    private long arrivalTime;
    private String name;
    
    public Patient(int id, int priority, long arrivalTime, String name) {
        this.id = id;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public long getArrivalTime() {
        return arrivalTime;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return "Patient{id=" + id + ", priority=" + priority + ", name='" + name + "', arrivalTime=" + arrivalTime + "}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Patient patient = (Patient) obj;
        return id == patient.id;
    }
}