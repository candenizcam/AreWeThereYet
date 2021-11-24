package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Key
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.*
import com.soywiz.korim.font.Font
import com.soywiz.korim.font.TtfFont
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import modules.basic.Colour
import pungine.Button
import pungine.PunImage
import pungine.PunScene
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle

/** This scene is the template for a PunGineIV game
 *
 */

@DelicateCoroutinesApi
@KorgeInternal
class GameScene : PunScene() {
    //override fun createSceneView(): Container = Puntainer()


    override suspend fun Container.sceneInit() {
        GlobalAccess.fingers = 2
        scenePuntainer.punImage("jackal", oneRectangle(), resourcesVfs["VFX/game-jackal.png"].readBitmap())
        outside.deploy(addFunction = { p: Puntainer, r: Rectangle ->
            scenePuntainer.addPuntainer(p, r)
        }, false)


    }


    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun Container.sceneMain() {
        if (GlobalAccess.soundsAreOn) {
            GlobalAccess.musicPlayer.play("musicbox.mp3")
        }

        val h = GlobalAccess.virtualSize.height.toDouble()
        val w = GlobalAccess.virtualSize.width.toDouble()


        /////////
        //GAME SCENE
        /////////


        //playfield.fitToFrame(Rectangle(0.0, w, FloorData.getHeight() * h, h))
        this.addChild(playfield)
        playfield.reshape(Rectangle(0.0, w, FloorData.getHeight() * h, h))
        font = TtfFont(resourcesVfs["MPLUSRounded1c-Medium.ttf"].readAll())
        generateScavengerHunt()

        // obstacles

        /*
        listOf("", "-gore").forEach { goreText ->
            obstacleLoader(goreText)
        }

         */
        obstacleLoader("")
        obstacleLoader("-gore")

        val initialInvisible = mutableListOf<Container>()

        scenePuntainer.punImage("window", oneRectangle(), resourcesVfs["environment/window.png"].readBitmap()).also {
            it.visible = false
            initialInvisible.add(it)
        }


        scenePuntainer.punImage(
            "hud1",
            thisRectangle.toRated(Rectangle(0.0, 554.0, 1080.0, 648.0)),
            resourcesVfs["UI/postit.png"].readBitmap()
        ).also {
            it.visible = false
            initialInvisible.add(it)
        }
        scenePuntainer.punImage(
            "hud2",
            thisRectangle.toRated(Rectangle(1594.0, 1894.0, 1054.0, 974.0)),
            resourcesVfs["UI/bandaid.png"].readBitmap()
        ).also {
            it.visible = false
            initialInvisible.add(it)
        }


        t1 =
            text(scavengerHuntList[0], font = font, textSize = 28.0, color = Colour.byHex("131A14").korgeColor).also {
                it.x = 108.0
                it.y = 117.0
                it.visible = false

            }

        t2 =
            text(scavengerHuntList[1], font = font, textSize = 28.0, color = Colour.byHex("131A14").korgeColor).also {
                it.x = 108.0
                it.y = 167.0
                it.visible = false

            }

        t3 =
            text(scavengerHuntList[2], font = font, textSize = 28.0, color = Colour.byHex("131A14").korgeColor).also {
                it.x = 108.0
                it.y = 217.0
                it.visible = false
            }


        //1594, 1894, 26, 106

        ////////////////////////////////////////// HEEEEREEEE
        hand = Hand("hand", oneRectangle()).also {
            it.visible = false
            initialInvisible.add(it)
        }
        hand.suspendInitAlternative("two")
        hand.suspendInitAlternative("one")
        //adjustHand()

        //////////////////////////////////////////
        this.addChild(hand)



        for (i in (0..10)) {
            //PunImage("obshit",resourcesVfs["obstacle.png"].readBitmap()).also {
            //    this.addChild(it)
            //    it.visible=false
            //}
            scenePuntainer.solidRect("obshit", Rectangle(0.0, 1.0, 0.0, 1.0), colour = Colour.RED).also {
                it.visible = false
                it.alpha = 0.2
            }
        }

        floor = scenePuntainer.relativePuntainer("floor", Rectangle(0.0, 1.0, 0.0, 1.0)) {
            it.singleColour(Colour.RED.korgeColor).also {
                it.alpha = 0.0
            }
        }

        scenePuntainer.relativePuntainer("floor", Rectangle(0.0, 1.0, 0.0, 1.0)) {}


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


            it.visible = false
            initialInvisible.add(it)
        }
        scoreText.text = "0"
        var scoreSaved = false


