package customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Product;
import model.User;
import services.FileSystemService;
import services.UserService;
import java.util.List;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class CustomerController {

    public final ObservableList<User> stores = FXCollections.observableArrayList();

    private static List<User> users = UserService.getUsers();

   @FXML
   private TableView<User> tableview;
   @FXML
   private TableColumn<User,String> username;
   @FXML
   private TableColumn<User,Button> view_products;
   @FXML
   private TextField filterField;

    public static void openCustomerPanel() throws IOException {
        Parent customerWindow = FXMLLoader.load(CustomerController.class.getResource("/customer.fxml"));
        Scene customerScene = new Scene(customerWindow);

        Stage window = new Stage();

        window.setScene(customerScene);
        window.setTitle("Customer Panel");
        window.show();
    }

    public void initialize(){
        username.setCellValueFactory(new PropertyValueFactory<User,String>("Username"));
        view_products.setCellValueFactory(new PropertyValueFactory<User,Button>("View Products"));

        for(User user : users)
        {
            if(Objects.equals(user.getRole(),"Store"))
                stores.add(user);
        }

        FilteredList<User> filteredData = new FilteredList<>(stores, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(store -> {


                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (store.getUsername().toLowerCase().indexOf(lowerCaseFilter) != -1 )
                    return true;
                else
                    return false;
            });
        });
        SortedList<User> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableview.comparatorProperty());

        tableview.setItems(sortedData);
    }



}
