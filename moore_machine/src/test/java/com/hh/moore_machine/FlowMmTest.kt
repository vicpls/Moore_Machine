package com.hh.moore_machine

import kotlinx.coroutines.*
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FlowMmTest {

    private val fmm = FlowMooreMashine<Int, Int>(1)

    init {
        fmm.addAllTransition(Util.tr)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun act_Test() = runTest(/*UnconfinedTestDispatcher()*/) {

        val testJob = launch {
            //println("launch start")
            fmm.act(1)
               .act(1)
               .act(1)

                .act(1)
                .act(1)
                .act(5)
            //println("launch end")
        }

        //runCurrent()
        advanceUntilIdle()
        //yield()
        //testJob.join()

        //println("assert")
        assertEquals(7, fmm.state.value)
    }




    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun act_Test2() = runTest(UnconfinedTestDispatcher()) {

        fmm.act(1)
            .act(1)
            .act(1)

            .act(1)
            .act(1)
            .act(5)

        advanceUntilIdle()

        assertEquals(7, fmm.state.value)




    }

    /*suspend fun a() {
        launch { println(":") }
        coroutineScope{ println() }
    }
    fun b()= flow{
        emit(1)
        launch { emit(2) }
    }

    @Test
    fun c() =runTest{
        b().collect{println(it)}
    }*/

}