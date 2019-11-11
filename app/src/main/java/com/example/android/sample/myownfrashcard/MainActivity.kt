package com.example.android.sample.myownfrashcard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

var intBackGroundColor = 0

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 単語の編集ボタンを押した時の処理
        buttonEdit.setOnClickListener {
            val intent = Intent(this@MainActivity, WordListActivity::class.java)
            startActivity(intent)
        }

        // 画面の背景色を洗濯したボタンの色に変更する

        buttonColor01.setOnClickListener {
            intBackGroundColor = R.color.color01
            constraintLayoutMain.setBackgroundResource(intBackGroundColor)
        }
        buttonColor02.setOnClickListener {
            intBackGroundColor = R.color.color02
            constraintLayoutMain.setBackgroundResource(intBackGroundColor)
        }
        buttonColor03.setOnClickListener {
            intBackGroundColor = R.color.color03
            constraintLayoutMain.setBackgroundResource(intBackGroundColor)
        }
        buttonColor04.setOnClickListener {
            intBackGroundColor = R.color.color04
            constraintLayoutMain.setBackgroundResource(intBackGroundColor)
        }
        buttonColor05.setOnClickListener {
            intBackGroundColor = R.color.color05
            constraintLayoutMain.setBackgroundResource(intBackGroundColor)
        }
        buttonColor06.setOnClickListener {
            intBackGroundColor = R.color.color06
            constraintLayoutMain.setBackgroundResource(intBackGroundColor)
        }


        buttonTest.setOnClickListener{
            val intent = Intent(this@MainActivity, TestActivity::class.java)
                when (radioGroup.checkedRadioButtonId) {
                    //暗記済みの単語を除外する
                    R.id.radioButton -> intent.putExtra(getString(R.string.intent_key_memory_flag), true)
                    //暗記済みの単語を除外しない
                    R.id.radioButton2 -> intent.putExtra(getString(R.string.intent_key_memory_flag), false)
                }
            startActivity(intent)
        }

    }
}
