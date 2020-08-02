package com.fastcon.etherapp.ui.registration;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fastcon.etherapp.R;
import com.fastcon.etherapp.data.local.PrefUtils;
import com.fastcon.etherapp.data.remote.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.bitcoinj.core.Base58;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.fastcon.etherapp.util.common.Commons.removeSpecialCharacter;


public class RegistrationViewModel extends ViewModel {


    MutableLiveData<String> createWallet = new MutableLiveData<>();
    MutableLiveData<String> createBitcoinWallet = new MutableLiveData<>();
    MutableLiveData<String> registrationResult = new MutableLiveData<>();
    MutableLiveData<String> ethPrivateKey = new MutableLiveData<>();
    MutableLiveData<String> btcPrivateKey = new MutableLiveData<>();

    public static String compressPubKey(BigInteger pubKey) {
        String pubKeyYPrefix = pubKey.testBit(0) ? "03" : "02";
        String pubKeyHex = pubKey.toString(16);
        String pubKeyX = pubKeyHex.substring(0, 64);
        return pubKeyYPrefix + pubKeyX;
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public void bitcoinWallet() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException {
        BigInteger privKey = Keys.createEcKeyPair().getPrivateKey();

        BigInteger pubKey = Sign.publicKeyFromPrivate(privKey);
        ECKeyPair keypair = new ECKeyPair(privKey, pubKey);

        String bcPub = compressPubKey(pubKey);

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] s1 = sha.digest(hexStringToByteArray(bcPub));

        MessageDigest rmd = MessageDigest.getInstance("RipeMD160");
        byte[] r1 = rmd.digest(s1);

        byte[] r2 = new byte[r1.length + 1];
        r2[0] = 0;
        for (int i = 0; i < r1.length; i++) {
            r2[i + 1] = r1[i];
        }
        byte[] a1 = new byte[25];

        byte[] s2 = sha.digest(r2);

        byte[] s3 = sha.digest(s2);


        for (int i = 0; i < r2.length; i++) {
            a1[i] = r2[i];
        }
        for (int i = 0; i < 4; i++) {
            a1[21 + i] = s3[i];
        }


        BigInteger publicKey = keypair.getPublicKey();
        BigInteger privateKey = keypair.getPrivateKey();

        Timber.v(publicKey.toString());
        createBitcoinWallet.postValue(Base58.encode(a1));
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

        createWallet.postValue(address);
        ethPrivateKey.postValue(privateKeyHex);
    }


    protected void emailPasswordRegistration(@NonNull Activity context, @NonNull String name, @NonNull String email, @NonNull String password, String etherKeyPriv, String etherKeyPub, String btcKeyPriv, String btcKeyPub) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            boolean check = !task.getResult().getSignInMethods().isEmpty();
            if (!check) {
                userRegistration(context, name, email, password, etherKeyPriv, etherKeyPub, btcKeyPriv, btcKeyPub, mAuth);
            } else {
                registrationResult.postValue("Registration Error");
                Toasty.custom(getApplicationContext(), "Email already exist!!", R.drawable.ic_baseline_warning_24, R.color.red, 5, true,
                        true).show();
            }
        }).addOnFailureListener(Throwable::printStackTrace);


    }

    private void userRegistration(@NonNull Activity context, @NonNull String name, @NonNull String email, @NonNull String password, String etherKeyPriv, String etherKeyPub, String btcKeyPriv, String btcKeyPub, FirebaseAuth mAuth) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(context, authResult -> {

                    if (authResult != null) {

                        User user = new User(name, email, etherKeyPub, etherKeyPriv, btcKeyPub, btcKeyPriv, PrefUtils.getDeviceToken());

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReferenceFromUrl("https://etherapp-15902.firebaseio.com/Users");
                        Timber.i(removeSpecialCharacter(email));
                        myRef.child(removeSpecialCharacter(email)).setValue(user).addOnSuccessListener(context, aVoid -> {

                            Timber.d("createUserWithEmail:success");
                            registrationResult.postValue("Successful Registration");
                        });
                    } else {
                        Timber.w("createUserWithEmail:failure");
                        registrationResult.postValue("Registration Error");
                    }

                });
    }

    protected void isCheckEmail(final String email) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            boolean check = !task.getResult().getSignInMethods().isEmpty();
            Timber.i("email exist : " + check);
        }).addOnFailureListener(e -> e.printStackTrace());

    }

}
