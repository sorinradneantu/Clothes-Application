package store;

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
import model.Order;
import model.OrderStatus;
import model.Product;
import model.User;
import services.FileSystemService;
import services.StoreService;
import services.UserService;


import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class StoreOrdersController {
    @FXML
    private TableView<OrderStatus> ordSt;
    @FXML
    private TableColumn<OrderStatus, String> order;
    @FXML
    private TableColumn<OrderStatus,String> accRej;  //accept/reject
    @FXML
    private TextField  filterField;
    private final ObservableList<OrderStatus> statList= FXCollections.observableArrayList();
    private static List<OrderStatus> stats;
    private static String accepReject;

    public void initialize(){
        order.setCellValueFactory(new PropertyValueFactory<OrderStatus,String>("Order"));
        for(OrderStatus or:stats){
            if(Objects.equals(or.getStatus(),"Pending")) {
                statList.add(or);
            }
        }

        FilteredList<OrderStatus> filteredData = new FilteredList<>(statList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(info -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (info.getO().getShopname().toLowerCase().indexOf(lowerCaseFilter) != -1 )
                    return true;
                else
                    return false;
            });
        });
        SortedList<OrderStatus> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(ordSt.comparatorProperty());

        ordSt.setItems(sortedData);

        Callback<TableColumn<OrderStatus,String>,TableCell<OrderStatus,String>> cellFactory2=(param) -> {
            final TableCell<OrderStatus,String> cell2 = new TableCell<OrderStatus,String>(){

                @Override
                public void updateItem(String item,boolean empty){
                    super.updateItem(item,empty);

                    if(empty)
                    {
                        setGraphic(null);
                        setText(null);
                    }else
                    {
                        final ChoiceBox selectStatus = new ChoiceBox();
                        selectStatus.getItems().add("Accept");
                        selectStatus.getItems().add("Reject");



                        selectStatus.setOnAction(event ->{

                            accepReject=(String)selectStatus.getValue();
                            OrderStatus os=ordSt.getItems().get(getIndex());
                            os.setStatus(accepReject);
                            OrderStatus temp=new OrderStatus(os.getO(),accepReject);
                            try {
                                StoreService.deleteStatus(os.getO().getShopname(),os.getO().getCustomername(),os.getO().getProductsOrd());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                StoreService.addOrdStatus(temp);
                            } catch (IOException e) {
                            }
                        });

                        setGraphic(selectStatus);
                        setText(null);

                    }
                }
            };

            return cell2;

        };
        accRej.setCellFactory(cellFactory2);
    }
    public static void viewOrdersPanel() throws IOException {
        stats = StoreService.loadStatusFromFile(FileSystemService.getPathToFile("config", "status.json"));
        Parent viewOrdersWindow = FXMLLoader.load(EditProductController.class.getResource("/storeOrder.fxml"));
        Scene viewOrdersScene = new Scene(viewOrdersWindow);

        Stage window = new Stage();

        window.setScene(viewOrdersScene);
        window.setTitle("View Orders Panel");
        window.show();
    }
}
