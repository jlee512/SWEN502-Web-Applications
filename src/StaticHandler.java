import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Created by Julian on 26/10/2017.
 */
public class StaticHandler implements HttpHandler {

    protected boolean verbose = true;
    protected File root;

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
        System.out.println(filename);

        // ------------------------ Routes are added here ------------------------------
        //Route (1) /welcome (i.e. index or landing page)
        //   /welcome
        if (filename.equals("")) {
            filename = "welcome_page.html";

        //Route (2) /home (i.e. the main page)
        //   /home
        } else if (filename.equals("home")) {
            filename = "home_page.html";
        } else if (filename.equals("add_project")) {
            filename = "add_project.html";
        } else if (filename.equals("about_me")) {
            filename = "about_me.html";
        } else {

        }

        // ------------------------ End of route addition -------------------------------

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
    protected void serveFile(File file, HttpExchange t) throws IOException {
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
    protected void serveDirectory(File dir, HttpExchange t) throws IOException {
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
    protected String getMimeType(String name) {
        if (name.endsWith(".txt"))  return "text/plain";
        if (name.endsWith(".html")) return "text/html; charset=utf-8";
        if (name.endsWith(".htm"))  return "text/html; charset=utf-8";
        if (name.endsWith(".css"))  return "text/css";
        if (name.endsWith(".js"))   return "text/javascript";
        if (name.endsWith(".png"))  return "image/png";
        if (name.endsWith(".jpg"))  return "image/jpeg";
        if (name.endsWith(".jpeg")) return "image/jpeg";
        //Added .svg file extension
        if (name.endsWith(".svg")) return "image/svg+xml";
        if (name.endsWith(".java")) return "text/plain";
        if (name.endsWith(".woff")) return "font/woff";
        if (name.endsWith(".json")) return "application/json";
        return "application/octet-stream";
    }

    protected void log(String message) {
        if (verbose)
            System.out.println(message);
    }
}
