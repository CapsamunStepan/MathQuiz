package com.mathquiz;

import android.content.Intent;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView questionTextView;
    private TextView timerTextView;
    private TextView questionNumberTextView; // Для отображения номера вопроса
    private RadioGroup optionsGroup;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private CountDownTimer timer;
    private static final int TIMER_DURATION = 20000; // 20 секунд
    private long quizStartTime; // Время начала викторины
    private int rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        rating = getIntent().getIntExtra("rating", 0);
        boolean isGame1 = getIntent().getBooleanExtra("isGame1", false);

        progressBar = findViewById(R.id.progressBar);
        questionTextView = findViewById(R.id.questionTextView);
        timerTextView = findViewById(R.id.timerTextView);
        questionNumberTextView = findViewById(R.id.questionNumberTextView); // Инициализация
        optionsGroup = findViewById(R.id.optionsGroup);
        Button nextButton = findViewById(R.id.nextButton);

        // Инициализируем список вопросов
        questionList = new ArrayList<>();
        if (!isGame1){
            initializeQuestions1();
        }
        else {
            initializeQuestions2();
        }



        // Фиксируем время начала викторины
        quizStartTime = System.currentTimeMillis();

        displayQuestion();

        nextButton.setOnClickListener(v -> {
            int selectedOptionId = optionsGroup.getCheckedRadioButtonId();
            if (selectedOptionId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedOptionId);
                int selectedAnswerIndex = optionsGroup.indexOfChild(selectedRadioButton);

                if (selectedAnswerIndex == questionList.get(currentQuestionIndex).getCorrectAnswerIndex()) {
                    score++;
                }

                // Переход к следующему вопросу или отображение результатов
                if (currentQuestionIndex < questionList.size() - 1) {
                    currentQuestionIndex++;
                    displayQuestion();
                } else {
                    showResults();
                }
            }
        });

        // Запуск таймера
        startTimer();
    }

    private void initializeQuestions1() {
        questionList.add(new Question("3 + 5 = ?", new String[]{"6", "7", "8", "9"}, 2));
        questionList.add(new Question("10 - 4 = ?", new String[]{"5", "3", "7", "6"}, 3));
        questionList.add(new Question("7 * 3 = ?", new String[]{"20", "21", "22", "23"}, 1));
        questionList.add(new Question("20 / 4 = ?", new String[]{"5", "9", "6", "7"}, 0));
        questionList.add(new Question("14 + 6 = ?", new String[]{"18", "2", "19", "20"}, 3));
        questionList.add(new Question("18 - 9 = ?", new String[]{"7", "9", "10", "11"}, 1));
        questionList.add(new Question("8 * 4 = ?", new String[]{"30", "32", "34", "36"}, 1));
        questionList.add(new Question("27 / 3 = ?", new String[]{"8", "11", "10", "9"}, 3));
        questionList.add(new Question("25 + 36 = ?", new String[]{"61", "62", "63", "64"}, 0));
        questionList.add(new Question("15 * 15 = ?", new String[]{"210", "225", "240", "250"}, 1));
    }

    private void initializeQuestions2() {
        questionList.add(new Question("Что такое квадратный корень из 16?", new String[]{"2", "4", "6", "8"}, 1));
        questionList.add(new Question("Сколько углов в треугольнике?", new String[]{"2", "3", "4", "5"}, 1));
        questionList.add(new Question("Какое число является делителем числа 12?", new String[]{"2", "5", "7", "9"}, 0));
        questionList.add(new Question("Что такое произведение 7 и 9?", new String[]{"56", "63", "72", "81"}, 1));
        questionList.add(new Question("Сколько метров в одном километре?", new String[]{"100", "500", "1000", "1500"}, 2));
        questionList.add(new Question("Какое число является простым?", new String[]{"4", "6", "7", "9"}, 2));
        questionList.add(new Question("Сколько дней в феврале в невисокосный год?", new String[]{"28", "29", "30", "31"}, 0));
        questionList.add(new Question("Какое значение имеет Пи (π) в математике?", new String[]{"2.14", "3.14", "3.16", "3.18"}, 1));
        questionList.add(new Question("Что такое квадрат числа 5?", new String[]{"10", "15", "20", "25"}, 3));
        questionList.add(new Question("Сколько градусов в круге?", new String[]{"360", "180", "90", "270"}, 0));
    }


    @SuppressLint("SetTextI18n")
    private void displayQuestion() {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        questionTextView.setText(currentQuestion.getQuestionText());

        // Отображаем варианты ответов
        optionsGroup.clearCheck();
        for (int i = 0; i < optionsGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) optionsGroup.getChildAt(i);
            radioButton.setText(currentQuestion.getOptions()[i]);
        }

        // Обновляем текст с номером вопроса
        questionNumberTextView.setText("Question " + (currentQuestionIndex + 1) + " of " + questionList.size());

        // Перезапуск таймера
        startTimer();
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }

        progressBar.setMax(TIMER_DURATION / 1000);
        progressBar.setProgress(TIMER_DURATION / 1000);

        timer = new CountDownTimer(TIMER_DURATION, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                timerTextView.setText(secondsRemaining + "s");

                // Обновляем прогресс бар
                progressBar.setProgress(secondsRemaining);
            }

            @Override
            public void onFinish() {
                // Таймер завершился, переходим к следующему вопросу
                if (currentQuestionIndex < questionList.size() - 1) {
                    currentQuestionIndex++;
                    displayQuestion();
                } else {
                    showResults();
                }
            }
        };
        timer.start();
    }

    @SuppressLint("SetTextI18n")
    private void showResults() {
        // Переходим к экрану со статистикой
        Intent intent = new Intent(QuizActivity.this, StatsActivity.class);
        intent.putExtra("score", score + rating);
        intent.putExtra("totalQuestions", questionList.size());
        intent.putExtra("correctAnswers", calculateCorrectAnswers());
        intent.putExtra("timeCompleted", getTotalTimeTaken()); // Если вы хотите передать время
        startActivity(intent);
        finish(); // Завершаем текущую активность, чтобы пользователь не мог вернуться назад
    }

    // Метод для подсчета правильных ответов
    private int calculateCorrectAnswers() {
        return score; // Возвращаем количество правильных ответов (score уже хранит этот результат)
    }

    // Метод для получения общего времени, потраченного на викторину
    @SuppressLint("DefaultLocale")
    private String getTotalTimeTaken() {
        long quizEndTime = System.currentTimeMillis();
        long totalTimeMillis = quizEndTime - quizStartTime; // Разница между временем окончания и началом

        // Преобразование времени из миллисекунд в секунды
        int totalTimeSeconds = (int) (totalTimeMillis / 1000);

        // Преобразование секунд в формат MM:SS
        int minutes = totalTimeSeconds / 60;
        int seconds = totalTimeSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel(); // Останавливаем таймер
        }
    }
}
