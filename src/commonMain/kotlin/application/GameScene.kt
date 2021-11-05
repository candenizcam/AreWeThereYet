package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.input.onClick
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.moveBy
import com.soywiz.korge.view.tween.moveTo
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Angle
import modules.basic.Colour
import pungine.PunScene
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.oneRectangle
import pungine.singleColour

/** This scene is the template for a PunGineIV game
 *
 */

class GameScene: PunScene() {
    override fun createSceneView(): Container = Puntainer()

    override suspend fun Container.sceneInit(): Unit{
        val h = GlobalAccess.virtualSize.height.toDouble()
        val w = GlobalAccess.virtualSize.width.toDouble()

        puntainer("floor", Rectangle(0.0,1.0,0.0,FloorData.getHeight()),relative = true) {
            it.singleColour(Colour.GREEN.korgeColor)
        }




        hitbox = puntainer("hitbox", Rectangle(200.0,250.0,FloorData.getHeight()*h,FloorData.getHeight()*h+50.0)) {
            it.singleColour(Colour.BLUE.korgeColor)
        }



        obstacles.add(puntainer("obs", Rectangle(w,w+50.0,FloorData.getHeight()*GlobalAccess.virtualSize.height,FloorData.getHeight()*GlobalAccess.virtualSize.height+50.0)) {
            it.singleColour(Colour.RED.korgeColor)


        })

        var s = 0.0

        this.addUpdater {dt->

            obstacles.forEach {
                it.x = it.x - dt.milliseconds*0.2
            }

            if(views.input.keys.justPressed(Key.SPACE)){
                hitboxDy = 300.0
            }


            hitbox.yConv += hitboxDy*dt.seconds
            //hitbox.yConv -= 50.0*dt.seconds

            if(hitbox.yConv-hitbox.virtualRectangle.height<(h*FloorData.getHeight())){
                hitbox.children.fastForEach {
                    it.alpha = 0.5
                }
                hitbox.yConv = h*(FloorData.getHeight())+hitbox.virtualRectangle.height
                //hitbox.yConv = 0.0
            }else{
                hitbox.children.fastForEach {
                    it.alpha = 1.0
                }
            }
            hitboxDy = hitboxDy - gravity*dt.seconds



        }



        super.sceneAfterInit()
    }

    var hitbox: Puntainer = Puntainer()

    var obstacles = mutableListOf<Puntainer>()

    val gravity = 200.0
    var hitboxDy =0.0


    object FloorData{
        val ratedY = 0.3

        fun getHeight(x: Double?=null): Double {
            return if(x!=null){
                ratedY
            }else{
                ratedY
            }
        }
    }


}