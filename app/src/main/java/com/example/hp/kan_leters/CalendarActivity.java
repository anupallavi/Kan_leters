package com.example.hp.kan_leters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

/**
 * Created by Hp on 5/17/2017.
 */

public class CalendarActivity extends AppCompatActivity {

    ContentDatabase myDB;
    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_view);
        myDB = new ContentDatabase(this);

        calendar = (CalendarView) findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String content = myDB.sendDate(dayOfMonth, month, year);
                Intent intent = new Intent(CalendarActivity.this, DiaryContent.class);
                intent.putExtra("Date", dayOfMonth + "/" + month + "/"+ year);
                intent.putExtra("Content", content);
                startActivity(intent);
            }


        });


    }
}