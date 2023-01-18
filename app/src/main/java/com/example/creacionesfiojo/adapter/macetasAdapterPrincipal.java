package com.example.creacionesfiojo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creacionesfiojo.DetalleJardinPrincipal;
import com.example.creacionesfiojo.DetalleMacetaPrincipal;
import com.example.creacionesfiojo.R;
import com.example.creacionesfiojo.model.jardin;
import com.example.creacionesfiojo.model.macetas;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class macetasAdapterPrincipal extends FirestoreRecyclerAdapter<macetas, macetasAdapterPrincipal.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public macetasAdapterPrincipal(@NonNull FirestoreRecyclerOptions<macetas> options, Activity activity) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull macetasAdapterPrincipal.ViewHolder holder, int position, @NonNull macetas Macetas) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.nombre.setText(Macetas.getNombre());
        holder.precio.setText(Macetas.getPrecio());

        String macetas = Macetas.getMacetas();
        try {
            if (!macetas.equals(""))
                Picasso.get()
                        .load(macetas)
                        .resize(1080, 1440)
                        .into(holder.macetas);
        }catch (Exception e) {
            Log.d("Exception", "e: " + e);
        }

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, DetalleMacetaPrincipal.class);
                i.putExtra("id_macetas", id);
                activity.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public macetasAdapterPrincipal.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_maceta, parent, false);
        return new macetasAdapterPrincipal.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, precio, dimensiones, material;
        ImageView macetas;
        RelativeLayout editar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombre);
            precio = itemView.findViewById(R.id.precio);
            macetas = itemView.findViewById(R.id.macetas);
            editar = itemView.findViewById(R.id.btn_editar);
        }
    }
}

