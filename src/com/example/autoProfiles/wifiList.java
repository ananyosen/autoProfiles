package com.example.autoProfiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout.LayoutParams;
import android.view.View;
import android.widget.*;

import java.util.*;

/**
 * Created by Ananyo on 4/9/2015.
 */
public class wifiList extends Activity
{
        String TAG = "aupro.wlist";
        int wSize;
        Button btnDone, btnCnc, btnClr;
        List <CheckBox> allCbk =new  ArrayList<CheckBox>();
        List <TextView> allTxt = new ArrayList<TextView>();
        List <Button> allBtn= new ArrayList<Button>();
        List <WifiConfiguration> wList;
        List <String> wListStr = new ArrayList<String>();
        boolean isBlack = true;
        public static String keyBlack = "BLACK", keyWhite = "WHITE";
        SharedPreferences sharedPref;
        SharedPreferences.Editor editor;

        Set <String> setNetwList = new HashSet<String>();
        Set <String> getNetwList = new HashSet<String>();
        Set <String> getAltrNetwList = new HashSet<String>();
        Intent I;

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.wifilist);
                btnDone = (Button) findViewById(R.id.buttonDn);
                btnClr = (Button) findViewById(R.id.buttonClr);
                btnCnc = (Button) findViewById(R.id.buttonCnc);
                allBtn.add(btnDone);
                allBtn.add(btnClr);
                allBtn.add(btnCnc);
                I = getIntent();
                isBlack = I.getBooleanExtra(launchActivity.intent_key, true);
                sharedPref = getSharedPreferences(launchActivity.appKey, MODE_PRIVATE);
                editor = sharedPref.edit();
                btnDone.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View view)
                        {
                                saveAndFinish();
                        }
                });
                btnCnc.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View view)
                        {
                                cancelAndFinish();
                        }
                });
                btnClr.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View view)
                        {
                                clearAll();
                        }
                });
                getConfiguredWifi();
        }

        void clearAll()
        {
                for (CheckBox x : allCbk)
                {
                        x.setChecked(false);
                }
        }

        void cancelAndFinish()
        {
                this.setResult(Activity.RESULT_CANCELED);
                finish();
        }

        void saveAndFinish()
        {
                for (int jjj = 0; jjj < allCbk.size(); jjj++)
                {
                        boolean isck = allCbk.get(jjj).isChecked();
                        if (isck)
                        {
                                String tmpStr =(String) allTxt.get(jjj).getText();
                                setNetwList.add(tmpStr);
                        }
                }
                if(isBlack)
                {
                        editor.putStringSet(keyBlack, setNetwList);
                        editor.commit();
                }
                else
                {
                        editor.putStringSet(keyWhite, setNetwList);
                        editor.commit();
                }
                this.setResult(RESULT_OK);
                finish();
        }

        void getConfiguredWifi()
        {
                WifiManager wManager;
                wManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                //wManager.startScan();
                if(wManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED)
                {
                        wList = wManager.getConfiguredNetworks();
                        for (WifiConfiguration x : wList)
                        {
                                wListStr.add(x.SSID);
                        }
                        Collections.sort(wListStr);
                        setLayout(wList);
                }
                else
                {
                        makeToast("Wifi Disabled");
                        for(Button x : allBtn)
                        {
                                x.setClickable(false);
                                x.setEnabled(false);
                        }
                        return;
                }
        }

        void setLayout(List <WifiConfiguration> wList)
        {
                if(isBlack)
                {
                        getNetwList = sharedPref.getStringSet(keyBlack, getNetwList);
                        getAltrNetwList = sharedPref.getStringSet(keyWhite, getAltrNetwList);
                }
                else
                {
                        getNetwList = sharedPref.getStringSet(keyWhite, getNetwList);
                        getAltrNetwList = sharedPref.getStringSet(keyBlack, getAltrNetwList);
                }
                LinearLayout lm = (LinearLayout) findViewById(R.id.linearParent);
                wSize = wList.size();
                for (int iii = 0 ; iii < wSize; iii++)
                {
                        boolean checkThis = false;
                        boolean unCheckable = false;
                        LinearLayout tempL = new LinearLayout(this);
                        tempL.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams tempLParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        tempL.setLayoutParams(tempLParam);
                        String ssidStr = wListStr.get(iii);
                        TextView temptxt = new TextView(this);
                        temptxt.setText(ssidStr);
                        // temptxt.setMaxWidth(200);
                        allTxt.add(temptxt);
                        CheckBox tempCheck = new CheckBox(this);
                        tempCheck.setText("");
                        tempCheck.setGravity(Gravity.RIGHT);
                        tempCheck.setId(iii);
                        unCheckable = getAltrNetwList.contains(ssidStr);
                        if(unCheckable)
                        {
                                tempCheck.setChecked(false);
                                tempCheck.setEnabled(false);
                                temptxt.setEnabled(false);
                        }
                        checkThis = getNetwList.contains(ssidStr);
                        if (checkThis)
                        {
                                tempCheck.setChecked(true);
                        }
                        allCbk.add(tempCheck);
                        tempL.addView(tempCheck);
                        tempL.addView(temptxt);
                        lm.addView(tempL);
                }
        }

        void makeToast(String msg)
        {
                Toast.makeText(wifiList.this, msg, Toast.LENGTH_SHORT).show();
        }

        void sendLog(String msg)
        {
                Log.d(TAG, msg);
        }
}