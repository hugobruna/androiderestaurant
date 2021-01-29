package fr.isen.hugo.androiderestaurant

import android.R.attr
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import fr.isen.hugo.androiderestaurant.model.Cart
import fr.isen.hugo.androiderestaurant.model.dataResult
import org.json.JSONObject
import java.io.*
import javax.crypto.Cipher
import javax.crypto.SecretKey


class CartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
    }

}