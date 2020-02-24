package librarysystem.UI.listmember;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.UI.listBook.BookListController;
import library.database.DatabaseHandler;
import librarysystem.UI.AddMember.AddMemberController;
import librarysystem.alert.AlertMaker;

public class MemberListController implements Initializable {

   ObservableList<Member> list = FXCollections.observableArrayList();

   @FXML
   private AnchorPane rootPane;
   @FXML
   private TableView<Member> tableView;
   @FXML
   private TableColumn<Member, String> nameCol;
   @FXML
   private TableColumn<Member, String> idCol;
   @FXML
   private TableColumn<Member, String> mobileCol;
   @FXML
   private TableColumn<Member, String> emailCol;

   @Override
   public void initialize(URL url, ResourceBundle rb) {
      initCol();
      loadData();

   }

   private void initCol() {
      nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
      idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
      mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
      emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
   }

   private void loadData() {
      list.clear();
      DatabaseHandler handler = DatabaseHandler.getInstance();
      String qu = "SELECT * FROM MEMBER";
      ResultSet rs = handler.execQuery(qu);

      try {
         while (rs.next()) {
            String name = rs.getString("name");
            String id = rs.getString("id");
            String mobile = rs.getString("mobile");
            String email = rs.getString("email");

            list.add(new Member(name, id, mobile, email));
         }
      } catch (SQLException ex) {
         Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);
      }

      tableView.setItems(list);
   }

   @FXML
   private void handleMemberDeleteAction(ActionEvent event) {
      Member selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
      if (selectedForDeletion == null) {
         AlertMaker.showErrorMessage("No Member selected", "please select member to delete");
         return;
      }
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Deleting Member");
      alert.setContentText("Are you sure you want to delete" + selectedForDeletion.getName());
      Optional<ButtonType> answer = alert.showAndWait();
      if (answer.get() == ButtonType.OK) {
         Boolean result = DatabaseHandler.getInstance().deleteMember(selectedForDeletion);
         if (result) {
            AlertMaker.showSimpleAlert("Member Deleted", selectedForDeletion.getName() + "was deleted successfully");
         } else {
            AlertMaker.showErrorMessage("failed", selectedForDeletion.getName() + "was not deleted");
         }
      } else {
         AlertMaker.showErrorMessage("Cancelled", "The operation was cancelled");
      }
   }

   @FXML
   private void handleMemberRefreshAction(ActionEvent event) {
      loadData();
   }

   @FXML
   private void handleMemberEditAction(ActionEvent event) {
      try {
         Member selectedForEdit = tableView.getSelectionModel().getSelectedItem();
         if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("No member selected", "please select member to edit");
         }

         FXMLLoader loader = new FXMLLoader(getClass().getResource("/librarysystem/UI/AddMember/AddMember.fxml"));
         Parent parent = loader.load();
         AddMemberController controller = (AddMemberController) loader.getController();
         controller.inflateUI(selectedForEdit);

         Stage stage = new Stage(StageStyle.DECORATED);
         stage.setTitle("Edit Member");
         stage.setScene(new Scene(parent));
         stage.show();

         stage.setOnCloseRequest((e) -> {
            handleMemberRefreshAction(new ActionEvent());
         });
      } catch (IOException ex) {
         Logger.getLogger(MemberListController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public static class Member {

      private final SimpleStringProperty name;
      private final SimpleStringProperty id;
      private final SimpleStringProperty mobile;
      private final SimpleStringProperty email;

      public Member(String name, String id, String mobile, String email) {
         this.name = new SimpleStringProperty(name);
         this.id = new SimpleStringProperty(id);
         this.mobile = new SimpleStringProperty(mobile);
         this.email = new SimpleStringProperty(email);
      }

      public String getName() {
         return name.get();
      }

      public String getId() {
         return id.get();
      }

      public String getMobile() {
         return mobile.get();
      }

      public String getEmail() {
         return email.get();
      }

   }

}
