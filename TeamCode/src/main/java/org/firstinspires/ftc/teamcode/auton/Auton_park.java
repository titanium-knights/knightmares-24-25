package org.firstinspires.ftc.teamcode.auton;
//this is TIME BASED
// TODO CHANGE NUMBERS AFTER
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.auton.AutonMethods;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;



@Autonomous(name="redBasket", group="Linear OpMode")
@Config
public class Auton_park extends AutonMethods {


    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        telemetry.addData("Initialized:", "Hopefully");
        telemetry.update();
        //start timer
        ElapsedTime runtime = new ElapsedTime();
        waitForStart();
        runtime.reset();

        rightLong();
        stopDrive();

    }
}