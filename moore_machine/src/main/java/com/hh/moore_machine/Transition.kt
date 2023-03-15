package com.hh.moore_machine

/**
 * The record of state-transition table
 * @param curState current state
 * @param signal inputs
 * @param newState next State
 */
data class Transition <T, R> (
    val curState: T,
    val signal: R,
    val newState: T
) {
    override fun toString(): String
            = "$curState($signal)=>$newState\n"
}