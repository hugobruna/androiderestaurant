package fr.isen.hugo.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.models.GooglePaymentRequest
import com.braintreepayments.api.models.PaymentMethodNonce
import com.google.gson.Gson
import fr.isen.hugo.androiderestaurant.databinding.ActivityPaymentBinding
import fr.isen.hugo.androiderestaurant.model.Cart
import fr.isen.hugo.androiderestaurant.model.dataResultCode
import org.json.JSONObject

private lateinit var binding: ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        invalidateOptionsMenu()
        title = "Payment"
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonPayment.setOnClickListener {
            sendOrder()
        }
    }

    private fun sendOrder(){
        val postUrl = "http://test.api.catering.bluecodegames.com/" + "user" + "/" + "order"
        val requestQueue = Volley.newRequestQueue(this)
        val postData = JSONObject()
        postData.put("id_shop", "1")
        postData.put("id_user", getSharedPrefs(applicationContext, "id_login"))
        postData.put("msg", Gson().toJson(cartReadFromFile(applicationContext)))
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, postUrl, postData, {
            Log.i("PaymentActivity", it.toString())
            var gson: dataResultCode = Gson().fromJson(it.toString(), dataResultCode::class.java)
            when {
                gson.verifyResult() -> {
                    cartWriteToFile(Cart(mutableListOf(), null, null), applicationContext)
                    setSharedPrefs(applicationContext, "cart_item_number", "0")
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                else -> Log.e("PaymentActivity", "Noting found :(")
            }
        }, {
            Log.e("LoginActivity", it.toString())
        })
        requestQueue.add(jsonObjectRequest)
    }
}