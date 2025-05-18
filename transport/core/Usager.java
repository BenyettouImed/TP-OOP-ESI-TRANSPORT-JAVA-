package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public class Usager extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    private final int id;

    public Usager(String nom, String prenom, LocalDate dateNaissance, boolean handicap) {
        super(nom, prenom, dateNaissance, handicap);
        this.id = nextId++;
    }

    public int getId() { return id; }

    // Custom serialization to handle static field
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        out.writeInt(nextId);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        nextId = in.readInt();
    }
}