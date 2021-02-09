package fr.isen.hugo.androiderestaurant

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.hugo.androiderestaurant.databinding.ActivityLoginBinding
import fr.isen.hugo.androiderestaurant.model.User
import fr.isen.hugo.androiderestaurant.model.dataResultLogin
import fr.isen.hugo.androiderestaurant.payment.demo.PaymentActivity
import org.json.JSONObject


private lateinit var binding: ActivityLoginBinding

class LoginActivity : MenuActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getSharedPrefs(applicationContext, "id_login") != "") {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }
        title = "Login"
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonSignUpLogin.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.buttonSubmitLogin.setOnClickListener {
            var user = User("", "", "", "", "")
            user.email = binding.inputEmailLogin.text.toString()
            user.password = binding.inputPasswordLogin.text.toString()

            var validation : Boolean = false
            var message = StringBuilder()
                    if(user.verifyEmail()){
                        if (user.verifyValidationPassword()){
                            validation = true
                        } else {
                            message.append("Password Invalid ! must contain 8 characters\n")
                            binding.inputPasswordLogin.setError(message)
                        }
                    } else {
                        message.append("Email Invalid ! This is not an email\n")
                        binding.inputEmailLogin.setError(message)
                    }

            if(validation){
                sendLogin(user)
            } else {
                val positiveButtonClick = { dialog: DialogInterface, which: Int ->
                    Toast.makeText(
                        applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT
                    ).show()
                }
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Invalid Information")
                builder.setMessage(message)
                builder.setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener(function = positiveButtonClick)
                )
                builder.show()
            }
        }
    }

    private fun sendLogin(user: User) {
        val postUrl = "http://test.api.catering.bluecodegames.com/" + "user" + "/" + "login"
        val requestQueue = Volley.newRequestQueue(this)
        val postData = JSONObject()
        postData.put("id_shop", "1")
        postData.put("email", user.email)
        postData.put("password", user.password)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, postUrl, postData, {
            var gson: dataResultLogin = Gson().fromJson(it.toString(), dataResultLogin::class.java)
            when {
                gson.verifyResult() -> {
                    setSharedPrefs(applicationContext, "id_login", gson.data.id.toString())
                    val intent = Intent(this, PaymentActivity::class.java)
                    startActivity(intent)
                }
                else -> Log.e("LoginActivity", "Noting found :(")
            }
        }, {
            Log.e("LoginActivity", it.toString())
        })
        requestQueue.add(jsonObjectRequest)
    }
}