package mx.digitalcoaster.rzertuche.flap_example;


import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.digitalcoaster.rzertuche.requestsflap.FlapRequests;


/**
 * Created by rzertuche on 5/6/16.
 */
public class Flap_API extends FlapRequests implements FlapRequests.FlapResponses {

    protected static Dialog dialog;
    protected static ArrayAdapter<String> arrayAdapter;
    protected static List<BluetoothDevice> foundDevices;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pago, container, false);

        registerCallback(this);
        loginUser("v1", "v1", "D", getContext());
//        getUserNode("v1", "token", "D", getContext());
//        getTransactions(getContext(), "token", "2016-01-01", "2016-02-10");
//        searchByAuthCodeOrOrder(getContext(), "token", "adq380", "");
//        searchByAuthCodeOrOrder(getContext(), "token", "", "231583");
//        checkCard();

//        startNomad();
//        scanBluetooth();

        startWalker();


        return rootView;


    }

    @Override
    public void isDeviceConnected(Boolean connected) {
        if (connected){
            Log.d("isDeviceConnected", "true");
            checkCard();
        }else {
            Log.d("isDeviceConnected", "false");
        }
    }
    @Override
    public void loginResponse(Boolean success, String message, String token, String servicios){
        if (success){
            Log.d("Login",token+" EXITO");
        } else {
            Log.d("Login",token+" con error: "+message);
        }
    }
    @Override
    public void userNodeResponse(JSONObject user, String status){
        Log.d("USER","user node: "+ status);
    }
    @Override
    public void getTransactionsResponse(Boolean success, JSONArray transactions, String message) {
        if(success){
            Log.d("TRANSACTIONS",transactions.toString());
        } else {
            Log.d("TRANSACTIONS",message);
        }
    }
    @Override
    public void getSearchResponse(Boolean success, JSONArray transactions){
        Log.d("SEARCH",transactions.toString());
    }
    @Override
    public void didCancelOrder(Boolean success, String message) {
        Log.d("didCancelOrder",success.toString());
        Log.d("didCancelOrder",message);
    }
    @Override
    public void didFindCard(){
        Log.d("didFindCard","yeap");
    }
    @Override
    public void askForCVV() {
        Log.d("askForCVV", "Dame el cvv");
        setCvv("549");
    }
    @Override
    public void didHavePointsOrMonths(Boolean points, ArrayList<String> months, Boolean isEMV) {
        Log.d("didHavePointsOrMonths", points.toString());
        Log.d("didHavePointsOrMonths", months.toString());
        if (isEMV) emvPayment(getContext());
        else msrPayment(getContext());
    }
    @Override
    public void didFinishPayment(Boolean success, String transactionId, String authCode, String criptograma, String message){
        Log.d("didFinishPayment", success.toString());
        Log.d("didFinishPayment", transactionId);
        Log.d("didFinishPayment", authCode);
        Log.d("didFinishPayment", criptograma);
        Log.d("didFinishPayment", message);
        if (success){
            //String image_base64= encodeToBase64(BitmapFactory.decodeResource(getResources(), R.drawable.img));
            //sendSignature(getContext(), image_base64, transactionId);
        }
    }
    @Override
    public void didSendSignature(Boolean success) {
        Log.d("didSendSignature", success.toString());
    }

    //
    @Override
    public void scanResult(Boolean success, List<BluetoothDevice> devices, String message){
        if (!success){
            Log.d("SCANRESULT", success.toString());
            Log.d("SCANRESULT", message);
        } else {
            Log.d("SCANRESULT", devices.toString());
            connectNomad(devices.get(0));
        }
    }
    @Override
    public void isBluetoothDeviceConnected(Boolean connected){
        Log.d("BluetoothConnected", connected.toString());
        if (connected){
            checkCardNomad();
        }
    }
    @Override
    public void pinRequest(){
        Log.d("pinRequest", "pinRequest");
    }
    @Override
    public void startedOnlineTransaction(Boolean isEMV){
        Log.d("startOnlineTransaction", "startedOnlineTransaction");
    }
    @Override
    public void confirmedAmount(){
        Log.d("confirmedAmount", "confirmedAmount");
    }
    @Override
    public void selectApplicationList(String[] appNameList){
        Log.d("selectApplicationList", "selectApplicationList");
    }


    public String encodeToBase64(Bitmap immagex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_CLOSE);
        //System.out.println(encodedImage);
        return encodedImage;
    }

}
