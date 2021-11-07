package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korau.sound.PlaybackParameters
import com.soywiz.korau.sound.PlaybackTimes
import com.soywiz.korau.sound.readMusic
import com.soywiz.korev.Key
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.GlobalScope
import modules.basic.Colour
import pungine.PunImage
import pungine.PunScene
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle

/** This scene is the template for a PunGineIV game
 *
 */

@KorgeInternal
class GameScene : PunScene() {
    override fun createSceneView(): Container = Puntainer()

    var scoreKeeper = ScoreKeeper()

    override suspend fun Container.sceneInit() {
        GlobalAccess.fingers=2


        val h = GlobalAccess.virtualSize.height.toDouble()
        val w = GlobalAccess.virtualSize.width.toDouble()

        /////////
        // WINDOW SCENE
        /////////

        /*
        val engineLoop = resourcesVfs["SFX/engine_heavy_loop-20.mp3"].readMusic()

        outside1 =
            punImage("outside", resourcesVfs["environment/Bg2.png"].readBitmap(), Rectangle(0.0, 3820.0, 0.0, 1080.0))
        outside2 = punImage(
            "outside",
            resourcesVfs["environment/Bg2.png"].readBitmap().flipX(),
            Rectangle(3820.0, 2 * 3820.0, 0.0, 1080.0)
        )

        //solidRect("blur", Rectangle(0.0,1.0,0.0,1.0),colour = Colour.rgba256(100,100,100,100).korgeColor,relative = true)
        window = punImage(
            "id",
            resourcesVfs["UI/glass.png"].readBitmap(),
            Rectangle(0.0,1.0,0.0,1.0),relative = true
        ).also {
            it.alpha=1.0
        }

        punImage(
            "id",
            resourcesVfs["UI/Wintop.png"].readBitmap(),
            Rectangle(0.0,1.0,0.0,1.0),relative = true
        )

        punImage(
            "id",
            resourcesVfs["UI/Windown.png"].readBitmap(),
            Rectangle(0.0,1.0,0.0,1.0),relative = true
        )

         */





        /////////
        //GAME SCENE
        /////////



        val playMusic = false
        var fadein = false

        val l1 = resourcesVfs["musicbox.mp3"].readMusic()
        val l2 = resourcesVfs["altlayer.mp3"].readMusic()
        val l3 = resourcesVfs["ominous.mp3"].readMusic()

        if (playMusic) {
            l1.play(PlaybackParameters(PlaybackTimes.INFINITE, volume = 0.3))
            l2.play(PlaybackParameters(PlaybackTimes.INFINITE, volume = 0.3))
            l3.play(PlaybackParameters(PlaybackTimes.INFINITE, volume = 0.3))
        }

        floor = puntainer("floor", Rectangle(0.0, 1.0, 0.0, FloorData.getHeight()), relative = true) { puntainer ->

            puntainer.singleColour(Colour.GREEN.korgeColor).also {
                it.alpha = 0.1
            }
        }

        outside1 =
            punImage("outside", resourcesVfs["environment/Bg2.png"].readBitmap(), Rectangle(0.0, 3820.0, 0.0, 1080.0))
        outside2 = punImage(
            "outside",
            resourcesVfs["environment/Bg2.png"].readBitmap().flipX(),
            Rectangle(3820.0, 2 * 3820.0, 0.0, 1080.0)
        )

        punImage("window", resourcesVfs["environment/window.png"].readBitmap(), oneRectangle(), true)

        playfield.fitToFrame(Rectangle(0.0, w, FloorData.getHeight() * h, h))
        this.addChild(playfield)

        val rareScavengerList = listOf("Red Bird","Red Traffic Sign","Yellow Tractor","Green Road Sign","Large Blue Sign","Small Blue Sign")
        val rarerScavengerList = listOf("Green Bird","Green Traffic Sign","Red Tractor","Yellow Road Sign","Large Green Sign","Small Green Sign")
        val rarestScavengerList = listOf("Blue Bird","Yellow Traffic Sign","Blue Tractor","Blue Road Sign","Large Yellow Sign","Small Yellow Sign")

        var hunt1 = rareScavengerList.random()
        var hunt2 = rarerScavengerList.random()
        var hunt3 = rarestScavengerList.random()
        var scavengerHuntList = listOf(hunt1,hunt2,hunt3)
        var sh1Type: ObstacleTypes
        var sh2Type: ObstacleTypes
        var sh3Type: ObstacleTypes
        when(rareScavengerList.indexOf(hunt1)){
            0 ->  sh1Type = ObstacleTypes.LOWBIRD
            1 ->  sh1Type = ObstacleTypes.LOWJUMP
            2 ->  sh1Type = ObstacleTypes.LONGJUMP
            3 ->  sh1Type = ObstacleTypes.DONTJUMP
            4 ->  sh1Type = ObstacleTypes.DUCK
            5 ->  sh1Type = ObstacleTypes.JUMPDUCK
        }
        when(rareScavengerList.indexOf(hunt2)){
            0 ->  sh2Type = ObstacleTypes.LOWBIRD
            1 ->  sh2Type = ObstacleTypes.LOWJUMP
            2 ->  sh2Type = ObstacleTypes.LONGJUMP
            3 ->  sh2Type = ObstacleTypes.DONTJUMP
            4 ->  sh2Type = ObstacleTypes.DUCK
            5 ->  sh2Type = ObstacleTypes.JUMPDUCK
        }
        when(rareScavengerList.indexOf(hunt3)){
            0 ->  sh3Type = ObstacleTypes.LOWBIRD
            1 ->  sh3Type = ObstacleTypes.LOWJUMP
            2 ->  sh3Type = ObstacleTypes.LONGJUMP
            3 ->  sh3Type = ObstacleTypes.DONTJUMP
            4 ->  sh3Type = ObstacleTypes.DUCK
            5 ->  sh3Type = ObstacleTypes.JUMPDUCK
        }
        //TODO rare-sh1Type, rarer-sh2Type ve rarest-sh3Typelar puan kazandıracak

        // obstacles
        val rarityList = listOf("rare","rarer","rarest")
        for (j in 0..0) {
            for (i in 1..3) {
                listOf("","-gore").forEach { goreText->
                    val folder = rarityList[i-1]+goreText
                    PunImage("dont-jump-$i$goreText", resourcesVfs["obs/$folder/dont-jump-$i.png"].readBitmap()).also {
                        obstacles.add(it)
                        this.addChild(it)
                        it.visible = false
                    }

                    PunImage("duck-$i$goreText", resourcesVfs["obs/$folder/duck-$i.png"].readBitmap()).also {
                        obstacles.add(it)
                        this.addChild(it)
                        it.visible = false
                    }

                    PunImage("high-jump-$i$goreText", resourcesVfs["obs/$folder/high-jump-$i.png"].readBitmap()).also {
                        obstacles.add(it)
                        this.addChild(it)
                        it.visible = false
                    }

                    PunImage("jump-duck-$i$goreText", resourcesVfs["obs/$folder/jump-duck-$i.png"].readBitmap()).also {
                        obstacles.add(it)
                        this.addChild(it)
                        it.visible = false
                    }

                    PunImage("long-jump-$i$goreText", resourcesVfs["obs/$folder/long-jump-$i.png"].readBitmap()).also {
                        obstacles.add(it)
                        this.addChild(it)
                        it.visible = false
                    }

                    PunImage("low-jump-$i$goreText", resourcesVfs["obs/$folder/low-jump-$i.png"].readBitmap()).also {
                        obstacles.add(it)
                        this.addChild(it)
                        it.visible = false
                    }

                    PunImage("bird-$i$goreText", resourcesVfs["obs/$folder/bird-$i.png"].readBitmap()).also {
                        obstacles.add(it)
                        this.addChild(it)
                        it.visible = false
                    }
                }

            }
        }

        adjustHand()
        this.addChild(hand)



        for (i in (0..10)) {
            //PunImage("obshit",resourcesVfs["obstacle.png"].readBitmap()).also {
            //    this.addChild(it)
            //    it.visible=false
            //}
            solidRect("obshit", Rectangle(0.0, 1.0, 0.0, 1.0), relative = true, colour = Colour.RED.korgeColor).also {
                it.visible = false
                it.alpha = 0.2
            }
        }

        floor = puntainer("floor", Rectangle(0.0,1.0,0.0,1.0),relative = true) {
            it.singleColour(Colour.RED.korgeColor).also {
                it.alpha = 0.1
            }
        }

        puntainer("floor", Rectangle(0.0,1.0,0.0,1.0),relative = true){}.also{

        }





        ////////////////////////////////////////////////////////////////

        // UPDATER

        ////////////////////////////////////////////////////////////////




        this.addUpdater { dt ->




            if(gameActive){

                backgroundRoll(dt)
                obstacles.forEach {
                    it.visible = false
                }




                val obshit = children.filterIsInstance<Puntainer>().filter { it.id == "obshit" }
                var obshitindex = 0
                obshit.forEach {
                    it.visible = false
                }



                val goreText = if(GlobalAccess.fingers==2){
                    ""
                }else{
                    "-gore"
                }
                ObstacleTypes.values().forEach { thisType ->
                    playfield.level.obstacles.filter { it.type == thisType }.also { list ->
                        ObstacleRarity.values().forEach { rarity ->
                            val obstacleData = list.filter { it.obstacleRarity ==rarity }
                            val o2 = obstacles.filter { it.id == thisType.relevantID(rarity.ordinal+1)+goreText }

                            obstacleData.forEachIndexed { index, obs ->
                                val hit = playfield.virtualRectangle.fromRated(
                                    Rectangle(
                                        Vector(obs.centerX, obs.centerY),
                                        obs.width,
                                        obs.height
                                    )
                                )
                                val r = hit.decodeRated(thisType.ratedRect())
                                o2[index].x = r.left
                                o2[index].yConv = r.top
                                o2[index].scaledHeight = r.height
                                o2[index].scaledWidth = r.width
                                o2[index].visible = true
                                //obshit[obshitindex].scaledWidth = hit.width
                                //obshit[obshitindex].scaledHeight = hit.height
                                //obshit[obshitindex].x = hit.left
                                //obshit[obshitindex].y = GlobalAccess.virtualSize.height - hit.top
                                //obshit[obshitindex].visible = true
                                //obshitindex += 1
                            }
                        }
                    }
                }

                if (views.input.keys.justPressed(Key.UP)) {
                    playfield.jump()
                }

                if (views.input.keys.justPressed(Key.DOWN)) {
                    playfield.duck()
                }

                if (views.input.keys.pressing(Key.DOWN).not()) {
                    playfield.stopDuck()
                }

                val r = playfield.virtualRectangle.fromRated(playfield.hitboxRect)

                if(playfield.collisionCheck()){



                    fadein = true
                    if(GlobalAccess.fingers==1){
                        death()
                        GlobalScope.launchImmediately { sceneContainer.changeTo<GameOverScene>( ) }
                    }else{
                        GlobalAccess.fingers-= 1
                        playfield.sliced()
                        hand.cutFinger()
                    }
                }
                else if(playfield.ducking>0.0){
                    hand.onDuck()
                } else if (playfield.jumping) {
                    hand.onAir()
                } else {
                    hand.onGround()

                }

                hand.update(dt, r)
                playfield.update(dt)


                floor.visible = hand.activeAnimationType==Hand.ActiveAnimationType.TWOFINGER_CUT
                floor.alpha = -1*hand.animIndex*hand.animIndex*8.0/605.0 + hand.animIndex*8.0/55.0

                if (playfield.collisionCheck()) {

                }

                if (fadein) {
                    if (l2.volume < 0.6) l2.volume += 0.1
                    else fadein = false
                }
            }else{

            }
        }

    }


