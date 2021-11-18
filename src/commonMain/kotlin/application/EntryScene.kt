package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.onDown
import com.soywiz.korge.input.onUp
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import modules.basic.Colour
import pungine.PunScene
import pungine.Puntainer
import pungine.geometry2D.Rectangle

/** This scene is the template for a PunGineIV game
 *
 */

@KorgeInternal
@DelicateCoroutinesApi
class EntryScene : PunScene() {
    override suspend fun Container.sceneMain() {
        if (GlobalAccess.soundsAreOn) {
            MusicPlayer.play("musicbox.mp3")
        }


        val outsiders = Outside()
        outsiders.deploy(addFunction = { p: Puntainer, r: Rectangle ->
            scenePuntainer.addPuntainer(p, r)
        })
        /*
        outsiders.deploy(addFunction = {l: List<Puntainer>->
            l.forEach {
                this.addChild(it)
            }
        })

         */

        //val bmp = resourcesVfs["environment/Bg_Small-3.png"].readBitmap()
        //outsiders.add(punImage("o1",bmp.clone(),Rectangle(0.0, 960.0, 0.0, 1080.0)))
        //outsiders.add(punImage("o2",bmp.clone(),Rectangle(960.0, 2*960.0, 0.0, 1080.0)))
        //outsiders.add(punImage("o3",bmp,Rectangle(960.0*2, 3*960.0, 0.0, 1080.0)))


        window = scenePuntainer.punImage(
            "id",
            Rectangle(0.0, 1.0, 0.0, 1.0),
            resourcesVfs["UI/glass-up.png"].readBitmap()
        ).also {
            it.alpha = 0.8
        }

        window = scenePuntainer.punImage(
            "id",
            Rectangle(0.0, 1.0, 0.0, 1.0),
            resourcesVfs["UI/name.png"].readBitmap()

        ).also {
            it.alpha = 0.8
        }

        credits = scenePuntainer.punImage(
            "credits_scene",
            Rectangle(0.0, 1.0, 0.0, 1.0),
            resourcesVfs["UI/glass-credits.png"].readBitmap()

        ).also {
            it.visible = false
        }

        scenePuntainer.punImage(
            "id",
            Rectangle(0.0, 1.0, 0.0, 1.0),
            resourcesVfs["UI/Wintop.png"].readBitmap()
        )

        scenePuntainer.punImage(
            "id",
            Rectangle(0.0, 1.0, 0.0, 1.0),
            resourcesVfs["UI/Windown.png"].readBitmap()
        )

        val playDown = scenePuntainer.punImage(
            "play_down",
            Rectangle(232.0 / 1920.0, 557.0 / 1920.0, 1 - 864.0 / 1080.0, 1 - 971.0 / 1080.0),
            resourcesVfs["UI/play-pushed.png"].readBitmap()
        ).also {
            it.visible = false
        }
        val playUp = scenePuntainer.punImage(
            "play_up",
            Rectangle(232.0 / 1920.0, 557.0 / 1920.0, 1 - 864.0 / 1080.0, 1 - 971.0 / 1080.0),
            resourcesVfs["UI/play-normal.png"].readBitmap()
        ).also { exit ->
            exit.onDown {
                exit.visible = false
                playDown.visible = true
            }
        }

        val scoreDown = scenePuntainer.punImage(
            "score_down",
            Rectangle(556.0 / 1920.0, 876.0 / 1920.0, 1 - 854.0 / 1080.0, 1 - 961.0 / 1080.0),
            resourcesVfs["UI/score-pushed.png"].readBitmap()
        ).also {
            it.visible = false
        }
        val scoreUp = scenePuntainer.punImage(
            "score_up",
            Rectangle(556.0 / 1920.0, 876.0 / 1920.0, 1 - 854.0 / 1080.0, 1 - 961.0 / 1080.0),
            resourcesVfs["UI/score-normal.png"].readBitmap()
        ).also { exit ->
            exit.onDown {
                exit.visible = false
                scoreDown.visible = true
            }
        }

        val settingsDown = scenePuntainer.punImage(
            "settings_down",
            Rectangle(880.0 / 1920.0, 1200.0 / 1920.0, 1 - 844.0 / 1080.0, 1 - 951.0 / 1080.0),
            resourcesVfs["UI/credits-pushed.png"].readBitmap()
        ).also {
            it.visible = false
        }
        val settingsUp = scenePuntainer.punImage(
            "settings_up",
            Rectangle(880.0 / 1920.0, 1200.0 / 1920.0, 1 - 844.0 / 1080.0, 1 - 951.0 / 1080.0),
            resourcesVfs["UI/credits-normal.png"].readBitmap()
        ).also { exit ->
            exit.onDown {
                exit.visible = false
                settingsDown.visible = true
            }
        }


        val exitDown = scenePuntainer.punImage(
            "exit_down",
            Rectangle(1204.0 / 1920.0, 1524.0 / 1920.0, 1 - 834.0 / 1080.0, 1 - 941.0 / 1080.0),
            resourcesVfs["UI/exit-pushed.png"].readBitmap()
        ).also {
            it.visible = false

        }
        val exitUp = scenePuntainer.punImage(
            "exit_up",
            Rectangle(1204.0 / 1920.0, 1524.0 / 1920.0, 1 - 834.0 / 1080.0, 1 - 941.0 / 1080.0),
            resourcesVfs["UI/exit-normal.png"].readBitmap()
        ).also { exit ->
            exit.onDown {
                exit.visible = false
                exitDown.visible = true
            }

            exit.onClick {
            }
        }


        sceneContainer.onClick {
            settingsUp.visible = true
            exitUp.visible = true
            scoreUp.visible = true
            playUp.visible = true
            credits.visible = false
        }

        sceneContainer.onUp {
            if (exitDown.visible) {
                exitUp.visible = true
                exitDown.visible = false
                sceneContainer.views.gameWindow.close()
            }

            if (settingsDown.visible) {
                settingsUp.visible = false
                settingsDown.visible = false
                exitUp.visible = false
                exitDown.visible = false
                scoreUp.visible = false
                scoreDown.visible = false
                playUp.visible = false
                playDown.visible = false
                credits.visible = true

                // settings event
            }

            if (scoreDown.visible) {
                scoreUp.visible = true
                scoreDown.visible = false
                // score event
            }

            if (playDown.visible) {
                playUp.visible = true
                playDown.visible = false
                SfxPlayer.playSfx("carDoorStartUp-16.mp3")
                launchImmediately { sceneContainer.changeTo<WindowScene>() }
            }


        }


        /**
         * play
        232, 557, 864, 971

        top score
        556, 876, 854, 961

        settings
        880, 1200, 844, 951

        exit
        1204, 1524, 834, 941
         */

        addUpdater { dt ->
            outsiders.update(dt.seconds * 0.3 * 1920)
            /*
            outsiders.forEach {
                it.x -= dt.seconds*0.3*1920
                if(it.x + it.width< -20.0){
                    it.x += it.width * 3
                }
            }

             */

        }



        super.sceneAfterInit()
        println("entry called")
    }

