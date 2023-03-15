package com.hh.moore_machine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class UnitTest {



    private val machine = MooreMachine<Int, Int>()
        .also {
            it.addAllTransition(Util.tr)
            it.init(1)
        }

    @Test
    fun emptyTransitionTest(){
        val b = MooreMachine<Int, Int>()
        val state = 555
        b.init(state)
        assertEquals("Without transition table state must be not change", state, b.act(1))
    }

    @Test
    fun actTest() {

        machine.act(1)
        machine.act(1)
        val noTrans = machine.act(999)
        val state = machine.act(1)

        assertEquals(1, state)
        assertEquals(5, noTrans)
    }

    @Test
    fun addTransitionTest(){

    }

    @Test
    fun verifyUnreachableTest() {

        assert(machine.verify(1))
        machine.addTransition(7, 1, 3)    // unreachable state 7
        assertFalse(machine.verify(1))
    }

    @Test
    fun verifyCopyTest() {
        val mM = MooreMachine<Int, Int>()
        mM.addAllTransition(Util.tr)
        mM.init(1)
        assert(machine.verify(1))

        mM.addTransition(5, 5, 5)       // copy of transition
        assertFalse(mM.verify(1))
    }

    @Test
    fun verifyAmbiguityTest() {
        val mM = MooreMachine<Int, Int>()
        mM.addAllTransition(Util.tr)
        mM.init(1)
        assert(mM.verify(1))

        mM.addTransition(5, 5, 1)       // ambiguity records
        assertFalse(mM.verify(1))
    }

    @Test
    fun logTest(){
        machine.logBufferSize = 3
        machine.act(1)
        machine.act(1)
        machine.act(5)
        machine.act(5)
        println(machine.getLog())
        println()
        machine.logBufferSize = 5
        machine.act(5)
        println(machine.getLog())
        machine.clearLog()
        machine.logBufferSize = 0
    }
}