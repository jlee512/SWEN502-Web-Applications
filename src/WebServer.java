import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class WebServer {

    public static void main(String[] args) throws IOException {
        // This creates a web server at http://localhost:8080/ that
        // waits for requests and passes them on to code to handle them.
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 10);
        // A "context" (in this library) is a prefix of the requested
        // path and an object to handle requests on that. We set up two
        // contexts by default.
        //
        server.createContext("/greeting", new GreetingHandler());
        // The second, StaticHandler, serves out regular files from a directory
        // when requested by name. We've asked it to serve the "web" directory
        // at http://localhost:8080/
        //The static handler class has been extracted and will be 'route' based rather than filename based
        //i.e. instead of requesting /welcome_page.html the server will respond to '/welcome' with welcome_page.html
        //Static handler will have predefined routes:
        //1) '/welcome' renders the welcome_page.html page to the browser
        server.createContext("/", new StaticHandler("web"));
        server.setExecutor(null);
        System.out.println("Starting server on http://localhost:8080/ ...");
        server.start();
    }

    // StaticHandler implements a traditional file-based web server.
    // It keeps track of a root directory to search in, and looks up
    // any files that are requested within that, then just sends them
    // as-is back to the web browser

    // GreetingHandler uses query-string parameters and a form to have the page
    // respond to the user. It's very similar to the HelloHandler except
    // for a patch in the middle that checks whether the user submitted the
    // form or not.
    static class GreetingHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            InputStream is = t.getRequestBody();
            byte[] body = WebServer.readStream(is);
            // Differences with HelloHandler start here.
            Headers headers = t.getResponseHeaders();
            headers.set("Content-type", "text/html; charset=utf-8");
            String response = "<html><head></head><body>";
            // We get the key=value parameters out of the query string:
            // ?user_name=Bob in the request will give us a map with the key
            // "user_name" holding "Bob".
            Map<String, String> params = WebServer.parseQuery(t.getRequestURI().getRawQuery());
            // We'll also try to get them from POST data if any
            if ("POST".equals(t.getRequestMethod())) {
                // Try to convert it to a string, and ignore otherwise.
                try {
                    // Posted forms have the same format as
                    // query strings, so we reuse the parsing.
                    String postData = new String(body, "UTF-8");
                    Map<String, String> postParams = WebServer.parseQuery(postData);
                    params.putAll(postParams);
                } catch (Exception ignored) {
                    // Bad!
                }
            }
            if (params.containsKey("user_name")) {
                response += "<b>Hello " + params.get("user_name") + "!</b><br />";
            }
            response += "<form method=\"post\" action=\"\">" +
                    "Name: <input type=\"text\" name=\"user_name\" />" +
                    "<input type=\"submit\" value=\"Submit\" /></form>";
            // Differences with HelloHandler end here.
            response += "</body></html>";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // Consume an entire input stream and return it as an array of
    // bytes.
    public static byte[] readStream(InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        int read = 0;
        while ((read = is.read(buf)) != -1) {
            out.write(buf, 0, read);
        }
        return out.toByteArray();
    }

    // Parses a query string into a map. The query string is the part of the
    // address after the "?", if any. Each parameter is separated from the
    // next with an ampersand (&) and the key is separated from the value
    // with an equals sign.
    public static Map<String, String> parseQuery(String query) {
        Map<String, String> res = new HashMap<String, String>();
        if (query == null)
            return res;
        String[] params = query.split("&");
        for (String param : params) {
            int index = param.indexOf("=");
            if (index == -1)
                continue;
            try {
                // Some characters get "URL-encoded" into %xx codes so that
                // they can pass through the network unimpeded.
                // We need to decode them to use them in our Java strings
                // as the real characters.
                res.put(URLDecoder.decode(param.substring(0, index), "UTF-8"),
                        URLDecoder.decode(param.substring(index + 1), "UTF-8"));
            } catch (UnsupportedEncodingException ignored) {}
        }
        return res;
    }

    // Report a particular HTTP error to the browser. For example,
    // error(404, "Not Found", t) or error(403, "Forbidden", t)
    // See <https://en.wikipedia.org/wiki/List_of_HTTP_status_codes>
    public static void error(int code, String desc, HttpExchange t) throws IOException {
        String response = code + " " + desc;
        t.sendResponseHeaders(code, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
