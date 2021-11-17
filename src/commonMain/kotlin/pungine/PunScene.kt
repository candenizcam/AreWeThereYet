package pungine

import application.GlobalAccess
import com.soywiz.klogger.AnsiEscape
import com.soywiz.korge.animate.AnLibrary
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.moveTo
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korio.file.std.resourcesVfs

import com.soywiz.korio.resources.Resources
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle

open class PunScene(var width: Double = InternalGlobalAccess.virtualSize.width.toDouble(), var height: Double=InternalGlobalAccess.virtualSize.height.toDouble()): Scene() {
    override fun createSceneView(): Container = Puntainer()

    override suspend fun Container.sceneInit(): Unit {
        // Override this for stuff
    }

    val centre: Vector
        get() {
            return Vector(width*0.5,height*0.5)
        }

    val thisRectangle: Rectangle
        get() {
            return Rectangle(this.centre,width,height)
        }


    /** This is the basic block generator for UI, it can be used directly or as a frame for smaller ui elements
     * id is the id for the puntainer which can be used to find in a search, it can be omitted if this is not required
     * rectangle is for the placement, if it is relative it is relative to its mother, if not values are taken as pixels of screen (not recommended)
     * call func is for any other functions to be executed while this is, callFunc is often used to place the children of this puntainer by its grid
     */
    protected suspend fun puntainer(id: String?=null, rectangle: Rectangle?=null,relative: Boolean=false,callFunc: (it: Puntainer)->Any): Puntainer {
        val relative = if (relative){
            rectangle?: oneRectangle()
        }else{
            thisRectangle.toRated(rectangle?: thisRectangle)
        }
        return Puntainer(id,relative).also {
            callFunc(it)
            this.sceneView.addChild(it)
            it.fitToFrame(thisRectangle)
        }
    }


    /** This is a puntainer for holding images, it is a child of regular puntainer with an input for bitmap
     */
    suspend fun punImage(id: String?=null, bitmap: Bitmap, rectangle: Rectangle?=null,relative: Boolean=false,callFunc: (it: PunImage)->Any={}): PunImage{
        val relative = if (relative){
            rectangle?: oneRectangle()
        }else{
            thisRectangle.toRated(rectangle?: thisRectangle)
        }
        return PunImage(id,bitmap,relative).also {
            callFunc(it)
            this.sceneView.addChild(it)
            it.fitToFrame(thisRectangle)
        }
    }

    /** This is a solidRect overload that takes rectangle as input instead of KorGE coordinates and handles transforms
     *
     */
    protected suspend fun solidRect(id: String?=null, rectangle: Rectangle, colour: RGBA=Colors.WHITE, relative: Boolean=false): Puntainer {
        return puntainer(id, rectangle=rectangle,relative = relative) {
            val r = it.sizeRectangle(thisRectangle)
            it.solidRect(100.0,100.0,color = colour)
            it.size(r.width*0.5,r.height*0.5)
            it.position(r.left, thisRectangle.top - r.top)
        }
    }

    protected suspend fun roundRect(id: String?=null, rectangle: Rectangle, rX: Double, rY: Double, colour: RGBA=Colors.WHITE, relative: Boolean=false): Puntainer {
        return puntainer(id, rectangle = rectangle,relative = relative) {
            val r = it.sizeRectangle(thisRectangle)
            it.roundRect(r.width, r.height, rX, rY, colour)
            it.position(r.left, thisRectangle.top - r.top)
        }
    }





}