        var gameOverItems = mutableListOf<Container>() //to set all invisible with a single move
        // game over stuff from here
        val gameOver = Puntainer("gameover", Rectangle(0.0, 1.0, 0.0, 1.0)).also { pt ->
            this.addChild(pt)
            for (i in 1..7) {
                Image(resourcesVfs["VFX/game_over-$i.png"].readBitmap()).also {
                    it.visible = false
                    pt.addChild(it)
                }
            }
            gameOverItems.add(pt)

        }


        val finalScoreBand =
            scenePuntainer.punImage("finalBand", oneRectangle(), resourcesVfs["UI/bandaid.png"].readBitmap()).also {
                it.visible = false
                gameOverItems.add(it)
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
            it.visible = false
            gameOverItems.add(it)
        }

        val menuButton = Button(
            "menu",
            resourcesVfs["UI/main-normal.png"].readBitmap(),
            resourcesVfs["UI/main-pushed.png"].readBitmap()
        ).also {
            it.clickFunction = {
                launchImmediately { sceneContainer.changeTo<EntryScene>() }
            }
            it.visible = false
            it.inactive = true
            gameOverItems.add(it)
        }
        scenePuntainer.addPuntainer(menuButton)

        val playAgainButton = Button(
            "play again",
            resourcesVfs["UI/play-again-normal.png"].readBitmap(),
            resourcesVfs["UI/play-again-pushed.png"].readBitmap()
        ).also {
            it.clickFunction = {
                //launchImmediately { sceneContainer.changeTo<GameScene>() }
                resetGame()

                gameOverItems.forEach {
                    it.visible = false
                }

            }
            it.visible = false
            it.inactive = true
            gameOverItems.add(it)
        }
        scenePuntainer.addPuntainer(playAgainButton)


        ////////////////////////////////////////////////////////////////

        // UPDATER

        ////////////////////////////////////////////////////////////////

        val fixedTime = TimeSpan(1000.0 / 60.0)
        this.addFixedUpdater(time = fixedTime) {
            //backgroundRoll(TimeSpan(1000.0/60.0))


            val r = playfield.virtualRectangle.fromRated(playfield.hitboxRect)
            hand.update(fixedTime, r)


        }


