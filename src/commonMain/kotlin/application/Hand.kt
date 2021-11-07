package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korau.sound.readMusic
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.*
import com.soywiz.korio.file.std.resourcesVfs
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import com.soywiz.korau.sound.readMusic

@KorgeInternal
class Hand: Puntainer {
    constructor(id: String?=null, relativeRectangle: Rectangle) : super(id,relativeRectangle){
        this.position(x,y)

        greenBlock = solidRect(100.0,100.0, Colour.GREEN.korgeColor).also {
            it.visible = blockMode
            this.addChild(it)
        }
        ActiveAnimationType.values().forEach {
            this.addChild(it.puntainerTwoFingers)
            this.addChild(it.puntainerOneFingers)
        }

        this.addChild(fingerCut)
    }

    val fingerCut= Puntainer()



    fun update(dt: TimeSpan, hitboxRectOnScreen: Rectangle){

        if(blockMode){
            greenBlock.setSize(hitboxRectOnScreen.width,hitboxRectOnScreen.height)
        }else{

            animIndex += dt.seconds*12


            var a = activeAnimation()

            if (animIndex>=activeSize()){
                if(activeAnimationType==ActiveAnimationType.TWOFINGER_JUMP){
                    activeAnimationType = ActiveAnimationType.TWOFINGER_FLY
                    a = activeAnimation()
                }else if(activeAnimationType==ActiveAnimationType.TWOFINGER_FALL){
                    activeAnimationType = ActiveAnimationType.TWOFINGER_RUN
                    a = activeAnimation()
                }else if(activeAnimationType==ActiveAnimationType.TWOFINGER_CUT){
                    println("cutfinger ended $animIndex")
                    activeAnimationType = ActiveAnimationType.TWOFINGER_RUN
                    a = activeAnimation()
                }
                else{
                    animIndex %= activeSize()
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
        if(activeAnimationType!=ActiveAnimationType.TWOFINGER_CUT){
            if(activeAnimationType.animationType()!="jump"){
                activeAnimationType = ActiveAnimationType.TWOFINGER_JUMP
            }
        }
    }

    fun onGround(){
        if(activeAnimationType!=ActiveAnimationType.TWOFINGER_CUT) {
            activeAnimationType = if (activeAnimationType.animationType() == "jump") {
                ActiveAnimationType.TWOFINGER_FALL
            } else {
                ActiveAnimationType.TWOFINGER_RUN
            }
        }
    }

    fun onDuck(){
        if(activeAnimationType!=ActiveAnimationType.TWOFINGER_CUT){
            ActiveAnimationType.TWOFINGER_DUCK
        }

        /*
        activeAnimationType = if(GlobalAccess.fingers==2){
            ActiveAnimationType.TWOFINGER_DUCK
        }else{
            ActiveAnimationType.ONEFINGER_DUCK
        }

         */

    }

    fun cutFinger(){
        if(activeAnimationType!=ActiveAnimationType.TWOFINGER_CUT){
            //activeAnimation().forEachChild { it.visible=false }

            activeAnimationType = ActiveAnimationType.TWOFINGER_CUT
            ActiveAnimationType.values().forEach {
                it.puntainerTwoFingers.children.fastForEach { it.visible=false }
                it.puntainerOneFingers.children.fastForEach { it.visible=false }
            }
            println("cutfinger calld")
        }

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
            if(value==ActiveAnimationType.TWOFINGER_CUT){
                println("cutfinger switched to active")
            }
        }
    }

    var jumpLocker=false




    fun activeAnimation(): Puntainer {
        return activeAnimationType.puntainer
    }

    fun activeSize(): Int{
        return activeAnimationType.sourceList().size
    }

    enum class ActiveAnimationType{
        TWOFINGER_RUN {
            override fun relativeRect(): Rectangle {
                return if(GlobalAccess.fingers==2){
                    Rectangle(281.0/500.0,(281.0+124.0)/500.0,13.0/500.0,213.0/500.0)
                }else{
                    Rectangle(294.0/500.0,(294.0+124.0)/500.0,16.0/500.0,216.0/500.0)
                }

            }

            override fun sourceList(): List<String> {
                return List(8) {"hands/walk-${it+1}.png"}
            }

            override fun sourceListOne(): List<String> {
                return List(8) {"hands/walk_one_finger-${it+1}.png"}
            }

            override fun animationType(): String {
                return "walk"
            }
        },
        TWOFINGER_JUMP {
            override fun relativeRect(): Rectangle {
                return if(GlobalAccess.fingers==2){
                    Rectangle(280.0/500.0,(280.0+124.0)/500.0,23.0/500.0,223.0/500.0)
                }else{
                    Rectangle(294.0/500.0,(294.0+124.0)/500.0,16.0/500.0,216.0/500.0)
                }

            }

            override fun sourceList(): List<String> {
                return List(4) {"hands/jump-${it+1}.png"}
            }

            override fun sourceListOne(): List<String> {
                return List(4) {"hands/jump_one_finger-${it+1}.png"}
            }

            override fun animationType(): String {
                return "jump"
            }
        },
        TWOFINGER_FLY {
            override fun relativeRect(): Rectangle {
                return if(GlobalAccess.fingers==2){
                    Rectangle(280.0/500.0,(280.0+124.0)/500.0,23.0/500.0,223.0/500.0)
                }else{
                    Rectangle(294.0/500.0,(294.0+124.0)/500.0,16.0/500.0,216.0/500.0)
                }

            }

            override fun sourceList(): List<String> {
                return List(1) {"hands/jump-${it+5}.png"}
            }

            override fun sourceListOne(): List<String> {
                return List(1) {"hands/jump_one_finger-${it+5}.png"}
            }

            override fun animationType(): String {
                return "jump"
            }
        },
        TWOFINGER_FALL {
            override fun relativeRect(): Rectangle {
                return if(GlobalAccess.fingers==2){
                    Rectangle(280.0/500.0,(280.0+124.0)/500.0,23.0/500.0,223.0/500.0)
                }else{
                    Rectangle(294.0/500.0,(294.0+124.0)/500.0,16.0/500.0,216.0/500.0)
                }

            }

            override fun sourceList(): List<String> {
                return List(4) {"hands/jump-${it+6}.png"}
            }

            override fun sourceListOne(): List<String> {
                return List(4) {"hands/jump_one_finger-${it+6}.png"}
            }

            override fun animationType(): String {
                return "jump"
            }
        },
        TWOFINGER_DUCK {
            override fun relativeRect(): Rectangle {
                return if(GlobalAccess.fingers==2){
                    Rectangle(287.0/500.0,(287.0+124.0)/500.0,22.0/500.0,142.0/500.0)
                }else{
                    Rectangle(287.0/500.0,(287.0+124.0)/500.0,22.0/500.0,142.0/500.0)
                }

            }

            override fun sourceList(): List<String> {
                return List(8) {"hands/duck-${it+1}.png"}
            }

            override fun sourceListOne(): List<String> {
                return List(8) {"hands/duck_one_finger-${it+1}.png"}
            }

            override fun animationType(): String {
                return "duck"
            }
        },
        TWOFINGER_CUT {
            override fun relativeRect(): Rectangle {
                return if(GlobalAccess.fingers==2){
                    Rectangle(294.0/500.0,(294.0+124.0)/500.0,16.0/500.0,216.0/500.0)
                }else{
                    Rectangle(294.0/500.0,(294.0+124.0)/500.0,16.0/500.0,216.0/500.0)
                }

            }

            override fun sourceList(): List<String> {
                return List(12) {"hands/finger_cut-${it+1}.png"}
            }

            override fun sourceListOne(): List<String> {
                return List(12) {"hands/finger_cut-${it+1}.png"}
            }

            override fun animationType(): String {
                return "cut"
            }
        };

        abstract fun relativeRect() :  Rectangle
        abstract fun sourceList(): List<String>
        abstract fun sourceListOne(): List<String>
        abstract fun animationType(): String

        var puntainerTwoFingers: Puntainer = Puntainer()
        var puntainerOneFingers: Puntainer = Puntainer()
        val puntainer: Puntainer
        get() {
            return if(GlobalAccess.fingers==2){
                puntainerTwoFingers
            }else{
                puntainerOneFingers
            }
        }



    }
}