package com.fastcon.etherapp.ui.send_funds.ethereum


import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.AsyncTask
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.FacebookSdk
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.databinding.ActivitySendEtherBinding
import com.fastcon.etherapp.notification.NotificationData
import com.fastcon.etherapp.notification.PushNotification
import com.fastcon.etherapp.ui.dialogs.SendingFundsProgress
import com.fastcon.etherapp.ui.profile.ProfileActivity
import com.fastcon.etherapp.ui.send_funds.FundSentNotification.Companion.sendNotification
import com.fastcon.etherapp.util.common.Commons
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.zxing.integration.android.IntentIntegrator
import es.dmoral.toasty.Toasty
import java8.util.Optional
import kotlinx.android.synthetic.main.activity_send_ether.*
import org.web3j.protocol.core.methods.response.TransactionReceipt
import timber.log.Timber


class SendEther : BaseActivity<ActivitySendEtherBinding>() {


    private lateinit var sendEtherViewModel: SendEtherViewModel
    private lateinit var from: EditText
    private lateinit var to: EditText
    private lateinit var amount: EditText
    private lateinit var receiverEmail: EditText
    private lateinit var sendingFundsProgress: SendingFundsProgress
    private lateinit var fm: FragmentManager
    private var etherAmount: String = ""
    private var receiverEmailText: String = ""
    private lateinit var myRef: DatabaseReference

    override fun getLayoutId(): Int {
        return R.layout.activity_send_ether
    }


    override fun initData() {
        sendEtherViewModel = ViewModelProviders.of(this).get(SendEtherViewModel::class.java)
        sendingFundsProgress = SendingFundsProgress().newInstance("Send Funds Progress")
        fm = supportFragmentManager
        declareViews()
        firebaseMessagingTopicDeclaration()

        myRef = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://etherapp-15902.firebaseio.com/Users").child(
            Commons.removeSpecialCharacter(PrefUtils.getEmail())
        )

    }

    override fun initEvent() {
        qrImageAnimate(qr_image)
        extractDetailsOfFundsToSend(send_eth_button)
        qrClickEvent(qr_image)
        displayReceipt()

    }

    private fun declareViews() {
        from = findViewById(R.id.ether_from)
        to = findViewById(R.id.ether_to)
        amount = findViewById(R.id.ether_amount)
        receiverEmail = findViewById(R.id.receivers_email)
        from.setText(PrefUtils.getEtherAddress())
    }


    private fun extractDetailsOfFundsToSend(button: Button) {
        button.setOnClickListener {

            val etherTo = to.text.toString()
            etherAmount = amount.text.toString()
            receiverEmailText = receiverEmail.text.toString()
            if (etherTo == "" || etherAmount == "" || receiverEmailText == "") {
                Toasty.custom(
                    FacebookSdk.getApplicationContext(),
                    "Complete all fields!!!",
                    R.drawable.ic_baseline_warning_24,
                    R.color.red,
                    5,
                    true,
                    true
                ).show()
            } else {
                sendFundProgressShow(fm)
                PrefUtils.saveReceiverEmail(receiverEmailText)
                sendEtherViewModel.RetrieveReceiversToken(myRef)
                sendEtherOnMainThread(etherTo, etherAmount)
            }
        }
    }


    private fun sendEtherOnMainThread(etherTo: String, etherAmount: String) {
        AsyncTask.execute {
            sendEtherViewModel.sendEther(etherTo, etherAmount)
        }
    }

    private fun displayReceipt() {
        sendEtherViewModel.receipt.observe(this, Observer { transactionReceipt ->
            setReceiptDetailsToTextFields(transactionReceipt)
            sendFundProgressHide()
            setFirebaseNotificationMessage()
        })
    }

    private fun setReceiptDetailsToTextFields(transactionReceipt: Optional<TransactionReceipt>) {
        makeReceiptVisible()
        ether_tx_receipt_c_gas_used.text =
            String.format(transactionReceipt.get().cumulativeGasUsed.toString())
        ether_tx_receipt_hash.text = transactionReceipt.get().blockHash
        ether_tx_receipt_to.text = transactionReceipt.get().to
        ether_tx_receipt_amount.text = etherAmount
        ether_tx_receipt_from.text = transactionReceipt.get().from
        ether_tx_receipt_gas_used.text = String.format(transactionReceipt.get().gasUsed.toString())
        ether_tx_receipt_tx_index.text =
            String.format(transactionReceipt.get().transactionIndex.toString())
    }

    private fun makeReceiptVisible() {
        receipt.visibility = View.VISIBLE
    }

    private fun setFirebaseNotificationMessage() {
        PushNotification(
            NotificationData(
                "Ether Received",
                "You have received $etherAmount ETH from ${PrefUtils.getUserName()}"
            ),
            PrefUtils.getReceiverToken()
        ).also {
            sendNotification(it)
        }
    }


    private fun qrImageAnimate(imageView: ImageView) {
        imageView.setBackgroundResource(R.drawable.animate_qr)
        val wifiAnimation = qr_image.background as AnimationDrawable
        wifiAnimation.start()
    }

    private fun qrClickEvent(imageView: ImageView) {
        imageView.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setBeepEnabled(true)
            integrator.setOrientationLocked(false)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            integrator.initiateScan()
        }
    }

    private fun sendFundProgressShow(fragmentManager: FragmentManager) {
        sendingFundsProgress.show(fragmentManager, "fragment_send_funds")
        sendingFundsProgress.isCancelable = false
    }

    private fun sendFundProgressHide() {
        sendingFundsProgress.dismiss()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    private fun firebaseMessagingTopicDeclaration() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            PrefUtils.saveDeviceToken(it.token)
        }
        //FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (scanResult != null) {
            ether_to.setText(scanResult.contents)

        } else {
            Timber.i("no results")
        }
    }

}