        this.addUpdater { dt ->

            if (firstRun) {
                firstRun = false
                t1.visible = true
                t2.visible = true
                t3.visible = true
                initialInvisible.forEach {
                    it.visible = true
                }
                scenePuntainer.puntainers.filter {
                    it.id == "jackal"
                }.first().visible = false
                outside.setVisible(true)
                launchImmediately {
                    // this can be used as an afterloader
                }
            }

            backgroundRoll(dt)

            if (gameActive) {

                score += fixedTime.seconds * 10
                scoreText.text = score.toInt().toString()


                if (GlobalAccess.fingers > 0) {
                    if (views.input.keys.justPressed(Key.UP) || views.input.keys.justPressed(Key.W)) {
                        playfield.jump()
                    }

                    if (views.input.keys.justPressed(Key.DOWN) || views.input.keys.justPressed(Key.S)) {
                        playfield.duck()
                    }

                    if (views.input.keys.pressing(Key.DOWN).not() || views.input.keys.justPressed(Key.S)) {
                        playfield.stopDuck()
                    }

                }
                launchImmediately { playfield.update(dt) }
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
                                o2[index].reshape(r)
                                o2[index].visible = true
                            }
                        }
                    }
                }


                var collided = playfield.collisionCheck()
                val collidedObstacleRarity = if (playfield.level.obstacles.isNotEmpty()) {
                    playfield.level.obstacles.first().obstacleRarity.ordinal
                } else {
                    5
                }

                // if ((collided == sh1Type.ordinal) && (collidedObstacleRarity == 0)) {
                if ((collided == sh1Type.ordinal) && (collidedObstacleRarity == 0) || ((sh1Type == ObstacleTypes.LOWBIRD) && (collided == 6) && (collidedObstacleRarity == 0))) {
                    score += 100
                    launchImmediately { SfxPlayer.playSfx("diDing.mp3") }
                    playfield.sliced()
                } else if ((collided == sh2Type.ordinal) && (collidedObstacleRarity == 1) || ((sh2Type == ObstacleTypes.LOWBIRD) && (collided == 6) && (collidedObstacleRarity == 1))) {
                    score += 250
                    launchImmediately { SfxPlayer.playSfx("diDing.mp3") }
                    playfield.sliced()
                } else if ((collided == sh3Type.ordinal) && (collidedObstacleRarity == 2) || ((sh3Type == ObstacleTypes.LOWBIRD) && (collided == 6) && (collidedObstacleRarity == 2))) {
                    score += 500
                    launchImmediately { SfxPlayer.playSfx("diDing.mp3") }
                    playfield.sliced()
                } else if (collided != -1) {
                    //   fadein = true
                    if (GlobalAccess.fingers == 1) {
                        death()

                    } else {
                        GlobalAccess.fingers -= 1
                        playfield.sliced()
                        launchImmediately { hand.cutFinger() }
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
            } else {
                val r = playfield.virtualRectangle.fromRated(playfield.hitboxRect)
                hand.update(dt, r)
                if (hand.isDead) {
                    gameOverIndex += dt.seconds * screenBleedSpeed
                    if (gameOverIndex >= gameOver.size) {
                        finalScoreText.x = GlobalAccess.virtualSize.width * 0.5
                        finalScoreText.y = GlobalAccess.virtualSize.height * 0.7
                        finalScoreText.visible = true
                        finalScoreText.text = "Score: ${score.toInt()}"
                        if (!scoreSaved) {
                            scoreSaved = true
                            GlobalAccess.scoreKeeper.addScore(score.toInt())
                            launchImmediately { GlobalAccess.scoreKeeper.save() }
                        }


                        finalScoreBand.scaledWidth = 300.0
                        finalScoreBand.scaledHeight = 80.0
                        finalScoreBand.x = GlobalAccess.virtualSize.width * 0.5 - 150.0
                        finalScoreBand.y = GlobalAccess.virtualSize.height * 0.7 - 10.0
                        finalScoreBand.visible = true

                        menuButton.reshape(
                            Rectangle(
                                Vector(
                                    638.0,
                                    GlobalAccess.virtualSize.height - 781.0 - 60.0
                                ), 320.0, 107.0, Rectangle.Corners.TOP_LEFT
                            )
                        )
                        menuButton.inactive = false
                        menuButton.visible = true

                        playAgainButton.reshape(
                            Rectangle(
                                Vector(
                                    962.0,
                                    GlobalAccess.virtualSize.height - 771.0 - 60.0
                                ), 320.0, 107.0, Rectangle.Corners.TOP_LEFT
                            )
                        )
                        playAgainButton.inactive = false
                        playAgainButton.visible = true

                    } else {
                        gameOver.visible = true
                        gameOver.children.fastForEach { it.visible = false }
                        gameOver.children[gameOverIndex.toInt()].visible = true

                    }
                }
            }
        }
    }

    fun generateScavengerHunt() {
        val rareScavengerList = listOf(
            "Red Bird",
            "Red Numbered Sign",
            "Yellow Tractor",
            "Green Overhead Sign",
            "Cloudy Blue Sign",
            "Snowy Blue Sign"
        )
        val rarerScavengerList = listOf(
            "Green Bird",
            "Green Numbered  Sign",
            "Red Tractor",
            "Yellow Overhead Sign",
            "Green Coffee Sign",
            "Green Tea Sign"
        )
        val rarestScavengerList = listOf(
            "Blue Bird",
            "Yellow Numbered Sign",
            "Blue Tractor",
            "Blue Overhead Sign",
            "Yellow Hamburger Sign",
            "Yellow Hot-Dog Sign"
        )

        val hunt1 = rareScavengerList.random()
        val hunt2 = rarerScavengerList.random()
        val hunt3 = rarestScavengerList.random()
        scavengerHuntList = listOf(hunt1, hunt2, hunt3)

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




        if(firstRun) {
            t1 =
                sceneContainer.text(
                    scavengerHuntList[0],
                    font = font,
                    textSize = 28.0,
                    color = Colour.byHex("131A14").korgeColor
                ).also {
                    it.x = 108.0
                    it.y = 117.0
                    it.visible = false

                }

            t2 =
                sceneContainer.text(
                    scavengerHuntList[1],
                    font = font,
                    textSize = 28.0,
                    color = Colour.byHex("131A14").korgeColor
                ).also {
                    it.x = 108.0
                    it.y = 167.0
                    it.visible = false

                }

            t3 =
                sceneContainer.text(
                    scavengerHuntList[2],
                    font = font,
                    textSize = 28.0,
                    color = Colour.byHex("131A14").korgeColor
                ).also {
                    it.x = 108.0
                    it.y = 217.0
                    it.visible = false
                }
        } else {
            t1.text = scavengerHuntList[0]
            t2.text = scavengerHuntList[1]
            t3.text = scavengerHuntList[2]
        }
    }

    fun resetGame() {
        GlobalAccess.fingers = 2
        hand.resetGame()
        playfield.level.obstacles.clear()
        score = 0.0
        gameOverIndex = 0.0
        screenBleedSpeed = 4.0
        gameActive = true
        playfield.level.speed = 0.3
        generateScavengerHunt()

    }


    var screenBleedSpeed = 2.0 // this is the coefficient for game over bleeding, it is faster on the second death


    // WINDOW SCENE VARIABLES

    //var windowDown = false
    //var windowUp = false
    //var window: Puntainer = Puntainer()
    //var gameScene = GameScene()


    // GAME SCENE VARIABLES

    lateinit var sh1Type: ObstacleTypes
    lateinit var sh2Type: ObstacleTypes
    lateinit var sh3Type: ObstacleTypes

    lateinit var font: Font

    lateinit var t1: Text
    lateinit var t2: Text
    lateinit var t3: Text

    lateinit var scavengerHuntList: List<String>
    var score = 0.0
    var gameActive = true
    lateinit var hand: Hand //= Hand("hand", oneRectangle())
    var playfield = Playfield("playfield", oneRectangle())
    lateinit var floor: Puntainer
    var obstacles = mutableListOf<Puntainer>()

    //val outsiders = mutableListOf<Puntainer>()
    var firstRun = true
    var gameOverIndex = 0.0
    var outside: Outside = Outside()


    private suspend fun obstacleLoader(goreText: String) {
        val rarityList = listOf("rare", "rarer", "rarest")
        val otherList =
            listOf("dont-jump", "duck", "high-jump", "jump-duck", "long-jump", "low-jump", "bird", "low-bird")
        for (i in 1..3) {
            val folder = rarityList[i - 1] + goreText

            otherList.forEach { otherString ->
                val os = if (otherString == "low-bird") {
                    "bird"
                } else {
                    otherString
                }
                PunImage("$otherString-$i$goreText", resourcesVfs["obs/$folder/$os-$i.png"].readBitmap()).also {
                    obstacles.add(it)
                    scenePuntainer.addChild(it)
                    it.visible = false
                }
            }
        }

    }


    fun backgroundRoll(dt: TimeSpan) {
        if (GlobalAccess.fingers > 0) {
            outside.update(dt.seconds * playfield.level.speed * 1920)
        }

    }


    @OptIn(DelicateCoroutinesApi::class)
    fun death() {
        launchImmediately { SfxPlayer.playSfx("cut.mp3") }
        gameActive = false

        hand.activeAnimationType = Hand.ActiveAnimationType.CUT
        GlobalAccess.fingers = 0

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