package dev.androidblog.today

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import timber.log.Timber


// todo 이미지 업로드 : https://firebase.google.com/docs/storage/android/upload-files?authuser=0


/**
 * 1. 데이터 파싱
 * 2. 데이터 검증
 * 3. 이미지 업로드
 * 4. 업로드한 위치를 받아 DB에 저장
 * 5. 1로 돌아감
 */

/**
 * 1. 데이터 파싱
 * 2. 데이터 검증
 * 3. 데이터 업로드
 * 4. 이후에 스토리지에 저장
 */

class ParsingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        setContentView(R.layout.activity_parsing)

        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            Timber.d("START!")
            function()
        }

    }

    ////////////
    private var page = 500

    private fun function() {
        lifecycleScope.launch(Dispatchers.IO) {
            repeat(10) {
                process(page)
                page++
                delay(500)
                Timber.d("=================END=================")
            }
        }
    }

    private fun process(page: Int) {
        val table = str.getElementById("content_table")
        val h1 = table.select("h1")
        val h2 = table.select("h2")
        val img = table.select("img")[0].absUrl("src")

        Timber.d("title : " + h1.text())
        var index = 0

        val spanP = h2.select("span")
        spanP.forEachIndexed { i, item ->
            val span = item.select("span")
            Timber.d("conts : " + span.text())

            // 줄바꿈체크
            val next = span.next()
            val nextnext = next.next()
            if (next.toString() == "<br>" && nextnext.toString() == "<br>") {
                Timber.d("=")
            }

        }

        Timber.d("image : $img")
    }
}