package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.position
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import kotlin.coroutines.CoroutineContext

class Playfield(id: String? = null, relativeRectangle: Rectangle) : Puntainer(id, relativeRectangle) {

    fun update(dt: TimeSpan) {
        if (jumpDelay > 0) {
            jumpDelay -= dt.seconds
            if (jumpDelay < 0) {
                hitboxSpeed = 1.2
                jumpCount += 1
            }

        }

        altHitboxRect = altHitboxRect.moved(0.0, hitboxSpeed * dt.seconds)
        if (altHitboxRect.bottom < 0) {
            altHitboxRect = hitboxRestRect
            jumpCount = 0
            jumping = jumpDelay > 0
        }

        gravity = if (hitboxSpeed <= 0) {
            -4.0
        } else {
            -1.5
        }
        hitboxSpeed += gravity * dt.seconds
        level.update(dt)

        ducking = (ducking - dt.seconds).coerceAtLeast(0.0)

        immune = (immune-dt.seconds).coerceAtLeast(0.0)

    }

    var immune = 0.0

    fun sliced(){
        immune = 1.0
    }

    fun collisionCheck(): Boolean {
        return if (immune==0.0){
            level.obstacles.any { hitboxRect.collides(it.rectangle) }
        }else{
            false
        }



    }


    val level = LevelGenerator()

    /**
     *  Jumpingu Jumpingu everybody
     */
    fun jump() {
        jumping = true
        if (jumpCount == 0) {
            jumpDelay = 0.2
            ducking = 0.0

        } else if (jumpCount == 1) {

            ducking = 0.0
            hitboxSpeed = 0.8
            jumpCount += 1

        }

    }

    /** Change here for ducking duration
     *
     */
    fun duck() {
        ducking = 1.0
        if (jumpCount != 0) {
            hitboxSpeed = -2.0
            jumpCount = 2
            ducking = 0.0
        }
    }

    fun stopDuck() {
        if (ducking != 0.0 && !jumping) {
            ducking = 0.0
        }
    }

    //
    var hitboxRestRect = Rectangle(281.0 / 1920, 281.0 / 1920 + 124.0 / 1920, 0.0, 200.0 / 840)
    var altHitboxRect = Rectangle(281.0 / 1920, 281.0 / 1920 + 124.0 / 1920, 0.0, 200.0 / 840)
    val hitboxRect: Rectangle
        get() {
            return if (ducking > 0) {
                altHitboxRect.split(2, 1)[2, 1]
            } else {
                altHitboxRect
            }
        }
    var gravity = -1.5
    var hitboxSpeed = 0.0
    var ducking = 0.0
    var jumpDelay = 0.0
    var jumpCount = 0
    var jumping = false

    init {
        this.position(x, y)
    }
}