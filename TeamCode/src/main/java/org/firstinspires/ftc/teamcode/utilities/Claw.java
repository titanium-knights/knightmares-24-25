package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DistanceSensor;

//:)

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@Config
public class Claw {
    Servo clawLeft;
    Servo clawRight;
    Servo clawTilt;

    // TODO: TUNE VALUES !!
    public static double closedPosLeft = 0.25;
    public static double openPosLeft = 1;
    public static double closedPosRight = 1;
    public static double openPosRight = 0.25;

    public static double forwardPos = 0.25;
    public static double backwardPos = 1;

    public static Telemetry telemetry;
    
    

    public Claw(HardwareMap hmap, Telemetry telemetry) {
        this.clawLeft = hmap.servo.get(CONFIG.clawLeft);
        this.clawRight = hmap.servo.get(CONFIG.clawRight);
        this.clawTilt = hmap.servo.get(CONFIG.clawTilt);
        this.telemetry = telemetry;
    }
    
    public void open() {
        telemetry.addLine("open claw");
        clawLeft.setPosition(openPosLeft);
        clawRight.setPosition(openPosRight);
    }
    public void close() {
        telemetry.addLine("close claw");
        clawLeft.setPosition(closedPosLeft);
        clawRight.setPosition(closedPosRight);
    }
    
    public void tiltForward() {
        telemetry.addLine("tilt forward");
        clawTilt.setPosition(forwardPos);
    }
    public void tiltBack() {
        telemetry.addLine("tilt back");
        clawTilt.setPosition(backwardPos);
    }
    
    public double getPosition() {
        return (clawLeft.getPosition() + clawRight.getPosition()) / 2.0;
    }
}