package com.android.base.utils

class StringUtil{

    companion object {

        fun  hidmiddleStr(str:String):String{

            return str.substring(0, 3) + "****" + str.substring(str.length-7, str.length-4)

        }

    }

}
