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
import org.firstinspires.ftc.teamcode.utilities.Latch;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Config
@TeleOp(name="DriveTrain Teleop")
public class Teleop extends OpMode {
    Slides slides;
    PullUp pullup;
    Claw claw;
    Latch latch;
    ClawRotator clawRotator;
    // ClawIteration2 claw;
    SimpleMecanumDrive drive;

    //Set normal power constant to 1, no point in slowing the robot down
    final double normalPower = 1;

    // in case of joystick drift, ignore very small values
    final float STICK_MARGIN = 0.7f;

    public boolean clawState = true;
    public boolean cRotatorAtDrop = false;

    //Prevents irreversible things such as pullup and plane launcher from running before this button is pressed
    // (will add later)
    // boolean validate = false;
    //makes validate button have to be pressed for a while before features enabled
    // int validatecount = 0;

    public enum SlideState {
        SLIDE_BOTTOM,
        SLIDE_LOW,
        SLIDE_MEDIUM,
        SLIDE_HIGH
    }
    public enum RotatorState {
        SLIDE_DOWN,
        SLIDE_UP,
        SLIDE_STOP
    }
    SlideState slideState = SlideState.SLIDE_BOTTOM;
    RotatorState rotatorState = RotatorState.SLIDE_DOWN;

    public enum PullUpState {
        NEUTRAL,
        REACH_UP
    }
    PullUpState pullupstate = PullUpState.NEUTRAL;

    ElapsedTime elapsedTime;

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

    boolean started = false;

    boolean slowMode = false;

    // boolean intakeRunning = false;
    @Override
    public void init() {
        this.drive = new SimpleMecanumDrive(hardwareMap);
        this.slides = new Slides(hardwareMap, telemetry);
        this.pullup = new PullUp(hardwareMap, telemetry);
        this.clawButton = ButtonPressState.UNPRESSED;
        this.cRotatorButton = ButtonPressState.UNPRESSED;
        this.slideButton = ButtonPressState.UNPRESSED;
        this.slideManual = ButtonPressState.UNPRESSED;
        this.slideManualUp = ButtonPressState.UNPRESSED;

        this.claw = new Claw(hardwareMap, telemetry);
        this.latch = new Latch(hardwareMap, telemetry);
        this.clawRotator = new ClawRotator(hardwareMap, telemetry);

        // slides.startPosition();
    }

