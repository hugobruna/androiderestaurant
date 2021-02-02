package fr.isen.hugo.androiderestaurant.model

import org.mindrot.jbcrypt.BCrypt


data class User(var lastname: String, var firstname: String, var address: String, var email: String, var password: String) {
    fun verifyLastname() :Boolean{
       return  (lastname.isNotEmpty()) && (lastname.isNotBlank()) && (isLetters(lastname))
    }

    fun verifyFirstname() :Boolean{
        return  (firstname.isNotEmpty()) && (firstname.isNotBlank()) && (isLetters(firstname))
    }

    fun verifyEmail() :Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun verifyValidationPassword() :Boolean{
        return password.length == 8
    }

    fun hashedPassword() :String {
        return BCrypt.hashpw(password, BCrypt.gensalt(12))
    }

    fun verifyPassword(hashed: String) :Boolean{
        return BCrypt.checkpw(password, hashed)
    }

    private fun isLetters(string: String): Boolean {
        for (c in string) {
            if (c !in 'A'..'Z' && c !in 'a'..'z') {
                return false
            }
        }
        return true
    }
}