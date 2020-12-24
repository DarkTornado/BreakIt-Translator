package com.darktornado.euckr2shiftjis;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.darktornado.library.SimpleRequester;

import org.json.JSONArray;

import java.net.URLEncoder;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayShowHomeEnabled(false);
        TextView text = new TextView(this);
        text.setText("입력 : ");
        text.setTextSize(19);
        text.setTextColor(Color.BLACK);
        getActionBar().setCustomView(text);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);

        TextView txt1 = new TextView(this);
        final EditText txt2 = new EditText(this);
        TextView txt3 = new TextView(this);
        final EditText txt4 = new EditText(this);

        txt1.setText("입력 : ");
        txt1.setTextSize(19);
        txt1.setTextColor(Color.BLACK);
        layout.addView(txt1);
        txt2.setHint("번역할 내용 입력...");
        txt2.setTextColor(Color.BLACK);
        txt2.setHintTextColor(Color.GRAY);
        layout.addView(txt2);

        TextView txt = new TextView(this);
        txt.setText(" ");
        txt.setTextSize(15);
        layout.addView(txt);

        LinearLayout lay2 = new LinearLayout(this);
        lay2.setWeightSum(2);
        Button tok = new Button(this);
        tok.setText("뷁어를 한국어로");
        tok.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1));
        tok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = txt2.getText().toString();
                if(input.equals("")) toast("입력된 내용이 없습니다.");
                else convert2K(input, txt4);
            }
        });
        tok.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    String input = txt2.getText().toString();
                    if (input.equals("")) {
                        toast("입력된 내용이 없습니다.");
                    } else {
                        String result = new String(input.getBytes("EUC-KR"), "Shift-JIS");
                        txt4.setText(result);
                        toast("뷁어를 일본어로 변경하였습니다.");
                    }
                } catch (Exception e) {
                    toast(e.toString());
                }
                return true;
            }
        });
        lay2.addView(tok);
        Button tob = new Button(this);
        tob.setText("한국어를 뷁어로");
        tob.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1));
        tob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = txt2.getText().toString();
                if(input.equals("")) toast("입력된 내용이 없습니다.");
                else convert2B(input, txt4);
            }
        });
        tob.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    String input = txt2.getText().toString();
                    if (input.equals("")) {
                        toast("입력된 내용이 없습니다.");
                    } else {
                        String result = new String(input.getBytes("Shift-JIS"), "EUC-KR");
                        txt4.setText(result);
                        toast("일본어를 뷁어로 변경하였습니다.");
                    }
                } catch (Exception e) {
                    toast(e.toString());
                }
                return true;
            }
        });
        lay2.addView(tob);
        layout.addView(lay2);

        txt = new TextView(this);
        txt.setText(" ");
        txt.setTextSize(15);
        layout.addView(txt);

        txt3.setText("출력 : ");
        txt3.setTextSize(19);
        txt3.setTextColor(Color.BLACK);
        layout.addView(txt3);
        txt4.setHint("결과가 출력되는 곳...");
        txt4.setTextColor(Color.BLACK);
        txt4.setHintTextColor(Color.GRAY);
        layout.addView(txt4);

        txt = new TextView(this);
        txt.setText(" ");
        txt.setTextSize(15);
        layout.addView(txt);

        Button copy = new Button(this);
        copy.setText("클립보드로 복사");
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = txt4.getText().toString();
                if (value.equals("")) {
                    toast("복사할 내용이 없습니다.");
                } else {
                    ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText("label", value));
                    toast("복사되었습니다.");
                }
            }
        });
        layout.addView(copy);

        Button info = new Button(this);
        info.setText("앱 정보 & 도움말");
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
        layout.addView(info);

        TextView maker = new TextView(this);
        maker.setText("\n© 2020 Dark Tornado, All rights reserved.\n");
        maker.setTextSize(13);
        maker.setTextColor(Color.BLACK);
        maker.setGravity(Gravity.CENTER);
        layout.addView(maker);

        int pad = dip2px(20);
        layout.setPadding(pad, pad, pad, pad);
        ScrollView scroll = new ScrollView(this);
        scroll.addView(layout);
        setContentView(scroll);
    }

    private void convert2K(String input, final EditText output){
        try {
            final String value = new String(input.getBytes("EUC-KR"), "Shift-JIS");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result = translate("ja", "ko", value);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            output.setText(result);
                            toast("뷁어를 한국어로 번역하였습니다.");
                        }
                    });
                }
            }).start();
        }catch (Exception e){
            toast(e.toString());
        }
    }

    private void convert2B(final String input, final EditText output) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String value = translate("ko", "ja", input);
                    final String result = new String(value.getBytes("Shift-JIS"), "EUC-KR");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            output.setText(result);
                            toast("한국어를 뷁어로 번역하였습니다.");
                        }
                    });
                } catch (Exception e) {
                    toast(e.toString());
                }
            }
        }).start();
    }

    public String translate(String lang1, String lang2, String value) {
        try {
            String result = SimpleRequester.create("https://translate.googleapis.com/translate_a/single")
                    .method(SimpleRequester.METHOD_POST)
                    .data("client", "gtx")
                    .data("sl", lang1)
                    .data("tl", lang2)
                    .data("dt", "t")
                    .data("q", URLEncoder.encode(value, "UTF-8"))
                    .data("q", value)
                    .data("ie", "UTF-8")
                    .data("oe", "UTF-8")
                    .execute().getResponseBody();
            return new JSONArray(result).getJSONArray(0).getJSONArray(0).getString(0);
        } catch (Exception e) {
//            return e.toString();
            return "번역 실패";
        }
        //return null;
    }

    private int dip2px(int dips) {
        return (int) Math.ceil(dips * this.getResources().getDisplayMetrics().density);
    }


    public void toast(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
