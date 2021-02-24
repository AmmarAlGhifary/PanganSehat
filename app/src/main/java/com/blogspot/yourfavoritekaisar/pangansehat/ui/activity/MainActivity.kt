@file:Suppress("DEPRECATION")

package com.blogspot.yourfavoritekaisar.pangansehat.ui.activity

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.blogspot.yourfavoritekaisar.pangansehat.R
import com.blogspot.yourfavoritekaisar.pangansehat.ui.fragment.CartFragment
import com.blogspot.yourfavoritekaisar.pangansehat.ui.fragment.ChatFragment
import com.blogspot.yourfavoritekaisar.pangansehat.ui.fragment.HomeFragment
import com.blogspot.yourfavoritekaisar.pangansehat.ui.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(HomeFragment())
    }


    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    moveToFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bottom_cart -> {
                    moveToFragment(CartFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bottom_profile -> {
                    moveToFragment(ProfileFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bottom_chat -> {
                    moveToFragment(ChatFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.container, fragment)
        fragmentTrans.commit()
    }

    override fun onBackPressed() {
        if (navController.graph.startDestination == navController.currentDestination?.id) {
            if (backPressedOnce) {
                super.onBackPressed()
                return
            }

            backPressedOnce = true
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler().postDelayed(2000) {
                backPressedOnce = false
            }
        } else {
            super.onBackPressed()
        }
    }
}