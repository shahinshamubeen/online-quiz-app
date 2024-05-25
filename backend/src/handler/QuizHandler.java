package handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import service.QuizService;

public class QuizHandler implements HttpHandler {
    private final QuizService quizService = new QuizService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            handleCors(exchange);
            return;
        }

        String path = exchange.getRequestURI().getPath();
        if (path.equals("/api/quiz/topics")) {
            handleTopicsWithDifficulties(exchange);
        } else if (path.equals("/api/quiz/questions")) {
            handleQuestions(exchange);
        } else if (path.equals("/api/quiz/answers")){
            handleAnswers(exchange);
        } else {
            exchange.sendResponseHeaders(404, -1); // 404 Not Found
        }
    }

    // Handle GET request to fetch topics with difficulties
    private void handleTopicsWithDifficulties(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<JSONObject> topics = quizService.getTopics();
            String response = topics.toString(); // Convert the list of topics to a string
            setResponseHeaders(exchange, response);
        } else {
            exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
        }
    }

    // Handle POST request to fetch questions
    private void handleQuestions(HttpExchange exchange) throws IOException {
        try {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(requestBody);
                String topic = json.getString("topic");
                String difficulty = json.getString("difficulty"); // Corrected key
    
                List<JSONObject> questions = quizService.getQuestions(topic, difficulty);
                String response = questions.toString(); // Convert the list of topics to a string
    
                setResponseHeaders(exchange, response);
            } else {
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1); // 500 Internal Server Error
        }
    }

    // Handle POST request to fetch answers
    private void handleAnswers(HttpExchange exchange) throws IOException {
        try {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(requestBody);
                String topic = json.getString("topic");
                String difficulty = json.getString("difficulty");
    
                List<JSONObject> answers = quizService.getAnswers(topic, difficulty);
                String response = answers.toString(); // Convert the list of topics to a string
    
                setResponseHeaders(exchange, response);
            } else {
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1); // 500 Internal Server Error
        }
    }
    
    // Set CORS headers
    private void handleCors(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(204, -1); // 204 No Content
    }

    // Set response headers
    private void setResponseHeaders(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
