package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class ClawIteration2 {

    int pos;



    public static DcMotor clawMotor;

    public ClawIteration2(HardwareMap hmap, Telemetry telemetry) {
        this.clawMotor = hmap.dcMotor.get(CONFIG.clawMotor);

        clawMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        clawMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        clawMotor.setZeroPowerBehavior(BRAKE);

    }

    DcMotor clawRotator;

    public int getEncoder() {
        return -clawMotor.getCurrentPosition();
    }

    public DcMotor.RunMode getMode() {
        return clawMotor.getMode();
    }

    public static void setPower(double power) {
        clawMotor.setPower(-0.95 * power);
    }

    public void stop() {
        clawMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(0);

        pos = getEncoder();


    }
    public boolean isBusy() {
        return clawMotor.isBusy();
    }

    public void setTarget(int target){
        clawMotor.setTargetPosition(-target);
    }

    public void reset(){
        clawMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        pos = 0;


        clawMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


  public void inside(){
    clawMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    setPower(1);
    }

    public void outside(){
        clawMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(-1);

        //rn crying over loosing two code
    }

}


