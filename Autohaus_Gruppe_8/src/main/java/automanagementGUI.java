import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class automanagementGUI extends JFrame {
    private JPanel Autos;
    private JTextField textField1;  // Kilometerstand
    private JTextField textField2;  // Preis
    private JTable table1;
    private JComboBox<String> comboBox1; // Marke
    private JComboBox<String> comboBox2; // Antriebsart
    private JComboBox<String> comboBox3; // Filter
    private JButton speichernButton;
    private JButton deleteButton;
    private JButton durchschnittspreisAllerAutosButton;

    private final AutoManager autoManager;
    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> sorter;

    public automanagementGUI() {
        // Initialisierung des AutoManagers
        autoManager = new AutoManager();
        tableModel = new DefaultTableModel(new String[]{"Marke", "Kilometerstand", "Antriebsart", "Preis"}, 0);
        table1.setModel(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table1.setRowSorter(sorter);

        // Beispiel-Daten hinzufügen
        initObjekte();

        // GUI-Einstellungen
        setTitle("Automanagement");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setContentPane(Autos);
        setVisible(true);

        // ActionListener für den "Speichern"-Button
        speichernButton.addActionListener(e -> speichernAuto());

        // ActionListener für den "Delete"-Button
        deleteButton.addActionListener(e -> löschenAuto());

        // Filter aktualisieren, wenn die Auswahl geändert wird
        comboBox3.addActionListener(e -> filterTabelle());

        // ActionListener für den "Durchschnittspreis"-Button
        durchschnittspreisAllerAutosButton.addActionListener(e -> berechneDurchschnittspreis());
    }

    private void initObjekte() {
        // Beispiel-Daten
        autoManager.addAuto(new Auto("Mercedes", 12980, "Benzin", 45030.50));
        autoManager.addAuto(new Auto("BMW", 60500, "Diesel", 26100.30));
        autoManager.addAuto(new Auto("Tesla", 34000, "Elektro", 45780.90));

        // Daten in die Tabelle einfügen
        updateTable();
    }

    private void speichernAuto() {
        try {
            // Eingabewerte abrufen
            String marke = comboBox1.getSelectedItem().toString();
            String kmStandText = textField1.getText();
            String antriebsart = comboBox2.getSelectedItem().toString();
            String preisText = textField2.getText();

            // Überprüfe, ob der Kilometerstand und der Preis gültig sind
            boolean kmStandValid = isValidKmStand(kmStandText);
            boolean preisValid = isValidPrice(preisText);

            if (!kmStandValid && !preisValid) {
                JOptionPane.showMessageDialog(this, "Fehler: Kilometerstand und Preis müssen gültige Zahlen sein!");
                return;
            }

            // Wenn nur der Kilometerstand oder der Preis ungültig ist, zeige entsprechende Fehlermeldung
            if (!kmStandValid) {
                JOptionPane.showMessageDialog(this, "Fehler: Der Kilometerstand muss eine gültige ganze Zahl sein!");
                return;
            }

            if (!preisValid) {
                JOptionPane.showMessageDialog(this, "Fehler: Der Preis muss eine gültige Zahl mit höchstens zwei Dezimalstellen sein!");
                return;
            }

            // Kilometerstand und Preis in entsprechende Datentypen umwandeln
            int kmStand = Integer.parseInt(kmStandText);
            double preis = Double.parseDouble(preisText);

            // Neues Auto-Objekt erstellen und speichern
            Auto neuesAuto = new Auto(marke, kmStand, antriebsart, preis);
            autoManager.addAuto(neuesAuto);

            // Tabelle aktualisieren
            updateTable();
            JOptionPane.showMessageDialog(this, "Auto erfolgreich hinzugefügt!");

            // Eingabefelder leeren
            textField1.setText("");
            textField2.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Fehler: Kilometerstand und Preis müssen gültige Zahlen sein!");
        }
    }

    private boolean isValidPrice(String price) {
        // Überprüfen, ob der Preis mit maximal 2 Dezimalstellen angegeben ist
        Pattern pattern = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
        Matcher matcher = pattern.matcher(price);
        return matcher.matches();
    }

    private boolean isValidKmStand(String kmStand) {
        // Überprüfen, ob der Kilometerstand nur ganze Zahlen enthält
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(kmStand);
        return matcher.matches();
    }

    private void löschenAuto() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow >= 0) {
            // Auto aus der Liste und Tabelle entfernen
            autoManager.removeAuto(selectedRow);
            updateTable();
            JOptionPane.showMessageDialog(this, "Auto erfolgreich gelöscht!");
        } else {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Auto aus, das gelöscht werden soll.");
        }
    }

    private void filterTabelle() {
        String filterOption = comboBox3.getSelectedItem().toString();
        switch (filterOption) {
            case "Nicht Filtern":
                sorter.setRowFilter(null);
                break;
            case "0-10.000":
                sorter.setRowFilter(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 10001, 1));
                break;
            case "10.001-25.000":
                sorter.setRowFilter(RowFilter.andFilter(List.of(
                        RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 10000, 1),
                        RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 25001, 1)
                )));
                break;
            case "25.001-50.000":
                sorter.setRowFilter(RowFilter.andFilter(List.of(
                        RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 25000, 1),
                        RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 50001, 1)
                )));
                break;
            case "50.001-80.000":
                sorter.setRowFilter(RowFilter.andFilter(List.of(
                        RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 50000, 1),
                        RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 80001, 1)
                )));
                break;
            case "80.001-120.000":
                sorter.setRowFilter(RowFilter.andFilter(List.of(
                        RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 80000, 1),
                        RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 120001, 1)
                )));
                break;
            case ">120.000":
                sorter.setRowFilter(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 120000, 1));
                break;
        }
    }

    private void updateTable() {
        // Tabelle leeren
        tableModel.setRowCount(0);

        // Alle Autos aus dem AutoManager zur Tabelle hinzufügen
        for (Auto auto : autoManager.getAutos()) {
            tableModel.addRow(new Object[]{auto.getMarke(), auto.getKmstand(), auto.getAntriebsart(), auto.getPreis()});
        }
    }

    private void berechneDurchschnittspreis() {
        List<Auto> autos = autoManager.getAutos();

        if (autos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Es gibt keine Autos, um den Durchschnittspreis zu berechnen!");
            return;
        }

        double summe = 0;
        for (Auto auto : autos) {
            summe += auto.getPreis();
        }

        double durchschnittspreis = summe / autos.size();
        JOptionPane.showMessageDialog(this, "Der Durchschnittspreis aller Autos beträgt: " + String.format("%.2f", durchschnittspreis) + " EUR");
    }

    public static void main(String[] args) {
        new automanagementGUI();
    }
}
