package tests

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import utils.mockServer
import utils.projectAPIData


class TestSuite {
    @Test
    fun `test 1` (){
        println(projectAPIData.projectId)
        println(projectAPIData.createdAt)
    }
}