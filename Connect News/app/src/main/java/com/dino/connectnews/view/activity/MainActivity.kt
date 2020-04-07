package com.dino.connectnews.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.dino.connectnews.R
import com.dino.connectnews.view.adapter.CategoryPageAdapter
import com.dino.connectnews.view.fragment.HomeFragment
import com.facebook.Profile
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private val auth = FirebaseAuth.getInstance()
    private var viewPager: ViewPager? = null
    private lateinit var tabLayout: TabLayout
    private lateinit var  currentProfile: Profile
    private var categoryPageAdapter: CategoryPageAdapter =
        CategoryPageAdapter(supportFragmentManager, criarCategorias()!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        viewPager = findViewById(R.id.viewPager)
        drawerLayout = findViewById(R.id.drawer_layout)
        tabLayout = findViewById(R.id.tabs)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)
        val headerView: View = navView.getHeaderView(0)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        viewPager?.adapter = categoryPageAdapter

        viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))

        val user = auth.currentUser
        val email: String = user?.email.toString()
        val nome = user?.displayName.toString()

        if (user != null){
            if (user.displayName != null){
                headerView.nav_nome?.text = nome
                headerView.nav_email.text = email
                Picasso.get().load(user.photoUrl).into(headerView.profile_image)
            }else{
                headerView.nav_nome?.text = getString(R.string.app_name)
                headerView.nav_email.text = email
            }
        }else{
            if (Profile.getCurrentProfile() != null){
                currentProfile = Profile.getCurrentProfile()
                val profileUrl =  currentProfile.getProfilePictureUri(200, 200).toString()
                headerView.nav_nome?.text =
                    String.format("%s " +" %s" ,currentProfile.firstName , currentProfile.lastName )
                headerView.nav_email?.text = currentProfile.id
                Picasso.get().load(profileUrl).into(headerView.profile_image)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings -> {

                if (auth.currentUser == null) {
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        when (p0.itemId) {
            R.id.nav_exit -> {
                auth.signOut()
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_myfavorites -> {
                if (auth.currentUser != null) {
                    startActivity(Intent(applicationContext, FavoritoActivity::class.java))
                } else {
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                }
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        return true
    }

    private fun criarCategorias(): List<Fragment>? {
        val fragments: ArrayList<Fragment> = ArrayList<Fragment>()
        val homeFragment = HomeFragment()
        fragments.add(homeFragment.newInstance("business"))
        fragments.add(homeFragment.newInstance("entertainment"))
        fragments.add(homeFragment.newInstance("general"))
        fragments.add(homeFragment.newInstance("health"))
        fragments.add(homeFragment.newInstance("science"))
        fragments.add(homeFragment.newInstance("sports"))
        fragments.add(homeFragment.newInstance("technology"))
        return fragments
    }
}
