package com.abhay.mishaltechnologiesassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.abhay.mishaltechnologiesassignment.databinding.ActivityMainBinding
import com.abhay.mishaltechnologiesassignment.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        initView()
        setObserver()
    }

    private fun initView(){
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainBinding.btnPrint.setOnClickListener {
            viewModel.getApiData()
        }
    }

    private fun setObserver(){
        viewModel.successResponseLiveData.observe(this){
            PrintUtil.createAndPrintPdf(this@MainActivity,it)
        }

        viewModel.errorTextLiveData.observe(this){
            mainBinding.errorText.text = it
        }
    }

}



