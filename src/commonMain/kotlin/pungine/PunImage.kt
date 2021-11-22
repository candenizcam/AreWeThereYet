package pungine

import com.soywiz.korge.view.Image
import com.soywiz.korge.view.anchor
import com.soywiz.korge.view.position
import com.soywiz.korim.bitmap.Bitmap
import pungine.geometry2D.Rectangle

/** This class handles simple image and it takes bitmap as an input,
 * It does not inherit Image directly, but it generates an Image into the puntainer, which it Inherits
 *
 */
open class PunImage: Puntainer{
    constructor(id: String?=null,bitmap: Bitmap): super(id){
        image = Image(bitmap)
        this.addChild(image)
    }


    override fun reshape(r: Rectangle, withChildren: Boolean) {
        super.reshape(r, withChildren)
        //image.scaleX = image.scaleX*r.width/image.globalBounds.width
        //image.scaleY = image.scaleY*r.height/image.globalBounds.height
    }


    private val image: Image
}