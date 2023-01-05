package com.example.creacionesfiojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminActivity extends AppCompatActivity {
    private View cerrarsesion;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private RelativeLayout jardin, religioso, pared, macetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        NavigationView navigationView = findViewById(R.id.navegationView);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        jardin = (RelativeLayout) findViewById(R.id.jardin);
        religioso = (RelativeLayout) findViewById(R.id.religioso);
        macetas = (RelativeLayout) findViewById(R.id.macetas);
        pared = (RelativeLayout) findViewById(R.id.pared);

        jardin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,  JardinAdmin.class);
                startActivity(intent);
            }
        });

        pared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,  ParedAdmin.class);
                startActivity(intent);
            }
        });

        macetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,  MacetaAdmin.class);
                startActivity(intent);
            }
        });

        religioso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,  ReligiosoAdmin.class);
                startActivity(intent);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {
                    case R.id.inicioP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AdminActivity.this, AdminActivity.class));
                        finish();
                        break;

                    case R.id.jardinP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AdminActivity.this, JardinAdmin.class));
                        finish();
                        break;

                    case R.id.paredP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AdminActivity.this, ParedAdmin.class));
                        finish();
                        break;

                    case R.id.religionP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AdminActivity.this, ReligiosoAdmin.class));
                        finish();
                        break;

                    case R.id.macetaP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AdminActivity.this, MacetaAdmin.class));
                        finish();
                        break;
                    case R.id.agregarJadrinA:
                        finish();
                        startActivity(new Intent(AdminActivity.this, AgregarFiguraJardin.class));
                        break;

                    case R.id.cerrarsesionA:

                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });

    }
}