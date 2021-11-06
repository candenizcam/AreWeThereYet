package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korma.math.roundDecimalPlaces
import kotlin.random.Random

class LevelGenerator {

    var obstacles = mutableListOf<Obstacle>()
    var dtAcc = 0.0
    var toNext = 2.0
    var nextFloor = 3.0
    var nextCeil = 5.0
    var speed = 0.3
    var acceleration = 1.001
    var difficulty = 1.2

    fun generate(){
        when((0..1).random()) {
            0 -> obstacles.add(Obstacle(ObstacleTypes.NORMAL, 1.05, 0.05, 0.1, 0.1))
            1 -> obstacles.add(Obstacle(ObstacleTypes.HIGH, 1.05, 0.25, 0.1, 0.1))
        }
    }

    fun update(dt: TimeSpan){
        obstacles.forEach {
            speed = speed*(acceleration)
            it.centerX = it.centerX - dt.seconds*speed
        }

        obstacles.removeAll { obstacle -> obstacle.centerX<0 }

        dtAcc+=dt.seconds

        if (dtAcc >= toNext){
            dtAcc = 0.0
            generate()

            val seed = Random.nextDouble()
            seed.roundDecimalPlaces(2)

            toNext = (nextCeil - nextFloor) * seed + nextFloor

            nextCeil /= difficulty
            nextFloor /= difficulty
            //difficulty *= 0.95
        }
    }
}