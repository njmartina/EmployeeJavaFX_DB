package sample;


import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
  void viewEmployees(ActionEvent event) {

    //Sets table columns for ID, firstName, and lastName using lambda expressions
    employeeID.setCellValueFactory(cellData -> cellData.getValue().employee_idProperty().asObject());
    employeeFirst.setCellValueFactory(cellData -> cellData.getValue().first_nameProperty());
    employeeLast.setCellValueFactory(cellData -> cellData.getValue().last_nameProperty());

    try {
      //Set select statement to grab from employee table in database
      String selectStmt = "SELECT * FROM EMPLOYEES";

      //ObservableList used to store Employee objects and show in TableView FX element
      ObservableList<Employee> empList = FXCollections.observableArrayList();

      //Calls DButil method to execute given query and stores in ResultSet
      ResultSet rsEmp = DButil.dbExecuteQuery(selectStmt);

      //Iterates through ResultSet to store employee ID and names into ObservableList
      while (rsEmp.next()){
        Employee employee = new Employee();
        employee.setEmployeeID(rsEmp.getInt(1));
        employee.setFirst_name(rsEmp.getString(2));
        employee.setLast_name(rsEmp.getString(3));

        empList.add(employee);
      }
      //Display list to TableView
      employeeTable.setItems(empList);

    } catch (SQLException | ClassNotFoundException e){
      System.out.println("Error");
    }

  }
}
