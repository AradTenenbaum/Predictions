package utils;

public class Url {
    public static String addQueryParam(String url, String key, String value) {
        return url + "&" + key + "=" + value;
    }

    public static String addFirstQueryParam(String url, String key, String value) {
        return url + "?" + key + "=" + value;
    }
}
