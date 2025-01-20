import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableCellRenderer;

public class AutomanagementGUI extends JFrame {
    // GUI-Elemente Definition
    private JPanel Autos;                                                                                               // Hauptpanel der GUI
    private JTextField textField1;                                                                                      // Eingabefeld für Kilometerstand
    private JTextField textField2;                                                                                      // Eingabefeld für Preis
    private JTable table1;                                                                                              // Tabelle zur Anzeige der Autos
    private JComboBox<String> comboBox1;                                                                                // ComboBox für Marke
    private JComboBox<String> comboBox2;                                                                                // ComboBox für Antriebsart
    private JButton speichernButton;                                                                                    // Button zum Speichern eines neuen Autos
    private JButton deleteButton;                                                                                       // Button zum Löschen eines Autos
    private JComboBox<String> comboBox3;                                                                                // ComboBox für Filteroptionen
    private JButton durchschnittspreisAllerAutosButton;                                                                 // Button zur Durchschnittspreis-Berechnung


    // Klassenvariablen für Logik
    private final AutoManager autoManager;                                                                              // AutoManager
    private final DefaultTableModel tableModel;                                                                         // Datenmodell für die Tabelle
    private final TableRowSorter<DefaultTableModel> sorter;                                                             // Sortiere für die Tabelle

