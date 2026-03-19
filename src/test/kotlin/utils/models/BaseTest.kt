package utils.models

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import utils.server.MockServer

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseTest {
    @BeforeAll
    fun initServer() {
        MockServer.start()
    }
    @AfterAll
    fun destroyServer() {
        MockServer.stop()
    }
}