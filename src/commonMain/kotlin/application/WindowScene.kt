package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korau.sound.PlaybackParameters
import com.soywiz.korau.sound.PlaybackTimes
import com.soywiz.korau.sound.readMusic
import com.soywiz.korev.Key
import com.soywiz.korge.input.onClick
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.scene.SceneContainer
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import modules.basic.Colour
import pungine.PunScene
import pungine.Puntainer
import pungine.geometry2D.Rectangle

@KorgeInternal
@DelicateCoroutinesApi
class WindowScene  : PunScene() {
    override fun createSceneView(): Container = Puntainer()

    var windowDown = false
    var windowUp = false

    override suspend fun Container.sceneInit(){
        //openingCrawl()
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

        //launchImmediately { gameScene.load() }
        //SceneContainer

        addUpdater {dt->
            outside1.x -= dt.seconds * 0.3 * 1920
            outside2.x -= dt.seconds * 0.3 * 1920
            if (outside1.x + outside1.width < -1000.0) {
                outside1.x += outside1.width * 2
            }
            if (outside2.x + outside2.width < -1000.0) {
                outside2.x += outside2.width * 2
            }

            if (views.input.keys.justPressed((Key.DOWN))){
                SfxPlayer.playSfx("windowDown-4.mp3")
            }

            if (views.input.keys.pressing(Key.DOWN)) {
                window.yConv-=(dt.seconds*0.3*GlobalAccess.virtualSize.height).coerceAtLeast(0.0)
            }else if(views.input.keys.pressing(Key.UP)){
                window.yConv+= (dt.seconds*0.3*GlobalAccess.virtualSize.height).coerceAtMost(GlobalAccess.virtualSize.height.toDouble())
            }



            if (views.input.keys.justPressed(Key.DOWN)) {
                windowUp = false
                windowDown = true
            } else if (views.input.keys.justPressed(Key.UP)) {
                windowDown = false
                windowUp = true
            } else if (views.input.keys.justPressed(Key.SPACE)) {
                windowDown = false
                windowUp = false
            }
            if (windowDown) {
                window.yConv-=(dt.seconds*0.3*GlobalAccess.virtualSize.height).coerceAtLeast(0.0)
            }else if (windowUp) {
                window.yConv+= (dt.seconds*0.3*GlobalAccess.virtualSize.height).coerceAtMost(GlobalAccess.virtualSize.height.toDouble())
            }

            if(window.yConv<0.0){
                launchImmediately{ sceneContainer.changeTo<GameScene>( ) }
            }
        }
        engineLoop.play(PlaybackParameters(PlaybackTimes.INFINITE, volume = 1.0))
        super.sceneAfterInit()
    }

    var outside1: Puntainer = Puntainer()
    var outside2: Puntainer = Puntainer()
    var window: Puntainer = Puntainer()


    //var gameScene = GameScene()

    // delete from all under here for a new scene

    suspend fun openingCrawl() {
        val bg = solidRect("id", Rectangle(0.0, 1.0, 0.0, 1.0), RGBA.float(0.04f, 0.02f, 0.04f, 1f), relative = true)

        val img = punImage(
            "id",
            resourcesVfs["pungo_transparent.png"].readBitmap(),
            Rectangle(390.0, 890.0, 110.0, 610.0)
        ).also {
            it.visible = false
            it.onClick {
                GlobalScope.launch {
                    //sceneContainer.changeTo<GameScene>(gameScene)
                    //sceneContainer = gameScene.sceneContainer
                    //sceneContainer.changeTo<GameScene>()
                    launchImmediately{sceneContainer.changeTo<GameScene>()}
                }
            }
        }


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
                    img.visible = true
                }
            }
        }
    }
}