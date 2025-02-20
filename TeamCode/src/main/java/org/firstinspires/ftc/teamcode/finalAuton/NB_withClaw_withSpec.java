package org.firstinspires.ftc.teamcode.finalAuton;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

// import org.firstinspires.ftc.teamcode.config.subsystem.ClawSubsystem;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
import org.firstinspires.ftc.teamcode.utilities.Slides;

//TODO ADD ARM STUFF


@Autonomous(name = "NB_withClaw_withSpec", group = "Examples")
public class NB_withClaw_withSpec extends OpMode{
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    public Claw claw;
    public ClawRotator clawRot;
    public Slides slides;


    private final Pose startP_HUMAN = new Pose(8, 64, Math.toRadians(0));
    private final Pose startP_BASKET = new Pose(8, 80, Math.toRadians(0));

    //only for basket- for now
    private final Pose scoreP = new Pose(12, 132, Math.toRadians(135));

    private final Pose pickupCloseP_BASKET = new Pose(56, 120, Math.toRadians(90));
    private final Pose pickupMiddleP_BASKET = new Pose(56, 132, Math.toRadians(90));
    private final Pose pickupFarP_BASKET = new Pose(56, 136, Math.toRadians(90));

    //the y will need a change during tuning
    private final Pose pickupCloseP_HUMAN = new Pose(56, 24, Math.toRadians(270));
    private final Pose pickupMiddleP_HUMAN = new Pose(56, 12, Math.toRadians(270));
    private final Pose pickupFarP_HUMAN = new Pose(56, 8, Math.toRadians(270));

    //11 cuz 3 inch for sample + 8 inch for robot
    private final Pose placeCloseP_HUMAN = new Pose(11, 24, Math.toRadians(270));
    private final Pose placeMiddleP_HUMAN = new Pose(11, 12, Math.toRadians(270));
    //will also be used as park
    private final Pose placeFarP_HUMAN = new Pose(11, 8, Math.toRadians(270));

    //its hundred(angel) not by mistake, there is a chance the block would end up outside the line so angel it + same reasoning for 124 instead of 120
    private final Pose placeCloseP_BASKET = new Pose(14, 124, Math.toRadians(100));
    private final Pose placeMiddleP_BASKET = new Pose(16, 132, Math.toRadians(90));
    private final Pose placeFarP_BASKET = new Pose(20, 136, Math.toRadians(90));


    private final Pose pickupCloseP_BASKETwCLAW = new Pose(36, 120, Math.toRadians(0));
    private final Pose pickupMiddleP_BASKETwCLAW = new Pose(36, 132, Math.toRadians(0));
    //the other two, the robot just picks up while facing forward, bc the robot cant go out the bounds of the field
    // it will just be turning slightly to pick up the sample, this number will probably be changed
    private final Pose pickupFarP_BASKETwCLAW = new Pose(36, 136, Math.toRadians(45));


    //none for human since placeFarP_HUMAN is the same thing (after tuning)
    private final Pose parkP_BASKET = new Pose(32, 8, Math.toRadians(90));

    private final Pose specimenP_HUMAN = new Pose(40, 64, Math.toRadians(0));
    private final Pose specimenControllP_HUMAN = new Pose(40, 36, Math.toRadians(0));
    private final Pose startControllP_HUMAN = new Pose(8, 32, Math.toRadians(0));
    private final Pose controllBeforeCloseP_HUMAN = new Pose(56, 36, Math.toRadians(0));


    private final Pose specimenP_BASKET = new Pose(40, 80, Math.toRadians(0));
    private final Pose specimenControllP_BASKET = new Pose(40, 112, Math.toRadians(0));
    private final Pose startControllP_BASKET = new Pose(8, 112, Math.toRadians(0));
    private final Pose controllBeforeCloseP_BASKET = new Pose(56, 112, Math.toRadians(0));

    private final Pose straightToParkP_HUMAN = new Pose(8, 8, Math.toRadians(0));
    private final Pose straightToParkP_BASKET = new Pose(8, 32, Math.toRadians(0));

    private Path startWithSpecimen_PATH, park;
    private PathChain specimenControllA_PATH, specimenControllB_PATH , pickUpClose_PATH, placeClose_PATH, pickUpMiddle_PATH, placeMiddle_PATH, pickUpFar_PATH, placeFar_PATH;


