package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.View
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.oneRectangle


@DelicateCoroutinesApi
@KorgeInternal
class Hand(id: String? = null, relativeRectangle: Rectangle) : Puntainer(id, relativeRectangle) {
    var fingerCut: Puntainer
    private val greenBlock: View
    init {
        //this.position(x,y)
        greenBlock = solidRect("sr", oneRectangle(), Colour.GREEN).also {
            it.visible = blockMode
            this.addChild(it)
        }
        fingerCut = Puntainer()
        this.addChild(fingerCut)
    }




    suspend fun suspendInitAlternative(fingerNo: String){

        ActiveAnimationType.values().forEach { it ->
            val punt= Puntainer("${fingerNo}_${it.animationID()}")
            this.addChild(punt)
            if(fingerNo=="two"){
                it.sourceList()
            }else{
                it.sourceListOne()
            }.forEach {s->
                Image(resourcesVfs[s].readBitmap()).also { it2->
                    it2.visible = false
                    punt.addChild(it2)
                }
            }


        }
    }




    private val animationSpeed = 15 //12 frames per second

    fun update(dt: TimeSpan, hitboxRectOnScreen: Rectangle){

        if(blockMode){
            greenBlock.setSize(hitboxRectOnScreen.width,hitboxRectOnScreen.height)
        }else{

            animIndex += dt.seconds*animationSpeed



            if(isDying.not()){
                var a = activeAnimation()

                if (animIndex>=activeSize()){
                    if(activeAnimationType==ActiveAnimationType.JUMP){
                        activeAnimationType = ActiveAnimationType.FLY
                        a = activeAnimation()
                    }else if(activeAnimationType==ActiveAnimationType.FALL){
                        activeAnimationType = ActiveAnimationType.RUN
                        a = activeAnimation()
                    }else if(activeAnimationType==ActiveAnimationType.CUT){
                        if(GlobalAccess.fingers==0){
                            isDying = true
                            animIndex=0.0
                            return
                        }else{
                            activeAnimationType = ActiveAnimationType.RUN
                        }

                        a = activeAnimation()
                    }
                    else{
                        animIndex %= activeSize()
                    }

                }
                val hitboxRect = hitboxRectOnScreen.decodeRated(activeAnimationType.relativeRect())

                a.children.forEachIndexed { index, image ->
                    //image.visible = index==animIndex.toInt()


                    //image.visible = true
                    image.scaledWidth= hitboxRect.width
                    image.scaledHeight = hitboxRect.height
                    image.x = hitboxRect.left
                    image.y = GlobalAccess.virtualSize.height - (hitboxRect.top)
                }
                if(activeAnimationType==ActiveAnimationType.DUCK){
                    val vss = a.children.map { it.visible }
                }
                //greenBlock.scaledWidth = hitboxRectOnScreen.width
                //greenBlock.scaledHeight = hitboxRectOnScreen.height
                //greenBlock.x = hitboxRectOnScreen.left
                //greenBlock.y = GlobalAccess.virtualSize.height-hitboxRectOnScreen.top
                //greenBlock.visible = true
            }else{
                if(animIndex>=animationSpeed*0.5){
                    isDead=true
                }
            }

        }

    }

    var blockMode = false

    var isDead = false
    var isDying = false

    fun onAir(){
        if(activeAnimationType!=ActiveAnimationType.CUT){
            if(activeAnimationType.animationType()!="jump"){
                activeAnimationType = ActiveAnimationType.JUMP
            }
        }
    }

    fun onGround(){
        if(activeAnimationType!=ActiveAnimationType.CUT) {
            activeAnimationType = if (activeAnimationType.animationType() == "jump") {
                ActiveAnimationType.FALL
            } else {
                ActiveAnimationType.RUN
            }
        }
    }

    fun onDuck(){
        if(activeAnimationType!=ActiveAnimationType.CUT){
            activeAnimationType=ActiveAnimationType.DUCK

        }
    }


