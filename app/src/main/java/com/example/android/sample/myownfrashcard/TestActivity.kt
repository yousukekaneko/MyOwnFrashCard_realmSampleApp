package com.example.android.sample.myownfrashcard

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*
import kotlin.collections.ArrayList

class TestActivity : AppCompatActivity(), View.OnClickListener {

    var booleanStatusMemory: Boolean = false
    //問題を暗記済みにするかどうか
    var boolMemorized: Boolean = false

    //テストの状態
    var intStatus : Int = 0
    val BEFORE_START : Int = 1
    val RUNNNING_QUESTION : Int = 2
    val RUNNNING_ANSWER: Int = 3
    val TEST_FINISHED : Int = 4

    lateinit var realm: Realm
    lateinit var result: RealmResults<WordDB>
    lateinit var word_list : ArrayList<WordDB>

    var intLength : Int = 0 //レコードの数(テストの問題数)
    var intCount : Int = 0 //今何門目を示すカウンター


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        //画面が開いた時

        val bundle = intent.extras
        booleanStatusMemory = bundle.getBoolean(getString(R.string.intent_key_memory_flag))

        constraintLayoutTest.setBackgroundResource(intBackGroundColor)

        //テスト状態を「開始前」に＋画像を非表示に
        intStatus = BEFORE_START
        imageViewFlashQuestion.visibility = View.INVISIBLE
        imageViewFlashAnswer.visibility = View.INVISIBLE

        //ボタンをテストを始めるに変える
        buttonNext.setBackgroundResource(R.drawable.image_button_test_start)

        //ボタンを確認テストをやめるに変える
        buttonEndTest.setBackgroundResource(R.drawable.image_button_end_test)

        buttonNext.setOnClickListener(this)

        buttonEndTest.setOnClickListener(this)

        checkBox.setOnClickListener {

//            if (checkBox.isChecked) boolMemorized = true else boolMemorized = false
//            上記の条件分岐を以下のように書き換えられる
            boolMemorized = checkBox.isChecked
        }
    }

    override fun onResume() {
        super.onResume()

        //realmインスタンスの取得
        realm = Realm.getDefaultInstance()

        if (booleanStatusMemory) {
            //暗記済みの単語を除外する
            result = realm.where(WordDB::class.java).equalTo(getString(R.string.db_field_memory_flag), false).findAll()
        } else {
            //暗記済みの単語を除外しない
            result = realm.where(WordDB::class.java).findAll()
        }

        intLength = result.size
        textViewRemaining.text = intLength.toString()

        //取得したテストデータをシャッフルする

        word_list = ArrayList(result)
        Collections.shuffle(word_list)
    }

    override fun onPause() {
        super.onPause()

        //realmの終了処理
        realm.close()
    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.buttonNext ->

                when (intStatus) {

                    //テスト開始前
                    BEFORE_START -> {
                        intStatus = RUNNNING_QUESTION
                        showQuestion()
                    }

                    RUNNNING_QUESTION -> {
                        intStatus = RUNNNING_ANSWER
                        showAnswer()
                    }

                    RUNNNING_ANSWER -> {
                        intStatus = RUNNNING_QUESTION
                        showQuestion()
                    }

                }

        R.id.buttonEndTest -> {
            AlertDialog.Builder(this@TestActivity).apply {
                setTitle("テストの終了")
                setMessage("テストを終了してもいいですか？")
                setPositiveButton("はい") {dialogInterface, i ->
                    if (intStatus == TEST_FINISHED) {
                        val selectDB = realm.where(WordDB::class.java).equalTo(getString(R.string.db_field_question), word_list[intCount -1].strQuestion).findFirst()!!
                        realm.beginTransaction()
                        selectDB.boolMemoryFlag = boolMemorized
                        realm.commitTransaction()
                    }

                    finish()
                }
                setNegativeButton("いいえ") {dialogInterface, i ->  }
                show()
            }
        }

        }

    }

    private fun showAnswer() {

        imageViewFlashAnswer.visibility = View.VISIBLE
        textViewFlashAnswer.text = word_list[intCount - 1].strAnswer

        buttonNext.setBackgroundResource(R.drawable.image_button_go_next_question)

        //最後の問題まできたら＝Length==intCountの条件にする
        if (intLength == intCount) {
            intStatus = TEST_FINISHED
            textViewMessage.text = "テスト終了"

            //ボタンを見えなくして使えなくする
            buttonNext.isEnabled = false
            buttonNext.visibility = View.INVISIBLE

            buttonEndTest.setBackgroundResource(R.drawable.image_button_back)
        }
    }

    private fun showQuestion() {

        if (intCount > 0) {

            val selectDB = realm.where(WordDB::class.java).equalTo(getString(R.string.db_field_question), word_list[intCount -1].strQuestion).findFirst()!!
            realm.beginTransaction()
                selectDB.boolMemoryFlag = boolMemorized
            realm.commitTransaction()

        }

        intCount ++
        textViewRemaining.text = (intLength - intCount).toString()

        imageViewFlashAnswer.visibility = View.INVISIBLE
        textViewFlashAnswer.text = ""
        imageViewFlashQuestion.visibility = View.VISIBLE
        textViewFlashQuestion.text = word_list[intCount - 1].strQuestion

        buttonNext.setBackgroundResource(R.drawable.image_button_go_answer)

        //問題の単語が暗記済みの場合はチェックを入れる
        checkBox.isChecked = word_list[intCount - 1].boolMemoryFlag
        boolMemorized = checkBox.isChecked
    }


}
