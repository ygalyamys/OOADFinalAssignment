import entity.ParkingLot;
import javax.swing.*;
import service.SpotAllocator;
import ui.EntryPanel;

public class TestEntryPanelUI {
    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot("MAIN", 3, 2, 5);

        SpotAllocator allocator = new SpotAllocator(lot);

        EntryPanel entryPanel = new EntryPanel(allocator);

        JFrame frame = new JFrame("Parking Lot - Entry Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        frame.add(entryPanel);

        frame.setVisible(true);
    }
}
