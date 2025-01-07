package com.example.snake.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.snake.viewmodel.Position

class GameView(context: Context, attr: AttributeSet) : View(context, attr) {

    var body: List<Position>? = null
    val gap = 3
    var size = 0
    var apple: Position? = null
    val paint = Paint().apply { color = Color.BLACK }
    val paintApple = Paint().apply { color = Color.RED }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.run {
            apple?.run {
                drawRect((x * size).toFloat() + gap, (y * size).toFloat() + gap
                    , ((x + 1 )* size).toFloat() - gap, ((y + 1 )* size).toFloat() - gap, paintApple)

            }
            body?.forEach { p
                ->
                drawRect((p.x * size).toFloat() + gap, (p.y * size).toFloat() + gap
                    , ((p.x + 1 )* size).toFloat() - gap, ((p.y + 1 )* size).toFloat() - gap, paint)
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        size = width / 20
    }
}