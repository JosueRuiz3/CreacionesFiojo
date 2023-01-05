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

import com.example.creacionesfiojo.DetalleParedAdmin;
import com.example.creacionesfiojo.DetalleParedPrincipal;
import com.example.creacionesfiojo.R;
import com.example.creacionesfiojo.model.pared;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class paredAdapterPrincipal extends FirestoreRecyclerAdapter<pared, paredAdapterPrincipal.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public paredAdapterPrincipal(@NonNull FirestoreRecyclerOptions<pared> options, Activity activity) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull paredAdapterPrincipal.ViewHolder holder, int position, @NonNull pared Pared) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.nombre.setText(Pared.getNombre());
        holder.precio.setText(Pared.getPrecio());

        String pared = Pared.getPared();
        try {
            if (!pared.equals(""))
                Picasso.get()
                        .load(pared)
                        .resize(1080, 1440)
                        .into(holder.pared);
        }catch (Exception e) {
            Log.d("Exception", "e: " + e);
        }

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, DetalleParedPrincipal.class);
                i.putExtra("id_pared", id);
                activity.startActivity(i);
            }
        });

    }

    @NonNull
    @Override
    public paredAdapterPrincipal.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pared, parent, false);
        return new paredAdapterPrincipal.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, precio, dimensiones, material;
        ImageView pared;
        RelativeLayout editar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombre);
            precio = itemView.findViewById(R.id.precio);
            pared = itemView.findViewById(R.id.pared);
            editar = itemView.findViewById(R.id.btn_editar);

        }
    }
}