    // WINDOW SCENE VARIABLES

    //var windowDown = false
    //var windowUp = false
    //var window: Puntainer = Puntainer()
    //var gameScene = GameScene()


    // GAME SCENE VARIABLES

    var gameActive = true

    val hand = Hand("hand", oneRectangle())
    var playfield = Playfield("playfield", oneRectangle())
    var floor = Puntainer()
    var obstacles = mutableListOf<Puntainer>()


    var outside1: Puntainer = Puntainer()
    var outside2: Puntainer = Puntainer()

    fun backgroundRoll(dt: TimeSpan){
        outside1.x -= dt.seconds * playfield.level.speed * 1920
        outside2.x -= dt.seconds * playfield.level.speed * 1920
        if (outside1.x + outside1.width < -1000.0) {
            outside1.x += outside1.width * 2
        }
        if (outside2.x + outside2.width < -1000.0) {
            outside2.x += outside2.width * 2
        }
    }

    suspend fun adjustHand() {
        Hand.ActiveAnimationType.values().forEach { animType ->
            animType.puntainerOneFingers.children.clear()

            animType.sourceList().forEach { s ->
                Image(resourcesVfs[s].readBitmap()).also {
                    it.visible = false
                    animType.puntainerTwoFingers.addChild(it)
                }

                if (animType==Hand.ActiveAnimationType.TWOFINGER_JUMP){
                    println(s)
                }

            }
            if (animType==Hand.ActiveAnimationType.TWOFINGER_JUMP){
                println("${animType.puntainerTwoFingers.children.size}")
            }
            animType.puntainerOneFingers.children.clear()
            animType.sourceListOne().forEach {


                Image(resourcesVfs[it].readBitmap()).also {
                    it.visible = false
                    animType.puntainerOneFingers.addChild(it)
                }
            }
        }

    }

    fun death(){
        gameActive=false
        hand.activeAnimationType = Hand.ActiveAnimationType.TWOFINGER_RUN
        /*
        Hand.ActiveAnimationType.values().forEach {
            it.puntainerTwoFingers.children.fastForEach { it.visible=false }
            it.puntainerOneFingers.children.fastForEach { it.visible=false }
        }

         */

    }


    object FloorData {
        const val ratedY = 240.0 / 1080.0

        fun getHeight(x: Double? = null): Double {
            return if (x != null) {
                ratedY
            } else {
                ratedY
            }
        }
    }


}