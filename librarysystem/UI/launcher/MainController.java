package librarysystem.UI.launcher;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.database.DatabaseHandler;
import librarysystem.alert.AlertMaker;

public class MainController implements Initializable {

   @FXML
   private HBox book_info;
   @FXML
   private HBox member_info;
   @FXML
   private Text bookName;
   @FXML
   private Text bookAuthor;
   @FXML
   private Text bookStatus;
   @FXML
   private TextField bookIdInput;
   @FXML
   private TextField memberIdInput;
   @FXML
   private Text memberName;
   @FXML
   private Text memberContact;
   @FXML
   private JFXButton issueBook;
   @FXML
   private JFXTextField bookId;
   private ListView<String> issueDataList;
   @FXML
   private JFXButton submissionBtn;
   @FXML
   private StackPane rootPane;
   @FXML
   private VBox toolBar;
   @FXML
   private Text memberNameHolder;
   @FXML
   private Text memberEmailholder;
   @FXML
   private Text bookNameHolder;

   @FXML
   private Text bookIssueDate;
   @FXML
   private Text nofDays;
   @FXML
   private Text finePerDay;

   @FXML
   private Text memberContactHolder;
   @FXML
   private Text bookAuthorHolder;
   @FXML
   private Text bookPublisherHolder;
   @FXML
   private JFXButton renewButton;
   @FXML
   private HBox submissionDataContainer;
   @FXML
   private AnchorPane rootAnchorPane;
   @FXML
   private StackPane bookInfoContainer;

   Boolean isReadyForSubmission = false;
   DatabaseHandler databaseHandler;
   PieChart bookChart;
   PieChart memberChart;
   @FXML
   private StackPane memberInfoContainer;
   @FXML
   private Tab bookIssueTab;

   @Override
   public void initialize(URL url, ResourceBundle rb) {
      JFXDepthManager.setDepth(book_info, 1);
      JFXDepthManager.setDepth(member_info, 1);

      databaseHandler = DatabaseHandler.getInstance();
//      initDrawer();
      initGraph();

   }

   @FXML
   private void loadAddMember(ActionEvent event) {
      loadWindow("/librarysystem/UI/AddMember/AddMember.fxml", "Add New Member");
   }

   @FXML
   private void loadAddBook(ActionEvent event) {
      loadWindow("/librarysystem/UI/AddBook/AddBook.fxml", "Add New Book");

   }

   @FXML
   private void loadMemberTable(ActionEvent event) {
      loadWindow("/librarysystem/UI/listmember/member_list.fxml", "Members Registered");
   }

   @FXML
   private void loadBookTable(ActionEvent event) {
      loadWindow("/library/UI/listBook/book_list.fxml", "Books");
   }

   void loadWindow(String loc, String title) {

      try {
         Parent parent = FXMLLoader.load(getClass().getResource(loc));
         Stage stage = new Stage(StageStyle.DECORATED);
         stage.setTitle(title);
         stage.setScene(new Scene(parent));
         stage.show();
      } catch (IOException ex) {
         Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
      }

   }

   @FXML
   private void loadMemberInfo(ActionEvent event) {
      enableDisableGraph(false);

      String id = memberIdInput.getText();
      String qu = "SELECT * FROM MEMBER WHERE id = '" + id + "'";
      ResultSet rs = databaseHandler.execQuery(qu);
      Boolean flag = false;

      try {
         while (rs.next()) {
            String mName = rs.getString("name");
            String mContact = rs.getString("mobile");

            memberName.setText(mName);
            memberContact.setText(mContact);

            flag = true;
         }

         if (!flag) {
            memberName.setText("No Such Member");
         }

      } catch (SQLException ex) {
         Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
      }

   }

