package com.fastcon.etherapp.ui.transaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fastcon.etherapp.data.DataManager
import com.fastcon.etherapp.data.remote.entity.BitcoinTXEntity
import com.fastcon.etherapp.data.remote.entity.EthereumTransactionEntity
import com.fastcon.etherapp.data.remote.model.BitcoinResponse
import com.fastcon.etherapp.data.remote.model.TransferResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransferViewModel : ViewModel() {
    val ethereumTransactionHistoryMutableLiveData: MutableLiveData<ArrayList<EthereumTransactionEntity>> =
        MutableLiveData()
    val transferHistoryErrorMsg: MutableLiveData<String> = MutableLiveData()
    var errorMsg: String = "error"
    var data = ArrayList<EthereumTransactionEntity>()
    val bitcoinTxHistoryMutableLiveData: MutableLiveData<ArrayList<BitcoinTXEntity>> =
        MutableLiveData()
    val bitcoinTxHistoryMutableLiveDataErrorMsg: MutableLiveData<String> = MutableLiveData()
    var errorMsgBitcoin: String = "error"
    var dataBitcoin = ArrayList<BitcoinTXEntity>()

    fun getEthereumTransactionHistory(url: String) {

        val call: Call<TransferResponse> = DataManager.getApiService().getTransferHistory(url)
        call.enqueue(object : Callback<TransferResponse> {
            override fun onFailure(call: Call<TransferResponse>, t: Throwable) {
                transferHistoryErrorMsg.value = errorMsg
            }

            override fun onResponse(
                call: Call<TransferResponse>,
                response: Response<TransferResponse>
            ) {


                for (i in response.body()?.result!!) {

                    data.add(
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
                ethereumTransactionHistoryMutableLiveData.value = data
            }
        })
    }

    fun getBitcoinTransactionHistory(url: String) {
        val call: Call<BitcoinResponse> = DataManager.getApiService().getBitcoinTxHistory(url)
        call.enqueue(object : Callback<BitcoinResponse> {
            override fun onFailure(call: Call<BitcoinResponse>, t: Throwable) {
                transferHistoryErrorMsg.value = errorMsg
            }

            override fun onResponse(
                call: Call<BitcoinResponse>,
                response: Response<BitcoinResponse>
            ) {


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
                bitcoinTxHistoryMutableLiveData.value = dataBitcoin
            }
        })
    }
}