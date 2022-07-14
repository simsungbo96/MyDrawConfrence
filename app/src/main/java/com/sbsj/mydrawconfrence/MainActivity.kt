package com.sbsj.mydrawconfrence

import android.graphics.Paint
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.sbsj.mydrawconfrence.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity(){

    private lateinit var activityMainBinding : ActivityMainBinding
    lateinit var paintView : PaintView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.DrawFrameLayout,DrawFragment())
            .commit()



    }


}