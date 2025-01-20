import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DurchschnittspreisTest {
    private AutoManager autoManager;

    @BeforeEach
    void setUp() {
        autoManager = new AutoManager();
    }

    @Test
    void testBerechneDurchschnittspreis() {
        // Beispiel-Daten hinzufügen
        autoManager.addAuto(new Auto("Mercedes", 4507, "Benzin", 135030.50));
        autoManager.addAuto(new Auto("BMW", 60456, "Diesel", 26100.30));
        autoManager.addAuto(new Auto("Tesla", 3598, "Elektro", 100879.45));
        autoManager.addAuto(new Auto("Toyota", 56560, "Diesel", 12560.30));
        autoManager.addAuto(new Auto("Audi", 22378, "Benzin", 30400.20));
        autoManager.addAuto(new Auto("Tesla", 4789, "Elektro", 67700.20));
        autoManager.addAuto(new Auto("Volkswagen", 98457, "Benzin", 8007.55));
        autoManager.addAuto(new Auto("Toyota", 110876, "Diesel", 9700.50));
        autoManager.addAuto(new Auto("Volkswagen", 20987, "Elektro", 21034.60));
        autoManager.addAuto(new Auto("Tesla", 32034, "Elektro", 45098.60));
        autoManager.addAuto(new Auto("Mercedes", 210000, "Diesel", 569.54));
        autoManager.addAuto(new Auto("Audi", 20670, "Diesel", 45569.50));
        autoManager.addAuto(new Auto("Mercedes", 23400, "Elektro", 100569.40));

        // Erwartete Durchschnittswerte
        double expectedDurchschnittspreis =
                (135030.50 + 26100.30 + 100879.45 + 12560.30 + 30400.20 + 67700.20 + 8007.55 + 9700.50 + 21034.60 + 45098.60 + 569.54
                        + 45569.50 + 100569.40) / 13;

        // Tatsächlicher Wert berechnet durch den AutoManager
        double actualDurchschnittspreis = autoManager.berechneDurchschnittspreis();

        // Überprüfung des erwarteten Ergebnisses
        assertEquals(expectedDurchschnittspreis, actualDurchschnittspreis, 0.01,
                "Der Durchschnittspreis wurde nicht korrekt berechnet.");
    }
}
