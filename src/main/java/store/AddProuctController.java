package store;

import exceptions.ProductAlreadyExistsException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.StoreService;

import javax.swing.*;
import java.io.IOException;

public class AddProuctController {
    @FXML
    private TextField nameInput;
    @FXML
    private TextField priceInput;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;

    public static void openAddProdPanel() throws IOException {
        Parent addProdWindow = FXMLLoader.load(AddProuctController.class.getResource("/addProduct.fxml"));
        Scene addProdScene = new Scene(addProdWindow);

        Stage window = new Stage();

        window.setScene(addProdScene);
        window.setTitle("Add Product Panel");
        window.show();
    }
    public void addNewProd() throws ProductAlreadyExistsException, IOException {
    try {
        StoreService.addData(nameInput.getText(), priceInput.getText(), usernameInput.getText(), passwordInput.getText());
        nameInput.clear();
        passwordInput.clear();
        priceInput.clear();
        usernameInput.clear();
    }catch(ProductAlreadyExistsException e){
        JOptionPane.showMessageDialog(null, e.getMessage());
        nameInput.clear();
        passwordInput.clear();
        priceInput.clear();
        usernameInput.clear();
    }
    }
}
