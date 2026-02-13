package ui;

import entity.*;
import service.SpotAllocator;

import javax.swing.*;
import java.awt.*;

public class EntryPanel extends JPanel {

    private JTextField txtPlate;
    private JComboBox<String> cmbType;
    private JTextArea txtResult;
    private SpotAllocator allocator;

    public EntryPanel(SpotAllocator allocator) {
        this.allocator = allocator;

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Plate Number:"));
        txtPlate = new JTextField();
        inputPanel.add(txtPlate);

        inputPanel.add(new JLabel("Vehicle Type:"));
        cmbType = new JComboBox<>(new String[]{
                "motorcycle", "car", "suv", "handicapped"
        });
        inputPanel.add(cmbType);

        JButton btnFind = new JButton("Find Spot");
        inputPanel.add(btnFind);

        add(inputPanel, BorderLayout.NORTH);

        txtResult = new JTextArea();
        txtResult.setEditable(false);
        add(new JScrollPane(txtResult), BorderLayout.CENTER);

        // Button Action
        btnFind.addActionListener(e -> handleFindSpot());
    }

    private void handleFindSpot() {
        String plate = txtPlate.getText();
        String type = (String) cmbType.getSelectedItem();

        if (plate.isEmpty()) {
            txtResult.setText("Please enter plate number.");
            return;
        }

        Vehicle vehicle;

        switch (type) {
            case "motorcycle":
                vehicle = new Motorcycle(plate);
                break;
            case "car":
                vehicle = new Car(plate);
                break;
            case "suv":
                vehicle = new SUV(plate);
                break;
            case "handicapped":
                vehicle = new HandicappedVehicle(plate);
                break;
            default:
                txtResult.setText("Invalid vehicle type.");
                return;
        }

        var spot = allocator.allocateSpot(vehicle);

        if (spot != null) {
            txtResult.setText("Allocated Spot ID: " + spot.getId());
        } else {
            txtResult.setText("No available spot.");
        }
    }
}
