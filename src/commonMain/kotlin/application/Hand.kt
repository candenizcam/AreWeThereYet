package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.*
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector

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

    fun update(dt: TimeSpan, hitboxRectOnScreen: Rectangle){
        if(blockMode){
            greenBlock.setSize(hitboxRectOnScreen.width,hitboxRectOnScreen.height)
        }else{
            animIndex += dt.seconds*4
            val a = activeAnimation()
            if (animIndex>=a.size){
                animIndex %= a.size
            }
            val hitboxRect = hitboxRectOnScreen.decodeRated(activeAnimationType.relativeRect())
            a.children.forEachIndexed { index, image ->
                image.visible = index==animIndex.toInt()
                image.scaledWidth= hitboxRect.width
                image.scaledHeight = hitboxRect.height
                image.x = hitboxRect.left
                image.y = GlobalAccess.virtualSize.height - (hitboxRect.top)
            }
            greenBlock.scaledWidth = hitboxRectOnScreen.width
            greenBlock.scaledHeight = hitboxRectOnScreen.height
            greenBlock.x = hitboxRectOnScreen.left
            greenBlock.y = GlobalAccess.virtualSize.height-hitboxRectOnScreen.top
            greenBlock.visible = true
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
        TWOFINGER_RUN {
            override fun relativeRect(): Rectangle {
                return Rectangle(281.0/500.0,(281.0+124.0)/500.0,13.0/500.0,213.0/500.0)
            }
        },
        TWOFINGER_JUMP {
            override fun relativeRect(): Rectangle {
                return Rectangle(281.0/500.0,(281.0+124.0)/500.0,13.0/500.0,213.0/500.0)
            }
        };

        abstract fun relativeRect() :  Rectangle
    }
}