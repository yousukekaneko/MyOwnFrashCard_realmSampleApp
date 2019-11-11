package com.example.android.sample.myownfrashcard

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

//モデルクラスの作成
open class WordDB : RealmObject() {
    //フィールドの設定

    @PrimaryKey
    // 問題
    var strQuestion: String = ""

    // 答え
    var strAnswer : String = ""

    // 暗記済みフラグ
    var boolMemoryFlag : Boolean = false

}