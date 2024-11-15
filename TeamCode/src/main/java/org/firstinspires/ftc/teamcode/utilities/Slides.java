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
    int pos; //up and down motor position
    int rot; // left and right rotator position

    //Current state of slide. 0 - idle, 1 - up, 2 - down
    //TODO: consider using an enum
    int state;
    int rotState;

    // limits
    int maxheight = -3000; // 3481
    int minheight = -100;

    // basket heights
    // TODO: tune these values
    int highheight = -2900;

    int lowheight = -1500;

    // rotator limits
    int uprot = 100; // 3481 proviously 25
    int downrot = 100; // 3481 proviously 25

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
        telemetry.addLine("extending");
        telemetry.addLine(String.valueOf(pos));

        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();
        // max limit
        if (pos <= maxheight){
            setPower(0);
            return;
        }
        if (state == 1 && pos <= -2200){
            setPower(-4);
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
        telemetry.addLine("retracting");
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();
        telemetry.addLine(String.valueOf(pos));

//        if (pos < -0.2){
//            setPower(0);
//            return;
//        }

        // slower retract closer down
        if (pos >= minheight){
            setPower(0);
            pos = getEncoder();
            return;
        }
        if (state == 2 && pos >= -900) {
            setPower(1);
            pos = getEncoder();
            return;
        }

        if (state == 2) {
            return;
        }

        state = 2;
        setPower(3);

    }

    // ROTATOR (rotater? rotator.) MANUAL

    public void rotateRight(){ //slide rotates outwards (up)
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rot = getRotatorEncoder();
        telemetry.addLine(String.valueOf(rot));

        //id
        if (rotState == 1 && rot >= 200){
            setRotPower(5);
            rot = getRotatorEncoder();
            return;
        }
        if (rotState == 1){
            return;
        }
        rotState = 1;
        setRotPower(10);
    }
    //TODO: add rotator limit @ 400

    public void rotateLeft() { // slide rotates inwards (down)
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rot = getRotatorEncoder();
        telemetry.addLine(String.valueOf(rot));

        if  (rotState == 2 && rot <= 200) {
            setRotPower(-4);
            rot = getRotatorEncoder();
            return;
        }
        if (rotState == 2) {
            return;
        }
        rotState = 2;
        setRotPower(-9);
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