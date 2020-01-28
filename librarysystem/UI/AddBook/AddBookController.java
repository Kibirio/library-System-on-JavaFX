
package librarysystem.UI.AddBook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.database.DatabaseHandler;


public class AddBookController implements Initializable {

    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField publisher;
    @FXML
    private JFXButton cancelButton;

    
    DatabaseHandler databaseHandler;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXButton savaButton;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        checkData();
        
    }    

    @FXML
    private void addBook(ActionEvent event) 
    {
        String bookId = id.getText();
        String bookAuthor = author.getText();
        String bookName = title.getText();
        String bookPublisher = publisher.getText();
        
        if(bookId.isEmpty() || bookAuthor.isEmpty() || bookName.isEmpty() || bookPublisher.isEmpty())
        {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setHeaderText(null);
          alert.setContentText("Please enter all the fields");
          alert.showAndWait();
          
        }
        
        String qu = "INSERT INTO BOOK VALUES ("+
                "'" + bookId + "'," +
                "'" + bookName + "'," +
                "'" + bookAuthor + "'," +
                "'" + bookPublisher + "'," +
                "" + "true" + "" +
                ")";
        System.out.println(qu);
        if(databaseHandler.execAction(qu))
        {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setHeaderText(null);
          alert.setContentText("Success");
          alert.showAndWait();  
        }
        else
        {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setHeaderText(null);
          alert.setContentText("Failed");
          alert.showAndWait(); 
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void checkData()
    {
        String qu = "SELECT title FROM BOOK";
        ResultSet rs = databaseHandler.execQuery(qu);
        
        
            try {
                while(rs.next())
                {
                    String bookTitle = rs.getString("title");
                    System.out.println(bookTitle);
                }
            } 
            catch (SQLException ex) {
                Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
    }
}
