package transport.core;

public enum CarteType {
    JUNIOR("Carte Junior", 0.3),
    SENIOR("Carte Senior", 0.25),
    SOLIDARITE("Carte Solidarité", 0.5),
    PARTENAIRE("Carte Partenaire", 0.4);

    private final String libelle;
    private final double reduction;

    CarteType(String libelle, double reduction) {
        this.libelle = libelle;
        this.reduction = reduction;
    }

    public String getLibelle() { return libelle; }
    public double getReduction() { return reduction; }
}