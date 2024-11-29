package com.mathquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StatsActivity extends AppCompatActivity {

    private TextView ratingValue;
    private TextView correctValue;
    private TextView completedValue;

    private int score;
    private int correct;
    private int total;
    private String completedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Инициализация элементов интерфейса
        ratingValue = findViewById(R.id.ratingValue);
        correctValue = findViewById(R.id.correctValue);
        completedValue = findViewById(R.id.completedValue);
        Button leaveButton = findViewById(R.id.leaveButton);
        Button newGameButton = findViewById(R.id.newGameButton);

        getIntentData();
        setStats();

        leaveButton.setOnClickListener(v -> {
            Intent intent = new Intent(StatsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        newGameButton.setOnClickListener(v -> {
            // Логика для начала новой игры
            startNewGame();
        });
    }

    // Метод для получения данных из Intent
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            score = intent.getIntExtra("score", 0); // 0 — значение по умолчанию, если данные не переданы
            correct = intent.getIntExtra("correctAnswers", 0);
            total = intent.getIntExtra("totalQuestions", 0);
            completedTime = intent.getStringExtra("timeCompleted");
        }
    }

    @SuppressLint("SetTextI18n")
    private void setStats() {
        ratingValue.setText(String.valueOf(score)); // Оценка
        correctValue.setText(correct + "/" + total); // Количество правильных/всего
        completedValue.setText(completedTime); // Время выполнения
    }

    private void startNewGame() {
        Intent intent = new Intent(StatsActivity.this, QuizActivity.class);
        intent.putExtra("isGame1", true);
        intent.putExtra("rating", score);
        startActivity(intent);
        finish();
    }
}
