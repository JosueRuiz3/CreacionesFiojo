package com.example.creacionesfiojo;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private TextView restablecer;
    private EditText textemail, textcontraseña;
    private CardView btniniciarsesion, btncrearcuenta;;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseDatabase firebaseDatabase;
    boolean valid = true;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        textcontraseña = (EditText) findViewById(R.id.textcontraseña);
        textemail = (EditText) findViewById(R.id.textemail);
        restablecer = (TextView) findViewById(R.id.restablecer);
        btncrearcuenta = (CardView) findViewById(R.id.btncrearcuenta);
        btniniciarsesion=(CardView)findViewById(R.id.btniniciarsesion);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent intent = new Intent(LoginActivity.this,  MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        btncrearcuenta.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        restablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view3) {
                Intent intent = new Intent(LoginActivity.this, RestablecerContrasena.class);
                startActivity(intent);
            }
        });


        btniniciarsesion.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View view2) {
               iniciarSesion();
              // Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
               //Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
              // startActivity(intent);
            }

        });
    }

    public void iniciarSesion(){
        checkField(textemail);
        checkField(textcontraseña);

        Log.d("TAG", "onClick: " + textemail.getText().toString());


        if (valid){
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(textemail.getText().toString().trim(), textcontraseña.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    checkUserAccessLevel(authResult.getUser().getUid());
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Correo Electronico o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = mFirestore.collection("Users").document(uid);
        //extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                //identify the user access level

                if (documentSnapshot.getString("admin") != null){

                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    Toast.makeText(LoginActivity.this, "Bienvenido Administrador", Toast.LENGTH_SHORT).show();
                    finish();
                }
                if (documentSnapshot.getString("userType") != null){
                    startActivity(new Intent(getApplicationContext(), PrincipalActivity.class));
                    Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }

    public boolean  checkField(EditText textField){
        if (textField.getText().toString().isEmpty()){
            textField.setError("Debes llenar los campos");
            Toast.makeText(LoginActivity.this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else  {
            valid = true;
        }
        return valid;
    }

}
