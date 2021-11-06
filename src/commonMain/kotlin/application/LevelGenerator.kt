package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korma.math.roundDecimalPlaces
import kotlin.random.Random

class LevelGenerator {

    var obstacles = mutableListOf<Obstacle>()
    var obstacleDistance = 0.0
    private var toNext = 1.0
    var nextFloor = 0.3
    private var nextCeil = 1.0
    var speed = 00.3
    val maxSpeed = 1.0
    var acceleration = 1.0001
    var lastGenerated = 0
    var nowGenerated = 0

    fun generate(){0
        nowGenerated=(0..5).random()
        when(nowGenerated) {
            0 -> obstacles.add(Obstacle(ObstacleTypes.LOWJUMP, 1.05, 100.0/840, 200.0/840, 124.0/1920))
            1 -> obstacles.add(Obstacle(ObstacleTypes.HIGHJUMP, 1.05, 170.0/840, 340.0/840, 248.0/1920))
            2 -> obstacles.add(Obstacle(ObstacleTypes.DUCK, 1.05, 1.0-350.0/840, 700.0/840, 124.0/1920))
            3 -> obstacles.add(Obstacle(ObstacleTypes.LONGJUMP, 1.05, 100.0/840, 200.0/840, 248.0/1920))
            4 -> obstacles.add(Obstacle(ObstacleTypes.DONTJUMP, 1.05, 1.0-170.0/840, 340.0/840, 124.0/1920))
            5 -> obstacles.add(Obstacle(ObstacleTypes.JUMPDUCT, 1.05, 240.0/840, 200.0/840, 124.0/1920))
        }
        lastGenerated=nowGenerated
    }

    fun update(dt: TimeSpan){
        if(speed < maxSpeed){
            speed *= (acceleration)
        }

        obstacles.forEach {
            it.centerX -= dt.seconds*speed
        }

        obstacles.removeAll { obstacle -> obstacle.centerX< -0.05 }

        obstacleDistance += dt.seconds*speed

        if (obstacleDistance >= toNext && obstacles.size<5){
            obstacleDistance = 0.0
            generate()

            val seed = Random.nextDouble()
            seed.roundDecimalPlaces(2)

            toNext = (nextCeil - nextFloor) * seed + nextFloor
        }
    }
}