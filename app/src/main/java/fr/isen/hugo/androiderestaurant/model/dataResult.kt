package fr.isen.hugo.androiderestaurant.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class dataResult(@SerializedName("data") val data: List<Category>):Serializable
