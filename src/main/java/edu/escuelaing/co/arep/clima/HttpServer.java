package edu.escuelaing.co.arep.clima;

import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import java.io.*;

public class HttpServer {
    private static final HttpServer _instance = new HttpServer();
    private static final Integer PORT = getPort();
    private static String city = "London";
    private static final String URL = getURL(city);
    private static final HashMap<String, String> contentType = new HashMap<String, String>();

    public static HttpServer getInstance() {
        return _instance;
    }

    private HttpServer() {
    }

    private static String getURL(String city) {
        String URL = "https://api.openweathermap.org/data/2.5/weather?q=" + city
                + "&appid=a7206c37efcfd0f2bc2479da4621a938";
        return URL;
    }

    public void startServer(String[] args) throws IOException, URISyntaxException {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35001);
        } catch (Exception e) {
            System.err.println("Could not listen on port: " + PORT + ". ");
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (Exception e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            try {
                manageConnection(clientSocket);
            } catch (URISyntaxException e) {
                System.err.println("URI incorrect.");
                System.exit(1);
            }
        }
        serverSocket.close();
    }

    public void manageConnection(Socket clientSocket) throws IOException, URISyntaxException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        ArrayList<String> request = new ArrayList<String>();
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            request.add(inputLine);
            if (!in.ready()) {
                break;
            }
        }

        String uriContentType = "";
        String uri = "";
        uriContentType = request.get(0).split(" ")[1];
        URI resource = new URI(uriContentType);
        uri = resource.getPath().split("/")[1];
        try {
            outputLine = getComponentResource(uri, resource);
            out.println(outputLine);
        } catch (Exception e) {
            System.out.println(e);
            outputLine = default404HTMLResponse();
            out.println(outputLine);
        }

        out.close();
        in.close();
        clientSocket.close();
    }

    private String getComponentResource(String uri, URI urir) throws IOException {
        if (uri.contains("clima")) {
            return defaultHttpMessage();
        } else if (uri.contains("consulta")) {
            return computeContentComponentResponse(uri, urir);
        } else {
            return default404HTMLResponse();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private String computeContentComponentResponse(String uri, URI urir) throws MalformedURLException, IOException {
        String ciudad = urir.getQuery().split("=")[1];
        String url = getURL(ciudad);
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            String outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type:application/json\r\n" + "\r\n" + json;
            return outputLine;
        } finally {
            is.close();
        }

    }

    private String default404HTMLResponse() {
        String outputLine = "HTTP/1.1 404 Not found\r\n" + "Content-Type: text/html\r\n" + "\r\n" + "<!DOCTYPE html>"
                + "<html>" + " <head>" + "     <title>404 Not Found </title>" + "     <meta charset=\"UTF-8\""
                + "     <meta name=\"viewport\"" + " </head>" + "<body>" + "     <div><h1>Error 404</h1></div>"
                + " </body>" + "</html>";
        return outputLine;
    }

    private String defaultHttpMessage() {
        String outputLine = "HTTP/1.1 200 ok\r\n" + "Content-Type: text/html\r\n" + "\r\n" + "<!DOCTYPE html>"
                + " <html>" + "     <head>" + "         <title> Wheather Page </title>"
                + "         <meta charset=\"UTF-8\""
                + "         <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"" + "     </head>"
                + "     <body>" + "         <div><h1>The Weather</h1></div>"
                + "<p>Esta página es para consultar el clima en una ciudad. Ingresar en la barra la ciudad a buscar. De esta manera consulta?lugar={ciudad o lugar}. No incluir los corchetes<p>"
                + "</html>";
        return outputLine;
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35001; // returns default port if heroku-port isn't set (i.e. on localhost)
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        try {
            HttpServer.getInstance().startServer(args);
        } catch (IOException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
