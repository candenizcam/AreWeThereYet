package application

import com.soywiz.kds.getExtra
import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.position
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import pungine.Puntainer
import pungine.geometry2D.Rectangle

class Hand: Puntainer {
    constructor(id: String?=null, relativeRectangle: Rectangle) : super(id,relativeRectangle){
        this.position(x,y)








    }

    fun update(dt: TimeSpan){
        animIndex += dt.seconds*4
        val a = activeAnimation()
        if (animIndex>=a.size){
            animIndex %= a.size
        }
        a.forEachIndexed { index, image ->
            image.visible = index==animIndex.toInt()


        }
    }





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