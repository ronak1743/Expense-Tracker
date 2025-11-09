package com.ronak.expensetracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ronak.expensetracker.Model.listmodel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserToDB extends AppCompatActivity {

    EditText amount,date,time,note;
    Button btn;
    TextView txt;
    FirebaseAuth auth;
    FirebaseDatabase database;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_to_db);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intilize();

        Intent i=getIntent();
        String s=i.getStringExtra("type");
        if(s.equals("IN")){
            txt.setTextColor(ContextCompat.getColor(UserToDB.this, R.color.green));
            amount.setTextColor(ContextCompat.getColor(UserToDB.this, R.color.green));
        }
        else{
            txt.setTextColor(ContextCompat.getColor(UserToDB.this, R.color.red));
            amount.setTextColor(ContextCompat.getColor(UserToDB.this, R.color.red));
        }

        Date d=new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(d);
        date.setText(formattedDate.toString());


        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String formattedTime = timeFormat.format(d);
        time.setText(formattedTime);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int y=calendar.get(Calendar.YEAR);
                int m=calendar.get(Calendar.MONTH);
                int d=calendar.get(Calendar.DATE);
                DatePickerDialog dpd=new DatePickerDialog(UserToDB.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate=dayOfMonth+"/"+(month+1)+"/"+year;
                        date.setText(selectedDate);
                    }
                },y,m,d);

                dpd.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            Calendar calendar=Calendar.getInstance();
            int hour=calendar.get(Calendar.HOUR_OF_DAY);
            int min=calendar.get(Calendar.MINUTE);
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd=new TimePickerDialog(UserToDB.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am="AM";
                        if(hourOfDay>=12){
                            am="PM";
                        }
                        if(hourOfDay>12){

                            String s=(hourOfDay-12)+":"+minute+am;
                            time.setText(s);
                        }
                        else{
                            if(hourOfDay==0){
                                hourOfDay=0;
                            }
                            String s=(hourOfDay)+":"+minute+am;
                            time.setText(s);
                        }
                    }
                },hour,min,false);
                tpd.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d=date.getText().toString();
                String t=time.getText().toString();
                String n=note.getText().toString();
                String a=amount.getText().toString();
                listmodel l=new listmodel(d,t,n,a,s);
                String UID=auth.getCurrentUser().getUid();
                String key=database.getReference().child("DATA").child(UID).push().getKey();
                database.getReference().child("DATA").child(UID).child(key).setValue(l).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UserToDB.this,"your data is added suceddfully",Toast.LENGTH_LONG);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserToDB.this,"Something went wrong",Toast.LENGTH_LONG);
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(UserToDB.this, MainActivity.class));
                    }
                },1000);
            }


        });

    }

    protected void intilize(){
        database=FirebaseDatabase.getInstance();
        btn=findViewById(R.id.submit_usertodb);
        amount=findViewById(R.id.amount_usertodb);
        date=findViewById(R.id.date_usertodb);
        time=findViewById(R.id.time_usertodb);
        note=findViewById(R.id.note_usertodb);
        txt=findViewById(R.id.amounttxt_user_to_db);
        auth=FirebaseAuth.getInstance();
    }
}