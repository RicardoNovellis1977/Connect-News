package com.example.telacortesparciais;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CortesAdapter extends RecyclerView.Adapter<CortesAdapter.MyViewHolder> {

    private List<CortesParciais> lCortes;
    private Context context;

    public CortesAdapter(List<CortesParciais> lCortes, Context context) {
        this.lCortes = lCortes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cortes_parciais, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CortesParciais cortes = lCortes.get(position);
        holder.nome.setText(cortes.getNome());
    }

    @Override
    public int getItemCount() {
        return lCortes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.txtCortesParciais);
        }
    }
}
