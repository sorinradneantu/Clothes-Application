package store;

import admin.AdminController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
import services.StoreService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Observable;


public class StoreController {
    private final ObservableList<Product> prodList= FXCollections.observableArrayList();
    private static List<Product> products;
    @FXML
    private TableView<Product> tableview;
    @FXML
    private TableColumn<Product,String> name;
    @FXML
    private TableColumn<Product,Double> price;
    @FXML
    private TableColumn<Product, Button> actions;
    @FXML
    private TextField filterField;
    public static void openStorePanel(Path pth) throws IOException {
        //load data from file with store name
       products=StoreService.loadDataFromFile(pth);
        Parent storeWindow = FXMLLoader.load(AdminController.class.getResource("/store.fxml"));
        Scene storeScene = new Scene(storeWindow);

        Stage window = new Stage();

        window.setScene(storeScene);
        window.setTitle("Store Panel");
        window.show();
    }



    public void initialize(){
        name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        actions.setCellValueFactory(new PropertyValueFactory<>("Actions"));
        for(Product prod:products){
            prodList.add(prod);
        }
        FilteredList<Product> filteredData = new FilteredList<>(prodList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {


                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (product.getName().toLowerCase().indexOf(lowerCaseFilter) != -1 )
                    return true;
                else
                    return false;
            });
        });
        SortedList<Product> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableview.comparatorProperty());

        tableview.setItems(sortedData);
    }
    public void delProd() throws IOException{
        DeleteProductController.deleteProdPanel();
    }
    public void addProd() throws IOException {
        AddProuctController.openAddProdPanel();

    }

}
