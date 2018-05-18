package com.app.csulb.rentmybookfinal;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import com.app.csulb.rentmybookfinal.Config.Config;

/*
    This class goes to paypal API once you selected to rent the book from the owner
 */
public class PaymentDetails extends AppCompatActivity {

    Button done;
    TextView txtId, txtAmount, txtStatus;

    // Unique request code to verify the transaction
    public static final int PAYPAL_REQUEST_CODE = 7171;

    // Configure the paypal sandbox environment
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    String owner_name, book_price;
    String amount = "";

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        // Get the price and the name of the owner
        Intent getPayInfo = getIntent();
        owner_name = getPayInfo.getExtras().getString("owner_name");
        book_price = getPayInfo.getExtras().getString("book_price");

        processPayment();

        // Show amount of the transaction
        txtId = (TextView) findViewById(R.id.txtId);
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtStatus = (TextView) findViewById(R.id.txtStatus);

    }

    // Process transaction
    private void processPayment() {
        amount = book_price.substring(1);
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD",
                owner_name, PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

        finish();

    }

    //If the money has been process, then the details will be shown
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {

                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }

        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }

    }

}

