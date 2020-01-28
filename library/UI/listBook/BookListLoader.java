/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.UI.listBook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Haryx_PurpleSoft
 */
public  class BookListLoader  extends Application {
    @Override
    public void start(Stage stage) throws Exception
    {
       Parent root =  FXMLLoader.load(getClass().getResource("book_list.fxml"));
       
       Scene scene = new Scene(root);
       
       stage.setScene(scene);
       stage.show();
    }
    
    public static void main(String[] args )
    {
        launch(args);
    }
}
