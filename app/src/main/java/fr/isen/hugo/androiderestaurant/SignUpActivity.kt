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
import fr.isen.hugo.androiderestaurant.databinding.ActivityListItemCartBinding
import fr.isen.hugo.androiderestaurant.databinding.ActivitySignUpBinding
import fr.isen.hugo.androiderestaurant.model.User
import fr.isen.hugo.androiderestaurant.model.dataResult
import fr.isen.hugo.androiderestaurant.model.dataResultLogin
import fr.isen.hugo.androiderestaurant.payment.demo.PaymentActivity
import org.json.JSONObject
import java.lang.StringBuilder

private lateinit var binding: ActivitySignUpBinding

class SignUpActivity : MenuActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        title = "Sign Up"
        setContentView(binding.root)

        binding.buttonSubmitSignUp.setOnClickListener {
            var user = User("","","", "", "")
            user.firstname = binding.inputFirstName.text.toString()
            user.lastname = binding.inputLastName.text.toString()
            user.email = binding.inputEmail.text.toString()
            user.password = binding.inputPassword.text.toString()
            user.address = binding.inputAddress.text.toString()

            var validation : Boolean = false
            var message = StringBuilder()
            if(user.verifyFirstname()){
                if (user.verifyLastname()){
                    if(user.verifyEmail()){
                        if (user.verifyValidationPassword()){
                            validation = true
                        } else {
                            message.append("Password Invalid ! must contain 8 characters\n")
                            binding.inputPassword.setError(message)
                        }
                    } else {
                        message.append("Email Invalid ! This is not an email\n")
                        binding.inputEmail.setError(message)
                    }
                } else {
                    message.append("Last name Invalid ! This is not a name\n")
                    binding.inputLastName.setError(message)
                }
            } else {
                message.append("First name Invalid ! This is not a name\n")
                binding.inputFirstName.setError(message)
            }

            if(validation){
                sendSignUp(user)
            } else {
                val positiveButtonClick = { dialog: DialogInterface, which: Int ->
                    Toast.makeText(applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT).show()
                }

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Invalid Information")
                builder.setMessage(message)
                builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
                builder.show()
            }

        }
    }

    private fun sendSignUp(user: User) {
        val postUrl = "http://test.api.catering.bluecodegames.com/" + "user" + "/" + "register"
        val requestQueue = Volley.newRequestQueue(this)
        val postData = JSONObject()
        postData.put("id_shop", "1")
        postData.put("firstname", user.firstname)
        postData.put("lastname", user.lastname)
        postData.put("address", user.address)
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
                else -> Log.e("SignUpActivity", "Noting found :(")
            }
        },{
            Log.e("SignUptActivity",  it.toString())
        })
        requestQueue.add(jsonObjectRequest)
    }
}