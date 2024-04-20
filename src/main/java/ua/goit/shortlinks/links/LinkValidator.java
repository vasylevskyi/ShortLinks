package ua.goit.shortlinks.links;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

public class LinkValidator {

    public static boolean isLinkValid(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }
}