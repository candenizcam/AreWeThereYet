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

    @OptIn(KorgeInternal::class)

    override suspend fun Container.sceneInit(){
        //openingCrawl()
        val engineLoop = resourcesVfs["SFX/engine_heavy_loop-20.mp3"].readMusic()

        val bmp = resourcesVfs["environment/Bg_Small.png"].readBitmap()
        outsiders.add(punImage("o1",bmp.clone(),Rectangle(0.0, 960.0, 0.0, 1080.0)))
        outsiders.add(punImage("o2",bmp.clone(),Rectangle(960.0, 2*960.0, 0.0, 1080.0)))
        outsiders.add(punImage("o3",bmp,Rectangle(960.0*2, 3*960.0, 0.0, 1080.0)))

        window = punImage(
            "id",
            resourcesVfs["UI/Glass-tutorial.png"].readBitmap(),
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
            if(freeze.not()){
                outsiders.forEach {
                    it.x -= dt.seconds*0.3*1920
                    if(it.x + it.width< -20.0){
                        it.x += it.width * 3
                    }
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
                    window.yConv=( window.yConv-dt.seconds*0.3*GlobalAccess.virtualSize.height).coerceAtLeast(-10.0)
                }else if (windowUp) {
                    window.yConv= (window.yConv+dt.seconds*0.3*GlobalAccess.virtualSize.height).coerceAtMost(GlobalAccess.virtualSize.height.toDouble())
                }
                //println(window.yConv)
                if(window.yConv<100.0){
                    freeze=true
                    GlobalScope.launch{ sceneContainer.changeTo<GameScene>( ) }
                }
            }


        }
        engineLoop.play(PlaybackParameters(PlaybackTimes.INFINITE, volume = 1.0))
        super.sceneAfterInit()
        println("window called")
    }

    var freeze = false

    var outside1: Puntainer = Puntainer()
    var outside2: Puntainer = Puntainer()
    val outsiders = mutableListOf<Puntainer>()
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