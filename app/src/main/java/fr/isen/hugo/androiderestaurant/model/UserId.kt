package fr.isen.hugo.androiderestaurant.model

import com.google.gson.annotations.SerializedName
import org.mindrot.jbcrypt.BCrypt

data class UserId(@SerializedName("id") val id: Int, @SerializedName("code") val code: String) {
    fun getToken() :String{
        return BCrypt.hashpw("$id$$code", BCrypt.gensalt(5))
    }
}