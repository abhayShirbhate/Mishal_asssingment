package com.abhay.mishaltechnologiesassignment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abhay.mishaltechnologiesassignment.listeners.ApiListeners
import com.abhay.mishaltechnologiesassignment.repository.Repository
import com.abhay.mishaltechnologiesassignment.repository.RepositoryImpl
import com.abhay.mishaltechnologiesassignment.retrofit.model.ApiCreds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel(),ApiListeners{
    private val repository : Repository = RepositoryImpl()

    val successResponseLiveData = MutableLiveData<String>()
    val errorTextLiveData = MutableLiveData<String>()

    fun getApiData(){
        CoroutineScope(Dispatchers.IO).launch{
            repository.getApiData(this@MainViewModel, ApiCreds("api","test"))
        }
    }

    override fun onApiCallSuccessResponse(response: String) {
        successResponseLiveData.postValue(response)
        errorTextLiveData.postValue("Success \n Data: $response")

    }

    override fun onApiCallFailureResponse(errorText: String) {
        errorTextLiveData.postValue(errorText)
    }
}