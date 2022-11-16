package com.example.examen10_11_22.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examen10_11_22.R;
import com.example.examen10_11_22.entities.Pelicula;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PeliculaAdapter extends RecyclerView.Adapter {

    List<Pelicula> data;

    public PeliculaAdapter(List<Pelicula> data){
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());//aquí llamamos al contexto

        View itemView = inflater.inflate(R.layout.item_peliculas, parent, false);//aquí hacemos referencia al item creado

        return new PeliculasViewHolder(itemView);//aquí retornamos el itemView creado
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView tvTitulo = holder.itemView.findViewById(R.id.tvTitulo);
        tvTitulo.setText(data.get(position).titulo);

        TextView tvSinopsis = holder.itemView.findViewById(R.id.tvSinopsis);
        tvSinopsis.setText(data.get(position).sinopsis);

        ImageView ivImagen= holder.itemView.findViewById(R.id.ivImagen);
        Picasso.get().load(data.get(position).imagen).into(ivImagen);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class PeliculasViewHolder extends RecyclerView.ViewHolder {
        public PeliculasViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
