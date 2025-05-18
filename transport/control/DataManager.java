package transport.control;

import transport.core.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String PERSONS_FILE = "persons.dat";
    private static final String FARE_MEDIA_FILE = "fare_media.dat";
    private static final String COMPLAINTS_FILE = "complaints.dat";

    // Save all Person objects (including Employees and Usagers)
    public static void savePersons(List<Person> persons) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PERSONS_FILE))) {
            oos.writeObject(persons);
        } catch (IOException e) {
            System.err.println("Error saving persons: " + e.getMessage());
        }
    }

    // Load all Person objects
    public static List<Person> loadPersons() {
        List<Person> persons = new ArrayList<>();
        File file = new File(PERSONS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PERSONS_FILE))) {
                persons = (List<Person>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading persons: " + e.getMessage());
            }
        }
        return persons;
    }

    // Save fare media (both Tickets and CartePersonnelle)
    public static void saveFareMedia(List<TitreTransport> fareMedia) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FARE_MEDIA_FILE))) {
            oos.writeObject(fareMedia);
        } catch (IOException e) {
            System.err.println("Error saving fare media: " + e.getMessage());
        }
    }

    // Load fare media
    public static List<TitreTransport> loadFareMedia() {
        List<TitreTransport> fareMedia = new ArrayList<>();
        File file = new File(FARE_MEDIA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FARE_MEDIA_FILE))) {
                fareMedia = (List<TitreTransport>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading fare media: " + e.getMessage());
            }
        }
        return fareMedia;
    }
    
    // For bonus: Complaint persistence
    public static void saveComplaints(List<Complaint> complaints) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COMPLAINTS_FILE))) {
            oos.writeObject(complaints);
        } catch (IOException e) {
            System.err.println("Error saving complaints: " + e.getMessage());
        }
    }
    
    public static List<Complaint> loadComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        File file = new File(COMPLAINTS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(COMPLAINTS_FILE))) {
                complaints = (List<Complaint>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading complaints: " + e.getMessage());
            }
        }
        return complaints;
    }
}