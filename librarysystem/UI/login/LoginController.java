package librarysystem.UI.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
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
import librarysystem.UI.settings.Preferences;
import org.apache.commons.codec.digest.DigestUtils;

public class LoginController implements Initializable {

   @FXML
   private JFXTextField username;
   @FXML
   private JFXPasswordField password;

   Preferences preference;

   @Override
   public void initialize(URL url, ResourceBundle rb) {
      preference = Preferences.getPreferences();
   }

   private void closeStage() {
      ((Stage) username.getScene().getWindow()).close();
   }

   void loadMainWindow() {
      try {
         Parent parent = FXMLLoader.load(getClass().getResource("/librarysystem/UI/launcher/Main.fxml"));
         Stage stage = new Stage(StageStyle.DECORATED);
         stage.setTitle("Palmoncode Library");
         stage.setScene(new Scene(parent));
         stage.show();
      } catch (IOException ex) {
         Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @FXML
   private void handleLoginButtonAction(ActionEvent event) {
//      titleLabel.setText("Library Login");
//      titleLabel.setStyle("-fx-background-color:black; -fx-text-fill:white");

      String uname = username.getText();
      String pwd = DigestUtils.sha1Hex(password.getText());

      if (uname.equals(preference.getUsername()) && pwd.equals(preference.getPassword())) {
         closeStage();
         loadMainWindow();
      } else {
//         titleLabel.setText("Invalid Credentials");
//         titleLabel.setStyle("-fx-background-color:#d32f2f; -fx-text-fill:white");
         username.getStyleClass().add("wrong-credentials");
         password.getStyleClass().add("wrong-credentials");
      }
   }

   @FXML
   private void handleCancelButtonAction(ActionEvent event) {
      System.exit(0);
   }

}
