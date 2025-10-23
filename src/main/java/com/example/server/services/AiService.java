package com.example.server.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiService {

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();

    private String callGemini(String prompt) {
        try {
            String requestBody = """
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "%s"
                        }
                      ]
                    }
                  ]
                }
                """.formatted(escapeJson(prompt));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent"))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", apiKey)
                    .timeout(Duration.ofSeconds(60)) // timeout increased
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Try up to 2 times if no text is returned
            for (int attempt = 1; attempt <= 2; attempt++) {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String result = parseResponse(response.body());
                if (!result.contains("No generated text")) {
                    return result;
                }
                Thread.sleep(2000); // wait 2 seconds before retry
            }

            return "{\"error\": \"No generated text returned after retries.\"}";

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"AI Error: " + e.getMessage() + "\"}";
        }
    }

    private String parseResponse(String responseBody) {
        try {
            JsonNode root = mapper.readTree(responseBody);
            JsonNode textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
            if (textNode.isMissingNode() || textNode.asText().isEmpty()) {
                return "{\"error\": \"No generated text found in Gemini response.\"}";
            }
            return textNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Error parsing Gemini response.\"}";
        }
    }

    private String escapeJson(String input) {
        return input.replace("\"", "\\\"").replace("\n", "\\n");
    }

    public String getExplanation(String code, String language) {
        String prompt = "Explain the following " + language + " code in simple terms, "
                + "line by line. Also include time and space complexity for each section:\n\n" + code;
        return callGemini(prompt);
    }

    public String fixCode(String code, String output, String language) {
        String prompt = "Fix the following " + language + " code based on this error. "
                + "Return only the corrected code:\nError:\n" + output + "\nCode:\n" + code;
        return callGemini(prompt);
    }

    public String optimizeCode(String code, String language) {
        String prompt = "Optimize the following " + language + " code for better performance and readability. "
                + "Explain the optimization and include time and space complexity before returning the optimized code:\n\n" + code;
        return callGemini(prompt);
    }
}
