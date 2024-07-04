package com.sladejr.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewScore;
    private TextView textViewTimer;
    private TextView textViewQuestion;
    private TextView textViewAns0;
    private TextView textViewAns1;
    private TextView textViewAns2;
    private TextView textViewAns3;

    private ArrayList<TextView> options = new ArrayList<>();

    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int min = 5;
    private int max = 30;

    private int countOfQuestions = 0;
    private int countOfRightAnswers = 0;
    private boolean gameOver = false;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewScore = findViewById(R.id.textViewScore);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewTimer = findViewById(R.id.textViewTime);


        button = findViewById(R.id.button);


        textViewAns0 = findViewById(R.id.textViewAns0);
        textViewAns1 = findViewById(R.id.textViewAns1);
        textViewAns2 = findViewById(R.id.textViewAns2);
        textViewAns3 = findViewById(R.id.textViewAns3);
        // <options> massiviga TextView tipidagi ma'lumotlarni(javob variantlari) qo'shamiz
        options.add(textViewAns0);
        options.add(textViewAns1);
        options.add(textViewAns2);
        options.add(textViewAns3);
        playNext();

        button.setOnClickListener(this::onClickAnswer);

        CountDownTimer timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long l) {
                textViewTimer.setText(getTime(l));
                if(l<6000) {
                    textViewTimer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                }
            }

            @Override
            public void onFinish() {
                gameOver = true;
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = sharedPreferences.getInt("max", 0);
                if(countOfRightAnswers>=max){
                    sharedPreferences.edit().putInt("max", countOfRightAnswers).apply();
                }
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                intent.putExtra("result", countOfRightAnswers);
                startActivity(intent);
            }
        };
        timer.start();
    }

    private void playNext(){
        generateQuestion();
        //variantlarni joylashtiramiz
        for(int i = 0; i < options.size(); i++ ){
            if(i == rightAnswerPosition) options.get(i).setText(String.valueOf(rightAnswer));
            else options.get(i).setText(String.valueOf(generateWrongAnswer()));
        }
        String score = String.format("%s / %s", countOfRightAnswers, countOfQuestions);
        textViewScore.setText(score);
    }

    private void generateQuestion(){
        int a = (int) (Math.random()*(max-min+1)+min);
        int b = (int) (Math.random()*(max-min+1)+min);
        int mark = (int)(Math.random()*2);
        isPositive = mark==1;

        if(isPositive) {
            rightAnswer = a+b;
            question = String.format("%s + %s", a, b);
        }
        else {
            rightAnswer = a - b;
            question = String.format("%s - %s", a, b);
        }
        textViewQuestion.setText(question);
        rightAnswerPosition = (int) (Math.random()*4);
    }

    private int generateWrongAnswer(){
        int result;
     do {
         result = (int) (Math.random()*max*2 +1) - (max-min);
     } while(result == rightAnswer);
     return result;
    }

    public void onClickAnswer(View view) {
        if(!gameOver) {
            TextView textView = (TextView) view;
            int chosenAnswer = Integer.parseInt(textView.getText().toString());
            if (chosenAnswer == rightAnswer) {
                Toast.makeText(this, "To`g`ri!", Toast.LENGTH_SHORT).show();
                countOfRightAnswers++;
            } else {
                Toast.makeText(this, "Xato", Toast.LENGTH_SHORT).show();
            }
            countOfQuestions++;
            playNext();
        }
    }

    private String getTime(long millis){
        int seconds = (int) (millis/1000);
        int minutes = seconds/60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

}