package org.firstinspires.ftc.teamcode.pedroauton;


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


@Autonomous(name = "nearHuman_push3_initialSpecimen_human2_pedro", group = "Examples")
public class nearHuman_push3_initialSpecimen_human2_pedro extends OpMode{
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    public Claw claw;
    public ClawRotator clawRot;
    public Slides slides;

//todo change the place specimen numbers by tuning
    private final Pose pickUpSpecimen = new Pose(136, 136, Math.toRadians(270));
    private final Pose placeSpecimenA = new Pose(136, 136, Math.toRadians(0));
    private final Pose placeSpecimenB = new Pose(136, 136, Math.toRadians(0));
    private final Pose placeSpecimenC = new Pose(136, 136, Math.toRadians(0));
    private final Pose placeSpecimenD = new Pose(136, 136, Math.toRadians(0));
    private final Pose placeSpecimenE = new Pose(136, 136, Math.toRadians(0));
    private final Pose specimenP_HUMAN = new Pose(40, 64, Math.toRadians(0));


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

    private final Pose placeCloseP_BASKETwCLAW = new Pose(36, 120, Math.toRadians(0));
    private final Pose placeMiddleP_BASKETwCLAW = new Pose(36, 132, Math.toRadians(0));
    //the other two, the robot just picks up while facing forward, bc the robot cant go out the bounds of the field
    // it will just be turning slightly to pick up the sample, this number will probably be changed
    private final Pose placeFarP_BASKETwCLAW = new Pose(36, 136, Math.toRadians(45));


    //none for human since placeFarP_HUMAN is the same thing (after tuning)
    private final Pose parkP_BASKET = new Pose(32, 8, Math.toRadians(90));

    private final Pose specimenControllP_HUMAN = new Pose(40, 36, Math.toRadians(0));
    private final Pose startControllP_HUMAN = new Pose(8, 32, Math.toRadians(0));
    private final Pose controllBeforeCloseP_HUMAN = new Pose(56, 36, Math.toRadians(0));


    private final Pose specimenP_BASKET = new Pose(40, 80, Math.toRadians(0));
    private final Pose specimenControllP_BASKET = new Pose(40, 112, Math.toRadians(0));
    private final Pose startControllP_BASKET = new Pose(8, 112, Math.toRadians(0));
    private final Pose controllBeforeCloseP_BASKET = new Pose(56, 112, Math.toRadians(0));

    //only for nearHuman_park
    private final Pose straightToParkP_HUMAN = new Pose(8, 8, Math.toRadians(0));

    private Path startWithSpecimen_PATH, park;
    private PathChain pickUpClose_PATH, placeClose_PATH,
            pickUpFar_PATH, specimenControllB_PATH,
            moveToFar_PATH, placeMiddle_PATH,
            moveToMiddle_PATH, pickUpMiddle_PATH,
            specimenControllA_PATH, placeFar_PATH,
            pickUpSpecimenA_PATH, placeSpecimenA_PATH,
    pickUpSpecimenB_PATH, placeSpecimenB_PATH,
    pickUpSpecimenC_PATH, placeSpecimenC_PATH,
    pickUpSpecimenD_PATH, placeSpecimenD_PATH,
    pickUpSpecimenE_PATH, placeSpecimenE_PATH;


