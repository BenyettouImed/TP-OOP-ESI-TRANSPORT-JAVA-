package transport.core;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum ComplaintType {
        TECHNIQUE, PAIEMENT, SERVICE
    }

    // Existing fields
    private final ComplaintType type;
    private final String description;
    private final LocalDateTime date;
    private final Person plaignant;
    
    // New fields
    private final int complaintNumber;
    private final String target; // Can be either station name or transport ID
    private boolean resolved;
    private static int nextComplaintNumber = 1;

    public Complaint(ComplaintType type, String description, Person plaignant, String target) {
        this.type = type;
        this.description = description;
        this.plaignant = plaignant;
        this.date = LocalDateTime.now();
        this.target = target;
        this.complaintNumber = nextComplaintNumber++;
        this.resolved = false;
    }

    // Getters for existing fields
    public ComplaintType getType() { return type; }
    public String getDescription() { return description; }
    public LocalDateTime getDate() { return date; }
    public Person getPlaignant() { return plaignant; }
    
    // Getters for new fields
    public int getComplaintNumber() { return complaintNumber; }
    public String getTarget() { return target; }
    public boolean isResolved() { return resolved; }
    
    // Setter for resolved status
    public void setResolved(boolean resolved) { 
        this.resolved = resolved; 
    }

    // Updated toString() to include all relevant information
    @Override
    public String toString() {
        return String.format("Réclamation #%d%nDate : %s | Type : %s | Cible : %s | %s : %s | Soumise par : %s %s",
                complaintNumber,
                date.toLocalDate(),
                type,
                target,
                (target.matches("^[A-Za-z]+\\d+$") ? "Moyen de transport" : "Station"),
                description,
                plaignant.getNom(),
                plaignant.getPrenom());
    }
    
    // Helper method to reset the complaint counter (for testing purposes)
    public static void resetComplaintCounter() {
        nextComplaintNumber = 1;
    }
}