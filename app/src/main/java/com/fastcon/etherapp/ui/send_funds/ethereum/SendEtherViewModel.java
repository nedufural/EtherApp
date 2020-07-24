package com.fastcon.etherapp.ui.send_funds.ethereum;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fastcon.etherapp.data.local.PrefUtils;
import com.fastcon.etherapp.data.remote.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;


import org.jetbrains.annotations.NotNull;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

import java8.util.Optional;
import timber.log.Timber;

import static com.fastcon.etherapp.util.common.Commons.TEST_NET;

public class SendEtherViewModel extends ViewModel {

    MutableLiveData<Optional<TransactionReceipt>> receipt = new MutableLiveData<>();
    MutableLiveData<String> receiverToken = new MutableLiveData<>();
    MutableLiveData<String> receiverTokenError = new MutableLiveData<>();


    public void sendEther(String recipientAddress, String valueToTransfer) throws IOException, InterruptedException {

        Web3j web3 = getWeb3jConnection();
        Timber.i("Successfully connected to Ethereum");

        // Add a private key here
        String pk = PrefUtils.getEthKey();

        //Get credentials
        Credentials credentials = getCredentials(web3, pk);
        //Get Nonce
        BigInteger nonce = getNonce(web3, credentials);

        RawTransaction rawTransaction = getRawTransactionAndGas(recipientAddress, valueToTransfer, nonce);

        // Sign the transaction
        String hexValue = getSignedMessage(credentials, rawTransaction);

        // Send transaction
        String transactionHash = getTransactionHash(web3, hexValue);

        // Wait for transaction to be mined
        Optional<TransactionReceipt> transactionReceipt = getTransactionReceiptOptional(web3, transactionHash);

        getFinalResult(web3, credentials, transactionHash, transactionReceipt);
    }

    @NotNull
    private RawTransaction getRawTransactionAndGas(String recipientAddress, String valueToTransfer, BigInteger nonce) {
        // Value to transfer (in wei)
        BigInteger value = Convert.toWei(valueToTransfer, Unit.ETHER).toBigInteger();

        // Gas Parameters
        BigInteger gasLimit = BigInteger.valueOf(21000);
        BigInteger gasPrice = Convert.toWei("1", Unit.GWEI).toBigInteger();

        // Prepare the rawTransaction
        return RawTransaction.createEtherTransaction(
                nonce,
                gasPrice,
                gasLimit,
                recipientAddress,
                value);
    }

    private void getFinalResult(Web3j web3, Credentials credentials, String transactionHash, Optional<TransactionReceipt> transactionReceipt) throws IOException {
        Timber.i("Transaction " + transactionHash + " was mined in block # " + transactionReceipt.get().getBlockNumber());
        Timber.i("Balance: %s", Convert.fromWei(web3.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance().toString(), Unit.ETHER));
    }

    @NotNull
    private String getSignedMessage(Credentials credentials, RawTransaction rawTransaction) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);
    }

    private String getTransactionHash(Web3j web3, String hexValue) throws IOException {
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();
        String transactionHash = ethSendTransaction.getTransactionHash();
        Timber.i("transactionHash: %s", transactionHash);
        return transactionHash;
    }

    @NotNull
    private Optional<TransactionReceipt> getTransactionReceiptOptional(Web3j web3, String transactionHash) throws IOException, InterruptedException {
        Optional<TransactionReceipt> transactionReceipt = null;
        do {
            Timber.i("checking if transaction " + transactionHash + " is mined....");
            EthGetTransactionReceipt ethGetTransactionReceiptResp = web3.ethGetTransactionReceipt(transactionHash).send();
            transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();

            Thread.sleep(3000); // Wait 3 sec
        } while (!transactionReceipt.isPresent());
        receipt.postValue(transactionReceipt);
        return transactionReceipt;
    }

    private BigInteger getNonce(Web3j web3, Credentials credentials) throws IOException {
        // Get the latest nonce
        EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
        return ethGetTransactionCount.getTransactionCount();
    }

    @NotNull
    private Credentials getCredentials(Web3j web3, String pk) throws IOException {
        System.out.println("private key" + pk);
        // Decrypt and open the wallet into a Credential object
        Credentials credentials = Credentials.create(pk);
        Timber.i("Account address: %s", credentials.getAddress());
        Timber.i("Account address: %s", credentials.getEcKeyPair().getPublicKey());
        Timber.i("Balance: %s", Convert.fromWei(web3.ethGetBalance(PrefUtils.getEtherAddress(), DefaultBlockParameterName.LATEST).send().getBalance().toString(), Unit.ETHER));
        return credentials;
    }

    @NotNull
    private Web3j getWeb3jConnection() {
        Timber.i("Connecting to Ethereum ...");
        return Web3j.build(new HttpService(TEST_NET));
    }

    public void RetrieveReceiversToken(DatabaseReference myRef){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                PrefUtils.saveReceiversToken(Objects.requireNonNull(user.getToken()));
                Timber.i(user.getToken());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Timber.i("error retrieving token");
            }
        });
    }


}