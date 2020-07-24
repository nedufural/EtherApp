package com.fastcon.etherapp.data.remote.entity

data class User(
    var name: String? = null,
    var email: String? = null,
    var pubKeyEther: String? = null,
    var privKeyEther: String? = null,
    var pubKeyBTC: String? = null,
    var privKeyBTC: String? = null,
    var token: String? = null
)