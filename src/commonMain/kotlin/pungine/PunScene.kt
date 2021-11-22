package pungine

import application.GlobalAccess
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA

import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle

open class PunScene(var width: Double = InternalGlobalAccess.virtualSize.width.toDouble(), var height: Double=InternalGlobalAccess.virtualSize.height.toDouble()): Scene() {
    override fun createSceneView(): Puntainer = Puntainer().also {
        scenePuntainer = it
    }
    lateinit var scenePuntainer: Puntainer // this can be referred as the scene puntainer directly


    val centre: Vector
        get() {
            return Vector(width*0.5,height*0.5)
        }


    val thisRectangle: Rectangle
        get() {
            return Rectangle(this.centre,width,height)
        }



    fun getPuntainers(): List<Puntainer> {
        return (sceneView as Puntainer).puntainers.toList()
    }


    fun addPuntainer(p: Puntainer, rectangle: Rectangle=thisRectangle, relative: Boolean=false){
        (sceneView as Puntainer).addPuntainer(p,rectangle,relative)
    }
}