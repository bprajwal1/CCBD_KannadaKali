package com.example.kkccbd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizB extends AppCompatActivity {

    String quizStatus = null;
    Integer quizNumber;
    Integer quizScore;
    String correct;
    Context context;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizb);

        Intent intent = getIntent();
        quizStatus = intent.getStringExtra(Util.EXTRA_MESSAGE);

        String quizStuff[]= quizStatus.split(" ");

        quizNumber = Integer.parseInt(quizStuff[0]);
        quizScore = Integer.parseInt(quizStuff[1]);

        TextView score = (TextView)findViewById(R.id.quizBScore);
        score.setText("Score: " + String.valueOf(quizScore) + "/" + String.valueOf(quizNumber));

        final List<String> choices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            choices.add(randomNum());
        }
        correct = choices.get(0);
        Collections.shuffle(choices);

        ImageView mainImage = this.findViewById(R.id.quizBMain);
        context = mainImage.getContext();
        id = context.getResources().getIdentifier("text_" + correct, "mipmap", context.getPackageName());
        mainImage.setImageResource(id);

        ImageView imageA = this.findViewById(R.id.quizBImageA);
        context = imageA.getContext();
        id = context.getResources().getIdentifier("image_" + choices.get(0), "mipmap", context.getPackageName());
        imageA.setImageResource(id);

        ImageView imageB = this.findViewById(R.id.quizBImageB);
        context = imageB.getContext();
        id = context.getResources().getIdentifier("image_" + choices.get(1), "mipmap", context.getPackageName());
        imageB.setImageResource(id);

        ImageView imageC = this.findViewById(R.id.quizBImageC);
        context = imageC.getContext();
        id = context.getResources().getIdentifier("image_" + choices.get(2), "mipmap", context.getPackageName());
        imageC.setImageResource(id);

        ImageView imageD = this.findViewById(R.id.quizBImageD);
        context = imageD.getContext();
        id = context.getResources().getIdentifier("image_" + choices.get(3), "mipmap", context.getPackageName());
        imageD.setImageResource(id);

        imageA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correct.equals(choices.get(0)))
                {
                    Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                    goNext(1);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                    goNext(0);
                }
            }
        });

        imageB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correct.equals(choices.get(1)))
                {
                    Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                    goNext(1);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                    goNext(0);
                }
            }
        });

        imageC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correct.equals(choices.get(2)))
                {
                    Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                    goNext(1);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                    goNext(0);
                }
            }
        });

        imageD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correct.equals(choices.get(3)))
                {
                    Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                    goNext(1);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                    goNext(0);
                }
            }
        });

        ImageView home = (ImageView) findViewById(R.id.quizBHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    String randomNum()
    {
        Random rand = new Random();
        int num1 = rand.nextInt(5) + 1;
        int num2 = rand.nextInt(10);

        return String.valueOf(num1 * 1000 + num2);
    }

    void goNext(int i)
    {
        Intent intent;
        Random rand = new Random();
        if(rand.nextInt(2) == 0) {
            intent = new Intent(getApplicationContext(), QuizB.class);
        }
        else {
            intent = new Intent(getApplicationContext(), QuizA.class);
        }
        if(i == 1)
        {
            quizScore += 1;
        }
        quizNumber += 1;
        String message = quizNumber + " " + quizScore;
        intent.putExtra(Util.EXTRA_MESSAGE, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
