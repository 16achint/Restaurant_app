package com.example.intershalaassignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.intershalaassignment.R
import com.example.intershalaassignment.fragment.FAQsFragment
import com.example.intershalaassignment.fragment.FavouritesRestaurantFragment
import com.example.intershalaassignment.fragment.DashboardFragment
import com.example.intershalaassignment.fragment.MyProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigation: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.drawer_layout, WelcomeFragment()) // Replace "FirstFragment" with the actual class name of your first fragment
//                .commit()
//        }

        title = "Find Restaurants"
        drawerLayout = findViewById(R.id.drawer_layout)
//        drawerLayout2 = findViewById(R.id.drawer_layout2)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigation = findViewById(R.id.navigationView)

        setUpToolBar()
        openHome()
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_Drawer,
            R.string.close_Drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigation.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    openHome()
                }
                R.id.my_profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame,MyProfileFragment()).commit()
                    supportActionBar?.title = "Profile"
                }
                R.id.favourite_restaurants -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame,FavouritesRestaurantFragment()).commit()
                    supportActionBar?.title = "Favourites Restaurants"
                }
                R.id.faqs -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame,FAQsFragment()).commit()
                    supportActionBar?.title = "FAQs"
                }
                R.id.setting -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.frame,MyProfileFragment()).commit()
//                    supportActionBar?.title = "Settings"
                    Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home,menu)
        return super.onCreateOptionsMenu(menu)
    }
        private fun openHome() {
            val fragment = DashboardFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame, fragment).commit()
            supportActionBar?.title = "Home"
            navigation.setCheckedItem(R.id.home)
        }

        override fun onBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.END))
                drawerLayout.closeDrawer(GravityCompat.END)
//            else if(drawerLayout2.visibility == View.GONE){
//                startActivity(Intent(this,MainActivity::class.java))
//                finish()
//            }
            else {
            val frag = supportFragmentManager.findFragmentById(R.id.frame)
            when (frag) {
                !is DashboardFragment -> openHome()
                else -> super.onBackPressed()
                }
            }
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.END)
            R.id.action_add_card -> {
                val intent = Intent(this,CartActivity::class.java)
                startActivity(intent)
                supportActionBar?.title = "Add to Cart"
            }
            R.id.action_sort -> {
                Toast.makeText(this,"Click item",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}