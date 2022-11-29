package com.example.papeleria.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.example.papeleria.MainActivity;
import com.example.papeleria.Menu;
import com.example.papeleria.R;
import com.example.papeleria.firebase.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    private EditText txtMail;
    private EditText txtPassword;
    private Button btnLogin;
    private Button lblRegister;
    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    SharedPreferences preferences;

    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        txtMail = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPaswsword);
        lblRegister = findViewById(R.id.btnI);
        btnLogin = findViewById(R.id.btnIngresar);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            userLogin();
        });


        lblRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Registrar.class);
                startActivity(intent);

            }
        });

    }//End onCreate


    public void userLogin(){
        String mail = txtMail.getText().toString();
        String password = txtPassword.getText().toString();
iniciaFirebase();
        if (TextUtils.isEmpty(mail)){
            txtMail.setError("Ingresa tu correo");
            txtMail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "Ingresa tu contraseña", Toast.LENGTH_SHORT).show();
            txtPassword.requestFocus();
        }else{

            mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        databaseReference= FirebaseDatabase.getInstance().getReference("User");
                        databaseReference.addChildEventListener(new ChildEventListener() {

                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                User aux= snapshot.getValue(User.class);

                                String nom = aux.getCorreo();
                                String nom2= mail;
                                if(nom.equals(nom2)){
                                    if (aux.getNivel()==0){

                                        Intent carrito = new Intent(getApplicationContext(), Menu.class);
                                        startActivity(carrito);
                                    }
                                    else {


                                        Intent gerente = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(gerente);

                                        }
                                }





                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            }


                        });


                    }else {
                        Log.w("TAG", "Error:", task.getException());
                        Toast.makeText(LoginActivity.this,"Usuario y/o contraseña incorrecta",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

    }


    private void iniciaFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDataBase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDataBase.getReference();
    }


}// End LoginActivity