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
    public Claw claw;
    public Slides slides;

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addData("Status", "Initializing hardware...");
        telemetry.update();

        // Initialize components and check for null references
        drivetrain = new SimpleMecanumDrive(hardwareMap);
        if (drivetrain == null) {
            telemetry.addData("Error", "drivetrain failed to initialize!");
        } else {
            telemetry.addData("SimpleMecanumDrive", "Initialized successfully");
        }

        clawrotator = new ClawRotator(hardwareMap, telemetry);
        if (clawrotator == null) {
            telemetry.addData("Error", "ClawRotator failed to initialize!");
        } else {
            telemetry.addData("ClawRotator", "Initialized successfully");
        }

        slides = new Slides(hardwareMap, telemetry);
        if (slides == null) {
            telemetry.addData("Error", "Slides failed to initialize!");
        } else {
            telemetry.addData("Slides", "Initialized successfully");
        }

        telemetry.addData("Status", "Initialization Complete");
        telemetry.update();
        waitForStart();
    }
    public final double POWER = 2;

    public void stopDrive() {
        drivetrain.move(0, 0, 0);
        telemetry.update();
        sleep(100);
    }

    // claw
    public void clawOpen(){
        claw.open();
        sleep(5000);
    }

    public void clawClose(double x) {
        claw.close();
        sleep(5000);
    }

    // slides

    public void slidesExtend(){
        slides.extend();
        sleep(5000);
    }

    public void slidesRetract(){
        slides.retract();
        sleep(5000);
    }

    // Going forward, backward, turning, going left, going right

    public void moveForward(double x){
        double duration = 1000 * x;
        drivetrain.move(0, POWER, 0);
        sleep((int)duration);
        stopDrive();
    }

     public void moveBackward(double x){
         double duration = 1000 * x;
         drivetrain.move(0, -POWER, 0);
         sleep((int)duration);
         stopDrive();
     }

     public void moveRight(double x){
         double duration = 1000 * x;
         drivetrain.move(-POWER, 0, 0);
         sleep((int)duration);
         stopDrive();
     }

     public void moveLeft(double x){
        double duration = 1000 * x;
        drivetrain.move(POWER, 0, 0);
        sleep((int)duration);
        stopDrive();
     }

     // slides rotate

     public void slidesRotateUp() {
         slides.rotateRight();
         sleep(5000);
     }

     public void slidesRotateDown() {
         slides.rotateLeft();
         sleep(5000);
     }

     // claw drop/pick up

     public void clawDrop () {
         clawrotator.toDrop();
         sleep(1000);
     }

     public void clawPick() {
         clawrotator.toPick();
         sleep(5000);
     }
 }





