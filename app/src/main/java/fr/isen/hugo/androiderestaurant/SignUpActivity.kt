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
import org.json.JSONObject
import java.lang.StringBuilder

private lateinit var binding: ActivitySignUpBinding

class SignUpActivity : MenuActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        title = "Sign Up"
        var user = User("","","", "", "")
        setContentView(R.layout.activity_sign_up)

        binding.buttonSubmitSignUp.setOnClickListener {
            user.firstname = binding.inputFirstName.text.toString()
            user.lastname = binding.inputLastName.text.toString()
            user.email = binding.inputEmail.text.toString()
            user.password = binding.inputPassword.text.toString()
            user.address = binding.inputAddress.text.toString()

            val positiveButtonClick = { dialog: DialogInterface, which: Int ->
                Toast.makeText(applicationContext,
                    android.R.string.yes, Toast.LENGTH_SHORT).show()
            }

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Androidly Alert")
            builder.setMessage("We have a message")
            builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))

            var validation : Boolean = false
            var message = StringBuilder()
            if(user.verifyFirstname()){
                if (user.verifyLastname()){
                    if(user.verifyEmail()){
                        if (user.verifyPassword()){
                            validation = true
                        } else {
                            message.append("Password Invalid ! must contain 8 characters\n")
                        }
                    } else {
                        message.append("Email Invalid ! This is not an email\n")
                    }
                } else {
                    message.append("Last name Invalid ! This is not a name\n")
                }
            } else {
                message.append("First name Invalid ! This is not a name\n")
            }

            if(validation){
                sendSignUp(user)
            } else {
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
           /*val gson: dataResult = Gson().fromJson(it.toString(), dataResult::class.java)
            gson.data.firstOrNull { it.name == category }?.dishes?.let { dishes ->
                displayDishes(dishes)
            } ?: run {
                Log.e("CategoryListActivity", "Noting found :(")
                /*
                binding.categoryLoading.visibility = View.GONE
                binding.categoryErrorMessage.text = "Nothing found :("
                 */
            }*/
        },{
            Log.e("CategoryListActivity",  it.toString())
        })
        requestQueue.add(jsonObjectRequest)
    }
}