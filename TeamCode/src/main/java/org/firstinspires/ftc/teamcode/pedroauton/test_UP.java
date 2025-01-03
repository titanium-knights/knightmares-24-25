package org.firstinspires.ftc.teamcode.pedroauton;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.*;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.pedroPathing.util.Timer;
// import org.firstinspires.ftc.teamcode.config.subsystem.ClawSubsystem;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
import org.firstinspires.ftc.teamcode.utilities.Slides;



@Autonomous(name = "test_UP", group = "Examples")
public class test_UP extends OpMode{
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    public Claw claw;
    public ClawRotator clawRot;
    public Slides slides;


    private final Pose START = new Pose(72, 72, Math.toRadians(0));
    private final Pose TEST_RIGHT  = new Pose(72, 82, Math.toRadians(0));
    private final Pose TEST_LEFT = new Pose(72, 62, Math.toRadians(0));
    private final Pose TEST_UP= new Pose(82, 72, Math.toRadians(0));
    private final Pose TEST_DOWN = new Pose(62, 72, Math.toRadians(0));


    private PathChain UP, DOWN, LEFT, RIGHT;

    public void buildPaths() {

        UP = follower.pathBuilder()
                .addPath(new BezierLine(new Point(START), new Point(TEST_UP)))
                .setLinearHeadingInterpolation(START.getHeading(), TEST_UP.getHeading())
                .build();

        DOWN = follower.pathBuilder()
                .addPath(new BezierLine(new Point(START), new Point(TEST_DOWN)))
                .setLinearHeadingInterpolation(START.getHeading(), TEST_DOWN.getHeading())
                .build();

        LEFT = follower.pathBuilder()
                .addPath(new BezierLine(new Point(START), new Point(TEST_LEFT)))
                .setLinearHeadingInterpolation(START.getHeading(), TEST_LEFT.getHeading())
                .build();

        RIGHT = follower.pathBuilder()
                .addPath(new BezierLine(new Point(START), new Point(TEST_RIGHT)))
                .setLinearHeadingInterpolation(START.getHeading(), TEST_RIGHT.getHeading())
                .build();

    }
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(UP);
                setPathState(-1);
                break;
        }
    }


    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void loop() {

        // These loop the movements of the robot
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();

        opmodeTimer.resetTimer();

        follower = new Follower(hardwareMap);
        follower.setStartingPose(START);

        buildPaths();

        claw = new Claw(hardwareMap, telemetry);
        clawRot = new ClawRotator(hardwareMap, telemetry);

        // Set the claw to positions for init
        claw.close();
        clawRot.toPick();
    }

    @Override
    public void init_loop() {}


    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {
    }
}
