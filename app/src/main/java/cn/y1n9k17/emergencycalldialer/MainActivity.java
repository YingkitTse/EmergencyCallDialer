package cn.y1n9k17.emergencycalldialer;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String gphonenum;
    private String gsms;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences spf = getSharedPreferences("phonesmsinfo", MODE_PRIVATE);
        if (!spf.contains("phone") || !spf.contains("sms")) {
            Toast.makeText(MainActivity.this, "你还没设置警告电话和警告信息！", Toast.LENGTH_LONG).show();
        } else {
            gphonenum = spf.getString("phone", "10010");
            gsms = spf.getString("sms", "cxll");
        }

        Button settingButton = (Button) findViewById(R.id.button);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentjump();
            }
        });

        Button exitButton = (Button) findViewById(R.id.button2);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        Button smsButton = (Button) findViewById(R.id.button5);
        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gphonenum == "" || gphonenum == null) {
                    Toast.makeText(MainActivity.this, "请先设置警告电话！", Toast.LENGTH_LONG).show();
                } else {
                    sendSMS();
                }
            }
        });

        Button dialButton = (Button) findViewById(R.id.button4);
        dialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gphonenum == "" || gphonenum == null) {
                    Toast.makeText(MainActivity.this, "请先设置警告电话！", Toast.LENGTH_LONG).show();
                } else {
                    makeCall();
                }
            }
        });

    }

    public void makeCall(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + gphonenum));
            startActivity(intent);
        }
    }

    public void sendSMS(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }else{
            SmsManager smsManager = SmsManager.getDefault();
            //PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, new Intent(), 0);
            ArrayList<String> msgContents = smsManager.divideMessage(gsms);
            for(String s:msgContents){
                smsManager.sendTextMessage(gphonenum, null, gsms, null, null);
            }
            //smsManager.sendTextMessage(gphonenum, null, gsms, pIntent, null);
            Toast.makeText(MainActivity.this, "短信已经发送！！", Toast.LENGTH_LONG).show();
        }
    }

    public void intentjump(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                makeCall();
            } else {
                //Permission Denied
                Toast.makeText(MainActivity.this, "授权拒绝。\n请到设置->应用程序中打开对应权限。", Toast.LENGTH_LONG).show();
            }
            return;
        }
        if(requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                sendSMS();
            } else {
                //Permission Denied
                Toast.makeText(MainActivity.this, "授权拒绝。\n请到设置->应用程序中打开对应权限。", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
