package org.firstinspires.ftc.teamcode.utilities

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry

//:)
@Config
class ClawRotator(hmap: HardwareMap, var telemetry: Telemetry) {

    var dropPos: Double = 0.8
    var pickPos: Double = 0.59

    var cRotatorServo: Servo = hmap.servo[CONFIG.clawRotator]

    init {
        cRotatorServo.direction = Servo.Direction.REVERSE
        //cRotatorServo.setPosition(pickPos);
        telemetry = telemetry
    }

    fun toDrop() {
        //cRotatorServo.setDirection(Servo.Direction.REVERSE);
        telemetry.addLine("claw to drop")
        cRotatorServo.position = dropPos
        telemetry.addLine(cRotatorServo.position.toString())
    }

    fun toPick() {
        //cRotatorServo.setDirection(Servo.Direction.REVERSE);
        telemetry.addLine("claw to pick")
        cRotatorServo.position = pickPos
        telemetry.addLine(cRotatorServo.position.toString())
    }

    val position: Double
        get() = (cRotatorServo.position)
    
}
