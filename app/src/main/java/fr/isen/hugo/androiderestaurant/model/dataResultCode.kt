package fr.isen.hugo.androiderestaurant.model

import com.google.gson.annotations.SerializedName

data class dataResultCode(@SerializedName("code") val code: Int) {
    fun verifyResult() :Boolean{
        return code == 200
    }
}