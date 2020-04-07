package com.example.telacortesparciais;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerViewCortes;
    public ArrayList<CortesParciais> cortes = new ArrayList<CortesParciais>();

    CortesParciais corte = new CortesParciais(R.drawable.ilustra, "Embalagem Diferente", "O item solicitado não esta disponível, mas temos esta opção que é o mesmo produto com embalagem e código diferentes, ok?");
    CortesParciais corte1 = new CortesParciais(R.drawable.ilustra_captacao, "Captação em Transito", "O item solicitado não esta disponível para pronta entrega, mas caso você possa esperar mais um pouquinho para receber seu pedido inteiro, conseguiremos incluir este produto.");
    CortesParciais corte2 = new CortesParciais(R.drawable.ilustra_plano_b, "Plano B", "O Plano B é a troca do produto selecionado por outro alternativo.");
    CortesParciais corte3 = new CortesParciais(R.drawable.ilustra_compra_garantida, "Compra Garantida", "O Compra Garantida te assegura as mesmas condições de compra quando o estoque estiver normalizado.");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cortes.add(corte);
        cortes.add(corte1);
        cortes.add(corte2);
        cortes.add(corte3);

        recyclerViewCortes = findViewById(R.id.recyclerTelas);
        CortesAdapter cortesAdapter = new CortesAdapter(cortes, this);
        recyclerViewCortes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCortes.setAdapter(cortesAdapter);

        recyclerViewCortes.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerViewCortes, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                switch (cortes.get(position).getNome()) {

                    case "Embalagem Diferente":

                    case  "Captação em Transito":

                    case  "Plano B":

                    case "Compra Garantida":
                        Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("ilustra",cortes.get(position).getImageIcon());
                        bundle.putString("nome",cortes.get(position).getNome());
                        bundle.putString("description",cortes.get(position).getDescription());

                        i.putExtras(bundle);
                        startActivity(i);
                        break;

                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

    }

}
