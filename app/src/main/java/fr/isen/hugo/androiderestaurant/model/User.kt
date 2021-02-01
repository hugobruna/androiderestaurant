package fr.isen.hugo.androiderestaurant.model


data class User(var lastname: String, var firstname: String, var address: String, var email: String, var password: String) {
    fun verifyLastname() :Boolean{
        return lastname.filter {  it in 'A'..'Z' || it in 'a'..'z' }.length == lastname.length
    }

    fun verifyFirstname() :Boolean{
        return firstname.filter { it in 'A'..'Z' || it in 'a'..'z' }.length == lastname.length
    }

    fun verifyEmail() :Boolean{
        return email.matches(("[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+").toRegex())
    }

    fun verifyPassword() :Boolean{
        return password.length == 8
    }
}