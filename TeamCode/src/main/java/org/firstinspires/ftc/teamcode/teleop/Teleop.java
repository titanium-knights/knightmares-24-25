package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
import org.firstinspires.ftc.teamcode.utilities.Slides;
//import org.firstinspires.ftc.teamcode.utilities.PullUp;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.Latch;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Config
@TeleOp(name="DriveTrain Teleop")
public class Teleop extends OpMode {
    Slides slides;
    // PullUp pullup;
    Claw claw;
    Latch latch;
    ClawRotator clawRotator;
    // ClawIteration2 claw;
    SimpleMecanumDrive drive;

    //Set normal power constant to 1, no point in slowing the robot down
    final double normalPower = 1;

    // in case of joystick drift, ignore very small values
    final float STICK_MARGIN = 0.5f;

    public boolean clawState = false;
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
    };
    public enum RotatorState {
        SLIDE_DOWN,
        SLIDE_UP,
        SLIDE_STOP
    };
    SlideState slideState = SlideState.SLIDE_BOTTOM;
    RotatorState rotatorState = RotatorState.SLIDE_DOWN;

    public enum PullUpState {
        NEUTRAL,
        REACH_UP
    }
    PullUpState pullupstate = PullUpState.REACH_UP;

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

    // boolean intakeRunning = false;
    @Override
    public void init() {
        this.drive = new SimpleMecanumDrive(hardwareMap);
        this.slides = new Slides(hardwareMap, telemetry);
        // this.pullup = new PullUp(hardwareMap);
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
        float x = gamepad1.left_stick_x;
        float y = gamepad1.left_stick_y;
        float turn = gamepad1.right_stick_x;
        move(x, -y, turn);

//        switch (bayState) {
//            case OPENED:
//                if (Math.abs(bay.getPosition() - Bay.openPosLeft) < 0.05 && baybutton==ButtonPressState.PRESSED_GOOD) {
//                    bay.close();
//                    bayState = BayState.CLOSED;
//                    telemetry.addLine("bayState" + bayState);
//                    telemetry.update();
//                    telemetry.addLine("pos " + pos);
//                    telemetry.update();
//                }
//                telemetry.addLine("OPENED");
//                telemetry.update();
//                break;
//
//            case CLOSED:
//                if (Math.abs(bay.getPosition() - Bay.closedPosLeft) < 0.05 && baybutton==ButtonPressState.PRESSED_GOOD) {
//                    bay.open();
//                    bayState = BayState.OPENED;
//                    telemetry.addLine("bayState" + bayState);
//                    telemetry.update();
//                    telemetry.addLine("pos " + pos);
//                    telemetry.update();
//                }
//
//                double position = Math.abs(bay.getPosition() - 0);
//                telemetry.addLine("position " + position);
//                telemetry.addLine("pressed? " + baybutton.toString());
//                telemetry.update();
//                break;
//            default:
//                bayState = BayState.CLOSED;
//                telemetry.addLine("bayState" + bayState);
//                telemetry.update();
//        }

        // SLIDES & INTAKE
//        switch (rotatorState) {
//            case SLIDE_DOWN:
//                if (Math.abs(slides.getRotatorEncoder() - 0) < 10) {
//                    if (rotatorButton == ButtonPressState.PRESSED_GOOD) {
//                        rotatorState = RotatorState.SLIDE_UP;
//                        telemetry.addData("rot", slides.getRotatorEncoder());
//                        telemetry.update();
//                        slides.up();
//                    }
//                }
//            case SLIDE_UP:
//                if (Math.abs(slides.getRotatorEncoder() - 30) < 10) {
//                    if (rotatorButton == ButtonPressState.PRESSED_GOOD) {
//                        rotatorState = RotatorState.SLIDE_DOWN;
//                        telemetry.addData("rot", slides.getRotatorEncoder());
//                        telemetry.update();
//                        slides.down();
//                    }
//                }
//            default:
//                rotatorState = RotatorState.SLIDE_DOWN;
//                telemetry.addLine("default");
//                telemetry.update();
//        }

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

//        switch (pullupstate) {
//            case NEUTRAL:
//                if (gamepad1.x && gamepad1.dpad_up) {
//                    pullup.manualLeftUp();
//                    pullup.manualRightUp();
//                    pullupstate = PullUpState.REACH_UP;
//                }
//                break;
//            case REACH_UP:
//                telemetry.addData("pullup1pos", + pullup.getPosition1());
//                telemetry.addData("pullup2pos", + pullup.getPosition2());
//                telemetry.update();
//                if (gamepad1.dpad_up) {
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
        // open/close dpad
        //if (gamepad1.dpad_left) {
            //telemetry.addLine("claw close");
            //telemetry.update();  
            //claw.close();
        //}
        //if (gamepad1.dpad_right) {
            //claw.open();
            //telemetry.addLine("claw open");
            //telemetry.update();
        //}

        // Claw tilt left/right dpad
        //if (gamepad1.dpad_up) {
            //telemetry.addLine("claw tilt forward");
            //telemetry.update();
            //telemetry.addLine(claw.toString());
            //claw.tiltForward();
        //}
        //if (gamepad1.dpad_down) {
            //telemetry.addLine("claw titl backwards");
            //telemetry.update();
            //claw.tiltBack();
        //}

        //  ROTATING SLIDES
        if (gamepad1.right_trigger > 0.5) {
            // going up
            // LATCH ON

//            telemetry.addLine("slides rotate right");
//            telemetry.update();
            slides.rotateRight();
        } else if (gamepad1.left_trigger > 0.5){
            // going down
            // LATCH OFF
//            telemetry.addLine("slides rotate left");
//            telemetry.update();
            slides.rotateLeft();
        } else {
            slides.stopRotator();
        }

        if (gamepad1.b && (clawButton == ButtonPressState.UNPRESSED) && !clawState) {
            clawButton = ButtonPressState.PRESSED_GOOD;
            claw.open();
            clawState = true;
            telemetry.addLine("claw open");
            telemetry.update();

        } else if (gamepad1.b && (clawButton == ButtonPressState.UNPRESSED) && clawState) {
            clawButton = ButtonPressState.PRESSED_GOOD;
            claw.close();
            clawState = false;
            telemetry.addLine("claw closed");
            telemetry.update();
        } else if (!(gamepad1.b) && (clawButton == ButtonPressState.PRESSED_GOOD)){
            clawButton = ButtonPressState.UNPRESSED;

        }
        if (gamepad1.y && (cRotatorButton == ButtonPressState.UNPRESSED) && !cRotatorAtDrop) {
            cRotatorButton = ButtonPressState.PRESSED_GOOD;
            clawRotator.toDrop();
            cRotatorAtDrop = true;
            telemetry.addLine("claw drop");
            telemetry.update();
        } else if (gamepad1.y && (cRotatorButton == ButtonPressState.UNPRESSED) && cRotatorAtDrop) {
            cRotatorButton = ButtonPressState.PRESSED_GOOD;
            clawRotator.toPick();
            cRotatorAtDrop = false;
            telemetry.addLine("claw pick");
            telemetry.update();
        } else if (!(gamepad1.y) && (cRotatorButton == ButtonPressState.PRESSED_GOOD)){
            cRotatorButton = ButtonPressState.UNPRESSED;
        }

        if (gamepad1.y) {
            clawRotator.toDrop();
            cRotatorAtDrop = true;
        } else if (gamepad1.a) {
            clawRotator.toPick();
            cRotatorAtDrop = false;
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
