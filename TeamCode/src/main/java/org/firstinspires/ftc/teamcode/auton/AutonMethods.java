package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.utilities.CONFIG.clawRotator;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utilities.Latch;

public abstract class AutonMethods extends LinearOpMode {
    public SimpleMecanumDrive drivetrain;
    public ClawRotator clawrotator;
    public Claw claw;
    public Slides slides;
    public Latch latch;

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addData("Status", "Initializing hardware...");
        telemetry.update();

        // Initialize components and check for null references
        drivetrain = new SimpleMecanumDrive(hardwareMap);
        clawrotator = new ClawRotator(hardwareMap, telemetry);
        slides = new Slides(hardwareMap, telemetry);
        claw = new Claw(hardwareMap, telemetry);
        latch = new Latch(hardwareMap, telemetry);

        telemetry.addData("Status", "Initialization Complete");
        telemetry.update();
        waitForStart();
    }
    public final double POWER = 0.5;

    public void stopDrive() {
        drivetrain.move(0, 0, 0);
        telemetry.update();
        sleep(100);
    }

    // latch
    public void latch() {
        latch.latchOn();
    }

    public void unlatch() {
        latch.latchOn();
    }


    // claw
    public void clawOpen(){
        claw.open();
        sleep(1000);
    }

    public void clawClose() {
        claw.close();
    }

    // slides

    public void slidesExtend(){
        slides.extend();
        sleep(2000);
        slides.stop();
    }

    public void slidesRetract(){
        slides.retract();
        sleep(2000);
        slides.stop();
    }

    // Going forward, backward, turning, going left, going right

    public void moveForward(double x){
        double duration = 900 * x;
        drivetrain.move(0, POWER, 0);
        sleep((int)duration);
        stopDrive();
    }

     public void moveBackward(double x){
         double duration = 900 * x;
         drivetrain.move(0, -POWER, 0);
         sleep((int)duration);
         stopDrive();
     }

     public void moveRight(double x){
         double duration = 1500 * x;
         drivetrain.move(-POWER, 0, 0);
         sleep((int)duration);
         stopDrive();
     }

     public void moveLeft(double x){
        double duration = 1500 * x;
        drivetrain.move(POWER, 0, 0);
        sleep((int)duration);
        stopDrive();
     }

     public void rotateCw(double x){
        double duration = 3250 * x;
        drivetrain.move(0, 0, POWER);
        sleep((int)duration);
        stopDrive();
     }

     public void rotateCcw(double x){
        double duration = 3250 * x;
        drivetrain.move(0, 0, -POWER);
        sleep((int)duration);
        stopDrive();
     }

     // slides rotate

     public void slidesRotateUp() {
         slides.rotateRight();
         sleep(800);
         slides.stopRotator();
//         Runnable slidesRunnable = new Runnable() {
//             @Override
//             public void run() {
//                 sleep(10);
//                 slides.stopRotator();
//             }
//         };
//         Thread newThread = new Thread(slidesRunnable);
//         newThread.start();

     }

     public void slidesRotateDown() {
         slides.rotateLeft();
         sleep(1000);
         slides.stopRotator();
     }

     // claw drop/pick up

     public void clawDrop () {
         clawrotator.toDrop();
     }

     public void clawPick() {
         clawrotator.toPick();
     }
 }





