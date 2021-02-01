package fr.isen.hugo.androiderestaurant

import android.content.Intent
import android.os.Bundle
import fr.isen.hugo.androiderestaurant.databinding.ActivityLoginBinding

private lateinit var binding: ActivityLoginBinding

class LoginActivity : MenuActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Login"
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonSignUpLogin.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}