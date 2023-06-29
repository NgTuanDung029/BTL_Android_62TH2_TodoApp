package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.todoapp.Adapter.AlarmReceiver;
import com.example.todoapp.databinding.ActivityClockBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class ClockActivity extends AppCompatActivity {
    private ActivityClockBinding binding;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_clock);
        createNotificationChannel();

        binding.selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Chọn giờ")
                        .build();
                timePicker.show(getSupportFragmentManager(), "androidknowledge");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (timePicker.getHour() > 12){
                            binding.selectTime.setText(
                                    String.format("%02d",(timePicker.getHour()-12)) +":"+ String.format("%02d", timePicker.getMinute())+"PM"
                            );
                        } else {
                            binding.selectTime.setText(timePicker.getHour()+":" + timePicker.getMinute()+ "AM");
                        }
                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                        calendar.set(Calendar.MINUTE, timePicker.getMinute());
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                    }
                });
            }
        });
        binding.setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(ClockActivity.this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(ClockActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
                Toast.makeText(ClockActivity.this, "Đã đặt báo thức", Toast.LENGTH_SHORT).show();
            }
        });
        binding.cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClockActivity.this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(ClockActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                if (alarmManager == null){
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                }
                alarmManager.cancel(pendingIntent);
                Toast.makeText(ClockActivity.this, "Đã hủy", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(ClockActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });
    }


    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "toto";
            String desc = "Báo thức";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("todo", name, imp);
            channel.setDescription(desc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}