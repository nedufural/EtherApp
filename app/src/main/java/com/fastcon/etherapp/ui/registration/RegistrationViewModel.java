package com.fastcon.etherapp.ui.registration;

import android.app.Activity;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fastcon.etherapp.R;
import com.fastcon.etherapp.data.local.PrefUtils;
import com.fastcon.etherapp.data.remote.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
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

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.fastcon.etherapp.util.common.Commons.SPEC;
import static com.fastcon.etherapp.util.common.Commons.removeSpecialCharacter;

public class RegistrationViewModel extends ViewModel {


    MutableLiveData<String> createWallet = new MutableLiveData<>();
    MutableLiveData<String> createBitcoinWallet = new MutableLiveData<>();
    MutableLiveData<String> registrationResult = new MutableLiveData<>();
    MutableLiveData<String> ethPrivateKey = new MutableLiveData<>();
    MutableLiveData<String> btcPrivateKey = new MutableLiveData<>();


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

        createWallet.postValue(address);
        ethPrivateKey.postValue(privateKeyHex);
    }


    protected void emailPasswordRegistration(@NonNull Activity context, @NonNull String name, @NonNull String email, @NonNull String password,  String etherKeyPriv,  String etherKeyPub,  String btcKeyPriv,  String btcKeyPub) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            boolean check = !task.getResult().getSignInMethods().isEmpty();
            if(!check){
                userRegistration(context, name, email, password, etherKeyPriv, etherKeyPub, btcKeyPriv, btcKeyPub, mAuth);
            }
            else{
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

    protected void isCheckEmail(final String email){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            boolean check = !task.getResult().getSignInMethods().isEmpty();
            Timber.i("email exist : "+check);
        }).addOnFailureListener(e -> e.printStackTrace());

    }

}
