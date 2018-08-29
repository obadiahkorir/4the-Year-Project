package com.toe.chowder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.alexgilleran.icesoap.exception.SOAPException;
import com.alexgilleran.icesoap.observer.SOAP11Observer;
import com.alexgilleran.icesoap.request.Request;
import com.alexgilleran.icesoap.request.RequestFactory;
import com.alexgilleran.icesoap.request.SOAP11Request;
import com.alexgilleran.icesoap.request.impl.RequestFactoryImpl;
import com.alexgilleran.icesoap.soapfault.SOAP11Fault;
import com.toe.chowder.envelopes.ProcessCheckoutEnvelope;
import com.toe.chowder.envelopes.TransactionConfirmEnvelope;
import com.toe.chowder.envelopes.TransactionStatusQueryEnvelope;
import com.toe.chowder.interfaces.PaymentListener;
import com.toe.chowder.responses.ProcessCheckoutResponse;
import com.toe.chowder.responses.TransactionConfirmResponse;
import com.toe.chowder.responses.TransactionStatusQueryResponse;
import com.toe.chowder.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;


/**
 * Created by Wednesday on 1/16/2016.
 */
public class Chowder extends Context {

    //      The Merchant captures the payment details and prepares call to the SAG’s endpoint
    //      The Merchant invokes SAG’s processCheckOut interface
    //      The SAG validates the request sent and returns a response
    //      Merchant receives the processCheckoutResponse parameters namely
    //      TRX_ID, ENC_PARAMS, RETURN_CODE, DESCRIPTION and CUST_MSG (Customer message)
    //      The merchant is supposed to display the CUST_MSG to the customer after which the merchant should invoke SAG’s confirmPaymentRequest interface to confirm the transaction
    //      The system will push a USSD menu to the customer and prompt the customer to enter their BONGA PIN and any other validation information.
    //      The transaction is processed on M-PESA and a callback is executed after completion of the transaction

    private String SUCCESS_CODE = "00";
    private RequestFactory requestFactory = new RequestFactoryImpl();

    //Need the url and SOAP action
    private String url = "https://safaricom.co.ke/mpesa_online/lnmo_checkout_server.php?wsdl";
    private String soapAction = "https://safaricom.co.ke/mpesa_online/lnmo_checkout_server.php?wsdl";

    //M-Pesa checkout parameters
    private String encParams = "";
    private String callBackUrl = "http://www.cictnotes.co.ke/processcheckout.php";
    private String callBackMethod = "POST";

    //Payment parameters
    private String merchantId ;
    private String passkey;

    private Activity activity;
    private ProgressDialog progress;
    private String merchantTransactionId;

    private String timestamp;
    private String password;

    //Listeners
    PaymentListener paymentCompleteListener;

    public Chowder(Activity activity, String merchantId, String passkey, PaymentListener paymentCompleteListener) {
        this.paymentCompleteListener = paymentCompleteListener;
        this.activity = activity;
        this.merchantId = merchantId;
        this.passkey = passkey;
    }

