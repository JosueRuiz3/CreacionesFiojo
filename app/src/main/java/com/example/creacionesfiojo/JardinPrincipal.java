package com.example.creacionesfiojo;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.SearchView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.creacionesfiojo.adapter.jardinAdapter;
import com.example.creacionesfiojo.adapter.jardinAdapterPrincipal;
import com.example.creacionesfiojo.model.jardin;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.remote.Datastore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JardinPrincipal extends AppCompatActivity {
    private static final String TAG = "FirestoreSearchActivity";
    private View cerrarsesion;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference reference;
    private jardinAdapterPrincipal jAdapter;
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
        setContentView(R.layout.activity_jardin_principal);

        NavigationView navigationView = findViewById(R.id.navegationView);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        mFirestore = FirebaseFirestore.getInstance();
        btnatras = findViewById(R.id.btnatras);
        editbuscar = findViewById(R.id.editbuscar);

        Query query = mFirestore.collection("Ceramica-Jardin")
                .orderBy("nombre", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<jardin> options = new FirestoreRecyclerOptions.Builder<jardin>()
                .setQuery(query, jardin.class)
                .build();

        activity = new ArrayList<>();

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(JardinPrincipal.this,  PrincipalActivity.class);
                startActivity(intent);
            }
        });


        editbuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "Searchbox has changed to: " + s.toString());
                Query query;
                if (s.toString().isEmpty()) {
                    query = mFirestore.collection("Ceramica-Jardin")
                            .orderBy("nombre", Query.Direction.ASCENDING);
                } else {
                    query = mFirestore.collection("Ceramica-Jardin")
                            .whereEqualTo("nombre", s.toString())
                            .orderBy("nombre", Query.Direction.ASCENDING);
                }

                FirestoreRecyclerOptions<jardin> options = new FirestoreRecyclerOptions.Builder<jardin>()
                        .setQuery(query, jardin.class)
                        .build();
                jAdapter.updateOptions(options);
                jAdapter.notifyDataSetChanged();
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
                    case R.id.jardinP:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(JardinPrincipal.this, JardinPrincipal.class));
                    finish();
                    break;

                    case R.id.paredP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(JardinPrincipal.this, ParedPrincipal.class));
                        finish();
                        break;

                    case R.id.religionP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(JardinPrincipal.this, ReligiosoPrincipal.class));
                        finish();
                        break;

                    case R.id.macetaP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(JardinPrincipal.this, MacetaPrincipal.class));
                        finish();
                        break;
                    case R.id.cerrarsesion:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(JardinPrincipal.this, LoginActivity.class));
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
        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        Query query = mFirestore.collection("Ceramica-Jardin");

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecycler.setLayoutManager(mLayoutManager);

        FirestoreRecyclerOptions<jardin> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<jardin>().setQuery(query, jardin.class).build();

        jAdapter= new jardinAdapterPrincipal(firestoreRecyclerOptions, this);
        jAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(jAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        jAdapter.startListening();
        mRecycler.getRecycledViewPool().clear();
        jAdapter.notifyDataSetChanged();
        jAdapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        jAdapter.stopListening();
    }

}


