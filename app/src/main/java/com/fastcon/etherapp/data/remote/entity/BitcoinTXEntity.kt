package com.fastcon.etherapp.data.remote.entity

import com.fastcon.etherapp.data.remote.model.BitcoinResponse

data class BitcoinTXEntity(
    val hash: String,
    val time: String,
    val inputs: List<BitcoinResponse.Tx.Input>,
    val out: List<BitcoinResponse.Tx.Out>
)