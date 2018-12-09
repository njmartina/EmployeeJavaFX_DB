package sample;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class Controller {
  @FXML
  private TableView employeeTable;
  @FXML
  private TableColumn<Employee, Integer> employeeID;
  @FXML
  private TableColumn<Employee, String> employeeFirst;
  @FXML
  private TableColumn<Employee, String> employeeLast;
  @FXML
  private TextField firstnameText;
  @FXML
  private TextField lastnameText;
  @FXML
  private TextField empID;
  @FXML
  private TextField updateID;
  @FXML
  private TextField updateFirst;
  @FXML
  private TextField updateLast;


  void populateEmployees(){
    //Sets table columns for ID, firstName, and lastName using lambda expressions
    employeeID.setCellValueFactory(cellData -> cellData.getValue().employee_idProperty().asObject());
    employeeFirst.setCellValueFactory(cellData -> cellData.getValue().both_namesProperty());
    employeeLast.setCellValueFactory(cellData -> cellData.getValue().last_nameProperty());


  }

  @FXML
  void addEmployee(ActionEvent event) {
    String updateStmt =
        "INSERT INTO EMPLOYEES " +
            "(FIRSTNAME, LASTNAME) " +
            "VALUES " +
            "('" + firstnameText.getText() + "', '" + lastnameText.getText() + "')";

    //Execute DELETE operation
    try {
      DButil.dbExecuteUpdate(updateStmt);
    } catch (SQLException | ClassNotFoundException e) {
      System.out.print("Error occurred while ADD Operation: " + e);
    }
    populateEmployees();
  }

  @FXML
  void deleteEmployee(ActionEvent event) {
    String updateStmt =
        "DELETE FROM EMPLOYEES " +
            "WHERE EMPLOYEEID =" + empID.getText();

    try {
      DButil.dbExecuteUpdate(updateStmt);
    } catch (SQLException | ClassNotFoundException e){
      System.out.println("Error occured while delete operation: " + e);
    }
    populateEmployees();
  }


  @FXML
  void viewEmployees(ActionEvent event) {
    populateEmployees();
  }

  @FXML
  void updateEmployee(ActionEvent event) {
    String updateStmt = "UPDATE EMPLOYEES " +
         "SET FIRSTNAME = " + "'" + updateFirst.getText() + "'," +
        " LASTNAME = " + "'" + updateLast.getText() + "'" +
        "WHERE EMPLOYEEID = " + updateID.getText();

    try {
      DButil.dbExecuteUpdate(updateStmt);
    } catch (SQLException | ClassNotFoundException e){
      System.out.println("Error occured while update operation: " + e);
    }
    populateEmployees();
  }
}
