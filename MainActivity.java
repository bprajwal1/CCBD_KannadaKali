package com.example.kkccbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton fruit = findViewById(R.id.fruit);
        fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Display.class);
                String message = "1000";
                intent.putExtra(Util.EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

        ImageButton animal = findViewById(R.id.animal);
        animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Display.class);
                String message = "2000";
                intent.putExtra(Util.EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

        ImageButton bird = findViewById(R.id.bird);
        bird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Display.class);
                String message = "3000";
                intent.putExtra(Util.EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

        ImageButton flower = findViewById(R.id.flower);
        flower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Display.class);
                String message = "4000";
                intent.putExtra(Util.EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

        ImageButton colour = findViewById(R.id.colour);
        colour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Display.class);
                String message = "5000";
                intent.putExtra(Util.EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

        ImageButton quiz = findViewById(R.id.quiz);
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuizA.class);
                String message = "0 0";
                intent.putExtra(Util.EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });
    }
}
