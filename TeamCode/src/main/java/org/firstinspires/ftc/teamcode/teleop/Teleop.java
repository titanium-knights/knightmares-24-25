package org.firstinspires.ftc.teamcode.teleop;

import static java.lang.Thread.sleep;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.backupAuton.auton.AutonMethods;
import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
import org.firstinspires.ftc.teamcode.utilities.Slides;
//import org.firstinspires.ftc.teamcode.utilities.PullUp;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.PullUp;
// import org.firstinspires.ftc.teamcode.utilities.Latch;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.SlidesState;

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
    final double normalPower = 0.8;

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
    ButtonPressState ultimateButton;

    boolean slowMode = false;
    boolean pullupstate1 = false;
    boolean pullupstate2 = false;
    boolean pulldownstate1 = false;
    boolean pulldownstate2 = false;
    int slidesPos;
    double time;
    int autonAction = 0;

    ElapsedTime runtime = new ElapsedTime();
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
        this.ultimateButton = ButtonPressState.UNPRESSED;


        this.claw = new Claw(hardwareMap, telemetry);
        // this.latch = new Latch(hardwareMap, telemetry);
        this.clawRotator = new ClawRotator(hardwareMap, telemetry);

        // slides.startPosition();
    }

    @Override
    public void loop() {

        telemetry.addLine("dis is slide numbre" + slides.getEncoder());

        if (gamepad1.left_trigger>0.1f && slideButton == ButtonPressState.UNPRESSED) {
            slideButton = ButtonPressState.PRESSED_GOOD;
        } else if (gamepad1.left_trigger>0.1f && slideButton==ButtonPressState.PRESSED_GOOD) {
            slideButton = ButtonPressState.DEPRESSED;
        } else if (gamepad1.left_trigger<0.1f) slideButton = ButtonPressState.UNPRESSED;

//        if (slides.getRotatorEncoder() < 600 && slides.getEncoder() > 500) {
//            while (slides.getEncoder() > 100) {
//                slides.retract();
//            }
//        }

        if (gamepad1.left_bumper){//slideManual==ButtonPressState.PRESSED_GOOD) {
            slides.retract();
            telemetry.addLine("slides rotator: " + String.valueOf(slides.getRotatorEncoder()));
            telemetry.addLine("slides: " + String.valueOf(slides.getEncoder()));
            telemetry.update();
        } else if (gamepad1.right_bumper){//slideManualUp==ButtonPressState.PRESSED_GOOD) {
//            if (slides.getRotatorEncoder() < 1000 && slides.getRotatorEncoder() > -1000) {
//                stop();
//            }
//            else {
//                slides.extend();
//            }
            slides.extend();
            telemetry.addLine("slides rotator: " + String.valueOf(slides.getRotatorEncoder()));
            telemetry.addLine("slides: " + String.valueOf(slides.getEncoder()));
            telemetry.update();
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
        telemetry.addLine("(" + x + ", " + y + ")");
        float turn = gamepad2.right_stick_x;
        if (slowMode) {
            telemetry.addLine("slowmode");
            float slowx = (float)0.4*x;
            float slowy = (float)0.4*y;
            stick_margin = 0.3f;
            move(slowx, slowy, turn);
        } else {
            stick_margin = 0.1f;
            move(x, y, turn);
        }
        int pul1 = pullup.getPosition1();
        int pul2 = pullup.getPosition2();

        if(gamepad2.b){
            pullup.rightDown();

        }
        if(gamepad2.x){
            pullup.leftDown();
        }


        //pull UP
        if(gamepad2.y){
            pullup.leftDown();
            pullup.rightDown();
        }
        else if(gamepad2.a){
            pullup.leftUp();
            pullup.rightUp();
        } else {
            pullup.stop();
        }

//        if (pullupstate1){
//            pulldownstate1 = false;
//
//            if (pullup.getPosition1() < -10) { // TODO: tune
//                pullup.stopLeft();
//                pullupstate1 = false;
//            } else {
//                pullup.leftUp();
//                telemetry.addLine(String.valueOf(pullup.getPosition1()) + "left up");
//            }
//        }
//        if (pullupstate2){
//            pulldownstate2 = false;
//
//            if (pullup.getPosition2() < -10) { // TODO: tune
//                pullup.stopRight();
//                pullupstate2 = false;
//            } else {
//                pullup.rightUp();
//                telemetry.addLine(String.valueOf(pullup.getPosition2()) + "righrt up");
//                telemetry.update();
//            }
//        }
//
//        if (pulldownstate1){
//            pullupstate1 = false;
//            if (pullup.getPosition1() > 4000) { // TODO: tune
//                pullup.stopLeft();
//                telemetry.addLine("stopped left");
//                pulldownstate1 = false;
//            } else {
//                pullup.leftDown();
//                telemetry.addLine(String.valueOf(pullup.getPosition1()) + "left down");
//                telemetry.update();
//            }
//        }
//        if (pulldownstate2){
//            pullupstate2 = false;
//            if (pullup.getPosition2() > 5000) { // TODO: tune
//                pullup.stopRight();
//                telemetry.addLine("stoppedRight");
//                pulldownstate2 = false;
//
//            } else {
//                pullup.rightDown();
//                telemetry.addLine(String.valueOf(pullup.getPosition2()) + "righrt down");
//                telemetry.update();
//            }
//
//
//        }
/*
        if(gamepad1.dpad_left){
            latch.latchOn();
        }

        if(gamepad1.dpad_right){
            latch.latchOff();
        }


        else */ if (gamepad1.right_trigger > 0.5) {
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
                telemetry.addLine("open claw: " + claw.getPosition());
                telemetry.update();
                clawOpen = true;
                clawButton = ButtonPressState.PRESSED_GOOD;
            } else {
                claw.close();
                telemetry.addLine("close claw: " + claw.getPosition());
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
//        if (gamepad1.dpad_up) {
//
//            while (slides.getEncoder() <= -100){
//                slides.retract();
//                telemetry.addLine("retracting slides: " + String.valueOf(slides.getEncoder()));
//
//            }
//            //TODO: tune
//            while (slides.getRotatorEncoder() <= 470){
//                slides.rotateRight();
//                telemetry.addLine("rotating slides: " + String.valueOf(slides.getRotatorEncoder()));
//            }
//
//            // latch.latchOn();
//
//            while (slides.getEncoder() >= -3000){
//                slides.extend();
//                telemetry.addLine("slides rotator: " + String.valueOf(slides.getRotatorEncoder()));
//                telemetry.addLine("slides: " + String.valueOf(slides.getEncoder()));
//                telemetry.update();
//            }qaaaaqsd
//            clawRotator.toDrop();
//
//        }
        if ((gamepad2.left_trigger > 0.7f) && (ultimateButton == ButtonPressState.UNPRESSED)){
            try {
                hangSpecimen();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void move(float x, float y, float turn) {
        // if the stick movement is negligible, set STICK_MARGIN to 0

        if (Math.abs(x) <= stick_margin) x = .0f;
        if (Math.abs(y) <= stick_margin) y = .0f;
        if (Math.abs(turn) <= stick_margin) turn = .0f;

        //Notation of a ? b : c means if a is true do b, else do c.
        double multiplier = normalPower;
        double stupidSrafeMultiplier = normalPower;
        if (Math.abs(x) > stick_margin) stupidSrafeMultiplier = 0.928057554 * normalPower;
        if (Math.abs(y) < 0.1) {
            drive.move(x * multiplier, y * multiplier, -turn * multiplier);
        }
        drive.move(x * multiplier, y * multiplier, -turn * multiplier);
    }

    public void setAutonAction (int action) {
        autonAction = action;
        runtime.reset();

    }
    public void hangSpecimen() throws InterruptedException {
        switch (autonAction) {
            case 0: // move back
                ultimateButton = ButtonPressState.PRESSED_GOOD;
                drive.move(0, -1, 0);
                if (time > 0.8) {
                    setAutonAction(1);
                }
                break;
            case 1: // stop
                drive.move(0, 0, 0);
                if (time > 0.1) {
                    setAutonAction(2);
                }
                break;
            case 2: // stop
                clawRotator.toDrop();
                if (time > 0.1) {
                    setAutonAction(3);
                }
                break;
            case 3: // rotate slides
                slides.rotateRight();
                if (time > 0.8) {
                    setAutonAction(4);
                }
                break;
            case 4: // stop slides
                slides.stopRotator();
                if (time > 0.1) {
                    setAutonAction(5);
                }
                break;
            case 5: // extend slides
                slides.extend();
                if (time > 0.8) {
                    setAutonAction(6);
                }
                break;
            case 6: // stop slides
                slides.stop();
                if (time > 0.1) {
                    setAutonAction(7);
                }
                break;
            case 7: // rotate claws
                clawRotator.toPick();
                if (time > 0.1) {
                    setAutonAction(8);
                }
                break;
            case 8: // slide retract a little bit
                slides.retract();
                if (time > 0.4) {
                    setAutonAction(9);
                }
                break;
            case 9: // stop slides
                slides.stop();
                if (time > 0.1) {
                    setAutonAction(10);
                }
                break;
            case 10: // stop slides
                slides.stop();
                if (time > 0.1) {
                    setAutonAction(11);
                }
                break;
            case 11: // drive forward a bit
                drive.move(0, 1, 0);
                if (time > 0.2) {
                    setAutonAction(12);
                }
                break;
            case 12: // stop driving
                drive.move(0, 0, 0);
                if (time > 0.1) {
                    setAutonAction(13);
                }
                break;
            case 13: // let go
                claw.open();
                if (time > 0.1) {
                    setAutonAction(14);
                }
                break;
            case 14: // rotate claws
                clawRotator.toDrop();
                if (time > 0.2) {
                    setAutonAction(15);
                }
                break;
            case 15: // retract slides
                slides.retract();
                if (time > 0.3) {
                    setAutonAction(16);
                }
                break;
            case 16: // top slides
                slides.stop();
                if (time > 0.1) {
                    setAutonAction(17);
                    ultimateButton = ButtonPressState.UNPRESSED;
                }

                break;
        }
    }

}