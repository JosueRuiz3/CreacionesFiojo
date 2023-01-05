package com.example.creacionesfiojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.creacionesfiojo.adapter.jardinAdapter;
import com.example.creacionesfiojo.adapter.paredAdapter;
import com.example.creacionesfiojo.model.jardin;
import com.example.creacionesfiojo.model.pared;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ParedAdmin extends AppCompatActivity {

    private paredAdapter pAdapter;
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
        setContentView(R.layout.activity_pared_admin);

        NavigationView navigationView = findViewById(R.id.navegationView);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.recyclerViewSinglePared);
        btnatras = findViewById(R.id.btnatras);

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(ParedAdmin.this,  AdminActivity.class);
                startActivity(intent);
            }
        });

        mRecycler = findViewById(R.id.recyclerViewSinglePared); toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
                        startActivity(new Intent(ParedAdmin.this, AdminActivity.class));
                        break;

                    case R.id.agregarJadrinA:
                        finish();
                        startActivity(new Intent(ParedAdmin.this, AgregarParedAdmin.class));
                        break;

                    case R.id.jardinA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ParedAdmin.this, JardinAdmin.class));
                        finish();
                        break;

                    case R.id.paredA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ParedAdmin.this, ParedAdmin.class));
                        finish();
                        break;

                    case R.id.religionA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ParedAdmin.this, ReligiosoAdmin.class));
                        finish();
                        break;

                    case R.id.macetaA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ParedAdmin.this, MacetaAdmin.class));
                        finish();
                        break;

                    case R.id.cerrarsesionA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ParedAdmin.this, LoginActivity.class));
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });
        setUpRecyclerView();
    }
        @SuppressLint("NotifyDataSetChanged")
        private void setUpRecyclerView() {
            mRecycler = findViewById(R.id.recyclerViewSinglePared);
            mRecycler.setLayoutManager(new LinearLayoutManager(this));

            Query query = mFirestore.collection("Ceramica-Pared");

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            mRecycler.setLayoutManager(mLayoutManager);

            FirestoreRecyclerOptions<pared> firestoreRecyclerOptions =
                    new FirestoreRecyclerOptions.Builder<pared>().setQuery(query, pared.class).build();

            pAdapter= new paredAdapter(firestoreRecyclerOptions, this);
            pAdapter.notifyDataSetChanged();
            mRecycler.setAdapter(pAdapter);
        }

        @Override
        protected void onStart() {
            super.onStart();
            pAdapter.startListening();
            mRecycler.getRecycledViewPool().clear();
            pAdapter.notifyDataSetChanged();
            pAdapter.startListening();
        }

        @Override
        protected void onStop() {
            super.onStop();
            pAdapter.stopListening();
        }
    }