package application

import com.soywiz.korau.sound.Sound
import com.soywiz.korau.sound.readSound
import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.onDown
import com.soywiz.korge.input.onUp
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.scene.SceneContainer
import com.soywiz.korge.view.views
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.SizeInt
import kotlinx.coroutines.DelicateCoroutinesApi
import pungine.geometry2D.Rectangle
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


}