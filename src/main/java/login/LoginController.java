package login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import services.UserService;

public class LoginController {
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private ChoiceBox roleInput;

    public void initialize() {
        roleInput.getItems().setAll("Customer", "Store", "Admin");

    }

    public void openRegister() throws Exception {

        Parent registerWindow = FXMLLoader.load(getClass().getResource("/register.fxml"));
        Scene registerScene = new Scene(registerWindow);

        Stage window = new Stage();

        window.setScene(registerScene);
        window.setTitle("Register");
        window.show();
    }
    public void login() throws Exception {

        UserService.checkUsers(usernameInput.getText(),passwordInput.getText(),(String)roleInput.getValue());
    }
}
