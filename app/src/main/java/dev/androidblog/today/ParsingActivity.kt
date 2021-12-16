package dev.androidblog.today

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.time.format.DateTimeFormatter


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
        setContentView(R.layout.activity_parsing)

        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            Log.d("TEST", "START!")
            parsingFunction()
        }

        val btnUpload = findViewById<Button>(R.id.btn_upload)
        btnUpload.setOnClickListener {
            upload()
        }

        val btn_read = findViewById<Button>(R.id.btn_read)
        btn_read.setOnClickListener {
            read()
        }

    }

    private fun upload() {
        val database = Firebase.database
        val images = database.getReference("images")

        lifecycleScope.launch {
            repeat(10) { index ->
                val image = Image(
                    "컨텐츠 $index",
                    "https://www.naver.com",
                    index
                )

                images.push().setValue(image)
            }
        }

    }

    private fun read() {
        val database = Firebase.database.reference
        database.child("images").orderByChild("date").startAt(5.toDouble()).addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TEST_", "onChildAdded : start")

                snapshot.children.forEach {
                    Log.d("TEST_", "onChildAdded : ${it.toString()}")

                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TEST_", "onChildChanged : ")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("TEST_", "onChildRemoved : ")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TEST_", "onChildMoved : ")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TEST_", "onCancelled : ")
            }

        })

    }

    ////////////
    private var page = 500

    private fun parsingFunction() {
        lifecycleScope.launch(Dispatchers.IO) {
            repeat(10) {
                process(page)
                page++
                delay(500)
                Log.d("TEST", "=================END=================")
            }
        }
    }

    private fun process(page: Int) {
        val table = str.getElementById("content_table")
        val h1 = table.select("h1")
        val h2 = table.select("h2")
        val img = table.select("img")[0].absUrl("src")

        Log.d("TEST", "title : " + h1.text())
        var index = 0

        val spanP = h2.select("span")
        spanP.forEachIndexed { i, item ->
            val span = item.select("span")
            Log.d("TEST", "conts : " + span.text())

            // 줄바꿈체크
            val next = span.next()
            val nextnext = next.next()
            if (next.toString() == "<br>" && nextnext.toString() == "<br>") {
                Log.d("TEST", "=")
            }

        }

        Log.d("TEST", "image : $img")
    }
}