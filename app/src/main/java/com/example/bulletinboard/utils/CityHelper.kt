package com.example.bulletinboard.utils

import android.content.Context
import com.example.bulletinboard.R
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

object CityHelper {
    fun getAllCounties(context: Context):ArrayList<String>{
        var tempArray = ArrayList<String>()
        try {

            val inputStream : InputStream = context.assets.open("countriesToCities.json")
            val size:Int = inputStream.available()
            val bytesArray = ByteArray(size)
            inputStream.read(bytesArray)
            val jsonFile = String(bytesArray)
            val jsonObject = JSONObject(jsonFile)
            val countryNames = jsonObject.names()
            if(countryNames != null){
                for (n in 0 until countryNames.length()){
                    tempArray.add(countryNames.getString(n))
                }
            }
        } catch (e:IOException){

        }
        return tempArray
    }
    fun filterListData(list : ArrayList<String>, searchText : String?) : ArrayList<String>{
        val tempList = ArrayList<String>()
        tempList.clear()
        for (selection : String in list){
            if(searchText == null){
                tempList.add("No result")
                return tempList
            }
            if(selection.toLowerCase().startsWith(searchText.toLowerCase()))
                tempList.add(selection)
            }
            if (tempList.size == 0)tempList.add("No result")
            return tempList
    }
}