    @Override
    public void loop() {

        //VALIDATE
//        if (gamepad1.dpad_up) {++validatecount;}
//        if (validatecount > 5) {validate = true;}

        if (gamepad1.left_trigger>0.1f && slideButton == ButtonPressState.UNPRESSED) {
            slideButton = ButtonPressState.PRESSED_GOOD;
        } else if (gamepad1.left_trigger>0.1f && slideButton==ButtonPressState.PRESSED_GOOD) {
            slideButton = ButtonPressState.DEPRESSED;
        } else if (gamepad1.left_trigger<0.1f) slideButton = ButtonPressState.UNPRESSED;

//        if (gamepad1.right_bumper && slideManual == ButtonPressState.UNPRESSED) {
//            slideManual = ButtonPressState.PRESSED_GOOD;
//        } else if (gamepad1.right_bumper && slideManual==ButtonPressState.PRESSED_GOOD) {
//            slideManual = ButtonPressState.DEPRESSED;
//        } else if (!gamepad1.right_bumper) slideManual = ButtonPressState.UNPRESSED;
//
//        if (gamepad1.left_bumper && slideManualUp == ButtonPressState.UNPRESSED) {
//            slideManualUp = ButtonPressState.PRESSED_GOOD;
//        } else if (gamepad1.left_bumper && slideManualUp==ButtonPressState.PRESSED_GOOD) {
//            slideManualUp = ButtonPressState.DEPRESSED;
//        } else if (!gamepad1.left_bumper) slideManualUp = ButtonPressState.UNPRESSED;

        if (gamepad1.left_bumper){//slideManual==ButtonPressState.PRESSED_GOOD) {
            slides.retract();
            telemetry.addLine("retracting");
        } else if (gamepad1.right_bumper){//slideManualUp==ButtonPressState.PRESSED_GOOD) {
            slides.extend();
        } else {
            slides.stop();
        }

        //DRIVE
        float x = gamepad2.left_stick_x;
        float y = gamepad2.left_stick_y;
        float turn = gamepad2.right_stick_x;
        move(x, -y, turn);

        switch (slideState) {
            case SLIDE_BOTTOM:
                if (Math.abs(slides.getEncoder() - 30) < 10) { // drop height
                    if (slideButton==ButtonPressState.PRESSED_GOOD) {
                        slideState = SlideState.SLIDE_HIGH;
                        telemetry.addData("pos", slides.getEncoder());
                        telemetry.update();
                        // slides.high();
                    }
                }
                break;
            case SLIDE_LOW:
                telemetry.addData("u shouldnt see this lol but encoder position", slides.getEncoder());
                telemetry.update();
//                if (Math.abs(slides.getEncoder() - 10) < 10) { // lowheight, changed 1400 something to 100
//                    if (gamepad1.left_trigger > 0.1f) {
//                        slides.tozero();
//                        intake.runIntake();
//                        slideState = SlideState.SLIDE_BOTTOM;
//                        telemetry.addData("pos", slides.getEncoder());
//                        telemetry.update();
//                    }
//                    if (gamepad1.right_trigger > 0.1f) {
//                        slides.middle();
//                        intake.stopIntake();
//                        slideState = SlideState.SLIDE_MEDIUM;
//                        telemetry.addData("pos", slides.getEncoder());
//                        telemetry.update();
//                    }
//                }
                break;
//            case SLIDE_MEDIUM:
//                if (Math.abs(slides.getEncoder() - 20) < 10) { // mid height 2424
//                    if (gamepad1.left_trigger > 0.1f) {
//                        slides.tozero();
//                        intake.runIntake();
//                        slideState = SlideState.SLIDE_BOTTOM;
//                        telemetry.addData("pos", slides.getEncoder());
//                        telemetry.update();
//                    }
//                    if (gamepad1.right_trigger > 0.1f) {
//                        slides.high();
//                        intake.stopIntake();
//                        slideState = SlideState.SLIDE_HIGH;
//                        telemetry.addData("pos", slides.getEncoder());
//                        telemetry.update();
//                    }
//                }
//                break;
            case SLIDE_HIGH:
                if (Math.abs(slides.getEncoder() - 30) < 10) { // high height 3481
                    if (slideButton==ButtonPressState.PRESSED_GOOD) {
                        slides.tozero();
                        slideState = SlideState.SLIDE_BOTTOM;
                        telemetry.addData("pos", slides.getEncoder());
                        telemetry.update();
                    }
                }
                break;
            default:
                slideState = SlideState.SLIDE_BOTTOM;
                telemetry.addLine("default");
                telemetry.update();
        }
        if(gamepad2.a){
            pullup.manualLeftUp();
        }
        if(gamepad2.x){
            pullup.manualRightUp();
        }
        if(gamepad2.y){
            pullup.manualRightDown();
        }
        if(gamepad2.b){
            pullup.manualLeftDown();
        }

        if(gamepad1.b){
            pullup.reachUp();
        }

//        switch (pullupstate) {
//            case NEUTRAL:
//                if (gamepad1.b) {
//                    pullup.reachUp();
//                    pullupstate = PullUpState.REACH_UP;
//                }
//                break;
//            case REACH_UP:
////                telemetry.addData("pullup1pos", + pullup.getPosition1());
////                telemetry.addData("pullup2pos", + pullup.getPosition2());
//                telemetry.update();
//                if (gamepad1.b) {
//                    pullup.manualLeftDown();
//                    pullup.manualRightDown();
//                    pullupstate = PullUpState.NEUTRAL;
//                }
//                break;
//            default:
//                pullupstate = PullUpState.NEUTRAL;
//        }

        if(gamepad1.dpad_left){
            latch.latchOn();
        }

        if(gamepad1.dpad_right){
            latch.latchOff();
        }



        //  ROTATING SLIDES
        if (gamepad1.right_trigger > 0.5) {
            slides.rotateRight();
            telemetry.addLine("rotator going up");
            telemetry.update();
        } else if (gamepad1.left_trigger > 0.5){
            slides.rotateLeft();
            telemetry.addLine("rotator going down");
            telemetry.update();
        } else {
            if (slides.getRotatorEncoder() >= 400){
                slowMode = true;
                slides.keepUp();
                telemetry.addLine("kept up");
                telemetry.update();
            } else {
                slides.stopRotator();
                slowMode = false;
            }


        }

        /* previous code
        if (gamepad1.b && (clawButton == ButtonPressState.UNPRESSED) && !clawState) {
            clawButton = ButtonPressState.PRESSED_GOOD;
            claw.open();
            clawState = true;

        } else if (gamepad1.x && (clawButton == ButtonPressState.UNPRESSED) && clawState) {
            clawButton = ButtonPressState.PRESSED_GOOD;
            claw.close();
            clawState = false;
        } else if (!(gamepad1.b) && (!(gamepad1.x) && (clawButton == ButtonPressState.PRESSED_GOOD))){
            clawButton = ButtonPressState.UNPRESSED;

        }
         */

        // improved code by yours truly:
        if (gamepad1.y && (clawButton == ButtonPressState.UNPRESSED)) {
            if (!clawState) { claw.open();  clawState = true; clawButton = ButtonPressState.PRESSED_GOOD;}
            else            { claw.close(); clawState = false; clawButton = ButtonPressState.PRESSED_GOOD;}

        } else if (!gamepad1.y && (clawButton == ButtonPressState.PRESSED_GOOD)) {
            clawButton = ButtonPressState.UNPRESSED;
        }


        if (gamepad1.a && (cRotatorButton == ButtonPressState.UNPRESSED) && !cRotatorAtDrop) {
            cRotatorButton = ButtonPressState.PRESSED_GOOD;
            clawRotator.toDrop();
            cRotatorAtDrop = true;
        } else if (gamepad1.a && (cRotatorButton == ButtonPressState.UNPRESSED) && cRotatorAtDrop) {
            cRotatorButton = ButtonPressState.PRESSED_GOOD;
            clawRotator.toPick();
            cRotatorAtDrop = false;
        } else if (!(gamepad1.a) && (cRotatorButton == ButtonPressState.PRESSED_GOOD)){
            cRotatorButton = ButtonPressState.UNPRESSED;
        }

//        if (gamepad1.y) {
//            clawRotator.toDrop();
//            cRotatorAtDrop = true;
//        } else if (gamepad1.a) {
//            clawRotator.toPick();
//            cRotatorAtDrop = false;
//        }
        // THE ULTIMATE BUTTON
        if (gamepad1.dpad_up) {

            //retract slides
            //rotate slides
            //latch on
            //extend slides
            Runnable slidesRunnable = new Runnable() {
                @Override
                public void run() {
                    while (slides.getEncoder() <= -100){
                        slides.retract();
                    }
                    //TODO: tune
                    while (slides.getRotatorEncoder() <= 470){
                        slides.rotateRight();
                    }

                    latch.latchOn();

                    while (slides.getEncoder() >= -3000){
                        slides.extend();
                    }
                }
            };
            Thread slidesThread = new Thread(slidesRunnable);
            slidesThread.start();


        } else if (gamepad1.dpad_down) {

            //retract slides
            //rotate slides
            //latch off
            //extend slides
            Runnable slidesRunnable = new Runnable() {
                @Override
                public void run() {
                    while (slides.getEncoder() <= -100){
                        slides.retract();
                    }
                    //TODO: tune
                    while (slides.getRotatorEncoder() >= 200){
                        slides.rotateLeft();
                    }

                    latch.latchOn();

                    while (slides.getEncoder() >= -300){
                        slides.extend();
                    }
                }
            };
            Thread slidesThread = new Thread(slidesRunnable);
            slidesThread.start();


        }
    }


    public void move(float x, float y, float turn) {
        // if the stick movement is negligible, set STICK_MARGIN to 0
        if (Math.abs(x) <= STICK_MARGIN) x = .0f;
        if (Math.abs(y) <= STICK_MARGIN) y = .0f;
        if (Math.abs(turn) <= STICK_MARGIN) turn = .0f;

        //Notation of a ? b : c means if a is true do b, else do c.
        double multiplier = normalPower;
        drive.move(-x * multiplier, y * multiplier, turn * multiplier);
    }


}
