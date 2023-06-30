package com.example.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.core.content.ContextCompat;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";

    private EditText newTaskText, newStartTimeText, newEndTimeText;
    private Button newTaskSaveButton;
    private DatabaseHandler db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        newTaskText = getView().findViewById(R.id.newTaskText);
        TimePicker startTimePicker = getView().findViewById(R.id.startTimePicker);
        TimePicker endTimePicker = getView().findViewById(R.id.endTimePicker);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle =getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            String startTime = bundle.getString("startTime");
            String endTime = bundle.getString("endTime");
            newTaskText.setText(task);
            setTimePicker(startTimePicker, startTime);
            setTimePicker(endTimePicker, endTime);
            if (task.length()>0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                String startTime = getTimePickerTime(startTimePicker);
                String endTime = getTimePickerTime(endTimePicker);
                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"), text, startTime, endTime);
                }
                else {
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    task.setStartTime(startTime);
                    task.setEndTime(endTime);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }
    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
    private void setTimePicker(TimePicker timePicker, String timeString) {
        int hourOfDay = 0;
        int minute = 0;
        if (timeString != null && !timeString.isEmpty()) {
            String[] timeParts = timeString.split(":");
            if (timeParts.length == 2) {
                hourOfDay = Integer.parseInt(timeParts[0]);
                minute = Integer.parseInt(timeParts[1]);
            }
        }
        timePicker.setHour(hourOfDay);
        timePicker.setMinute(minute);
    }
    private String getTimePickerTime(TimePicker timePicker) {
        int hourOfDay = timePicker.getHour();
        int minute = timePicker.getMinute();
        return String.format("%02d:%02d", hourOfDay, minute);
    }
}
