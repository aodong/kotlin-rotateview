package com.example.aodong.rotateview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.concurrent.thread

/**
 * Created by aodong on 13/09/17.
 */
class RotateView : View {

    var tempWidth: Int = 0
    var tempHeight: Int = 0
    var radius: Float = 300f

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)


    init {
        thread {
            kotlin.concurrent.timer("timer", false, 0, 1000L, {
                post { invalidate() }
            })
        }
    }


    override fun onDraw(canvas: Canvas?) {
        var paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeWidth = 5f

        canvas?.drawCircle(tempWidth / 2.0f, tempHeight / 2.0f, radius, paint)

        val paintDegree = Paint()
        paintDegree.strokeWidth = 3f
        for (i in 0..59) {
            if (i % 5 == 0) {
                paintDegree.strokeWidth = 5f
                paintDegree.textSize = 30f
                paintDegree.color = Color.parseColor("#000000")
                canvas?.drawLine((tempWidth / 2).toFloat(), tempHeight / 2 - radius, (tempWidth / 2).toFloat(), 40f + (tempHeight / 2 - radius), paintDegree)
                var degree = "12"
                if (i != 0)
                    degree = (i / 5).toString()
                canvas?.drawText(degree, tempWidth / 2 - paintDegree.measureText(degree) / 2, tempHeight / 2 - radius + 90f, paintDegree)
            } else {
                paintDegree.strokeWidth = 3f//秒
                paintDegree.textSize = 15f
                paintDegree.color = Color.parseColor("#33000000")
                canvas?.drawLine((tempWidth / 2).toFloat(), (tempHeight / 2 - radius), (tempWidth / 2).toFloat(), 30f + (tempHeight / 2 - radius), paintDegree)
            }
            canvas?.rotate(6f, (tempWidth / 2).toFloat(), (tempHeight / 2).toFloat())
        }

        //画秒针
        var calendar = Calendar.getInstance()
        var second: Int = calendar.get(Calendar.SECOND)
        var secondArray = tempXAndTempY(second)
        var tempSecondX = secondArray[0]
        var tempSecondY = secondArray[1]
        var paintS = Paint()
        paintS.strokeWidth = 5f
        paintS.color = Color.parseColor("#000000")
        canvas?.drawLine((tempWidth / 2).toFloat(), (tempHeight / 2).toFloat(), (tempWidth / 2).toFloat() + (tempSecondX * radius).toFloat(), (tempHeight / 2).toFloat() + (tempSecondY * radius).toFloat(), paintS)

        //画分针
        var minute = calendar.get(Calendar.MINUTE)
        var minuteArray = tempXAndTempY(minute, second)
        var tempMinuteX = minuteArray[0]
        var tempMinuteY = minuteArray[1]
        var paintM = Paint()
        paintM.strokeWidth = 10f
        paintM.color = Color.parseColor("#000000")
        canvas?.drawLine((tempWidth / 2).toFloat(), (tempHeight / 2).toFloat(), (tempWidth / 2).toFloat() + (tempMinuteX * (radius - 50)).toFloat(), (tempHeight / 2).toFloat() + (tempMinuteY * (radius - 50)).toFloat(), paintM)
        //画时针
        var paintH = Paint()
        paintH.strokeWidth = 15f
        paintH.color = Color.parseColor("#000000")
        var hour = calendar.get(Calendar.HOUR)
        var hourArray = tempXAndTempY(hour * 5, minute, second)
        var tempHourX = hourArray[0]
        var tempHourY = hourArray[1]
        canvas?.drawLine((tempWidth / 2).toFloat(), (tempHeight / 2).toFloat(), (tempWidth / 2).toFloat() + (tempHourX * (radius - 100)).toFloat(), (tempHeight / 2).toFloat() + (tempHourY * (radius - 100)).toFloat(), paintH)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        tempWidth = widthSize
        tempHeight = heightSize
    }


    private fun tempXAndTempY(x: Int, y: Int = 0, z: Int = 0): Array<Double> {
        var tempX = 0.0
        var tempY = 0.0

        var angle = x * 6.0 + y / 60.0 * 6 + z / 60 * 60 * 6

        when (x) {
            in 0..15 -> {
                tempX = Math.sin(angle * Math.PI / 180)
                tempY = -Math.cos(angle * Math.PI / 180)
            }
            in 16..30 -> {
                tempX = Math.sin(Math.PI - angle * Math.PI / 180)
                tempY = Math.cos(Math.PI - angle * Math.PI / 180)
            }
            in 31..45 -> {
                tempX = -Math.sin(angle * Math.PI / 180 - Math.PI)
                tempY = Math.cos(angle * Math.PI / 180 - Math.PI)
            }
            in 45..59 -> {
                tempX = -Math.sin(2 * Math.PI - angle * Math.PI / 180)
                tempY = -Math.cos(2 * Math.PI - angle * Math.PI / 180)
            }
        }
        return arrayOf(tempX, tempY)
    }
}