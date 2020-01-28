/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarysystem.UI.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Haryx_PurpleSoft
 */
public class MainController implements Initializable {


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void loadAddMember(ActionEvent event) {
        loadWindow("/liibrarysystem/UI/AddMember/AddMember.fxml", "Add New Member");
    }

    @FXML
    private void loadAddBook(ActionEvent event) {
        loadWindow("/liibrarysystem/UI/AddBook/AddBook.fxml", "Add New Book");
    }

    @FXML
    private void loadViewMember(ActionEvent event) {
        loadWindow("/liibrarysystem/UI/listmember/member_list.fxml", "View Members");
    }

    @FXML  
    private void loadViewBook(ActionEvent event) {
        loadWindow("/library/UI/listBook/book_list.fxml", "View Books");
    }
    
    void loadWindow(String loc, String title)
    {
       
        try 
        {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage =  new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } 
        catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
       
        
    }
    
}
