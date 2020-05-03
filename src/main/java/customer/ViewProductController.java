package customer;

import admin.AdminController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
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

public class ViewProductController {

    @FXML
    TableView<Product> tableview;
    @FXML
    TableColumn<Product,String> name;
    @FXML
    TableColumn<Product,Double> price;
    @FXML
    TableColumn<Product, ChoiceBox> size;
    @FXML
    TableColumn<Product,ChoiceBox> quantity;
    @FXML
    TextField filterField;


    private final ObservableList<Product> prodList= FXCollections.observableArrayList();
    private static List<Product> products;


    public static void openViewProductsPanel(Path pth) throws IOException {

        products=StoreService.loadDataFromFile(pth);
        Parent viewProductsWindow = FXMLLoader.load(ViewProductController.class.getResource("/viewproducts.fxml"));
        Scene viewProductsScene = new Scene(viewProductsWindow);

        Stage window = new Stage();

        window.setScene(viewProductsScene);
        window.setTitle("Shop panel");
        window.show();
    }

    public void initialize(){
        name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        price.setCellValueFactory(new PropertyValueFactory<>("Price"));

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


}
