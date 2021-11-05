package pungine

import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import pungine.geometry2D.Rectangle
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

    var virtualRectangle: Rectangle = Rectangle(0.0,100.0,0.0,100.0)

    fun sizeRectangle(frameRectangle: Rectangle):Rectangle = frameRectangle.fromRated(relativeRectangle)


    /** This method should be called with the frame rectangle (that it should fit) and it handles all of its children
     *
     */
    fun fitToFrame(frameRectangle: Rectangle){
        val s = sizeRectangle(frameRectangle)
        this.virtualRectangle = sizeRectangle(frameRectangle)
        var childrenThatMatters = 0
        forEachChild {
            if(it is Puntainer){
                it.fitToFrame(s)
                childrenThatMatters+=1
            }
        }
        if(childrenThatMatters==0){
            this.size(s.width,s.height)
            this.position(s.left, InternalGlobalAccess.virtualSize.height - s.top)
            //this.position(s.left,0.0 - s.top)
        }
    }


    var yConv: Double = 0.0
        get(){
            return InternalGlobalAccess.virtualSize.height.toDouble()-super.y
        }
        set(value) {
            field = value
            super.y = InternalGlobalAccess.virtualSize.height.toDouble()-value
        }


    fun singleColour(colour: RGBA =Colors.WHITE): Puntainer {
        solidRect(virtualRectangle.width,virtualRectangle.height,colour)
        return this
    }

    fun relativePuntainer(id: String?=null,relativeRectangle: Rectangle, callFunc: (it: Puntainer)->Any){
        Puntainer(id, relativeRectangle).also {
            callFunc(it)
            this.addChild(it)
        }
    }

}


fun singleColour(id: String?=null, relativeRectangle: Rectangle, colour: RGBA=Colors.WHITE): Puntainer = Puntainer(id,relativeRectangle).singleColour(colour)
