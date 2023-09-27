package generic;

public class SimpleRequestObject {
    private String message;

    public SimpleRequestObject(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SimpleRequestObject{" +
                "message='" + message + '\'' +
                '}';
    }
}
