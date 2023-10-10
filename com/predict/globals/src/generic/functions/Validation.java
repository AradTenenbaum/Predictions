package generic.functions;

public class Validation {

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int getInteger(String integer) {
        return Integer.parseInt(integer);
    }
}
