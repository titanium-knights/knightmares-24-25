package org.firstinspires.ftc.teamcode.rrauton;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
package org.firstinspires.ftc.teamcode.rrauton;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.rr.MecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.PullUp;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Slides;
@Config
@Autonomous(name = "nearBasket", group = "Autonomous")
public class nearBasket extends LinearOpMode {
    private Claw claw;
    private Slides slides;
    private PullUp pullup;

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d begPose = new Pose2d(-60, -12, 0); // 0,0 is the center of the board TODO: tune

        telemetry.addData("Initialized: ", "");
        telemetry.update();

        MecanumDrive drivetrain = new MecanumDrive(hardwareMap, begPose);

        claw = new Claw(hardwareMap);
        slides = new Slides(hardwareMap);
        pullup = new PullUp(hardwareMap);

        Actions.runBlocking(claw.closeAction());
        Actions.runBlocking(pullup.toInitPosAction());

        TrajectoryActionBuilder tab = drivetrain.actionBuilder(begPose)
                .lineToX(-36)
                .setTangent(Math.toRadians(90)) // needed when switching between running x and y
                .lineToY(-36)
                .setTangent(Math.toRadians(0))
                .lineToX(-12)
                .setTangent(Math.toRadians(90))
                .lineToY(-48)
                .setTangent(Math.toRadians(0))
                .lineToX(-60)
                .lineToX(-12)
                .setTangent(Math.toRadians(90))
                .lineToY(-60)
                .setTangent(Math.toRadians(0))
                .lineToX(-60)
                .lineToX(-12)
                .setTangent(Math.toRadians(90))
                .lineToY(-66)
                .setTangent(Math.toRadians(0))
                .lineToX(-60);

        // you run stuff that is not drivetrain in between each tab

        TrajectoryActionBuilder specimenTab1 = drivetrain.actionBuilder(begPose)
                .setTangent(Math.toRadians(90))
                .lineToY(0)
                .setTangent(0)
                .lineToX(-36);
        // move the arm
        // move the slides
        // move forward
        TrajectoryActionBuilder specimenTab2 = drivetrain.actionBuilder(new Pose2d(-36, 0, 0))
                .lineToX(-30);
        // move the slides down
        // open the claw
        // move back to initial position
        TrajectoryActionBuilder specimenTab3 = drivetrain.actionBuilder(new Pose2d(-30, 0, 0))
                .lineToX(-60)
                .setTangent(Math.toRadians(90))
                .lineToY(12);

        waitForStart();

        // doesn't build everything on time unless you sleep
        // start low and build your way up
        sleep(1000); // adjust if needed.

        if (isStopRequested()) return;
        Action trajectoryAction = tab.build();
        // add the
        SequentialAction specimenPlaceAction = new SequentialAction(specimenTab1.build(), arm.toScoreSpecimenPosAction(), slides.getSlideAction(SlideState.MEDIUM), specimenTab2.build(), slides.getSlideAction(SlideState.MEDIUMSCORE), claw.openAction(), specimenTab3.build());

        Actions.runBlocking(new SequentialAction(
                specimenPlaceAction,
                trajectoryAction
        ));
    }
}
