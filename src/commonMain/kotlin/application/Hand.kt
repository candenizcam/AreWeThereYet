package application

import com.soywiz.kds.getExtra
import com.soywiz.klock.TimeSpan
import com.soywiz.klogger.AnsiEscape
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.View
import com.soywiz.korge.view.position
import com.soywiz.korge.view.solidRect
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.oneRectangle

class Hand: Puntainer {
    constructor(id: String?=null, relativeRectangle: Rectangle) : super(id,relativeRectangle){
        this.position(x,y)

        greenBlock = solidRect(100.0,100.0, Colour.GREEN.korgeColor).also {
            it.visible = true
            this.addChild(it)
        }










    }

    fun update(dt: TimeSpan){
        if(blockMode){
            val korgeWidth = width
            val korgeHeight = height
            greenBlock.setSize(width,height)

        }else{
            animIndex += dt.seconds*4
            val a = activeAnimation()
            if (animIndex>=a.size){
                animIndex %= a.size
            }
            a.forEachIndexed { index, image ->
                image.visible = index==animIndex.toInt()
            }
        }

    }

    var blockMode = true
    val greenBlock: View





    var twoFingerRun = listOf<Image>()
        set(value) {
            field = value
            value.forEach {
                this.addChild(it)
                it.visible = false
            }
        }

    var twoFingerJump= listOf<Image>()
        set(value) {
            field = value
            value.forEach {
                this.addChild(it)
                it.visible = false
            }
        }

    var animIndex = 0.0
    var activeAnimationType = ActiveAnimationType.TWOFINGER_RUN
    set(value) {
        activeAnimation().forEach { it.visible=false }
        field=value
    }




    fun activeAnimation(): List<Image> {
        return when(activeAnimationType){
            ActiveAnimationType.TWOFINGER_RUN->{
                twoFingerRun
            }
            ActiveAnimationType.TWOFINGER_JUMP->{
                twoFingerJump
            }
        }
    }

    enum class ActiveAnimationType{
        TWOFINGER_RUN,
        TWOFINGER_JUMP
    }
}