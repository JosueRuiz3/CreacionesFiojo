package com.example.creacionesfiojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MacetaAdmin extends AppCompatActivity {


    private View cerrarsesion;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private RecyclerView mRecycler;
    private Query query;
    private RelativeLayout editar;
    private Context context;
    private RelativeLayout btnatras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maceta_admin);

        NavigationView navigationView = findViewById(R.id.navegationView);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.recyclerViewSingle);
        btnatras = findViewById(R.id.btnatras);

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(MacetaAdmin.this,  AdminActivity.class);
                startActivity(intent);
            }
        });

        mRecycler = findViewById(R.id.recyclerViewSingle); toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
                    case R.id.inicioA:
                        finish();
                        startActivity(new Intent(MacetaAdmin.this, AdminActivity.class));
                        break;

                    case R.id.agregarJadrinA:
                        finish();
                        startActivity(new Intent(MacetaAdmin.this, AgregarParedAdmin.class));
                        break;

                    case R.id.jardinA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MacetaAdmin.this, JardinAdmin.class));
                        finish();
                        break;

                    case R.id.paredA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MacetaAdmin.this, ParedAdmin.class));
                        finish();
                        break;

                    case R.id.religionA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MacetaAdmin.this, ReligiosoAdmin.class));
                        finish();
                        break;

                    case R.id.macetaA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MacetaAdmin.this, MacetaAdmin.class));
                        finish();
                        break;

                    case R.id.cerrarsesionA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MacetaAdmin.this, LoginActivity.class));
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });

    }
}