package application


data class Obstacle(var type: ObstacleTypes, var centerX: Double, var centerY: Double, var height: Double, var width: Double)
enum class ObstacleTypes{
    NORMAL,
    HIGH
}