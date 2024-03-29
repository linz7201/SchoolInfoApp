package com.nickli.scheduleapp.schedule;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nickli.scheduleapp.R;

import java.util.Locale;

public class ScheduleAdd extends AppCompatActivity {

    // Define variables
    Button classTime;
    Button btnAddClass;
    TextView tvCancel;

    FirebaseAuth auth;
    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;

    TextInputEditText classPeriod;
    TextInputEditText className;
    TextInputEditText classDay;

    int hour, minutes;
    private String class_Time;
    private Uri mCurrentReminderUri;

    @Override
    // When activity is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sets frontend layout and all the viewIDs
        setContentView(R.layout.actvitity_schedule_add);

        classTime = findViewById(R.id.btn_classTime);
        btnAddClass = findViewById(R.id.btnAddClass);
        tvCancel = findViewById(R.id.tv_cancel);

        classPeriod = findViewById(R.id.classPeriod);
        className = findViewById(R.id.className);
        classDay = findViewById(R.id.classDay);

        // setOnClickListener to go back activities and upload schedule
        btnAddClass.setOnClickListener(view -> {
            classInfoUpload();
        });

        tvCancel.setOnClickListener(view -> {
            startActivity(new Intent(ScheduleAdd.this, ScheduleActivity.class));
        });

    }

    // Method to upload class info
    private void classInfoUpload() {
        auth = FirebaseAuth.getInstance();

        // Gets text from textInput in activity
        String class_Period = classPeriod.getText().toString();
        String class_Name = className.getText().toString();
        String class_Day = classDay.getText().toString();

        //checkFilePermissions();
        FirebaseUser user = auth.getCurrentUser();
        String userID = user.getUid();

        // Set location of upload in the database
        String location_database = "users/" + userID + "/schedule/";
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(location_database);

        // Sets parameters to make sure InputTexts are not null
        if (TextUtils.isEmpty(class_Period)) {
            classPeriod.setError("Period cannot be empty");
            classPeriod.requestFocus();
        } else if (TextUtils.isEmpty(class_Name)){
            className.setError("Class Name cannot be empty");
            className.requestFocus();
        } else if (TextUtils.isEmpty(class_Day)){
            classDay.setError("Class Day cannot be empty");
            classDay.requestFocus();
        } else if (class_Time == null) {
            classTime.setError("Class Time cannot be empty");
            classTime.requestFocus();
        } else {
            // If all of the above is false, ScheduleUpload is passed to upload the class info
            ScheduleUpload upload = new ScheduleUpload(class_Period, class_Name, class_Time, class_Day);
            String uploadId = class_Period;
            assert uploadId != null;
            mDatabaseRef.child(uploadId).setValue(upload);

            startActivity(new Intent(ScheduleAdd.this, ScheduleActivity.class));
        }
    }

    // Implements pop-up time picker for class time
    public void popTimePicker(View view){

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Gets the time and formats it into readable format
                hour = hourOfDay;
                minutes = minute;
                classTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minutes));
                class_Time = String.format(Locale.getDefault(), "%02d:%02d", hour, minutes);
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;

        // Shows the TimePicker actvity
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minutes, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}
