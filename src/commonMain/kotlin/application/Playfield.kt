package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.position
import pungine.Puntainer
import pungine.geometry2D.Rectangle

class Playfield: Puntainer {
    constructor(id: String?=null, relativeRectangle: Rectangle) : super(id,relativeRectangle){
        this.position(x,y)


    }

    fun update(dt: TimeSpan){
        _hitboxRect = _hitboxRect.moved(0.0,hitboxSpeed*dt.seconds)
        if(_hitboxRect.bottom<0){
            _hitboxRect = hitboxRestRect
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

        return level.obstacles.any { hitboxRect.collides(it.rectangle) }

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
        }
    }

    fun stopDuck(){
        if (ducking!=0.0 && !jumping){
            ducking=0.0
        }
    }
//
    var hitboxRestRect = Rectangle(281.0/1920,281.0/1920 + 124.0/1920,0.0,200.0/840)
    var _hitboxRect = Rectangle(281.0/1920,281.0/1920 + 124.0/1920,0.0,200.0/840)
    val hitboxRect: Rectangle
    get() {
        return if(ducking>0){_hitboxRect.split(2,1)[2,1]} else{_hitboxRect}
    }
    var gravity = -1.5
    var hitboxSpeed = 0.0
    var ducking = 0.0
    var jumpCount = 0
    var jumping = false
}