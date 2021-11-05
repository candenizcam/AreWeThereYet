package pungine

import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.ViewFileRef
import com.soywiz.korge.view.position
import com.soywiz.korge.view.renderToBitmap
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.BmpSlice
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.bitmap.sliceWithSize
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.format.writeTo
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.resources.Resourceable
import com.soywiz.korma.geom.vector.VectorPath
import modules.basic.Colour
import pungine.geometry2D.Rectangle


class PunImage: Puntainer{
    constructor(id: String?=null,bitmap: Bitmap): super(id){
        this.addChild(Image(bitmap))
        bitmapDisplayed = bitmap
        bitmapSource = bitmap.clone()
    }

    constructor(id: String?=null,bitmap: Bitmap, relativeRectangle: Rectangle) : super(id,relativeRectangle){
        this.position(x,y)
        this.addChild(Image(bitmap))
        bitmapDisplayed = bitmap
        bitmapSource = bitmap.clone()
    }

    var bitmapSource: Bitmap // this is the original form of the bitmap
        private set

    var bitmapDisplayed: Bitmap // this is the altered form of the bitmap
        private set


    val widthIndex: IntRange
        get() {
            return (0 until this.bitmapDisplayed.width)
        }

    val heightIndex: IntRange
        get() {
            return (0 until this.bitmapDisplayed.height)
        }


    /** This is the kind of function that i'd hug & never let go
     * it takes a function as an input which takes two ints, which are i and j indices of pixels and runs the function for each pixel
     */
    fun applyToPixels(f: (Int, Int)->Any){
        for(i in widthIndex){
            for(j in heightIndex){
                f(i,j)
            }
        }
    }

    fun setBitmap(bitmap: Bitmap){
        bitmapSource = bitmap
        this.bitmapDisplayed = bitmap
    }

    fun resetBitmap(){
        this.bitmapDisplayed = bitmapSource.clone()
        applyModifications()
    }

    /** Upon modifying image, this function is called to change the displayed image
     */
    @OptIn(KorgeInternal::class)
    fun applyModifications(){
        (this.children[0] as Image).bitmap = this.bitmapDisplayed.clone().slice()
    }


    fun getPixel(x: Int, y: Int): Colour {
        return Colour.byKorgeRGBA(bitmapDisplayed.getRgbaClamped(x,y))
    }

    fun setPixel(x:Int,y:Int,rgba: RGBA){
        bitmapDisplayed.setRgba(x,y,rgba)

    }

    fun setPixel(x:Int, y: Int, colour: Colour){
        bitmapDisplayed.setRgba(x,y,colour.korgeColor)
    }


}


/*
/** This is the view class to handle single images that are loaded from files
 * at one point we can make a version for bitmap
 */
class PunImage(bitmap: Resourceable<out BmpSlice>,
               anchorX: Double = 0.0,
               anchorY: Double = anchorX,
               hitShape: VectorPath? = null,
               smoothing: Boolean = true
) : BaseImage(bitmap, anchorX, anchorY, hitShape, smoothing), ViewFileRef by ViewFileRef.Mixin(){
    constructor(
        bitmap: Bitmap,
        anchorX: Double = 0.0,
        anchorY: Double = anchorX,
        hitShape: VectorPath? = null,
        smoothing: Boolean = true
    ) : this(bitmap.slice(), anchorX, anchorY, hitShape, smoothing)

    val widthIndex: IntRange
        get() {
            return (0 until this.bitmap.bmp.width)
        }

    val heightIndex: IntRange
        get() {
            return (0 until this.bitmap.bmp.height)
        }






    fun getPixel(x: Int, y: Int): Colour {
        return Colour.byKorgeRGBA(bitmap.bmp.getRgbaClamped(x,y))
    }

    fun setPixel(x:Int,y:Int,rgba: RGBA){
        bitmap.bmp.setRgba(x,y,rgba)
    }

    fun setPixel(x:Int, y: Int, colour: Colour){
        bitmap.bmpBase.setRgba(x,y,colour.korgeColor)
        bitmap.bmp.setRgba(x,y,colour.korgeColor)
        baseBitmap.bmp.setRgba(x,y,colour.korgeColor)
        baseBitmap.bmpBase.setRgba(x,y,colour.korgeColor)


    }


}

 */