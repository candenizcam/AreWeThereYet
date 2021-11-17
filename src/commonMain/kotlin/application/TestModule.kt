package application

import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.scene.Module
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korinject.injector
import kotlinx.coroutines.DelicateCoroutinesApi
import pungine.InternalGlobalAccess

@DelicateCoroutinesApi
@KorgeInternal
object TestModule: Module() {
    override val mainScene = EntryScene::class
    override val size = InternalGlobalAccess.virtualSize // Virtual Size
    override val windowSize = InternalGlobalAccess.windowSize// Window Size

    override suspend fun AsyncInjector.configure() {
        mapPrototype { GameScene() }
        mapPrototype { EntryScene() }
        mapPrototype { WindowScene() }
    }
}