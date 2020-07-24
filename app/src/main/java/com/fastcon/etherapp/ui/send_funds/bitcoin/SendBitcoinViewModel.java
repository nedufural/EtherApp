/*
package com.fastcon.etherapp.ui.send_funds.bitcoin;

import com.fastcon.etherapp.util.common.Commons;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.PeerAddress;
import org.bitcoinj.core.PeerException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Utils;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SendBitcoinViewModel {
    public static void main(String network,String walletFileName,String amountToSend,String recipient) {


        // the Bitcoin network to use
        final NetworkParameters netParams;

        // check for production Bitcoin network ...
        if (network.equalsIgnoreCase("prod")) {
            netParams = NetworkParameters.prodNet();
            // ... otherwise use the testnet
        } else {
            netParams = NetworkParameters.testNet();
        }

        // data structure for block chain storage
        BlockStore blockStore = new MemoryBlockStore(netParams);

        // declare object to store and understand block chain
        BlockChain chain;

        // declare wallet
        Wallet wallet;

        try {

            // wallet file that contains Bitcoins we can send
            final File walletFile = new File(walletFileName);

            // load wallet from file
            wallet = Wallet.loadFromFile(walletFile);

            // how man milli-Bitcoins to send
            BigInteger btcToSend = new BigInteger(amountToSend);

            // initialize BlockChain object
            chain = new BlockChain(netParams, wallet, blockStore);

            // instantiate Peer object to handle connections
            final Peer peer = new Peer(netParams, new PeerAddress(InetAddress.getLocalHost()), chain);

            // connect to peer node on localhost
            peer.connect();

            // recipient address provided by official Bitcoin client
            Address recipientAddress = new Address(netParams, recipient);
            // tell peer to send amountToSend to recipientAddress
            Transaction sendTxn = wallet.sendCoins(peer, recipientAddress, btcToSend);
            if (sendTxn == null) {
                System.out.println("Cannot send requested amount of " + Utils.bitcoinValueToFriendlyString(btcToSend)
                        + " BTC; wallet only contains " + Utils.(wallet.getBalance()) + " BTC.");
            } else {

                // once communicated to the network (via our local peer),
                // the transaction will appear on Bitcoin explorer sooner or later
                System.out.println(Utils.bitcoinValueToFriendlyString(btcToSend) + " BTC sent. You can monitor the transaction here:\n"
                        + "http://blockexplorer.com/tx/" + sendTxn.getHashAsString());
            }
            // save wallet with new transaction(s)
            wallet.saveToFile(walletFile);
            // handle the various exceptions; this needs more work
        } catch (BlockStoreException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (PeerException e) {
            e.printStackTrace();
        } catch (AddressFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InsufficientMoneyException e) {
            e.printStackTrace();
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }
    }


}

*/
