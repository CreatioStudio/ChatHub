package vip.creatio.ChatBridge.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Net {
    private static final Net instance = new Net();

    private Net() {
    }

    public static Net getInstance() {
        return instance;
    }

    public HttpURLConnection getConnection(String url, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(1000);
        return connection;
    }

    public void post(String url, Map<String, String> headers, byte[] data) throws IOException {
        HttpURLConnection connection = getConnection(url, "POST");
        for (String key : headers.keySet()) {
            connection.setRequestProperty(key, headers.get(key));
        }
        connection.connect();
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.write(data);
        dataOutputStream.flush();
        dataOutputStream.close();
        connection.getInputStream();
    }

    public void postJSONRequest(String url, JSONObject data) throws IOException {
        Map<String,String > headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        post(url, headers, data.toString().getBytes());
    }

    public JSONObject parseData(InputStream inputStream) {
        StringBuilder dataString = new StringBuilder();
        try {
            Scanner scanner = new Scanner(inputStream);
            String temp = "";
            while (temp != null) {
                temp = scanner.nextLine();
                dataString.append(temp);
            }
        } catch (NoSuchElementException ignored) {
        }
        return JSON.parseObject(dataString.toString());
    }

    public void sendResponse(HttpExchange exchange, String response) throws IOException {
        JSONObject responseData = new JSONObject();
        responseData.put("message", response);
        response = responseData.toString();
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
