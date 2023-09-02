package com.example.voicegpt;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

public class MainActivity2 extends AppCompatActivity{


    private EditText userInputEditText;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        userInputEditText = findViewById(R.id.userInputEditText);
        client = new OkHttpClient();
    }

    public void onSendMessageClick(View view) {
        String userMessage = userInputEditText.getText().toString();
        sendToGPT3(userMessage);
    }

    private void sendToGPT3(String userMessage) {
        String apiKey = "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"; // Replace with your actual API key

        String url = "https://api.openai.com/v1/engines/davinci-codex/completions";
        RequestBody requestBody = createRequestBody(userMessage);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                // Process and display the response in the chat UI
            }
        });
    }

    private RequestBody createRequestBody(String userMessage) {
        // Create the request body based on the user's message
        return null;
    }
}
