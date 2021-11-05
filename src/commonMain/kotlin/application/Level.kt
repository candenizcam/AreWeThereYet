package application

import com.soywiz.klock.TimeSpan

class LevelGenerator {

    var obstacles = mutableListOf<Obstacle>()

    fun generate(){
        obstacles.add(Obstacle(ObstacleTypes.NORMAL, 0.05, 0.35, 0.1, 0.1))
    }

    fun update(dt: TimeSpan){
        obstacles.forEach {
            it.centerX = it.centerX - dt.milliseconds*0.2
        }

        obstacles.removeAll { obstacle -> obstacle.centerX<0 }

        if (dt.milliseconds%3 == 0.0){
            generate()
        }
    }
}