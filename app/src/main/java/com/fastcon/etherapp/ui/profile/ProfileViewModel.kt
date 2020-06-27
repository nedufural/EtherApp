package com.fastcon.etherapp.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fastcon.etherapp.data.DataManager
import com.fastcon.etherapp.data.remote.entity.BitcoinBalanceResponse
import com.fastcon.etherapp.service.ApiService
import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jService
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal


class ProfileViewModel : ViewModel() {
    /**
     * https://kauri.io/manage-an-ethereum-account-with-java-and-web3j/925d923e12c543da9a0a3e617be963b4/a
     * */

    val bitcoinBalanceLiveData: MutableLiveData<ArrayList<BitcoinBalanceResponse>> = MutableLiveData()
    val _bitcoinBalanceLiveData: MutableLiveData<String> = MutableLiveData()
    val bitcoinBalanceErrorMsg: MutableLiveData<String> = MutableLiveData()
    var errorMsg: String = "error"

    var ethBalance : MutableLiveData<BigDecimal?>? = MutableLiveData()

    var web3: Web3j? = Web3j.build(HttpService("https://rinkeby.infura.io/v3/b6a2befbf40043a5b25bde109d3037ea"))
    fun getETHBalance(ethAccount:String){
        val balanceWei: EthGetBalance = web3?.ethGetBalance(
            ethAccount,
            DefaultBlockParameterName.LATEST)!!.send()
        println("balance in wei: $balanceWei")

        val balanceInEther: BigDecimal = Convert.fromWei(balanceWei.balance.toString(),Convert.Unit.ETHER)
        println("balance in ether: $balanceInEther")
        ethBalance?.postValue(balanceInEther)
    }

    fun getBitcoinBalance(url:String){
       val call:Call<BitcoinBalanceResponse> = DataManager.getApiService().getBitcoinBalance(url)
        call.enqueue(object : Callback<BitcoinBalanceResponse>{
            override fun onFailure(call: Call<BitcoinBalanceResponse>, t: Throwable) {
                bitcoinBalanceErrorMsg.value = errorMsg
            }

            override fun onResponse(
                call: Call<BitcoinBalanceResponse>,
                response: Response<BitcoinBalanceResponse>
            ) {
                val result = response.body()
                if (response.body() != null && result?.status !="false"){
                    _bitcoinBalanceLiveData.value =result!!.data.confirmedBalance
                }
            }
        })
    }
}