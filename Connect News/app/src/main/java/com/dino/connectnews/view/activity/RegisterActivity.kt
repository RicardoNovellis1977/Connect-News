package com.dino.connectnews.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dino.connectnews.R
import com.dino.connectnews.data.network.Base64Custom
import com.dino.connectnews.data.network.ConfiguracaoFirebase
import com.dino.connectnews.data.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var usuario = Usuario()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register_now.setOnClickListener {
            val textoNome: String = textoNome.text.toString()
            val textoEmail: String = txt_registrar_email.text.toString()
            val textoSenha: String = txt_registrar_senha.text.toString()
            val textoConfirmaSenha: String = txtConfirmaSenha.text.toString()

            if (TextUtils.isEmpty(textoEmail)) {
                Toast.makeText(
                    applicationContext,
                    "Please fil in the required fields",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(textoSenha)) {
                Toast.makeText(
                    applicationContext,
                    "Please fil in the required fields",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (textoSenha.length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Password must be at least 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (!textoSenha.equals(textoConfirmaSenha)) {
                Toast.makeText(applicationContext, "password are different", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (!textoEmail.isEmpty()) {
                if (!textoSenha.isEmpty()) {
                    if (!textoConfirmaSenha.isEmpty()) {
                        if (textoSenha.equals(textoConfirmaSenha)) {

                            usuario = Usuario(textoNome, textoEmail, textoSenha, "")
                            val identificadorUsuario: String =
                                Base64Custom.codificarBase64(usuario.email)
                            usuario =
                                Usuario(textoNome, textoEmail, textoSenha, identificadorUsuario)
                            cadastrarUsuario()
                            ConfiguracaoFirebase.salvar(usuario)
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Senhas diferentes", Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Preencha o Password", Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Preencha o Password", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@RegisterActivity,
                    "Preencha o E-mail", Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    fun cadastrarUsuario() {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(usuario.email, usuario.senha).addOnCompleteListener {

            if (it.isSuccessful) {

                Toast.makeText(
                    this@RegisterActivity,
                    "Sucesso ao cadastrar usuario", Toast.LENGTH_SHORT
                ).show()
                abrirLoginUsuario()
            } else {
                var excecao = ""
                try {
                    throw it.getException()!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    excecao = "Digite uma senha mais forte"
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    excecao = "Por favor, digite um email valido"
                } catch (e: FirebaseAuthUserCollisionException) {
                    excecao = "Esta conta ja foi cadastrada"
                } catch (e: Exception) {
                    excecao = "Erro ao cadastrar usuario: " + e.message
                    e.printStackTrace()
                }

                Toast.makeText(
                    this@RegisterActivity,
                    excecao, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun abrirLoginUsuario() {
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

