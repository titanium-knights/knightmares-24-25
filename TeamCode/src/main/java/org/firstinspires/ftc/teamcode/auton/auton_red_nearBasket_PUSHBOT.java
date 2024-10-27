package org.firstinspires.ftc.teamcode.auton;
//this is TIME BASED
// TODO CHANGE NUMBERS AFTER, THIS IS FOR WHEN WE DONT USE CLAW
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Autonomous(name="redBasket", group="Linear OpMode")
@Config

public class auton_red_nearBasket_PUSHBOT extends LinearOpMode {
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

        //TODO they said we can place anywhere thats touching
        // the walls so imagen we set it up on the 5th mat

        //push INITIAL sample (given, closer to basket)
        drivetrain.move(POWER, 0, 0);
        telemetry.addLine("Push INITIAL sample in basket zone");
        telemetry.update();
        sleep(1500);

        //move up behind the 3 samples

        // i defined power to be 0.5 in this case, and its mostly just means speed (i think?)
        // so im setting a speed for y and nothing for x and turn cuz i just want it to go forward
        // later on i used 0.4 instead of POWER because i was paranoid the robot would run over the sample so i made it slightly slower
        // i think a negative x value means going right and a positive x value means going left (from the perspective of the robot) for some reason
        // y is normal
        // idfk how to do turn we'll deal with it when we have to
        drivetrain.move(0, POWER, 0);
        // printing text to the phone, just useful for keeping track of what the robots doing
        telemetry.addLine("move back to original area");
        telemetry.update();
        // this just means how long i want the drivetrain.move() command to run for according to aubrey
        // this is the main value youre tuning
        sleep(2200);


        // move up directly behind the first sample
        drivetrain.move(POWER, 0, 0);
        telemetry.addLine("move behind the first sample");
        telemetry.update();
        sleep(500);


        // push the first sample
        drivetrain.move(0, -POWER, 0);
        telemetry.addLine("pushing first sample");
        telemetry.update();
        sleep(1900);

        // go back to sample area
        drivetrain.move(0, POWER, 0);
        telemetry.addLine("pushing first sample");
        telemetry.update();
        sleep(1900);

        //Slightly move near the second sample
        //TODO the x is negetive of what it was in auton_red_nearHuman
        drivetrain.move(POWER, 0, 0);
        telemetry.addLine("move beside second sample");
        telemetry.update();
        sleep(700);

        //push 2nt sample
        drivetrain.move(0, -0.4, 0);
        telemetry.addLine("push second sample in human");
        telemetry.update();
        sleep(2000);

        //Go back to samples area
        drivetrain.move(0, 0.4, 0);
        telemetry.addLine("Go back to samples (up)");
        telemetry.update();
        sleep(2000);

        //Slightly move near the third sample
        //TODO the x is negetive of what it was in auton_red_nearHuman
        drivetrain.move(POWER, 0, 0);
        telemetry.addLine("move beside third sample");
        telemetry.update();
        sleep(600);

        //push 3rd sample
        drivetrain.move(0, -0.4, 0);
        telemetry.addLine("push THIRD sample in human");
        telemetry.update();
        sleep(1900);


    }
}
