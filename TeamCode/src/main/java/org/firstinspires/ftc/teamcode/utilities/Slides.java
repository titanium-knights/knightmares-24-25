package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

//NEGATIVE IS UP
import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slides {

    // to go up
    //Looking from front, Left (acc on the right in this view), must go clockwise
    // Right (Acc on the left in this view), must go counter clockwise
    // Positive power is counter clockwise,

    //position at initial
    int pos;
    //Current state of slide. 0 - idle, 1 - up, 2 - down
    //TODO: consider using an enum
    int state;

    // Preset heights,
    // TODO: CALIBRATE
    int maxheight = 25; // 3481

    int midheight = 20; // 2424
    int lowheight = 10; // 1472
    int dropheight = 800;

    public Slides(HardwareMap hmap){
        this.slideMotor = hmap.dcMotor.get(CONFIG.slide);
        this.state = this.pos = 0;

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        slideMotor.setZeroPowerBehavior(BRAKE);
    }
    DcMotor slideMotor;

    public int getEncoder(){
        return -slideMotor.getCurrentPosition();
    }

    public int getTarget(){
        return slideMotor.getTargetPosition();
    }
    public DcMotor.RunMode getMode(){
        return slideMotor.getMode();
    }

    public void setPower(double power){
        slideMotor.setPower(-0.95*power); // constant removed
    }

    public void stop(){
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(0);

        pos = getEncoder();

        state = 0;
    }

    public boolean isBusy() {return slideMotor.isBusy();}

    public void setTarget(int target){
        slideMotor.setTargetPosition(-target);
    }

    public void reset(){
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        pos = 0;
        state = 0;

        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void runToPosition(){
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(0.9);

    }
    public void todrop(){
        setTarget(dropheight);
        runToPosition();
    }

    public void tozero(){
        setTarget(0);
        runToPosition();
        pos = getEncoder();
    }

    public void low(){
        setTarget(lowheight);
        runToPosition();
        pos = getEncoder();
    }

    public void middle(){
        setTarget(midheight);
        runToPosition();
        pos = getEncoder();
    }
    public void high(){
        setTarget(maxheight);
        runToPosition();
        pos = getEncoder();
    }

    public void upHold(){
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();
        if (pos >= maxheight){
            setPower(0);
            return;
        }
        if (state == 1 && pos >= midheight + 15){
            setPower(1);
            pos = getEncoder();
            return;
        }
        if (state == 1){
            return;
        }
        state = 1;
        setPower(1);
    }

    public void downHold() {
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();

        if (state == 2 && pos <= 1800) {
            setPower(-0.4);
            pos = getEncoder();
            return;
        }

        if (state == 2) {
            return;
        }
        state = 2;
        setPower(-0.6);

    }
}