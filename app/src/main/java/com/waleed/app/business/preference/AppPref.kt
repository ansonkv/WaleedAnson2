package com.waleed.app.business.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.waleed.app.R
import com.google.gson.Gson

class AppPref(val context: Context) {
    private val shadePreferenceName = context.resources.getString(R.string.app_name)

    private fun loadPref(): SharedPreferences {
        return context.getSharedPreferences(shadePreferenceName, Context.MODE_PRIVATE)
    }

    fun saveInteger(key: String, value: Int) {

        loadPref().edit {

            putInt(key, value).apply()

        }
    }

    fun getInteger(key: String): Int? {

        return loadPref().getInt(key, 0)
    }

    fun saveString(key: String, value: String) {

        loadPref().edit {

            putString(key, value).apply()

        }
    }

    fun removeString(key: String) {

    }

    fun getString(key: String): String? {

        return loadPref().getString(key, null)
    }

    fun saveObject(key: String, `object`: Any) {

        val json = Gson().toJson(`object`)

        saveString(key, json)
    }

    fun deleteObject(key: String) {
        removeString(key)
    }

    fun loadObject(key: String, returnType: Class<*>): Any? {

        val json = loadPref().getString(key, null)

        if (json != null) {

            return Gson().fromJson(json, returnType)
        }

        return null
    }


    fun saveBoolean(key: String, value: Boolean) {

        loadPref().edit {

            putBoolean(key, value)

        }
    }


    fun getBoolean(booleanFlag: String, defaultValue: Boolean): Boolean {

        val sharedPref = loadPref()

        return sharedPref.getBoolean(booleanFlag, defaultValue)

    }


    fun removeSharedPref(Key: String) {

        loadPref().edit {

            remove(Key)
        }
    }
}