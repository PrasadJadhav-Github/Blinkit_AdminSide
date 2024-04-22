package com.example.blinkit_adminside.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.blinkit_adminside.R
import com.example.blinkit_adminside.databinding.ActivityAdminMainBinding


class AdminMainActivity : AppCompatActivity() {
    private  lateinit var  binding :ActivityAdminMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(binding.bottomMenu,Navigation.findNavController(this,
            R.id.fragmentContainerView2
        ))

    }
}