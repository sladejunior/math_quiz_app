package com.sladejr.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

Button buttonStart;
TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        buttonStart = findViewById(R.id.buttonStart);
        textViewResult = findViewById(R.id.textViewResult);

        Intent intent = getIntent();
        if(intent!= null && intent.hasExtra("result")){
            int result = intent.getIntExtra("result",0);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            int max = sharedPreferences.getInt("max",0);
            String score = String.format("Sizning natijangiz: %s \n Eng yuqori natija: %s",result, max);
            textViewResult.setText(score);
        }

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newGame = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(newGame);
            }
        });
    }
}