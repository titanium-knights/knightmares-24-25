package org.firstinspires.ftc.teamcode.auton;
//this is TIME BASED
// TODO CHANGE NUMBERS AFTER
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Autonomous(name="blueHuman", group="Linear OpMode")
@Config

public class auton_blue_nearHuman extends LinearOpMode {
    //check what this does (just next line)
    public final double POWER = 0.5;
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("STARTING! (hopfully)");
        telemetry.update();

        //start timer
        ElapsedTime runtime = new ElapsedTime();
        SimpleMecanumDrive drivetrain = new SimpleMecanumDrive(hardwareMap);

        waitForStart();
        runtime.reset();


        //TODO add this if we can go diagolanly and not hit stuff (to save time)
        //go diagonally towards the first sample,
        // a little more x so we behind the samplet
//        drivetrain.move(10, 8, 0);
//        telemetry.addLine("did it make it behind the first sample");
//        telemetry.update();

        //push INITIAL sample (given)
        drivetrain.move(-POWER, 0, 0);
        telemetry.addLine("Push INITIAL sample in human zone");
        telemetry.update();
        sleep(1500);

        //move up
        drivetrain.move(0, POWER, 0);
        telemetry.addLine("move up");
        telemetry.update();
        sleep(2200);

        // do a 90 degree turn, so the side with nothing pushes the sample
//        drivetrain.move(0, 0, -POWER);
//        telemetry.addLine("turn 90 Degree");
//        telemetry.update();
//        sleep(2700);
//        // 2000 - 45 degrees
//        // 3000 - 100 degrees
//        // 4000 - 135 degrees

        //Slightly move near the first sample
        drivetrain.move(-POWER, 0, 0);
        telemetry.addLine("move beside first sample");
        telemetry.update();
        sleep(500);

        //push 1st sample
        drivetrain.move(0, -POWER, 0);
        telemetry.addLine("push first sample in human");
        telemetry.update();
        sleep(1900);

        //Go back to samples area
        drivetrain.move(0, POWER, 0);
        telemetry.addLine("Go back to samples (up)");
        telemetry.update();
        sleep(1900);

        //Slightly move near the second sample
        drivetrain.move(-POWER, 0, 0);
        telemetry.addLine("move beside second sample");
        telemetry.update();
        sleep(700);

        //push 2nd sample
        drivetrain.move(0, -0.4, 0);
        telemetry.addLine("push SECOND sample in human");
        telemetry.update();
        sleep(2000);

        //Go back to samples area
        drivetrain.move(0, 0.4, 0);
        telemetry.addLine("Go back to samples (up)");
        telemetry.update();
        sleep(2000);

        //Slightly move near the third sample
        drivetrain.move(-POWER, 0, 0);
        telemetry.addLine("move beside second sample");
        telemetry.update();
        sleep(600);

        //push 3rd sample
        drivetrain.move(0, -0.4, 0);
        telemetry.addLine("push THIRD sample in human");
        telemetry.update();
        sleep(2000);

        // Parking (turn toward ascent zone)
        drivetrain.move(0, 0, 0.4);
        telemetry.addLine("PARKING IN OBSERVATION(TURN)");
        telemetry.update();
        sleep(2000);

        //move forward
        drivetrain.move(POWER, 0, 0);
        telemetry.addLine("PARKING IN OBSERVATION(MOVE)");
        telemetry.update();
        sleep(1000000);
        telemetry.addLine("STAY AT HUMAN UNTIL ROUND END");
        telemetry.update();


    }
}
