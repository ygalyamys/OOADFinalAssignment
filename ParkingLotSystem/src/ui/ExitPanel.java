package ui;

import control.ExitController;
import entity.Receipt;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * ExitPanel (Boundary / UI class):
 * - Collects user inputs (plate, payment method, amount).
 * - Lets user PREVIEW the bill first (realistic UX).
 * - Then processes payment through ExitController.
 *
 * Member 3 focus:
 * 1) Billing math presentation (hours/rate/total)
 * 2) Payment interface (Cash/Card) + validation
 * 3) Scenario 2 (Exit/Pay) UI demo
 */

public class ExitPanel extends JPanel {

    private final ExitController exitController;

    // ===== UI Components =====
    private final JTextField plateField = new JTextField(12);
    private final JComboBox<String> methodBox = new JComboBox<>(new String[]{"CASH", "CARD"});
    private final JTextField amountField = new JTextField(8);

    private final JButton previewBtn = new JButton("View Bill");
    private final JButton payBtn = new JButton("Pay & Exit");

    private final JTextArea output = new JTextArea(16, 46);

    // ===== Simple UI state =====
    // We only allow "Pay" after a successful preview, to prevent guessing the amount.
    private String lastPreviewPlate = null;

    public ExitPanel(ExitController exitController) {
        this.exitController = exitController;

        setLayout(new BorderLayout(10, 10));

        // --- 1) Form area (inputs) ---
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("License Plate:"));
        form.add(plateField);

        form.add(new JLabel("Payment Method:"));
        form.add(methodBox);

        form.add(new JLabel("Amount Paid (RM):"));
        form.add(amountField);

        // --- 2) Button area ---
        // Pay is disabled until user previews the bill (good UX + easier to explain).
        payBtn.setEnabled(false);

        previewBtn.addActionListener(e -> onPreviewBill());
        payBtn.addActionListener(e -> onPay());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.add(previewBtn);
        buttons.add(payBtn);

        // --- 3) Output area (receipt / bill preview) ---
        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        // --- 4) Small usability: if user changes plate after preview, disable Pay ---
        plateField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { invalidatePreviewIfPlateChanged(); }
            @Override public void removeUpdate(DocumentEvent e) { invalidatePreviewIfPlateChanged(); }
            @Override public void changedUpdate(DocumentEvent e) { invalidatePreviewIfPlateChanged(); }
        });

        add(form, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);
        add(new JScrollPane(output), BorderLayout.SOUTH);
    }

    /*
    STEP 1: Show the bill BEFORE paying.
    
    - UI (Boundary) delegates to ExitController (Control)
    - ExitController coordinates BillingService + FineService + domain entities
    - UI then displays the formatted breakdown to the user
    */
    private void onPreviewBill() {
        try {
            String plate = plateField.getText().trim().toUpperCase();
            if (plate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Plate number is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Controller returns a Receipt-like object, but with method=PREVIEW and totalPaid=0.
            Receipt preview = exitController.previewBill(plate);

            // Label as BILL PREVIEW so the assessor sees the intent clearly.
            String previewText = preview.format().replace("==== EXIT RECEIPT ====", "==== BILL PREVIEW ====");

            output.setText(previewText
                    + "\n\nNext: Enter an amount and click 'Pay & Exit'."
                    + "\n(If amount is insufficient, outstanding will be recorded.)\n");

            // Enable Pay because we now have shown the price.
            lastPreviewPlate = plate;
            payBtn.setEnabled(true);

        } catch (Exception ex) {
            payBtn.setEnabled(false);
            lastPreviewPlate = null;
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
    STEP 2: Execute payment + exit.
    Performs input validation (so controller receives clean data).
    */
    private void onPay() {
        try {
            // ===== 1) Read + validate inputs =====
            String plate = plateField.getText().trim().toUpperCase();
            if (plate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Plate number is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Require preview before pay
            if (lastPreviewPlate == null || !plate.equals(lastPreviewPlate)) {
                JOptionPane.showMessageDialog(this, "Please click 'View Bill' first for this plate.", "Validation", JOptionPane.WARNING_MESSAGE);
                payBtn.setEnabled(false);
                return;
            }

            String method = (String) methodBox.getSelectedItem();

            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the payment amount.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Amount must be a number (example: 10.00).", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ===== 2) Call controller (business logic is inside services) =====
            Receipt receipt = exitController.exitLot(plate, method, amount);

            // ===== 3) Display receipt (plus outstanding info if any) =====
            StringBuilder sb = new StringBuilder();
            sb.append(receipt.format());

            double outstanding = exitController.getOutstandingFines(plate);
            if (outstanding > 0) {
                sb.append("\nNOTE: Outstanding unpaid balance stored in ledger: RM ")
                        .append(String.format("%.2f", outstanding))
                        .append("\n");
            }

            output.setText(sb.toString());

            // After payment, require new preview for any further action.
            payBtn.setEnabled(false);
            lastPreviewPlate = null;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
    If user edits the plate field after previewing,
    we disable Pay so they cannot accidentally pay for the wrong vehicle.
    */
    private void invalidatePreviewIfPlateChanged() {
        String currentPlate = plateField.getText().trim().toUpperCase();
        if (lastPreviewPlate != null && !currentPlate.equals(lastPreviewPlate)) {
            payBtn.setEnabled(false);
        }
    }
}
