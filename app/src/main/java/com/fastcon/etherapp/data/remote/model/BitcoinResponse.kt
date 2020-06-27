package com.fastcon.etherapp.data.remote.model
import com.google.gson.annotations.SerializedName


data class BitcoinResponse(
    @SerializedName("address")
    val address: String,
    @SerializedName("final_balance")
    val finalBalance: Int,
    @SerializedName("hash160")
    val hash160: String,
    @SerializedName("n_tx")
    val nTx: Int,
    @SerializedName("total_received")
    val totalReceived: Long,
    @SerializedName("total_sent")
    val totalSent: Long,
    @SerializedName("txs")
    val txs: ArrayList<Tx>
) {
    data class Tx(
        @SerializedName("block_height")
        val blockHeight: Int,
        @SerializedName("block_index")
        val blockIndex: Int,
        @SerializedName("hash")
        val hash: String,
        @SerializedName("inputs")
        val inputs: List<Input>,
        @SerializedName("lock_time")
        val lockTime: Int,
        @SerializedName("out")
        val `out`: List<Out>,
        @SerializedName("rbf")
        val rbf: Boolean,
        @SerializedName("relayed_by")
        val relayedBy: String,
        @SerializedName("result")
        val result: Int,
        @SerializedName("size")
        val size: Int,
        @SerializedName("time")
        val time: Int,
        @SerializedName("tx_index")
        val txIndex: Int,
        @SerializedName("ver")
        val ver: Int,
        @SerializedName("vin_sz")
        val vinSz: Int,
        @SerializedName("vout_sz")
        val voutSz: Int,
        @SerializedName("weight")
        val weight: Int
    ) {
        data class Input(
            @SerializedName("prev_out")
            val prevOut: PrevOut,
            @SerializedName("script")
            val script: String,
            @SerializedName("sequence")
            val sequence: Long,
            @SerializedName("witness")
            val witness: String
        ) {
            data class PrevOut(
                @SerializedName("addr")
                val addr: String,
                @SerializedName("n")
                val n: Int,
                @SerializedName("script")
                val script: String,
                @SerializedName("spending_outpoints")
                val spendingOutpoints: List<SpendingOutpoint>,
                @SerializedName("spent")
                val spent: Boolean,
                @SerializedName("tx_index")
                val txIndex: Int,
                @SerializedName("type")
                val type: Int,
                @SerializedName("value")
                val value: Long
            ) {
                data class SpendingOutpoint(
                    @SerializedName("n")
                    val n: Int,
                    @SerializedName("tx_index")
                    val txIndex: Int
                )
            }
        }

        data class Out(
            @SerializedName("addr")
            val addr: String,
            @SerializedName("n")
            val n: Int,
            @SerializedName("script")
            val script: String,
            @SerializedName("spending_outpoints")
            val spendingOutpoints: List<SpendingOutpoint>,
            @SerializedName("spent")
            val spent: Boolean,
            @SerializedName("tx_index")
            val txIndex: Int,
            @SerializedName("type")
            val type: Int,
            @SerializedName("value")
            val value: Long
        ) {
            data class SpendingOutpoint(
                @SerializedName("n")
                val n: Int,
                @SerializedName("tx_index")
                val txIndex: Int
            )
        }
    }
}