public class Auto {
    private final String marke;
    private final int kmstand;
    private final String antriebsart;
    private final double preis;

    public Auto(String marke, int kmstand, String antriebsart, double preis) {
        this.marke = marke;
        this.kmstand = kmstand;
        this.antriebsart = antriebsart;
        this.preis = preis;
    }

    public String getMarke() {
        return marke;
    }

    public int getKmstand() {
        return kmstand;
    }

    public String getAntriebsart() {
        return antriebsart;
    }

    public double getPreis() {
        return preis;
    }

    @Override
    public String toString() {
        return String.format("%s (%.0f km, %s, %.2f €)", marke, (double) kmstand, antriebsart, preis);
    }
}
