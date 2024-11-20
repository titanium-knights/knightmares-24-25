package org.firstinspires.ftc.teamcode.utilities

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

//NEGATIVE IS UP
//Code credited to Shawn Mendes the handsome ;)
class Slides(hmap: HardwareMap, var telemetry: Telemetry) {
    // to go up
    //Looking from front, Left (acc on the right in this view), must go clockwise
    // Right (Acc on the left in this view), must go counter clockwise
    // Positive power is counter clockwise,
    //position at initial
    var pos: Int = 0 //up and down motor position
    var rot: Int = 0 // left and right rotator position

    //Current state of slide. 0 - idle, 1 - up, 2 - down
    //TODO: consider using an enum
    var state: Int
    var rotState: Int = 0

    // limits
    var maxheight: Int = -3000 // 3481
    var minheight: Int = -100

    // basket heights
    // TODO: tune these values
    var highheight: Int = -2900

    var lowheight: Int = -1500

    // rotator limits
    var uprot: Int = 100 // 3481 proviously 25
    var downrot: Int = 100 // 3481 proviously 25

    fun getEncoder(): Int {
        return -slideMotor.currentPosition
    }

    fun getRotatorEncoder(): Int {
        return -slideRotator.currentPosition
    }

    //slide rotator code from now on
    fun getPosition1(): Int {
        return slideRotator.currentPosition
    }

    //getting target (idk what that is)
    fun getTarget(): Int {
        return slideMotor.targetPosition
    }

    fun getRotTarget(): Int {
        return slideRotator.targetPosition
    }

    //get runmode?
    fun getMode(): RunMode {
        return slideMotor.mode
    }
    //assign motors to slide motors and slide rotator motors
    var slideMotor: DcMotor = hmap.dcMotor[CONFIG.slide]
    var slideRotator: DcMotor = hmap.dcMotor[CONFIG.slideRot]

    init {
        this.state = this.pos

        slideMotor.mode = RunMode.STOP_AND_RESET_ENCODER
        slideMotor.mode = RunMode.RUN_WITHOUT_ENCODER
        slideMotor.zeroPowerBehavior = ZeroPowerBehavior.BRAKE

        slideRotator.mode = RunMode.STOP_AND_RESET_ENCODER
        slideRotator.mode = RunMode.RUN_WITHOUT_ENCODER
        slideRotator.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
    }

    //stop motors
    fun stop() {
        slideMotor.mode = RunMode.RUN_WITHOUT_ENCODER
        setPower(0.0)

        pos = encoder

        state = 0
    }

    fun stopRotator() {
        slideRotator.mode = RunMode.RUN_WITHOUT_ENCODER
        setRotPower(0.0)

        pos = rotatorEncoder

        rotState = 0
    }

    val isBusy: Boolean
        //is busy
        get() = slideMotor.isBusy

    fun rotatorIsBusy(): Boolean {
        return slideRotator.isBusy
    }

    //reset
    fun reset() {
        slideMotor.mode = RunMode.STOP_AND_RESET_ENCODER

        pos = 0
        state = 0

        slideMotor.mode = RunMode.RUN_WITHOUT_ENCODER
    }

    fun resetRotator() {
        slideRotator.mode = RunMode.STOP_AND_RESET_ENCODER

        rot = 0
        rotState = 0

        slideRotator.mode = RunMode.RUN_WITHOUT_ENCODER
    }

    //run to position
    fun runToPosition() {
        slideMotor.mode = RunMode.RUN_TO_POSITION
        setPower(0.9)
    }

    fun runRotToPosition() {
        slideRotator.mode = RunMode.RUN_TO_POSITION
        setPower(0.9)
    }

    fun tozero() {
        target = 0
        runToPosition()
        pos = encoder
    }

    // BASKET PRESETS
    fun lowBasket() {
        target = lowheight
        runToPosition()
        pos = encoder
    }

    fun highBasket() {
        target = highheight
        runToPosition()
        pos = encoder
    }

    // ROTATOR PRESETS
    fun up() {
        rotTarget = uprot
        runRotToPosition()
        rot = rotatorEncoder
    }

    fun down() {
        rotTarget = downrot
        runRotToPosition()
        rot = rotatorEncoder
    }

    // SLIDES MANUAL
    fun extend() {
        telemetry.addLine("extending")
        telemetry.addLine(pos.toString())

        slideMotor.mode = RunMode.RUN_WITHOUT_ENCODER
        pos = encoder
        // max limit
        if (pos <= maxheight) {
            setPower(0.0)
            return
        }
        if (state == 1 && pos <= -2200) {
            setPower(-4.0)
            pos = encoder
            return
        }
        if (state == 1) {
            return
        }
        state = 1
        setPower(-7.0)
    }


    fun retract() {
        telemetry.addLine("retracting")
        slideMotor.mode = RunMode.RUN_WITHOUT_ENCODER
        pos = encoder
        telemetry.addLine(pos.toString())

        //        if (pos < -0.2){
//            setPower(0);
//            return;
//        }

        // slower retract closer down
        if (pos >= minheight) {
            setPower(0.0)
            pos = encoder
            return
        }
        if (state == 2 && pos >= -900) {
            setPower(1.0)
            pos = encoder
            return
        }

        if (state == 2) {
            return
        }

        state = 2
        setPower(3.0)
    }

    // ROTATOR (rotater? rotator.) MANUAL
    fun keepUp() {
        slideRotator.mode = RunMode.RUN_WITHOUT_ENCODER
        rot = rotatorEncoder
        telemetry.addLine("AWAIUWNAIFNEIONFUEWHGUEWHGUEWGHPOU")
        setRotPower(1.0)
    }

    fun rotateRight() { //slide rotates outwards (up)
        slideRotator.mode = RunMode.RUN_WITHOUT_ENCODER
        rot = rotatorEncoder
        telemetry.addLine(rot.toString())


        //id
//        if (rotState == 1 && rot >= 200){
//            setRotPower(3);
//            rot = getRotatorEncoder();
//            return;
//        }
//        if (rotState == 1){
//            return;
//        }
        rotState = 1
        setRotPower(10.0)
    }

    //TODO: add rotator limit @ 400
    fun rotateLeft() { // slide rotates inwards (down)
        slideRotator.mode = RunMode.RUN_WITHOUT_ENCODER
        rot = rotatorEncoder
        telemetry.addLine(rot.toString())

        if (rotState == 2 && rot <= 200) {
            setRotPower(-4.0)
            rot = rotatorEncoder
            return
        }
        if (rotState == 2) {
            return
        }
        rotState = 2
        setRotPower(-10.0)
    }

    val encoder: Int
        get() = -slideMotor.currentPosition
    val rotatorEncoder: Int
        get() = -slideRotator.currentPosition
    val position1: Int
        //slide rotator code from now on
        get() = slideRotator.currentPosition
    var target: Int
        //getting target (idk what that is)
        get() = slideMotor.targetPosition
        //set target
        set(target) {
            slideMotor.targetPosition = -target
        }
    var rotTarget: Int
        get() = slideRotator.targetPosition
        set(target) {
            slideRotator.targetPosition = -target
        }
    val mode: RunMode
        //get runmode?
        get() = slideMotor.mode

    //set power
    fun setPower(power: Double) {
        slideMotor.power = -0.95 * power // constant removed
    }

    fun setRotPower(power: Double) {
        slideRotator.power = -0.10 * power // constant removed
    }
}