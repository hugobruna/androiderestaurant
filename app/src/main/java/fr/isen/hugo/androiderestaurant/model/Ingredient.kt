package fr.isen.hugo.androiderestaurant.model

import com.google.gson.annotations.SerializedName

data class Ingredient(@SerializedName("name_en") val name: String)