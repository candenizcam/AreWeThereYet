package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.*
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle

@KorgeInternal
class Hand(id: String? = null, relativeRectangle: Rectangle) : Puntainer(id, relativeRectangle) {


    fun update(dt: TimeSpan, hitboxRectOnScreen: Rectangle){
        if(blockMode){
            greenBlock.setSize(hitboxRectOnScreen.width,hitboxRectOnScreen.height)
        }else{

            animIndex += dt.seconds*12


            var a = activeAnimation()
            if (animIndex>=a.size){
                if(activeAnimationType==ActiveAnimationType.TWOFINGER_JUMP){
                    activeAnimationType = ActiveAnimationType.TWOFINGER_FLY
                    a = activeAnimation()
                }else if(activeAnimationType==ActiveAnimationType.TWOFINGER_FALL){
                    activeAnimationType = ActiveAnimationType.TWOFINGER_RUN
                    a = activeAnimation()
                }
                else{
                    animIndex %= a.size
                }

            }
            val hitboxRect = hitboxRectOnScreen.decodeRated(activeAnimationType.relativeRect())
            a.children.forEachIndexed { index, image ->
                image.visible = index==animIndex.toInt()
                image.scaledWidth= hitboxRect.width
                image.scaledHeight = hitboxRect.height
                image.x = hitboxRect.left
                image.y = GlobalAccess.virtualSize.height - (hitboxRect.top)
            }
            if(activeAnimationType==ActiveAnimationType.TWOFINGER_DUCK){
                val vss = a.children.map { it.visible }
                println(vss)
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

    fun onAir(){
        if(activeAnimationType.animationType()!="jump"){
            activeAnimationType = ActiveAnimationType.TWOFINGER_JUMP
        }
    }

    fun onGround(){
        if(activeAnimationType.animationType()=="jump"){
            activeAnimationType = ActiveAnimationType.TWOFINGER_FALL
        }else{
            activeAnimationType = ActiveAnimationType.TWOFINGER_RUN
        }
    }

    fun onDuck(){
        activeAnimationType = ActiveAnimationType.TWOFINGER_DUCK
    }



    var animIndex = 0.0
    var activeAnimationType = ActiveAnimationType.TWOFINGER_RUN
    set(value) {
        if(value!=field){
            println(value.toString())
            activeAnimation().children.forEach { it.visible=false }
            animIndex=0.0
            field=value
            jumpLocker=false
        }

    }

    var jumpLocker=false




    fun activeAnimation(): Puntainer {
        return activeAnimationType.puntainer
    }

    enum class ActiveAnimationType{
        TWOFINGER_RUN {
            override fun relativeRect(): Rectangle {
                return Rectangle(281.0/500.0,(281.0+124.0)/500.0,13.0/500.0,213.0/500.0)
            }

            override fun sourceList(): List<String> {
                return List(8) {"hands/walk-${it+1}.png"}
            }

            override fun animationType(): String {
                return "walk"
            }
        },
        TWOFINGER_JUMP {
            override fun relativeRect(): Rectangle {
                return Rectangle(280.0/500.0,(280.0+124.0)/500.0,23.0/500.0,223.0/500.0)
            }

            override fun sourceList(): List<String> {
                return List(4) {"hands/jump-${it+1}.png"}
            }

            override fun animationType(): String {
                return "jump"
            }
        },
        TWOFINGER_FLY {
            override fun relativeRect(): Rectangle {
                return Rectangle(280.0/500.0,(280.0+124.0)/500.0,23.0/500.0,223.0/500.0)
            }

            override fun sourceList(): List<String> {
                return List(1) {"hands/jump-${it+5}.png"}
            }

            override fun animationType(): String {
                return "jump"
            }
        },
        TWOFINGER_FALL {
            override fun relativeRect(): Rectangle {
                return Rectangle(280.0/500.0,(280.0+124.0)/500.0,23.0/500.0,223.0/500.0)
            }

            override fun sourceList(): List<String> {
                return List(4) {"hands/jump-${it+6}.png"}
            }

            override fun animationType(): String {
                return "jump"
            }
        },
        TWOFINGER_DUCK {
            override fun relativeRect(): Rectangle {
                return Rectangle(287.0/500.0,(287.0+124.0)/500.0,22.0/500.0,142.0/500.0)
            }

            override fun sourceList(): List<String> {
                return List(8) {"hands/duck-${it+1}.png"}
            }

            override fun animationType(): String {
                return "duck"
            }
        };

        abstract fun relativeRect() :  Rectangle
        abstract fun sourceList(): List<String>
        abstract fun animationType(): String

        var puntainer: Puntainer = Puntainer()

    }

    init {
        this.position(x,y)
        greenBlock = solidRect(100.0,100.0, Colour.GREEN.korgeColor).also {
            it.visible = blockMode
            this.addChild(it)
        }
        ActiveAnimationType.values().forEach {
            this.addChild(it.puntainer)
        }
    }
}