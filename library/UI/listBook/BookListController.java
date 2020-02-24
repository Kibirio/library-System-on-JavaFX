/*
 *  this class is used to display the books retrieved from the database
 */
package library.UI.listBook;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
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
import library.database.DatabaseHandler;
import librarysystem.UI.AddBook.AddBookController;
import librarysystem.UI.launcher.MainController;
import librarysystem.alert.AlertMaker;

public class BookListController implements Initializable {

   ObservableList<Book> list = FXCollections.observableArrayList();

   @FXML
   private AnchorPane rootPane;
   @FXML
   private TableView<Book> tableView;
   @FXML
   private TableColumn<Book, String> titleCol;
   @FXML
   private TableColumn<Book, String> idCol;
   @FXML
   private TableColumn<Book, String> authorCol;
   @FXML
   private TableColumn<Book, String> publisherCol;
   @FXML
   private TableColumn<Book, Boolean> availabilityCol;

   @Override
   public void initialize(URL url, ResourceBundle rb) {
      initCol();
      loadData();
   }

   private void initCol() {
      titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
      idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
      authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
      publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
      availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
   }

   private void loadData() {
      list.clear();
      DatabaseHandler handler = DatabaseHandler.getInstance();
      String qu = "SELECT * FROM BOOK";
      ResultSet rs = handler.execQuery(qu);

      try {
         while (rs.next()) {
            String titlex = rs.getString("title");
            String id = rs.getString("id");
            String author = rs.getString("author");
            String publisher = rs.getString("publisher");
            Boolean available = rs.getBoolean("isAvail");

            list.add(new Book(titlex, id, author, publisher, available));
         }
      } catch (SQLException ex) {
         Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);
      }

      tableView.setItems(list);
   }

   @FXML
   private void handleBookDeleteAction(ActionEvent event) {
      // fetch the selected row
      Book selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
      if (selectedForDeletion == null) {
         AlertMaker.showErrorMessage("No Book Selected", "Please select a book for issue");
         return;
      }
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Deleting Book");
      alert.setContentText("Are you sure you want to delete the book" + selectedForDeletion.getTitle());
      Optional<ButtonType> answer = alert.showAndWait();
      if (answer.get() == ButtonType.OK) {
         Boolean result = DatabaseHandler.getInstance().deleteBook(selectedForDeletion);
         if (result) {
            AlertMaker.showSimpleAlert("Book Deleted", selectedForDeletion.getTitle() + "was deleted successfully");
            list.remove(selectedForDeletion);
         } else {
            AlertMaker.showErrorMessage("Failed", selectedForDeletion.getTitle() + "could not be deleted");
         }
      } else {
         AlertMaker.showSimpleAlert("Deletion Cancelled", "Deletion process was cancelled");
      }
   }

   @FXML
   private void handleBookEditAction(ActionEvent event) {
      Book selectedForEdit = tableView.getSelectionModel().getSelectedItem();
      if (selectedForEdit == null) {
         AlertMaker.showErrorMessage("No Book Selected", "Please select a book to edit");
         return;
      }

      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/librarysystem/UI/AddBook/AddBook.fxml"));
         Parent parent = loader.load();

         AddBookController controller = (AddBookController) loader.getController();
         controller.inflateUI(selectedForEdit); // call method in addBookController to show the data selectedd

         Stage stage = new Stage(StageStyle.DECORATED);
         stage.setTitle("Edit Book");
         stage.setScene(new Scene(parent));
         stage.show();

         // update the book list when you close the window
         stage.setOnCloseRequest((e) -> {
            handleBookRefreshAction(new ActionEvent());
         }
         );
      } catch (IOException ex) {
         Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   // method to refresh the books after they are updated
   @FXML
   private void handleBookRefreshAction(ActionEvent event) {
      loadData();
   }

   public static class Book {

      private final SimpleStringProperty title;
      private final SimpleStringProperty id;
      private final SimpleStringProperty author;
      private final SimpleStringProperty publisher;
      private final SimpleBooleanProperty availability;

      public Book(String title, String id, String author, String publisher, Boolean availability) {
         this.title = new SimpleStringProperty(title);
         this.id = new SimpleStringProperty(id);
         this.author = new SimpleStringProperty(author);
         this.publisher = new SimpleStringProperty(publisher);
         this.availability = new SimpleBooleanProperty(availability);
      }

      public String getTitle() {
         return title.get();
      }

      public String getId() {
         return id.get();
      }

      public String getAuthor() {
         return author.get();
      }

      public String getPublisher() {
         return publisher.get();
      }

      public boolean getAvailability() {
         return availability.get();
      }

   }
}
