public class Auto {

    // Attribute der Klasse Auto
    private final String marke;                                                                                         // Marke des Autos
    private final int kmstand;                                                                                          // Kilometerstand des Autos
    private final String antriebsart;                                                                                   // Antriebsart des Autos (z. B. Benzin, Diesel, Elektro)
    private final double preis;                                                                                         // Preis des Autos

    // Konstruktor der Klasse Auto
    public Auto(String marke, int kmstand, String antriebsart, double preis) {

        // Initialisierung der Attribute mit den übergebenen Werten
        this.marke = marke;
        this.kmstand = kmstand;
        this.antriebsart = antriebsart;
        this.preis = preis;
    }

    // Getter Methode für Marke
    public String getMarke() {
        return marke;                                                                                                   // Rückgabe der Marke
    }

    // Getter Methode für Kilometerstand
    public int getKmstand() {
        return kmstand;                                                                                                 // Rückgabe des Kilometerstands
    }

    // Getter Methode für Antriebsart
    public String getAntriebsart() {
        return antriebsart;                                                                                             // Rückgabe der Antriebsart
    }

    // Getter Methode für Preis
    public double getPreis() {
        return preis;                                                                                                   // Rückgabe des Preises
    }
}
