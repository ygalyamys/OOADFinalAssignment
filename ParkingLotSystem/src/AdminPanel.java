package ui;

import control.AdminController;
import entity.ParkingSession;
import service.FineScheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AdminPanel extends JPanel {

    private final AdminController controller;

    private JLabel occupancyLabel;
    private JLabel revenueLabel;

    private JTable vehicleTable;
    private JTable fineTable;

    private JComboBox<FineScheme> fineSchemeComboBox;

    public AdminPanel(AdminController controller) {
        this.controller = controller;
        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel dashboardPanel = new JPanel(new GridLayout(1, 2));

        occupancyLabel = new JLabel("Occupancy: 0%");
        revenueLabel = new JLabel("Total Revenue: RM 0.00");

        dashboardPanel.add(occupancyLabel);
        dashboardPanel.add(revenueLabel);

        add(dashboardPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        vehicleTable = new JTable();
        fineTable = new JTable();

        tabbedPane.add("Current Vehicles", new JScrollPane(vehicleTable));
        tabbedPane.add("Outstanding Fines", new JScrollPane(fineTable));

        add(tabbedPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        fineSchemeComboBox = new JComboBox<>();
        JButton applySchemeButton = new JButton("Apply Fine Scheme");
        JButton refreshButton = new JButton("Refresh");

        applySchemeButton.addActionListener(e -> applyFineScheme());
        refreshButton.addActionListener(e -> refreshData());

        bottomPanel.add(new JLabel("Fine Scheme:"));
        bottomPanel.add(fineSchemeComboBox);
        bottomPanel.add(applySchemeButton);
        bottomPanel.add(refreshButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {

        double occupancy = controller.getOccupancyRate();
        occupancyLabel.setText("Occupancy: " + String.format("%.2f", occupancy) + "%");

        double revenue = controller.getTotalRevenue();
        revenueLabel.setText("Total Revenue: RM " + String.format("%.2f", revenue));

        populateVehicleTable(controller.getCurrentVehicles());
        populateFineTable(controller.getOutstandingFines());
    }

    private void populateVehicleTable(List<ParkingSession> sessions) {

        String[] columns = {"Plate", "Entry Time", "Spot"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (ParkingSession session : sessions) {
            model.addRow(new Object[]{
                    session.getLicensePlate(),
                    String.valueOf(session.getEntryTime()),
                    session.getSpotId()
            });
        }

        vehicleTable.setModel(model);
    }

    private void populateFineTable(Map<String, Double> fines) {

        String[] columns = {"License Plate", "Outstanding Amount"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Map.Entry<String, Double> entry : fines.entrySet()) {
            model.addRow(new Object[]{
                    entry.getKey(),
                    "RM " + String.format("%.2f", entry.getValue())
            });
        }

        fineTable.setModel(model);
    }

    private void applyFineScheme() {
        FineScheme selectedScheme = (FineScheme) fineSchemeComboBox.getSelectedItem();
        controller.setFineScheme(selectedScheme);
        JOptionPane.showMessageDialog(this, "Fine scheme updated successfully.");
    }

    public void setFineSchemes(List<FineScheme> schemes) {
        DefaultComboBoxModel<FineScheme> model = new DefaultComboBoxModel<>();
        for (FineScheme scheme : schemes) {
            model.addElement(scheme);
        }
        fineSchemeComboBox.setModel(model);
    }
}
