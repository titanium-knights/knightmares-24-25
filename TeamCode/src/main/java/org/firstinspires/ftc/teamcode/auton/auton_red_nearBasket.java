package org.firstinspires.ftc.teamcode.auton;
//this is TIME BASED
// TODO CHANGE NUMBERS AFTER
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Autonomous(name="redHuman", group="Linear OpMode")
@Config
public class auton_red_nearBasket extends LinearOpMode{
    public final double POWER = 0.5;
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("STARTING! (hopfully)");
        telemetry.update();
        //start timer
        ElapsedTime runtime = new ElapsedTime();
        SimpleMecanumDrive drivetrain = new SimpleMecanumDrive(hardwareMap);

        waitForStart();
        runtime.reset();
        //move forward 1 block
        //move left 0.2 block
        //move forward 0.3 block
        //pick up sample
        //move in reverse 1 block
        //move left 0.2 block
        //place sample in basket
        //move forward 1.2 block
        //pick up sample
        //move reverse 1.2 block
        //place sample in basket
        //move left 0.2 block
        //move forward 1.2 block
        //pick up sample
        //move reverse 1.2 block
        //move right 0.2 block
        //place block in basket
        //move right 5 block
        //reverse 0.2 block
        //park robot back to starting point
    }
}
