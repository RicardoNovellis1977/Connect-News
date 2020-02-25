package com.dino.connectnews.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.dino.connectnews.R
import com.dino.connectnews.data.network.ConfiguracaoFirebase
import com.dino.connectnews.data.model.Usuario
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.util.*


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener,
    View.OnClickListener {

    private lateinit var auth: FirebaseAuth//
    private var TAG: String = "SignInActivity"
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var RC_SIGN_IN: Int = 9001
    private lateinit var loginButton: LoginButton
    private lateinit var callbackManager: CallbackManager
    lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val textRegister: TextView = textRegisterNow
        val buttonLogin: Button = button_login
        auth = FirebaseAuth.getInstance()

        toolbar = findViewById(R.id.toolbarLogin)
        toolbar.navigationIcon = ContextCompat.getDrawable(this,R.drawable.abc_ic_ab_back_mtrl_am_alpha)

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    MainActivity::class.java
                )
            )
        })

        buttonLogin.setOnClickListener {

            val textoEmail: String = edt_email.text.toString()
            val textoSenha: String = edt_password.text.toString()

            if (!textoEmail.isEmpty()) {
                if (!textoSenha.isEmpty()) {
                    validarLogin()
                } else {
                    Toast.makeText(applicationContext, "Preencha sua Senha ", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(applicationContext, "Preencha seu Email ", Toast.LENGTH_LONG).show()
            }
        }

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        loginButton = findViewById<View>(R.id.login_button) as LoginButton
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"))

        callbackManager = CallbackManager.Factory.create()

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)

                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {
            }

        })


        textRegister.setOnClickListener {

            startActivity(Intent(applicationContext, RegisterActivity::class.java))
            finish()
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        sign_in_button.setOnClickListener(this)

    }

    private fun validarLogin() {

        auth.signInWithEmailAndPassword(edt_email.text.toString(), edt_password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser

                    abrirHome()
                    finish()
                    // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //updateUI(null)
                }
            }
    }

    fun abrirHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun signIn() {
        val signInIntent: Intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        if (auth.currentUser == null) {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithCredential:success")

                        val user = auth.currentUser
                        val email: String = user?.email.toString()
//                        val id: String = user?.uid.toString()
                        val nome = user?.displayName.toString()
//                        val favoritos : ArrayList<Article>? = usuario.favoritos
//                        usuario = Usuario(nome, email, "Google", id, favoritos)
//
//                        nav_email.text = email
//                        nav_nome.text = nome
//                        if (user?.uid != usuario.id){
//                            ConfiguracaoFirebase.salvar(usuario)
//                        }

                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                        // updateUI(user)

                        // Sign in success, update UI with the signed-in user's information
                    } else {
                        // If sign in fails, display a message to the user.
                        // Log.w(TAG, "signInWithCredential:failure", task.exception)
                        // Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                        // updateUI(null)
                    }

                }
        }else{
            Toast.makeText(applicationContext, "Deu Ruim o Login", Toast.LENGTH_LONG).show()

        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(applicationContext, "Deu Ruim", Toast.LENGTH_LONG).show()
    }

    override fun onClick(v: View?) {
        signIn()
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // updateUI(null)
                }
            }
    }

    private fun getUserProfile() {
        val profileTracker: ProfileTracker = object : ProfileTracker() {
            override fun onCurrentProfileChanged(
                oldProfile: Profile,
                currentProfile: Profile
            ) { // Agora que temos o token do usuario podemos requisitar os dados
                Profile.fetchProfileForCurrentAccessToken()
                if (currentProfile != null) {
                    val fbUserId = currentProfile.id
                    val profileName = currentProfile.name
                    //val profileUrl =  currentProfile.getProfilePictureUri(200, 200).toString()
                    val usuario : Usuario = Usuario(profileName,"","",fbUserId)
                    ConfiguracaoFirebase.salvar(usuario)
                    Log.d(
                        "FB profile",
                        "got new/updated profile from thread $fbUserId"
                    )
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("nome", profileName)
                    //intent.putExtra("image", profileUrl)
                    intent.putExtra("id", fbUserId )
                    startActivity(intent)
                }
            }
        }
        profileTracker.startTracking()
    }

}