    public void processPayment(String amount, String phoneNumber, String productId) {
        progress = ProgressDialog.show(activity, "Please wait",
                "Connecting to Safaricom...", true);

        String referenceId = productId;
        timestamp = Utils.generateTimestamp();
        merchantTransactionId = Utils.generateRandomId();
        password = Utils.generatePassword(merchantId + passkey + timestamp).replaceAll("\\s+", "");

//      The Merchant captures the payment details and prepares call to the SAG’s endpoint.
//      The Merchant invokes SAG’s processCheckOut interface.
        Log.d("M-PESA REQUEST", new ProcessCheckoutEnvelope(merchantId, password, timestamp, merchantTransactionId, referenceId, amount, phoneNumber, encParams, callBackUrl, callBackMethod).toString());

        trustEveryone();
        SOAP11Request<ProcessCheckoutResponse> processCheckoutRequest = requestFactory.buildRequest(url, new ProcessCheckoutEnvelope(merchantId, password, timestamp, merchantTransactionId, referenceId, amount, phoneNumber, encParams, callBackUrl, callBackMethod), soapAction, ProcessCheckoutResponse.class);
        processCheckoutRequest.execute(processCheckoutObserver);
    }

    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }

    private SOAP11Observer<ProcessCheckoutResponse> processCheckoutObserver = new SOAP11Observer<ProcessCheckoutResponse>() {

        @Override
        public void onCompletion(Request<ProcessCheckoutResponse, SOAP11Fault> request) {
            if (request.getResult() != null) {
                String returnCode = request.getResult().getReturnCode();
                String processDescription = request.getResult().getDescription();
                String transactionId = request.getResult().getTransactionId();
                String encParams = request.getResult().getEncParams();
                String customMessage = request.getResult().getCustomMessage();

                Log.d("M-PESA REQUEST", "Return code: " + returnCode);

//                The SAG validates the request sent and returns a response.
//                Merchant receives the processCheckoutResponse parameters namely
//                TRX_ID, ENC_PARAMS, RETURN_CODE, DESCRIPTION and
//                CUST_MSG (Customer message).

                if (returnCode.equals(SUCCESS_CODE)) {
                    progress.dismiss();
//                   The merchant is supposed to display the CUST_MSG to the customer after which
//                   the merchant should invoke SAG’s confirmPaymentRequest
//                   interface to confirm the transaction

                    showCustomMessage(activity, customMessage, transactionId);
                } else {
                    progress.dismiss();
                    Log.d("M-PESA REQUEST", "Process checkout failed: " + returnCode);
                    Toast.makeText(activity, "Something went wrong. Checkout failed: " + returnCode, Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("M-PESA REQUEST", "Result is null");
                //Toast.makeText(activity, "Please trtinternet connection and try again. No response from Safaricom.", Toast.LENGTH_SHORT).show();
                android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(Chowder.this);
                a_builder.setMessage("Safaricom online services are unavailable,use \n" +
                        " PAYBILL NUMBER: 877433\n\n" +
                        "ACCOUNT NUMBER:  Githeriman\n\n" +
                        "To make payments manually").setCancelable(false)

                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        }

        @Override
        public void onException(Request<ProcessCheckoutResponse, SOAP11Fault> request, SOAPException e) {
            progress.dismiss();
            Log.e("M-PESA REQUEST", "Error: " + e.toString());
            /*Toast.makeText(activity, "Something went wrong. Error: " + e.toString(), Toast.LENGTH_SHORT).show();*/
        }
    };

    private void showCustomMessage(final Context context, String customMessage, final String transactionId) {
        new AlertDialog.Builder(context)
                .setTitle("Payment Ready")
                .setMessage(customMessage)
                .setIcon(android.R.drawable.sym_contact_card)
                .setCancelable(false)
                .setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("M-PESA REQUEST", new TransactionConfirmEnvelope(merchantId, password, timestamp, transactionId, merchantTransactionId).toString());

                        progress = ProgressDialog.show(context, "Please wait",
                                "Processing your payment...", true);
                        SOAP11Request<TransactionConfirmResponse> transactionConfirmRequest = requestFactory.buildRequest(url, new TransactionConfirmEnvelope(merchantId, password, timestamp, transactionId, merchantTransactionId), soapAction, TransactionConfirmResponse.class);
                        transactionConfirmRequest.execute(transactionConfirmObserver);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private SOAP11Observer<TransactionConfirmResponse> transactionConfirmObserver = new SOAP11Observer<TransactionConfirmResponse>() {

        @Override
        public void onCompletion(Request<TransactionConfirmResponse, SOAP11Fault> request) {
//            The system will push a USSD menu to the customer and prompt the customer to enter
//            their BONGA PIN and any other validation information.
//            The transaction is processed on M-PESA and a callback is executed after completion of the transaction.

            if (request.getResult() != null) {
                String returnCode = request.getResult().getReturnCode();
                String processDescription = request.getResult().getDescription();
                String merchantTransactionId = request.getResult().getMerchantTransactionId();
                String transactionId = request.getResult().getTransactionId();

                Log.d("M-PESA REQUEST", "Return code: " + returnCode);

                if (returnCode.equals(SUCCESS_CODE)) {
                    progress.dismiss();
                    paymentReady(returnCode, processDescription, merchantTransactionId, transactionId);
                } else {
                    progress.dismiss();
                    Log.d("M-PESA REQUEST", "Transaction confirmation failed: " + returnCode);
                    Toast.makeText(activity, "Something went wrong. Transaction confirmation failed: " + returnCode, Toast.LENGTH_SHORT).show();
                }
            } else {
                progress.dismiss();
                Log.d("M-PESA REQUEST", "Result is null");
                Toast.makeText(activity, "Something went wrong. No response from Safaricom. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onException(Request<TransactionConfirmResponse, SOAP11Fault> request, SOAPException e) {
            progress.dismiss();
            Log.e("M-PESA REQUEST", "Error: " + e.toString());
            Toast.makeText(activity, "Something went wrong. Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public void checkTransactionStatus(String merchantId, String  transactionId) {
        progress = ProgressDialog.show(activity, "Please wait",
                "Checking the transaction status...", true);

        timestamp = Utils.generateTimestamp();
        merchantTransactionId = Utils.generateRandomId();
        password = Utils.generatePassword(merchantId + passkey + timestamp).replaceAll("\\s+", "");
        Log.d("M-PESA REQUEST", new TransactionStatusQueryEnvelope(merchantId, password, timestamp, transactionId,  merchantTransactionId).toString());

        trustEveryone();
        SOAP11Request<TransactionStatusQueryResponse> transactionStatusQueryRequest = requestFactory.buildRequest(url, new TransactionStatusQueryEnvelope(merchantId, password, timestamp, transactionId, merchantTransactionId), soapAction, TransactionStatusQueryResponse.class);
        transactionStatusQueryRequest.execute(transactionStatusQueryObserver);
    }

    private SOAP11Observer<TransactionStatusQueryResponse> transactionStatusQueryObserver = new SOAP11Observer<TransactionStatusQueryResponse>() {

        @Override
        public void onCompletion(Request<TransactionStatusQueryResponse, SOAP11Fault> request) {
            if (request.getResult() != null) {

                String msisdn = request.getResult().getMsisdn();
                String amount = request.getResult().getAmount();
                String mpesaTransactionDate = request.getResult().getMpesaTransactionDate();
                String mpesaTransactionId = request.getResult().getMpesaTransactionId();
                String transactionStatus = request.getResult().getTransactionStatus();
                String returnCode = request.getResult().getReturnCode();
                String processDescription = request.getResult().getDescription();
                String merchantTransactionId = request.getResult().getMerchantTransactionId();
                String encParams = request.getResult().getEncParams();
                String transactionId = request.getResult().getTransactionId();

                Log.d("M-PESA REQUEST", "Mpesa transaction id: " + mpesaTransactionId);

                if (returnCode.equals(SUCCESS_CODE)) {
                    progress.dismiss();
                    if (mpesaTransactionId == null || mpesaTransactionId.equals("N/A") || mpesaTransactionDate == null) {
                        paymentFailure(merchantId, msisdn, amount, transactionStatus, processDescription);
                    } else {
                        paymentSuccess(merchantId, msisdn, amount, mpesaTransactionDate, mpesaTransactionId, transactionStatus, returnCode, processDescription, merchantTransactionId, encParams, transactionId);

                        /*Intent intent = new Intent();
                        String pkg ="com.safariappreneurs.kikuyuapp";
                        String clazz =pkg + ".MainActivity";
                        intent.setComponent(new ComponentName(pkg, clazz));
                        activity.startActivity(intent);*/
                    }
                } else {
                    progress.dismiss();
                    Log.d("M-PESA REQUEST", "Transaction confirm failed: " + returnCode);
                    paymentFailure(merchantId, msisdn, amount, transactionStatus, processDescription);
                }
            } else {
                progress.dismiss();
                Log.d("M-PESA REQUEST", "Result is null");
                Toast.makeText(activity, " No response from Safaricom. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onException(Request<TransactionStatusQueryResponse, SOAP11Fault> request, SOAPException e) {
            progress.dismiss();
            Log.e("M-PESA REQUEST", "Error: " + e.toString());
            //Toast.makeText(activity, "Something went wrong. Error: " + e.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(activity, "Turn on your internet connection to view your payment status " , Toast.LENGTH_SHORT).show();
        }
    };

    private void paymentReady(String returnCode, String processDescription, String merchantTransactionId, String transactionId) {
        paymentCompleteListener.onPaymentReady(returnCode, processDescription, merchantTransactionId, transactionId);
    }

    private void paymentSuccess(String merchantId, String msisdn, String amount, String mpesaTransactionDate, String mpesaTransactionId, String transactionStatus, String returnCode, String processDescription, String merchantTransactionId, String encParams, String transactionId) {
        paymentCompleteListener.onPaymentSuccess(merchantId, msisdn, amount, mpesaTransactionDate, mpesaTransactionId, transactionStatus, returnCode, processDescription, merchantTransactionId, encParams, transactionId);
    }

    private void paymentFailure(String merchantId, String msisdn, String amount, String transactionStatus, String processDescription) {
        paymentCompleteListener.onPaymentFailure(merchantId, msisdn, amount, transactionStatus, processDescription);
    }

    @Override
    public AssetManager getAssets() {
        return null;
    }

    @Override
    public Resources getResources() {
        return null;
    }

    @Override
    public PackageManager getPackageManager() {
        return null;
    }

    @Override
    public ContentResolver getContentResolver() {
        return null;
    }

    @Override
    public Looper getMainLooper() {
        return null;
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void setTheme(int resid) {

    }

    @Override
    public Resources.Theme getTheme() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return null;
    }

    @Override
    public String getPackageResourcePath() {
        return null;
    }

    @Override
    public String getPackageCodePath() {
        return null;
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return null;
    }

    @Override
    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        return null;
    }

    @Override
    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        return null;
    }

    @Override
    public boolean deleteFile(String name) {
        return false;
    }

    @Override
    public File getFileStreamPath(String name) {
        return null;
    }

    @Override
    public File getFilesDir() {
        return null;
    }

    @Override
    public File getNoBackupFilesDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalFilesDir(String type) {
        return null;
    }

    @Override
    public File[] getExternalFilesDirs(String type) {
        return new File[0];
    }

    @Override
    public File getObbDir() {
        return null;
    }

    @Override
    public File[] getObbDirs() {
        return new File[0];
    }

    @Override
    public File getCacheDir() {
        return null;
    }

    @Override
    public File getCodeCacheDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalCacheDir() {
        return null;
    }

    @Override
    public File[] getExternalCacheDirs() {
        return new File[0];
    }

    @Override
    public File[] getExternalMediaDirs() {
        return new File[0];
    }

    @Override
    public String[] fileList() {
        return new String[0];
    }

    @Override
    public File getDir(String name, int mode) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return null;
    }

    @Override
    public boolean deleteDatabase(String name) {
        return false;
    }

    @Override
    public File getDatabasePath(String name) {
        return null;
    }

    @Override
    public String[] databaseList() {
        return new String[0];
    }

    @Override
    public Drawable getWallpaper() {
        return null;
    }

    @Override
    public Drawable peekWallpaper() {
        return null;
    }

    @Override
    public int getWallpaperDesiredMinimumWidth() {
        return 0;
    }

    @Override
    public int getWallpaperDesiredMinimumHeight() {
        return 0;
    }

    @Override
    public void setWallpaper(Bitmap bitmap) throws IOException {

    }

    @Override
    public void setWallpaper(InputStream data) throws IOException {

    }

    @Override
    public void clearWallpaper() throws IOException {

    }

    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public void startActivity(Intent intent, Bundle options) {

    }

    @Override
    public void startActivities(Intent[] intents) {

    }

    @Override
    public void startActivities(Intent[] intents, Bundle options) {

    }

    @Override
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {

    }

    @Override
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {

    }

    @Override
    public void sendBroadcast(Intent intent) {

    }

    @Override
    public void sendBroadcast(Intent intent, String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcast(Intent intent, String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

    }

    @Override
    public void sendStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

    }

    @Override
    public void removeStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Override
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

    }

    @Override
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
        return null;
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {

    }

    @Nullable
    @Override
    public ComponentName startService(Intent service) {
        return null;
    }

    @Override
    public boolean stopService(Intent service) {
        return false;
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return false;
    }

    @Override
    public void unbindService(ServiceConnection conn) {

    }

    @Override
    public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
        return false;
    }

    @Override
    public Object getSystemService(String name) {
        return null;
    }

    @Override
    public String getSystemServiceName(Class<?> serviceClass) {
        return null;
    }

    @Override
    public int checkPermission(String permission, int pid, int uid) {
        return 0;
    }

    @Override
    public int checkCallingPermission(String permission) {
        return 0;
    }

    @Override
    public int checkCallingOrSelfPermission(String permission) {
        return 0;
    }

    @Override
    public int checkSelfPermission(String permission) {
        return 0;
    }

    @Override
    public void enforcePermission(String permission, int pid, int uid, String message) {

    }

    @Override
    public void enforceCallingPermission(String permission, String message) {

    }

    @Override
    public void enforceCallingOrSelfPermission(String permission, String message) {

    }

    @Override
    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {

    }

    @Override
    public void revokeUriPermission(Uri uri, int modeFlags) {

    }

    @Override
    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
        return 0;
    }

    @Override
    public int checkCallingUriPermission(Uri uri, int modeFlags) {
        return 0;
    }

    @Override
    public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
        return 0;
    }

    @Override
    public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags) {
        return 0;
    }

    @Override
    public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {

    }

    @Override
    public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {

    }

    @Override
    public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {

    }

    @Override
    public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags, String message) {

    }

    @Override
    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return null;
    }

    @Override
    public Context createConfigurationContext(Configuration overrideConfiguration) {
        return null;
    }

    @Override
    public Context createDisplayContext(Display display) {
        return null;
    }
}