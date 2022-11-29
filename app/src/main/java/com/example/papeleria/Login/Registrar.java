package com.example.papeleria.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.papeleria.R;
import com.example.papeleria.firebase.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registrar extends AppCompatActivity {


    private EditText txtMail1;
    private EditText txtUser;
    private EditText txtPassword1;
    private Button btnRegister;
    private Button btnRegisterA;
    private TextView lblLogin;


    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        txtMail1 = findViewById(R.id.txtMail);

        txtPassword1 = findViewById(R.id.txtPassword);
         txtUser= findViewById(R.id.txtnNom);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegisterA = findViewById(R.id.btnRegisterAdmin);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(view -> {
            createuser();
        });
        btnRegisterA.setOnClickListener(view -> {
            createAdmin();
        });


    }//End onCreate


    private void iniciaFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDataBase.getReference();
    }


    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }// End openLoginActivity

    public void createuser() {


        String mail = txtMail1.getText().toString();

        String password = txtPassword1.getText().toString();
        String name = txtUser.getText().toString();

        if (TextUtils.isEmpty(mail)) {
            txtMail1.setError("Ingrese un Correo");
            txtMail1.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            txtMail1.setError("Ingrese una Contraseña");
            txtMail1.requestFocus();
        }
        else if(TextUtils.isEmpty(name)){
            txtMail1.setError("Ingrese un Nombre");
            txtMail1.requestFocus();
        }

        else {

            mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("users").document(userID);

                        Map<String, Object> user = new HashMap<>();

                        user.put("Correo", mail);
                        user.put("Nombre", name);

                        user.put("Contraseña", password);
                        iniciaFirebase();
                        nivel(mail,name);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG", "onSuccess: Datos registrados" + userID);
                            }
                        });
                        Toast.makeText(Registrar.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registrar.this, LoginActivity.class));
                    } else {
                        Toast.makeText(Registrar.this, "Usuario no registrado" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }


    public void createAdmin() {


        String mail = txtMail1.getText().toString();

        String password = txtPassword1.getText().toString();
        String name = txtUser.getText().toString();

        if (TextUtils.isEmpty(mail)) {
            txtMail1.setError("Ingrese un Correo");
            txtMail1.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            txtMail1.setError("Ingrese una Contraseña");
            txtMail1.requestFocus();
        }
        else if(TextUtils.isEmpty(name)){
            txtMail1.setError("Ingrese un Nombre");
            txtMail1.requestFocus();
        }

        else {

            mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("users").document(userID);

                        Map<String, Object> user = new HashMap<>();

                        user.put("Correo", mail);
                        user.put("Nombre", name);

                        user.put("Contraseña", password);
                        iniciaFirebase();
                        nivelAdmin(mail,name);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG", "onSuccess: Datos registrados" + userID);
                            }
                        });
                        Toast.makeText(Registrar.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registrar.this, LoginActivity.class));
                    } else {
                        Toast.makeText(Registrar.this, "Usuario no registrado" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }
    public void nivel(String correo,String n){
        User u = new User(correo,n,0);
        databaseReference.child("User").child(u.getName()).setValue(u);
    }

    public void nivelAdmin(String correo,String n){
        User u = new User(correo,n,1);
        databaseReference.child("User").child(u.getName()).setValue(u);
    }



}
