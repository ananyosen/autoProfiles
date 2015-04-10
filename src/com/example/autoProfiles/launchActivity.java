package com.example.autoProfiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Ananyo on 4/9/2015.
 */
public class launchActivity extends Activity
{
        /**
         * Called when the activity is first created.
         */
        String TAG = "aupro.launch";
        public static final String intent_key = "BLACK_WHITE";
        Button bWhite, bBlack;
        int requestResult = 391;
        public static final String appKey = "com.ananyo.autoprof";
        SharedPreferences sharedPref;
        SharedPreferences.Editor editor;
        AudioManager auMan;
        int maxVol = 0;
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.main);
                bWhite = (Button) findViewById(R.id.buttonWhite);
                bBlack = (Button) findViewById(R.id.buttonBlack);
                sharedPref = getSharedPreferences(launchActivity.appKey, MODE_PRIVATE);
                auMan = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                bWhite.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View view)
                        {
                                startForWhite();
                        }
                });
                bBlack.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View view)
                        {
                                startForBlack();
                        }
                });

                startProfileMan();
        }

        void startForWhite()
        {
                Intent i = new Intent(launchActivity.this, wifiList.class);
                i.putExtra(intent_key, false);
                startActivityForResult(i, requestResult);
        }

        void startForBlack()
        {
                Intent i = new Intent(launchActivity.this, wifiList.class);
                i.putExtra(intent_key, true);
                startActivityForResult(i, requestResult);
        }

        void startProfileMan()
        {
                Intent i = new Intent(launchActivity.this, profileMan.class);
                startService(i);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data)
        {
                if (requestCode == requestResult)
                {
                        if (resultCode == RESULT_OK)
                        {
                                makeToast("Done setting preferences");
                        }
                }
        }

        void makeToast(String msg)
        {
                Toast.makeText(launchActivity.this, msg, Toast.LENGTH_SHORT).show();
        }

        void sendLog(String msg)
        {
                Log.d(TAG, msg);
        }
}
