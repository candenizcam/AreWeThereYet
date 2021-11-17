package pungine

import application.GlobalAccess
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import modules.basic.Colour
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle

/** This is the one size fits all container for pungine, any specialized views should be inherited from this first, and then implemented as view children
 *
 */
open class Puntainer: Container {
    constructor(id: String?=null) : super(){
        this.id = id
    }

    /** The rectangle in this function is relative to its surroundings and is used for later reference
     *
     */
    constructor(id: String?=null, relativeRectangle: Rectangle) : super(){
        this.id = id
        this.position(x,y)
        this.relativeRectangle = relativeRectangle

    }

    val id: String? //in this version, id is optional, it is null unless user wants to find it

    var relativeRectangle = oneRectangle()
        private set

    var virtualRectangle: Rectangle = InternalGlobalAccess.virtualRect



    fun sizeRectangle(frameRectangle: Rectangle):Rectangle = frameRectangle.fromRated(relativeRectangle)

    /** This method takes a rectangle (of pixels) as input and sets the size of this puntainer
     */
    open fun reshape(r: Rectangle, withChildren: Boolean=true){
        virtualRectangle = r
        scaleX = scaleX*r.width/globalBounds.width
        scaleY = scaleY*r.height/globalBounds.height
        x = x-globalBounds.x+r.left
        y = y - globalBounds.y + (InternalGlobalAccess.virtualSize.height - r.top)
        if(withChildren){
            puntainers.forEach {
                it.reshape(r.fromRated(it.relativeRectangle),withChildren)
            }
        }
    }

    fun reshape(width: Double?=null, height: Double?=null, centreX: Double?=null, centreY:Double?=null){
        reshape(Rectangle(Vector(centreX?:this.centre.x,centreY?:this.centre.y),width?:this.width,height?:this.height))
    }


    var yConv: Double = 0.0
        get(){
            return InternalGlobalAccess.virtualSize.height.toDouble()-super.y
        }
        set(value) {
            field = value
            super.y = InternalGlobalAccess.virtualSize.height.toDouble()-value
        }

    val centre: Vector
        get() {
            return Vector(this.x+this.width*0.5,this.yConv-this.height*0.5)
        }

    val puntainers= mutableListOf<Puntainer>()


    fun singleColour(colour: RGBA =Colors.WHITE): Puntainer {
        solidRect(virtualRectangle.width,virtualRectangle.height,colour)
        return this
    }



    /** Rect in add Puntainer
     *
     */
    fun addPuntainer(p: Puntainer, rect: Rectangle? = null, relative: Boolean = false){
        puntainers.add(p)
        this.addChild(p)
        val r = if(this.parent is Puntainer){
            this.virtualRectangle
        }else{
            InternalGlobalAccess.virtualRect
        }
        if(rect==null){
            r.fromRated(p.relativeRectangle)
        }else{
            if(relative){
                r.fromRated(rect)
            }else{
                rect
            }

        }.also {
            p.reshape(it)
        }
    }


    // Rest is for putting containers in containers
    fun relativePuntainer(id: String?=null,relativeRectangle: Rectangle, callFunc: (it: Puntainer)->Any): Puntainer {
        return Puntainer(id, relativeRectangle).also {
            callFunc(it)
            addPuntainer(it)
        }
    }

    fun solidRect(id: String?=null,relativeRectangle: Rectangle, colour: Colour =Colour.WHITE): Puntainer {
        return relativePuntainer(id,relativeRectangle){
            it.addChild(SolidRect(10.0, 10.0, colour.korgeColor))
        }
    }

    fun roundRect(id: String?=null, relativeRectangle: Rectangle, rX: Double, rY: Double, colour: Colour =Colour.WHITE): Puntainer{
        return relativePuntainer(id,relativeRectangle){
            it.addChild(RoundRect(10.0, 10.0,rX, rY, colour.korgeColor))
        }
    }

    fun punImage(id: String?=null, relativeRectangle: Rectangle, bitmap: Bitmap): Puntainer{
        return PunImage(id,bitmap).also {
            this.addPuntainer(it,relativeRectangle,relative = true)
        }
    }



}

