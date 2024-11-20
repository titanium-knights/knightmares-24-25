package org.firstinspires.ftc.teamcode.utilities

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

class ClawIteration2(hmap: HardwareMap, telemetry: Telemetry?) {
    var pos: Int = 0


    var clawRotator: DcMotor? = null
    var clawMotor: DcMotor

    fun setPower(power: Double) {
        clawMotor.power = -0.95 * power
    }

    init {
        clawMotor = hmap.dcMotor[CONFIG.claw]

        clawMotor.mode = RunMode.STOP_AND_RESET_ENCODER
        clawMotor.mode = RunMode.RUN_WITHOUT_ENCODER
        clawMotor.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
    }

    val encoder: Int
        get() = -clawMotor.currentPosition

    val mode: RunMode
        get() = clawMotor.mode

    fun stop() {
        clawMotor.mode = RunMode.RUN_WITHOUT_ENCODER
        setPower(0.0)

        pos = encoder
    }

    val isBusy: Boolean
        get() = clawMotor.isBusy

    fun setTarget(target: Int) {
        clawMotor.targetPosition = -target
    }

    fun reset() {
        clawMotor.mode = RunMode.STOP_AND_RESET_ENCODER

        pos = 0


        clawMotor.mode = RunMode.RUN_WITHOUT_ENCODER
    }


    fun inside() {
        clawMotor.mode = RunMode.RUN_WITHOUT_ENCODER
        setPower(1.0)
    }

    fun outside() {
        clawMotor.mode = RunMode.RUN_WITHOUT_ENCODER
        setPower(-1.0)

        //rn crying over loosing two code
    }
}


