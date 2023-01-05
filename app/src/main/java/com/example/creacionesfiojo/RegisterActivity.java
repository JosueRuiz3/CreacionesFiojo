package com.example.creacionesfiojo;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.creacionesfiojo.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextconfirmar, editTextcontraseña, editTextEmail, editTextnombre, editTextapellido;
    private CardView btncrearcuenta;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    boolean valid = true;
    private ProgressBar progressBar;
    private RelativeLayout btnatras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        editTextcontraseña = (EditText) findViewById(R.id.editTextcontraseña);
        editTextconfirmar = (EditText) findViewById(R.id.editTextconfirmar);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextnombre = (EditText) findViewById(R.id.editTextnombre);
        editTextapellido = (EditText) findViewById(R.id.editTextapellido);
        btncrearcuenta=(CardView)findViewById(R.id.btncrearcuenta);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnatras = (RelativeLayout) findViewById(R.id.btnatras);


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent intent = new Intent(RegisterActivity.this,  LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,  MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btncrearcuenta.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }

    private void  register(){

        checkField(editTextEmail);
        checkField(editTextcontraseña);
        checkField(editTextconfirmar);
        checkField(editTextnombre);
        checkField(editTextapellido);

        String email = editTextEmail.getText().toString().trim();
        String password = editTextcontraseña.getText().toString().trim();
        String confirmar = editTextconfirmar.getText().toString().trim();
        String name = editTextnombre.getText().toString().trim();
        String apellido = editTextapellido.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if(!email.isEmpty() && !password.isEmpty() && !confirmar.isEmpty() && !name.isEmpty() && !apellido.isEmpty()){
            if (isEmailValid(email)){
                if (password.equals(confirmar)){
                    if (password.length() >= 6){
                        createUser(email, password, name, apellido);
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "La contraseña debe contener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();

                }
            }
            else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "El correo no es valido", Toast.LENGTH_SHORT).show();

            }
        }
        else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Debes de llenar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    private void createUser(String email, String password, String name, String lastname) {


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Map<String, Object> map = new HashMap<>();
                map.put("email", email);
                map.put("name", name);
                map.put("lastname", lastname);
                String id = mAuth.getCurrentUser().getUid();
                String userType = "Cliente";

                Users user = new Users(email, name, lastname, id, userType);


                mFirestore.collection("Users").document(id).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        Toast.makeText(RegisterActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isEmailValid(String email){
        String expression = "^[\\w\\-.]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public boolean checkField(EditText textField){
        if (textField.getText().toString().isEmpty()){
            textField.setError("Debes llenar los campos");
            valid = false;
        }
        else  {
            valid = true;
        }
        return valid;
    }
}