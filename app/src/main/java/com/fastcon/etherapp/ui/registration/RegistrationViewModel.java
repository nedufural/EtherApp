package com.fastcon.etherapp.ui.registration;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fastcon.etherapp.data.remote.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.bitcoinj.core.Base58;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;
import java.util.Objects;

import timber.log.Timber;

import static com.fastcon.etherapp.util.Commons.SPEC;

public class RegistrationViewModel extends ViewModel {


    MutableLiveData<String> createWallet = new MutableLiveData<>();
    MutableLiveData<String> createBitcoinWallet = new MutableLiveData<>();
    MutableLiveData<String> registrationResult = new MutableLiveData<String>();
    MutableLiveData<String> ethPrivateKey = new MutableLiveData<String>();
    MutableLiveData<String> btcPrivateKey = new MutableLiveData<String>();


    public void bitcoinWallet() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException {
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());
        KeyPair keypair = g.generateKeyPair();
        PublicKey publicKey = keypair.getPublic();
        PrivateKey privateKey = keypair.getPrivate();


        ECPublicKey epub = (ECPublicKey) publicKey;
        ECPoint pt = epub.getW();
        byte[] bcPub = new byte[33];
        bcPub[0] = 2;
        System.arraycopy(pt.getAffineX().toByteArray(), 0, bcPub, 1, 32);

        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] s1 = new byte[0];
        if (sha != null) {
            s1 = sha.digest(bcPub);
        }

        MessageDigest rmd = MessageDigest.getInstance("RipeMD160", "BC");
        byte[] ripeMD = rmd.digest(s1);

        //add 0x00
        byte[] ripeMDPadded = new byte[ripeMD.length + 1];
        ripeMDPadded[0] = 0;

        System.arraycopy(ripeMD, 0, ripeMDPadded, 1, 1);

        byte[] shaFinal = new byte[0];
        if (sha != null) {
            shaFinal = sha.digest(sha.digest(ripeMDPadded));
        }

        //append ripeMDPadded + shaFinal = sumBytes
        byte[] sumBytes = new byte[25];
        System.arraycopy(ripeMDPadded, 0, sumBytes, 0, 21);
        System.arraycopy(shaFinal, 0, sumBytes, 21, 4);
        System.out.println("Bitcoin Address: " + Base58.encode(sumBytes));
        //base 58 encode
        createBitcoinWallet.postValue(Base58.encode(sumBytes));
        btcPrivateKey.postValue(privateKey.toString());
    }


    protected void createEthereumWallet() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
// create new private/public key pair
        ECKeyPair keyPair = Keys.createEcKeyPair();

        BigInteger publicKey = keyPair.getPublicKey();
        String publicKeyHex = Numeric.toHexStringWithPrefix(publicKey);

        BigInteger privateKey = keyPair.getPrivateKey();
        String privateKeyHex = Numeric.toHexStringWithPrefix(privateKey);

        // create credentials + address from private/public key pair
        Credentials credentials = Credentials.create(new ECKeyPair(privateKey, publicKey));
        String address = credentials.getAddress();

        // print resulting data of new account
        System.out.println("private key: '" + privateKeyHex + "'");
        System.out.println("public key: '" + publicKeyHex + "'");
        System.out.println("address: '" + address + "'\n");
        createWallet.postValue(address);
        ethPrivateKey.postValue(privateKeyHex);
    }


    protected void emailPasswordRegistration(@NonNull Activity context, @NonNull String name, @NonNull String email, @NonNull String password, @NonNull String etherKeyPriv, @NonNull String etherKeyPub, @NonNull String btcKeyPriv, @NonNull String btcKeyPub) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(context, authResult -> {

                    if (authResult != null) {

                        User user = new User(name, email, etherKeyPub, etherKeyPriv, btcKeyPub, btcKeyPriv);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReferenceFromUrl("https://etherapp-15902.firebaseio.com/Users");

                        myRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(user).addOnSuccessListener(context, aVoid -> {
                            Timber.d("createUserWithEmail:success");
                            registrationResult.postValue("Successful Registration");
                        });
                    } else {
                        Timber.w("createUserWithEmail:failure");
                        registrationResult.postValue("Registration Error");
                    }

                });
    }

}
