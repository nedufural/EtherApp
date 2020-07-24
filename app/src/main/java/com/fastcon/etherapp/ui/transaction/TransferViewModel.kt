package com.fastcon.etherapp.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.FacebookSdk.getApplicationContext
import com.fastcon.etherapp.R
import com.fastcon.etherapp.data.DataManager
import com.fastcon.etherapp.data.remote.entity.BitcoinTXEntity
import com.fastcon.etherapp.data.remote.entity.EthereumTransactionEntity
import com.fastcon.etherapp.data.remote.model.BitcoinResponse
import com.fastcon.etherapp.data.remote.model.TransferResponse
import com.fastcon.etherapp.exception.NetworkExceptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransferViewModel : ViewModel() {
    private var ethereumTransactionHistoryMutableLiveData: MutableLiveData<ArrayList<EthereumTransactionEntity>>? =
        null

    fun getEthereumTransactionHistory(): LiveData<ArrayList<EthereumTransactionEntity>>? {
        if (ethereumTransactionHistoryMutableLiveData == null) {
            ethereumTransactionHistoryMutableLiveData = MutableLiveData()
        }
        return ethereumTransactionHistoryMutableLiveData
    }

    private var transferHistoryErrorMsg: MutableLiveData<String>? = null
    fun getTransferHistoryErrorMsg(): LiveData<String>? {
        if (transferHistoryErrorMsg == null) {
            transferHistoryErrorMsg = MutableLiveData()
        }
        return transferHistoryErrorMsg
    }


    private var bitcoinTxHistoryMutableLiveData: MutableLiveData<ArrayList<BitcoinTXEntity>>? =
        null

    fun getBitcoinTxHistoryMutableLiveData(): LiveData<ArrayList<BitcoinTXEntity>>? {
        if (bitcoinTxHistoryMutableLiveData == null) {
            bitcoinTxHistoryMutableLiveData = MutableLiveData()
        }
        return bitcoinTxHistoryMutableLiveData
    }

    private var tnxErrorMsg: MutableLiveData<String>? = null
    fun getExchangeErrorMsg(): LiveData<String>? {
        if (tnxErrorMsg == null) {
            tnxErrorMsg = MutableLiveData()
        }
        return tnxErrorMsg
    }


    private var errorMsg: String = getApplicationContext().getString(R.string.error)
    private var dataEther = ArrayList<EthereumTransactionEntity>()
    private var dataBitcoin = ArrayList<BitcoinTXEntity>()


    fun getEthereumTransactionHistory(url: String) {

        val call: Call<TransferResponse> = DataManager.getApiService().getTransferHistory(url)
        call.enqueue(object : Callback<TransferResponse> {
            override fun onFailure(call: Call<TransferResponse>, t: Throwable) {
                transferHistoryErrorMsg?.value = errorMsg
            }

            override fun onResponse(
                call: Call<TransferResponse>,
                response: Response<TransferResponse>
            ) {
                val responseCode = NetworkExceptions.getException(response.code())
                if (response.code() == 200) {
                    if (response.body() != null) {
                        addEtherResponseToList(response)
                    } else {
                        tnxErrorMsg?.value =
                            getApplicationContext().getString(R.string.response_empty)
                    }
                } else {
                    tnxErrorMsg?.value = responseCode
                }
            }
        })
    }

    fun getBitcoinTransactionHistory(url: String) {
        val call: Call<BitcoinResponse> = DataManager.getApiService().getBitcoinTxHistory(url)
        call.enqueue(object : Callback<BitcoinResponse> {
            override fun onFailure(call: Call<BitcoinResponse>, t: Throwable) {
                transferHistoryErrorMsg?.value = errorMsg
            }

            override fun onResponse(
                call: Call<BitcoinResponse>,
                response: Response<BitcoinResponse>
            ) {
                val responseCode = NetworkExceptions.getException(response.code())
                if (response.code() == 200) {
                    if (response.body() != null) {
                        addBTCResponseToList(response)
                        bitcoinTxHistoryMutableLiveData?.value = dataBitcoin
                    } else {
                        tnxErrorMsg?.value =
                            getApplicationContext().getString(R.string.response_empty)
                    }
                } else {
                    tnxErrorMsg?.value = responseCode
                }
            }
        })
    }

    private fun addBTCResponseToList(response: Response<BitcoinResponse>) {
        for (i in response.body()?.txs!!) {

            dataBitcoin.add(
                BitcoinTXEntity(
                    i.hash,
                    i.time.toString(),
                    i.inputs,
                    i.out
                )
            )
        }
    }

    fun addEtherResponseToList(response: Response<TransferResponse>) {

        for (i in response.body()?.result!!) {
            dataEther.add(
                EthereumTransactionEntity(
                    i.to,
                    i.from,
                    i.value,
                    i.timeStamp,
                    i.txreceiptStatus,
                    i.blockHash,
                    i.blockNumber,
                    i.confirmations,
                    i.gas,
                    i.gasUsed,
                    i.gasPrice,
                    i.nonce,
                    i.transactionIndex,
                    i.hash
                )
            )
        }
        ethereumTransactionHistoryMutableLiveData?.value = dataEther
    }

}