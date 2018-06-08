package me.developer.yotharit.foodstorystock.manager

import android.content.Context

class Contextor private constructor(context: Context) {

    var mContext: Context

    init {
        mContext = context
    }

    companion object {

        var instance: Contextor? = null

        fun initInstance(context: Context): Contextor {
            if (instance == null)
                instance = Contextor(context)
            return instance!!
        }

    }
}