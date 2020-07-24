package com.fastcon.etherapp

import android.app.Application
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.orhanobut.hawk.Hawk
import okhttp3.OkHttpClient
import org.bouncycastle.jce.provider.BouncyCastleProvider
import timber.log.Timber
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.Provider
import java.security.Security
import javax.net.ssl.SSLContext


class EtherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(applicationContext).build()
        Timber.plant(Timber.DebugTree())
        setupDebugTools()
        baseSecurity()
        setupBouncyCastle()
    }


    private fun setupDebugTools() {
        Stetho.initializeWithDefaults(this)
        OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }

    private fun baseSecurity() {
             //Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext: SSLContext = SSLContext.getDefault()
            sslContext.createSSLEngine()
    }

    private fun setupBouncyCastle() {
        val provider: Provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        if (provider == BouncyCastleProvider::class.java) {
            // BC with same package name, shouldn't happen in real life.
            return
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }
}