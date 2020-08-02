package com.fastcon.etherapp.data.local


import android.content.Context.MODE_PRIVATE
import com.facebook.FacebookSdk.getApplicationContext
import com.fastcon.etherapp.data.remote.entity.TradedVolumeEntity
import com.fastcon.etherapp.data.remote.model.BitcoinResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.hawk.Hawk
import org.web3j.abi.datatypes.Bool
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*


/**
 * Storing API Key in shared preferences to
 * add it in header part of every retrofit request
 */
object PrefUtils {

    fun getNewsList(): List<Any?> {

        return Hawk.get("news_list")
    }

    fun storeNewsList(list: List<Any?>) {
        Hawk.put("news_list", list)
    }


    fun saveDeviceToken(device_token: String) {
        Hawk.put("device_token", device_token)
    }

    @JvmStatic
    fun getDeviceToken(): String {
        return Hawk.get("device_token", "")
    }

    fun clearDeviceToken() {
         Hawk.delete("device_token")
    }

    fun saveUserName(userName: String) {
        Hawk.put("userName", userName)
    }

    fun getUserName(): String {
        return Hawk.get("userName", "")
    }

    fun clearUserName(){
        Hawk.delete("userName")
    }
     private fun getData(key: String): String? {
        return Hawk.get(key, "")

    }

    private fun saveData(key: String, values: String) {
        Hawk.put(key, values)

    }

    private fun toSimpleString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
        return format.format(date)
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

    fun saveEthereumAddress(ethAddress: String?) {
        Hawk.put("ethereumAddress", ethAddress)
    }

    @JvmStatic
    fun getEtherAddress(): String {
        return Hawk.get("ethereumAddress", "default")
    }

    @JvmStatic
    fun clearEtherAddress() {
        Hawk.delete("ethereumAddress")
    }


    fun saveBtcAddress(btcAddress: String?) {
        Hawk.put("bitcoinAddress", btcAddress)
    }
    @JvmStatic
    fun getBitcoinAddress(): String {
        return Hawk.get("bitcoinAddress", "default")
    }


    @JvmStatic
    fun clearBitcoinAddress() {
        Hawk.delete("ethereumAddress")
    }

    @JvmStatic
    fun saveBitKey(privateKey: String) {
        Hawk.put("bitKey", privateKey)
    }

    fun getBitKey(): String {
        return Hawk.get("bitKey", "default")
    }

    @JvmStatic
    fun clearBTCKey() {
        Hawk.delete("bitKey")
    }

    @JvmStatic
    fun saveEthKey(privateKey: String) {
        Hawk.put("ethKey", privateKey)
    }

    @JvmStatic
    fun getEthKey(): String {
        return Hawk.get("ethKey", "default")
    }

    @JvmStatic
    fun clearEthKey() {
        Hawk.delete("ethKey")
    }

    fun saveBitcoinInputDetails(inputs: List<BitcoinResponse.Tx.Input>) {
        Hawk.put("btc_in_data", inputs)
    }

    fun getBitcoinInputDetails(): List<BitcoinResponse.Tx.Input> {
        return Hawk.get("btc_in_data")
    }

    fun saveBitcoinOutputDetails(out: List<BitcoinResponse.Tx.Out>) {
        Hawk.put("btc_out_data", out)
    }

    fun getBitcoinOutputDetails(): List<BitcoinResponse.Tx.Out> {
        return Hawk.get("btc_out_data")
    }

    fun setEmail(email: String) {
        Hawk.put("email", email)
    }

    fun getEmail(): String {
        return Hawk.get("email", "default")
    }

    fun getReceiverToken():String {
        return Hawk.get("receiverToken","default")
    }

    @JvmStatic
    fun saveReceiversToken(Token: String) {
        Hawk.put("receiverToken",Token)
    }

    fun clearReceiverToken() {
        Hawk.delete("receiverToken")
    }





    fun saveReceiverEmail(receiverEmailText: String) {
        Hawk.put("receiver_email",receiverEmailText)
    }
    @JvmStatic
    fun setCurrencyDisplayID(i: Int) {
        Hawk.put("currency_display_id",i);
    }
    @JvmStatic
    fun getCurrencyDisplayID():Int {
        return Hawk.get("currency_display_id");
    }

    fun setEnableBio(enable: Boolean){
        Hawk.put("enable_bio",enable);
    }

    fun getEnableBio():Boolean{
        return Hawk.get("enable_bio",false);
    }
    fun clearEnableBio() {
        Hawk.delete("enable_bio")
    }
}