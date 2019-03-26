package com.example.a20130830.myapplication;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter adapter;
    private NdefMessage mMessage;
    private Button btn;
    private TextView tv;
    Tag mytag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        tv = (TextView) findViewById(R.id.tv);
        btn.setOnClickListener(go);
        adapter = NfcAdapter.getDefaultAdapter(this);
        Bundle bundle =getIntent().getExtras();
        String id = bundle.getString("key");

        if (adapter != null) {
            //adapter.setNdefPushMessage(mMessage, this);
            Toast.makeText(this, "already set ndefmessage", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "adapter is null", Toast.LENGTH_LONG).show();
        }


        mMessage = new NdefMessage(createTextRecord(id, Locale.getDefault(),true));
    }
    public NdefRecord createTextRecord(String payload, Locale locale, boolean encodeInUtf8) {

        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = payload.getBytes(utfEncoding);
        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return record;
    }




    //@SuppressWarnings("deprecation")
    @Override
    public void onResume(){
        super.onResume();
        if(adapter != null) adapter.enableForegroundNdefPush(this,mMessage);

    }

    //@SuppressWarnings("deprecation")
    @Override
    public void onPause(){
        super.onPause();
        if(adapter != null) adapter.disableForegroundNdefPush(this);
    }
    private Button.OnClickListener go = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, login.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    };

}
