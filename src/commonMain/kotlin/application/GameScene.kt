package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korau.sound.PlaybackParameters
import com.soywiz.korau.sound.PlaybackTimes
import com.soywiz.korau.sound.readMusic
import com.soywiz.korev.Key
import com.soywiz.korge.input.onClick
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.*
import com.soywiz.korim.font.TtfFont
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.text.TextAlignment
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



    override suspend fun Container.sceneInit(){
        println("game scene starts")
        GlobalAccess.fingers = 2
        val bmp = resourcesVfs["environment/Bg_Small.png"].readBitmap()
        outsiders.add(punImage("o1",bmp,Rectangle(0.0, 960.0, 0.0, 1080.0)))
        outsiders.add(punImage("o2",bmp,Rectangle(960.0, 2*960.0, 0.0, 1080.0)))
        outsiders.add(punImage("o3",bmp,Rectangle(960.0*2, 3*960.0, 0.0, 1080.0)))



    }



    override suspend fun Container.sceneMain() {
        scoreKeeper.load()
        val h = GlobalAccess.virtualSize.height.toDouble()
        val w = GlobalAccess.virtualSize.width.toDouble()





        /////////
        //GAME SCENE
        /////////


        val playMusic = true
        var fadein = false

        val l1 = resourcesVfs["musicbox.mp3"].readMusic()
        val l2 = resourcesVfs["altlayer.mp3"].readMusic()
        val l3 = resourcesVfs["ominous.mp3"].readMusic()
        val engineLoop = resourcesVfs["SFX/engine_heavy_loop-20.mp3"].readMusic()

        if (playMusic) {
            l1.play(PlaybackParameters(PlaybackTimes.INFINITE, volume = 0.3))
            l2.play(PlaybackParameters(PlaybackTimes.INFINITE, volume = 0.0))
            l3.play(PlaybackParameters(PlaybackTimes.INFINITE, volume = 0.0))
            engineLoop.play(PlaybackParameters(PlaybackTimes.INFINITE, volume = 1.0))
        }







        playfield.fitToFrame(Rectangle(0.0, w, FloorData.getHeight() * h, h))
        this.addChild(playfield)

        val rareScavengerList = listOf(
            "Red Bird",
            "Red Numbered Sign",
            "Yellow Tractor",
            "Green Overhead Sign",
            "Ginormous Blue Sign",
            "Small Blue Sign"
        )
        val rarerScavengerList = listOf(
            "Green Bird",
            "Green Numbered  Sign",
            "Red Tractor",
            "Yellow Overhead Sign",
            "Ginormous Green Sign",
            "Small Green Sign"
        )
        val rarestScavengerList = listOf(
            "Blue Bird",
            "Yellow Numbered Sign",
            "Blue Tractor",
            "Blue Overhead Sign",
            "Ginormous Yellow Sign",
            "Small Yellow Sign"
        )

        var hunt1 = rareScavengerList.random()
        var hunt2 = rarerScavengerList.random()
        var hunt3 = rarestScavengerList.random()
        var scavengerHuntList = listOf(hunt1, hunt2, hunt3)

        when (rareScavengerList.indexOf(hunt1)) {
            0 -> sh1Type = ObstacleTypes.LOWBIRD
            1 -> sh1Type = ObstacleTypes.LOWJUMP
            2 -> sh1Type = ObstacleTypes.LONGJUMP
            3 -> sh1Type = ObstacleTypes.DONTJUMP
            4 -> sh1Type = ObstacleTypes.DUCK
            5 -> sh1Type = ObstacleTypes.JUMPDUCK
        }
        when (rarerScavengerList.indexOf(hunt2)) {
            0 -> sh2Type = ObstacleTypes.LOWBIRD
            1 -> sh2Type = ObstacleTypes.LOWJUMP
            2 -> sh2Type = ObstacleTypes.LONGJUMP
            3 -> sh2Type = ObstacleTypes.DONTJUMP
            4 -> sh2Type = ObstacleTypes.DUCK
            5 -> sh2Type = ObstacleTypes.JUMPDUCK
        }
        when (rarestScavengerList.indexOf(hunt3)) {
            0 -> sh3Type = ObstacleTypes.LOWBIRD
            1 -> sh3Type = ObstacleTypes.LOWJUMP
            2 -> sh3Type = ObstacleTypes.LONGJUMP
            3 -> sh3Type = ObstacleTypes.DONTJUMP
            4 -> sh3Type = ObstacleTypes.DUCK
            5 -> sh3Type = ObstacleTypes.JUMPDUCK
        }

        // obstacles
        val rarityList = listOf("rare", "rarer", "rarest")
        for (j in 0..0) {
            for (i in 1..3) {
                listOf("", "-gore").forEach { goreText ->
                    val folder = rarityList[i - 1] + goreText
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

                    PunImage("low-bird-$i$goreText", resourcesVfs["obs/$folder/bird-$i.png"].readBitmap()).also {
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
        punImage("window", resourcesVfs["environment/window.png"].readBitmap(), oneRectangle(), true)

        punImage("hud", resourcesVfs["UI/hunt-score.png"].readBitmap())
        val font = TtfFont(resourcesVfs["MPLUSRounded1c-Medium.ttf"].readAll())


        val t1 =
            text(scavengerHuntList[0], font = font, textSize = 28.0, color = Colour.byHex("131A14").korgeColor).also {
                it.x = 108.0
                it.y = 117.0
                it.visible = false

            }

        val t2 =
            text(scavengerHuntList[1], font = font, textSize = 28.0, color = Colour.byHex("131A14").korgeColor).also {
                it.x = 108.0
                it.y = 167.0
                it.visible = false

            }

        val t3 =
            text(scavengerHuntList[2], font = font, textSize = 28.0, color = Colour.byHex("131A14").korgeColor).also {
                it.x = 108.0
                it.y = 217.0
                it.visible = false
            }




        //1594, 1894, 26, 106

        ////////////////////////////////////////// HEEEEREEEE
        println("enter")
        hand = Hand("hand", oneRectangle())
        println("middle")
        hand.suspendInit()
        //adjustHand()
        println("out")
        //////////////////////////////////////////
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

        floor = puntainer("floor", Rectangle(0.0, 1.0, 0.0, 1.0), relative = true) {
            it.singleColour(Colour.RED.korgeColor).also {
                it.alpha = 0.0
            }
        }

        puntainer("floor", Rectangle(0.0, 1.0, 0.0, 1.0), relative = true) {}.also {

        }


        //108, 350, 117, 267

        val scoreText = text(
            "11111",
            font = font,
            textSize = 42.0,
            color = Colour.byHex("131A14").korgeColor,
            alignment = TextAlignment.TOP_CENTER
        ).also {
            it.x = (1594.0 + 1894.0) * 0.5
            it.y = 26.0 + 12.0

        }
        scoreText.text = "0"



        val gameOver = Puntainer("gameover", Rectangle(0.0,1.0,0.0,1.0)).also { pt->
            this.addChild(pt)
            for(i in 1..7){
                Image(resourcesVfs["VFX/game_over-$i.png"].readBitmap()).also {
                    it.visible = false
                    pt.addChild(it)
                }
            }

        }

        val finalScoreBand  = punImage("finalBand", resourcesVfs["UI/bandaid.png"].readBitmap(), oneRectangle(), true).also {
            it.visible=false
        }



        val finalScoreText = text(
            "11111",
            font = font,
            textSize = 42.0,
            color = Colour.byHex("131A14").korgeColor,
            alignment = TextAlignment.TOP_CENTER
        ).also {
            it.x = (1594.0 + 1894.0) * 0.5
            it.y = 26.0 + 12.0
            it.visible=false
        }




        ////////////////////////////////////////////////////////////////

        // UPDATER

        ////////////////////////////////////////////////////////////////

        val fixedTime = TimeSpan(1000.0/60.0)
        this.addFixedUpdater(time=fixedTime){
            //backgroundRoll(TimeSpan(1000.0/60.0))

            if(GlobalAccess.fingers>0){
                if (views.input.keys.justPressed(Key.UP)) {
                    playfield.jump()
                }

                if (views.input.keys.justPressed(Key.DOWN)) {
                    playfield.duck()
                }

                if (views.input.keys.pressing(Key.DOWN).not()) {
                    playfield.stopDuck()
                }
                score += fixedTime.seconds * 10
                scoreText.text = score.toInt().toString()
            }

            val r = playfield.virtualRectangle.fromRated(playfield.hitboxRect)
            hand.update(fixedTime, r)

            playfield.update(fixedTime)
        }




        this.addUpdater { dt ->

            if (firstRun) {
                firstRun = false
                t1.visible = true
                t2.visible = true
                t3.visible = true
            }

            backgroundRoll(dt)

            if (gameActive) {
                obstacles.forEach {
                    it.visible = false
                }

                val goreText = if (GlobalAccess.fingers == 2) {
                    ""
                } else {
                    "-gore"
                }
                ObstacleTypes.values().forEach { thisType ->
                    playfield.level.obstacles.filter { it.type == thisType }.also { list ->
                        ObstacleRarity.values().forEach { rarity ->
                            val obstacleData = list.filter { it.obstacleRarity == rarity }
                            val o2 = obstacles.filter { it.id == thisType.relevantID(rarity.ordinal + 1) + goreText }

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
                            }
                        }
                    }
                }






                var collided = playfield.collisionCheck()
                val collidedObstacleRarity = if(playfield.level.obstacles.isNotEmpty()){
                     playfield.level.obstacles.first().obstacleRarity.ordinal
                }else{
                    5
                }

                if ((collided == sh1Type.ordinal)&&(collidedObstacleRarity==0)) {
                    score += 100
                    playfield.sliced()
                } else if ((collided == sh2Type.ordinal)&&(collidedObstacleRarity==1)) {
                    score += 250
                    playfield.sliced()
                } else if ((collided == sh3Type.ordinal)&&(collidedObstacleRarity==2)) {
                    score += 500
                    playfield.sliced()
                } else if (collided != -1) {
                    fadein = true
                    if (GlobalAccess.fingers == 1) {
                        death()

                    } else {
                        GlobalAccess.fingers -= 1
                        playfield.sliced()
                        hand.cutFinger()
                        scoreKeeper.addScore(score)
                        scoreKeeper.save()
                    }
                } else if (playfield.ducking > 0.0) {
                    hand.onDuck()
                } else if (playfield.jumping) {
                    hand.onAir()
                } else {
                    hand.onGround()
                }

                //val r = playfield.virtualRectangle.fromRated(playfield.hitboxRect)
                //hand.update(dt, r)
                //playfield.update(dt)


                floor.visible = hand.activeAnimationType == Hand.ActiveAnimationType.CUT
                floor.alpha = -1 * hand.animIndex * hand.animIndex * 8.0 / 605.0 + hand.animIndex * 8.0 / 55.0

                if (fadein) {
                    if (l2.volume < 0.6) l2.volume += 0.1
                    else fadein = false
                }
            }else{
                val r = playfield.virtualRectangle.fromRated(playfield.hitboxRect)
                hand.update(dt,r)
                if(hand.isDead){
                    gameOverIndex += dt.seconds*2
                    if(gameOverIndex>=gameOver.size){
                        finalScoreText.x = GlobalAccess.virtualSize.width*0.5
                        finalScoreText.y = GlobalAccess.virtualSize.height*0.7
                        finalScoreText.visible=true
                        finalScoreText.text = "Score: ${score.toInt()}"

                        finalScoreBand.scaledWidth = 300.0
                        finalScoreBand.scaledHeight = 80.0
                        finalScoreBand.x = GlobalAccess.virtualSize.width*0.5-150.0
                        finalScoreBand.y = GlobalAccess.virtualSize.height*0.7-10.0
                        finalScoreBand.visible=true

                    }else{
                        gameOver.children.fastForEach { it.visible=false }
                        gameOver.children[gameOverIndex.toInt()].visible=true
                    }
                    //launchImmediately { sceneContainer.changeTo<GameOverScene>() }
                }




            }
        }

        onClick {
            if(gameOverIndex>=gameOver.size){
                launchImmediately { sceneContainer.changeTo<EntryScene>( ) }
            }
        }
        println("game scene called")
    }

    // WINDOW SCENE VARIABLES

    //var windowDown = false
    //var windowUp = false
    //var window: Puntainer = Puntainer()
    //var gameScene = GameScene()


    // GAME SCENE VARIABLES

    lateinit var sh1Type: ObstacleTypes
    lateinit var sh2Type: ObstacleTypes
    lateinit var sh3Type: ObstacleTypes

    var score = 0.0
    var gameActive = true
    lateinit var hand: Hand //= Hand("hand", oneRectangle())
    var playfield = Playfield("playfield", oneRectangle())
    lateinit var floor:Puntainer
    var obstacles = mutableListOf<Puntainer>()
    val outsiders = mutableListOf<Puntainer>()
    var firstRun = true
    var gameOverIndex = 0.0

    fun backgroundRoll(dt: TimeSpan) {
        if(GlobalAccess.fingers>0){
            outsiders.forEach {
                it.x -= dt.seconds *playfield.level.speed * 1920
                if(it.x + it.width< -20.0){
                    it.x += it.width * 3
                }
            }
        }

    }

    /*
    suspend fun adjustHand() {
        println("hand adjusting")
        Hand.ActiveAnimationType.values().forEach { animType ->
            animType.puntainerOneFingers.children.clear()

            animType.sourceList().forEach { s ->
                Image(resourcesVfs[s].readBitmap()).also {
                    it.visible = false
                    animType.puntainerTwoFingers.addChild(it)
                }


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

     */




    fun death() {
        gameActive = false
        hand.activeAnimationType = Hand.ActiveAnimationType.CUT
        GlobalAccess.fingers=0
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