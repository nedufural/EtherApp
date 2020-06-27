package com.fastcon.etherapp.data.model.entity

data class ExchangeRateEntity (
    val currencySymbol: String,
    val id: String,
    val rateUsd: String,
    val symbol: String,
    val type: String
)