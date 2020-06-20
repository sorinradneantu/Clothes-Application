package customer;


import exceptions.UsernameAlreadyExistsException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Order;
import model.Product;
import model.ProductToOrder;
import model.User;
import services.StoreService;
import services.UserService;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewProductController {

    @FXML
    private TableView<Product> tableview;
    @FXML
    private TableColumn<Product,String> name;
    @FXML
    private TableColumn<Product,Double> price;
    @FXML
    private TableColumn<Product,String> size;
    @FXML
    private TableColumn<Product,String> quantity;
    @FXML
    private TextField filterField;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private TextField shopnameInput;
    @FXML
    private TableColumn add;
    @FXML
    private TextField productsincart;

    private final ObservableList<Product> prodList= FXCollections.observableArrayList();
    private static List<Product> products;

    ArrayList<ProductToOrder> productsOrd = new ArrayList<ProductToOrder>();
    ArrayList<Product> productstoorder = new ArrayList<Product>();

    private static int productscounter=0;
    private static int quant=0;
    private static String sizeselector;



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


        productsincart.setEditable(false);
        productsincart.setText(String.valueOf(productscounter));


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
                        final ChoiceBox selectQuantity = new ChoiceBox();

                        selectQuantity.getItems().add("1");
                        selectQuantity.getItems().add("2");
                        selectQuantity.getItems().add("3");
                        selectQuantity.getItems().add("4");
                        selectQuantity.getItems().add("5");

                        selectQuantity.setOnAction(event ->{

                            quant=Integer.parseInt((String)selectQuantity.getValue());

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

                            sizeselector=(String)selectSize.getValue();

                        });

                        setGraphic(selectSize);
                        setText(null);

                    }
                }
            };

            return cell2;

        };

        size.setCellFactory(cellFactory2);

        Callback<TableColumn<Product,String>, TableCell<Product,String>> cellFactory3=(param) -> {
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
                        final Button add = new Button("Add");

                        add.setOnAction(event ->{
                            if(quant==0)
                            {
                                JOptionPane.showMessageDialog(null, "No quantity selected !");
                                return;
                            }
                            if(!sizeselector.equals("S") && !sizeselector.equals("M") && !sizeselector.equals("L") && !sizeselector.equals("XL"))
                            {
                                JOptionPane.showMessageDialog(null, "No size selected !");
                                return;
                            }

                            Product p = tableview.getItems().get(getIndex());
                            ProductToOrder newProd = new ProductToOrder(p,quant,sizeselector);
                            productsOrd.add(newProd);
                            productscounter = productscounter+quant;
                            productsincart.setText(String.valueOf(productscounter));
                            quant=0;
                            tableview.refresh();
                        });

                        setGraphic(add);
                        setText(null);

                    }
                }
            };

            return cell;

        };

    add.setCellFactory(cellFactory3);



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


    private static List<User> users=UserService.getUsers();
    private static List<Order> orders=UserService.getOrders();



    public void placeNewOrder() throws IOException, UsernameAlreadyExistsException {

        if(productscounter==0) {
            JOptionPane.showMessageDialog(null, "No items added in cart !");
            return;
        }

        int i = 0;
        int j = 0;
        for (User user : users) {
            if (!Objects.equals(usernameInput.getText(), user.getUsername()))
                i++;
        }
        for (User user : users)
        {
            if (!Objects.equals(shopnameInput.getText(), user.getUsername()))
                j++;
        }
        if(i==users.size() || j==users.size()){
            JOptionPane.showMessageDialog(null, "Failed security test");
            productsOrd.clear();
            productscounter=0;
            productsincart.setText(String.valueOf(productscounter));
        }else {
            for (User user : users) {
                if (Objects.equals(usernameInput.getText(), user.getUsername()))
                    if (Objects.equals(user.getPassword(), UserService.encodePassword(usernameInput.getText(), passwordInput.getText())))
                    {
                        try {
                            UserService.addOrder(shopnameInput.getText(), usernameInput.getText(), productsOrd);
                            productsOrd.clear();
                            productscounter=0;
                            productsincart.setText(String.valueOf(productscounter));
                            JOptionPane.showMessageDialog(null, "Order placed successfully !");
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.getMessage());
                            productsOrd.clear();
                            productscounter=0;
                            productsincart.setText(String.valueOf(productscounter));
                        }finally
                        {
                            productsOrd.clear();
                            productscounter=0;
                            productsincart.setText(String.valueOf(productscounter));
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed security test");
                        productsOrd.clear();
                        productscounter=0;
                        productsincart.setText(String.valueOf(productscounter));
                    }
                }
            productsOrd.clear();
            productscounter=0;
            productsincart.setText(String.valueOf(productscounter));

           }
        productsOrd.clear();
        productscounter=0;
        productsincart.setText(String.valueOf(productscounter));
        UserService.loadUsersFromFile();

            shopnameInput.clear();
            passwordInput.clear();
            usernameInput.clear();

        }









}
