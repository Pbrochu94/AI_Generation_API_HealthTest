package utils.models

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import utils.server.MockServer

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseTest {
    companion object {
        private var started = false
        @BeforeAll
        @JvmStatic
        fun startServer() {
            if (!started) {
                MockServer.start()
                started = true
            }
        }
    }
}