package com.fastcon.etherapp.data.local


import android.content.Context.MODE_PRIVATE
import com.facebook.FacebookSdk.getApplicationContext
import com.fastcon.etherapp.data.remote.entity.TradedVolumeEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.hawk.Hawk
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*


/**
 * Storing API Key in shared preferences to
 * add it in header part of every retrofit request
 */
object PrefUtils {
    private fun getFiles(): HashMap<String, String> {
        return Hawk.get("files") ?: hashMapOf()
    }

    fun saveFile(key: String, value: String) {
        val files = getFiles()
        files[key] = value
        Hawk.put("files", files)

    }

    fun hasExpired(): Boolean {
        val tokenExpired = getExpiredDate()
        println("=======================Expired time : ${toSimpleString(tokenExpired)})")
        println("=======================Current time : ${toSimpleString(Date())}")
        if (tokenExpired > Date()) {
            println("=======================has expired : false")
            return false
        }
        println("=======================has expired : true")
        return true
    }


    private fun getExpiredDate(): Date {

        return Hawk.get("Expired_Date", Date())
    }

    fun setExpiredTime(exipires: String) {
        if (exipires.isNotEmpty()) {
            var expiredDate = "$exipires +0900"
            print("=======================Expired time== : ${expiredDate.format("yyyy-MM-dd HH:mm:ssZ")})")
            var ex = expiredDate.format("yyyy-MM-dd HH:mm:ssZ")
            Hawk.put("Expired_Date", ex)

        }
    }

    private fun clearExpiredTime() {
        Hawk.delete("Expired_Date")
    }

    private fun clearApiKey() {
        Hawk.delete("API_KEY")
    }


    fun getApiKey(): String {

        return getData("API_KEY") ?: ""
    }

    fun storeApiKey(apiKey: String) {
        saveData("API_KEY", apiKey)
    }

    fun getNewsList(): List<Any?> {

        return Hawk.get("news_list")
    }

    fun storeNewsList(list: List<Any?>) {
        Hawk.put("news_list", list)
    }


    fun setLogin(isLogin: Boolean) {
        Hawk.put("isLogin", isLogin)
    }


    fun hasLogin(): Boolean {
        return Hawk.get("isLogin", false)

    }

    fun setRequestCurrencies(currencies: String) {
        Hawk.put("currencies", currencies)
    }


    fun getRequestCurrencies(): String {
        return Hawk.get("currencies", "")

    }

    fun setRequestCrypto(currencies: String) {
        Hawk.put("crypto", currencies)
    }


    fun getRequestCrypto(): String {
        return Hawk.get("crypto", "")

    }

    fun clearRequestCrypto() {
        Hawk.delete("crypto")
    }

    fun clearRequestCurrencies() {
        Hawk.delete("currencies")
    }
    /*   fun saveUser(user: LoginResponse) {
           Hawk.put("userProfile", user)

       }

       fun getUser(): LoginResponse {
           return Hawk.get("userProfile", LoginResponse())
       }*/

    fun saveDeviceToken(device_token: String) {
        Hawk.put("device_token", device_token)
    }

    fun getDeviceToken(): String {
        return Hawk.get("device_token", "")
    }

    fun saveUserName(userName: String) {
        Hawk.put("userName", userName)
    }

    fun getUserName(): String {
        return Hawk.get("userName", "")
    }

    fun savePassword(password: String) {
        Hawk.put("password", password)
    }

    fun getPassword(): String {
        return Hawk.get("password", "")
    }

    fun saveWalletPath(path: String) {
        Hawk.put("c", path)
    }

    fun getWalletPath(): String {
        return Hawk.get("saveWalletPath", "")
    }

    fun saveArrayList(list: ArrayList<String>, key: String?) {
        val prefs = getApplicationContext().getSharedPreferences("App_List", MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()
    }

    fun getArrayList(key: String?): ArrayList<String?>? {
        val prefs = getApplicationContext().getSharedPreferences("App_List", MODE_PRIVATE)
        val gson: Gson = Gson()
        val json = prefs.getString(key, null)
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson(json, type)
    }

    private fun getData(key: String): String? {
        return Hawk.get(key, "")

    }

    private fun saveData(key: String, values: String) {
        Hawk.put(key, values)

    }

    fun setIsEnable(isEnable: Boolean) {
        Hawk.put("isEnabled", isEnable)

    }

    fun isEnable(): Boolean {
        return Hawk.get("isEnabled", false)

    }

    fun logout() {
        clearApiKey()
        setLogin(false)
    }

    private fun toSimpleString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
        return format.format(date)
    }

    fun expired() {

    }

    fun isSignedIn(): Boolean {
        return Hawk.get("loggedIn", false)
    }

    fun setSignedIn(status: Boolean) {
        Hawk.put("loggedIn", status)
    }

    fun saveTradedHistoryDetailTime(timestamp: Long?) {
        Hawk.put("traded_date", timestamp)
    }

    fun getTradedHistoryDetailTime(): Long {
        return Hawk.get("traded_date")
    }

    fun saveTradedList(tradedVolume: ArrayList<TradedVolumeEntity>) {
        Hawk.put("tradedVolume", tradedVolume)
    }

    fun getTradedList(): List<TradedVolumeEntity> {
        return Hawk.get("tradedVolume")
    }

    fun ethereumAddress(ethAddress: String?) {
        Hawk.put("ethereumAddress", ethAddress)
    }

    fun getEtherAddress(): String {
        return Hawk.get("ethereumAddress","default")
    }

    fun btcAddress(btcAddress: String?) {
        Hawk.put("bitcoinAddress", btcAddress)
    }

    fun getBitcoinAddress(): String {
        return Hawk.get("bitcoinAddress","default")
    }

    @JvmStatic
    fun saveBitKey(privateKey: String) {
        Hawk.put("bitKey", privateKey)
    }

    fun getBitKey(): String {
        return Hawk.get("bitKey","default")
    }

    @JvmStatic
    fun saveEthKey(privateKey: String) {
        Hawk.put("ethKey",privateKey)
    }

    fun getEthKey():String{
        return Hawk.get("ethKey","default")
    }
}