    public void buildPaths() {

        startWithSpecimen_PATH = new Path(new BezierLine(new Point(startP_HUMAN), new Point(specimenP_HUMAN)));
        startWithSpecimen_PATH.setLinearHeadingInterpolation(startP_HUMAN.getHeading(), specimenP_HUMAN.getHeading());

        specimenControllA_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(specimenP_HUMAN), new Point( specimenControllP_HUMAN )))
                .setLinearHeadingInterpolation(specimenP_HUMAN.getHeading(),  specimenControllP_HUMAN .getHeading())
                .build();

        specimenControllB_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(specimenControllP_HUMAN), new Point( controllBeforeCloseP_HUMAN )))
                .setLinearHeadingInterpolation(specimenControllP_HUMAN.getHeading(),  controllBeforeCloseP_HUMAN .getHeading())
                .build();

        pickUpClose_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(controllBeforeCloseP_HUMAN), new Point(pickupCloseP_HUMAN)))
                .setLinearHeadingInterpolation(controllBeforeCloseP_HUMAN.getHeading(), pickupCloseP_HUMAN.getHeading())
                .build();

        placeClose_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupCloseP_HUMAN), new Point(placeCloseP_HUMAN)))
                .setLinearHeadingInterpolation(pickupCloseP_HUMAN.getHeading(), placeCloseP_HUMAN.getHeading())
                .build();

        moveToMiddle_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeCloseP_HUMAN), new Point(pickupCloseP_HUMAN)))
                .setLinearHeadingInterpolation(placeCloseP_HUMAN.getHeading(), pickupCloseP_HUMAN.getHeading())
                .build();

        pickUpMiddle_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupCloseP_HUMAN), new Point(pickupMiddleP_HUMAN)))
                .setLinearHeadingInterpolation(pickupCloseP_HUMAN.getHeading(), pickupMiddleP_HUMAN.getHeading())
                .build();

        placeMiddle_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupMiddleP_HUMAN), new Point(placeMiddleP_HUMAN)))
                .setLinearHeadingInterpolation(pickupMiddleP_HUMAN.getHeading(), placeMiddleP_HUMAN.getHeading())
                .build();

        moveToFar_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeMiddleP_HUMAN), new Point(pickupMiddleP_HUMAN)))
                .setLinearHeadingInterpolation(placeMiddleP_HUMAN.getHeading(), pickupMiddleP_HUMAN.getHeading())
                .build();

        pickUpFar_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupMiddleP_HUMAN), new Point(pickupFarP_HUMAN)))
                .setLinearHeadingInterpolation(pickupMiddleP_HUMAN.getHeading(), pickupFarP_HUMAN.getHeading())
                .build();

        placeFar_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupFarP_HUMAN), new Point(placeFarP_HUMAN)))
                .setLinearHeadingInterpolation(pickupFarP_HUMAN.getHeading(), placeFarP_HUMAN.getHeading())
                .build();

        pickUpSpecimenA_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeFarP_HUMAN), new Point(pickUpSpecimen)))
                .setLinearHeadingInterpolation(placeFarP_HUMAN.getHeading(), pickUpSpecimen.getHeading())
                .build();

        placeSpecimenA_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickUpSpecimen), new Point(placeSpecimenA)))
                .setLinearHeadingInterpolation(pickUpSpecimen.getHeading(), placeSpecimenA.getHeading())
                .build();

        pickUpSpecimenB_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeSpecimenA), new Point(pickUpSpecimen)))
                .setLinearHeadingInterpolation(placeSpecimenA.getHeading(), pickUpSpecimen.getHeading())
                .build();

        placeSpecimenB_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickUpSpecimen), new Point(placeSpecimenB)))
                .setLinearHeadingInterpolation(pickUpSpecimen.getHeading(), placeSpecimenB.getHeading())
                .build();

        pickUpSpecimenC_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeSpecimenB), new Point(pickUpSpecimen)))
                .setLinearHeadingInterpolation(placeSpecimenB.getHeading(), pickUpSpecimen.getHeading())
                .build();

        placeSpecimenC_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickUpSpecimen), new Point(placeSpecimenC)))
                .setLinearHeadingInterpolation(pickUpSpecimen.getHeading(), placeSpecimenC.getHeading())
                .build();
        pickUpSpecimenD_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeSpecimenC), new Point(pickUpSpecimen)))
                .setLinearHeadingInterpolation(placeSpecimenC.getHeading(), pickUpSpecimen.getHeading())
                .build();

        placeSpecimenD_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickUpSpecimen), new Point(placeSpecimenD)))
                .setLinearHeadingInterpolation(pickUpSpecimen.getHeading(), placeSpecimenD.getHeading())
                .build();

        pickUpSpecimenE_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeSpecimenD), new Point(pickUpSpecimen)))
                .setLinearHeadingInterpolation(placeSpecimenD.getHeading(), pickUpSpecimen.getHeading())
                .build();

        placeSpecimenE_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickUpSpecimen), new Point(placeSpecimenE)))
                .setLinearHeadingInterpolation(pickUpSpecimen.getHeading(), placeSpecimenE.getHeading())
                .build();

        park = new Path(new BezierLine(new Point(placeSpecimenE), new Point(pickUpSpecimen)));
        park.setLinearHeadingInterpolation(placeSpecimenE.getHeading(), pickUpSpecimen.getHeading());



    }


    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                claw.close();
                clawRot.toNeutral();
                follower.followPath(startWithSpecimen_PATH);
                setPathState(1);
                break;
            case 1:
                if(follower.getPose().getX() > (specimenP_HUMAN.getX() - 1) && follower.getPose().getY() > (specimenP_HUMAN.getY() - 1)) {
                    //slides.hookSpecimen();
                    //clawRot.hookSpecimen();
                    claw.open();
                    clawRot.toDrop();
                    slides.down();
                    slides.rotateLeft();
                    follower.followPath(specimenControllA_PATH,true);
                    setPathState(2);
                }
                break;
            case 2:
                if(follower.getPose().getX() > (specimenControllP_HUMAN.getX() - 1) && follower.getPose().getY() > (specimenControllP_HUMAN.getY() - 1)) {
                    follower.followPath(specimenControllB_PATH,true);
                    setPathState(3);
                }
                break;
            case 3:
                if(follower.getPose().getX() > (controllBeforeCloseP_HUMAN.getX() - 1) && follower.getPose().getY() > (controllBeforeCloseP_HUMAN.getY() - 1)) {
                    follower.followPath(pickUpClose_PATH,true);
                    setPathState(4);
                }
                break;
            case 4:
                if(follower.getPose().getX() > (pickupCloseP_HUMAN.getX() - 1) && follower.getPose().getY() > (pickupCloseP_HUMAN.getY() - 1)) {
                    follower.followPath(placeClose_PATH,true);
                    setPathState(5);
                }
                break;
            case 5:
                if(follower.getPose().getX() > (placeCloseP_HUMAN.getX() - 1) && follower.getPose().getY() > (placeCloseP_HUMAN.getY() - 1)) {
                    follower.followPath(moveToMiddle_PATH,true);
                    setPathState(6);
                }
                break;
            case 6:
                if(follower.getPose().getX() > (pickupCloseP_HUMAN.getX() - 1) && follower.getPose().getY() > (pickupCloseP_HUMAN.getY() - 1)) {
                    follower.followPath(pickUpMiddle_PATH,true);
                    setPathState(7);
                }
                break;
            case 7:
                if(follower.getPose().getX() > (pickupMiddleP_HUMAN.getX() - 1) && follower.getPose().getY() > (pickupMiddleP_HUMAN.getY() - 1)) {
                    follower.followPath(placeMiddle_PATH, true);
                    setPathState(8);
                }
                break;
            case 8:
                if(follower.getPose().getX() > (placeMiddleP_HUMAN.getX() - 1) && follower.getPose().getY() > (placeMiddleP_HUMAN.getY() - 1)) {
                    follower.followPath(moveToFar_PATH, true);
                    setPathState(9);
                }
                break;
            case 9:
                if(follower.getPose().getX() > (pickupMiddleP_HUMAN.getX() - 1) && follower.getPose().getY() > (pickupMiddleP_HUMAN.getY() - 1)) {
                    follower.followPath(pickUpFar_PATH, true);
                    setPathState(10);
                }
                break;
            case 10:
                if(follower.getPose().getX() > (pickupFarP_HUMAN.getX() - 1) && follower.getPose().getY() > (pickupFarP_HUMAN.getY() - 1)) {
                    follower.followPath(placeFar_PATH,true);
                    setPathState(11);
                }
                break;
