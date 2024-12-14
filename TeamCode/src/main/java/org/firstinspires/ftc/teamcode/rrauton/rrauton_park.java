package org.firstinspires.ftc.teamcode.rrauton;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.rr.MecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.PullUp;
import org.firstinspires.ftc.teamcode.utilities.Slides;

@Config
@Autonomous(name = "rrauton_park", group = "Autonomous")
public class rrauton_park extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d begPose = new Pose2d(-60, -12, 0); // 0,0 is the center of the board TODO: tune

        telemetry.addData("Initialized: ", "");
        telemetry.update();

        MecanumDrive drivetrain = new MecanumDrive(hardwareMap, begPose);

        //TODO: add 1 arg constructors to these hardware classes to prevent errors
        Claw claw = new Claw(hardwareMap, telemetry);
        Slides slides = new Slides(hardwareMap, telemetry);
        PullUp pullup = new PullUp(hardwareMap, telemetry);

        // these were added to the util classes they basically do the same thing as claw.close but they need to be funky for roadrunner so its a different method
        Actions.runBlocking(claw.closeAction());
        // TODO: this function does not exist
        // Actions.runBlocking(pullup.toInitPosAction());

        // the actual path
        // each "tab" is like a sequence of x and y movements
        //TODO NOT TUNED
        TrajectoryActionBuilder tab = drivetrain.actionBuilder(begPose)
                .lineToX(72);
        //subtract 72 by half it's width


        waitForStart();

        // doesn't build everything on time unless you sleep
        // start low and build your way up
        sleep(1000); // adjust if needed.

        if (isStopRequested()) return;
        Action trajectoryAction = tab.build();


        Actions.runBlocking(new SequentialAction(
                // specimenPlaceAction,
                trajectoryAction
        ));
    }
}
