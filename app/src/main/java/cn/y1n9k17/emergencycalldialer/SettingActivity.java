package cn.y1n9k17.emergencycalldialer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SettingActivity extends AppCompatActivity {

    private EditText phonenum;
    private EditText smstext;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACT = 3;
    private static final int MY_INTENT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SharedPreferences spf = getSharedPreferences("phonesmsinfo", MODE_PRIVATE);
        final SharedPreferences.Editor edit = spf.edit();

        Button settingbackButton = (Button)findViewById(R.id.button6);
        settingbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*******
                Intent intent = new Intent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                //finish();
                 ******/
                finish();
            }
        });

        phonenum = (EditText)findViewById(R.id.editText);
        smstext = (EditText)findViewById(R.id.editText2);
        if(!spf.contains("phone") || spf.getString("phone", null) == "") {
            Toast.makeText(SettingActivity.this, "你未设置警告号码或短信内容！", Toast.LENGTH_SHORT).show();
        }
        else{
            phonenum.setText(spf.getString("phone", null));
            smstext.setText(spf.getString("sms", null));
        }

        Button mChooseContactButton = (Button)findViewById(R.id.button8);
        mChooseContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseContact();
            }
        });

        Button saveButton = (Button)findViewById(R.id.button7);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putString("sms", smstext.getText().toString());
                edit.putString("phone", phonenum.getText().toString());
                if(edit.commit()){
                    Toast.makeText(SettingActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button aboutButton = (Button)findViewById(R.id.button3);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(SettingActivity.this, aboutActivity.class);
                intent.putExtra("phone", phonenum.getText().toString());
                intent.putExtra("sms", smstext.getText().toString());
                startActivityForResult(intent,MY_INTENT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case(0):
                if(data == null){
                    return;
                }
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                String number = cursor.getString(cursor.getColumnIndexOrThrow(Contacts.Phones.NUMBER));
                System.out.println("number:"+number);
                phonenum.setText(number);
                phonenum.setSelection(number.length());
                break;
            case(MY_INTENT_REQUEST_CODE):
                if(resultCode == aboutActivity.RESULT_CODE){
                    Bundle bundle = data.getExtras();
                    String str = bundle.getString("backmsg");
                    Toast.makeText(SettingActivity.this, str, Toast.LENGTH_LONG).show();
                }
            default:
                break;
        }
    }

    public void chooseContact(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},MY_PERMISSIONS_REQUEST_READ_CONTACT );
        }else {
            Intent i = new Intent(Intent.ACTION_PICK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setType("vnd.android.cursor.dir/phone");
            startActivityForResult(i, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACT){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //TODO:DO SOMETHING
                chooseContact();
            } else {
                //Permission Denied
                Toast.makeText(SettingActivity.this, "授权拒绝。\n请到设置->应用程序中打开对应权限。", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