    public AutomanagementGUI() {
        // Initialisierung des AutoManagers
        autoManager = new AutoManager();

        tableModel = new DefaultTableModel(new String[]{"Marke", "Kilometerstand", "Antriebsart", "Preis"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;                                                                                           // Alle Zellen in der Tabelle sind nicht mehr bearbeitbar
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {                                                           // Datentypen in der Tabelle für Kilometerstand und Preis damit TableRowSorter richtig funktioniert
                switch (columnIndex) {
                    case 1: // Kilometerstand
                        return Integer.class;
                    case 3: // Preis
                        return Double.class;
                    default:
                        return String.class;
                }
            }
        };

        table1.setModel(tableModel);                                                                                    // Modell der Tabelle zuweisen
        EinheitenRenderer();                                                                                            // Renderer setzen aus Zeile 150

        // Sortierer für die Tabelle initialisieren
        sorter = new TableRowSorter<>(tableModel);
        table1.setRowSorter(sorter);

        // Einfügen der Beispiel-Daten
        initObjekte();

        // GUI-Fenster Layout
        setTitle("Automanagement");                                                                                     // Titel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                                                                 // Schließen beim Beenden
        setSize(800, 550);                                                                                  // Fenstergröße
        setContentPane(Autos);                                                                                          // Hauptpanel
        setVisible(true);                                                                                               // Fenster sichtbar machen
        setLocationRelativeTo(null);                                                                                    // Fenster in die mitte des Bildschirms

        // Event-Listener für Buttons und Dropdowns hinzufügen
        speichernButton.addActionListener(_ -> speichernAuto());                                                        // ActionListener für den "Speichern"-Button

        deleteButton.addActionListener(_ -> loeschenAuto());                                                            // ActionListener für den "Delete"-Button

        comboBox3.addActionListener(_ -> filterTabelle());                                                              // Filter aktualisieren, wenn die Auswahl geändert wird

        durchschnittspreisAllerAutosButton.addActionListener(_ -> berechneDurchschnittspreis());                        // ActionListener für den "Durchschnittspreis"-Button
    }

    private void initObjekte() {
        // Beispiel-Daten einfügen
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

        // Tabelle aktuualisieren
        updateTable();
    }

    private void speichernAuto() {
        try {
            // Eingabewerte abrufen
            String marke = Objects.requireNonNull(comboBox1.getSelectedItem()).toString();                              // Marke abrufen
            String kmStandText = textField1.getText();                                                                  // Kilometerstand abrufen
            String antriebsart = Objects.requireNonNull(comboBox2.getSelectedItem()).toString();                        // Antriebsart abrufen
            String preisText = textField2.getText();                                                                    // Preis abrufen

            // Überprüfe, ob der Kilometerstand und der Preis gültig sind
            boolean kmStandPruefung = pruefeKmStand(kmStandText);                                                       // Kilometerstand prüfen
            boolean preisPruefung = pruefePreis(preisText);                                                             // Preis prüfen

            // Wenn Kilometerstand und Preis beide ungültig, zeige diese Fehlermeldung an
            if (!kmStandPruefung && !preisPruefung) {
                JOptionPane.showMessageDialog(this, "Fehler: Kilometerstand und Preis müssen gültige Zahlen sein!");
                return;
            }

            // Wenn nur Kilometerstand ungültig, zeige diese Fehlermeldung an
            if (!kmStandPruefung) {
                JOptionPane.showMessageDialog(this, "Fehler: Der Kilometerstand muss eine positive ganze Zahl sein!\nBeispiel: 30000");
                return;
            }
            // Wenn nur Preis ungültig, zeige diese Fehlermeldung an
            if (!preisPruefung) {
                JOptionPane.showMessageDialog(this, "Fehler: Der Preis muss eine positive Zahl mit höchstens zwei Dezimalstellen sein!\nBeispiel: 12000.50");
                return;
            }

            // Kilometerstand/Preis -Eingabe in entsprechende Datentypen umwandeln
            int kmStand = Integer.parseInt(kmStandText);
            double preis = Double.parseDouble(preisText);                                                               //Preis muss als double in Liste gespeichert werden um später damit rechnen zu können.

            // Neues Auto-Objekt erstellen und speichern
            Auto neuesAuto = new Auto(marke, kmStand, antriebsart, preis);
            autoManager.addAuto(neuesAuto);

            // Tabelle aktualisieren
            updateTable();
            JOptionPane.showMessageDialog(this, "Auto erfolgreich hinzugefügt!");

            // Eingabefelder leeren
            textField1.setText("");
            textField2.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fehler: Kilometerstand und Preis müssen gültige Zahlen sein!");
        }
    }



    // Renderer für formatierung in der Tabelle
    private void EinheitenRenderer() {

        table1.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof Double) {
                    setText(String.format("%.2f" + " EUR", value));                                                      // Formatiert den Wert als String mit 2 nachkommastellen und EUR-Einheit
                } else {
                    setText(value == null ? "" : value.toString());
                }
            }
        });
        table1.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof Integer) {
                    setText(value + " km");                                                                             // Formatiert den Wert mit km-Einheit
                } else {
                    setText(value == null ? "" : value.toString());
                }
            }
        });
    }



    private boolean pruefePreis(String preis) {
        // Überprüfen ob Preis mit maximal 2 Dezimalstellen angegeben wurde
        Pattern patternPreis = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
        Matcher matcherPreis = patternPreis.matcher(preis);

        // Überprüfen ob positiv mit try catch
        if (matcherPreis.matches()) {
            try {
                double preisWert = Double.parseDouble(preis);
                return preisWert > 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private boolean pruefeKmStand(String kmStand) {
        // Überprüfen ob Kilometerstand-Eingabe nur ganze Zahlen
        Pattern patternKmStand = Pattern.compile("^\\d+$");
        Matcher matcherKmStand = patternKmStand.matcher(kmStand);

        // Überprüfen ob positiv mit try catch
        if (matcherKmStand.matches()) {
            try {
                int kmWert = Integer.parseInt(kmStand);
                return kmWert > 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private void loeschenAuto() {
        // Auto auswählen und löschen
        int selectedRow = table1.getSelectedRow();                                                                      // Ausgewählte Zeile abrufen
        if (selectedRow >= 0) {
            autoManager.removeAuto(selectedRow);                                                                        // Auto entfernen (Methode in Automanager)
            updateTable();                                                                                              // Tabelle aktualisieren
            JOptionPane.showMessageDialog(this, "Auto erfolgreich gelöscht!");                   // Ausgabe von Erfolg im dialogfenster
        } else {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Auto in der Tabelle aus, das gelöscht werden soll."); // Ausgabe von Misserfolg im dialogfenster da (selectedRow >= 0) = false
        }
    }
    // Filter Funktion in ComboBox3
    private void filterTabelle() {
        // Tabelle aktualisiren basierend auf Filteroption
        String filterOption = Objects.requireNonNull(comboBox3.getSelectedItem()).toString();
        switch (filterOption) {
            case "Nicht Filtern":                                                                                       // "Nicht Filtern" als Option 0 damit nicht direkt gefiltert wird
                sorter.setRowFilter(null);
                break;
            case "0-10.000":                                                                                            // Filteroption 1
                sorter.setRowFilter(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 10001, 1));
                break;
            case "10.001-25.000":                                                                                       // Filteroption 2
                sorter.setRowFilter(RowFilter.andFilter(List.of(
                        RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 10000, 1),
                        RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 25001, 1)
                )));
                break;
            case "25.001-50.000":                                                                                       // Filteroption 3
                sorter.setRowFilter(RowFilter.andFilter(List.of(
                        RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 25000, 1),
                        RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 50001, 1)
                )));
                break;
            case "50.001-80.000":                                                                                       // Filteroption 4
                sorter.setRowFilter(RowFilter.andFilter(List.of(
                        RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 50000, 1),
                        RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 80001, 1)
                )));
                break;
            case "80.001-120.000":                                                                                      // Filteroption 5
                sorter.setRowFilter(RowFilter.andFilter(List.of(
                        RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 80000, 1),
                        RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 120001, 1)
                )));
                break;
            case ">120.000":                                                                                            // Filteroption 6
                sorter.setRowFilter(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 120000, 1));
                break;
        }
    }

    // Durchschnittspreis-Berechnung in die Klasse AutoManager für erleichterte Testung mit JUnit
    private void berechneDurchschnittspreis() {
        // Aufruf der berechneten Methode aus AutoManager
        double durchschnittspreis = autoManager.berechneDurchschnittspreis();
        if (durchschnittspreis == 0) {
            JOptionPane.showMessageDialog(this, "Es gibt keine Autos, um den Durchschnittspreis zu berechnen!");
        } else {
            JOptionPane.showMessageDialog(this, "Der Durchschnittspreis aller Autos beträgt: " + String.format("%.2f", durchschnittspreis) + " EUR");
        }
    }

    private void updateTable() {
        // Tabelle leeren
        tableModel.setRowCount(0);

        // Autos aus dem AutoManager zur Tabelle hinzufügen
        for (Auto auto : autoManager.getAutos()) {
            tableModel.addRow(new Object[]{auto.getMarke(), auto.getKmstand(), auto.getAntriebsart(), auto.getPreis()});
        }
    }

    // Hauptprogramm starten
    public static void main(String[] args) {
        new AutomanagementGUI();
    }
}
