package com.example.thefinalexam;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class QuestionActivity extends AppCompatActivity {

    ArrayList<Actress> ACTRESS_ITEMS_FOR_TEST = new ArrayList<>();
    int randomNum;
    private Button confirmButton;
    private TextView Question;
    private RadioButton yesButton;
    private RadioButton noButton;
    private String questionName;
    private String questionCup;
    private TextView dateText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Uri ActressUri = Uri.parse("content://com.example.thefinalexam.ActressProvider/actress");
//        ContentValues contentValues = new ContentValues();
        Cursor ActressCursor = getContentResolver().query(ActressUri, new String[]{"_id", "ActressName", "ActressCup", "ActressAge", "ActressHeight", "PosterUrl"}, null, null, null);
            confirmButton = (Button) findViewById(R.id.confirmButton);
            Question = (TextView) findViewById(R.id.questionView);
            yesButton = (RadioButton) findViewById(R.id.yesButton);
            noButton = (RadioButton) findViewById(R.id.noButton);
            ACTRESS_ITEMS_FOR_TEST.clear();

            if (ActressCursor != null) {
                while (ActressCursor.moveToNext()) {
                    ACTRESS_ITEMS_FOR_TEST.add(new Actress(ActressCursor.getString(ActressCursor.getColumnIndex("_id")), ActressCursor.getString(ActressCursor.getColumnIndex("ActressName")), ActressCursor.getString(ActressCursor.getColumnIndex("ActressCup")), ActressCursor.getString(ActressCursor.getColumnIndex("ActressHeight")), ActressCursor.getString(ActressCursor.getColumnIndex("ActressAge")), ActressCursor.getString(ActressCursor.getColumnIndex("PosterUrl"))));
                }
                ActressCursor.close();
            }
            final int randomNumForName = getRandom();
            final int randomNumForCup = getRandom();
            questionName = ACTRESS_ITEMS_FOR_TEST.get(randomNumForName).getName();
            questionCup = ACTRESS_ITEMS_FOR_TEST.get(randomNumForCup).getCup();
            Question.setText("請問" + questionName + "的罩杯是" + questionCup + "嗎?");

            confirmButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (yesButton.isChecked()) {
                        if (ACTRESS_ITEMS_FOR_TEST.get(randomNumForName).getCup() == ACTRESS_ITEMS_FOR_TEST.get(randomNumForCup).getCup()) {
                            Toast.makeText(QuestionActivity.this, "答對了!好棒", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(QuestionActivity.this, "答錯了..QQ", Toast.LENGTH_LONG).show();
                        }
                    } else if (noButton.isChecked()) {
                        if (ACTRESS_ITEMS_FOR_TEST.get(randomNumForName).getCup() != ACTRESS_ITEMS_FOR_TEST.get(randomNumForCup).getCup()) {
                            Toast.makeText(QuestionActivity.this, "答對了!好棒", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(QuestionActivity.this, "答錯了..QQ", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(QuestionActivity.this, "請選擇是或否!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                new TimePickerDialog(QuestionActivity.this, new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+8:00"));
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);
                        cal.set(Calendar.SECOND,0);
                        MainActivity.add_alarm(QuestionActivity.this, cal);
                        Toast.makeText(QuestionActivity.this, "提醒已設定在 "+String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":" + cal.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(QuestionActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, hour, minute, true).show();
            }
        });
    }

        public int getRandom () {
            randomNum = (int) (Math.random() * ACTRESS_ITEMS_FOR_TEST.size());
            return randomNum;
        }
    }

