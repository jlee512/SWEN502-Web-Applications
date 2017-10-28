import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Julian on 28/10/2017.
 */
public class Contact {
    //This object acts to perform data-access from the server to the database for all Messages on the website

    int id;
    String subject;
    String contact_details;
    String message_body;

    public Contact(int id, String subject, String contact_details, String message_body) {
        this.id = id;
        this.subject = subject;
        this.contact_details = contact_details;
        this.message_body = message_body;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getContact_details() {
        return contact_details;
    }

    public String getMessage_body() {
        return message_body;
    }

    public static ArrayList<Contact> getAllMessages() {
        // New arraylist to store results of querying all Projects
        ArrayList<Contact> all_contact = new ArrayList<>();

        LocalSQLiteDB localSQLiteDB = LocalSQLiteDB.getDatabaseObject();
        try (Connection c = localSQLiteDB.connection()) {
            try (PreparedStatement stmt = c.prepareStatement("SELECT * FROM contact;")) {

                //Store the query results in a ResultSet object
                try (ResultSet r = stmt.executeQuery()) {
                    while (r.next()) {
                        int contact_id = r.getInt("id");
                        String subject = r.getString("subject");
                        String contact_details = r.getString("contact_details");
                        String message_body = r.getString("message_body");
                        all_contact.add(new Contact(contact_id, subject, contact_details, message_body));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return all_contact;
    }

    //Method for submitting a new message to the database
    public static void addMessageToDB(String subject, String contact_details, String message_body) {

    }

    //Protoype interface to review all messages posted to the system through the contact form
    public static void printAllMessagesToDate() {
        ArrayList<Contact> messages = Contact.getAllMessages();

        System.out.println("Reviewing messages to date: ");
        System.out.println("****************************");

        for (int i = 0; i < messages.size(); i++) {
            Contact message = messages.get(i);
            System.out.println("Message: " + message.getId() + " from: " + message.getContact_details());
            System.out.println("Subject: " + message.getSubject());
            System.out.println(message.getMessage_body());
            System.out.println("------------------------------------------------------------");
        }

    }

    public static void main(String[] args) {
        printAllMessagesToDate();
    }
}
