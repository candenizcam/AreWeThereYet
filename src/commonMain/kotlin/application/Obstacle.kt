package application

import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector


data class Obstacle(var type: ObstacleTypes, var centerX: Double, var centerY: Double, var height: Double, var width: Double){
    val rectangle: Rectangle
    get() {
        return Rectangle(Vector(centerX,centerY),width,height)
    }
}
enum class ObstacleTypes{
    NORMAL,
    HIGH
}