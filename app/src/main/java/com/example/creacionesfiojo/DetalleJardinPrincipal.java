package com.example.creacionesfiojo;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetalleJardinPrincipal extends AppCompatActivity {

    TextView txtnombre, txtprecio, txtdimensiones, txtmaterial;
    ImageView jardin_photo;
    CardView btn_editar, btn_selecfoto;
    FirebaseFirestore mfirestore;
    String idd;
    RelativeLayout btnatras, btn_whatsApp, btn_facebook, btn_email, btn_insta;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    Context context;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_jardin_principal);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navegationView);
        storageReference = FirebaseStorage.getInstance().getReference().child("ImageFolder");
        progressDialog = new ProgressDialog(this);
        mfirestore = FirebaseFirestore.getInstance();
        String id = getIntent().getStringExtra("id_jardin");
        jardin_photo = findViewById(R.id.jardin_photo);
        btn_insta = findViewById(R.id.btn_insta);
        btn_facebook = findViewById(R.id.btn_facebook);
        btn_email = findViewById(R.id.btn_email);
        btn_whatsApp = findViewById(R.id.btn_whatsApp);
        btnatras = (RelativeLayout) findViewById(R.id.btnatras);
        txtnombre = findViewById(R.id.txtnombre);
        txtprecio = findViewById(R.id.txtprecio);
        txtdimensiones = findViewById(R.id.txtdimensiones);
        txtprecio = findViewById(R.id.txtprecio);
        txtmaterial = findViewById(R.id.txtmaterial);
        btn_selecfoto = findViewById(R.id.btn_selecfoto);
        idd = id;
        get(id);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent intent = new Intent(DetalleJardinPrincipal.this,  JardinPrincipal.class);
                startActivity(intent);
                finish();
            }
        };

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalleJardinPrincipal.this, JardinPrincipal.class);
                startActivity(intent);
                finish();
            }
        });

        btn_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String YourPageURL = "https://www.instagram.com/p/CmE4WKjuYXj/?igshid=YmMyMTA2M2Y%3D";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);

            }
        });



        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailintent = new Intent(Intent.ACTION_SEND);
                emailintent.setType("plain/text");
                emailintent.putExtra(Intent.EXTRA_EMAIL,new String[] {"onbarrantes@gmail.com" });
                emailintent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailintent.putExtra(Intent.EXTRA_TEXT,"");
                startActivity(Intent.createChooser(emailintent, "Send mail..."));
            }
        });

        btn_whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone="+85675478;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String YourPageURL = "https://www.facebook.com/profile.php?id=100067333165934&mibextid=ZbWKwL";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));

                startActivity(browserIntent);
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
                        startActivity(new Intent(DetalleJardinPrincipal.this, PrincipalActivity.class));
                        finish();
                        break;

                    case R.id.jardinP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(DetalleJardinPrincipal.this, JardinPrincipal.class));
                        finish();
                        break;

                    case R.id.paredP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(DetalleJardinPrincipal.this, ParedPrincipal.class));
                        finish();
                        break;

                    case R.id.religionP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(DetalleJardinPrincipal.this, ReligiosoPrincipal.class));
                        finish();
                        break;

                    case R.id.macetaP:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(DetalleJardinPrincipal.this, MacetaPrincipal.class));
                        finish();
                        break;
                    case R.id.cerrarsesion:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(DetalleJardinPrincipal.this, LoginActivity.class));
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });

    }
    private void get(String id){
        mfirestore.collection("Ceramica-Jardin").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("nombre");
                String dimension = documentSnapshot.getString("dimensiones");
                String materia = documentSnapshot.getString("material");
                String price = documentSnapshot.getString("precio");
                String jardiN = documentSnapshot.getString("jardin");

                txtnombre.setText(name);
                txtdimensiones.setText(dimension);
                txtmaterial.setText(materia);
                txtprecio.setText(price);
                try {
                    if(!jardiN.equals("")){

                        Picasso.get()
                                .load(jardiN)
                                .resize(1080, 1440)
                                .into(jardin_photo);
                    }
                }catch (Exception e){
                    Log.v("Error", "e: " + e);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}