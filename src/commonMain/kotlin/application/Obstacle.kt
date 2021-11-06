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
    LOWJUMP {
        override fun fileLocation(s: String): String {
            return "obs$s/low-jump-1.png"
        }
    },
    HIGHJUMP {
        override fun fileLocation(s: String): String {
            return "obs$s/high-jump-1.png"
        }
    },
    DUCT {
        override fun fileLocation(s: String): String {
            return "obs$s/duck-1.png"
        }
    },
    LONGJUMP {
        override fun fileLocation(s: String): String {
            return "obs$s/long-jump-1.png"
        }
    },
    DONTJUMP {
        override fun fileLocation(s: String): String {
            return "obs$s/dont-jump-1.png"
        }
    },
    JUMPDUCT {
        override fun fileLocation(s: String): String {
            return "obs$s/jump-duck-1.png"
        }
    };

    abstract fun fileLocation(s: String=""): String
}