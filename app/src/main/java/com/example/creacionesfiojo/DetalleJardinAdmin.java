package com.example.creacionesfiojo;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DetalleJardinAdmin extends AppCompatActivity {

    boolean valid = true;
    EditText txtnombre, txtprecio, txtdimensiones, txtmaterial;
    ImageView jardin_photo, btn_eliminar, btn_habilitar;
    CardView btn_editar, btn_selecfoto;
    FirebaseFirestore mfirestore;
    String idd;
    RelativeLayout btnatras;
    ProgressDialog progressDialog;
    Uri imageUri;
    private FirebaseAuth mAuth;
    String storage_path = "jardin/*";
    String photo = "photo";
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_jardin_admin);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navegationView);
        storageReference = FirebaseStorage.getInstance().getReference().child("ImageFolder");
        progressDialog = new ProgressDialog(this);
        mfirestore = FirebaseFirestore.getInstance();
        String id = getIntent().getStringExtra("id_jardin");
        btn_habilitar = findViewById(R.id.btn_habilitar);
        jardin_photo = findViewById(R.id.jardin_photo);
        btn_eliminar = findViewById(R.id.btn_eliminar);
        btn_editar = findViewById(R.id.btn_editar);
        btnatras =  findViewById(R.id.btnatras);
        txtnombre = findViewById(R.id.txtnombre);
        txtprecio = findViewById(R.id.txtprecio);
        txtdimensiones = findViewById(R.id.txtdimensiones);
        txtprecio = findViewById(R.id.txtprecio);
        txtmaterial = findViewById(R.id.txtmaterial);
        btn_selecfoto = findViewById(R.id.btn_selecfoto);
        idd = id;
        get(id);

        btn_selecfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btn_habilitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleJardinAdmin.this);
                builder.setMessage("¿Desea activar las casillas de edicion?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                txtnombre.setFocusableInTouchMode(true);
                                txtprecio.setFocusableInTouchMode(true);
                                txtmaterial.setFocusableInTouchMode(true);
                                txtdimensiones.setFocusableInTouchMode(true);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();

            }
        });

        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleJardinAdmin.this);
                builder.setMessage("¿Desea editar este producto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                idd = id;
                                checkField(txtnombre);
                                checkField(txtdimensiones);
                                checkField(txtmaterial);
                                checkField(txtprecio);

                                String nombre = txtnombre.getText().toString().trim();
                                String dimensiones = txtdimensiones.getText().toString().trim();
                                String material = txtmaterial.getText().toString().trim();
                                String precio = txtprecio.getText().toString().trim();
                                String download_uri = jardin_photo.getImageMatrix().toString().trim();

                                if (!nombre.isEmpty() && !dimensiones.isEmpty() && !material.isEmpty() && !precio.isEmpty() && !download_uri.isEmpty()) {
                                    subirDatos(nombre, dimensiones, material, precio, id);


                                } else {
                                    Toast.makeText(DetalleJardinAdmin.this, "Ingrese los datos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();

            }
        });

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleJardinAdmin.this);
                builder.setMessage("¿Desea eliminar este producto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                idd = id;
                                delete(id);
                                finish();
                                Intent intent = new Intent(DetalleJardinAdmin.this, JardinAdmin.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent intent = new Intent(DetalleJardinAdmin.this,  JardinAdmin.class);
                startActivity(intent);
                finish();
            }
        };

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(DetalleJardinAdmin.this, JardinAdmin.class);
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
                    case R.id.inicioA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(DetalleJardinAdmin.this, AdminActivity.class));
                        finish();
                        break;

                    case R.id.jardinA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(DetalleJardinAdmin.this, JardinAdmin.class));
                        finish();
                        break;

                    case R.id.paredA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(DetalleJardinAdmin.this, ParedAdmin.class));
                        finish();
                        break;

                    case R.id.religionA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(DetalleJardinAdmin.this, ReligiosoAdmin.class));
                        finish();
                        break;

                    case R.id.macetaA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(DetalleJardinAdmin.this, MacetaAdmin.class));
                        finish();
                        break;
                    case R.id.agregarJadrinA:
                        finish();
                        startActivity(new Intent(DetalleJardinAdmin.this, AgregarFiguraJardin.class));
                        break;

                    case R.id.cerrarsesionA:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(DetalleJardinAdmin.this, LoginActivity.class));
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });

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
    private void subirDatos(String nombre, String dimensiones, String material, String precio, String id) {

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
                                    map.put("nombre", nombre);
                                    map.put("dimensiones", dimensiones);
                                    map.put("material", material);
                                    map.put("precio", precio);
                                    map.put("jardin", download_uri);
                                    mfirestore.collection("Ceramica-Jardin").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            finish();
                                            Toast.makeText(DetalleJardinAdmin.this, "Datos Actualizados con exito", Toast.LENGTH_SHORT).show();
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
                            startActivity(new Intent(DetalleJardinAdmin.this, AgregarFiguraJardin.class));                        }
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

    private void delete(String id) {
        mfirestore.collection("Ceramica-Jardin").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(DetalleJardinAdmin.this, "Eliminado Correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al borrar los datos", Toast.LENGTH_SHORT).show();

            }
        });
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