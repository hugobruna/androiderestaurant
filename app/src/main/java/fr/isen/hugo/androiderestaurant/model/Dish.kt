package fr.isen.hugo.androiderestaurant.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.net.URL

data class Dish (@SerializedName("name_en") val name: String, @SerializedName("ingredients") val ingredients: List<Ingredient>, @SerializedName("prices") val prices : List<Price>, @SerializedName("images") val images : List<String>): Serializable {

    fun getPrice() = prices[0].unit_price.toDouble()
    fun getFormattedPrice() = prices[0].unit_price + "€"
    fun getFirstImage() = if (images.isNotEmpty() && images[0].isNotEmpty()){
        images[0]
    } else {
        null
    }

    fun getAllImages() = if (images.isNotEmpty() && images.any {it.isNotEmpty()}){
        images.filter { it.isNotEmpty() }
    } else {
        null
    }
}