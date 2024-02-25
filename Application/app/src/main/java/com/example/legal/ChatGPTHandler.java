package com.example.legal;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatGPTHandler {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    public interface ChatGPTResponseListener {
        void onChatGPTResponse(String response);
        void onError(String error);
    }

    public static void sendMessage(String userPrompt, ChatGPTResponseListener listener) {
        new ChatGPTAsyncTask(listener).execute(userPrompt);
    }

    private static class ChatGPTAsyncTask extends AsyncTask<String, Void, String> {

        private final ChatGPTResponseListener listener;

        public ChatGPTAsyncTask(ChatGPTResponseListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", "Bearer " + API_KEY);

                String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}, {\"role\": \"user\", \"content\": \"" + params[0] + "\"}]}";

                urlConnection.setDoOutput(true);

                try (OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = requestBody.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = urlConnection.getResponseCode();

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()))) {
                        StringBuilder errorResponse = new StringBuilder();
                        String errorResponseLine;
                        while ((errorResponseLine = errorReader.readLine()) != null) {
                            errorResponse.append(errorResponseLine.trim());
                        }
                        Log.e("ChatGPT", "API Error Response: " + errorResponse.toString());
                        return "Error: " + errorResponse.toString();
                    }
                }

                try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonResponse = new JSONObject(result);
                JSONArray choicesArray = jsonResponse.getJSONArray("choices");
                if (choicesArray.length() > 0) {
                    JSONObject firstChoice = choicesArray.getJSONObject(0);
                    JSONObject assistantMessage = firstChoice.getJSONObject("message");
                    String assistantResponse = assistantMessage.getString("content");
                    listener.onChatGPTResponse(assistantResponse);
                } else {
                    listener.onError("No assistant response found");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                listener.onError("Error parsing JSON response");
            }
        }
    }
}
