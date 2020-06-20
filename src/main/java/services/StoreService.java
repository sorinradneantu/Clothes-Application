package services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.webkit.dom.DocumentImpl;
import exceptions.*;
import model.*;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class StoreService {
   // private static List<Product> products;
    private static  List<User> users=UserService.getUsers();
    private static final Path USER_PATH = FileSystemService.getPathToFile("config", "users.json");
    public static void loadUsers() throws IOException,UsernameAlreadyExistsException {
       users=UserService.getUsers();
    }
    public static List<OrderStatus> loadStatusFromFile(Path DATA_PATH) throws IOException {
        List<OrderStatus> stats;
        ObjectMapper objMap = new ObjectMapper();
        objMap.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        stats = objMap.readValue(DATA_PATH.toFile(), new TypeReference<List<OrderStatus>>() {
        });
        return stats;
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
    public static void deleteData(String prodname,String username,String password)throws IOException,ProductDoesNotExist,ProductAlreadyExistsException{
        Path pth;
        int i = 0;
        for (User user : users) {
            if (!Objects.equals(username, user.getUsername()))
                i++;
        }
        if(i==users.size()){
            JOptionPane.showMessageDialog(null, "Failed security test");
        }else {
            for(User user:users){
                if (Objects.equals(username, user.getUsername())){
                    if (Objects.equals(user.getPassword(), UserService.encodePassword(username, password))) {
                        List<Product> products;
                        pth = FileSystemService.getPathToFile("config", username + ".json");
                        products = loadDataFromFile(pth);
                        checkProductDoesNotExist(prodname,pth);
                        for (Iterator<Product> iter = products.listIterator(); iter.hasNext(); ) {
                            Product a = iter.next();
                            if (Objects.equals(a.getName(),prodname)) {
                                iter.remove();
                            }
                        }
                        try {
                            FileWriter fwrite = new FileWriter(String.valueOf(pth));
                            fwrite.write("[]");
                            fwrite.close();
                        } catch (IOException e) {
                            System.out.println("An error occurred.");
                            e.printStackTrace();
                        }
                        for(Product prod:products) {
                            String tmp=String.valueOf(prod.getPrice());
                            addData(prod.getName(), tmp, username, password);
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Failed security test");
                    }
                }
            }
        }
    }

    public static void editData(String curentNameInput,String newNameInput,String  newPriceInput,String usernameInput, String passwordInput) throws IOException, ProductAlreadyExistsException, ProductDoesNotExist {
        Path pth;
        int i = 0;
        for (User user : users) {
            if (!Objects.equals(usernameInput, user.getUsername()))
                i++;
        }
        if(i==users.size()){
            JOptionPane.showMessageDialog(null, "Failed security test");
        }else{
            for(User user : users){
                if (Objects.equals(usernameInput, user.getUsername())){
                    if (Objects.equals(user.getPassword(), UserService.encodePassword(usernameInput, passwordInput))){
                        pth = FileSystemService.getPathToFile("config", usernameInput + ".json");
                       deleteData(curentNameInput,usernameInput,passwordInput);
                       addData(newNameInput,newPriceInput,usernameInput,passwordInput);
                    }else{
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
    private static void checkProductDoesNotExist(String name, Path pth) throws ProductDoesNotExist,IOException{
        List<Product> products;
        products=loadDataFromFile(pth);
        int i=0;
        for(Product prod:products){
            if(!Objects.equals(name, prod.getName()))
                i=i+1;
        }
        if(i==products.size()){
            throw new ProductDoesNotExist(name);
        }
    }
    public static void addOrdStatus(OrderStatus o) throws IOException {
        Path pth;
        pth = FileSystemService.getPathToFile("config",  "status.json");
        List<OrderStatus> stats;
        stats = loadStatusFromFile(pth);
        stats.add(o);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(pth.toFile(),stats);
        } catch (IOException e) {
            throw new CouldNotWriteOrderException();
        }
    }
    public static void deleteStatus(String shopname, String customername, ArrayList<ProductToOrder> productsOrd ) throws IOException {
        Path pth;
        List<OrderStatus> stats;
        pth = FileSystemService.getPathToFile("config", "status.json");
        stats = loadStatusFromFile(pth);
       for (Iterator<OrderStatus> iter = stats.listIterator(); iter.hasNext(); ) {
            OrderStatus a = iter.next();
            if (Objects.equals(shopname,a.getO().getShopname())) {
                if(Objects.equals(a.getO().getCustomername(),customername)){
                    if(Objects.equals(a.getO().getProductsOrd(),productsOrd)) {
                        iter.remove();
                    }
                }
            }
        }
        try {
            FileWriter fwrite = new FileWriter(String.valueOf(pth));
            fwrite.write("[]");
            fwrite.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"An error occurre");
            e.printStackTrace();
        }
      for(OrderStatus os:stats) {
          StoreService.addOrdStatus(os);
       }

    }

}
