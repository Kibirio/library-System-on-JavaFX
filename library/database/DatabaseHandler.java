/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.database;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


/**
 *
 * @author Haryx_PurpleSoft
 */
public class DatabaseHandler {
   
    private static DatabaseHandler handler;
    
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/library", "root", "";
    private static Connection conn = null;
    private static Statement stmt = null;
    
    private DatabaseHandler ()
    {
        createConnection();
        setupBookTable();
        setupMemberTable();
    }
    
    public static DatabaseHandler getInstance()
    {
        if (handler ==  null)
        {
            handler = new DatabaseHandler();
        }
        return handler;
    }
    
    void createConnection()
    {
        try
        {
           Class.forName("com.mysql.jdbc.Driver");
           conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "root");
        } 
        catch (Exception ex) {
             ex.printStackTrace();
         }
    }
    
    void setupBookTable()
    {
        String TABLE_NAME = "BOOK";
        try
        {
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(), null); 
            
//            if (tables.next())
//            {
//                System.out.println("Table" + TABLE_NAME + "already exists.Ready for go");
//            }
//            else
//            {
//                stmt.execute("CREATE TABLE" + TABLE_NAME + "("
//                        + "     id varchar(200) primary key,\n"
//                        + "     title varchar(200),\n"
//                        + "     author varchar(200),\n"
//                        + "     publisher varchar(200),\n"
//                        + "     isAvail boolean default true"
//                        + " )");
//            }
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage() + "... setupDatabase");
        }
        finally
        {
            
        }
    }
    
    public ResultSet execQuery(String query)
    {
        ResultSet result;
        try
        {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }
        catch(SQLException ex)
        {
            System.out.println("Exception at execQuery:datahandler" + ex.getLocalizedMessage());
            return null;
        }
        finally {}
        
        return result;
    }
    
    public boolean execAction(String qu)
    {
        try
        {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error:" +ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        finally{}
    }
    
    void setupMemberTable()
    {
        
      String TABLE_NAME = "MEMBER";
        try
        {
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(), null); 
            
            if (tables.next())
            {
                System.out.println("Table" + TABLE_NAME + "already exists.Ready for go");
            }
            else
            {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     id varchar(200) primary key,\n"
                        + "     name varchar(200),\n"
                        + "     mobile varchar(200),\n"
                        + "     email varchar(200)\n"
                        + " )");
            }
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage() + "... setupDatabase");
        }
        finally
        {
            
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
