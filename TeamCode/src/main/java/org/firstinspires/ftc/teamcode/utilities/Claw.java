package org.firstinspires.ftc.teamcode.utilities;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

//:)

import org.firstinspires.ftc.robotcore.external.Telemetry;


@Config
    public class Claw {

        Servo clawServo;

        // TODO: TUNE VALUES !!

        public Claw(HardwareMap hmap) {
            this.clawServo = hmap.servo.get(CONFIG.claw);
            this.clawServo.setDirection(Servo.Direction.FORWARD);
        }

        public static double closedPos = 0.48f;
        public static double openPos = 0.65f;


        public void open() {
            clawServo.setPosition(openPos);
        }

        public class OpenClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                open();
                return false;
            }
        }

        public Action openAction() {  return new OpenClaw();  }

        public void close() {
            clawServo.setPosition(closedPos);
        }

        public class CloseClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                close();
                return false;
            }
        }

        public Action closeAction() {  return new CloseClaw();  }

        public double getPosition() {
            return (clawServo.getPosition());
        }

        //claw close

}