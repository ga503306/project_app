package com.example.a20130830.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class login extends AppCompatActivity {
    Button btn;
    EditText id_edit,pass_edit;
    String id,password;

   Handler G_Handler = new Handler(){
       @Override
       public void handleMessage(Message msg){
           super.handleMessage(msg);
           if(msg.what==1){
               String data = (String)msg.obj;
               if(data.equals("t")) {
                   Toast.makeText(login.this, "Success", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent();

                   Bundle bundle = new Bundle();//傳學號
                   bundle.putString("key",(id_edit.getText().toString()));
                   intent.putExtras(bundle);

                   intent.setClass(login.this, MainActivity.class);
                   startActivity(intent);
                   login.this.finish();
               }else if(data.equals("f")){
                   Toast.makeText(login.this,"Fail", Toast.LENGTH_SHORT).show();
               }
               }
           }

   };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn= (Button) findViewById(R.id.btn);
        id_edit= (EditText) findViewById(R.id.id_edit);
        pass_edit= (EditText) findViewById(R.id.pass_edit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData(id_edit.getText().toString(), pass_edit.getText().toString());
                Toast.makeText(login.this,"sent", Toast.LENGTH_SHORT).show();
            }
        });
    }
        public void SendData(final  String id, final String password) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://210.240.202.109/applogin.php");
                        //TODO:網址
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(15000);
                        conn.setRequestMethod("POST");
                        //TODO:若需使用put 之類的，在這裡變更
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                        //TODO: outData1 請輸入要跟在網址後面的指令

                        PrintWriter out = new PrintWriter(conn.getOutputStream());

                        //TODO: outData1 請輸入要跟在網址後面的指令
                        String outData1 = "password=" + password + "&id=" + id;
                        out.print(outData1);
                        out.close();
                        conn.connect();

                        InputStream input = conn.getInputStream();
                        try {
                            Thread.sleep(2000);
                        } catch (Exception ex) {
                        }

                        int len = 0;
                        byte[] buffer = new byte[4096];

                        len = input.read(buffer);
                        byte[] data = new byte[len];
                        System.arraycopy(buffer, 0, data, 0, len);
                        String a = new String(data);

                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = a;
                        G_Handler.sendMessage(msg);


                    } catch (IOException e) {

                    }
                }
            }).start();
        }


}
