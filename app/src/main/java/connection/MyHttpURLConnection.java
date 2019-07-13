package connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by DigitalNet on 9/4/2016.
 */
public class MyHttpURLConnection {

    public String sendGet(String url, String urlParameters) throws Exception {

        //String url = "http://www.emessa.website/sud/api/view/real_estate";
        url += urlParameters;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("GET");
        con.setRequestProperty("charset" , "utf-8");


        //String urlParameters = "{\"id\":NULL}";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
        writer.write(urlParameters);
        writer.close();
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("louai Sending 'GET' request to URL : " + url);
        System.out.println("louai Post parameters : " + urlParameters);
        System.out.println("louai Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        return response.toString();

    }

    public String sendGet(String base_url) throws Exception {

        String url = base_url;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

/*        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);*/

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
        return response.toString();
    }

    public String sendPost(String url, String urlParameters) throws Exception {

        //String url = "http://www.emessa.website/sud/api/view/real_estate";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("charset" , "utf-8");
        con.setRequestProperty("Content-Type", "application/json; utf-8");


        //String urlParameters = "{\"id\":NULL}";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
        writer.write(urlParameters);
        writer.close();
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("louai Sending 'POST' request to URL : " + url);
        System.out.println("louai Post parameters : " + urlParameters);
        System.out.println("louai Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        return response.toString();

    }
}
