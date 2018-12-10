package sample;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class Scheduler {

  private ObservableList<Employee> empList = FXCollections.observableArrayList();
  @FXML
  private DatePicker datePicker;
  @FXML
  private TableView<Employee> empView;
  @FXML
  private TableColumn<Employee, String> empColumn;
  @FXML
  private ComboBox<Employee> empBox;
  @FXML
  private DatePicker newDate;

  @FXML
  void initialize() {
    try {
      //Set select statement to grab from employee table in database
      String selectStmt = "SELECT * FROM EMPLOYEES";

      //Calls DButil method to execute given query and stores in ResultSet
      ResultSet rsEmp = DButil.dbExecuteQuery(selectStmt);

      //Iterates through ResultSet to store employee ID and names into ObservableList
      while (rsEmp.next()) {
        Employee employee = new Employee();
        employee.setEmployeeID(rsEmp.getInt("EMPLOYEEID"));
        employee.setFirst_name(rsEmp.getString("FIRSTNAME"));
        employee.setLast_name(rsEmp.getString("LASTNAME"));
        employee.setBoth_names(employee.getFirstName(), employee.getLastName());
        employee.setMeet_date(rsEmp.getDate("MEETDATE"));
        System.out.println(employee.getMeet_date());
        empList.add(employee);
      }

    } catch (SQLException | ClassNotFoundException e) {
      System.out.println("Error");
    }

    //Creates custom cellFactory to display Employee both_name field to ComboBox
    Callback<ListView<Employee>, ListCell<Employee>> factory = lv -> new ListCell<Employee>() {
      @Override
      protected void updateItem(Employee item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? "" : item.getBoth_names());
      }
    };

    //Sets ComboBox factory to custom factory to show full name
    empBox.setCellFactory(factory);
    empBox.setButtonCell(factory.call(null));
    empBox.setItems(empList);

  }

  @FXML
  void dateSelect(ActionEvent event) {
    populateTable();
  }

  @FXML
  void addEmp(ActionEvent event) {

    //Gets currently selected date from DatePicker
    Date selectedDate = java.sql.Date.valueOf(datePicker.getValue());

    //Iterates through total list of Employees, finds one that matches the selected one from ComboBox
    //Sets the Employee's meeting date to the selected one
    //Updates Employee in database with new meet_date
    for (Employee r : empList) {
      if (r.getEmployeeID() == empBox.getValue().getEmployeeID()) {
        r.setMeet_date(selectedDate);
        String updateStmt = "UPDATE EMPLOYEES " +
            "SET MEETDATE = " + "'" + selectedDate + "'" +
            "WHERE EMPLOYEEID = " + r.getEmployeeID();
        try {
          DButil.dbExecuteUpdate(updateStmt);
        } catch (SQLException | ClassNotFoundException e) {
          System.out.println("Error occured while update operation: " + e);
        }
      }
    }

    populateTable();
  }

  @FXML
  void changeDate(ActionEvent event) {

    //Gets current date and selected Employee from TableView
    Date selectedDate = java.sql.Date.valueOf(newDate.getValue());
    Employee selectedEmp = empView.getSelectionModel().getSelectedItem();

    //Iterates through total list of Employees, finds one that matches the selected one from TableView
    //Sets the Employee's meeting date to selected one from new DatePicker
    //Updates Employee in database with new meet_date
    for (Employee r : empList) {
      if (r.getEmployeeID() == selectedEmp.getEmployeeID()) {
        r.setMeet_date(selectedDate);
      }
    }
    String updateStmt = "UPDATE EMPLOYEES " +
        "SET MEETDATE = " + "'" + selectedDate + "'" +
        "WHERE EMPLOYEEID = " + selectedEmp.getEmployeeID();
    try {
      DButil.dbExecuteUpdate(updateStmt);
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println("Error occured while update operation: " + e);
    }
    populateTable();

  }

  @FXML
  void deleteEmp(ActionEvent event) {

    //Gets currently selected Employee from TableView
    Employee selectedEmp = empView.getSelectionModel().getSelectedItem();

    //Iterates through total list of Employees, finds one that matches selected one from TableView
    //Removes Employee from the list\
    //Utilizes Iterator to avoid ConcurrentModificationException
    Iterator<Employee> iter = empList.iterator();
    while (iter.hasNext()){
      Employee emp = iter.next();
      if (emp.getEmployeeID() == selectedEmp.getEmployeeID())
        iter.remove();
    }

    //Deletes selected Employee from the database
    String updateStmt =
        "DELETE FROM EMPLOYEES " +
            "WHERE EMPLOYEEID =" + selectedEmp.getEmployeeID();
    try {
      DButil.dbExecuteUpdate(updateStmt);
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println("Error occured while delete operation: " + e);
    }
    populateTable();
  }


  private void populateTable() {

    //Set TableView column to display both_names property of Employees
    empColumn.setCellValueFactory(cellData -> cellData.getValue().both_namesProperty());

    //Convert LocalDate from DatePicker to Date for use with Employee meet_date
    Date selectedDate = java.sql.Date.valueOf(datePicker.getValue());

    //New ObservableList for displaying attending members
    ObservableList<Employee> currentEmps = FXCollections.observableArrayList();

    //Iterates through total list of Employees and finds when meet_date matches date selected
    for (Employee r : empList) {
      if (r.getMeet_date().equals(selectedDate)) {
        currentEmps.add(r);
      }
    }

    //Sets TableView to display current Employees
    empView.setItems(currentEmps);

    //Disables horizontal scrollbar on TableViews
    empView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
  }

}
