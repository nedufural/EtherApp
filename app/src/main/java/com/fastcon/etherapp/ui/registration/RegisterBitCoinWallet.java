/*
package com.fastcon.etherapp.ui.registration;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.wallet.Wallet;

import java.io.File;
import java.io.IOException;

public class RegisterBitCoinWallet {

    public static void main() {
        // work with testnet
        final NetworkParameters netParams = NetworkParameters.testNet();
        // Try to read the wallet from storage, create a new one if not possible.
        Wallet wallet = null;
        final File walletFile = new File("test.wallet");
        try {
            wallet = new Wallet(netParams);
            // 5 times
            for (int i = 0; i < 5; i++) {
                // create a key and add it to the wallet
                wallet.addKey(new ECKey());
            }
            // save wallet contents to disk
            wallet.saveToFile(walletFile);
        } catch (IOException e) {
            System.out.println("Unable to create wallet file.");
        }

        // fetch the first key in the wallet directly from the keychain ArrayList
        ECKey firstKey = wallet.getIssuedReceiveKeys().get(0);

        // output key
        System.out.println("First key in the wallet:\n" + firstKey);

        // and here is the whole wallet
        System.out.println("Complete content of the wallet:\n" + wallet);
        // we can use the hash of the public key
        // to check whether the key pair is in this wallet
        if (wallet.isPubKeyHashMine(firstKey.getPubKeyHash())) {
            System.out.println("Yep, that's my key.");
        } else {
            System.out.println("Nope, that key didn't come from this wallet.");
        }
    }
}

*/
