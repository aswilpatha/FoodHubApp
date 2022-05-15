package com.nsbm.project.foodhubapp

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {

    var config:PayPalConfiguration?=null
    var amount:Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        config=PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(UserInfo.clientId)

        var i=Intent(this,PayPalService::class.java)
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config)
        startService(i)

        var clickme=findViewById<Button>(R.id.paypal_btn)
        var textview=findViewById<TextView>(R.id.amount_text_view)
        clickme.setOnClickListener{
            //amount=textview.text.toString().toDouble()
            amount=100.00;
            var payment=PayPalPayment(BigDecimal.valueOf(amount),"USD","FoodHubApp",PayPalPayment.PAYMENT_INTENT_SALE)
            var intent=Intent(this, PaymentActivity::class.java)
             intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config)
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment)
            startActivityForResult(intent,123)

        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==123){
            if (resultCode==Activity.RESULT_OK){
                var obj=Intent(this,ConfirmationActivity::class.java)
                startActivity(obj)


            }
        }
    }

    override fun onDestroy() {
        stopService(Intent(this,PayPalService::class.java))

        super.onDestroy()
    }

}