package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class TitreTransport implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    private final int id;
    private final LocalDate dateAchat;
    private final double prix;

    public TitreTransport(double prix) {
        this.id = nextId++;
        this.dateAchat = LocalDate.now();
        this.prix = prix;
    }

    // Getters
    public int getId() { return id; }
    public LocalDate getDateAchat() { return dateAchat; }
    public double getPrix() { return prix; }

    public abstract boolean estValide(LocalDate date) throws TitreNonValideException;

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