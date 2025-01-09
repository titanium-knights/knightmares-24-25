package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.HashMap;
import java.util.Objects;

public class SimpleMecanumDrive {

    public SimpleMecanumDrive(HardwareMap hmap) {
        fl = hmap.get(DcMotor.class, CONFIG.FRONT_LEFT);
        fr = hmap.get(DcMotor.class, CONFIG.FRONT_RIGHT);
        bl = hmap.get(DcMotor.class, CONFIG.BACK_LEFT);
        br = hmap.get(DcMotor.class, CONFIG.BACK_RIGHT);

        initMotor(fl, DcMotorSimple.Direction.FORWARD, new double[]{1,1});
        initMotor(bl, DcMotorSimple.Direction.FORWARD, new double[]{1,-1});
        initMotor(fr, DcMotorSimple.Direction.FORWARD, new double[]{-1,1});
        initMotor(br, DcMotorSimple.Direction.FORWARD, new double[]{-1,-1});
/*
        fl.setDirection(DcMotorSimple.Direction.FORWARD);
        bl.setDirection(DcMotorSimple.Direction.FORWARD);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
*/
/*
        directions.put(fl, new double[]{1, 1});
        directions.put(bl, new double[]{1, -1});
        directions.put(fr, new double[]{-1, 1});
        directions.put(br, new double[]{-1, -1});
 */
    }
    public void initMotor(
            DcMotor motor,
            DcMotorSimple.Direction direction,   // delete if not awesome
            double[] doubles                     // ^^^ same here ^^^
    )
    {
        // minimizes redundant and/or repetitive code
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(BRAKE);

        // might work, might not work. we shall see
        motor.setDirection(direction); // idk about this. perchance it works
        directions.put(motor, doubles);
    }
    public static DcMotor fl, fr, bl, br;

    public static HashMap<DcMotor, double[]> directions = new HashMap<>();

    public void move(double x, double y, double turn) {

        // dot of fl and br
        double dot_fl = dot(Objects.requireNonNull(directions.get(fl)), new double[]{x, y}) + turn;
        double dot_fr = (dot(Objects.requireNonNull(directions.get(fr)), new double[]{x, y}) + turn);
        double dot_bl = dot(Objects.requireNonNull(directions.get(bl)), new double[]{x, y}) - turn;
        double dot_br = dot(Objects.requireNonNull(directions.get(br)), new double[]{x, y}) - turn;

        double max = Math.max(1, Math.max(Math.max(Math.abs(dot_fl), Math.abs(dot_fr)), Math.max(Math.abs(dot_bl), Math.abs(dot_br))));
        fl.setPower(dot_fl / max);
        br.setPower(dot_br / max);
        fr.setPower(dot_fr / max);
        bl.setPower(dot_bl / max);
    }
//    public void moveFL(double x, double y, double turn) {
//        double dot_fl = dot(Objects.requireNonNull(directions.get(fl)), new double[]{x, y}) + turn;
//        double dot_fr = (dot(Objects.requireNonNull(directions.get(fr)), new double[]{x, y}) + turn);
//        double dot_bl = dot(Objects.requireNonNull(directions.get(bl)), new double[]{x, y}) - turn;
//        double dot_br = dot(Objects.requireNonNull(directions.get(br)), new double[]{x, y}) - turn;
//
//        double max = Math.max(1, Math.max(Math.max(Math.abs(dot_fl), Math.abs(dot_fr)), Math.max(Math.abs(dot_bl), Math.abs(dot_br))));
//        fl.setPower(dot_fl / max);
//        br.setPower(0);
//        fr.setPower(0);
//        bl.setPower(0);
//    }
//    public void moveFR(double x, double y, double turn) {
//        double dot_fl = dot(Objects.requireNonNull(directions.get(fl)), new double[]{x, y}) + turn;
//        double dot_fr = (dot(Objects.requireNonNull(directions.get(fr)), new double[]{x, y}) + turn);
//        double dot_bl = dot(Objects.requireNonNull(directions.get(bl)), new double[]{x, y}) - turn;
//        double dot_br = dot(Objects.requireNonNull(directions.get(br)), new double[]{x, y}) - turn;
//
//        double max = Math.max(1, Math.max(Math.max(Math.abs(dot_fl), Math.abs(dot_fr)), Math.max(Math.abs(dot_bl), Math.abs(dot_br))));
//        fl.setPower(0);
//        br.setPower(0);
//        fr.setPower(dot_fr / max);
//        bl.setPower(0);
//    }
//
//    public void moveBL(double x, double y, double turn) {
//        double dot_fl = dot(Objects.requireNonNull(directions.get(fl)), new double[]{x, y}) + turn;
//        double dot_fr = (dot(Objects.requireNonNull(directions.get(fr)), new double[]{x, y}) + turn);
//        double dot_bl = dot(Objects.requireNonNull(directions.get(bl)), new double[]{x, y}) - turn;
//        double dot_br = dot(Objects.requireNonNull(directions.get(br)), new double[]{x, y}) - turn;
//
//        double max = Math.max(1, Math.max(Math.max(Math.abs(dot_fl), Math.abs(dot_fr)), Math.max(Math.abs(dot_bl), Math.abs(dot_br))));
//        fl.setPower(0);
//        br.setPower(0);
//        fr.setPower(0);
//        bl.setPower(dot_bl / max);
//    }
//
//    public void moveBR(double x, double y, double turn) {
//        double dot_fl = dot(Objects.requireNonNull(directions.get(fl)), new double[]{x, y}) + turn;
//        double dot_fr = (dot(Objects.requireNonNull(directions.get(fr)), new double[]{x, y}) + turn);
//        double dot_bl = dot(Objects.requireNonNull(directions.get(bl)), new double[]{x, y}) - turn;
//        double dot_br = dot(Objects.requireNonNull(directions.get(br)), new double[]{x, y}) - turn;
//
//        double max = Math.max(1, Math.max(Math.max(Math.abs(dot_fl), Math.abs(dot_fr)), Math.max(Math.abs(dot_bl), Math.abs(dot_br))));
//        fl.setPower(0);
//        br.setPower(dot_br / max);
//        fr.setPower(0);
//        bl.setPower(0);
//    }

    // Each double[] will be a direction vector of length two
    public double dot(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1];
    }
    public int getfl() {
        return fl.getCurrentPosition();
    }
    public int getbr() {
        return br.getCurrentPosition();
    }
    public int getfr() {
        return fr.getCurrentPosition();
    }
    public int getbl() {
        return bl.getCurrentPosition();
    }

}