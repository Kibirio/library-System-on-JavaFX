package library.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import library.UI.listBook.BookListController.Book;
import librarysystem.UI.listmember.MemberListController.Member;

public class DatabaseHandler {

   private static DatabaseHandler handler;

//    private static final String DB_URL = "jdbc:mysql://localhost:3306/library", "root", "";
   private static Connection conn = null;
   private static Statement stmt = null;

   private DatabaseHandler() {
      createConnection();
      setupBookTable();
      setupMemberTable();
      setupIssueTable();
   }

   public static DatabaseHandler getInstance() {
      if (handler == null) {
         handler = new DatabaseHandler();
      }
      return handler;
   }

   void createConnection() {
      try {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "root");
      } catch (Exception ex) {
         JOptionPane.showMessageDialog(null, "cant load database", "Database Error", JOptionPane.ERROR);
         System.exit(0);
      }
   }

   void setupBookTable() {
      String TABLE_NAME = "BOOK";
      try {
         stmt = conn.createStatement();

         DatabaseMetaData dbm = conn.getMetaData();
         ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

         if (tables.next()) {
            System.out.println("Table" + TABLE_NAME + "already exists.Ready for go");
         } else {
            stmt.execute("CREATE TABLE" + TABLE_NAME + "("
                    + "     id varchar(200) primary key,\n"
                    + "     title varchar(200),\n"
                    + "     author varchar(200),\n"
                    + "     publisher varchar(200),\n"
                    + "     isAvail boolean default true"
                    + " )");
         }
      } catch (SQLException e) {
         System.err.println(e.getMessage() + "... setupDatabase");
      } finally {

      }
   }

   public ResultSet execQuery(String query) {
      ResultSet result;
      try {
         stmt = conn.createStatement();
         result = stmt.executeQuery(query);
      } catch (SQLException ex) {
         System.out.println("Exception at execQuery:datahandler" + ex.getLocalizedMessage());
         return null;
      } finally {
      }

      return result;
   }

   public boolean execAction(String qu) {
      try {
         stmt = conn.createStatement();
         stmt.execute(qu);
         return true;
      } catch (SQLException ex) {
         JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
         return false;
      } finally {
      }
   }

   void setupMemberTable() {

      String TABLE_NAME = "MEMBER";
      try {
         stmt = conn.createStatement();

         DatabaseMetaData dbm = conn.getMetaData();
         ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

         if (tables.next()) {
            System.out.println("Table" + TABLE_NAME + "already exists.Ready for go");
         } else {
            stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                    + "     id varchar(200) primary key,\n"
                    + "     name varchar(200),\n"
                    + "     mobile varchar(200),\n"
                    + "     email varchar(200)\n"
                    + " )");
         }
      } catch (SQLException e) {
         System.err.println(e.getMessage() + "... setupDatabase");
      } finally {

      }
   }

   void setupIssueTable() {
      String TABLE_NAME = "ISSUE";
      try {
         stmt = conn.createStatement();

         DatabaseMetaData dbm = conn.getMetaData();
         ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

         if (tables.next()) {
            System.out.println("Table" + TABLE_NAME + "already exists.Ready for go");
         } else {
            stmt.execute("CREATE TABLE" + " " + TABLE_NAME + "("
                    + "     bookID varchar(200) primary key,\n"
                    + "     memberID varchar(200),\n"
                    + "     issueTime timestamp default CURRENT_TIMESTAMP ,\n"
                    + "     renew_Count integer default 0,\n"
                    + "     FOREIGN KEY (bookID) REFERENCES BOOK(id), \n"
                    + "     FOREIGN KEY (memberID) REFERENCES MEMBER(id) \n"
                    + " )");
         }
      } catch (SQLException e) {
         System.err.println(e.getMessage() + "... setupDatabase");
      } finally {

      }
   }

   public boolean deleteBook(Book book) {
      try {
         String deleteStatement = "DELETE FROM BOOK WHERE ID = ?";
         PreparedStatement stmt = conn.prepareStatement(deleteStatement);
         stmt.setString(1, book.getId());
         int res = stmt.executeUpdate();
//         System.out.println(res);
         if (res == 1) {
            return true;
         }

      } catch (SQLException ex) {
         Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
      return false;
   }

   public boolean deleteMember(Member member) {
      try {
         String deleteStmt = "DELETE FROM MEMBER WHERE ID =?";
         PreparedStatement ps = conn.prepareStatement(deleteStmt);
         ps.setString(1, member.getId());
         int res = ps.executeUpdate();
         if (res == 1) {
            return true;
         }

      } catch (SQLException ex) {
         Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
      }

      return false;
   }

   //checking whether the book is issued before deletion
   public boolean isBookAlraedyIssued(Book book) {
      try {
         String checkStmt = "SELECT COUNT(*) FROM ISSUE WHERE bookId = ?";
         PreparedStatement stmt = conn.prepareStatement(checkStmt);
         stmt.setString(1, book.getId());
         ResultSet rs = stmt.executeQuery();

         if (rs.next()) {
            int count = rs.getInt(1);
            System.out.println(count);

            return (count > 0);
         }
      } catch (SQLException ex) {
         Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
      return false;
   }

   // method to update book when the after the book is edited
   public boolean updateBook(Book book) {
      try {
         String update = "UPDATE BOOK SET TITLE =?, AUTHOR = ?, PUBLISHER =? WHERE ID =?";
         PreparedStatement stmt = conn.prepareStatement(update);
         stmt.setString(1, book.getTitle());
         stmt.setString(2, book.getAuthor());
         stmt.setString(3, book.getPublisher());
         stmt.setString(4, book.getId());
         int res = stmt.executeUpdate();

         return (res > 0);
      } catch (SQLException ex) {
         Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
      return false;
   }

   public boolean updateMember(Member member) {
      try {
         String update = "UPDATE MEMBER SET NAME =?, MOBILE = ?, EMAIL =? WHERE ID =?";
         PreparedStatement stmt = conn.prepareStatement(update);
         stmt.setString(1, member.getName());
         stmt.setString(2, member.getMobile());
         stmt.setString(3, member.getEmail());
         stmt.setString(4, member.getId());
         int res = stmt.executeUpdate();

         return (res > 0);
      } catch (SQLException ex) {
         Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
      return false;
   }

}