   @FXML
   private void loadBookInfo(ActionEvent event) {
      enableDisableGraph(false);

      String bookId = bookIdInput.getText();
      String qu = "SELECT * FROM BOOK WHERE id = ' " + bookId + " ' ";
      ResultSet rs = databaseHandler.execQuery(qu);
      Boolean flag = false;

      try {
         while (rs.next()) {
            String bName = rs.getString("title");
            String bAuthor = rs.getString("author");
            Boolean bStatus = rs.getBoolean("isAvail");

            bookName.setText(bName);
            bookAuthor.setText(bAuthor);
            String status = (bStatus) ? "Available" : "Not Available";
            bookStatus.setText(status);

            flag = true;
         }

         if (!flag) {
            bookName.setText("No such book Available");
         }

      } catch (SQLException ex) {
         Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @FXML
   private void loadIssueOperation(ActionEvent event) {
      String memberID = memberIdInput.getText();
      String bookID = bookIdInput.getText();

      JFXButton yesButton = new JFXButton("YES");
      yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
         String str = "INSERT INTO ISSUE(memberID, bookID) VALUES ("
                 + "'" + memberID + "',"
                 + "'" + bookID + "')";
         String str2 = "UPDATE BOOK SET isAvail = false WHERE id = '" + bookID + "' ";
         System.out.println(str + "and" + str2);

         if (databaseHandler.execAction(str) && databaseHandler.execAction(str)) {

            JFXButton button = new JFXButton("Done");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Book Issue Complete", null);
            refreshGraphs();

         } else {

            JFXButton button = new JFXButton("OKay.I'll Check");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Issue Operation Failed", null);
         }

         clearIssueEntries();

      });

      JFXButton noButton = new JFXButton("NO");
      noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {

         JFXButton button = new JFXButton("OKay");
         AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Issue Operation Cancelled", null);

         clearIssueEntries();
      });

      AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(yesButton, noButton), "confirm issue", "Are you sure want to issue the book" + bookName.getText() + " to" + memberName.getText() + "?");

   }

   @FXML
   private void loadBookIssuedInfo(ActionEvent event) {
      clearEntries();
      ObservableList<String> issueData = FXCollections.observableArrayList();
      isReadyForSubmission = false;

      try {
         String id = bookId.getText();
         String myQuery = "SELECT ISSUE.bookID, ISSUE.memberID, ISSUE.issueTime, ISSUE.renew_Count,\n"
                 + "MEMBER.name, MEMBER.mobile, MEMBER.email,\n"
                 + "BOOK.title, BOOK.author, BOOK.publisher\n"
                 + "FROM ISSUE\n"
                 + "LEFT JOIN MEMBER\n"
                 + "ON ISSUE.memberID=MEMBER.ID\n"
                 + "LEFT JOIN BOOK\n"
                 + "ON ISSUE.bookID=BOOK.ID\n"
                 + "WHERE ISSUE.bookID='" + id + "'";

         ResultSet rs = databaseHandler.execQuery(myQuery);
         if (rs.next()) {
            memberNameHolder.setText(rs.getString("name"));
            memberEmailholder.setText(rs.getString("mobile"));
            memberContactHolder.setText(rs.getString("email"));

            bookNameHolder.setText(rs.getString("title"));
            bookAuthorHolder.setText(rs.getString("author"));
            bookPublisherHolder.setText(rs.getString("publisher"));

            Timestamp mIssueTime = rs.getTimestamp("issueTime");
            Date dateOfIssue = new Date(mIssueTime.getTime());
            bookIssueDate.setText(dateOfIssue.toString());
            Long timeElapsed = System.currentTimeMillis() - mIssueTime.getTime();
            Long daysElapsed = TimeUnit.DAYS.convert(timeElapsed, TimeUnit.MILLISECONDS);
            nofDays.setText(daysElapsed.toString());
            finePerDay.setText("Not supported  yet");

            isReadyForSubmission = true;
            disableEnableControls(true);
            submissionDataContainer.setOpacity(1);

         } else {
            // pop up jfxDialog
            JFXButton button = new JFXButton("Okay.I'll check");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No such book exists in database", null);
         }

      } catch (Exception ex) {
         ex.printStackTrace();
      }

   }

   @FXML
   private void loadSubmissionInfo(ActionEvent event) {
      if (!isReadyForSubmission) {
         Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setTitle("Failed");
         alert.setHeaderText(null);
         alert.setContentText("Please select a book to submit");
         alert.showAndWait();
         return;
      }

      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Confirm Submission Operation");
      alert.setHeaderText(null);
      alert.setContentText("Are you sure you want to return the book ?");
//            alert.showAndWait();

      Optional<ButtonType> response = alert.showAndWait();
      if (response.get() == ButtonType.OK) {
         String id = bookId.getText();
         String ac1 = "DELETE FROM ISSUE WHERE BOOKID = '" + id + "'";
         String ac2 = "UPDATE BOOK SET ISAVAL = TRUE WHERE ID = '" + id + "'";

         if (databaseHandler.execAction(ac1) && databaseHandler.execAction(ac2)) {

            JFXButton btn = new JFXButton("Done");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(btn), "The book has been submitted", null);
            loadBookIssuedInfo(null);
            disableEnableControls(false);
            submissionDataContainer.setOpacity(0);

         } else {
            JFXButton btn = new JFXButton("Okay.I'll check");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(btn), "Submission Has Failed", null);

         }
      } else {
         JFXButton btn = new JFXButton("Okay");
         AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(btn), "Submission operation cancelled", null);

         Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("Cancelled");
         alert.setHeaderText(null);
         alert.setContentText("Submission operation cancelled");
         alert.showAndWait();
      }
   }

   @FXML
   private void loadIssueOp(ActionEvent event) {

      if (!isReadyForSubmission) {
         JFXButton btn = new JFXButton("Okay");
         AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(btn), "Please select a book to renew", "Can't submit a null book");
         return;
      }

      JFXButton yesButton = new JFXButton("Yes, Please");
      yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent evnt) -> {
         String id = bookId.getText();
         String ac1 = "DELETE FROM ISSUE WHERE BOOKID = '" + id + "'";
         String ac2 = "UPDATE BOOK SET ISAVAL = TRUE WHERE ID = '" + id + "'";

         if (databaseHandler.execAction(ac1) && databaseHandler.execAction(ac2)) {

            JFXButton btn = new JFXButton("Done");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(btn), "The book has been submitted", null);
            loadBookIssuedInfo(null);
            disableEnableControls(false);
            submissionDataContainer.setOpacity(0);

         } else {
            JFXButton btn = new JFXButton("Okay.I'll check");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(btn), "Submission Has Failed", null);

         }
      });

      JFXButton noButton = new JFXButton("No");
      noButton.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent ev) -> {
         JFXButton btn = new JFXButton("Okay");
         AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(btn), "submission cancelled", null);
      });

      AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(yesButton, noButton), "Confirm submission operation", "Are you want to submit the book");

