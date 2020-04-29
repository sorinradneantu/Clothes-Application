package register;
import javafx.fxml.FXML;
import exceptions.UsernameAlreadyExistsException;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import services.UserService;
import javafx.scene.text.Text;

public class RegisterController {
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private Text registerMessage;
    @FXML
    public void handleRegisterAction() {
        try {
            UserService.addUser(usernameInput.getText(), passwordInput.getText(), "Customer");
            registerMessage.setText("Account created successfully!");
        } catch (UsernameAlreadyExistsException e) {
            registerMessage.setText(e.getMessage());
        }
    }
}
