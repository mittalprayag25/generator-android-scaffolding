package <%= appPackage %>.commons.preference

import android.content.Context
import android.content.SharedPreferences
import <%= appPackage %>.commons.preference.PreferenceConstants.PREFS_FILENAME


class PreferenceHelper {

    var preferences: SharedPreferences

    constructor(context: Context) {
        preferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    }

    fun edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = preferences.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     */
    operator fun set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    inline operator fun <reified T : Any> get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> preferences.getString(key, defaultValue as? String) as T?
            Int::class -> preferences.getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> preferences.getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> preferences.getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> preferences.getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}