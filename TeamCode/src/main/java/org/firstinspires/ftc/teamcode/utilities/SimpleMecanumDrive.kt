package org.firstinspires.ftc.teamcode.utilities

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import java.util.Objects
import kotlin.math.abs
import kotlin.math.max

class SimpleMecanumDrive(hmap: HardwareMap) {
    init {
        fl = hmap.get(DcMotor::class.java, CONFIG.FRONT_LEFT)
        fr = hmap.get(DcMotor::class.java, CONFIG.FRONT_RIGHT)
        bl = hmap.get(DcMotor::class.java, CONFIG.BACK_LEFT)
        br = hmap.get(DcMotor::class.java, CONFIG.BACK_RIGHT)

        fl.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
        bl.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
        fr.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
        br.zeroPowerBehavior = ZeroPowerBehavior.BRAKE

        fl.direction = DcMotorSimple.Direction.FORWARD
        bl.direction = DcMotorSimple.Direction.REVERSE
        fr.direction = DcMotorSimple.Direction.REVERSE // origina lly FORWARD
        br.direction = DcMotorSimple.Direction.REVERSE

        directions[fl] = doubleArrayOf(1.0, 1.0)
        directions[fr] = doubleArrayOf(1.0, -1.0)
        directions[bl] =
            doubleArrayOf(1.0, -1.0)
        directions[br] = doubleArrayOf(1.0, 1.0)
    }

    fun move(x: Double, y: Double, turn: Double) {
        // dot of fl and br

        val dot_fl = dot(Objects.requireNonNull(directions[fl]), doubleArrayOf(x, y)) - turn
        val dot_fr = (dot(
            Objects.requireNonNull(
                directions[fr]
            ), doubleArrayOf(x, y)
        ) + turn)
        val dot_bl = dot(Objects.requireNonNull(directions[bl]), doubleArrayOf(x, y)) - turn
        val dot_br = dot(Objects.requireNonNull(directions[br]), doubleArrayOf(x, y)) + turn

        val max = max(1.0, max(max(abs(dot_fl), abs(dot_fr)), max(abs(dot_bl), abs(dot_br))))
        fl.power = dot_fl / max
        br.power = dot_br / max
        fr.power = dot_fr / max
        bl.power = dot_bl / max
    }

    fun moveFL(x: Double, y: Double, turn: Double) {
        val dot_fl = dot(Objects.requireNonNull(directions[fl]), doubleArrayOf(x, y)) - turn
        val dot_fr = (dot(
            Objects.requireNonNull(
                directions[fr]
            ), doubleArrayOf(x, y)
        ) + turn)
        val dot_bl = dot(Objects.requireNonNull(directions[bl]), doubleArrayOf(x, y)) - turn
        val dot_br = dot(Objects.requireNonNull(directions[br]), doubleArrayOf(x, y)) + turn

        val max = max(1.0, max(max(abs(dot_fl), abs(dot_fr)), max(abs(dot_bl), abs(dot_br))))
        fl.power = dot_fl / max
        br.power = 0.0
        fr.power = 0.0
        bl.power = 0.0
    }

    fun moveFR(x: Double, y: Double, turn: Double) {
        val dot_fl = dot(Objects.requireNonNull(directions[fl]), doubleArrayOf(x, y)) - turn
        val dot_fr = (dot(
            Objects.requireNonNull(
                directions[fr]
            ), doubleArrayOf(x, y)
        ) + turn)
        val dot_bl = dot(Objects.requireNonNull(directions[bl]), doubleArrayOf(x, y)) - turn
        val dot_br = dot(Objects.requireNonNull(directions[br]), doubleArrayOf(x, y)) + turn

        val max = max(1.0, max(max(abs(dot_fl), abs(dot_fr)), max(abs(dot_bl), abs(dot_br))))
        fl.power = 0.0
        br.power = 0.0
        fr.power = dot_fr / max
        bl.power = 0.0
    }

    fun moveBL(x: Double, y: Double, turn: Double) {
        val dot_fl = dot(Objects.requireNonNull(directions[fl]), doubleArrayOf(x, y)) - turn
        val dot_fr = (dot(
            Objects.requireNonNull(
                directions[fr]
            ), doubleArrayOf(x, y)
        ) + turn)
        val dot_bl = dot(Objects.requireNonNull(directions[bl]), doubleArrayOf(x, y)) - turn
        val dot_br = dot(Objects.requireNonNull(directions[br]), doubleArrayOf(x, y)) + turn

        val max = max(1.0, max(max(abs(dot_fl), abs(dot_fr)), max(abs(dot_bl), abs(dot_br))))
        fl.power = 0.0
        br.power = 0.0
        fr.power = 0.0
        bl.power = dot_bl / max
    }

    fun moveBR(x: Double, y: Double, turn: Double) {
        val dot_fl = dot(Objects.requireNonNull(directions[fl]), doubleArrayOf(x, y)) - turn
        val dot_fr = (dot(
            Objects.requireNonNull(
                directions[fr]
            ), doubleArrayOf(x, y)
        ) + turn)
        val dot_bl = dot(Objects.requireNonNull(directions[bl]), doubleArrayOf(x, y)) - turn
        val dot_br = dot(Objects.requireNonNull(directions[br]), doubleArrayOf(x, y)) + turn

        val max = max(1.0, max(max(abs(dot_fl), abs(dot_fr)), max(abs(dot_bl), abs(dot_br))))
        fl.power = 0.0
        br.power = dot_br / max
        fr.power = 0.0
        bl.power = 0.0
    }

    // Each double[] will be a direction vector of length two
    fun dot(a: DoubleArray, b: DoubleArray): Double {
        return a[0] * b[0] + a[1] * b[1]
    }


    companion object {
        var fl: DcMotor
        var fr: DcMotor
        var bl: DcMotor
        var br: DcMotor

        var directions: HashMap<DcMotor, DoubleArray> = HashMap()
    }
}