package login;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class LoginController {
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private ChoiceBox roleInput;

    public void initialize(){
        roleInput.getItems().setAll("Customer","Store","Admin");
    }

}
