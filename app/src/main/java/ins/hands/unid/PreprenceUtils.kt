package ins.hands.unid

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
    private var editor: SharedPreferences.Editor = prefs.edit()

    fun getStringArray(key: String): MutableList<String> {
        val gson = Gson()
        val arrayType = object : TypeToken<MutableList<String>>() {}.type
        val json = prefs.getString(key, null)
        return if (json != null) gson.fromJson(json, arrayType)
        else ArrayList()
    }

    fun setStringArray(key: String, values: MutableList<String>) {
        val gson = Gson()
        val str = gson.toJson(values)
        if (values.isNotEmpty()) {
            editor.putString(key, str)
        } else {
            editor.putString(key, null)
        }
        editor.apply()
    }

    fun getString(key: String, defValue: String?): String? {
        return prefs.getString(key, defValue)
    }

    fun setString(key: String, str: String?) {
        prefs.edit().putString(key, str).apply()
    }

    fun getInt(key: String, int: Int): Int {
        return prefs.getInt(key, int)
    }

    fun setInt(key: String, int: Int) {
        prefs.edit().putInt(key, int).apply()
    }

    fun getBoolean(key: String, boolean: Boolean): Boolean {
        return prefs.getBoolean(key, boolean)
    }

    fun setBoolean(key: String, boolean: Boolean) {
        prefs.edit().putBoolean(key, boolean).apply()
    }

    fun clear() {
        editor.clear().apply()
    }

    fun remove(key: String) {
        editor.remove(key).apply()
    }
}