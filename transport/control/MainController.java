package transport.control;

import transport.core.*;
import java.time.LocalDate;
import java.util.*;

public class MainController {
    private final List<TitreTransport> titres;
    private final List<Person> personnes;
    private final List<Complaint> complaints;

    public MainController() {
        // Initialize with persisted data
        this.titres = DataManager.loadFareMedia();
        this.personnes = DataManager.loadPersons();
        this.complaints = DataManager.loadComplaints();
    }

    // Existing methods remain unchanged until ajouterComplaint
    public TitreTransport acheterTicket() {
        Ticket ticket = new Ticket();
        titres.add(0, ticket); // Add at beginning to maintain reverse chronological order
        DataManager.saveFareMedia(titres); // Persist immediately
        return ticket;
    }

    public TitreTransport acheterCartePersonnelle(Person p) throws ReductionImpossibleException, TitreNonValideException {
        CartePersonnelle carte = new CartePersonnelle(p);
        titres.add(0, carte); // Add at beginning
        DataManager.saveFareMedia(titres);
        return carte;
    }

    public void ajouterPersonne(Person p) {
        personnes.add(p);
        DataManager.savePersons(personnes);
    }

    public List<TitreTransport> getTitresTransport() {
        // Already sorted by date due to insertion order
        return Collections.unmodifiableList(titres);
    }

    public boolean verifierValidite(TitreTransport titre, LocalDate date) {
        try {
            return titre.estValide(date);
        } catch (TitreNonValideException e) {
            return false;
        }
    }

    // Updated complaint methods
    public Complaint ajouterComplaint(Complaint.ComplaintType type, String description, Person plaignant, String target) {
    Complaint complaint = new Complaint(type, description, plaignant, target);
    complaints.add(complaint);
    DataManager.saveComplaints(complaints);
    
    if (shouldSuspendTarget(target)) {
        System.out.println("ALERT: " + getTargetType(target) + " " + target + " suspended due to excessive complaints");
    }
    return complaint; // Return the created complaint
}

    public List<Complaint> getComplaints() {
        // Return sorted by type (TECHNIQUE first) then by complaint number
        return complaints.stream()
               .sorted(Comparator.comparing(Complaint::getType)
                                 .thenComparingInt(Complaint::getComplaintNumber))
               .toList();
    }

    public boolean shouldSuspendStation() {
        // Count complaints targeting stations (non-numeric targets)
        return complaints.stream()
               .filter(c -> !c.getTarget().matches(".*\\d+.*"))
               .count() > 3;
    }

    public boolean shouldSuspendTransport(String transportId) {
        // Count complaints targeting specific transport
        return complaints.stream()
               .filter(c -> c.getTarget().equalsIgnoreCase(transportId))
               .count() > 3;
    }

    // New helper methods for complaint management
    public boolean shouldSuspendTarget(String target) {
        return complaints.stream()
               .filter(c -> c.getTarget().equalsIgnoreCase(target))
               .count() > 3;
    }

    public String getTargetType(String target) {
        return target.matches(".*\\d+.*") ? "Transport" : "Station";
    }

    public List<Complaint> getComplaintsByPerson(Person person) {
        return complaints.stream()
               .filter(c -> c.getPlaignant().equals(person))
               .sorted(Comparator.comparing(Complaint::getType)
                                 .thenComparingInt(Complaint::getComplaintNumber))
               .toList();
    }

    public List<Complaint> getComplaintsByTarget(String target) {
        return complaints.stream()
               .filter(c -> c.getTarget().equalsIgnoreCase(target))
               .sorted(Comparator.comparing(Complaint::getType)
                                 .thenComparingInt(Complaint::getComplaintNumber))
               .toList();
    }

    public void resolveComplaint(int complaintNumber) {
        complaints.stream()
            .filter(c -> c.getComplaintNumber() == complaintNumber)
            .findFirst()
            .ifPresent(c -> {
                c.setResolved(true);
                DataManager.saveComplaints(complaints);
            });
    }

    // Existing methods remain unchanged
    public List<Person> getPersonnes() {
        return Collections.unmodifiableList(personnes);
    }

    public List<Employee> getEmployees() {
        return personnes.stream()
                       .filter(p -> p instanceof Employee)
                       .map(p -> (Employee)p)
                       .toList();
    }

    public List<Usager> getUsagers() {
        return personnes.stream()
                      .filter(p -> p instanceof Usager)
                      .map(p -> (Usager)p)
                      .toList();
    }
}