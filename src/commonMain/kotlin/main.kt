import application.TestModule
import com.soywiz.korge.*
import com.soywiz.korge.internal.KorgeInternal
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
@KorgeInternal
suspend fun main() = Korge(Korge.Config(module = TestModule))