package services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.CouldNotWriteUsersException;
import exceptions.UsernameAlreadyExistsException;
import model.User;
import org.apache.commons.io.FileUtils;
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

import javax.swing.*;

public class UserService {

    private static List<User> users;
    private static final Path USERS_PATH = FileSystemService.getPathToFile("config", "users.json");
    private static JSONArray jrr=new JSONArray();

    public static void loadUsersFromFile() throws IOException {

        if (!Files.exists(USERS_PATH)) {
            FileUtils.copyURLToFile(UserService.class.getClassLoader().getResource("users.json"), USERS_PATH.toFile());
        }


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        users = objectMapper.readValue(USERS_PATH.toFile(), new TypeReference<List<User>>() {
        });
        for(User user : users)
        {
            JSONObject newObj = new JSONObject();
            newObj.put("Username",user.getUsername());
            newObj.put("Password",user.getPassword());
            newObj.put("Role",user.getRole());
            jrr.add(newObj);
        }
    }

    public static void addUser(String username, String password, String role) throws UsernameAlreadyExistsException {
        checkUserDoesNotAlreadyExist(username);
        users.add(new User(username, encodePassword(username, password), role));
        persistUsers();

        JSONObject newObj = new JSONObject();
        newObj.put("Username",username);
        newObj.put("Password",password);
        newObj.put("Role",role);
        jrr.add(newObj);
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

    private static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }
    public static void checkUsers(String username,String password,String role) {
        JSONObject obj = new JSONObject();
        obj.put("Username", username);
        obj.put("Password", encodePassword(username, password));
        obj.put("Role", role);

        int i;
        for (i = 0; i < jrr.size(); i++) {
            if (obj.equals(jrr.get(i))) {
                if (obj.get("Role").equals("Admin"))
                    JOptionPane.showMessageDialog(null, "Logged as ADMIN");
                if (obj.get("Role").equals("Customer"))
                    JOptionPane.showMessageDialog(null, "Logged as CUSTOMER");
                if (obj.get("Role").equals("Store manager"))
                    JOptionPane.showMessageDialog(null, "Logged as STORE MANAGER");
            } else if (i == jrr.size() - 1) {
                JOptionPane.showMessageDialog(null, "Incorrect user or password !");
            }
        }
    }

}
