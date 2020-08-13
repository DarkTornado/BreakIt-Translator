package com.darktornado.euckr2shiftjis

import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import android.text.Html
import android.widget.TextView
import android.graphics.Color
import java.io.BufferedReader
import java.io.InputStreamReader

class InfoActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar.title = "앱 정보"
        actionBar.setDisplayShowHomeEnabled(false);
        val layout = LinearLayout(this)
        layout.orientation = 1

        val txt = TextView(this)
        txt.text = Html.fromHtml("앱 이름 : 뷁어 번역기<br>버전 : 1.0<br>제작자 : Dark Tornado<br><br>&nbsp;'뷁어'라고 불리는 깨진 일본어를 한국어로 번역하거나, 그 반대로 해주는 앱입니다. 번역 버튼을 길게 누르면 한국어 대신 일본어로 번역되거나 합니다.<br>&nbsp;한국어에서 뷁어로 또는 뷁어에서 한국어로 번역하기 위해서는 인터넷 연결이 필요합니다.<br><br><br>" +
                "<b><big>오픈 소스 라이선스</big><br><br>jsoup</b><br><br>" + loadLicense().replace("\n", "<br>"))
        txt.setTextColor(Color.BLACK)
        txt.textSize = 17f
        layout.addView(txt)

        val pad = dip2px(20)
        layout.setPadding(pad, pad, pad, dip2px(60))
        val scroll = ScrollView(this)
        scroll.addView(layout)
        setContentView(scroll)
    }


    private fun loadLicense(): String {
        try {
            val isr = InputStreamReader(assets.open("jsoup.txt"))
            val br = BufferedReader(isr)
            var str = br.readLine()
            var line = br.readLine()
            while (line != null) {
                str += "\n" + line
                line = br.readLine()
            }
            isr.close()
            br.close()
            return str
        } catch (e: Exception) {
            return "[라이선스 정보 불러오기 실패]"
        }
    }

    private fun dip2px(dips: Int): Int {
        return Math.ceil((dips * this.resources.displayMetrics.density).toDouble()).toInt()
    }
}
