package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.onDown
import com.soywiz.korge.input.onUp
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.text
import com.soywiz.korim.font.TtfFont
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import modules.basic.Colour
import pungine.Button
import pungine.PunScene
import pungine.Puntainer
import pungine.geometry2D.Rectangle

/** This scene is the template for a PunGineIV game
 *
 */

@KorgeInternal
@DelicateCoroutinesApi
class EntryScene : PunScene() {
    override suspend fun Container.sceneInit() {
        GlobalAccess.init()
        if (GlobalAccess.soundsAreOn) {
            MusicPlayer.play("musicbox.mp3")
        }


        val outsiders = Outside()
        outsiders.deploy(addFunction = { p: Puntainer, r: Rectangle ->
            scenePuntainer.addPuntainer(p, r)
        })




        val glass = scenePuntainer.punImage(
            "id",
            Rectangle(0.0, 1.0, 0.0, 1.0),
            resourcesVfs["UI/glass-up.png"].readBitmap()
        ).also {
            it.alpha = 0.8
        }

        val name = scenePuntainer.punImage(
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


        val thereAreScores = GlobalAccess.scoreKeeper.scores.isNotEmpty()
        val scoreTextText = if(thereAreScores){
            "Top Score: "+GlobalAccess.scoreKeeper.scores.maxOrNull()!!.toInt().toString()
        }else{
            "It's too sunny outside to care about scores."
        }

        val font = TtfFont(resourcesVfs["MPLUSRounded1c-Medium.ttf"].readAll())
        val scoreText = text(
            scoreTextText,
            font = font,
            textSize = 48.0,
            color = Colour.rgba(0.1,0.0,0.0).korgeColor,
            alignment = TextAlignment.TOP_CENTER
        ).also {
            it.x = GlobalAccess.windowSize.width*0.5
            it.y = GlobalAccess.windowSize.height*0.5
            it.visible = false
        }




        Button("play",resourcesVfs["UI/play-normal.png"].readBitmap(),resourcesVfs["UI/play-pushed.png"].readBitmap()).also {
            it.clickFunction = {
                launchImmediately {
                    SfxPlayer.playSfx("carDoorStartUp-16.mp3")
                    sceneContainer.changeTo<WindowScene>()
                }
            }
            scenePuntainer.addPuntainer(it,Rectangle(232.0 / 1920.0, 557.0 / 1920.0, 1 - 864.0 / 1080.0, 1 - 971.0 / 1080.0),relative = true)
        }


        Button("score",resourcesVfs["UI/score-normal.png"].readBitmap(),resourcesVfs["UI/score-pushed.png"].readBitmap()).also {
            it.clickFunction = {
                //TODO score event
                scenePuntainer.puntainers.filterIsInstance<Button>().forEach {
                    it.visible=false
                }
                scoreText.visible=true
                if(thereAreScores.not()){
                    glass.tint = Colour.byHex("F9D71C").korgeColor
                    name.tint = Colour.byHex("F9D71C").korgeColor
                }else{
                    glass.tint = Colour.rgba(0.5,0.0,0.05).korgeColor
                    name.tint = Colour.rgba(0.5,0.0,0.05).korgeColor
                }

            }
            scenePuntainer.addPuntainer(it,Rectangle(556.0 / 1920.0, 876.0 / 1920.0, 1 - 854.0 / 1080.0, 1 - 961.0 / 1080.0),relative = true)
        }

        Button("credits",resourcesVfs["UI/credits-normal.png"].readBitmap(),resourcesVfs["UI/credits-pushed.png"].readBitmap()).also {
            it.clickFunction = {
                credits.visible=true
                scenePuntainer.puntainers.filterIsInstance<Button>().forEach {
                    it.visible=false
                }


                //TODO score event

            }
            scenePuntainer.addPuntainer(it,Rectangle(880.0 / 1920.0, 1200.0 / 1920.0, 1 - 844.0 / 1080.0, 1 - 951.0 / 1080.0),relative = true)
        }

        Button("score",resourcesVfs["UI/exit-normal.png"].readBitmap(),resourcesVfs["UI/exit-pushed.png"].readBitmap()).also {
            it.clickFunction = {
                sceneContainer.views.gameWindow.close()
            }
            scenePuntainer.addPuntainer(it,Rectangle(1204.0 / 1920.0, 1524.0 / 1920.0, 1 - 834.0 / 1080.0, 1 - 941.0 / 1080.0),relative = true)

        }

        onClick {
            if(credits.visible || scoreText.visible){
                credits.visible=false
                scoreText.visible=false
                scenePuntainer.puntainers.filterIsInstance<Button>().forEach {
                    it.visible=true
                }
                glass.tint = Colour.WHITE.korgeColor
                name.tint = Colour.WHITE.korgeColor
            }
        }


        addUpdater { dt ->
            outsiders.update(dt.seconds * 0.3 * 1920)

        }


        if(GlobalAccess.entrySceneFirstCalled.not()){
            openingCrawl()
            GlobalAccess.entrySceneFirstCalled=true
        }







        super.sceneAfterInit()
    }

    var credits: Puntainer = Puntainer()


    // delete from all under here for a new scene

    suspend fun openingCrawl() {

        val bg = scenePuntainer.solidRect("id", Rectangle(0.0, 1.0, 0.0, 1.0), Colour.rgba(0.04, 0.02, 0.04, 1.0))


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
                }
            }
        }
    }
}