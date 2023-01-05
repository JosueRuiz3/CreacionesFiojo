package com.example.creacionesfiojo;

import androidx.activity.OnBackPressedCallback;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.example.creacionesfiojo.adapter.jardinAdapterPrincipal;
import com.example.creacionesfiojo.adapter.paredAdapter;
import com.example.creacionesfiojo.adapter.paredAdapterPrincipal;
import com.example.creacionesfiojo.model.pared;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.remote.Datastore;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ParedPrincipal extends AppCompatActivity {

    private static final String TAG = "FirestoreSearchActivity";
    private View cerrarsesion;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference reference;
    private paredAdapterPrincipal pAdapter;
    private RecyclerView mRecycler;
    private RelativeLayout btnatras;
    private Context context;
    private DataSnapshot postSnapshot;
    private List<Datastore> activity;
    private ProgressBar mprogress;
    private StorageReference storageReference;
    private SearchView search_view;
    private EditText editbuscar;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pared_principal);

        NavigationView navigationView = findViewById(R.id.navegationView);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        mFirestore = FirebaseFirestore.getInstance();
        btnatras = findViewById(R.id.btnatras);
        editbuscar = findViewById(R.id.editbuscar);


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                Intent intent = new Intent(ParedPrincipal.this,  PrincipalActivity.class);
                startActivity(intent);
            }
        };

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(ParedPrincipal.this,  PrincipalActivity.class);
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
                    case R.id.jardinP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ParedPrincipal.this, JardinPrincipal.class));
                        finish();
                        break;

                    case R.id.paredP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ParedPrincipal.this, ParedPrincipal.class));
                        finish();
                        break;

                    case R.id.religionP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ParedPrincipal.this, ReligiosoPrincipal.class));
                        finish();
                        break;

                    case R.id.macetaP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ParedPrincipal.this, MacetaPrincipal.class));
                        finish();
                        break;

                    case R.id.cerrarsesion:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(ParedPrincipal.this, LoginActivity.class));
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

        pAdapter= new paredAdapterPrincipal(firestoreRecyclerOptions, this);
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