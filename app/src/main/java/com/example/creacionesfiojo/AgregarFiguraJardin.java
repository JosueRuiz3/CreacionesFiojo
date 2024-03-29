package com.example.creacionesfiojo;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AgregarFiguraJardin extends AppCompatActivity {

    private EditText txtDescripcion, txtnombre, txtdimensiones, txtmaterial, txtprecio;
    private FirebaseFirestore mfirestore;
    private CardView btnagregar, btn_selecfoto;
    private ImageView jardin_photo;
    private Button  btn_remove_photo;
    private Uri imageUri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    boolean valid = true;
    private FirebaseAuth mAuth;
    private String storage_path = "jardin/*";
    private  String photo = "photo";
    private String idd;
    private DatabaseReference mDatabase;
    private static final int ImageBack = 1;
    private RelativeLayout btnatras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_figura_jardin);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navegationView);
        txtnombre = findViewById(R.id.txtnombre);
        txtdimensiones = findViewById(R.id.txtdimensiones);
        txtmaterial = findViewById(R.id.txtmaterial);
        txtprecio = findViewById(R.id.txtprecio);
        btnagregar = findViewById(R.id.btnagregar);
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("ImageFolder");
        btn_selecfoto = findViewById(R.id.btn_selecfoto);
        jardin_photo = findViewById(R.id.jardin_photo);
        btn_selecfoto = findViewById(R.id.btn_selecfoto);
        progressBar = findViewById(R.id.progressBar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnatras = findViewById(R.id.btnatras);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                Intent intent = new Intent(AgregarFiguraJardin.this,  JardinAdmin.class);
                startActivity(intent);
            }
        };

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(AgregarFiguraJardin.this,  JardinAdmin.class);
                startActivity(intent);
            }
        });

        btn_selecfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();

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
                    case R.id.inicioA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AgregarFiguraJardin.this, AdminActivity.class));
                        finish();
                        break;

                    case R.id.jardinA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AgregarFiguraJardin.this, JardinAdmin.class));
                        finish();
                        break;

                    case R.id.paredA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AgregarFiguraJardin.this, ParedAdmin.class));
                        finish();
                        break;

                    case R.id.religionA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AgregarFiguraJardin.this, ReligiosoAdmin.class));
                        finish();
                        break;

                    case R.id.macetaA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AgregarFiguraJardin.this, MacetaAdmin.class));
                        finish();
                        break;
                    case R.id.agregarJadrinA:
                        finish();
                        startActivity(new Intent(AgregarFiguraJardin.this, AgregarFiguraJardin.class));
                        break;

                    case R.id.cerrarsesionA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AgregarFiguraJardin.this, LoginActivity.class));
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });
    }

    private void guardar(){

        checkField(txtnombre);
        checkField(txtdimensiones);
        checkField(txtmaterial);
        checkField(txtprecio);

        String nombre = txtnombre.getText().toString().trim();
        String dimensiones = txtdimensiones.getText().toString().trim();
        String material = txtmaterial.getText().toString().trim();
        String precio = txtprecio.getText().toString().trim();
        String imagen = jardin_photo.getImageMatrix().toString().trim();

        if (!nombre.isEmpty() && !dimensiones.isEmpty() && !material.isEmpty() && !precio.isEmpty() && !imagen.isEmpty()) {
            subirDatos(nombre, dimensiones, material, precio);

        } else {
            Toast.makeText(AgregarFiguraJardin.this, "Ingrese los datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {

            imageUri = data.getData();
            jardin_photo.setImageURI(imageUri);
        }
    }


    private void subirDatos(String nombre, String dimensiones, String material, String precio) {

        DocumentReference id = mfirestore.collection("Ceramica-Jardin").document();
        Map<String, Object> map = new HashMap<>();

        if(imageUri!=null)     {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());

            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String download_uri = uri.toString();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("id", id.getId());
                                    map.put("nombre", nombre);
                                    map.put("dimensiones", dimensiones);
                                    map.put("material", material);
                                    map.put("precio", precio);
                                    map.put("jardin", download_uri);
                                    mfirestore.collection("Ceramica-Jardin").document(id.getId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            finish();
                                            Toast.makeText(AgregarFiguraJardin.this, "Datos guardados con exito", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                            progressDialog.dismiss();
                            startActivity(new Intent(AgregarFiguraJardin.this, AgregarFiguraJardin.class));                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progres = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progres+"%");
                        }
                    }) ;
        }
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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}