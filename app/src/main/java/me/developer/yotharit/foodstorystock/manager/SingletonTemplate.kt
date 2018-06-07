package me.developer.yotharit.foodstorystock.manager

class Singleton private constructor() {



    init {

    }

    private object Holder { val INSTANCE = Singleton() }

    companion object {
        val instance: Singleton by lazy { Holder.INSTANCE }
    }
    var b:String? = null
}