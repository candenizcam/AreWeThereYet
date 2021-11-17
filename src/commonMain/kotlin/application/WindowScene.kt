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
import com.soywiz.korge.view.addFixedUpdater
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.async
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
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

    override suspend fun Container.sceneMain(){
        //openingCrawl()
        //val engineLoop = resourcesVfs["SFX/engine_heavy_loop-20.mp3"].readMusic()
        //val t = resourcesVfs["music/musicbox.mp3"].readMusic()

        MusicPlayer.play("musicbox.mp3")

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

        //val gameScene = GameScene()
      //  this.addChild(gameScene.sceneView)
      //  sceneContainer.
        addUpdater {dt->
            if(freeze.not()){
                outsiders.forEach {
                    it.x -= dt.seconds*0.3*1920
                    if(it.x + it.width< -20.0){
                        it.x += it.width * 3
                    }
                }

                if (views.input.keys.justPressed((Key.DOWN))){
                    launchImmediately { SfxPlayer.playSfx("windowDown-4.mp3") }
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
                if(window.yConv<100.0){
                    launchImmediately {sceneContainer.changeTo<GameScene>()   }
                }
            }
        }


       // engineLoop.play(PlaybackParameters(PlaybackTimes.INFINITE, volume = 1.0))
       // t.play(PlaybackParameters(PlaybackTimes.INFINITE, volume = 1.0))
       // launchImmediately { MusicPlayer.play("musicbox.mp3") }
        super.sceneAfterInit()
        println("window called")
    }

    var freeze = false

    val outsiders = mutableListOf<Puntainer>()
    var window: Puntainer = Puntainer()


}