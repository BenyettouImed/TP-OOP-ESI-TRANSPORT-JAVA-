package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;  // Required version ID
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private boolean handicap;

    public Person(String nom, String prenom, LocalDate dateNaissance, boolean handicap) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.handicap = handicap;
    }

    // Getters
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public boolean isHandicap() { return handicap; }

    public int getAge() {
        return LocalDate.now().getYear() - dateNaissance.getYear();
    }
    @Override
public String toString() {
    return prenom + " " + nom; // or just nom if you want
}

}