package com.abhay.mishaltechnologiesassignment.listeners

interface ApiListeners {
    fun onApiCallSuccessResponse(response:String)
    fun onApiCallFailureResponse(errorText:String)
}