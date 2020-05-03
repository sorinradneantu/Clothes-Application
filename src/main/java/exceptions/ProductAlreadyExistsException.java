package exceptions;

public class ProductAlreadyExistsException extends Exception {
    private String name;

    public ProductAlreadyExistsException(String name) {
        super(String.format("An product with the name %s already exists!", name));
        this.name=name;
    }

    public String getUsername() {
        return name;
    }
}
