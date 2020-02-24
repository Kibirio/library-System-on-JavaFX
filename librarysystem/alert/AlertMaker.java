package librarysystem.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertMaker {

   public static void showSimpleAlert(String title, String content) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle(title);
      alert.setHeaderText(null);
      alert.setContentText(content);
      alert.showAndWait();
   }

   public static void showErrorMessage(String title, String content) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(title);
      alert.setContentText(content);
      alert.showAndWait();
   }

   public static void showErrorMessage(Exception ex) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error Occurred");
      alert.setHeaderText("Error Occurred");
      alert.setContentText(ex.getLocalizedMessage());
      alert.showAndWait();
   }
}
