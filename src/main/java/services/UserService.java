package services;

import admin.AdminController;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.CouldNotWriteUsersException;
import exceptions.UsernameAlreadyExistsException;
import model.User;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import store.StoreController;

import javax.swing.*;

public class UserService {

    private static List<User> users;
    private static final Path USERS_PATH = FileSystemService.getPathToFile("config", "users.json");

    public static void loadUsersFromFile() throws IOException,UsernameAlreadyExistsException {

        if (!Files.exists(USERS_PATH)) {
            FileUtils.copyURLToFile(UserService.class.getClassLoader().getResource("users.json"), USERS_PATH.toFile());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        users = objectMapper.readValue(USERS_PATH.toFile(), new TypeReference<List<User>>() {
        });
        if(users.size()==0)
        {
            UserService.addUser("Admin","Admin1234","Admin");
        }
    }
    public static List<User> getUsers(){
        return users;
    }

    public static void addUser(String username, String password, String role) throws UsernameAlreadyExistsException {
        checkUserDoesNotAlreadyExist(username);
        users.add(new User(username, encodePassword(username, password), role));
        persistUsers();
        if(Objects.equals(role,"Store")) {
            Path STORE_PATH = FileSystemService.getPathToFile("config", username + ".json");
            try {
                File myObj = new File(String.valueOf(STORE_PATH));
                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            try {
                FileWriter fwrite = new FileWriter(String.valueOf(STORE_PATH));
                fwrite.write("[]");
                fwrite.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    private static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExistsException {
        for (User user : users) {
            if (Objects.equals(username, user.getUsername()))
                throw new UsernameAlreadyExistsException(username);
        }
    }

    private static void persistUsers() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(USERS_PATH.toFile(), users);
        } catch (IOException e) {
            throw new CouldNotWriteUsersException();
        }
    }

    public static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // This is the way a password should be encoded when checking the credentials
        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", ""); //to be able to save in JSON format
    }

    public static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }
    public static void checkUsers(String username,String password,String role) throws IOException {
        int i = 0;
        for (User user : users) {
            if (!Objects.equals(username, user.getUsername()))
                i++;
        }
        if (i == users.size()) {
            JOptionPane.showMessageDialog(null, "Wrong credentials");
        } else {
            for (User user : users) {
                if (Objects.equals(username, user.getUsername())) {
                    if (Objects.equals(user.getPassword(), encodePassword(username, password))) {
                        if (Objects.equals(role, user.getRole())) {
                            if (Objects.equals(role, "Customer")) {
                                JOptionPane.showMessageDialog(null, "Logged as CUSTOMER");
                            } else if (Objects.equals(role, "Admin")) {
                                {
                                    AdminController.openAdminPanel();
                                }
                            } else if (Objects.equals(role, "Store")) {
                                Path STORE_PATH = FileSystemService.getPathToFile("config", username + ".json");
                                StoreController.openStorePanel(STORE_PATH);
                            } else {
                                JOptionPane.showMessageDialog(null, "Wrong credentials");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Wrong credentials");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong credentials");
                    }
                }
            }

        }
    }

}
