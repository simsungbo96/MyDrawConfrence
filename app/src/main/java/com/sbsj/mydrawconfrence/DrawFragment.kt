package com.sbsj.mydrawconfrence

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sbsj.mydrawconfrence.databinding.FragmentDrawBinding

class DrawFragment : Fragment(R.layout.fragment_draw) {
    var fragmentDrawBinding : FragmentDrawBinding? = null
    lateinit var paintView : PaintView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        paintView = PaintView(context)
        fragmentDrawBinding = FragmentDrawBinding.bind(view)
        fragmentDrawBinding.DrawFrameLayout = findViewById<PaintView>(R.layout.fragment_draw)
        fragmentDrawBinding?.let {
            it.DrawChangeButton.setOnClickListener {
                paintView.changeDrawColor()
            }
        }

    }
}