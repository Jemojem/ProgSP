package Laba6.Part2;

import javax.swing.*;
import java.io.*;
import java.util.Scanner;

public class CardRegistration extends JFrame {

    private JTextField cardHolderField;
    private JTextArea notesArea;
    private JComboBox<String> cardTypeComboBox;
    private JCheckBox primaryCardCheckBox;
    private JButton saveButton, loadButton;

    private static final String FILE_NAME = "card_data.txt";

    public CardRegistration() {
        setTitle("Card Registration");
        setSize(400, 300);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setupComponents();

        loadCardDataFromFile();

        addCloseConfirmation();

        setVisible(true);
    }

    private void setupComponents() {
        setLayout(new java.awt.GridLayout(6, 2, 5, 5));

        JLabel nameLabel = new JLabel("Card Holder:");
        cardHolderField = new JTextField();
        add(nameLabel);
        add(cardHolderField);

        JLabel cardTypeLabel = new JLabel("Card Type:");
        cardTypeComboBox = new JComboBox<>(new String[]{"Credit", "Debit", "Prepaid"});
        add(cardTypeLabel);
        add(cardTypeComboBox);

        primaryCardCheckBox = new JCheckBox("Primary Card");
        add(new JLabel());
        add(primaryCardCheckBox);

        JLabel notesLabel = new JLabel("Notes:");
        notesArea = new JTextArea(4, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        add(notesLabel);
        add(notesScrollPane);

        saveButton = new JButton("Save");
        loadButton = new JButton("Load");

        saveButton.addActionListener(e -> saveCardDataToFile());

        loadButton.addActionListener(e -> loadCardDataFromFile());

        add(saveButton);
        add(loadButton);
    }

    private void addCloseConfirmation() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(
                        CardRegistration.this,
                        "Do you want to save changes before exiting?",
                        "Exit Confirmation",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );
                if (confirmed == JOptionPane.YES_OPTION) {
                    saveCardDataToFile();
                    dispose();
                } else if (confirmed == JOptionPane.NO_OPTION) {
                    dispose();
                }
            }
        });
    }

    private void saveCardDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            writer.println(cardHolderField.getText());
            writer.println(cardTypeComboBox.getSelectedItem().toString());
            writer.println(primaryCardCheckBox.isSelected());
            writer.println(notesArea.getText());
            JOptionPane.showMessageDialog(this, "Data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving data.");
        }
    }

    private void loadCardDataFromFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                if (scanner.hasNextLine()) cardHolderField.setText(scanner.nextLine());
                if (scanner.hasNextLine()) cardTypeComboBox.setSelectedItem(scanner.nextLine());
                if (scanner.hasNextBoolean()) primaryCardCheckBox.setSelected(scanner.nextBoolean());
                scanner.nextLine();
                StringBuilder notes = new StringBuilder();
                while (scanner.hasNextLine()) {
                    notes.append(scanner.nextLine()).append("\n");
                }
                notesArea.setText(notes.toString().trim());
                JOptionPane.showMessageDialog(this, "Data loaded successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading data.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CardRegistration::new);
    }
}
