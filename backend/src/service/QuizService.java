package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.QuizDao;

public class QuizService {
    private final QuizDao quizDao = new QuizDao();

    public List<JSONObject> getTopics() {
        List<JSONObject> topics;
        try {
            List<Map<String, Object>> topicData = quizDao.fetchTopics();
            topics = convertTopicsToJson(topicData);
        } catch (SQLException | ClassNotFoundException e) {
            // Handle exception gracefully, return empty list
            e.printStackTrace();
            return new ArrayList<>();
        }
        return topics;
    }
      
    // Convert topics to JSON format
    private List<JSONObject> convertTopicsToJson(List<Map<String, Object>> topics) throws SQLException, ClassNotFoundException {
        List<JSONObject> convertedTopics = new ArrayList<>();
        for (Map<String, Object> topicMap : topics) {
            String topicName = (String) topicMap.get("topic");
            JSONObject topicObj = new JSONObject();
            topicObj.put("topic", topicName);
      
            // Fetch difficulties for this topic
            List<String> difficulties = quizDao.fetchDifficulties(topicName);
            JSONArray difficultiesArray = new JSONArray(difficulties);
            topicObj.put("difficulties", difficultiesArray);
      
            convertedTopics.add(topicObj);
        }
        return convertedTopics;
    }

    // Retrieve questions based on topic and difficulty
    public List<JSONObject> getQuestions(String topic, String difficulty) {
        List<JSONObject> questions;
        try {
            questions = convertQuestionsToJson(quizDao.fetchQuestions(topic, difficulty));
        } catch (SQLException | ClassNotFoundException e) {
            // Handle exception gracefully, return empty list
            e.printStackTrace();
            return new ArrayList<>();
        }
        return questions;
    }

    // Convert questions to JSON format
    private List<JSONObject> convertQuestionsToJson(List<Map<String, Object>> questions) {
        List<JSONObject> convertedQuestions = new ArrayList<>();
        for (Map<String, Object> questionMap : questions) {
            JSONObject question = new JSONObject();
            question.put("id", questionMap.get("id"));
            question.put("question", questionMap.get("question"));

            // Parse options from String to JSONArray
            String optionsString = (String) questionMap.get("options");
            if (optionsString != null) {
                JSONArray optionsArray = new JSONArray(optionsString);
                question.put("options", optionsArray);
            }

            question.put("hint", questionMap.get("hint"));
            convertedQuestions.add(question);
        }
        return convertedQuestions;
    }

    // Retrieve answers based on topic and difficulty
    public List<JSONObject> getAnswers(String topic, String difficulty) {
        List<JSONObject> answers;
        try {
            answers = convertAnswersToJson(quizDao.fetchAnswers(topic, difficulty));
        } catch (SQLException | ClassNotFoundException e) {
            // Handle exception gracefully, return empty list
            e.printStackTrace();
            return new ArrayList<>();
        }
        return answers;
    }

    // Convert answers to JSON format
    private List<JSONObject> convertAnswersToJson(List<Map<String, Object>> answers) {
        List<JSONObject> convertedAnswers = new ArrayList<>();
        for (Map<String, Object> answerMap : answers) {
            JSONObject answer = new JSONObject();
            answer.put("id", answerMap.get("id"));
            answer.put("answer", answerMap.get("answer"));
            convertedAnswers.add(answer);
        }
        return convertedAnswers;
    }
}
