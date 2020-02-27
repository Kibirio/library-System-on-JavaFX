package librarysystem.alert;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

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

   public static void showMaterialDialog(StackPane root, Node nodeToBeBlurred, List<JFXButton> controls, String header, String body) {
      BoxBlur blur = new BoxBlur(3, 3, 3);

      JFXDialogLayout dialogLayout = new JFXDialogLayout();
      JFXButton button = new JFXButton("Okay. I'll check");
      JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.TOP);

      controls.forEach(controlButtons -> {
         button.getStyleClass().add("dialog-button");
         button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
            dialog.close();
         });
      });

      dialogLayout.setHeading(new Text("No such book exists in issue records"));
      dialogLayout.setActions(controls);
      dialog.show();
      dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
         nodeToBeBlurred.setEffect(null);
      });
      nodeToBeBlurred.setEffect(blur);
   }

}
