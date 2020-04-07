package com.example.telacortesparciais;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public ImageView icon;
    public TextView name;
    public TextView description;
    public LinearLayout linearLayout;
    public LinearLayout linearLayoutCard;
    public TextView textoRiscado;

    @BindView(R.id.paProduto)
    public ProductAdder edtTextQuantidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        icon = findViewById(R.id.imageIcon);
        name = findViewById(R.id.textName);
        description = findViewById(R.id.textDescription);
        linearLayout = findViewById(R.id.linearLayoutDate);
        linearLayoutCard = findViewById(R.id.linearLayoutPlanoB);
        textoRiscado = findViewById(R.id.textRiscado);

       edtTextQuantidade.setNumberEditable(false);
       edtTextQuantidade.setMaxValueAllowed(999);
       edtTextQuantidade.setMinValueAllowed(0);

        Bundle bundle = getIntent().getExtras();
        int icone = bundle.getInt("ilustra");
        String nome = bundle.getString("nome");
        String descricao = bundle.getString("description");

        icon.setImageResource(icone);
        name.setText(nome);
        description.setText(descricao);

        if (nome.equals("Captação em Transito") || nome.equals("Compra Garantida")){
            linearLayout.setVisibility(View.VISIBLE);
        }

        if (nome.equals("Plano B")){
            linearLayoutCard.setVisibility(View.VISIBLE);
            textoRiscado.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        toolbar = findViewById(R.id.toolbarCortesParciais);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}
