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
public class auton_red_nearBasket extends AutonMethods {
    public final double POWER = 0.5;


    public void runOpMode() throws InterruptedException {
        telemetry.addData("Initialized:", "Hopefully");
        telemetry.update();
        //start timer
        ElapsedTime runtime = new ElapsedTime();



        waitForStart();
        runtime.reset();

        //move forward 1 block
        //clawOpen();
        forwardOneeee();
        stopDrive();
        //move left 0.2 block
        leftOneHalf();
        //move forward 0.3 block
        forwardHalf();
        //pick up sample
        //clawClose();
        //move in reverse 1 block
        backOneee();
        //move left 0.2 block
        leftOneHalf();
        //place sample in basket
        slidesRotateUp();
        slidesExtend();
        clawDrop();
        //clawOpen();
        //clawClose();
        clawPick();
        slidesRetract();
        //move forward 1.2 block
        forwardOneeee();
        //pick up sample
       // clawOpen();
        //clawClose();
        //move reverse 1.2 block
        backOneee();
        //place sample in basket
        slidesRotateUp();
        slidesExtend();
        clawDrop();
        //clawOpen();
       // clawClose();
        clawPick();
        slidesRetract();
        //move left 0.2 block
        leftOneHalf();
        //move forward 1.2 block
        forwardOneeee();
        //pick up sample
        //clawOpen();
        //clawClose();
        //move reverse 1.2 block
        backOneee();
        //move right 0.2 block
        rightOneHalf();
        //place block in basket
        slidesRotateUp();
        slidesExtend();
        clawDrop();
        //clawOpen();
        //clawClose();
        clawPick();
        slidesRetract();
        //move right 5 block
        rightFive();
        //reverse 0.2 block
        backOneHalf();
        //park robot back to starting point
        stopDrive();



        telemetry.addLine("Run time:" +  runtime.toString());
        telemetry.update();
    }
}
