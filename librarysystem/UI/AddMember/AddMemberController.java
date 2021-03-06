package librarysystem.UI.AddMember;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.UI.listBook.BookListController;
import library.database.DatabaseHandler;
import librarysystem.UI.listmember.MemberListController;
import librarysystem.alert.AlertMaker;

public class AddMemberController implements Initializable {

   DatabaseHandler handler;

   @FXML
   private JFXTextField name;
   @FXML
   private JFXTextField id;
   @FXML
   private JFXTextField mobile;
   @FXML
   private JFXTextField email;
   @FXML
   private JFXButton savaButton;
   @FXML
   private JFXButton cancelButton;
   @FXML
   private AnchorPane rootPane;

   private Boolean isInEditMode = Boolean.FALSE;

   @Override
   public void initialize(URL url, ResourceBundle rb) {

      handler = DatabaseHandler.getInstance();
   }

   @FXML
   private void addMember(ActionEvent event) {
      String mName = name.getText();
      String mId = id.getText();
      String mMobile = mobile.getText();
      String mEmail = email.getText();

      Boolean flag = mName.isEmpty() || mId.isEmpty() || mMobile.isEmpty() || mEmail.isEmpty();

      if (flag) {
         Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setHeaderText(null);
         alert.setContentText("please enter all the fields");
         alert.showAndWait();
      }

      String st = "INSERT INTO MEMBER VALUES ("
              + " ' " + mId + " ', "
              + " ' " + mName + " ', "
              + " ' " + mMobile + " ', "
              + " ' " + mEmail + " ' "
              + ")";
      System.out.println(st);

      if (isInEditMode) {
         handleEditOperation();
         return;
      }

      if (handler.execAction(st)) {
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setHeaderText(null);
         alert.setContentText("successfully saved");
         alert.showAndWait();
      } else {
         Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setHeaderText(null);
         alert.setContentText("Error occured");
         alert.showAndWait();
      }
   }

   @FXML
   private void cancel(ActionEvent event) {
      Stage stage = (Stage) rootPane.getScene().getWindow();
      stage.close();

   }

   public void inflateUI(MemberListController.Member member) {
      name.setText(member.getName());
      id.setText(member.getId());
      mobile.setText(member.getMobile());
      email.setText(member.getEmail());
      id.setEditable(false);
      isInEditMode = true;
   }

   private void handleEditOperation() {
      MemberListController.Member member = new MemberListController.Member(name.getText(), id.getText(), mobile.getText(), email.getText());
      if (handler.updateMember(member)) {
         AlertMaker.showSimpleAlert("Succes", "book updated");
      } else {
         AlertMaker.showErrorMessage("Failed", "Could not update the book");
      }
   }
}
