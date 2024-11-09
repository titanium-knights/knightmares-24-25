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
    public class ClawRotator {
        Servo cRotatorServo;
        public ClawRotator(HardwareMap hmap, Telemetry telemetry) {
            this.cRotatorServo = hmap.servo.get(CONFIG.clawRotator);
            this.telemetry = telemetry;
        }
        public static double dropPos = 0.30; //-0.70
        public static double pickPos = 0.1;

        public static Telemetry telemetry;


        public void toDrop() {
            telemetry.addLine("claw to drop");
            cRotatorServo.setPosition(dropPos);
        }
        public void toPick() {
            telemetry.addLine("claw to pick");
            cRotatorServo.setPosition(pickPos);
        }
        public double getPosition() {
            return (cRotatorServo.getPosition());
        }

    }
