package fr.isen.hugo.androiderestaurant.model

import com.google.gson.annotations.SerializedName

data class Category (@SerializedName("name_en") val name: String, @SerializedName("items") val dishes: List<Dish>)