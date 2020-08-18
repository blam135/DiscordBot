package api;

import org.apache.http.HttpException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RESTApi {
    public static String GET(String url) {
        try {
            URL urlForGetRequest = new URL(url);
            String readLine = null;
            HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();
                while ((readLine = in .readLine()) != null) {
                    response.append(readLine);
                }
                in.close();
                return response.toString();
            } else {
                throw new HttpException();
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            return "MalformedURLException";
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        } catch (HttpException he) {
            return "HttpException";
        }
    }
}
