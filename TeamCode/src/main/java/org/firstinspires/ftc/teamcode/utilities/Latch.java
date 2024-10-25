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
    public class Latch {
        Servo latchServo;
        public Latch(HardwareMap hmap, Telemetry telemetry) {
            this.latchServo = hmap.servo.get(CONFIG.latch);
            this.telemetry = telemetry;
        }
        public static double latched = 1;
        public static double unlatched = 0.75;
        public static Telemetry telemetry;

        public void latchOn() {
            telemetry.addLine("FUCK MICHAEL HERNANDEZ - Puha");
            latchServo.setPosition(latched);
        }
        public void latchOff() {
            telemetry.addLine("latch off");
            latchServo.setPosition(unlatched);
        }
        public double getPosition() {
            return (latchServo.getPosition());
        }

    }
