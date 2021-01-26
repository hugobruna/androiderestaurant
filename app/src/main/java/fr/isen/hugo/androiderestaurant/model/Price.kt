package fr.isen.hugo.androiderestaurant.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Price (@SerializedName("price") val unit_price : String, @SerializedName("size") val size : String):Serializable

