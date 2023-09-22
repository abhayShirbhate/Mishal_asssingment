package com.abhay.mishaltechnologiesassignment.repository

import com.abhay.mishaltechnologiesassignment.listeners.ApiListeners
import com.abhay.mishaltechnologiesassignment.retrofit.model.ApiCreds

interface Repository {
    fun getApiData(listeners: ApiListeners,apiCreds: ApiCreds)
}