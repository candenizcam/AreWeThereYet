package application

import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.GlobalScope
import pungine.PunScene
import pungine.Puntainer
import pungine.geometry2D.Rectangle

class GameOverScene: PunScene() {
    override fun createSceneView(): Container = Puntainer()

    override suspend fun Container.sceneMain(){
        val gameOver = Puntainer("gameover", Rectangle(0.0,1.0,0.0,1.0)).also { pt->
            this.addChild(pt)
            for(i in 1..7){
                Image(resourcesVfs["VFX/game_over-$i.png"].readBitmap()).also {
                    it.visible = false
                    pt.addChild(it)
                }
            }

        }

        addUpdater { dt->


            animIndex+=dt.seconds*2
            if(animIndex>=gameOver.size){
                launchImmediately { sceneContainer.changeTo<EntryScene>( ) }
            }else{
                gameOver.children.fastForEach { it.visible=false }
                gameOver.children[animIndex.toInt()].visible=true
            }

        }


    }

    var animIndex=0.0
}