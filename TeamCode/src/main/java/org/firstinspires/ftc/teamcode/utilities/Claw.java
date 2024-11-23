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

        Servo clawServo;

        // TODO: TUNE VALUES !!

        public Claw(HardwareMap hmap, Telemetry telemetry) {
            this.clawServo = hmap.servo.get(CONFIG.claw);
            this.telemetry = telemetry;
            this.clawServo.setDirection(Servo.Direction.FORWARD);
        }

        public static double closedPos = 0.5f;
        public static double openPos = 0.75f;


        public Telemetry telemetry;

        public void open() {
            telemetry.addLine("open claw");
            clawServo.setPosition(openPos);
        }
        public void close() {
            telemetry.addLine("close claw");
            clawServo.setPosition(closedPos);
        }

        public double getPosition() {
            return (clawServo.getPosition());
        }
    }