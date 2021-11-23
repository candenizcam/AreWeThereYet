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

    var windowDown = false
    var windowUp = false

    @OptIn(KorgeInternal::class)
    override suspend fun Container.sceneMain(){
        //openingCrawl()
        //val engineLoop = resourcesVfs["SFX/engine_heavy_loop-20.mp3"].readMusic()
        //val t = resourcesVfs["music/musicbox.mp3"].readMusic()
        if(GlobalAccess.soundsAreOn){
           GlobalAccess.musicPlayer.play("musicbox.mp3")
        }

        //val bmp = resourcesVfs["environment/Bg_Small.png"].readBitmap()
        val outside = Outside()
        outside.deploy(addFunction = {p: Puntainer, r: Rectangle->
            scenePuntainer.addPuntainer(p,r)
        })

        window = scenePuntainer.punImage(
            "id",
            Rectangle(0.0,1.0,0.0,1.0),
            resourcesVfs["UI/Glass-tutorial.png"].readBitmap()
        ).also {
            it.alpha=1.0
        }

        scenePuntainer.punImage(
            "id",
            Rectangle(0.0,1.0,0.0,1.0),
            resourcesVfs["UI/Wintop.png"].readBitmap()
        )

        scenePuntainer.punImage(
            "id",
            Rectangle(0.0,1.0,0.0,1.0),
            resourcesVfs["UI/Windown.png"].readBitmap()
        )

        addUpdater {dt->
            if(freeze.not()){

                outside.update(dt.seconds*0.3*1920)

                if (views.input.keys.justPressed((Key.DOWN))){
                    launchImmediately { SfxPlayer.playSfx("windowDown-4.mp3") }
                }

                if (views.input.keys.pressing(Key.DOWN)) {
                    window.yConv-=(dt.seconds*0.3*GlobalAccess.virtualSize.height).coerceAtLeast(0.0)
                }else if(views.input.keys.pressing(Key.UP)){
                    window.yConv+= (dt.seconds*0.3*GlobalAccess.virtualSize.height).coerceAtMost(GlobalAccess.virtualSize.height.toDouble())
                }



                if (views.input.keys.justPressed(Key.DOWN) || views.input.keys.justPressed(Key.S)) {
                    windowUp = false
                    windowDown = true
                } else if (views.input.keys.justPressed(Key.UP) || views.input.keys.justPressed(Key.W)) {
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
    }

    var freeze = false
    var window: Puntainer = Puntainer()
}