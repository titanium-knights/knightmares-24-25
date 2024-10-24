package org.firstinspires.ftc.teamcode.auton;
//this is TIME BASED

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Autonomous(name="pray, i mean it pull up the bible", group="Linear OpMode")
@Config

public class auton_red_nearHuman extends LinearOpMode {
    //check what this does (just next line)
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("STARTING! (hopfully)");
        telemetry.update();

        //start timer
        ElapsedTime runtime = new ElapsedTime();
        SimpleMecanumDrive drivetrain = new SimpleMecanumDrive(hardwareMap);

        waitForStart();
        runtime.reset();

        //go diagonally towards the first specimen,
        // a little more x so we behind the speciment
        drivetrain.move(10, 8, 0);
        telemetry.addLine("did it make it behind the first specimen");
        telemetry.update();

        sleep(2000);
        //stop for amount, change number later

        // do a 90 degree turn, so the side with nothing pushes the specimen
        drivetrain.move(0, 0, 10);
        sleep(2000);
        //stop for amount, change number later

        //push 1st specimen
        drivetrain.move(0, 20, 0);
        telemetry.addLine("push first specimen in human ");
        telemetry.update();

        sleep(2000);
        //stop for amount, change number later

        //slightly move to the side so nock specimen
        drivetrain.move(10, 0, 0); //added random y value to get rid of error
        telemetry.addLine("did it make it behind the first specimen");
        telemetry.update();

        sleep(2000);
        //stop for amount, change number later

    } //I definitely did work today! :P -Sal
}