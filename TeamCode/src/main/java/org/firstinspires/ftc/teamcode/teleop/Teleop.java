package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
import org.firstinspires.ftc.teamcode.utilities.Slides;
//import org.firstinspires.ftc.teamcode.utilities.PullUp;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.PullUp;
// import org.firstinspires.ftc.teamcode.utilities.Latch;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Config
@TeleOp(name="DriveTrain Teleop")
public class Teleop extends OpMode {
    Slides slides;
    PullUp pullup;
    Claw claw;
    // Latch latch;
    ClawRotator clawRotator;
    // ClawIteration2 claw;
    SimpleMecanumDrive drive;

    //Set normal power constant to 1, no point in slowing the robot down
    final double normalPower = 1;

    // in case of joystick drift, ignore very small values
    public float stick_margin = 0.7f;

    public boolean clawOpen = true;
    public boolean cRotatorAtDrop = false;

    enum ButtonPressState {
        PRESSED_GOOD, //the first time we see the button pressed
        DEPRESSED, //you haven't let go
        UNPRESSED // its not pressed
    }
    ButtonPressState slideButton;
    ButtonPressState rotatorButton;
    ButtonPressState clawButton;
    ButtonPressState cRotatorButton;
    ButtonPressState slideManual;
    ButtonPressState slideManualUp;

    boolean slowMode = false;
    boolean pullupstate1 = false;
    boolean pullupstate2 = false;
    boolean pulldownstate1 = false;
    boolean pulldownstate2 = false;
    int slidesPos;
    @Override
    public void init() {
        this.drive = new SimpleMecanumDrive(hardwareMap);
        this.slides = new Slides(hardwareMap);
        this.pullup = new PullUp(hardwareMap);
        this.clawButton = ButtonPressState.UNPRESSED;
        this.cRotatorButton = ButtonPressState.UNPRESSED;
        this.slideButton = ButtonPressState.UNPRESSED;
        this.slideManual = ButtonPressState.UNPRESSED;
        this.slideManualUp = ButtonPressState.UNPRESSED;

        this.claw = new Claw(hardwareMap);
        // this.latch = new Latch(hardwareMap, telemetry);
        this.clawRotator = new ClawRotator(hardwareMap, telemetry);

        // slides.startPosition();
    }

