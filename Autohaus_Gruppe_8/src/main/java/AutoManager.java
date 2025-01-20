import java.util.ArrayList;
import java.util.List;

public class AutoManager {
    // Liste zur Speicherung von Auto Objekten
    private final List<Auto> autos;

    public AutoManager() {
        autos = new ArrayList<>();                                                                                      // Initialisierung der Liste mit einer ArrayList
    }

    // Methode zum Hinzufügen von Autos zur Liste
    public void addAuto(Auto auto) {
        autos.add(auto);                                                                                                // Auto Objekt zur Liste hinzufügen
    }

    // Methode zum Entfernen von Autos aus der Liste
    public void removeAuto(int index) {
        if (index >= 0 && index < autos.size()) {                                                                       // Überprüfen ob Index gültig ist
            autos.remove(index);                                                                                        // Auto an angegebener Position entfernen
        }
    }

    // Methode zum Abrufen aller Autos
    public List<Auto> getAutos() {
        return autos;                                                                                                   // Liste aller Autos zurückgeben
    }
    // hierfür Erstellung eines JUnit Test da KmFilter-Methode "private void" und somit kein Rückgabewert
    public double berechneDurchschnittspreis() {
        if (autos.isEmpty()) {
            return 0;  // Falls keine Autos vorhanden sind, wird 0 zurückgegeben
        }

        // Preise summieren
        double summe = 0;
        for (Auto auto : autos) {
            summe += auto.getPreis();
        }

        // Durchschnitt berechnen
        return summe / autos.size();
    }
}
