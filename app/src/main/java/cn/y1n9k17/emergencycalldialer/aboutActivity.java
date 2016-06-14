package cn.y1n9k17.emergencycalldialer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class aboutActivity extends AppCompatActivity {

    public final static int RESULT_CODE = 1;
    private int STINGERFLAG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        SharedPreferences spf = getSharedPreferences("phonesmsinfo", MODE_PRIVATE);
        final SharedPreferences.Editor edit = spf.edit();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String phonenum = bundle.getString("phone");
        String smstext = bundle.getString("sms");

        TextView stingerView = (TextView)findViewById(R.id.textView6);
        stingerView.setText("设定的电话号码为："+phonenum+"\n预设短信内容为："+smstext);
        Button aboutBackButton = (Button)findViewById(R.id.button9);
        aboutBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(STINGERFLAG == 1){
                    intent.putExtra("backmsg", "已找到彩蛋！");
                } else {
                    intent.putExtra("backmsg", "测试成功！");
                }
                setResult(RESULT_CODE, intent);
                finish();
            }
        });

        Button initButton = (Button)findViewById(R.id.button11);
        initButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.clear();
                if(edit.commit()){
                    Toast.makeText(aboutActivity.this, "数据清理完成！", Toast.LENGTH_LONG).show();
                }
            }
        });

        TextView stingerButton = (TextView)findViewById(R.id.textView5);
        stingerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/yingkittse");
                Intent sintent = new Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(sintent);
                STINGERFLAG = 1;
            }
        });



    }
}
