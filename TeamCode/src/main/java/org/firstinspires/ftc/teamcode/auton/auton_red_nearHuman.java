package org.firstinspires.ftc.teamcode.auton;
//this is TIME BASED
// TODO CHANGE NUMBERS AFTER
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Autonomous(name="pray to rng-sus(that was sal, but pls actually pray-ipek)", group="Linear OpMode")
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

        //TODO add this if we can go diagolanly and not hit stuff (to save time)
        //go diagonally towards the first specimen,
        // a little more x so we behind the speciment
//        drivetrain.move(10, 8, 0);
//        telemetry.addLine("did it make it behind the first specimen");
//        telemetry.update();

        // move right
        drivetrain.move(10, 0, 0);
        telemetry.addLine("move right");
        telemetry.update();
        sleep(2000);

        //move up
        drivetrain.move(0, 30, 0);
        telemetry.addLine("move up");
        telemetry.update();
        sleep(2000);

        // do a 90 degree turn, so the side with nothing pushes the specimen
        drivetrain.move(0, 0, 10);
        telemetry.addLine("turn 90 Degree");
        telemetry.update();
        sleep(2000);

        //push 1st specimen
        drivetrain.move(0, -20, 0);
        telemetry.addLine("push first specimen in human");
        telemetry.update();
        sleep(2000);

        //Go back to spesimens area
        drivetrain.move(0, 20, 0);
        telemetry.addLine("Go back to spesimens (up)");
        telemetry.update();
        sleep(2000);

        //Slightly move near the second spesimen
        drivetrain.move(5, 0, 0);
        telemetry.addLine("move beside second spesimen");
        telemetry.update();
        sleep(2000);

        //push 2nd specimen
        drivetrain.move(0, -20, 0);
        telemetry.addLine("push SECOND specimen in human");
        telemetry.update();
        sleep(2000);

        //Go back to spesimens area
        drivetrain.move(0, 20, 0);
        telemetry.addLine("Go back to spesimens (up)");
        telemetry.update();
        sleep(2000);

        //Slightly move near the second spesimen
        drivetrain.move(5, 0, 0);
        telemetry.addLine("move beside second spesimen");
        telemetry.update();
        sleep(2000);

        //push 3rd specimen AND
        drivetrain.move(0, -30, 0);
        telemetry.addLine("push THIRD specimen in human");
        telemetry.update();
        sleep(2000);
        // TODO UNTIL AUTON ENDS
        telemetry.addLine("STAY AT HUMAN UNTIL ROUND END");
        telemetry.update();

        //im trying to find if park is human (im preety sure it is)
    }
}