//      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//      alert.setTitle("Confirm Renew Operation");
//      alert.setHeaderText(null);
//      alert.setContentText("Are you sure you want to renew the book ?");
////            alert.showAndWait();
//
//      Optional<ButtonType> response = alert.showAndWait();
//      if (response.get() == ButtonType.OK) {
//         String ac = "UPDATE ISSUE SET issueTime = CURRENT_TIMESTAMP, RENEW_COUNT = RENEW_COUNT+1 WHERE BOOKID = '" + bookId.getText() + "'";
//         System.out.println(ac);
//
//         if (databaseHandler.execAction(ac)) {
//            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Success");
//            alert.setHeaderText(null);
//            alert.setContentText("Book Has Been Renewed");
//            alert.showAndWait();
//            loadBookIssuedInfo(null);
//         } else {
//            Alert alert1 = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Failed");
//            alert.setHeaderText(null);
//            alert.setContentText("Renew operation failed");
//            alert.showAndWait();
//         }
//      } else {
//         Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
//         alert.setTitle("Cancelled");
//         alert.setHeaderText(null);
//         alert.setContentText("Renew operation cancelled");
//         alert.showAndWait();
//      }
   }

   @FXML
   private void loadSetting(ActionEvent event) {
      loadWindow("/librarysystem/UI/settings/Settings.fxml", "Settings");
   }

   @FXML
   private void handleCloseMenu(ActionEvent event) {
      ((Stage) rootPane.getScene().getWindow()).close();
   }

   @FXML
   private void handleMenuAddBook(ActionEvent event) {
      loadWindow("/librarysystem/UI/AddBook/AddBook.fxml", "Add New Book");
   }

   @FXML
   private void handleMenuAddMember(ActionEvent event) {
      loadWindow("/librarysystem/UI/AddMember/AddMember.fxml", "Add New Member");
   }

   @FXML
   private void handleMenuViewBook(ActionEvent event) {
      loadWindow("/library/UI/listBook/book_list.fxml", "Books");
   }

   @FXML
   private void handleMenuViewMember(ActionEvent event) {
      loadWindow("/librarysystem/UI/listmember/member_list.fxml", "Members Registered");
   }

   @FXML
   private void handleMenuFullscreen(ActionEvent event) {
      Stage stage = ((Stage) rootPane.getScene().getWindow());
      stage.setFullScreen(!stage.isFullScreen());
   }

//   private void initDrawer() {
//      try {
//         VBox toolBar = FXMLLoader.load(getClass().getResource("/librarysystem/UI/main/toolBar/ToolBar.fxml"));
//         drawer.setSidePane(toolBar);
//         drawer.setDefaultDrawerSize(150);
//      } catch (IOException ex) {
//         Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
//      }
//      HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
//      task.setRate(-1);
//      hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
////         public void handle(Event event) {
//         task.setRate(task.getRate() * -1);
//         task.play();
//         if (drawer.isClosed()) {
//            drawer.open();
//            drawer.setMinWidth(200);
//         } else {
//            drawer.close();
//            drawer.setMinWidth(0);
//         }
//
//      });
//
//   }
   private void clearEntries() {
      memberNameHolder.setText("");
      memberEmailholder.setText("");
      memberContactHolder.setText("");

      bookNameHolder.setText("");
      bookAuthorHolder.setText("");
      bookPublisherHolder.setText("");

      bookIssueDate.setText("");
      nofDays.setText("");
      finePerDay.setText("");

      disableEnableControls(false);
      submissionDataContainer.setOpacity(0);
   }

   private void disableEnableControls(boolean enableConfig) {
      if (enableConfig) {
         renewButton.setDisable(false);
         submissionBtn.setDisable(false);

      } else {
         renewButton.setDisable(true);
         submissionBtn.setDisable(true);
      }

   }

   private void clearIssueEntries() {
      bookIdInput.clear();
      memberIdInput.clear();
      bookName.setText("");
      bookAuthor.setText("");
      bookStatus.setText("");
      memberName.setText("");
      memberContact.setText("");
      enableDisableGraph(true);
   }

   private void initGraph() {
      bookChart = new PieChart(databaseHandler.getBookGraphStatistics());
      bookInfoContainer.getChildren().add(bookChart);
      memberChart = new PieChart(databaseHandler.getMemberGraphStatistics());
      memberInfoContainer.getChildren().add(memberChart);

      bookIssueTab.setOnSelectionChanged((Event event) -> {
         clearIssueEntries();
         if (bookIssueTab.isSelected()) {
            refreshGraphs();
         }
      });
   }

   private void enableDisableGraph(Boolean status) {
      if (status) {
         bookChart.setOpacity(1);
         memberChart.setOpacity(1);
      } else {
         bookChart.setOpacity(0);
         memberChart.setOpacity(0);
      }
   }

   private void refreshGraphs() {
      bookChart.setData(databaseHandler.getBookGraphStatistics());
      memberChart.setData(databaseHandler.getMemberGraphStatistics());
   }

}
