package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

//NEGATIVE IS UP
//Code credited to Shawn Mendes the handsome ;)
import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utilities.SlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlidesRotatorState;

public class Slides {

    // to go up
    //Looking from front, Left (acc on the right in this view), must go clockwise
    // Right (Acc on the left in this view), must go counter clockwise
    // Positive power is counter clockwise,

    //position at initial
    int pos; //up and down motor position
    int rot; // left and right rotator position

    //Current state of slide. 0 - idle, 1 - up, 2 - down
    //TODO: consider using an enum
    SlidesState state;
    SlidesRotatorState rotState;

    // limits
    int maxheight = -3200; // 3481
    int minheight = -100;

    // basket heights
    // TODO: tune these values
    int highheight = -2900;

    int lowheight = -1500;

    // rotator limits
    int uprot = 100; // 3481 proviously 25
    int downrot = 100; // 3481 proviously 25

    //assign motors to slide motors and slide rotator motors
    DcMotor slideMotor;
    DcMotor slideRotator;

    public Slides(HardwareMap hmap){
        this.slideMotor = hmap.dcMotor.get(CONFIG.slide);
        this.slideRotator = hmap.dcMotor.get(CONFIG.slideRot);
        this.pos = 0;
        this.state = SlidesState.LEFT;

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor.setZeroPowerBehavior(BRAKE);

        slideRotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideRotator.setZeroPowerBehavior(BRAKE);
    }

    //stop motors
    public void stop(){
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(0);

        pos = getEncoder();

        this.state = SlidesState.STOP;
    }
    public void stopRotator(){
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setRotPower(0);

        pos = getRotatorEncoder();

        this.rotState = SlidesRotatorState.STOP;
    }

    //is busy
    public boolean isBusy() {return slideMotor.isBusy();}
    public boolean rotatorIsBusy() {return slideRotator.isBusy();}
    //set target
    public void setTarget(int target){
        slideMotor.setTargetPosition(-target);
    }
    public void setRotTarget(int target){
        slideRotator.setTargetPosition(-target);
    }

    //reset
    public void reset(){
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        pos = 0;
        this.state = SlidesState.STOP;

        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void resetRotator(){
        slideRotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rot = 0;
        this.rotState = SlidesRotatorState.STOP;

        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    //run to position


    public static int slideMaxNum = -3200;
    public static int slideMinNum = -100;
//
//    public void runToPosUP(){
//        int targetPosition = slideUPNum;
//        slideMotor.setTargetPosition(targetPosition);
//    }
//
//    public void runtoPosDOWN(){
//        int targetPosition = slideDOWNNum;
//        slideMotor.setTargetPosition(targetPosition);
//    }


    public void runToPosition(){
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(0.9);

    }

    public void runRotToPosition(){
        slideRotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(0.9);
    }

    public void tozero() {
        setTarget(slideMinNum);
        runToPosition();
        pos = getEncoder();
    }

    public void slideMaxHeight(){
        setTarget(slideMaxNum);
        runToPosition();
        pos = getEncoder();
    }

    public void slideMinHeight(){
        setTarget(slideMinNum);
        runToPosition();
        pos = getEncoder();
    }

    // BASKET PRESETS

    public void lowBasket(){
        setTarget(lowheight);
        runToPosition();
        pos = getEncoder();
    }

    public void highBasket(){
        setTarget(highheight);
        runToPosition();
        pos = getEncoder();
    }

    // ROTATOR PRESETS

    public void up(){
        setRotTarget(uprot);
        runRotToPosition();
        rot = getRotatorEncoder();
    }
    public void down(){
        setRotTarget(downrot);
        runRotToPosition();
        rot = getRotatorEncoder();
    }

    // SLIDES MANUAL

    public void extend(){
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // max limit
//        if (pos <= maxheight){
//            setPower(0);
//            return;
//        }
//        if (state == SlidesState.LEFT && pos <= -2200){
//            setPower(-4);
//            pos = getEncoder();
//            return;
//        }
//        if (state == SlidesState.LEFT){
//            return;
//        }
        state = SlidesState.LEFT;
        setPower(-7);
    }


    public void retract() {
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        state = SlidesState.RIGHT;
        setPower(5);
    }
    public class Retract implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            retract();
            return false;
        }
    }
    public Action retractAction() {  return new Slides.Retract();  }


    // ROTATOR (rotater? rotator.) MANUAL
    public void keepUp() {
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setRotPower(2);

    }
    public void keepDown() {
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setRotPower(-2);
    }


    public void rotateLeft(){ //slide rotates down
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rotState = SlidesRotatorState.LEFT;
        setRotPower(8);
    }
    public class RotateLeft implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            rotateLeft();
            return false;
        }
    }
    public Action rotateLeftAction() {  return new Slides.RotateLeft();  }


    //TODO: add rotator limit @ 400
    public void rotateRight() { // slide rotates up
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rotState = SlidesRotatorState.RIGHT;
        setRotPower(-10);
    }
    public class RotateRight implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            rotateRight();
            return false;
        }
    }
    public Action rotateRightAction() {  return new Slides.RotateRight();  }


    public class SlideAction implements Action {
        SlidesState slidesStateAction;
        SlidesRotatorState rotStateAction;

        public SlideAction(SlidesState slidesState, SlidesRotatorState rotState) {
            this.slidesStateAction = slidesState;
            this.rotStateAction = rotState;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            state = this.slidesStateAction;
            rotState = this.rotStateAction;

            return false;
        }
    }

    public Action getSlidesAction(SlidesState slidesState, SlidesRotatorState slidesRotatorState) {
        return new SlideAction(slidesState, slidesRotatorState);
    }

    public int getEncoder() {
        return -slideMotor.getCurrentPosition();
    }
    public int getRotatorEncoder() {
        return -slideRotator.getCurrentPosition();
    }
    //slide rotator code from now on
    public int getPosition1(){
        return slideRotator.getCurrentPosition();
    }
    //getting target (idk what that is)
    public int getTarget() {
        return slideMotor.getTargetPosition();
    }
    public int getRotTarget() {
        return slideRotator.getTargetPosition();
    }
    //get runmode?
    public DcMotor.RunMode getMode(){
        return slideMotor.getMode();
    }

    //set power
    public void setPower(double power){
        slideMotor.setPower(-0.95*power); // constant removed
    }
    public void setRotPower(double power){
        slideRotator.setPower(-0.10*power); // constant removed
    }
}