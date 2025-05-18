package transport.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public class CartePersonnelle extends TitreTransport implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double PRIX_BASE = 5000.0;
    
    private final Person proprietaire;
    private final CarteType type;

    public CartePersonnelle(Person proprietaire) throws ReductionImpossibleException, TitreNonValideException {
        super(calculerPrix(proprietaire));
        this.proprietaire = proprietaire;
        this.type = determinerTypeCarte(proprietaire);
    }

    private static double calculerPrix(Person p) throws ReductionImpossibleException {
        CarteType type = determinerTypeCarte(p);
        return PRIX_BASE * (1 - type.getReduction());
    }

    public static CarteType determinerTypeCarte(Person p) throws ReductionImpossibleException {
        int age = Period.between(p.getDateNaissance(), LocalDate.now()).getYears();
        
        // Check for most advantageous discount first
        if (p.isHandicap()) {
            return CarteType.SOLIDARITE; // 50% reduction
        }
        if (p instanceof Employee) {
            return CarteType.PARTENAIRE; // 40% reduction
        }
        if (age < 25) {
            return CarteType.JUNIOR; // 30% reduction
        }
        if (age > 65) {
            return CarteType.SENIOR; // 25% reduction
        }
        
        throw new ReductionImpossibleException("Vous n'avez droit à aucune réduction.");
    }

    @Override
    public boolean estValide(LocalDate date) throws TitreNonValideException {
        if (date.isAfter(getDateAchat().plusYears(1))) {
            throw new TitreNonValideException("Carte personnelle numéro " + getId() + " invalide");
        }
        return true;
    }

    public String getInfoCarte() {
        return String.format(
            "Carte personnelle créée le %s\n" +
            "Numéro de la carte: %d\n" +
            "Proprietaire de la carte : %s %s\n" +
            "Type de la carte: %s\n" +
            "Prix de la carte : %.1f DA",
            getDateAchat(),
            getId(),
            proprietaire.getPrenom(),
            proprietaire.getNom(),
            type,
            getPrix()
        );
    }

    // Getters
    public Person getProprietaire() { return proprietaire; }
    public CarteType getType() { return type; }

    public enum CarteType {
        JUNIOR(0.3),
        SENIOR(0.25),
        SOLIDARITE(0.5),
        PARTENAIRE(0.4);

        private final double reduction;

        CarteType(double reduction) {
            this.reduction = reduction;
        }

        public double getReduction() {
            return reduction;
        }
    }
}