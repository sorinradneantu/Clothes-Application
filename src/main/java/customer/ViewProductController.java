package customer;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Product;
import services.StoreService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ViewProductController {

    @FXML
    private TableView<Product> tableview;
    @FXML
    private TableColumn<Product,String> name;
    @FXML
    private TableColumn<Product,Double> price;
    @FXML
    private TableColumn size;
    @FXML
    private TableColumn quantity;
    @FXML
    private TextField filterField;


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


        Callback<TableColumn<Product,String>, TableCell<Product,String>> cellFactory=(param) -> {
            final TableCell<Product,String> cell = new TableCell<Product,String>(){

                @Override
                public void updateItem(String item,boolean empty){
                    super.updateItem(item,empty);

                    if(empty)
                    {
                        setGraphic(null);
                        setText(null);
                    }else
                    {
                        final TextField selectQuantity = new TextField("0");

                        selectQuantity.setOnAction(event ->{

                            Product p = getTableView().getItems().get(getIndex());
                        });

                        setGraphic(selectQuantity);
                        setText(null);

                    }
                }
            };

            return cell;

        };

        quantity.setCellFactory(cellFactory);

        Callback<TableColumn<Product,String>,TableCell<Product,String>> cellFactory2=(param) -> {
            final TableCell<Product,String> cell2 = new TableCell<Product,String>(){

                @Override
                public void updateItem(String item,boolean empty){
                    super.updateItem(item,empty);

                    if(empty)
                    {
                        setGraphic(null);
                        setText(null);
                    }else
                    {
                        final ChoiceBox selectSize = new ChoiceBox();
                        selectSize.getItems().add("S");
                        selectSize.getItems().add("M");
                        selectSize.getItems().add("L");
                        selectSize.getItems().add("XL");


                        selectSize.setOnAction(event ->{

                            Product p = getTableView().getItems().get(getIndex());
                        });

                        setGraphic(selectSize);
                        setText(null);

                    }
                }
            };

            return cell2;

        };

        size.setCellFactory(cellFactory2);





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
