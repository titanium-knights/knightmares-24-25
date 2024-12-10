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

        if (gamepad1.left_trigger>0.1f && slideButton == ButtonPressState.UNPRESSED) {
            slideButton = ButtonPressState.PRESSED_GOOD;
        } else if (gamepad1.left_trigger>0.1f && slideButton==ButtonPressState.PRESSED_GOOD) {
            slideButton = ButtonPressState.DEPRESSED;
        } else if (gamepad1.left_trigger<0.1f) slideButton = ButtonPressState.UNPRESSED;

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

        if(gamepad2.a){
            pullup.rightDown();
            pullup.leftDown();

        }

        if(gamepad2.y){
            pullup.rightUp();
            pullup.leftUp();
        }

        if(gamepad1.dpad_left){
            latch.latchOn();
        }

        if(gamepad1.dpad_right){
            latch.latchOff();
        }

        //  ROTATING SLIDES
        if (gamepad1.right_trigger > 0.5) {
            slides.rotateRight();

        } else if (gamepad1.left_trigger > 0.5){
            slides.rotateLeft();
            telemetry.addLine("rotator going down");
            telemetry.update();
        } else {
            slides.stopRotator();
        }
        if (slides.getEncoder() <= -2200){
            slowMode = true;
            slides.keepUp();
            telemetry.addLine("kept up");
            telemetry.update();
        } else {
            slowMode = false;
            telemetry.update();
    }

        // improved code by yours truly:
        if (gamepad1.y && (clawButton == ButtonPressState.UNPRESSED)) {
            if (!clawOpen) { claw.open();  clawOpen = true; clawButton = ButtonPressState.PRESSED_GOOD;}
            else            { claw.close(); clawOpen = false; clawButton = ButtonPressState.PRESSED_GOOD;}

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

        // THE ULTIMATE BUTTON
        if (gamepad1.dpad_up) {

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
        drive.move(-x * multiplier, y * multiplier, turn * multiplier);
    }

}
