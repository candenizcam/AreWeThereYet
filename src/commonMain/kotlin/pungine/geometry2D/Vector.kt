package pungine.geometry2D

import com.soywiz.korma.geom.Point
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

/** Vector is used as point where needed,
 *
 */
data class Vector(val x: Double, val y: Double) {


    val slope: Double
        get() {
            return y/x
        }

    fun rotated(rad: Double): Point{
        return Point(x*cos(rad)-y*sin(rad),y*cos(rad)+x*sin(rad))
    }

    fun rotated(deg: Int): Point{
        return rotated(deg.toFloat()/180*PI)
    }

    /** Vector addition
     */
    operator fun plus(other: Vector): Vector {
        return Vector(x+other.x,y+other.y)
    }

    operator fun minus(other: Vector): Vector {
        return Vector(x-other.x,y-other.y)
    }

    /** vector inner product
     */
    operator fun times(other: Vector): Double{
        return (x*other.x)+(y*other.y)
    }

    /** Scalar multiplication
     */
    operator fun times(other: Float): Vector {
        return Vector(x*other,y*other)
    }

    operator fun times(other: Int): Vector {
        return Vector(x*other,y*other)
    }

    operator fun times(other: Double): Vector {
        return Vector(x*other,y*other)
    }

    operator fun div(other: Int): Vector {
        return Vector(x/other,y/other)
    }

    operator fun div(other: Float): Vector {
        return Vector(x/other,y/other)
    }

    operator fun div(other: Double): Vector {
        return Vector(x/other,y/other)
    }

    override fun toString(): String {
        return "Point($x,$y)"
    }
}