    @Override
    public void loop() {

        if (gamepad1.left_trigger>0.1f && slideButton == ButtonPressState.UNPRESSED) {
            slideButton = ButtonPressState.PRESSED_GOOD;
        } else if (gamepad1.left_trigger>0.1f && slideButton==ButtonPressState.PRESSED_GOOD) {
            slideButton = ButtonPressState.DEPRESSED;
        } else if (gamepad1.left_trigger<0.1f) slideButton = ButtonPressState.UNPRESSED;

        if (gamepad1.left_bumper){//slideManual==ButtonPressState.PRESSED_GOOD) {
            slides.retract();
            telemetry.addLine("retracting slides: " + String.valueOf(slides.getEncoder()));
        } else if (gamepad1.right_bumper){//slideManualUp==ButtonPressState.PRESSED_GOOD) {
            slides.extend();
            telemetry.addLine("extending slides: " + String.valueOf(slides.getEncoder()));
        } else {
            slides.stop();
        }
        //DRIVETRAIN TELEMETRY
        telemetry.addLine(String.valueOf(drive.getfl()) + "get front left");
        telemetry.addLine(String.valueOf(drive.getbr()) + "get back right");
        telemetry.addLine(String.valueOf(drive.getfr()) + "get front right");
        telemetry.addLine(String.valueOf(drive.getbl()) + "get back left");
        telemetry.update();

        //DRIVE
        float x = gamepad2.left_stick_x;
        float y = gamepad2.left_stick_y;
        float turn = gamepad2.right_stick_x;
        if (slowMode) {
            telemetry.addLine("slowmode");
            float slowx = (float)0.4*x;
            float slowy = (float)0.4*y;
            stick_margin = 0.3f;
            move(slowx, -slowy, turn);
        } else {
            stick_margin = 0.7f;
            move(x, -y, turn);
        }
        int pul1 = pullup.getPosition1();
        int pul2 = pullup.getPosition2();

        if(gamepad2.b){
            pullup.rightDown();

        }
        if(gamepad2.x){
            pullup.leftDown();
        }



        if(gamepad2.y){
            pullupstate1 = true;
            pullupstate2 = true;

        }
        if (pullupstate1){


            if (pullup.getPosition1() < -5000) { // TODO: tune
                pullup.stopLeft();
                pullupstate1 = false;
            } else {
                pullup.leftUp();
                telemetry.addLine(String.valueOf(pullup.getPosition1()) + "left up");
            }
        }
        if (pullupstate2){


            if (pullup.getPosition2() < -5000) { // TODO: tune
                pullup.stopRight();
                pullupstate2 = false;
            } else {
                pullup.rightUp();
                telemetry.addLine(String.valueOf(pullup.getPosition2()) + "righrt up");
                telemetry.update();
            }
        }
        if(gamepad2.a){
            pulldownstate1 = true;
            pulldownstate2 = true;

        }
        if (pulldownstate1){
            if (pullup.getPosition1() > -50) { // TODO: tune
                pullup.stopLeft();
                telemetry.addLine("stopped left");
                pulldownstate1 = false;

            } else {
                pullup.leftDown();
                telemetry.addLine(String.valueOf(pullup.getPosition1()) + "left down");
                telemetry.update();
            }
        }
        if (pulldownstate2){
            if (pullup.getPosition2() > -50) { // TODO: tune
                pullup.stopRight();
                telemetry.addLine("stoppedRight");
                pulldownstate2 = false;

            } else {
                pullup.rightDown();
                telemetry.addLine(String.valueOf(pullup.getPosition2()) + "righrt down");
                telemetry.update();
            }


        }

//        if(gamepad1.dpad_left){
//            latch.latchOn();
//        }
//
//        if(gamepad1.dpad_right){
//            latch.latchOff();
//        }

        //  ROTATING SLIDES
        if (gamepad1.right_trigger > 0.5) {
            slides.rotateRight();
            telemetry.addLine("rotating slides: " + String.valueOf(slides.getRotatorEncoder()));

        } else if (gamepad1.left_trigger > 0.5){
            slides.rotateLeft();
            telemetry.addLine("rotating slides: " + String.valueOf(slides.getRotatorEncoder()));
            telemetry.update();
        } else {
            slides.stopRotator();
        }
        if (gamepad2.dpad_down){
            slowMode = true;
            telemetry.addLine("keep up: " + String.valueOf(slides.getRotatorEncoder()));
            telemetry.update();
        } else if (gamepad2.dpad_up){
            slowMode = false;
            telemetry.update();
    }

        // improved code by yours truly:
        if (gamepad1.y && (clawButton == ButtonPressState.UNPRESSED)) {
            if (!clawOpen) {
                claw.open();
                telemetry.addLine("open claw");
                telemetry.update();
                clawOpen = true;
                clawButton = ButtonPressState.PRESSED_GOOD;
            } else {
                claw.close();
                telemetry.addLine("close claw");
                telemetry.update();
                clawOpen = false;
                clawButton = ButtonPressState.PRESSED_GOOD;
            }

        } else if (!gamepad1.y && (clawButton == ButtonPressState.PRESSED_GOOD)) {
            clawButton = ButtonPressState.UNPRESSED;
        }


        if (gamepad1.a && (cRotatorButton == ButtonPressState.UNPRESSED) && !cRotatorAtDrop) {
            cRotatorButton = ButtonPressState.PRESSED_GOOD;
            clawRotator.toDrop();
            telemetry.addLine("claw to drop position: " + clawRotator.getPosition());
            cRotatorAtDrop = true;
        } else if (gamepad1.a && (cRotatorButton == ButtonPressState.UNPRESSED) && cRotatorAtDrop) {
            cRotatorButton = ButtonPressState.PRESSED_GOOD;
            clawRotator.toPick();
            telemetry.addLine("claw to pick position: " + clawRotator.getPosition());
            cRotatorAtDrop = false;
        } else if (!(gamepad1.a) && (cRotatorButton == ButtonPressState.PRESSED_GOOD)){
            cRotatorButton = ButtonPressState.UNPRESSED;
        }

        // THE ULTIMATE BUTTON
        if (gamepad1.dpad_up) {

            while (slides.getEncoder() <= -100){
                slides.retract();
                telemetry.addLine("retracting slides: " + String.valueOf(slides.getEncoder()));

            }
            //TODO: tune
            while (slides.getRotatorEncoder() <= 470){
                slides.rotateRight();
                telemetry.addLine("rotating slides: " + String.valueOf(slides.getRotatorEncoder()));
            }

            // latch.latchOn();

            while (slides.getEncoder() >= -3000){
                slides.extend();
                telemetry.addLine("extending slides: " + String.valueOf(slides.getEncoder()));
            }

            clawRotator.toDrop();

        }
    }

    public void move(float x, float y, float turn) {
        // if the stick movement is negligible, set STICK_MARGIN to 0
        if (Math.abs(x) <= stick_margin) x = .0f;
        if (Math.abs(y) <= stick_margin) y = .0f;
        if (Math.abs(turn) <= stick_margin) turn = .0f;

        //Notation of a ? b : c means if a is true do b, else do c.
        double multiplier = normalPower;
        drive.move(-x * multiplier, y * multiplier, -turn * multiplier);
    }

}
