package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public class Employee extends Person implements Serializable {
    private static final long serialVersionUID = 1L;  // Required for serialization
    private String matricule;
    private TypeFunction fonction;

    public Employee(String nom, String prenom, LocalDate dateNaissance, boolean handicap, 
                   String matricule, TypeFunction fonction) {
        super(nom, prenom, dateNaissance, handicap);
        this.matricule = matricule;
        this.fonction = fonction;
    }

    // Getters
    public String getMatricule() { return matricule; }
    public TypeFunction getFonction() { return fonction; }
}