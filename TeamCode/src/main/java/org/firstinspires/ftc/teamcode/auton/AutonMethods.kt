package org.firstinspires.ftc.teamcode.auton

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.utilities.Claw
import org.firstinspires.ftc.teamcode.utilities.ClawRotator
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive
import org.firstinspires.ftc.teamcode.utilities.Slides

abstract class AutonMethods : LinearOpMode() {
    var drivetrain: SimpleMecanumDrive? = null
    var clawrotator: ClawRotator? = null
    var claw: Claw? = null
    var slides: Slides? = null

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        telemetry.addData("Status", "Initializing hardware...")
        telemetry.update()

        // Initialize components and check for null references
        drivetrain = SimpleMecanumDrive(hardwareMap)
        if (drivetrain == null) {
            telemetry.addData("Error", "drivetrain failed to initialize!")
        } else {
            telemetry.addData("SimpleMecanumDrive", "Initialized successfully")
        }

        clawrotator = ClawRotator(hardwareMap, telemetry)
        if (clawrotator == null) {
            telemetry.addData("Error", "ClawRotator failed to initialize!")
        } else {
            telemetry.addData("ClawRotator", "Initialized successfully")
        }

        slides = Slides(hardwareMap, telemetry)
        if (slides == null) {
            telemetry.addData("Error", "Slides failed to initialize!")
        } else {
            telemetry.addData("Slides", "Initialized successfully")
        }

        telemetry.addData("Status", "Initialization Complete")
        telemetry.update()
        waitForStart()
    }

    val POWER: Double = 2.0

    fun clawOpen() {
        claw!!.open()
        sleep(5000)
    }

    fun slidesExtend() {
        slides!!.extend()
        telemetry.update()
        sleep(6000)
    }

    fun slidesRetract() {
        slides!!.retract()
        telemetry.update()
    }

    // public void clawClose(){
    // claw.close();
    // sleep(100);
    // }
    fun stopDrive() {
        drivetrain!!.move(0.0, 0.0, 0.0)
        telemetry.update()
        sleep(100)
    }

    // Going forward, backward, turning, going left, going right
    fun backOneee() {
        //goes in reverse a little more than 1, about 1.2

        drivetrain!!.move(0.0, POWER, 0.0)
        sleep(1450)
        stopDrive()
    }

    fun rightOneHalf() {
        drivetrain!!.move(-POWER, 0.0, 0.0)
        telemetry.update()
        sleep(3000)
        stopDrive()
    }

    fun slidesRotateUp() {
        slides!!.rotateRight()
        telemetry.update()
        sleep(1000)
    }

    fun slidesRotateDown() {
        slides!!.rotateLeft()
        telemetry.update()
        sleep(1000)
    }

    fun forwardOne() {
        if (drivetrain != null) {
            drivetrain!!.move(0.0, POWER, 0.0)
            telemetry.addData("Action", "Moving forward")
        } else {
            telemetry.addData("Error", "Drivetrain is not initialized in forwardOneeee!")
        }
        telemetry.update()
        sleep(2000)
        stopDrive()
    }

    fun clawDrop() {
        clawrotator!!.toDrop()
        telemetry.update()
        sleep(1450)
    }

    fun clawPick() {
        clawrotator!!.toPick()
        telemetry.update()
    }


    fun rightLong() {
        // goes to the ending spot in auton
        drivetrain!!.move(-POWER, 0.0, 0.0)
        telemetry.update()
        sleep(8000)
        stopDrive()
    }

    fun leftOneHalf() {
        //should be a little less than 0.5 around 0.2
        drivetrain!!.move(POWER, 0.0, 0.0)
        telemetry.update()
        sleep(3000)
        stopDrive()
    }

    fun forwardHalf() {
        drivetrain!!.move(0.0, -POWER, 0.0)
        telemetry.update()
        sleep(900)
        stopDrive()
    }

    fun backOneHalf() {
        drivetrain!!.move(0.0, POWER, 0.0)
        telemetry.update()
        sleep(450)
        stopDrive()
    }
}



