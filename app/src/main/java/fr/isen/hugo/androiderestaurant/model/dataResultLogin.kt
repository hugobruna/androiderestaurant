package fr.isen.hugo.androiderestaurant.model

import com.google.gson.annotations.SerializedName

data class dataResultLogin(@SerializedName("data") val data: UserId, @SerializedName("code") val code: Int) {
    fun verifyResult() :Boolean{
        return code == 200
    }
}