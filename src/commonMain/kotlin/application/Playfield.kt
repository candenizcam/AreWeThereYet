package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Key
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.position
import com.soywiz.korge.view.solidRect
import com.soywiz.korge.view.views
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
            jumping=false
        }
        gravity = if(hitboxSpeed<=0){
            -4.0
        } else{
            -1.5
        }
        hitboxSpeed += gravity*dt.seconds
        level.update(dt)

        ducking = (ducking-dt.seconds).coerceAtLeast(0.0)

    }

    fun collisionCheck(): Boolean {

        val relevantRect = if(ducking>0){hitboxRect.split(2,1)[2,1]} else{hitboxRect}
        return level.obstacles.any { relevantRect.collides(it.rectangle) }

    }

    val level = LevelGenerator()

    /**
     *  Jumpingu Jumpingu everybody
     */
    fun jump(){
        jumping=true
        if(jumpCount==0){

            ducking = 0.0
            hitboxSpeed = 1.2
            jumpCount+=1

        } else if(jumpCount==1){

            ducking = 0.0
            hitboxSpeed = 0.8
            jumpCount+=1

        }

    }

    /** Change here for ducking duration
     *
     */
    fun duck(){
        ducking = 1.0
        if(jumpCount!=0){
            hitboxSpeed = -2.0
            jumpCount = 2
            ducking=0.0
        }
    }

    fun stopDuck(){
        if (ducking!=0.0 && !jumping){
            ducking=0.0
        }
    }
//
    var hitboxRestRect = Rectangle(281.0/1920,281.0/1920 + 124.0/1920,0.0,200.0/840)
    var hitboxRect = Rectangle(281.0/1920,281.0/1920 + 124.0/1920,0.0,200.0/840)
    var gravity = -1.5
    var hitboxSpeed = 0.0
    var ducking = 0.0
    var jumpCount = 0
    var jumping = false
}