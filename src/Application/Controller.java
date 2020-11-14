package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    public ListView viewTitle;
    @FXML
    public ListView viewAuthor;
    @FXML
    public ListView ViewYear;
    @FXML
    public ListView viewId;
    @FXML
    public TextField idDelete;
    @FXML
    public TextField addId;
    @FXML
    public TextField addTitle;
    @FXML
    public TextField addAuthor;
    @FXML
    public TextField addYear;
    @FXML
    private Button closeButton;

    public void updateView() throws ClassNotFoundException, SQLException {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();
        String sqlexec = "SELECT * FROM film_library.film";
        Statement statement = connection.createStatement();
        ResultSet data = statement.executeQuery(sqlexec);
        viewTitle.getItems().clear();
        viewAuthor.getItems().clear();
        ViewYear.getItems().clear();
        viewId.getItems().clear();
        while (data.next()) {
            viewTitle.getItems().add(viewTitle.getItems().size(), data.getString("title"));
            viewAuthor.getItems().add(viewAuthor.getItems().size(), data.getString("author"));
            ViewYear.getItems().add(ViewYear.getItems().size(), data.getDate("date"));
            viewId.getItems().add(viewId.getItems().size(), data.getInt("id"));
        }
        connection.close();
    }

    @FXML
    public void initialize() throws ClassNotFoundException, SQLException {
        updateView();
    }

    public void closeWindow() throws ClassNotFoundException, SQLException, IOException {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void deleteRow() throws ClassNotFoundException, SQLException {
        if (idDelete.getText().matches("[-+]?\\d+")) {
            int id = Integer.parseInt(idDelete.getText());
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            String query = "DELETE FROM film_library.film WHERE id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.execute();
            connection.close();
            updateView();
        }
    }


    public void addRow() throws ClassNotFoundException, SQLException {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();
        String sqlexec = "SELECT id FROM film_library.film";
        Statement statement = connection.createStatement();
        ResultSet data = statement.executeQuery(sqlexec);
        ArrayList<Integer> idExists = new ArrayList<Integer>();
        while (data.next()) {
            idExists.add(data.getInt("id"));
        }
        connection.close();

        if (addId.getText().isEmpty() || !addId.getText().matches("[-+]?\\d+") || addAuthor.getText().isEmpty() || addTitle.getText().isEmpty() || addYear.getText().isEmpty()) {
            System.out.println("Can't add this row.");
        }
        else {
            if (idExists.contains(Integer.parseInt(addId.getText()))) {
                System.out.println("Id exists");
            }
            else {
                ConnectionClass connectionClass_2 = new ConnectionClass();
                Connection connection_2 = connectionClass_2.getConnection();
                String query = "INSERT INTO film_library.film VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStmt = connection_2.prepareStatement(query);
                preparedStmt.setInt(1, Integer.parseInt(addId.getText()));
                preparedStmt.setNString(2, addAuthor.getText());
                preparedStmt.setNString(3, addTitle.getText());
                preparedStmt.setNString(4, addYear.getText());
                preparedStmt.execute();
                connection_2.close();
                updateView();
            }
        }
        addId.clear();
        addAuthor.clear();
        addTitle.clear();
        addYear.clear();
    }
}
