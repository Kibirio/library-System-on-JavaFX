/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarysystem.UI.launcher;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.database.DatabaseHandler;

public class MainLoader extends Application {

   @Override
   public void start(Stage stage) throws Exception {
      Parent root = FXMLLoader.load(getClass().getResource("/librarysystem/UI/login/Login.fxml"));

      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
      stage.setTitle("Library Management");

      new Thread(() -> {
         DatabaseHandler.getInstance();
      }).start();
   }

   public static void main(String[] args) {
      launch(args);
   }

}
