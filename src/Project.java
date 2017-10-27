import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Julian on 27/10/2017.
 */
public class Project {

    //Project object properties
    int id;
    String title;
    String description;
    String hyperlink;
    String hyperlink_description;
    String image_filepath;


    public Project(int id, String title, String description, String hyperlink, String hyperlink_description, String image_filepath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.hyperlink = hyperlink;
        this.hyperlink_description = hyperlink_description;
        this.image_filepath = image_filepath;
    }

    public static ArrayList<Project> getAllProjects() {
        // New arraylist to store results of querying all Projects
        ArrayList<Project> all_projects = new ArrayList<>();

        LocalSQLiteDB localSQLiteDB = LocalSQLiteDB.getDatabaseObject();
        try (Connection c = localSQLiteDB.connection()) {
            try (PreparedStatement stmt = c.prepareStatement("SELECT p.id, p.title, p.description, p.hyperlink, p.hyperlink_description, p.image_filepath FROM project AS p;")) {

                //Store the query results in a ResultSet object
                try (ResultSet r = stmt.executeQuery()) {
                    while (r.next()) {
                        int project_id = r.getInt("id");
                        String project_title = r.getString("title");
                        String project_description = r.getString("description");
                        String project_hyperlink = r.getString("hyperlink");
                        String project_hyperlink_description = r.getString("hyperlink_description");
                        String project_image_filepath = r.getString("image_filepath");
                        all_projects.add(new Project(project_id, project_title, project_description, project_hyperlink, project_hyperlink_description, project_image_filepath));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //Check if projects have been successfully retrieved, return full arraylist of projects, otherwise return null
        if (all_projects.size() > 0) {
            return all_projects;
        } else {
            return null;
        }
    }

}
