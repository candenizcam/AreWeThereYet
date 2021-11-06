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
    var acceleration = 1.0001

    private fun generate(){
        when((0..1).random()) {
            0 -> obstacles.add(Obstacle(ObstacleTypes.NORMAL, 1.05, 0.05, 0.1, 0.1))
            1 -> obstacles.add(Obstacle(ObstacleTypes.HIGH, 1.05, 0.25, 0.1, 0.1))
        }
    }

    fun update(dt: TimeSpan){
        if(speed < 1.3){
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