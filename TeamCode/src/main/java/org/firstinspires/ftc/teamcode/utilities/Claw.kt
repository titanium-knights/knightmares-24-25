package org.firstinspires.ftc.teamcode.utilities

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry

//:)
@Config
class Claw(hmap: HardwareMap, var telemetry: Telemetry) {
    var clawServo: Servo = hmap.servo[CONFIG.claw]

    // TODO: TUNE VALUES !!
    init {
        clawServo.direction = Servo.Direction.FORWARD
    }

    fun open() {
        telemetry.addLine("open claw")
        clawServo.position = openPos
    }

    fun close() {
        telemetry.addLine("close claw")
        clawServo.position = closedPos
    }

    val position: Double
        get() = (clawServo.position)

    companion object {
        var closedPos: Double = 0.3
        var openPos: Double = 0.5
    }
}