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

        // scoring preload
        clawClose();
        clawClose(); // twice because nothing ever works the first time, for some reason
        clawPick();
        clawPick();
        moveForward(1);
        rotateCw(0.125);
        moveBackward(0.6);
        slidesRotateUp();
        // clawPick();
        slidesExtend();
        latch();
        moveBackward(0.2);
        clawDrop();
        clawOpen();
        moveForward(0.2);
        clawPick();
        unlatch();
        slidesRetract();
        slidesRotateDown();
        moveForward(0.6);
        rotateCcw(0.125);

        // *** past here i didnt tune but it should be close
        moveBackward(0.4);
        moveLeft(0.8);
        moveForward(0.2);
        clawClose();
        rotateCw(0.125);
        moveBackward(0.5);
        // slidesRotateUp();
        clawPick();
        // slidesExtend();
        moveBackward(0.2);
        clawDrop();
        clawOpen();
        moveForward(0.2);
        clawPick();
        // slidesRetract();
        // slidesRotateDown();


        /*
        //move forward 1.2 block
        moveForward(1);
        //pick up sample
        clawOpen();
        clawClose(1);
        //move reverse 1.2 block
        moveBackward(1);
        //place sample in basket
        slidesRotateUp();
        slidesExtend(1);
        clawDrop(1);
        clawOpen();
        clawClose(1);
        clawPick();
       slidesRetract();
        //move left 0.2 block
        moveLeft(1);
        //move forward 1.2 block
        moveForward(1);
        //pick up sample
        clawOpen();
        clawClose(1);
        // move reverse 1.2 block
        moveBackward(1);
        //move right 0.2 block
        moveRight(1);
        //place block in basket
        slidesRotateUp();
        slidesExtend(1);
        clawDrop(1);
        clawOpen();
        clawClose(1);
        clawPick();
        slidesRetract();
       // move right 5 block
        moveRight(1);
        //reverse 0.2 block
        moveBackward(1);
        //park robot back to starting point
        stopDrive();
        */


        telemetry.addLine("Run time:" +  runtime.toString());
        telemetry.update();
    }
}
