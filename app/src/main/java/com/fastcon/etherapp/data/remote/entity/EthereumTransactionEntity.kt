package com.fastcon.etherapp.data.remote.entity

data class EthereumTransactionEntity(
    val to: String,
    val from: String,
    val value: String,
    val timeStamp: String,
    val txReceiptStatus: String,
    val blockHash: String,
    val blockNumber: String,
    val confirmations: String,
    val gas: String,
    val gasUsed: String,
    val gasPrice: String,
    val nonce: String,
    val transactionIndex: String,
    val hash: String
)