package com.example.ccallctraning

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*
import kotlin.concurrent.schedule

class TestActivity : AppCompatActivity(), View.OnClickListener {


    var numberOfRemaining: Int = 0
    var numberOfCorrect: Int = 0
    lateinit var soundPool: SoundPool
    var numberOfQuestion: Int = 0

    var soundIdCorrect: Int = 0
    var soundIdIncorrect: Int = 0
    lateinit var timer: Timer;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        // テスト数を受け取り表示を行う。
        val bundle: Bundle = intent.extras
        numberOfQuestion = bundle.getInt("numberOfQuestion")
        textViewRemaining.text = numberOfQuestion.toString()

        numberOfRemaining = numberOfQuestion
        numberOfCorrect = 0
        // テスト画面を作ったら

        // 電卓ボタンが押されたら
        button0.setOnClickListener(this)
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        button_.setOnClickListener(this)
        buttonClear.setOnClickListener(this)
        // 答えボタンが押されましたら

        buttonAnswerCheck.setOnClickListener {
            if(textViewAnswer.text.toString()!=="" && textViewAnswer.text.toString()!=="-")
            answerCheck()
        }
        // 戻るボタン押されたら
        buttonBack.setOnClickListener {
//            val intent = Intent(this@TestActivity,MainActivity::class.java)
//            startActivity(intent)
            // この画面をfinish
            finish()
        }
        // いちもんめを出す
        question()

    }

    override fun onResume() {
        super.onResume()
        //効果音を出すためのクラス：Soundpoolの準備
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder().setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
                .setMaxStreams(1)
                .build()
        } else {
            SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        }
        soundIdCorrect = soundPool.load(this, R.raw.sound_correct, 1)
        soundIdIncorrect = soundPool.load(this, R.raw.sound_incorrect, 1)


        // Timer処理
        timer = Timer()

    }

    override fun onPause() {
        super.onPause()
        soundPool.release()
        timer.cancel()
    }

    private fun question() {
        // 戻るボタンを非活性
        buttonBack.isEnabled = false
        buttonAnswerCheck.isEnabled = true

        button0.isEnabled = true
        button1.isEnabled = true
        button2.isEnabled = true
        button3.isEnabled = true
        button4.isEnabled = true
        button5.isEnabled = true
        button6.isEnabled = true
        button7.isEnabled = true
        button8.isEnabled = true
        button9.isEnabled = true
        button_.isEnabled = true
        buttonClear.isEnabled = true


        // 2つの値をランダムで出す
        val random = Random()
        val leftNum = random.nextInt(100) + 1
        val raightNum = random.nextInt(100) + 1
        textViewRight.text = raightNum.toString()
        textViewLeft.text = leftNum.toString()

        when (random.nextInt(2) + 1) {
            1 -> textViewOperator.text = "+"
            2 -> textViewOperator.text = "-"
        }
        // 前の問題で入力した答えを消す
        textViewAnswer.text = ""

        // まる✗画像をみえないようにする
        imageView.visibility = View.INVISIBLE


    }


    private fun answerCheck() {
        //戻るボタン。答え合わせ電卓ボタンを使えなくする
        buttonBack.isEnabled = false
        button0.isEnabled = false
        button1.isEnabled = false
        button2.isEnabled = false
        button3.isEnabled = false
        button4.isEnabled = false
        button5.isEnabled = false
        button6.isEnabled = false
        button7.isEnabled = false
        button8.isEnabled = false
        button9.isEnabled = false
        buttonClear.isEnabled = false
        button_.isEnabled = false
        buttonAnswerCheck.isEnabled = false

        // のこり問題数をへらす
        numberOfRemaining -= 1
        textViewRemaining.text = numberOfRemaining.toString()

        //まつバツ画像表示
        imageView.visibility = View.VISIBLE

        // 答え合わせ
        val intMyAnwer: Int = textViewAnswer.text.toString().toInt()
        val intRealAnser: Int = if (textViewAnswer.text === "+") {
            textViewLeft.text.toString().toInt() + textViewRight.text.toString().toInt()

        } else {
            textViewLeft.text.toString().toInt() - textViewRight.text.toString().toInt()
        }
        if (intMyAnwer == intRealAnser) {
            numberOfCorrect += 1
            textViewCorrect.text = numberOfCorrect.toString()
            imageView.setImageResource(R.drawable.pic_correct)
            soundPool.play(soundIdCorrect, 1.0f, 1.0f, 1, 0, 1.0f)


        } else {
            imageView.setImageResource(R.drawable.pic_incorrect)
            soundPool.play(soundIdIncorrect, 1.0f, 1.0f, 1, 0, 1.0f)


        }
        // 正答率を計算して表示
        val intPoint: Int =
            ((numberOfCorrect.toDouble() / (numberOfQuestion - numberOfRemaining).toDouble()) * 100).toInt()
        textViewPoint.text = intPoint.toString()


        if (numberOfRemaining == 0) {
            // 残り問題数がなくなった場合

            buttonBack.isEnabled = true
            buttonAnswerCheck.isEnabled = false

            textViewMessage.text = "テスト完了"
        } else {
            // 残り問題数がある場合一秒後に値表示
            timer.schedule(1000,{
                runOnUiThread{question()}
            })

        }
    }

    // ぼたんおされたらの処理
    override fun onClick(v: View?) {
        val button: Button = v as Button
        when (v?.id) {
            R.id.buttonClear -> textViewAnswer.text = ""
            R.id.button_ -> if (textViewAnswer.text.toString() == "") textViewAnswer.text = "-"
            R.id.button0 -> if (textViewAnswer.text.toString() != "0" && textViewAnswer.text != "-") textViewAnswer.append(
                button.text
            )
            else ->
                if (textViewAnswer.text.toString() == "0") {
                    textViewAnswer.text = button.text
                } else {
                    textViewAnswer.append(button.text)
                }
        }
    }
}


