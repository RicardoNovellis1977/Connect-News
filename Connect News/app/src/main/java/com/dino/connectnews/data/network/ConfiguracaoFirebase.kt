package com.dino.connectnews.data.network

import com.dino.connectnews.data.model.Usuario
import com.google.firebase.database.FirebaseDatabase

class ConfiguracaoFirebase {
    companion object {
        fun salvar(usuario: Usuario) {
            val referenceFirebase = FirebaseDatabase.getInstance()
          val myRef =  referenceFirebase.getReference("usuario")
              myRef.child(usuario.id).setValue(usuario)
        }
    }
}