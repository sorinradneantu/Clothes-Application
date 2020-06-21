package store;


import exceptions.ProductAlreadyExistsException;
import exceptions.ProductDoesNotExist;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import services.StoreService;

import javax.swing.*;
import java.io.IOException;

public class DeleteProductController {
    @FXML
    private TextField productInput;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;

    public static void deleteProdPanel() throws IOException {
        Parent deleteProdWindow = FXMLLoader.load(AddProuctController.class.getResource("/delete.fxml"));
        Scene deleteProdScene = new Scene(deleteProdWindow);

        Stage window = new Stage();

        window.setScene(deleteProdScene);
        window.setTitle("Delete Product Panel");
        window.show();
    }
    public void deleteProd()throws IOException, ProductDoesNotExist , ProductAlreadyExistsException {
    try {
        StoreService.deleteData(productInput.getText(),usernameInput.getText(),passwordInput.getText());
        productInput.clear();
        passwordInput.clear();
        usernameInput.clear();
    }catch (ProductDoesNotExist e){
        JOptionPane.showMessageDialog(null, e.getMessage());
        productInput.clear();
        passwordInput.clear();
        usernameInput.clear();
    }
    }
}
