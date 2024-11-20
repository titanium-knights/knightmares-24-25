package org.firstinspires.ftc.teamcode.auton

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.util.ElapsedTime

//this is TIME BASED
// TODO CHANGE NUMBERS AFTER
@Autonomous(name = "redBasket", group = "Linear OpMode")
@Config
class auton_park : AutonMethods() {
    @Throws(InterruptedException::class)
    override fun runOpMode() {
        super.runOpMode()
        telemetry.addData("Initialized:", "Hopefully")
        telemetry.update()
        //start timer
        val runtime = ElapsedTime()
        waitForStart()
        runtime.reset()

        rightLong()
        stopDrive()
    }
}