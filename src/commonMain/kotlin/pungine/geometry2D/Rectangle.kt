package pungine.geometry2D

import kotlin.math.abs

fun oneRectangle():Rectangle = Rectangle(0.0,1.0,0.0,1.0)

class Rectangle {
    val height: Double
    val width: Double
    val centre: Vector
    constructor(w1: Double, w2: Double, h1: Double, h2: Double){
        centre = Vector(w1*0.5+w2*0.5,h1*0.5+h2*0.5)
        width = abs(w1-w2)
        height = abs(h1-h2)
    }
    constructor(centre: Vector, width: Double, height: Double){
        this.centre=centre
        this.width = width
        this.height = height
    }
    constructor(corner: Vector,width: Double, height: Double, cornerType: Corners=Corners.TOP_LEFT){
        this.width = width
        this.height = height
        this.centre = when(cornerType){
            Corners.TOP_LEFT->{
                Vector(corner.x+width*0.5,corner.y - height*0.5)
            }
            Corners.TOP_RIGHT->{
                Vector(corner.x-width*0.5,corner.y - height*0.5)
            }
            Corners.BOTTOM_LEFT->{
                Vector(corner.x+width*0.5,corner.y + height*0.5)
            }
            Corners.BOTTOM_RIGHT->{
                Vector(corner.x-width*0.5,corner.y + height*0.5)
            }
        }
    }

    /** Other properties
     */
    val left: Double
        get() {
            return centre.x-width/2.0
        }
    val right: Double
        get() {
            return centre.x+width/2.0
        }
    val top: Double
        get() {
            return centre.y+height/2.0
        }
    val bottom: Double
        get() {
            return centre.y-height/2.0
        }

    fun getCorner(cornerType: Corners): Vector {
        return when(cornerType){
            Corners.TOP_LEFT->{
                Vector(left,top)
            }
            Corners.TOP_RIGHT->{
                Vector(right,top)
            }
            Corners.BOTTOM_LEFT->{
                Vector(left,bottom)
            }
            Corners.BOTTOM_RIGHT->{
                Vector(right,bottom)
            }
        }
    }

    /** Returns the rated rectangle of other taking this as 1x1
     * this.toRated(this) == one rectangle
     */
    fun toRated(other: Rectangle): Rectangle {
        val w1 = (other.left-left)/width
        val w2 = (other.right-left)/width
        val h1 = (other.top-bottom)/height
        val h2 = (other.bottom-bottom)/height
        return Rectangle(w1,w2,h1,h2)
    }

    /** Returns the rectangle that is rated by the other
     * this.fromRated(this.toRated(other)) == this
     */
    fun fromRated(other: Rectangle): Rectangle {
        val w1 = left+other.left*width
        val w2 = left+other.right*width
        val h1 = bottom+other.bottom*height
        val h2 = bottom+other.top*height
        return Rectangle(w1,w2,h1,h2)
    }


    /** Translation functions, returns translated rectangles
     */
    fun moved(vector: Vector): Rectangle {
        return Rectangle(this.centre+vector,width,height)
    }

    fun moved(x: Double, y: Double): Rectangle {
        return moved(Vector(x,y))
    }

    fun split(rows: Int=1, cols: Int=1): Splits {
        return Splits(List(rows) { 1.0 },List(cols) { 1.0 },this)
    }

    fun split(rows: List<Double> = listOf(1.0), cols: List<Double> = listOf(1.0)): Splits{
        return Splits(rows,cols,this)
    }

    /** Resize function, returns translated rectangle
     * w and h are coefficients to be multiplied by width and height respectively
     * if a cornerType is introduced, the resizing is done according to the corner
     */
    fun resized(w: Double=1.0, h: Double=1.0, fromCorner: Corners?=null): Rectangle {
        return if (fromCorner==null){
            Rectangle(this.centre,width*w,height*h)
        }else{
            val t = getCorner(fromCorner)
            Rectangle(getCorner(fromCorner),width*w,height*h,fromCorner)
        }
    }





    enum class Corners{
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    class Splits internal constructor(
        rows: List<Double> = listOf(1.0),
        cols: List<Double> = listOf(1.0),
        mother: Rectangle
    ) {
        private val rects = mutableListOf<Rectangle>()
        private val rowNo = rows.size
        private val colNo = cols.size
        init {
            val heightCoeff = mother.height/rows.sum()
            val widthCoeff = mother.width/cols.sum()

            var l = mother.left
            var t = mother.top

            for(i in rows.indices){
                for (j in cols.indices){
                    rects.add(Rectangle(l,l+rows[i]*widthCoeff,t,t-cols[j]*heightCoeff))

                    l+= cols[j]*widthCoeff
                }
                t-= rows[i]*heightCoeff
                l= mother.left
            }
        }

        operator fun get(r: Int, c: Int): Rectangle{
            return rects[(r-1)*colNo+c-1]
        }

    }
}