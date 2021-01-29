package fr.isen.hugo.androiderestaurant.model

data class Cart(private val itemCarts: List<ItemCart>, private val user: User, private val delivery: Delivery)