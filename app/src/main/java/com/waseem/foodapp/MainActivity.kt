package com.waseem.foodapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.waseem.foodapp.databinding.ActivityMainBinding
import com.waseem.foodapp.databinding.ExitDialogBinding
import com.waseem.foodapp.utils.Constants
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    //https://www.themealdb.com/api.php
    //www.themealdb.com/api/json/v1/1/random.php


    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetExitDialog: BottomSheetDialog
    private lateinit var exitDialogBinding: ExitDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        initExitDialog()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
       val  mNavController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(mNavController)
        binding.bottomNavView.itemIconTintList = null





        binding.applyButton.setOnClickListener {
            Constants.interstitialDialog(this, adLoadCallback = {

            })
        }

    }
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exitDialog()

            }
        }

    private fun initExitDialog() {
        runCatching {
            bottomSheetExitDialog = BottomSheetDialog(this)
            exitDialogBinding = ExitDialogBinding.inflate(layoutInflater)
            bottomSheetExitDialog.setContentView(exitDialogBinding.root)


        }
    }

    private fun exitDialog() {
        kotlin.runCatching {
            bottomSheetExitDialog.show()
            exitDialogBinding.buttonCancel.setOnClickListener {
                bottomSheetExitDialog.dismiss()
            }

            exitDialogBinding.buttonYes.setOnClickListener {
                bottomSheetExitDialog.dismiss()
                finishAffinity()
                finish()
                exitProcess(1)
            }
        }
    }
}