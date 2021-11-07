package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Key
import com.soywiz.korge.input.mouse
import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.onDown
import com.soywiz.korge.input.onUp
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.centerOnStage
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import pungine.PunImage
import pungine.PunScene
import pungine.Puntainer
import pungine.geometry2D.Rectangle

/** This scene is the template for a PunGineIV game
 *
 */

@DelicateCoroutinesApi
class EntryScene : PunScene() {
    override fun createSceneView(): Container = Puntainer()

    override suspend fun Container.sceneInit(){
        //openingCrawl()

        SfxPlayer.loadSounds()

        outside1 =
            punImage("outside", resourcesVfs["environment/Bg2.png"].readBitmap(), Rectangle(0.0, 3820.0, 0.0, 1080.0))
        outside2 = punImage(
            "outside",
            resourcesVfs["environment/Bg2.png"].readBitmap().flipX(),
            Rectangle(3820.0, 2 * 3820.0, 0.0, 1080.0)
        )

        window = punImage(
            "id",
            resourcesVfs["UI/blur.jpg"].readBitmap(),
            Rectangle(0.0,1.0,0.0,1.0),relative = true
        ).also {
            it.alpha=0.8
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

        val playDown = punImage("play_down",resourcesVfs["UI/play-pushed.png"].readBitmap(),Rectangle(232.0/1920.0,557.0/1920.0,1-864.0/1080.0,1-971.0/1080.0),relative = true).also {
            it.visible = false
        }
        val playUp = punImage("play_up",resourcesVfs["UI/play-normal.png"].readBitmap(),Rectangle(232.0/1920.0,557.0/1920.0,1-864.0/1080.0,1-971.0/1080.0),relative = true).also { exit->
            exit.onDown {
                exit.visible = false
                playDown.visible=true
            }
        }

        val scoreDown = punImage("score_down",resourcesVfs["UI/score-pushed.png"].readBitmap(),Rectangle(556.0/1920.0,876.0/1920.0,1-854.0/1080.0,1-961.0/1080.0),relative = true).also {
            it.visible = false
        }
        val scoreUp = punImage("score_up",resourcesVfs["UI/score-normal.png"].readBitmap(),Rectangle(556.0/1920.0,876.0/1920.0,1-854.0/1080.0,1-961.0/1080.0),relative = true).also { exit->
            exit.onDown {
                exit.visible = false
                scoreDown.visible=true
            }
        }

        val settingsDown = punImage("settings_down",resourcesVfs["UI/settings-pushed.png"].readBitmap(),Rectangle(880.0/1920.0,1200.0/1920.0,1-844.0/1080.0,1-951.0/1080.0),relative = true).also {
            it.visible = false
        }
        val settingsUp = punImage("settings_up",resourcesVfs["UI/settings-normal.png"].readBitmap(),Rectangle(880.0/1920.0,1200.0/1920.0,1-844.0/1080.0,1-951.0/1080.0),relative = true).also { exit->
            exit.onDown {
                exit.visible = false
                settingsDown.visible=true
            }
        }


        val exitDown = punImage("exit_down",resourcesVfs["UI/exit-pushed.png"].readBitmap(),Rectangle(1204.0/1920.0,1524.0/1920.0,1-834.0/1080.0,1-941.0/1080.0),relative = true).also {
            it.visible = false

        }
        val exitUp = punImage("exit_up",resourcesVfs["UI/exit-normal.png"].readBitmap(),Rectangle(1204.0/1920.0,1524.0/1920.0,1-834.0/1080.0,1-941.0/1080.0),relative = true).also { exit->
            exit.onDown {
                exit.visible = false
                exitDown.visible=true
                println("exit down")
            }

            exit.onClick {
                println("exit clicked")
            }
        }

        this.onUp {
            if(exitDown.visible){
                exitUp.visible=true
                exitDown.visible=false
                views.gameWindow.close()
            }

            if(settingsDown.visible){
                settingsUp.visible=true
                settingsDown.visible=false
                // settings event
            }

            if(scoreDown.visible){
                scoreUp.visible=true
                scoreDown.visible=false
                // score event
            }

            if(playDown.visible){
                playUp.visible=true
                playDown.visible=false
                SfxPlayer.playSfx("carDoorStartUp-16.mp3")
                launchImmediately{sceneContainer.changeTo<WindowScene>()}
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

        addUpdater {dt->
            outside1.x -= dt.seconds * 0.3 * 1920
            outside2.x -= dt.seconds * 0.3 * 1920
            if (outside1.x + outside1.width < -1000.0) {
                outside1.x += outside1.width * 2
            }
            if (outside2.x + outside2.width < -1000.0) {
                outside2.x += outside2.width * 2
            }
        }







        super.sceneAfterInit()
    }

    var outside1: Puntainer = Puntainer()
    var outside2: Puntainer = Puntainer()
    var window: Puntainer = Puntainer()



    // delete from all under here for a new scene

    suspend fun openingCrawl() {
        val bg = solidRect("id", Rectangle(0.0, 1.0, 0.0, 1.0), RGBA.float(0.04f, 0.02f, 0.04f, 1f), relative = true)

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
        punImage("id", resource, Rectangle(0.0, 1.0, 0.0, 1.0), true).also {
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