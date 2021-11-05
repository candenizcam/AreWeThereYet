package modules.basic

import com.soywiz.korim.color.RGBA
import kotlin.math.abs

/** This bad boy can be used to store and move around colours
 * it is initialized using companion functions of various colour entry types
 * base type is always 0f-1f, but various other input types are supported
 */
class Colour private constructor(r: Float, g: Float, b: Float, a: Float) {
    companion object{
        fun rgba256(r: Int, g: Int, b: Int, a: Int=255): Colour {
            return rgba(r/255f,g/255f,b/255f,a/255f)
        }

        fun rgba(r: Float, g: Float, b: Float, a: Float=1f): Colour {
            return Colour(r.coerceIn(0f,1f),g.coerceIn(0f,1f),b.coerceIn(0f,1f),a.coerceIn(0f,1f)).also {
                it.setHex()
                it.setHsv()
            }
        }

        fun hsva256(h: Int, s: Int, v: Int, a: Int=255): Colour {
            return hsva(h/255f,s/255f,v/255f,a/255f)
        }

        fun hsva(h: Float, s: Float, v: Float, a: Float=1f): Colour{
            val h = h.coerceIn(0f,1f)
            val s = s.coerceIn(0f,1f)
            val v = v.coerceIn(0f,1f)
            val a = a.coerceIn(0f,1f)
            val hCirc = (h*360).toInt()
            val c = v*s
            val m: Float = v-c
            val x: Float = c*(1f - abs((hCirc/60f)%2-1f))
            return when(hCirc){
                in 0..60 ->{
                    Colour(c+m,x+m,m,a)
                }
                in 60..120->{
                    Colour(x+m,c+m,m,a)
                }
                in 120..180->{
                    Colour(m,c+m,x+m,a)
                }
                in 180..240->{
                    Colour(m,x+m,c+m,a)
                }
                in 240..300->{
                    Colour(x+m,m,c+m,a)
                }
                in 300..360->{
                    Colour(c+m,m,x+m,a)
                }
                else ->{
                    Colour(m,m,m,a)
                }
            }.also {
                it.setHex()
                it.h = h
                it.s = s
                it.v = v
            }
        }

        fun byHex(hexCode: String): Colour {
            return when(hexCode.length){
                3->{
                    hexCode.map { decodeHex(it)*17/255f }.let {
                        Colour(it[0],it[1],it[2],1f)
                    }
                }
                4->{
                    hexCode.map { decodeHex(it)*17/255f }.let {
                        Colour(it[0],it[1],it[2],it[3])
                    }
                }
                6->{
                    (0..2).map { decodeHex(hexCode[2*it])*16 + decodeHex(hexCode[2*it+1])  }.let{
                        Colour(it[0]/255f,it[1]/255f,it[2]/255f,1f)
                    }

                }
                8->{
                    (0..3).map { decodeHex(hexCode[2*it])*16 + decodeHex(hexCode[2*it+1])  }.let{
                        Colour(it[0]/255f,it[1]/255f,it[2]/255f,it[3]/255f)
                    }
                }
                else -> {
                    throw Exception("Error, $hexCode is invalid")
                }
            }.also {
                it.setHsv()
                it.hex= hexCode
            }
        }

        fun byKorgeRGBA(r: RGBA): Colour {
            return rgba(r.rf,r.gf,r.bf,r.af)
        }

        private fun decodeHex(c: Char): Int {
            return try{
                "$c".toInt()
            }catch (e: Exception){
                when(c){
                    'A' -> 10
                    'B' -> 11
                    'C' -> 12
                    'D' -> 13
                    'E' -> 14
                    'F' -> 15
                    else -> throw (Exception("Error, invalid char $c for hex decoding"))
                }
            }
        }

        /** Common types
         *
         */
        val WHITE = Colour.rgba256(255,255,255)
        val BLACK = Colour.rgba256(0,0,0)
        val RED = Colour.rgba256(255,0,0)
        val GREEN = Colour.rgba256(0,255,0)
        val BLUE = Colour.rgba256(0,0,255)
        val CYAN = Colour.rgba256(0,255,255)
        val MAGENTA = Colour.rgba256(255,0,255)
        val YELLOW = Colour.rgba256(255,255,0)
        val GRIZEL = Colour.byHex("171819")
    }



    var r= r
        private set
    var g= g
        private set
    var b= b
        private set
    var a= a
        private set
    var hex = ""
        private set
    var h=0f
        private set
    var s=0f
        private set
    var v=0f
        private set
    val korgeColor: RGBA
        get() {
            return RGBA.float(r,g,b,a)
        }



    fun copy(): Colour {
        return rgba(r,g,b,a)
    }

    private fun setHex() {
        var h = ""
        listOf(r,g,b,a).forEach {
            (it*255).toInt().also {it2->
                h += toHexDigit(it2/16)
                h += toHexDigit(it2%16)
            }
        }
        hex = h
    }

    private fun toHexDigit(n: Int): String {
        return if(n>9){
            ('A'.toInt()-10+n).toChar()
        }else{
            n
        }.toString()

    }

    private fun setHsv(){
        val cMax = listOf(r,g,b).maxOrNull()?:0f
        val cMin = listOf(r,g,b).minOrNull()?:0f
        val delta = cMax-cMin
        h = when (cMax) {
            r -> {
                60f*((g-b)/delta%6)
            }
            g -> {
                60f*((b-r)/delta+2)
            }
            b -> {
                60f*((r-g)/delta+4)
            }
            else -> {
                0f
            }
        }/360f
        s = if(cMax>0){
            delta/cMax
        }else{
            0f
        }
        v = cMax
    }


}