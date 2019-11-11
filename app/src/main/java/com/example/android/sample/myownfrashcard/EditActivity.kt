package com.example.android.sample.myownfrashcard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    lateinit var realm : Realm

    var strQuestion : String = ""
    var strAnswer : String = ""
    var intPosition : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val bundle: Bundle = intent.extras
        val strStatus = bundle.getString(getString(R.string.intent_key_status))
        textViewStatus.text = strStatus

        if (strStatus == getString(R.string.status_change)) {
            strQuestion = bundle.getString(getString(R.string.intent_key_question))
            strAnswer = bundle.getString(getString(R.string.intent_key_answer))

            editTextQuestion.setText(strQuestion)
            editText2.setText(strAnswer)
            intPosition = bundle.getInt(getString(R.string.intent_key_position))

            editTextQuestion.isEnabled = false

        } else {
            editTextQuestion.isEnabled = true
        }

        constraintLayoutEdit.setBackgroundResource(intBackGroundColor)

        buttonRegister.setOnClickListener {

            if (strStatus == getString(R.string.status_add)) {
                addNewWord()
            } else {
                changeWord()
            }

        }

        buttonBack2.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()

        // Realmインスタンスの取得
        realm = Realm.getDefaultInstance()
    }

    override fun onPause() {
        super.onPause()

        realm.close()
    }

    private fun changeWord() {
        val results = realm.where(WordDB::class.java).findAll().sort(getString(R.string.db_field_question))
        val selectedDB = results[intPosition]!!
        
        val dialog = AlertDialog.Builder(this@EditActivity).apply { 
            setTitle(selectedDB.strAnswer + "の変更")
            setMessage("変更してもいいですか？")
            setPositiveButton("はい") { dialogInterface, i ->
                realm.beginTransaction()
//        selectedDB.strQuestion = editTextQuestion.text.toString()
                selectedDB.strAnswer = editText2.text.toString()
                selectedDB.boolMemoryFlag = false
                realm.commitTransaction()

                editTextQuestion.setText("")
                editText2.setText("")

                Toast.makeText(this@EditActivity,"修正が完了しました", Toast.LENGTH_SHORT).show()

                finish()
            }
            setNegativeButton("いいえ") {dialogInterface, i ->  }
            show()
        }

        
    }

    private fun addNewWord() {

        val dialog = AlertDialog.Builder(this@EditActivity).apply {
            setTitle("登録")
            setMessage("登録してもいいですか？")
            setPositiveButton("はい") { dialogInterface, i ->

                try {
                    // 新しい単語の登録処理
                    realm.beginTransaction() //開始処理

                    val wordDB = realm.createObject(WordDB::class.java, editTextQuestion.text.toString())
//        wordDB.strQuestion = editTextQuestion.text.toString()
                    wordDB.strAnswer = editText2.text.toString()
                    wordDB.boolMemoryFlag = false

                    Toast.makeText(this@EditActivity,"登録が完了しました", Toast.LENGTH_SHORT).show()

                } catch (e: RealmPrimaryKeyConstraintException) {

                    Toast.makeText(this@EditActivity,"その単語はすでに登録されています", Toast.LENGTH_SHORT).show()

                } finally {

                    editTextQuestion.setText("")
                    editText2.setText("")
                    realm.commitTransaction() //終了処理
                }


            }
            setNegativeButton("いいえ") { dialogInterface, i ->  }
            show()
        }


    }


}