    public void buildPaths() {

        startWithSpecimen_PATH = new Path(new BezierLine(new Point(startP_BASKET), new Point(specimenP_BASKET)));
        startWithSpecimen_PATH.setLinearHeadingInterpolation(startP_BASKET.getHeading(), specimenP_BASKET.getHeading());

        specimenControllA_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(specimenP_BASKET), new Point( specimenControllP_BASKET )))
                .setLinearHeadingInterpolation(specimenP_BASKET.getHeading(),  specimenControllP_BASKET .getHeading())
                .build();

        specimenControllB_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(specimenControllP_BASKET), new Point( controllBeforeCloseP_BASKET )))
                .setLinearHeadingInterpolation(specimenControllP_BASKET.getHeading(),  controllBeforeCloseP_BASKET .getHeading())
                .build();
        pickUpClose_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(specimenControllP_BASKET), new Point(pickupCloseP_BASKETwCLAW)))
                .setLinearHeadingInterpolation(specimenControllP_BASKET.getHeading(), pickupCloseP_BASKETwCLAW.getHeading())
                .build();

        placeClose_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupCloseP_BASKETwCLAW), new Point(scoreP)))
                .setLinearHeadingInterpolation(pickupCloseP_BASKETwCLAW.getHeading(), scoreP.getHeading())
                .build();

        pickUpMiddle_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scoreP), new Point(pickupMiddleP_BASKETwCLAW)))
                .setLinearHeadingInterpolation(scoreP.getHeading(), pickupMiddleP_BASKETwCLAW.getHeading())
                .build();

        placeMiddle_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupMiddleP_BASKETwCLAW), new Point(scoreP)))
                .setLinearHeadingInterpolation(pickupMiddleP_BASKETwCLAW.getHeading(), scoreP.getHeading())
                .build();

        pickUpFar_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scoreP), new Point(pickupFarP_BASKETwCLAW)))
                .setLinearHeadingInterpolation(scoreP.getHeading(), pickupFarP_BASKETwCLAW.getHeading())
                .build();

        placeFar_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupFarP_BASKETwCLAW), new Point(scoreP)))
                .setLinearHeadingInterpolation(pickupFarP_BASKETwCLAW.getHeading(), scoreP.getHeading())
                .build();

        //basically park
        park = new Path(new BezierLine(new Point(placeFarP_BASKET), new Point(straightToParkP_BASKET)));
        park.setLinearHeadingInterpolation(placeFarP_BASKET.getHeading(), straightToParkP_BASKET.getHeading());
    }


    private int notCase = 0;
    public void autonomousPathUpdate() {
        if (notCase == 0) {
            telemetry.addLine("case " + notCase);
            telemetry.update();
            follower.followPath(startWithSpecimen_PATH);
            notCase = 1;
        }
        if (notCase == 1) {
            telemetry.addLine("case " + notCase);
            telemetry.update();
            if((Math.abs(follower.getPose().getX() - specimenP_BASKET.getX()) < 1) && Math.abs(follower.getPose().getY() - specimenP_BASKET.getY()) < 1) {
                follower.followPath(specimenControllA_PATH,true);
                notCase = 2;
            }
        }
        if (notCase == 2) {
            telemetry.addLine("case " + notCase);
            telemetry.update();
            if((Math.abs(follower.getPose().getX() - specimenControllP_BASKET.getX()) < 1) && Math.abs(follower.getPose().getY() - specimenControllP_BASKET.getY()) < 1) {
                follower.followPath(specimenControllB_PATH,true);
                notCase = 3;
            }
        }
        if(notCase ==3){
            telemetry.addLine("case " + notCase);
            telemetry.update();
            if((Math.abs(follower.getPose().getX() - controllBeforeCloseP_BASKET.getX()) < 1) && Math.abs(follower.getPose().getY() - controllBeforeCloseP_BASKET.getY()) < 1) {

                follower.followPath(pickUpClose_PATH, true);
                notCase = 4;
            }
        }
        if(notCase ==4){
            telemetry.addLine("case " + notCase);
            telemetry.update();
            if((Math.abs(follower.getPose().getX() - pickupCloseP_BASKETwCLAW.getX()) < 1) && Math.abs(follower.getPose().getY() - pickupCloseP_BASKETwCLAW.getY()) < 1) {

                follower.followPath(placeClose_PATH, true);
                notCase = 5;
            }
        }
        if(notCase ==5){
            telemetry.addLine("case " + notCase);
            telemetry.update();
            if((Math.abs(follower.getPose().getX() - scoreP.getX()) < 1) && Math.abs(follower.getPose().getY() - scoreP.getY()) < 1) {

                follower.followPath(pickUpMiddle_PATH, true);
                notCase = 6;
            }
        }
        if(notCase == 6){
            telemetry.addLine("case " + notCase);
            telemetry.update();
            if((Math.abs(follower.getPose().getX() - pickupMiddleP_BASKETwCLAW.getX()) < 1) && Math.abs(follower.getPose().getY() - pickupMiddleP_BASKETwCLAW.getY()) < 1) {

                follower.followPath(placeMiddle_PATH, true);
                notCase = 7;
            }
        }
        if (notCase ==7){
            telemetry.addLine("case " + notCase);
            telemetry.update();
            if((Math.abs(follower.getPose().getX() - scoreP.getX()) < 5) && Math.abs(follower.getPose().getY() - scoreP.getY()) < 5) {

                follower.followPath(pickUpFar_PATH, true);
                notCase = 8;
            }
        }
        if(notCase == 8){
            telemetry.addLine("case " + notCase);
            telemetry.update();
            if((Math.abs(follower.getPose().getX() - pickupFarP_BASKETwCLAW.getX()) < 5) && Math.abs(follower.getPose().getY() - pickupFarP_BASKETwCLAW.getY()) < 5) {

                follower.followPath(placeFar_PATH, true);
                notCase = 9;
            }
        }
        if(notCase == 9){
            telemetry.addLine("case " + notCase);
            telemetry.update();
            if((Math.abs(follower.getPose().getX() - scoreP.getX()) < 5) && Math.abs(follower.getPose().getY() - scoreP.getY()) < 5) {

                follower.followPath(park, true);
                notCase = 10;
            }
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
        Constants.setConstants(FConstants.class, LConstants.class);
        opmodeTimer = new Timer();

        opmodeTimer.resetTimer();

        follower = new Follower(hardwareMap);
        follower.setStartingPose(startP_BASKET);

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
