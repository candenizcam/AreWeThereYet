package application

import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korma.geom.SizeInt
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlin.native.concurrent.ThreadLocal

/** This file contains variables for global access in the application,
 * user can manipulate here during development
 */
@DelicateCoroutinesApi
@KorgeInternal
@ThreadLocal
object GlobalAccess {
    val virtualSize = SizeInt(1920, 1080) // Virtual Size, this is the size that code pretends is the size of the screen

    var windowSize = SizeInt(1920, 1080) // Window Size, this is the actual size of the window in pixels

    var fingers = 2
    var firstEntry = true
    val soundsAreOn = false // change this before publishing
    val scoreKeeper = ScoreKeeper()
     init {
         scoreKeeper.load()
     }



    var entrySceneFirstCalled = false // this is so that opening crawl is only called once


}