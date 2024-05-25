import com.sun.net.httpserver.HttpServer;

import handler.GreetHandler;
import handler.QuizHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {    
    public class Main {
        public static void main(String[] args) throws IOException {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/api/greet", new GreetHandler());
            server.createContext("/api/quiz", new QuizHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("Server started on port 8080");
        }
    }
}