// todo new
            case 11:
                if(follower.getPose().getX() > (placeFarP_HUMAN.getX() - 1) && follower.getPose().getY() > (placeFarP_HUMAN.getY() - 1)) {
                    follower.followPath(pickUpSpecimenA_PATH,true);
                    setPathState(12);
                }
                break;
            case 12:
                if(follower.getPose().getX() > (pickUpSpecimen.getX() - 1) && follower.getPose().getY() > (pickUpSpecimen.getY() - 1)) {
                    follower.followPath(placeSpecimenA_PATH,true);
                    setPathState(13);
                }
                break;
            case 13:
                if(follower.getPose().getX() > (placeSpecimenA.getX() - 1) && follower.getPose().getY() > (placeSpecimenA.getY() - 1)) {
                    follower.followPath(pickUpSpecimenB_PATH,true);
                    setPathState(14);
                }
                break;

            case 14:
                if(follower.getPose().getX() > (pickUpSpecimen.getX() - 1) && follower.getPose().getY() > (pickUpSpecimen.getY() - 1)) {
                    follower.followPath(placeSpecimenB_PATH,true);
                    setPathState(15);
                }
                break;

            case 15:
                if(follower.getPose().getX() > (placeSpecimenB.getX() - 1) && follower.getPose().getY() > (placeSpecimenB.getY() - 1)) {
                    follower.followPath(pickUpSpecimenC_PATH,true);
                    setPathState(16);
                }
                break;

            case 16:
                if(follower.getPose().getX() > (pickUpSpecimen.getX() - 1) && follower.getPose().getY() > (pickUpSpecimen.getY() - 1)) {
                    follower.followPath(placeSpecimenC_PATH,true);
                    setPathState(17);
                }
                break;

            case 17:
                if(follower.getPose().getX() > (placeSpecimenC.getX() - 1) && follower.getPose().getY() > (placeSpecimenC.getY() - 1)) {
                    follower.followPath(pickUpSpecimenD_PATH,true);
                    setPathState(18);
                }
                break;

            case 18:
                if(follower.getPose().getX() > (pickUpSpecimen.getX() - 1) && follower.getPose().getY() > (pickUpSpecimen.getY() - 1)) {
                    follower.followPath(placeSpecimenD_PATH,true);
                    setPathState(19);
                }
                break;

            case 19:
                if(follower.getPose().getX() > (placeSpecimenE.getX() - 1) && follower.getPose().getY() > (placeSpecimenE.getY() - 1)) {
                    follower.followPath(pickUpSpecimenE_PATH,true);
                    setPathState(20);
                }
                break;

            case 20:
                if(follower.getPose().getX() > (pickUpSpecimen.getX() - 1) && follower.getPose().getY() > (pickUpSpecimen.getY() - 1)) {
                    follower.followPath(placeSpecimenE_PATH,true);
                    setPathState(21);
                }
                break;

            case 21:
                if(follower.getPose().getX() > (placeSpecimenE.getX() - 1) && follower.getPose().getY() > (placeSpecimenE.getY() - 1)) {
                    follower.followPath(park,true);
                    setPathState(22);
                }
                break;

            case 22:
                if(follower.getPose().getX() > (pickUpSpecimen.getX() - 1) && follower.getPose().getY() > (pickUpSpecimen.getY() - 1)) {
                    setPathState(-1);
                }
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
        Constants.setConstants(FConstants.class, LConstants.class);
        opmodeTimer = new Timer();

        opmodeTimer.resetTimer();

        follower = new Follower(hardwareMap);
        follower.setStartingPose(startP_HUMAN);

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