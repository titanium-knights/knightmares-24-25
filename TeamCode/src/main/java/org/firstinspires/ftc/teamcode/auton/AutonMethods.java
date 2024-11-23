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

 public void clawOpen(double x){
     double duration = 1.75 * x;
     claw.open();
    sleep((int)duration);
 }

public void slidesExtend(double x){
    double duration = 1.75 * x;
    slides.extend();
    telemetry.update();
    sleep((int)duration);
}

public void slidesRetract(){
    slides.retract();
    telemetry.update();
}

 public void clawClose(double x) {
     double duration = x * 1.75;
     claw.close();
     sleep((int) duration);
}
     public void stopDrive() {
         drivetrain.move(0, 0, 0);
         telemetry.update();
         sleep(100);
     }

     // Going forward, backward, turning, going left, going right
     public void backTiles(double x){
         double duration = 1.75 * x;
         //goes in reverse a little more than 1, about 1.2
         drivetrain.move(0, POWER, 0);
         sleep(1450);
         stopDrive();
     }

     public void rightTiles( double x){
         double duration = x * 1.75;

         drivetrain.move(-POWER, 0, 0);
         telemetry.update();
         sleep((int) duration);
         stopDrive();
     }

     public void slidesRotateUp() {
         slides.rotateRight();
         telemetry.update();
         sleep(1000);
     }

     public void slidesRotateDown() {
         slides.rotateLeft();
         telemetry.update();
         sleep(1000);
     }

     public void forwardTiles( double x){
         double duration = 1.75 * x;
         if (drivetrain != null) {
             drivetrain.move(0, POWER, 0);
             telemetry.addData("Action", "Moving forward");
         } else {
             telemetry.addData("Error", "Drivetrain is not initialized in forwardOneeee!");
         }
         telemetry.update();
         sleep((int) duration);
         stopDrive();
     }

     public void clawDrop (int x) {
         clawrotator.toDrop();
         telemetry.update();
         sleep(x);
     }

     public void clawPick() {
         clawrotator.toPick();
         telemetry.update();
     }


     public void rightLong() {
         // goes to the ending spot in auton
         drivetrain.move(-POWER, 0, 0);
         telemetry.update();
         sleep(8000);
         stopDrive();
     }

     public void leftTiles( double x){
         //should be a little less than 0.5 around 0.2
         double duration = 1.75 * x;
         drivetrain.move(POWER, 0, 0);
         telemetry.update();
         sleep((int) duration);
         stopDrive();
     }
     public void forwardHalf () {
         drivetrain.move(0, -POWER, 0);
         telemetry.update();
         sleep(900);
         stopDrive();
     }

     public void backOneHalf() {
         drivetrain.move(0, POWER, 0);
         telemetry.update();
         sleep(450);
         stopDrive();
     }


 }





