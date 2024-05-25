package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.DatabaseConfig;

public class QuizDao {

    // Fetch topics from the database
    public List<Map<String, Object>> fetchTopics() throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> topics = new ArrayList<>();
        String query = "SELECT DISTINCT topic FROM quizapp.questions";
      
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
      
            while (resultSet.next()) {
                Map<String, Object> topic = new HashMap<>();
                topic.put("topic", resultSet.getString("topic"));
                topics.add(topic);
            }
        }
        return topics;
    }

    // Fetch difficulties for a specific topic
    public List<String> fetchDifficulties(String topic) throws SQLException, ClassNotFoundException {
        List<String> difficulties = new ArrayList<>();
        String query = "SELECT DISTINCT difficulty FROM quizapp.questions WHERE topic = ?";
      
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
      
            statement.setString(1, topic);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    difficulties.add(resultSet.getString("difficulty"));
                }
            }
        }
        return difficulties;
    }

    // Fetch questions for a specific topic and difficulty
    public List<Map<String, Object>> fetchQuestions(String topic, String difficulty) throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> questions = new ArrayList<>();
        String query = "SELECT id, question, options, hint FROM quizapp.questions WHERE topic = ? AND difficulty = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, topic);
            statement.setString(2, difficulty);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> question = new HashMap<>();
                    question.put("id", resultSet.getInt("id"));
                    question.put("question", resultSet.getString("question"));
                    question.put("options", resultSet.getString("options")); // Store options as String for now
                    question.put("hint", resultSet.getString("hint"));
                    questions.add(question);
                }
            }
        }
        return questions;
    }

    // Fetch answers for a specific topic and difficulty
    public List<Map<String, Object>> fetchAnswers(String topic, String difficulty) throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> answers = new ArrayList<>();
        String query = "SELECT id, answer FROM quizapp.questions WHERE topic = ? AND difficulty = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, topic);
            statement.setString(2, difficulty);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> answer = new HashMap<>();
                    answer.put("id", resultSet.getInt("id"));
                    answer.put("answer", resultSet.getString("answer"));
                    answers.add(answer);
                }
            }
        }
        return answers;
    }
}
