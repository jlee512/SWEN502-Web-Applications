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
        // The first is just a "hello world" context, that does the bare
        // minimum to serve out the string "Hello, world!" at
        // http://localhost:8080/hello
        server.createContext("/hello", new HelloHandler());
        server.createContext("/greeting", new GreetingHandler());
        // The second, StaticHandler, serves out regular files from a directory
        // when requested by name. We've asked it to serve the "web" directory
        // at http://localhost:8080/
        server.createContext("/", new StaticHandler("web"));
        server.setExecutor(null);
        System.out.println("Starting server on http://localhost:8080/ ...");
        server.start();
    }

    // HelloHandler is a minimal handler that just sends "Hello, world!"
    // as the response to every request. It shows the necessary steps for 
    // custom handlers to work.
    static class HelloHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            InputStream is = t.getRequestBody();
            byte[] body = WebServer.readStream(is);
            String response = "Hello, world!";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // StaticHandler implements a traditional file-based web server.
    // It keeps track of a root directory to search in, and looks up
    // any files that are requested within that, then just sends them
    // as-is back to the web browser.
    static class StaticHandler implements HttpHandler {

        private boolean verbose = true;
        private File root;

        public StaticHandler(String path) {
            root = new File(path);
        }

        public void handle(HttpExchange t) throws IOException {
            log(t.getRequestMethod() + " " + t.getRequestURI());
            InputStream is = t.getRequestBody();
            // If we don't read the whole body, things can break, but we're
            // not using it at the moment.
            byte[] body = WebServer.readStream(is);
            // We want to take the request path and strip out the context's
            // prefix set up earlier, and any extra / characters, so that
            // we can use it as a filename.
            String prefix = t.getHttpContext().getPath();
            String filename = t.getRequestURI().getPath().substring(prefix.length());

            while (filename.startsWith("/"))
                filename = filename.substring(1);
            // Create a new File object representing this file inside
            // that root, and check that it exists.
            File file = new File(root, filename);
            if (!file.exists() || (filename.indexOf("..") != -1)) {
                WebServer.error(404, "Not Found", t);
                return;
            }


            if (file.isDirectory()) {
                serveDirectory(file, t);
            } else {
                serveFile(file, t);
            }
        }

        // Serves out an individual file as read from the disk to the
        // browser, assuming that it exists.
        private void serveFile(File file, HttpExchange t) throws IOException {
            Headers headers = t.getResponseHeaders();
            headers.set("Content-type", getMimeType(file.getName()));
            log("--> serving file as " + getMimeType(file.getName()));
            byte[] response = Files.readAllBytes(file.toPath());
            t.sendResponseHeaders(200, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }

        // Serves out directory dir as an HTML page with links to
        // each file in the directory.
        private void serveDirectory(File dir, HttpExchange t) throws IOException {
            log("--> serving directory");
            File index = new File(dir, "index.html");
            if (index.exists()) {
                log("--> found index");
                serveFile(index, t);
                return;
            }
            Headers headers = t.getResponseHeaders();
            headers.set("Content-type", "text/html; charset=utf-8");
            File[] files = dir.listFiles();
            String response = "<html><head></head><body>"
            		+ "<h1>Directory " + dir.getName() + "</h1>"
            		+ "<ul>";
            for (File f : files) {
            	String n = f.getName() + (f.isDirectory() ? "/" : "");
                response += "<li><a href=\"" + n + "\">" + n + "</a></li>";
            }
            response += "</ul></body></html>";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        // Pick a suitable content-type header for a file based on its
        // extension. This list is incomplete and you might find you
        // need to extend it.
        // See <https://en.wikipedia.org/wiki/Media_type>
        private String getMimeType(String name) {
            if (name.endsWith(".txt"))  return "text/plain";
            if (name.endsWith(".html")) return "text/html; charset=utf-8";
            if (name.endsWith(".htm"))  return "text/html; charset=utf-8";
            if (name.endsWith(".css"))  return "text/css";
            if (name.endsWith(".js"))   return "text/javascript";
            if (name.endsWith(".png"))  return "image/png";
            if (name.endsWith(".jpg"))  return "image/jpeg";
            if (name.endsWith(".jpeg")) return "image/jpeg";
            if (name.endsWith(".java")) return "text/plain";
            if (name.endsWith(".woff")) return "font/woff";
            if (name.endsWith(".json")) return "application/json";
            return "application/octet-stream";
        }

        private void log(String message) {
            if (verbose)
                System.out.println(message);
        }
    }

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
