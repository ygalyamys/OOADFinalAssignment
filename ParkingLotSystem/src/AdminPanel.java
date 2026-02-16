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

    private JLabel totalSpotsLabel;
    private JLabel occupiedSpotsLabel;
    private JLabel availableSpotsLabel;
    private JLabel occupancyRateLabel;
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

        JPanel dashboard = new JPanel(new GridLayout(2, 3));
        dashboard.setBorder(BorderFactory.createTitledBorder("Admin Dashboard"));

        totalSpotsLabel = new JLabel();
        occupiedSpotsLabel = new JLabel();
        availableSpotsLabel = new JLabel();
        occupancyRateLabel = new JLabel();
        revenueLabel = new JLabel();

        dashboard.add(totalSpotsLabel);
        dashboard.add(occupiedSpotsLabel);
        dashboard.add(availableSpotsLabel);
        dashboard.add(occupancyRateLabel);
        dashboard.add(revenueLabel);

        add(dashboard, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        vehicleTable = new JTable();
        fineTable = new JTable();

        tabs.add("Vehicle Report", new JScrollPane(vehicleTable));
        tabs.add("Fine Report", new JScrollPane(fineTable));

        add(tabs, BorderLayout.CENTER);

        JPanel bottom = new JPanel();

        fineSchemeComboBox = new JComboBox<>();
        JButton applyBtn = new JButton("Apply Scheme");
        JButton refreshBtn = new JButton("Refresh");

        applyBtn.addActionListener(e -> controller.setFineScheme(
                (FineScheme) fineSchemeComboBox.getSelectedItem()));

        refreshBtn.addActionListener(e -> refreshData());

        bottom.add(fineSchemeComboBox);
        bottom.add(applyBtn);
        bottom.add(refreshBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    private void refreshData() {
        totalSpotsLabel.setText("Total: " + controller.getTotalSpots());
        occupiedSpotsLabel.setText("Occupied: " + controller.getOccupiedSpots());
        availableSpotsLabel.setText("Available: " + controller.getAvailableSpots());
        occupancyRateLabel.setText("Occupancy: " + String.format("%.2f", controller.getOccupancyRate()) + "%");
        revenueLabel.setText("Revenue: RM " + String.format("%.2f", controller.getTotalRevenue()));

        populateVehicleTable(controller.getCurrentVehicles());
        populateFineTable(controller.getOutstandingFines());
    }

    private void populateVehicleTable(List<ParkingSession> sessions) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Plate","Entry","Spot"},0);
        for (ParkingSession s : sessions)
            model.addRow(new Object[]{s.getLicensePlate(), s.getEntryTime(), s.getSpotId()});
        vehicleTable.setModel(model);
    }

    private void populateFineTable(Map<String, Double> fines) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Plate","Fine"},0);
        for (String plate : fines.keySet())
            model.addRow(new Object[]{plate, fines.get(plate)});
        fineTable.setModel(model);
    }
}
