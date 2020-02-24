package librarysystem.UI.settings;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.database.DatabaseHandler;

public class SettingsDriver extends Application {

   @Override
   public void start(Stage stage) throws Exception {
      Parent root = FXMLLoader.load(getClass().getResource("/librarysystem/UI/settings/Settings.fxml"));

      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
      stage.setTitle("Settings");

      new Thread(() -> {
         DatabaseHandler.getInstance();
      }).start();
   }

   public static void main(String[] args) {
      launch(args);
   }

}
