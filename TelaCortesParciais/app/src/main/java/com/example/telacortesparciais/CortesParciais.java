package com.example.telacortesparciais;

import android.widget.ImageView;

public class CortesParciais {

    private int imageIcon;
    private String nome;
    private String description;

    public CortesParciais() {
    }

    public CortesParciais(int imageIcon, String nome, String description) {
        this.imageIcon = imageIcon;
        this.nome = nome;
        this.description = description;
    }

    public int getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(int imageIcon) {
        this.imageIcon = imageIcon;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
