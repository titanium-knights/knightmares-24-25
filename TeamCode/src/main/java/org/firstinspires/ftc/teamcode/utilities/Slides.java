package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

//NEGATIVE IS UP
//Code credited to Shawn Mendes the handsome ;)
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Slides {

    // to go up
    //Looking from front, Left (acc on the right in this view), must go clockwise
    // Right (Acc on the left in this view), must go counter clockwise
    // Positive power is counter clockwise,

    //position at initial
    int pos;//up and down motor position
    int rot; // left and right rotator position

    //Current state of slide. 0 - idle, 1 - up, 2 - down
    //TODO: consider using an enum
    int state;
    int rotState;

    // Preset heights,
    // TODO: CALIBRATE

    // limits
    int maxheight = 25; // 3481
    //int minheight = 5;

    // bar heights
    int highheight = 22; // for high bar

    int lowheight = 10; // 1472 for low bar

    int startheight = 6;

    int maxrot = 100; // 3481 proviously 25
    int midrot = 20; // 2424
    int lowrot = 10; // 1472
    public static Telemetry telemetry;

    //assign motors to slide motors and slide rotator motors
    DcMotor slideMotor;
    DcMotor slideRotator;

    public Slides(HardwareMap hmap, Telemetry telemetry){
        this.slideMotor = hmap.dcMotor.get(CONFIG.slide);
        this.slideRotator = hmap.dcMotor.get(CONFIG.slideRot);
        this.state = this.pos = 0;
        this.telemetry = telemetry;

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor.setZeroPowerBehavior(BRAKE);

        slideRotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideRotator.setZeroPowerBehavior(BRAKE);
    }

    public void startPosition() {

        setTarget(startheight);
        runToPosition();
        telemetry.addLine("at start height");
    }

    //stop motors
    public void stop(){
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(0);

        pos = getEncoder();

        state = 0;
    }
    public void stopRotator(){
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setRotPower(0);

        pos = getRotatorEncoder();

        rotState = 0;
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
        state = 0;

        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void resetRotator(){
        slideRotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rot = 0;
        rotState = 0;

        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    //run to position
    public void runToPosition(){
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(0.9);
    }

    public void runRotToPosition(){
        slideRotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(0.9);

    }

    public void tozero() {
        setTarget(0);
        runToPosition();
        pos = getEncoder();
    }

    public void lowBar(){
        setTarget(lowheight);
        runToPosition();
        pos = getEncoder();
    }

    public void highBar(){
        setTarget(highheight);
        runToPosition();
        pos = getEncoder();
    }
//
//    public void up(){
//        setRotTarget(maxrot);
//        runRotToPosition();
//        rot = getRotatorEncoder();
//    }
//    public void down(){
//        setRotTarget(lowrot);
//        runRotToPosition();
//        rot = getRotatorEncoder();
//    }

    public void extend(){
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();
        telemetry.addLine(String.valueOf(pos));
        // max limit
//        if (pos >= maxheight){
//            setPower(0);
//            return;
//        }
        if (state == 1 && pos >= 35){
            setPower(-3);
            pos = getEncoder();
            return;
        }
        if (state == 1){
            return;
        }
        state = 1;
        setPower(-5);
    }
    public void retract() {
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();
        telemetry.addLine(String.valueOf(pos));

//        if (pos < minheight){
//            setPower(0);
//            return;
//        }

        // slower retract closer down
        if (state == 2 && pos <= 1800) {
            setPower(3);
            pos = getEncoder();
            return;
        }

        if (state == 2) {
            return;
        }

        state = 2;
        setPower(5);

    }
    public void rotateRight(){
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getRotatorEncoder();

        // rotate limits
        if (rot >= maxrot) {
            setRotPower(0);
            return;
        }
        //
        if (rotState == 1 && rot >= midrot + 15){
            setRotPower(8);
            rot = getRotatorEncoder();
            return;
        }
        if (rotState == 1){
            return;
        }
        rotState = 1;
        setRotPower(5);
    }
    public void rotateLeft() {
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rot = getRotatorEncoder();

        if (rot <= 1800) { //if (rotState == 2 && rot <= 1800) {
            setRotPower(-0.4);
            rot = getRotatorEncoder();
            return;
        }
        if (rotState == 2) {
            return;
        }
        rotState = 2;
        setRotPower(-0.6);
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