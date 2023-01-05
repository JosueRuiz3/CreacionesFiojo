package com.example.creacionesfiojo;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.units.qual.C;

public class RestablecerContrasena extends AppCompatActivity {

    private EditText editTextEmail;
    private CardView btnrestablecer;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    RelativeLayout btnatras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer_contrasena);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        btnrestablecer = (CardView) findViewById(R.id.btnrestablecer);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        btnatras = (RelativeLayout) findViewById(R.id.btnatras);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent intent = new Intent(RestablecerContrasena.this,  LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestablecerContrasena.this,  LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnrestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Introduzca su correo electrónico registrado", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RestablecerContrasena.this, "Le hemos enviado instrucciones para restablecer su contraseña.", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(RestablecerContrasena.this, "Si es necesario revisa la bandeja de Spam.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RestablecerContrasena.this, LoginActivity.class));
                                } else {
                                    Toast.makeText(RestablecerContrasena.this, "No se ha podido enviar el correo electrónico de restablecimiento.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RestablecerContrasena.this, LoginActivity.class));
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

}