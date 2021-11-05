package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.position
import com.soywiz.korge.view.solidRect
import com.soywiz.korim.bitmap.Bitmap
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector

class Playfield: Puntainer {
    constructor(id: String?=null, relativeRectangle: Rectangle) : super(id,relativeRectangle){
        this.position(x,y)


    }

    fun update(dt: TimeSpan){
        hitboxRect = hitboxRect.moved(0.0,hitboxSpeed*dt.seconds)
        if(hitboxRect.bottom<0){
            hitboxRect = hitboxRestRect
            jumpCount=0
        }
        hitboxSpeed += gravity
        level.update(dt)

        ducking = (ducking-dt.seconds).coerceAtLeast(0.0)

    }

    fun collisionCheck(): Boolean {

        return level.obstacles.filter { hitboxRect.collides(Rectangle(Vector(it.centerX,it.centerY),it.width,it.height)) }.isNotEmpty()

    }

    val level = LevelGenerator()

    /**
     *  Jumpingu Jumpingu everybody
     */
    fun jump(){
        if(jumpCount<2){

            if(ducking==0.0){
                hitboxSpeed = 1.5
                jumpCount+=1
            }
        }

    }

    /** Change here for ducking duration
     *
     */
    fun duck(){
        if(jumpCount==0){
            ducking = 0.5
        }

    }

    var hitboxRestRect = Rectangle(0.3,0.35,0.0,0.3)
    var hitboxRect = Rectangle(0.3,0.35,0.0,0.1)
    var gravity = -0.015
    var hitboxSpeed = 0.0
    var ducking = 0.0
    var jumpCount = 0
}