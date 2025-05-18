package transport.core;

public enum TypeFunction {
    AGENT("Agent de station"),
    CONDUCTEUR("Conducteur");

    private final String libelle;

    TypeFunction(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}