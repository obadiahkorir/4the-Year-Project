package com.example.obadiahkorir.projects;



        import android.app.Activity;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.Bundle;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.toe.chowder.Chowder;
        import com.toe.chowder.interfaces.PaymentListener;


public class Premium extends AppCompatActivity implements PaymentListener {

    //Test parameters you can replace these with your own PayBill details
    String PAYBILL_NUMBER = "0714988589";
    String PASSKEY = "be9dc35907bd98ee471bbfe9ddd87e724cdef18ac3eabfecdfd08f2cc4a5c3e0";

    EditText  etPhoneNumber;
    TextView etAmount;
    Button bPay, bConfirm, bAccess, bLogin;
    ImageView imageView;
    int attempt_counter = 3;
    private static TextView attempts;
    //private PrefManager prefManager;
    private SharedPreference sharedPreference;

    Chowder chowder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

       /* prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            accessMainApp();
            finish();
        }*/



        setUp();
    }

    private void setUp() {
        chowder = new Chowder(Premium.this, PAYBILL_NUMBER, PASSKEY, this);

        etAmount = (TextView) findViewById(R.id.etAmount);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);

        bPay = (Button) findViewById(R.id.bPay);
        bConfirm = (Button) findViewById(R.id.bConfirm);
        bAccess = (Button) findViewById(R.id.bAccess);

        attempts = (TextView)findViewById(R.id.textView_attemt_Count);
        attempts.setText(Integer.toString(attempt_counter));

        bPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etPhoneNumber.getText().toString().trim().length() <= 0 || !isNetworkStatusAvialable(getApplicationContext()) ) {

                    android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(Premium .this);
                    a_builder.setMessage("Ensure your internet is turned on\n" +
                            " And  Enter Your Phone Number to make payment").setCancelable(false)

                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();





                }else {

                    String amount = etAmount.getText().toString().trim();
                    String phoneNumber = etPhoneNumber.getText().toString().trim();
                    //Your product's ID must have 13 digits
                    String productId = "crimestop";

                    chowder.processPayment(amount, phoneNumber.replaceAll("\\+", ""), productId);
                    //      That's it! You can now process payments using the M-Pesa API
                    //      IMPORTANT: Any cash you send to the test PayBill number is non-refundable, so use small amounts to test


                    //   ##What's happening:
                    //      The Merchant captures the payment details and prepares call to the SAG’s endpoint
                    //      The Merchant invokes SAG’s processCheckOut interface
                    //      The SAG validates the request sent and returns a response
                    //      Merchant receives the processCheckoutResponse parameters namely
                    //      TRX_ID, ENC_PARAMS, RETURN_CODE, DESCRIPTION and CUST_MSG (Customer message)
                    //      The merchant is supposed to display the CUST_MSG to the customer after which the merchant should invoke SAG’s confirmPaymentRequest interface to confirm the transaction
                    //      The system will push a USSD menu to the customer and prompt the customer to enter their BONGA PIN and any other validation information.
                    //      The transaction is processed on M-PESA and a callback is executed after completion of the transaction
                }     }
        });





        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLastPayment();
            }
        });
        bAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessApp();
            }
        });
    }

    private void confirmLastPayment() {
        SharedPreferences sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        //We saved the last transaction id to Shared Preferences
        String transactionId = sp.getString("chowderTransactionId", null);

        //Call chowder.checkTransactionStatus to check a transaction
        //Check last transaction
        if (transactionId != null) {
            chowder.checkTransactionStatus(PAYBILL_NUMBER, transactionId);


        } else {
            Toast.makeText(getApplicationContext(), "No previous transaction available", Toast.LENGTH_SHORT).show();
        }
    }

    private void accessApp() {
        //SharedPreferences sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        //We saved the last transaction id to Shared Preferences
        //String transactionStatus = sp.getString("transactionStatus", null);

        //Call chowder.checkTransactionStatus to check a transaction
        //Check last transaction
        sharedPreference = new SharedPreference();
        String transactionStatus;



        //Retrieve a value from SharedPreference
        Activity context = this;
        transactionStatus = sharedPreference.getValue(context);




        if (transactionStatus !=null) {
            //chowder.checkTransactionStatus(PAYBILL_NUMBER, transactionStatus);
            Intent n = new Intent(Premium.this, PremiumContent.class);

            startActivity(n);

        } else {
            Toast.makeText(getApplicationContext(), "You havent made payment yet.No transaction data available", Toast.LENGTH_LONG).show();
            attempt_counter--;
            attempts.setText(Integer.toString(attempt_counter));
            if(attempt_counter == 0){
                bAccess.setEnabled(false);
                android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(Premium.this);
                a_builder.setMessage("Have you made your payment?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(Premium.this);
                                a_builder.setMessage("Ensure your internet connection is on,then click on the confirm payment button\n" +
                                        "If your last transaction is shown on the screen ,restart app and login again\n" +
                                        "if you dont get any message call:0727045828 for further assistance").setCancelable(false)

                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();




                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(Premium.this);
                                a_builder.setMessage("Ensure your internet connection is on,then  enter your phone number on the space provided\n" +
                                        "click on the pay button..it is a mpesa payment method\n" +
                                        "if your payment is successful ,restart app and press the login button").setCancelable(false)

                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        });

                android.app.AlertDialog alert = a_builder.create();
                alert.setTitle("Login Assistant");
                alert.show();
            }
        }
    }


    @Override
    public void onPaymentReady(String returnCode, String processDescription, String merchantTransactionId, String transactionId) {
        //The user is now waiting to enter their PIN
        //You can use the transaction id to confirm payment to make sure you store the ids somewhere if you want the user to be able to check later
        //Save the transaction ID
        SharedPreferences sp = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        sp.edit().putString("chowderTransactionId", transactionId).apply();

        new AlertDialog.Builder(Premium.this)

                .setTitle("Payment in progress")
                .setMessage("Please wait for a pop up from Safaricom and enter your Bonga PIN")
                .setIcon(android.R.drawable.sym_contact_card)
                .setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Well you can skip the dialog if you want, but it will make the user feel safer, they'll know what's going on instead of sitting there
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onPaymentSuccess(String merchantId, String msisdn, String amount, String mpesaTransactionDate, String mpesaTransactionId, String transactionStatus, String returnCode, String processDescription, String merchantTransactionId, String encParams, String transactionId) {
        //The payment was successful.
        sharedPreference = new SharedPreference();
        Activity context = this;
        sharedPreference.save(context, transactionStatus);
        new AlertDialog.Builder(Premium.this)
                .setTitle("Payment confirmed")
                .setMessage(transactionStatus + ". Your amount of Ksh." + amount + " has been successfully paid from " + msisdn + " to PayBill number " + merchantId + " with the M-Pesa transaction code " + mpesaTransactionId + " on " + mpesaTransactionDate + ".\n\nPassword:happy2017\n\nsmall letters and joined\n\nThank you")
                .setIcon(android.R.drawable.sym_contact_card)
                .setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //Well you can skip the dialog if you want, but it might make the user feel safer
                        //The user has successfully paid so give them their goodies
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onPaymentFailure(String merchantId, String msisdn, String amount, String transactionStatus, String processDescription) {
        //The payment failed.
        new AlertDialog.Builder(Premium.this)
                .setTitle("Payment failed")
                .setMessage(transactionStatus + ". Your amount of Ksh." + amount + " was not paid from " + msisdn + " to PayBill number " + merchantId + ". Please try again.")
                .setIcon(android.R.drawable.sym_contact_card)
                .setCancelable(false)
                .setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String amount = etAmount.getText().toString().trim();
                        String phoneNumber = etPhoneNumber.getText().toString().trim();
                        //Your product's ID must have 13 digits
                        String productId = "kikuyuculture";

                        chowder.processPayment(amount, phoneNumber.replaceAll("\\+", ""), productId);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Well you can skip the dialog if you want, but it might make the user feel safer
                //The user has successfully paid so give them their goodies
                dialog.dismiss();
            }
        }).show();
    }
   /* private void accessMainApp() {
        prefManager.setFirstTimeLaunch(false);
        SharedPreferences sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        //We saved the last transaction id to Shared Preferences
        String transactionId = sp.getString("chowderTransactionId", null);

        //Call chowder.checkTransactionStatus to check a transaction
        //Check last transaction
        if (transactionId != null) {
            chowder.checkTransactionStatus(PAYBILL_NUMBER, transactionId);
            Intent i = new Intent(Premium.this, MainActivity.class);

            startActivity(i);

        } else {

            Intent j= new Intent("com.safariappreneurs.kikuyuapp.PAYMENT");
            startActivity(j);

        }
    }*/

    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                if(netInfos.isConnected())
                    return true;
        }
        return false;
    }


}
