package fr.isen.hugo.androiderestaurant

import android.content.Context
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import fr.isen.hugo.androiderestaurant.model.Cart
import java.io.*

fun cartWriteToFile(cart: Cart, context: Context) {
    try {
        val json = Gson().toJson(cart)
        val cipher = encrypt(context, json)

        val outputStreamWriter =
            OutputStreamWriter(context.openFileOutput("cart.txt", Context.MODE_PRIVATE))
        outputStreamWriter.write(Base64.encodeToString(cipher, Base64.DEFAULT))
        outputStreamWriter.close()
    } catch (e: IOException) {
        Log.e("Exception", "File write failed: " + e.toString())
    }
}


fun cartReadFromFile(context: Context): Cart {
    var ret = ""
    var cart = Cart(mutableListOf(),null, null)
    try {
        val inputStream: InputStream = context.openFileInput("cart.txt")
        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var receiveString: String? = ""
            val stringBuilder = StringBuilder()
            while (bufferedReader.readLine().also { receiveString = it } != null) {
                stringBuilder.append(receiveString)
            }
            inputStream.close()
            ret = stringBuilder.toString()
            val cipher = Base64.decode(ret, Base64.DEFAULT)
            val message = decrypt(context, cipher)
            cart = Gson().fromJson(message, Cart::class.java)
        }
    } catch (e: FileNotFoundException) {
        Log.e("login activity", "File not found: " + e.toString())
    } catch (e: IOException) {
        Log.e("login activity", "Can not read file: " + e.toString())
    }
    return cart
}