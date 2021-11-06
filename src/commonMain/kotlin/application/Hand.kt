package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.View
import com.soywiz.korge.view.position
import com.soywiz.korge.view.solidRect
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle

class Hand: Puntainer {
    constructor(id: String?=null, relativeRectangle: Rectangle) : super(id,relativeRectangle){
        this.position(x,y)

        greenBlock = solidRect(100.0,100.0, Colour.GREEN.korgeColor).also {
            it.visible = blockMode
            this.addChild(it)
        }

        this.addChild(twoFingerRun)
        this.addChild(twoFingerJump)










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
            a.children.forEachIndexed { index, image ->
                image.visible = index==animIndex.toInt()
            }
            a.scaledWidth = width
            a.scaledHeight = height
            a.x = x
            a.y = y
        }

    }

    var blockMode = false
    val greenBlock: View





    var twoFingerRunList = listOf<Image>()
        set(value) {
            field = value
            twoFingerRun.children.clear()
            value.forEach {
                twoFingerRun.addChild(it)

                //this.addChild(it)
                it.visible = false
            }
        }

    val twoFingerRun = Puntainer()
    val twoFingerJump = Puntainer()

    var twoFingerJumpList= listOf<Image>()
        set(value) {
            field = value
            value.forEach {
                twoFingerJump.addChild(it)
                it.visible = false
            }
        }

    var animIndex = 0.0
    var activeAnimationType = ActiveAnimationType.TWOFINGER_RUN
    set(value) {
        activeAnimation().children.forEach { it.visible=false }
        field=value
    }




    fun activeAnimation(): Puntainer {
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