package org.firstinspires.ftc.teamcode.utilities

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry

//:)
@Config
class Latch(hmap: HardwareMap, var telemetry: Telemetry) {
    var latchServo: Servo = hmap.servo[CONFIG.latch]
    fun latchOn() {
        Companion.telemetry.addLine("latch on")
        Companion.telemetry.addLine(latchServo.position.toString())
        latchServo.position = latched
    }

    fun latchOff() {
        Companion.telemetry.addLine("latch off")
        Companion.telemetry.addLine(latchServo.position.toString())
        latchServo.position = unlatched
    }

    val position: Double
        get() = (latchServo.position)

    companion object {
        var latched: Double = 1.0 // -0.70
        var unlatched: Double = 0.6
    }
}
