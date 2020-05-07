package services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.CouldNotWriteProductsException;
import exceptions.CouldNotWriteUsersException;
import exceptions.ProductAlreadyExistsException;
import exceptions.UsernameAlreadyExistsException;
import model.Product;
import model.User;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class StoreService {
   // private static List<Product> products;
    private static  List<User> users=UserService.getUsers();
    private static final Path USER_PATH = FileSystemService.getPathToFile("config", "users.json");
    public static void loadUsers() throws IOException,UsernameAlreadyExistsException {
       users=UserService.getUsers();
    }
    public static List<Product> loadDataFromFile(Path DATA_PATH) throws IOException {
        List<Product> products;
        ObjectMapper objMap = new ObjectMapper();
        objMap.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        products = objMap.readValue(DATA_PATH.toFile(), new TypeReference<List<Product>>() {
        });
        return products;
    }
    public static void addData(String name, String price, String username,String password ) throws ProductAlreadyExistsException, IOException {
        Path pth;
        int i = 0;
        for (User user : users) {
            if (!Objects.equals(username, user.getUsername()))
                i++;
        }
        if(i==users.size()){
            JOptionPane.showMessageDialog(null, "Failed security test");
        }else {
            for (User user : users) {
                if (Objects.equals(username, user.getUsername())) {
                    if (Objects.equals(user.getPassword(), UserService.encodePassword(username, password))) {
                        List<Product> products;
                        pth = FileSystemService.getPathToFile("config", username + ".json");
                        products = loadDataFromFile(pth);
                        checkProductDoesNotAlreadyExist(name, pth);
                        products.add(new Product(name, Double.parseDouble(price)));
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            objectMapper.writerWithDefaultPrettyPrinter().writeValue(pth.toFile(), products);
                        } catch (IOException e) {
                            throw new CouldNotWriteProductsException();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed security test");
                    }
                }
            }
        }
    }
    private static void checkProductDoesNotAlreadyExist(String name,Path pth) throws ProductAlreadyExistsException,IOException {
        List<Product> products;
        products=loadDataFromFile(pth);
        for (Product prod:products) {
            if (Objects.equals(name, prod.getName()))
                throw new ProductAlreadyExistsException(name);
        }
    }

}
