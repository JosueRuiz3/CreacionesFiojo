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

import com.example.creacionesfiojo.DetalleJardinAdmin;
import com.example.creacionesfiojo.DetalleReligiosoAdmin;
import com.example.creacionesfiojo.R;
import com.example.creacionesfiojo.model.jardin;
import com.example.creacionesfiojo.model.religioso;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class religiosoAdapter extends FirestoreRecyclerAdapter<religioso, religiosoAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public religiosoAdapter(@NonNull FirestoreRecyclerOptions<religioso> options, Activity activity) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull religiosoAdapter.ViewHolder holder, int position, @NonNull religioso Religioso) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.nombre.setText(Religioso.getNombre());
        holder.precio.setText(Religioso.getPrecio());

        String religioso = Religioso.getReligioso();
        try {
            if (!religioso.equals(""))
                Picasso.get()
                        .load(religioso)
                        .resize(1080, 1440)
                        .into(holder.religioso);
        }catch (Exception e) {
            Log.d("Exception", "e: " + e);
        }

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, DetalleReligiosoAdmin.class);
                i.putExtra("id_religioso", id);
                activity.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public religiosoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_religioso, parent, false);
        return new religiosoAdapter.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, precio, dimensiones, material;
        ImageView religioso, eliminar;
        RelativeLayout editar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombre);
            precio = itemView.findViewById(R.id.precio);
            religioso = itemView.findViewById(R.id.religioso);
            editar = itemView.findViewById(R.id.btn_editar);
            eliminar = itemView.findViewById(R.id.btn_eliminar);

        }
    }
}
