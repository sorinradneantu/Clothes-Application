package admin;

import exceptions.UsernameAlreadyExistsException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;

public class AdminController {

    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private Text registerMessage;

    public static void openAdminPanel() throws IOException {
        Parent adminWindow = FXMLLoader.load(AdminController.class.getResource("/admin.fxml"));
        Scene adminScene = new Scene(adminWindow);

        Stage window = new Stage();

        window.setScene(adminScene);
        window.setTitle("Admin Panel");
        window.show();
    }

    public void registerStore() throws UsernameAlreadyExistsException {
        try {
            UserService.addUser(usernameInput.getText(), passwordInput.getText(), "Store");
            registerMessage.setText("Account created successfully!");
        }catch(UsernameAlreadyExistsException e)
            {
                registerMessage.setText(e.getMessage());
            }
        }

}
