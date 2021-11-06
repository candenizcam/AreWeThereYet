package application

import com.soywiz.klock.TimeSpan
import com.soywiz.kmem.toInt
import com.soywiz.korau.format.AudioDecodingProps
import com.soywiz.korau.sound.*
import com.soywiz.korev.Key
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.*
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import modules.basic.Colour
import pungine.PunImage
import pungine.PunScene
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import kotlin.coroutines.CoroutineContext

/** This scene is the template for a PunGineIV game
 *
 */

class GameScene: PunScene() {
    override fun createSceneView(): Container = Puntainer()

    @OptIn(KorgeInternal::class)
    override suspend fun Container.sceneInit(){
        val h = GlobalAccess.virtualSize.height.toDouble()
        val w = GlobalAccess.virtualSize.width.toDouble()

        var l1 = resourcesVfs["layer1.mp3"].readMusic().decode().toStream()
        var l2 = resourcesVfs["layer2.mp3"].readMusic().decode().toStream()
        var tw = resourcesVfs["twister.mp3"].readMusic()

        val testAudio = nativeSoundProvider.createSound(resourcesVfs["layer1.mp3"], streaming = true, props = AudioDecodingProps.DEFAULT)

        testAudio.decode()

        launch {
            l1.playAndWait()
        }

        floor = puntainer("floor", Rectangle(0.0,1.0,0.0,FloorData.getHeight()),relative = true) {

            it.singleColour(Colour.GREEN.korgeColor).also {
                it.alpha = 0.1
            }
        }

        //puntainer() {  }

        outside1=punImage("outside",resourcesVfs["environment/bg-loop.png"].readBitmap(), Rectangle(0.0,7640.0,0.0,1080.0))
        outside2=punImage("outside",resourcesVfs["environment/bg-loop.png"].readBitmap(), Rectangle(7640.0,2*7640.0,0.0,1080.0))

        punImage("window",resourcesVfs["environment/window.png"].readBitmap(), oneRectangle(),true)

        playfield.fitToFrame(Rectangle(0.0,w,FloorData.getHeight()*h,h))
        this.addChild(playfield)

        adjustHand()
        this.addChild(hand)


        // obstacles
        for(i in 0..4){
            PunImage("normal",resourcesVfs["obstacle.png"].readBitmap()).also {
                obstacles.add(it)
                this.addChild(it)
                it.visible=false
            }

        }




        /*
        obstacles.add(puntainer("obs", Rectangle(w,w+50.0,FloorData.getHeight()*GlobalAccess.virtualSize.height,FloorData.getHeight()*GlobalAccess.virtualSize.height+50.0)) {
            it.singleColour(Colour.RED.korgeColor)


        })

         */

        var s = 0.0


        this.addUpdater {dt->



            /*
            obstacles.forEach {
                it.x = it.x - dt.milliseconds*0.2
            }

             */

            obstacles.forEach {
                it.visible = false
            }



            outside1.x -= dt.milliseconds*20
            outside2.x -= dt.milliseconds*20
            if(outside1.x + outside1.width< -1000.0){
                outside1.x += outside1.width*2
            }
            if(outside2.x + outside2.width< -1000.0){
                outside2.x += outside2.width*2
            }

            playfield.level.obstacles.forEachIndexed { index,obs->
                val r = playfield.virtualRectangle.fromRated(Rectangle(Vector(obs.centerX,obs.centerY),obs.width,obs.height))

                obstacles[index].x = r.left
                obstacles[index].yConv = r.top
                obstacles[index].scaledHeight = r.height
                obstacles[index].scaledWidth = r.width
                obstacles[index].visible=true
            }




            if(views.input.keys.justPressed(Key.UP)){
                playfield.jump()
            }

            if(views.input.keys.justPressed(Key.DOWN)){
                playfield.duck()
            }

            if(views.input.keys.pressing(Key.DOWN).not()){
                playfield.stopDuck()
            }

            val r = playfield.virtualRectangle.fromRated(playfield.hitboxRect)

            hand.update(dt, r)
            playfield.update(dt)

            if(playfield.collisionCheck()){
                floor.visible = false
            }else{
                floor.visible = true
            }

            if(playfield.collisionCheck()){
                l2.currentTime = l1.currentTime
                }
        }
        super.sceneAfterInit()
    }

    val hand = Hand("hand",oneRectangle())
    var playfield = Playfield("playfield", oneRectangle())
    var floor = Puntainer()

    var obstacles = mutableListOf<Puntainer>()

    val gravity = 200.0
    var hitboxDy =0.0
    var outside1: Puntainer = Puntainer()
    var outside2: Puntainer = Puntainer()

    suspend fun adjustHand(){
        hand.twoFingerRunList= List(8){
            val i = listOf("pungo_transparent.png","pungo_transparent_2.png","pungo_transparent_3.png","pungo_transparent_4.png")

            Image(resourcesVfs["hands/walk-${it+1}.png"].readBitmap())
        }

        hand.twoFingerJumpList =  List(2){
            val i = listOf("pungo_transparent_2.png","pungo_transparent_4.png")
            Image(resourcesVfs[i[it]].readBitmap())
        }

    }




    object FloorData{
        val ratedY = 240.0/1080.0

        fun getHeight(x: Double?=null): Double {
            return if(x!=null){
                ratedY
            }else{
                ratedY
            }
        }
    }


}