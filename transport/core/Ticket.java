// Ticket.java
package transport.core;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ticket extends TitreTransport {
    private static final double PRICE = 50.0;

    public Ticket() {
        super(PRICE);
    }

    @Override
    public boolean estValide(LocalDate date) throws TitreNonValideException {
        if (!date.isEqual(getDateAchat())) {
            throw new TitreNonValideException("Ticket numéro " + getId() + " expiré - valable uniquement le : " + getDateAchat());
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Ticket créé le : %s\nNuméro du ticket : %d\nPrix du ticket : %.1f DA",
                getDateAchat(), getId(), getPrix());
    }
}