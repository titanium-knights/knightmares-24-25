package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.utilities.CONFIG.clawRotator;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
import org.firstinspires.ftc.robotcore.external.Telemetry;



public abstract class AutonMethods extends LinearOpMode {
    public SimpleMecanumDrive drivetrain;
    public ClawRotator clawrotator;
    // public Claw claw;
    public Slides slides;

    @Override
    public void runOpMode() throws InterruptedException{
        drivetrain = new SimpleMecanumDrive(hardwareMap);
        clawrotator = new ClawRotator(hardwareMap, telemetry);
        // claw = new Claw(hardwareMap, telemetry);
        slides = new Slides(hardwareMap, telemetry);
    }

    public final double POWER = 0.5;

// public void clawOpen(){
    // claw.open();
    //sleep(5000);
// }

public void slidesExtend(){
    slides.extend();
    sleep(6000);
}

public void slidesRetract(){
    slides.retract();
}

// public void clawClose(){
    // claw.close();
    // sleep(100);
// }
    public void stopDrive(){
        drivetrain.move(0,0,0);
        sleep(100);
    }

    // Going forward, backward, turning, going left, going right
    public void backOneee() {

       //goes in reverse a little more than 1, about 1.2
        drivetrain.move(0, POWER, 0);
        sleep(1450);
        stopDrive();
    }

    public void rightOneHalf() {
        drivetrain.move(-POWER, 0, 0);
        sleep(3000);
        stopDrive();
    }

    public void slidesRotateUp(){
    slides.rotateRight();
    sleep(1000);
    }

    public void slidesRotateDown(){
    slides.rotateLeft();
    sleep(1000);
    }

    public void forwardOneeee(){
        drivetrain.move(0, -POWER, 0);
        sleep(1450);
        stopDrive();
    }

    public void clawDrop(){
        clawrotator.toDrop();
        sleep(1450);
    }

    public void clawPick(){
    clawrotator.toPick();
    }


    public void rightFive() {
        // goes to the ending spot in auton
        drivetrain.move(-POWER,0, 0);
        sleep(5200);
        stopDrive();
    }

    public void leftOneHalf()  {
        //should be a little less than 0.5 around 0.2
        drivetrain.move(POWER, 0, 0);
        sleep(3000);
        stopDrive();
    }
    public void forwardHalf() {
        drivetrain.move(0,-POWER, 0);
        sleep(900);
        stopDrive();
    }

    public void backOneHalf() {
        drivetrain.move(0,POWER, 0);
        sleep(450);
        stopDrive();
    }











}



