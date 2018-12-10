package sample;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Nicolo Martina, modified code from Prof. Vanselow and ONUR BASKIRT
 */

public class DButil {

    //Connection and database url
    private static Connection connection = null;

    final static String DATABASE_URL = "jdbc:derby:lib\\employeeDB";

    //Connect to database
    public static void dbConnect() throws SQLException, ClassNotFoundException {

      try {
        connection = DriverManager.getConnection(DATABASE_URL, "", "");
      } catch (SQLException e){
        System.out.println("Connection failed");
        throw e;
      }
    }
    //Disconnect from database
    public static void dbDisconnect() throws SQLException {
      try {
        if (connection != null && !connection.isClosed()) {
          connection.close();
        }
      } catch (Exception e) {
        throw e;
      }
    }

    //Execute query function
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {

      //Declare as null
      Statement stmt = null;
      ResultSet resultSet = null;
      CachedRowSetImpl crs = null;

      try{
        //Connect to database
        dbConnect();
        System.out.println("Select statement: " + queryStmt + "\n");

        //Create statement
        stmt = connection.createStatement();

        //Execute query into ResultSet
        resultSet = stmt.executeQuery(queryStmt);

        //CachedRowSetImplementation in order to avoid SQL connection error
        crs = new CachedRowSetImpl();
        crs.populate(resultSet);
      } catch (SQLException e){
        System.out.println("Problem occured at executeQuery: " + e);
        throw e;
      } finally {

        //Close ResultSet and statement
        if (resultSet != null){
          resultSet.close();
        }
        if (stmt != null){
          stmt.close();
        }
        //Close connection
        dbDisconnect();
      }
      //Return CachedRowSet
      return crs;
    }

  public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
    //Declare statement as null
    Statement stmt = null;
    try {
      //Connect to DB (Establish Oracle Connection)
      dbConnect();
      //Create Statement
      stmt = connection.createStatement();
      //Run executeUpdate operation with given sql statement
      stmt.executeUpdate(sqlStmt);
    } catch (SQLException e) {
      System.out.println("Problem occurred at executeUpdate operation : " + e);
      throw e;
    } finally {
      if (stmt != null) {
        //Close statement
        stmt.close();
      }
      //Close connection
      dbDisconnect();
    }
  }
}
