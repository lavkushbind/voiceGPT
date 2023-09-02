package com.example.voicegpt;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

        private static final int RECORD_AUDIO_PERMISSION_CODE = 1;
        private TextView resultTextView;
        private SpeechRecognizer speechRecognizer;
        private boolean isTriggerDetected = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                resultTextView = findViewById(R.id.resultTextView);
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

                Button startButton = findViewById(R.id.startButton);
                startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                startVoiceRecognition();
                        }
                });

                // Check and request permission when the app starts
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.RECORD_AUDIO},
                                RECORD_AUDIO_PERMISSION_CODE);
                } else {
                        // Permission already granted, proceed with setup
                        setupVoiceRecognition();
                }
        }

        private void startVoiceRecognition() {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                speechRecognizer.startListening(intent);
        }

        private void processRecognizedText(ArrayList<String> results) {
                if (results != null && !results.isEmpty()) {
                        String recognizedText = results.get(0);

                        // Check if the trigger phrase is detected
                        if (!isTriggerDetected && recognizedText.toLowerCase().contains("hello abhishek")) {
                                isTriggerDetected = true;
                                Toast.makeText(this, "Trigger detected! Now recording.", Toast.LENGTH_SHORT).show();
                                return; // Don't process the trigger phrase as a command
                        }

                        // Process the recognized text
                        if (isTriggerDetected) {
                                // Handle the complete command after the trigger phrase
                                resultTextView.setText(recognizedText);
                        }
                }
        }

        private final RecognitionListener recognitionListener = new RecognitionListener() {
                @Override
                public void onResults(Bundle results) {
                        ArrayList<String> resultData = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        processRecognizedText(resultData);
                }

                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {
                        // Handle error during speech recognition
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                        // Handle partial recognition results
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                        // Handle speech recognition events
                }

                // Implement other callback methods as needed
        };

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                if (requestCode == RECORD_AUDIO_PERMISSION_CODE) {
                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                // Permission granted, proceed with setup
                                setupVoiceRecognition();
                        } else {
                                // Permission denied, handle this scenario
                                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                        }
                }
        }

        private void setupVoiceRecognition() {
                speechRecognizer.setRecognitionListener(recognitionListener);
        }
}
