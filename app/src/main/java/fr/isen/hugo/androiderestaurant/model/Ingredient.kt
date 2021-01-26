package fr.isen.hugo.androiderestaurant.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Ingredient(@SerializedName("name_en") val name: String):Serializable