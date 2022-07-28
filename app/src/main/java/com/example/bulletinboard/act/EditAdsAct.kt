package com.example.bulletinboard.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.bulletinboard.R
import com.example.bulletinboard.databinding.ActivityEditAdsBinding
import com.example.bulletinboard.databinding.ActivityMainBinding
import com.example.bulletinboard.dialogs.DialogSpinnerHelper
import com.example.bulletinboard.utils.CityHelper

class EditAdsAct : AppCompatActivity() {
    private lateinit var rootElement: ActivityEditAdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        val listCountry = CityHelper.getAllCounties(this)
        val dialog = DialogSpinnerHelper()
        dialog.showSpinnerDialog(this, listCountry)

    }
}