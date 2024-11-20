package org.firstinspires.ftc.teamcode.utilities

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.HardwareMap

class PullUp(hmap: HardwareMap) {
    //DANIEL comment: For this, we don't really care about degrees so, we deal with
    //everything in encoder ticks or number of rotations
    var pullUpMotor1: DcMotor = hmap.dcMotor[CONFIG.pullUpMotor1]
    var pullUpMotor2: DcMotor = hmap.dcMotor[CONFIG.pullUpMotor2]

    init {
        setInit()
    }

    fun getPosition1(): Int {
        return pullUpMotor1.currentPosition
    }

    fun getPosition2(): Int {
        return pullUpMotor2.currentPosition
    }

    fun setInit() {
        // makes it so the motor is not loose when power is 0
        pullUpMotor2.mode = RunMode.STOP_AND_RESET_ENCODER
        pullUpMotor1.mode = RunMode.STOP_AND_RESET_ENCODER
        pullUpMotor1.mode = RunMode.RUN_WITHOUT_ENCODER
        pullUpMotor2.mode = RunMode.RUN_WITHOUT_ENCODER
        pullUpMotor1.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
        pullUpMotor2.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
    }

    val position1: Int
        get() = pullUpMotor1.currentPosition
    val position2: Int
        get() = pullUpMotor2.currentPosition


    val isBusy1: Boolean
        get() = pullUpMotor1.isBusy

    val isBusy2: Boolean
        get() = pullUpMotor2.isBusy

    fun stop() { // sets power to 0 - everything stops
        pullUpMotor1.mode = RunMode.RUN_WITHOUT_ENCODER
        pullUpMotor2.mode = RunMode.RUN_WITHOUT_ENCODER
        setPower(0.0)
    }

    fun setPower(power: Double) {
        pullUpMotor1.power = power
        pullUpMotor2.power = power
    }

    fun setTargetPosition(position: Int) {
        pullUpMotor1.targetPosition = position
        pullUpMotor2.targetPosition = position
    }

    fun reachUp() {
        setTargetPosition(topHeight)
        pullUpMotor1.mode = RunMode.RUN_TO_POSITION
        pullUpMotor2.mode = RunMode.RUN_TO_POSITION

        // run to position is always in presets or else itll be jittery
        pullUpMotor1.power = 0.9
        pullUpMotor2.power = 0.9
    }

    fun liftUp() {
        setTargetPosition(0)
        pullUpMotor1.mode = RunMode.RUN_TO_POSITION
        pullUpMotor2.mode = RunMode.RUN_TO_POSITION

        // run to position is always in presets or else itll be jittery
        pullUpMotor1.power = -0.9
        pullUpMotor2.power = -0.9
    }

    // pullUpMotor1 and 2 are reversed. If you want it to go up, power will be negative. If you want it to go down, power will be positive.
    fun manualRightDown() { // -1
        pullUpMotor2.mode = RunMode.RUN_WITHOUT_ENCODER
        pullUpMotor2.power = 1.0
    }

    fun manualRightUp() { // 1
        pullUpMotor2.mode = RunMode.RUN_WITHOUT_ENCODER
        pullUpMotor2.power = -1.0
    }

    fun manualLeftUp() { // 1
        pullUpMotor1.mode = RunMode.RUN_WITHOUT_ENCODER
        pullUpMotor1.power = -1.0
    }

    fun manualLeftDown() { // -1
        pullUpMotor1.mode = RunMode.RUN_WITHOUT_ENCODER
        pullUpMotor1.power = 1.0
    }

    fun stopLeft() {
        pullUpMotor1.power = 0.0
        pullUpMotor1.mode = RunMode.RUN_WITHOUT_ENCODER
    }

    fun stopRight() {
        pullUpMotor2.power = 0.0
        pullUpMotor2.mode = RunMode.RUN_WITHOUT_ENCODER
    }

    companion object {
        var Encoder_Ticks: Double = 537.7

        var topHeight: Int = -100 // 24 * Encoder_Ticks
    }
}