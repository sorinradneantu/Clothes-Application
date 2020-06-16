package store;

import exceptions.ProductAlreadyExistsException;
import exceptions.ProductDoesNotExist;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.StoreService;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class EditProductController {
    @FXML
    private TextField curentNameInput;
    @FXML
    private TextField newNameInput;
    @FXML
    private TextField newPriceInput;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    public static void EditProdPanel() throws IOException {
        Parent editProdWindow = FXMLLoader.load(EditProductController.class.getResource("/edit.fxml"));
        Scene editProdScene = new Scene(editProdWindow);

        Stage window = new Stage();

        window.setScene(editProdScene);
        window.setTitle("Edit Product Panel");
        window.show();
    }
    public void editProduct()throws IOException, ProductAlreadyExistsException {
        try {
            StoreService.editData(curentNameInput.getText(), newNameInput.getText(), newPriceInput.getText(), usernameInput.getText(), passwordInput.getText());
        }catch (ProductAlreadyExistsException | ProductDoesNotExist e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
