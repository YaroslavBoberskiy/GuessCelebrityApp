package com.yboberskiy.guesscelebrityapp;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String QUERY_URL = "http://www.posh24.se/kandisar";

    private ImageView celebrityImage;
    private Button answer_A_Button;
    private Button answer_B_Button;
    private Button answer_C_Button;
    private Button answer_D_Button;

    private String content;
    private String[][] linksAndNames;

    private Random r = new Random();
    private int correctAnswer;
    ArrayList<Integer> randNums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celebrityImage = (ImageView) findViewById(R.id.celebrityImageView);
        answer_A_Button = (Button) findViewById(R.id.answer_A_Button);
        answer_B_Button = (Button) findViewById(R.id.answer_B_Button);
        answer_C_Button = (Button) findViewById(R.id.answer_C_Button);
        answer_D_Button = (Button) findViewById(R.id.answer_D_Button);

        answer_A_Button.setOnClickListener(this);
        answer_B_Button.setOnClickListener(this);
        answer_C_Button.setOnClickListener(this);
        answer_D_Button.setOnClickListener(this);

        getPhotoLinksAndCelebrityNames();
        setupQuizz();

    }

    public ArrayList<Integer> randomNumbersGenerator() {
        ArrayList<Integer> randomNumList = new ArrayList<Integer>();
        for (int i = 0; i < 4; ) {
            int rand = r.nextInt(100);
            if (!randomNumList.contains(rand)) {
                randomNumList.add(rand);
                i++;
            }
        }
        return randomNumList;
    }

    public void setupImage(String url) {
        DownloadBitmapTask task = new DownloadBitmapTask();
        try {
            Bitmap image = task.execute(url).get();
            celebrityImage.setImageBitmap(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupQuizz() {

        randNums = randomNumbersGenerator();

        answer_A_Button.setText(linksAndNames[randNums.get(0)][1]);
        answer_B_Button.setText(linksAndNames[randNums.get(1)][1]);
        answer_C_Button.setText(linksAndNames[randNums.get(2)][1]);
        answer_D_Button.setText(linksAndNames[randNums.get(3)][1]);

        correctAnswer = r.nextInt(4);

        setupImage(linksAndNames[randNums.get(correctAnswer)][0]);

    }

    public void getPhotoLinksAndCelebrityNames() {
        DownloadUrlContentTask task = new DownloadUrlContentTask();
        String urlContent = null;
        try {
            urlContent = task.execute(QUERY_URL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Pattern p = Pattern.compile("src=\"(.*?)\"/>");
        Matcher m = p.matcher(urlContent);

        linksAndNames = new String[100][];

        for (int i = 0; i < 100; i++) {
            m.find();
            linksAndNames[i] = m.group(1).split("\" alt=\"");
        }
    }

    public void checkAnswer(String answer) {
        if (linksAndNames[randNums.get(correctAnswer)][1].toLowerCase().
                equals(answer.toLowerCase())) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong! This was " +
                            linksAndNames[randNums.get(correctAnswer)][1],
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.answer_A_Button:
                checkAnswer(answer_A_Button.getText().toString());
                setupQuizz();
                break;
            case R.id.answer_B_Button:
                checkAnswer(answer_B_Button.getText().toString());
                setupQuizz();
                break;
            case R.id.answer_C_Button:
                checkAnswer(answer_C_Button.getText().toString());
                setupQuizz();
                break;
            case R.id.answer_D_Button:
                checkAnswer(answer_D_Button.getText().toString());
                setupQuizz();
                break;
        }
    }
}
