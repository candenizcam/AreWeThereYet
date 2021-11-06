package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korma.math.roundDecimalPlaces
import kotlin.random.Random

class LevelGenerator {

    var obstacles = mutableListOf<Obstacle>()
    var obstacleDistance = 0.0
    var toNext = 1.0
    var nextFloor = 0.3
    var nextCeil = 1.0
    var speed = 00.3
    var acceleration = 1.0001

    fun generate(){
        when((0..1).random()) {
            0 -> obstacles.add(Obstacle(ObstacleTypes.LOWJUMP, 1.05, 100.0/1080, 200.0/1080, 124.0/1920))
            1 -> obstacles.add(Obstacle(ObstacleTypes.HIGHJUMP, 1.05, 170.0/1080, 340.0/1080, 248.0/1920))
            2 -> obstacles.add(Obstacle(ObstacleTypes.DUCT, 1.05, 1.0-370.0/1080, 740.0/1080, 124.0/1920))
            3 -> obstacles.add(Obstacle(ObstacleTypes.LONGJUMP, 1.05, 100.0/1080, 200.0/1080, 248.0/1920))
            4 -> obstacles.add(Obstacle(ObstacleTypes.DONTJUMP, 1.05, 1.0-170.0/1080, 340.0/1080, 124.0/1920))
            5 -> obstacles.add(Obstacle(ObstacleTypes.JUMPDUCT, 1.05, 240.0/1080, 200.0/1080, 124.0/1920))
        }
    }

    fun update(dt: TimeSpan){
        if(speed < 1.3){
            speed *= (acceleration)
        }

        obstacles.forEach {
            it.centerX -= dt.seconds*speed
        }

        obstacles.removeAll { obstacle -> obstacle.centerX<0 }

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