    var window: Puntainer = Puntainer()
    var credits: Puntainer = Puntainer()
    //val outsiders = mutableListOf<Puntainer>()


    // delete from all under here for a new scene

    suspend fun openingCrawl() {

        val bg = scenePuntainer.solidRect("id", Rectangle(0.0, 1.0, 0.0, 1.0), Colour.rgba(0.04, 0.02, 0.04, 1.0))

        /*
        val img = punImage(
            "id",
            resourcesVfs["pungo_transparent.png"].readBitmap(),
            Rectangle(390.0, 890.0, 110.0, 610.0)
        ).also {
            it.visible = false
            it.onClick {
                launchImmediately { sceneContainer.changeTo<GameScene>() }
            }
        }

         */


        val resource = resourcesVfs["PunGine.png"].readBitmap()
        scenePuntainer.punImage("id", Rectangle(0.0, 1.0, 0.0, 1.0), resource).also {
            it.alpha = 0.0
            var counter = 0.0
            it.addUpdater { dt: TimeSpan ->
                if (counter < 3.0) {
                    bg.alpha = 1.0
                    counter += dt.seconds
                    if (counter < 1.2) {
                        it.alpha = counter / 1.2
                    } else if (counter > 1.8) {
                        it.alpha = (3.0 - counter) / 1.2
                    }
                } else {
                    it.alpha = 0.0
                    bg.alpha = 0.0
                    //img.visible = true
                }
            }
        }
    }
}