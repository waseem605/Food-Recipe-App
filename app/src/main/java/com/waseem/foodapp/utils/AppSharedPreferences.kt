package com.waseem.foodapp.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AppSharedPreferences {
    private var preferencesName: String = "App_Preferences"

    private const val Selected_Model = "Selected_Model"
    private const val KEY_SAVED_LIST = "savedListKey"
    private const val isOnboardingShown = "isOnboardingShown"
    const val languageKey = "languageKey"
    const val languageByNameKey = "languageByNameKey"
    const val IS_FLASH_ACTIVE = "IS_FLASH_ACTIVE"
    const val IS_VIBRATE_ACTIVE = "IS_VIBRATE_ACTIVE"

    private val gson = Gson()

    fun Context.setBool(key: String, flag: Boolean) {
        val sharedPreference = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(key, flag)
        editor.apply()
    }
    fun Context.getBool(key: String): Boolean {
        val sharedPreference = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreference?.getBoolean(key, false) ?: false
    }

    fun Context.setSubscriptionBool(key: String, flag: Boolean) {
        val sharedPreference = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(key, flag)
        editor.apply()
    }

    fun Context.getSubscriptionBool(key: String): Boolean {
        val sharedPreference = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreference?.getBoolean(key, false) ?: false
    }


    fun Context.getBoolDownload(key: String): Boolean {
        val sharedPreference = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreference?.getBoolean(key, false) ?: false
    }

    fun saveStringPrice(context: Context, Key: String, value: String) {
        val sharedPreference = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(Key, value)
        editor.apply()
    }

    fun Context.getStringPrice( Key: String): String {
        val sharedPreference = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreference.getString(Key, "").toString()
    }



    fun getString(context: Context, key: String): String {
        val sharedPreference = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreference.getString(key, "").toString()
    }
/*
    private fun saveSavedList(context: Context, savedList: ArrayList<SavedListModel>) {
        val sharedPreference = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val json = gson.toJson(savedList)
        editor.putString(KEY_SAVED_LIST, json)
        editor.apply()
    }

    fun updateSavedList(context: Context, model: SavedListModel) {
        val currentList = getSavedList(context)
        currentList.add(0, model)
        saveSavedList(context, currentList)
    }

    fun deleteSavedListItem(context: Context, model: SavedListModel):String {
        val currentList = getSavedList(context)
        val msg = if (currentList.contains(model)){
            currentList.remove(model)
            "Delete Successfully"
        }else{
            "item not found"
        }
        saveSavedList(context, currentList)
        return msg
    }
    fun getSavedList(context: Context): ArrayList<SavedListModel> {
        val sharedPreference = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val json = sharedPreference.getString(KEY_SAVED_LIST, null)
        val type = object : TypeToken<ArrayList<SavedListModel>>() {}.type

        return gson.fromJson(json, type) ?: ArrayList()
    }


    fun Context.saveSelectedItem( user: SavedListModel) {
        val sharedPreference = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val userJson = gson.toJson(user)
        sharedPreference.edit().putString(Selected_Model, userJson).apply()
    }

    fun Context.getSelectedItem(): SavedListModel? {
        val sharedPreference = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val userJson = sharedPreference.getString(Selected_Model, null)
        return if (userJson != null) {
            gson.fromJson(userJson, SavedListModel::class.java)
        } else {
            null
        }
    }
*/
    fun Context.saveStringValue(key: String, value: String) {
        val sharedPreference = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun Context.getLanguageName(key: String): String {
        val sharedPreference = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreference.getString(key, "").toString()
    }

    fun Context.getLanguage(Key: String): String {
        val sharedPreference = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreference.getString(Key, "en").toString()
    }
    fun Context.isOnboardingShown(): Boolean {
        val sharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(isOnboardingShown, false)
    }
    fun Context.markOnboardingAsShown() {
        val sharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(isOnboardingShown, true)
        editor.apply()
    }
}