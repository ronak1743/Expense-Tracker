package com.ronak.expensetracker;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ronak.expensetracker.Adapters.ListAdapter;
import com.ronak.expensetracker.Model.UserModel;
import com.ronak.expensetracker.Model.listmodel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Dialog d1;
    FirebaseAuth auth;
    ImageView img;
    ArrayList<listmodel> list = new ArrayList<>();
    ListAdapter listAdapter;
    Button cashinbtn, cashoutbtn;
    FirebaseDatabase database;
    RecyclerView rv;
    UserModel u1;
    String UID1;
    TextView cashintxt, cashouttxt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        initialize();


        checkuser();
        FirebaseUser cur = auth.getCurrentUser();
        UID1 = cur.getUid();


        listAdapter = new ListAdapter(this, list);

        getData();

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(listAdapter);


        cashinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserToDB.class);
                i.putExtra("type", "IN");
                startActivity(i);
            }
        });

        cashoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserToDB.class);
                i.putExtra("type", "OUT");
                startActivity(i);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopup(v);
            }
        });

    }


    public  void initialize(){
        auth = FirebaseAuth.getInstance();
        cashinbtn = findViewById(R.id.cash_in_btn);
        cashoutbtn = findViewById(R.id.cash_out_btn);
        img=findViewById(R.id.user_logout);
        cashintxt = findViewById(R.id.total_in_main);
        cashouttxt = findViewById(R.id.total_out_main);
        rv = findViewById(R.id.recycle_main);
    }


    public void getData(){

        FirebaseDatabase.getInstance().getReference().child("DATA").child(UID1)
                .addValueEventListener(new ValueEventListener() {
                    long a1=0;
                    long a2=0;
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                a1=0;
                a2=0;
                for (DataSnapshot s : snapshot.getChildren()) {

                    listmodel l = s.getValue(listmodel.class);
                    list.add(l);
                    long x=Long.parseLong(l.getAmount());

                    if(l.getType().equals("IN")){
                        a1+=x;
                    }
                    else{
                        a2+=x;
                    }

                    cashintxt.setText(a1+"");
                    cashouttxt.setText(a2+"");
                }
                listAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    public  void checkuser(){

        if (auth.getCurrentUser() == null) {
            Intent i = new Intent(MainActivity.this, SingInActivity.class);
            startActivity(i);
            finish();
        }

    }

    public  void createPopup(View v){
        PopupMenu popupMenu=new PopupMenu(MainActivity.this,v);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu,popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.changepass_menu) {
                    changePassword();
                }
                else if(item.getItemId()==R.id.singout_menu){
                    singout();
                }
                return false;
            }
        });
    }

    public  void singout(){
        auth.signOut();
        Intent i=new Intent(MainActivity.this,SingActivity.class);
        startActivity(i);
        finish();
    }

    public void changePassword(){
        d1=new Dialog(MainActivity.this);
        database=FirebaseDatabase.getInstance();
        d1.setContentView(R.layout.change_password);
        d1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        EditText oldpassword=d1.findViewById(R.id.old_password_change);
        EditText newpassword=d1.findViewById(R.id.new_password_change);
        Button btn=d1.findViewById(R.id.changebtn_change);
        d1.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpass=oldpassword.getText().toString();
                String newpass=newpassword.getText().toString();
                FirebaseUser cur = auth.getCurrentUser();
                UID1 = cur.getUid();

               database.getReference().child("User").child(UID1).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.exists()){
                           UserModel us=snapshot.getValue(UserModel.class);
                           if(us!=null  && us.getPassword().equals(oldpass)){
                               cur.updatePassword(newpass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       us.setPassword(newpass);
                                       database.getReference().child("User").child(UID1).setValue(us);
                                       Toast.makeText(MainActivity.this, "Password changed", Toast.LENGTH_SHORT).show();

                                   }
                               });
                           }
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });

                d1.dismiss();
            }
        });
    }
}