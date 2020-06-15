package exceptions;

public class ProductDoesNotExist extends Exception{
    private String name;

    public ProductDoesNotExist(String name) {
        super(String.format("An product with the name %s does not exists!", name));
        this.name=name;
    }

    public String getUsername() {
        return name;
    }
}
