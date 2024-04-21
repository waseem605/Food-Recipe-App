package com.waseem.foodapp.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.waseem.foodapp.R

import com.waseem.foodapp.databinding.AppOpenDialogBoxBinding


class AppOpenDialogBox(private var mContext: Context,private var check: Int) : Dialog(mContext) {
    private lateinit var binding: AppOpenDialogBoxBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        val width = (mContext.resources.displayMetrics.widthPixels)
        val height = (mContext.resources.displayMetrics.heightPixels)
        binding = AppOpenDialogBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCancelable(false)

        try {
            window?.setLayout(width, height)
            if (check==1){
                binding.animation.setAnimation(R.raw.ads_loader_bottom)
                binding.textView3.text = context.resources.getText(R.string.welcome_back)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}