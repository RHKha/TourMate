package com.example.tourmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourmate.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText nameEt,emailEt,passwordEt;
    private String name,email,password;
    private Button signUpBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
            finish();
        }


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = nameEt.getText().toString();
                email = emailEt.getText().toString();
                password = passwordEt.getText().toString();

                signUpRegister(name,email,password);
            }
        });
    }

    public void loginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    private void signUpRegister(final String name, final String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userId = firebaseAuth.getCurrentUser().getUid();
                    Map<String,Object> userMap = new HashMap<>();

                    userMap.put("name",name);
                    userMap.put("email",email);

                    DatabaseReference userRef = databaseReference.child("users").child(userId).child("userInfo");
                    userRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"Success",Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                finish();
                            }
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }


    private void init() {
        nameEt = findViewById(R.id.nameEtId);
        emailEt = findViewById(R.id.emailEtId);
        passwordEt = findViewById(R.id.passwordEtId);
        signUpBtn = findViewById(R.id.signUpBtnId);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }




}
