package com.example.autoProfiles;

import android.app.Service;
import android.content.*;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.Toast;

import java.util.*;

/**
 * Created by Ananyo on 4/10/2015.
 */
public class profileMan extends Service
{
        WifiManager mWifi;
        AudioManager auMan;
        WReceiver mRecv;
        List<ScanResult> mwList;
        SharedPreferences sPref;
        Set<String> strB = new HashSet<String>(), strW = new HashSet<String>();
        int maxVol = 0;
        Timer mTimer = new Timer();
        public long period = 60000;
        TimerTask mTask = new TimerTask()
        {
                @Override
                public void run()
                {
                        WifiManager wiMan =(WifiManager) getSystemService(Context.WIFI_SERVICE);
                        if (wiMan.isWifiEnabled())
                        {
                                wiMan.startScan();
                        }
                }
        };

        @Override
        public IBinder onBind(Intent intent)
        {
                return null;
        }

        @Override
        public void onCreate()
        {
                mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                mRecv = new WReceiver();
                auMan =(AudioManager) getSystemService(Context.AUDIO_SERVICE);
                registerReceiver(mRecv, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mTimer.scheduleAtFixedRate(mTask, 0, period);
                makeToast("service created");
               // mWifi.startScan();
        }

        void setLow()
        {
                int currVol = auMan.getStreamVolume(AudioManager.STREAM_RING);
                maxVol = auMan.getStreamMaxVolume(AudioManager.STREAM_RING);
                while(currVol != 0)
                {
                        auMan.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                        currVol = auMan.getStreamVolume(AudioManager.STREAM_RING);
                }
                auMan.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }

        void setHigh()
        {
                int currVol = auMan.getStreamVolume(AudioManager.STREAM_RING);
                maxVol = auMan.getStreamMaxVolume(AudioManager.STREAM_RING);
                while(currVol != maxVol)
                {
                        auMan.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                        currVol = auMan.getStreamVolume(AudioManager.STREAM_RING);
                }
                auMan.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }

        class WReceiver extends BroadcastReceiver
        {
                public void onReceive(Context c, Intent intent)
                {
                        //makeToast("scan");
                        sPref = getSharedPreferences(launchActivity.appKey, MODE_PRIVATE);
                        strB = sPref.getStringSet(wifiList.keyBlack, strB);
                        strW = sPref.getStringSet(wifiList.keyWhite, strW);
                        mwList = mWifi.getScanResults();
                        //makeToast("Scanned");
                        boolean isWhite = false, isBlack = false;
                        for (ScanResult aMwList : mwList)
                        {
                                String tmpSSID = "\"" + aMwList.SSID + "\"";
                                isBlack = strB.contains(tmpSSID);
                                if (isBlack)
                                {
                                        //makeToast("blacklisted network found");
                                        setLow();
                                        break;
                                }
                        }
                        if(!isBlack)
                        {
                                for (ScanResult aMwList : mwList)
                                {
                                        String tmpSSID = "\"" + aMwList.SSID + "\"";
                                        isWhite = strW.contains(tmpSSID);
                                        if (isWhite)
                                        {
//                                                makeToast("whitelisted network found");
                                                setHigh();
                                                break;
                                        }
                                }
                        }
                }
        }

        void makeToast(String msg)
        {
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
}