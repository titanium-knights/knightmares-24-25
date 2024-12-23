package org.firstinspires.ftc.teamcode.pedroauton;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.pedroPathing.util.Timer;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.ClawRotator;

@Autonomous(name = "nearBasket_park_RED_pedro", group = "Examples")
public class nearBasket_park_RED_pedro extends OpMode{
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;


    private int pathState;

    public Claw claw;
    public ClawRotator clawRot;

    /** CORDINATES FOR RED  **/

    //TODO: all numbers need to be tuned BUT the current numbers should be preety close (our robot's 16x16 size was applied)
    // I understood the rotations as 0/360 right, 90 up, 180 left, 270 down, if this is wrong, change accordingly or just hit up ipek and ill try fix

    // here so we can align with the line better (must check during tuning if these cordinates r actually on the line tho)
    private final Pose startPose = new Pose(136, 80, Math.toRadians(180));


    private final Pose scorePose = new Pose(132, 12, Math.toRadians(315));

    /** ALL THREE ARE FOR RED SAMPLES */
    private final Pose pickupClose = new Pose(88, 120, Math.toRadians(90));

    //lowkey this might affect the 3rd sample (far sample), if it does
    // tunner just lower 132 until u notice it doesnt touch the far sample
    private final Pose pickupMiddle = new Pose(88, 132, Math.toRadians(90));

    private final Pose pickupFar = new Pose(88, 136, Math.toRadians(90));

    //set aside 3 inch for the samples
    private final Pose humanZone = new Pose(136, 133, Math.toRadians(90));

    //this probably wont work because their won't be enough space for samples so just lower x, IF that doesnt work
    // lower both but more of the x because the alliance needs to park as well
    //the rotation for this doesnt matter btw
    //TODO: this park will be only making it to the small portion of the human area's park
    // as the robot nearer to the human zone will get the 144,144 area
    private final Pose parkPose = new Pose(136, 112, Math.toRadians(180));

    private final Pose submersibleControlPose = new Pose(136, 104, Math.toRadians(180));

    //ik that both bellow r the same as some pickup BUT it will make more sence whilst reading/writing/tunning code
    private final Pose sampleMiddleControlPose = new Pose(88, 120, Math.toRadians(180));

    private final Pose sampleFarControlPose = new Pose(88, 132, Math.toRadians(180));

    private final Pose placeSpesimenPose = new Pose(104, 80, Math.toRadians(180));

    private final Pose submersibleControlPoseWithSpesimen = new Pose(104, 80, Math.toRadians(180));



    //todo: bc we just go park- we dont actually use any of these except parkPose and the path park
    // in its place we can also just use humanZone(since basicly same thing)

    /* These are our Paths and PathChains that we will define in buildPaths() */
    private Path scorePreload, park; //we still no use scorePreload
    private PathChain grabClose, grabMiddle, grabFar, placeCloseInHuman, placeMiddleinHuman, placeFarinHuman, placeSpesimen, grabCloseAfterSpesimen;

    public void buildPaths() {

        park = new Path(new BezierLine(new Point(startPose), new Point(parkPose)));
        park.setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading());


        placeSpesimen = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(placeSpesimenPose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), placeSpesimenPose.getHeading())
                .build();

        grabCloseAfterSpesimen = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(placeSpesimenPose), new Point(submersibleControlPoseWithSpesimen),new Point(pickupClose)))
                .setLinearHeadingInterpolation(placeSpesimenPose.getHeading(), pickupClose.getHeading())
                .build();

        //TODO: This is only used when we dont use spesimen
        //if linear no work, try tangential
        //we need curve here so we dont hit the submersible (Middle box area)
        //if this dont work, the controll pose I put may be the issue
        grabClose = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(startPose), new Point(submersibleControlPose),new Point(pickupClose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickupClose.getHeading())
                .build();

        //if linear no work, try Constant
        placeCloseInHuman = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupClose), new Point(humanZone)))
                .setLinearHeadingInterpolation(pickupClose.getHeading(), humanZone.getHeading())
                .build();

        //if linear no work, try tangential
        //if this dont work, the controll pose I put may be the issue
        grabMiddle = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(humanZone), new Point(sampleMiddleControlPose), new Point(pickupMiddle)))
                .setLinearHeadingInterpolation(humanZone.getHeading(), pickupMiddle.getHeading())
                .build();

        //if linear no work, try Constant
        placeMiddleinHuman = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupMiddle), new Point(humanZone)))
                .setLinearHeadingInterpolation(pickupMiddle.getHeading(), humanZone.getHeading())
                .build();

        //if linear no work, try tangential
        //if this dont work, the controll pose I put may be the issue
        grabFar = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(humanZone), new Point(sampleFarControlPose), new Point(pickupFar)))
                .setLinearHeadingInterpolation(humanZone.getHeading(), pickupFar.getHeading())
                .build();

        //if linear no work, try Constant
        placeFarinHuman = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupFar), new Point(humanZone)))
                .setLinearHeadingInterpolation(pickupFar.getHeading(), humanZone.getHeading())
                .build();
    }


    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(park);
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

        follower.update();
        autonomousPathUpdate();

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
        follower.setStartingPose(startPose);

        buildPaths();

        claw = new Claw(hardwareMap);
        clawRot = new ClawRotator(hardwareMap, telemetry);

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

