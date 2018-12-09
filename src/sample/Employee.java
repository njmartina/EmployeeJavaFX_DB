package sample;

import java.time.LocalDate;
import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Nicolo Martina, Employee class for implementation with database StringProperty and
 * IntegerProperty used for use in TableView FX element Basic class with constructor, getters, and
 * setters
 */
public class Employee {

  private IntegerProperty employee_id;
  private StringProperty first_name;
  private StringProperty last_name;
  private StringProperty both_names;
  private Date meet_date;

  public Employee() {
    this.employee_id = new SimpleIntegerProperty();
    this.first_name = new SimpleStringProperty();
    this.last_name = new SimpleStringProperty();
    this.both_names = new SimpleStringProperty();
  }

  public int getEmployeeID() {
    return employee_id.get();
  }

  public IntegerProperty employee_idProperty() {
    return employee_id;
  }

  public void setEmployeeID(int employee_id) {
    this.employee_id.set(employee_id);
  }

  public String getFirstName() {
    return first_name.get();
  }

  public StringProperty first_nameProperty() {
    return first_name;
  }

  public void setFirst_name(String first_name) {
    this.first_name.set(first_name);
  }

  public String getLastName() {
    return last_name.get();
  }

  public StringProperty last_nameProperty() {
    return last_name;
  }

  public void setLast_name(String last_name) {
    this.last_name.set(last_name);
  }

  public void setBoth_names(String first_name, String last_name){
    this.both_names.set(first_name + " " + last_name);
  }

  public String getBoth_names(){
    return both_names.get();
  }

  public StringProperty both_namesProperty(){
    return both_names;
  }

  public void setMeet_date(Date date){
    this.meet_date = date;
  }

  public Date getMeet_date(){
    return meet_date;
  }

}