    suspend fun cutFinger(){
        if(activeAnimationType!=ActiveAnimationType.CUT) {
            //activeAnimation().forEachChild { it.visible=false }
            SfxPlayer.playSfx("cut.mp3")
            activeAnimationType = ActiveAnimationType.CUT

            /*
            ActiveAnimationType.values().forEach {


                /*
                listOf("one_","two_").forEach { pref->
                    children.filterIsInstance<Puntainer>().filter { it.id==pref+activeAnimationType.animationID()  }.forEach {
                        it.visible=false
                    }
                }

                 */

                //it.puntainerTwoFingers.children.fastForEach { it.visible = false }
                //it.puntainerOneFingers.children.fastForEach { it.visible = false }
            }

             */
            when ((0..2).random()) {
                0 -> SfxPlayer.playSfx("daddyScared.mp3")
                1 -> SfxPlayer.playSfx("mommyScared.mp3")
                2 -> SfxPlayer.playSfx("mommy&daddy.mp3")
            }
        }

    }



    var animIndex = 0.0
    var activeAnimationType = ActiveAnimationType.RUN
        set(value) {
            if(value!=field){
                hideEverything()
                animIndex=0.0
                field=value
                jumpLocker=false
            }
        }

    var jumpLocker=false

    private fun hideEverything(){
        children.filterIsInstance<Puntainer>().forEach {  puntainer->
            puntainer.children.fastForEach { view->
                view.visible=false
            }
        }
    }



    fun activeAnimation(): Puntainer {
        val pref = if(GlobalAccess.fingers!=0){
            if(activeAnimationType!=ActiveAnimationType.CUT){
                listOf("one_","two_")[GlobalAccess.fingers-1]
            }else{
                "two_"
            }
        }else{
            "one_"
        }
        return children.filterIsInstance<Puntainer>().filter { it.id==pref+activeAnimationType.animationID() }[0]
    }

    fun activeSize(): Int{
        return activeAnimationType.sourceList().size
    }

    enum class ActiveAnimationType{
        RUN {
            override fun relativeRect(): Rectangle {
                return if(GlobalAccess.fingers==2){
                    Rectangle(281.0/500.0,(281.0+124.0)/500.0,13.0/500.0,213.0/500.0)
                }else{
                    Rectangle(294.0/500.0,(294.0+124.0)/500.0,16.0/500.0,216.0/500.0)
                }
            }
            override fun sourceList(): List<String> { return List(8) {"hands/walk-${it+1}.png"} }
            override fun sourceListOne(): List<String> { return List(8) {"hands/walk_one_finger-${it+1}.png" } }
            override fun animationType(): String { return "walk" }
            override fun animationID(): String { return "run" }
        },
        JUMP {
            override fun relativeRect(): Rectangle {
                return if(GlobalAccess.fingers==2){
                    Rectangle(280.0/500.0,(280.0+124.0)/500.0,23.0/500.0,223.0/500.0)
                }else{
                    Rectangle(294.0/500.0,(294.0+124.0)/500.0,16.0/500.0,216.0/500.0)
                }
            }
            override fun sourceList(): List<String> { return List(4) {"hands/jump-${it+1}.png"} }
            override fun sourceListOne(): List<String> { return List(4) {"hands/jump_one_finger-${it+1}.png"} }
            override fun animationType(): String { return "jump" }
            override fun animationID(): String { return "jump" }
        },
        FLY {
            override fun relativeRect(): Rectangle {
                return if(GlobalAccess.fingers==2){
                    Rectangle(280.0/500.0,(280.0+124.0)/500.0,23.0/500.0,223.0/500.0)
                }else{
                    Rectangle(294.0/500.0,(294.0+124.0)/500.0,16.0/500.0,216.0/500.0)
                }
            }
            override fun sourceList(): List<String> { return List(1) {"hands/jump-${it+5}.png"} }
            override fun sourceListOne(): List<String> { return List(1) {"hands/jump_one_finger-${it+5}.png"} }
            override fun animationType(): String { return "jump" }
            override fun animationID(): String { return "fly" }
        },
        FALL {
            override fun relativeRect(): Rectangle {
                return if(GlobalAccess.fingers==2){
                    Rectangle(280.0/500.0,(280.0+124.0)/500.0,23.0/500.0,223.0/500.0)
                }else{
                    Rectangle(294.0/500.0,(294.0+124.0)/500.0,16.0/500.0,216.0/500.0)
                }
            }
            override fun sourceList(): List<String> { return List(4) {"hands/jump-${it+6}.png"} }
            override fun sourceListOne(): List<String> { return List(4) {"hands/jump_one_finger-${it+6}.png"} }
            override fun animationType(): String { return "jump" }
            override fun animationID(): String { return "fall" }
        },
        DUCK {
            override fun relativeRect(): Rectangle {
                return if(GlobalAccess.fingers==2){
                    Rectangle(287.0/500.0,(287.0+124.0)/500.0,22.0/500.0,142.0/500.0)
                }else{
                    Rectangle(287.0/500.0,(287.0+124.0)/500.0,22.0/500.0,142.0/500.0)
                }
            }
            override fun sourceList(): List<String> { return List(3) {"hands/duck-${it+4}.png"} }
            override fun sourceListOne(): List<String> { return List(3) {"hands/duck_one_finger-${it+4}.png"} }
            override fun animationType(): String { return "duck" }
            override fun animationID(): String { return "duck" }
        },
        CUT {
            override fun relativeRect(): Rectangle {
                return if(GlobalAccess.fingers==2){
                    Rectangle(294.0/500.0,(294.0+124.0)/500.0,16.0/500.0,216.0/500.0)
                }else{
                    Rectangle(294.0/500.0,(294.0+124.0)/500.0,16.0/500.0,216.0/500.0)
                }
            }
            override fun sourceList(): List<String> { return List(12) {"hands/finger_cut-${it+1}.png"} }
            override fun sourceListOne(): List<String> { return List(13) {"hands/last_finger-${it+1}.png"} }
            override fun animationType(): String { return "cut" }
            override fun animationID(): String { return "cut" }
        };

        abstract fun relativeRect() :  Rectangle
        abstract fun sourceList(): List<String>
        abstract fun sourceListOne(): List<String>
        abstract fun animationType(): String
        abstract fun animationID(): String
    }


}