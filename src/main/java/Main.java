import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.FileSystemService;
import services.UserService;

import java.nio.file.Path;

public class  Main extends Application {
    private static final Path USERS_PATH = FileSystemService.getPathToFile("config", "users.json");

    Stage window;
    Scene loginScene;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

       //System.out.println(USERS_PATH);
        //System.out.println("java version: "+System.getProperty("java.version"));
       // System.out.println("javafx.version: " + System.getProperty("javafx.version"));
        UserService.loadUsersFromFile();

        window=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        loginScene = new Scene(root,767,552);
        window.setScene(loginScene);
        window.setTitle("Login");
        window.show();


    }
}
