package application

import com.soywiz.korau.sound.PlaybackParameters
import com.soywiz.korau.sound.PlaybackTimes
import com.soywiz.korau.sound.readMusic
import com.soywiz.korev.Key
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
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
        val h = GlobalAccess.virtualSize.height.toDouble()
        val w = GlobalAccess.virtualSize.width.toDouble()

        val playMusic = false
        var fadein = false

        val l1 = resourcesVfs["musicbox.mp3"].readMusic()
        val l2 = resourcesVfs["altlayer.mp3"].readMusic()
        val l3 = resourcesVfs["ominous.mp3"].readMusic()

        if (playMusic) {
            l1.play(PlaybackParameters(PlaybackTimes.INFINITE))
            l2.play(PlaybackParameters(PlaybackTimes.INFINITE, volume = 0.0))
            l3.play(PlaybackParameters(PlaybackTimes.INFINITE, volume = 0.0))
        }

        floor = puntainer("floor", Rectangle(0.0, 1.0, 0.0, FloorData.getHeight()), relative = true) { puntainer ->

            puntainer.singleColour(Colour.GREEN.korgeColor).also {
                it.alpha = 0.1
            }
        }

        //puntainer() {  }

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

        adjustHand()
        this.addChild(hand)


        // obstacles
        for (i in 0..1) {
            PunImage("dont-jump-1", resourcesVfs["obs/rare/dont-jump-1.png"].readBitmap()).also {
                obstacles.add(it)
                this.addChild(it)
                it.visible = false
            }

            PunImage("duck-1", resourcesVfs["obs/rare/duck-1.png"].readBitmap()).also {
                obstacles.add(it)
                this.addChild(it)
                it.visible = false
            }

            PunImage("high-jump-1", resourcesVfs["obs/rare/high-jump-1.png"].readBitmap()).also {
                obstacles.add(it)
                this.addChild(it)
                it.visible = false
            }

            PunImage("jump-duck-1", resourcesVfs["obs/rare/jump-duck-1.png"].readBitmap()).also {
                obstacles.add(it)
                this.addChild(it)
                it.visible = false
            }

            PunImage("long-jump-1", resourcesVfs["obs/rare/long-jump-1.png"].readBitmap()).also {
                obstacles.add(it)
                this.addChild(it)
                it.visible = false
            }

            PunImage("low-jump-1", resourcesVfs["obs/rare/low-jump-1.png"].readBitmap()).also {
                obstacles.add(it)
                this.addChild(it)
                it.visible = false
            }

            PunImage("bird-1", resourcesVfs["obs/rare/bird-1.png"].readBitmap()).also {
                obstacles.add(it)
                this.addChild(it)
                it.visible = false
            }

        }

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


        /*
        obstacles.add(puntainer("obs", Rectangle(w,w+50.0,FloorData.getHeight()*GlobalAccess.virtualSize.height,FloorData.getHeight()*GlobalAccess.virtualSize.height+50.0)) {
            it.singleColour(Colour.RED.korgeColor)


        })

         */


        this.addUpdater { dt ->


            /*
            obstacles.forEach {
                it.x = it.x - dt.milliseconds*0.2
            }

             */

            obstacles.forEach {
                it.visible = false
            }



            outside1.x -= dt.seconds * playfield.level.speed * 1920
            outside2.x -= dt.seconds * playfield.level.speed * 1920
            if (outside1.x + outside1.width < -1000.0) {
                outside1.x += outside1.width * 2
            }
            if (outside2.x + outside2.width < -1000.0) {
                outside2.x += outside2.width * 2
            }


            val obshit = children.filterIsInstance<Puntainer>().filter { it.id == "obshit" }
            var obshitindex = 0
            obshit.forEach {
                it.visible = false
            }
            ObstacleTypes.values().forEach { thisType ->
                playfield.level.obstacles.filter { it.type == thisType }.also { list ->
                    val o = obstacles.filter { it.id == thisType.relevantID() }
                    list.forEachIndexed { index, obs ->
                        val hit = playfield.virtualRectangle.fromRated(
                            Rectangle(
                                Vector(obs.centerX, obs.centerY),
                                obs.width,
                                obs.height
                            )
                        )
                        val r = hit.decodeRated(thisType.ratedRect())
                        o[index].x = r.left
                        o[index].yConv = r.top
                        o[index].scaledHeight = r.height
                        o[index].scaledWidth = r.width
                        o[index].visible = true
                        obshit[obshitindex].scaledWidth = hit.width
                        obshit[obshitindex].scaledHeight = hit.height
                        obshit[obshitindex].x = hit.left
                        obshit[obshitindex].y = GlobalAccess.virtualSize.height - hit.top
                        obshit[obshitindex].visible = true
                        obshitindex += 1
                    }
                }
            }

            /*

            playfield.level.obstacles.forEachIndexed { index,obs->
                val r = playfield.virtualRectangle.fromRated(Rectangle(Vector(obs.centerX,obs.centerY),obs.width,obs.height))

                obstacles[index].x = r.left
                obstacles[index].yConv = r.top
                obstacles[index].scaledHeight = r.height
                obstacles[index].scaledWidth = r.width
                obstacles[index].visible=true
            }

             */




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

            if (playfield.ducking > 0.0) {
                hand.onDuck()
            } else if (playfield.jumping) {
                hand.onAir()
            } else {
                hand.onGround()

            }

            hand.update(dt, r)
            playfield.update(dt)

            floor.visible = !playfield.collisionCheck()

            if (playfield.collisionCheck()) {
                fadein = true
            }

            if (fadein) {
                if (l2.volume < 0.6) l2.volume += 0.1
                else fadein = false
            }

        }
        super.sceneAfterInit()
    }

    val hand = Hand("hand", oneRectangle())
    var playfield = Playfield("playfield", oneRectangle())
    var floor = Puntainer()
    var obstacles = mutableListOf<Puntainer>()

    //val gravity = 200.0
    //var hitboxDy =0.0
    var outside1: Puntainer = Puntainer()
    var outside2: Puntainer = Puntainer()

    suspend fun adjustHand() {
        Hand.ActiveAnimationType.values().forEach { animType ->
            animType.sourceList().forEach { s ->
                Image(resourcesVfs[s].readBitmap()).also {
                    it.visible = false
                    animType.puntainerTwoFingers.addChild(it)
                }

            }
        }
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