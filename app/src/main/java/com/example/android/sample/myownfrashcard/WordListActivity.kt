package com.example.android.sample.myownfrashcard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_word_list.*

class WordListActivity : AppCompatActivity(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    lateinit var realm : Realm
    lateinit var results: RealmResults<WordDB>
    lateinit var wordList: ArrayList<String>
    lateinit var adapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)

        constraintLayoutWordList.setBackgroundResource(intBackGroundColor)

        buttonAddNewWord.setOnClickListener {
            val intent = Intent(this@WordListActivity, EditActivity::class.java)
            intent.putExtra(getString(R.string.intent_key_status), getString(R.string.status_add))
            startActivity(intent)
        }

        buttonBack.setOnClickListener {
            finish()
        }

        buttonSort.setOnClickListener {

            results = realm.where(WordDB::class.java).findAll().sort(getString(R.string.db_field_memory_flag))

            wordList.clear()

            results.forEach {
                if (it.boolMemoryFlag) {
                    wordList.add(it.strQuestion + " : " + it.strAnswer + "【暗記済】")
                } else {
                    wordList.add(it.strQuestion + " : " + it.strAnswer)
                }
            }

            listView.adapter = adapter
        }

        listView.onItemClickListener = this
        listView.onItemLongClickListener = this

    }

    override fun onResume() {
        super.onResume()

        //realmインスタンスの取得
        realm = Realm.getDefaultInstance()
        results = realm.where(WordDB::class.java).findAll().sort(getString(R.string.db_field_answer))

        wordList = ArrayList()

        val length = results.size

//        for (i in 0 until length -1 ) {
//            if (results[i]!!.boolMemoryFlag) {
//                wordList.add(results[i]?.strAnswer + " : " + results[i]?.strQuestion + "【暗記済】")
//            } else {
//                wordList.add(results[i]?.strAnswer + " : " + results[i]?.strQuestion)
//            }
//        }

        results.forEach {
            if (it.boolMemoryFlag) {
                wordList.add(it.strQuestion + " : " + it.strAnswer + "【暗記済】")
            } else {
                wordList.add(it.strQuestion + " : " + it.strAnswer)
            }
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, wordList)
        listView.adapter = adapter
    }

    override fun onPause() {
        super.onPause()

        //realmの終了
        realm.close()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val selectedDB = results[position]!!
        val strSelectedQuestion = selectedDB.strQuestion
        val strSelectedAnswer = selectedDB.strAnswer

        val intent = Intent(this@WordListActivity, EditActivity::class.java).apply {
            putExtra(getString(R.string.intent_key_question), strSelectedQuestion)
            putExtra(getString(R.string.intent_key_answer), strSelectedAnswer)
            putExtra(getString(R.string.intent_key_position), position)
            putExtra(getString(R.string.intent_key_status), getString(R.string.status_change))
        }
        startActivity(intent)
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {

        val selectedDB = results[position]

        val dialog = AlertDialog.Builder(this@WordListActivity).apply {
            setTitle(selectedDB?.strAnswer + "の削除")
            setMessage("削除してもいいですか？")
            setPositiveButton("はい"){ dialogInterface, i ->
                realm.beginTransaction()
                selectedDB?.deleteFromRealm()
                realm.commitTransaction()

                wordList.removeAt(position)

                listView.adapter = adapter
            }
            setNegativeButton("いいえ"){ dialogInterface, i ->  
                show()
            }
        }



        return true
    }

}
