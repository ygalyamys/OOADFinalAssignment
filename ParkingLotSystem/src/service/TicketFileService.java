package service;

import entity.Ticket;
import enums.SpotCategory;
import java.io.*;
import java.time.LocalDateTime;

public class TicketFileService {

    private static final String FILE_NAME = "tickets.txt";

    // Save a ticket to file (append mode)
    public static void saveTicket(Ticket ticket) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {

            String line =
                    ticket.getTicketID() + "," +
                    ticket.getPlateNumber() + "," +
                    ticket.getSpotID() + "," +
                    ticket.getSpotCategory() + "," +
                    ticket.getEntryTime();

            fw.write(line + "\n");

        } catch (IOException e) {
            System.out.println("Error saving ticket to file");
            e.printStackTrace();
        }
    }

    // Find a ticket by its ID
    public static Ticket findTicketByID(String searchID) {

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length < 5) continue;

                String ticketID = parts[0];

                if (ticketID.equals(searchID)) {

                    String plate = parts[1];
                    String spotID = parts[2];
                    SpotCategory category = SpotCategory.valueOf(parts[3]);
                    LocalDateTime entryTime = LocalDateTime.parse(parts[4]);

                    return new Ticket(ticketID, plate, spotID, category, entryTime);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Ticket file not found.");
        } catch (IOException e) {
            System.out.println("Error reading ticket file.");
            e.printStackTrace();
        }

        return null;
    }
}
