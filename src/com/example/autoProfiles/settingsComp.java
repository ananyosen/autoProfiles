package com.example.autoProfiles;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.w3c.dom.Text;

/**
 * Created by Ananyo on 4/10/2015.
 */
public class settingsComp extends Activity
{
        Spinner spnAuMode;
        Button btnDnPref, btnCncPref;
        SeekBar mSeekBar;
        TextView textSeek;
        AudioManager mAuMan;
        int spIndex = 0;
        int maxVol = 0;
        int currVol = maxVol;
        SharedPreferences shPref;
        public static String volKey = "NORMAL_VOLUME";
        public static String blackKey = "BLACKLISTED_MODE";
        SharedPreferences.Editor editor;
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.settings);
                shPref = getSharedPreferences(launchActivity.appKey, MODE_PRIVATE);
                spnAuMode = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.arrSpn, R.layout.textview);
                adapter.setDropDownViewResource(R.layout.textview);
                spnAuMode.setAdapter(adapter);
                spIndex = shPref.getInt(blackKey, 0);
                spnAuMode.setSelection(spIndex);
                btnCncPref = (Button) findViewById(R.id.buttonCncPref);
                btnDnPref = (Button) findViewById(R.id.buttonDnPref);
                mAuMan = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                maxVol = mAuMan.getStreamMaxVolume(AudioManager.STREAM_RING);
                mSeekBar = (SeekBar) findViewById(R.id.seekBarVol);
                mSeekBar.setMax(maxVol);
                currVol = shPref.getInt(volKey, maxVol);
                mSeekBar.setProgress(currVol);
                textSeek = (TextView) findViewById(R.id.textViewSeekb);
                textSeek.setText(currVol + " / "+ maxVol);
                btnDnPref.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View view)
                        {
                                saveNFinish();
                        }
                });
                btnCncPref.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View view)
                        {
                                cancelNFinish();
                        }
                });
                mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
                {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromuser)
                        {
                                currVol = progress;
                                textSeek.setText(progress + " / "+ maxVol);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar)
                        {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar)
                        {

                        }
                });
        }

        void saveNFinish()
        {
                boolean writeA = false, writeB = false;
                spIndex = spnAuMode.getSelectedItemPosition();
                writeA = shPref.edit().putInt(volKey, currVol).commit();
                writeB = shPref.edit().putInt(blackKey, spIndex).commit();
                //makeToast(""+spIndex);
                if (writeA && writeB)
                {
                        this.setResult(Activity.RESULT_OK);
                }
                else
                {
                        this.setResult(Activity.RESULT_CANCELED);
                }
                finish();
        }

        void cancelNFinish()
        {
                this.setResult(Activity.RESULT_CANCELED);
                finish();
        }

        void makeToast(String msg)
        {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
}