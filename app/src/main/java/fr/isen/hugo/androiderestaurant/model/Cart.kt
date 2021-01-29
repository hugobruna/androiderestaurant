package fr.isen.hugo.androiderestaurant.model

data class Cart(var itemCarts: MutableList<ItemCart>, val user: User?, val delivery: Delivery?){

}