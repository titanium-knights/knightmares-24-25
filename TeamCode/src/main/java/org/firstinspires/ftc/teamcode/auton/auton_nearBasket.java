package org.firstinspires.ftc.teamcode.auton;
//this is TIME BASED
// TODO CHANGE NUMBERS AFTER
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="nearBasket", group="Linear OpMode")
@Config
public class auton_nearBasket extends AutonMethods {


@Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        telemetry.addData("Initialized:", "Hopefully");
        telemetry.update();
        //start timer
        ElapsedTime runtime = new ElapsedTime();
        waitForStart();
        runtime.reset();

        // move forward 1 block
        clawOpen(1);
        forwardTiles(1);
        stopDrive();
        //move left 0.2 block
        leftTiles(1);
        //move forward 0.3 block
        forwardHalf();
        //pick up sample
        clawClose(1);
        //move in reverse 1 block
        backTiles(1);
        //move left 0.2 block
        leftTiles(1);
        //place sample in basket
        slidesRotateUp();
        slidesExtend(1);
        clawDrop(1);
        clawOpen(1);
        clawClose(1);
        clawPick();
        slidesRetract();
        //move forward 1.2 block
        forwardTiles(1);
        //pick up sample
        clawOpen(1);
        clawClose(1);
        //move reverse 1.2 block
        backTiles(1);
        //place sample in basket
        slidesRotateUp();
        slidesExtend(1);
        clawDrop(1);
        clawOpen(1);
        clawClose(1);
        clawPick();
       slidesRetract();
        //move left 0.2 block
        leftTiles(1);
        //move forward 1.2 block
        forwardTiles(1);
        //pick up sample
        clawOpen(1);
        clawClose(1);
        // move reverse 1.2 block
        backTiles(1);
        //move right 0.2 block
        rightTiles(1);
        //place block in basket
        slidesRotateUp();
        slidesExtend(1);
        clawDrop(1);
        clawOpen(1);
        clawClose(1);
        clawPick();
        slidesRetract();
       // move right 5 block
        rightTiles(1);
        //reverse 0.2 block
        backTiles(1);
        //park robot back to starting point
        stopDrive();



        telemetry.addLine("Run time:" +  runtime.toString());
        telemetry.update();
    }
}
