package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.utilities.Claw
import org.firstinspires.ftc.teamcode.utilities.ClawRotator
import org.firstinspires.ftc.teamcode.utilities.Latch
import org.firstinspires.ftc.teamcode.utilities.PullUp
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive
import org.firstinspires.ftc.teamcode.utilities.Slides
import kotlin.math.abs

//import org.firstinspires.ftc.teamcode.utilities.PullUp;
@Config
@TeleOp(name = "DriveTrain Teleop")
class Teleop : OpMode() {
    var slides: Slides? = null
    var pullup: PullUp? = null
    var claw: Claw? = null
    var latch: Latch? = null
    var clawRotator: ClawRotator? = null

    // ClawIteration2 claw;
    var drive: SimpleMecanumDrive? = null

    //Set normal power constant to 1, no point in slowing the robot down
    val normalPower: Double = 1.0

    // in case of joystick drift, ignore very small values
    val STICK_MARGIN: Float = 0.7f

    var clawState: Boolean = true
    var cRotatorAtDrop: Boolean = false

    //Prevents irreversible things such as pullup and plane launcher from running before this button is pressed
    // (will add later)
    // boolean validate = false;
    //makes validate button have to be pressed for a while before features enabled
    // int validatecount = 0;
    enum class SlideState {
        SLIDE_BOTTOM,
        SLIDE_LOW,
        SLIDE_MEDIUM,
        SLIDE_HIGH
    }

    enum class RotatorState {
        SLIDE_DOWN,
        SLIDE_UP,
        SLIDE_STOP
    }

    var slideState: SlideState = SlideState.SLIDE_BOTTOM
    var rotatorState: RotatorState = RotatorState.SLIDE_DOWN

    enum class PullUpState {
        NEUTRAL,
        REACH_UP
    }

    var pullupstate: PullUpState = PullUpState.REACH_UP

    var elapsedTime: ElapsedTime? = null

    enum class ButtonPressState {
        PRESSED_GOOD,  //the first time we see the button pressed
        DEPRESSED,  //you haven't let go
        UNPRESSED // its not pressed
    }

    var slideButton: ButtonPressState? = null
    var rotatorButton: ButtonPressState? = null
    var clawButton: ButtonPressState? = null
    var cRotatorButton: ButtonPressState? = null
    var slideManual: ButtonPressState? = null
    var slideManualUp: ButtonPressState? = null

    var started: Boolean = false

    // boolean intakeRunning = false;
    override fun init() {
        this.drive = SimpleMecanumDrive(hardwareMap)
        this.slides = Slides(hardwareMap, telemetry)
        this.pullup = PullUp(hardwareMap)
        this.clawButton = ButtonPressState.UNPRESSED
        this.cRotatorButton = ButtonPressState.UNPRESSED
        this.slideButton = ButtonPressState.UNPRESSED
        this.slideManual = ButtonPressState.UNPRESSED
        this.slideManualUp = ButtonPressState.UNPRESSED

        this.claw = Claw(hardwareMap, telemetry)
        this.latch = Latch(hardwareMap, telemetry)
        this.clawRotator = ClawRotator(hardwareMap, telemetry)

        // slides.startPosition();
    }

    override fun loop() {
        //VALIDATE
//        if (gamepad1.dpad_up) {++validatecount;}
//        if (validatecount > 5) {validate = true;}

        if (gamepad1.left_trigger > 0.1f && slideButton == ButtonPressState.UNPRESSED) {
            slideButton = ButtonPressState.PRESSED_GOOD
        } else if (gamepad1.left_trigger > 0.1f && slideButton == ButtonPressState.PRESSED_GOOD) {
            slideButton = ButtonPressState.DEPRESSED
        } else if (gamepad1.left_trigger < 0.1f) slideButton = ButtonPressState.UNPRESSED

        //        if (gamepad1.right_bumper && slideManual == ButtonPressState.UNPRESSED) {
//            slideManual = ButtonPressState.PRESSED_GOOD;
//        } else if (gamepad1.right_bumper && slideManual==ButtonPressState.PRESSED_GOOD) {
//            slideManual = ButtonPressState.DEPRESSED;
//        } else if (!gamepad1.right_bumper) slideManual = ButtonPressState.UNPRESSED;
//
//        if (gamepad1.left_bumper && slideManualUp == ButtonPressState.UNPRESSED) {
//            slideManualUp = ButtonPressState.PRESSED_GOOD;
//        } else if (gamepad1.left_bumper && slideManualUp==ButtonPressState.PRESSED_GOOD) {
//            slideManualUp = ButtonPressState.DEPRESSED;
//        } else if (!gamepad1.left_bumper) slideManualUp = ButtonPressState.UNPRESSED;
        if (gamepad1.left_bumper) { //slideManual==ButtonPressState.PRESSED_GOOD) {
            slides!!.retract()
            telemetry.addLine("retracting")
        } else if (gamepad1.right_bumper) { //slideManualUp==ButtonPressState.PRESSED_GOOD) {
            slides!!.extend()
        } else {
            slides!!.stop()
        }

        //DRIVE
        val x = gamepad1.left_stick_x
        val y = gamepad1.left_stick_y
        val turn = gamepad1.right_stick_x
        move(x, -y, turn)

        when (slideState) {
            SlideState.SLIDE_BOTTOM -> if (abs((slides.getEncoder() - 30).toDouble()) < 10) { // drop height
                if (slideButton == ButtonPressState.PRESSED_GOOD) {
                    slideState = SlideState.SLIDE_HIGH
                    telemetry.addData("pos", slides.getEncoder())
                    telemetry.update()
                    // slides.high();
                }
            }

            SlideState.SLIDE_LOW -> {
                telemetry.addData(
                    "u shouldnt see this lol but encoder position",
                    slides.getEncoder()
                )
                telemetry.update()
            }

            SlideState.SLIDE_HIGH -> if (abs((slides.getEncoder() - 30).toDouble()) < 10) { // high height 3481
                if (slideButton == ButtonPressState.PRESSED_GOOD) {
                    slides!!.tozero()
                    slideState = SlideState.SLIDE_BOTTOM
                    telemetry.addData("pos", slides.getEncoder())
                    telemetry.update()
                }
            }

            else -> {
                slideState = SlideState.SLIDE_BOTTOM
                telemetry.addLine("default")
                telemetry.update()
            }
        }
        when (pullupstate) {
            PullUpState.NEUTRAL -> if (gamepad1.x) {
                pullup!!.manualLeftUp()
                pullup!!.manualRightUp()
                pullupstate = PullUpState.REACH_UP
            }

            PullUpState.REACH_UP -> {
                telemetry.addData("pullup1pos", +pullup.getPosition1())
                telemetry.addData("pullup2pos", +pullup.getPosition2())
                telemetry.update()
                if (gamepad1.x) {
                    pullup!!.manualLeftDown()
                    pullup!!.manualRightDown()
                    pullupstate = PullUpState.NEUTRAL
                }
            }

            else -> pullupstate = PullUpState.NEUTRAL
        }
        if (gamepad1.dpad_left) {
            latch!!.latchOn()
        }

        if (gamepad1.dpad_right) {
            latch!!.latchOff()
        }


        //  ROTATING SLIDES
        if (gamepad1.right_trigger > 0.5) {
            slides!!.rotateRight()
            telemetry.addLine("rotator going up")
            telemetry.update()
        } else if (gamepad1.left_trigger > 0.5) {
            slides!!.rotateLeft()
            telemetry.addLine("rotator going down")
            telemetry.update()
        } else {
            if (slides.getRotatorEncoder() >= 400) {
                slides!!.keepUp()
                telemetry.addLine("kept up")
                telemetry.update()
            } else {
                slides!!.stopRotator()
            }
        }

        /* previous code
        if (gamepad1.b && (clawButton == ButtonPressState.UNPRESSED) && !clawState) {
            clawButton = ButtonPressState.PRESSED_GOOD;
            claw.open(
            );
            clawState = true;


        } else if (gamepad1.x && (clawButton == ButtonPressState.UNPRESSED) && clawState) {
            clawButton = ButtonPressState.PRESSED_GOOD;
            claw.close();
            clawState = false;
        } else if (!(gamepad1.b) && (!(gamepad1.x) && (clawButton == ButtonPressState.PRESSED_GOOD))){
            clawButton = ButtonPressState.UNPRESSED;

        }
         */

        // improved code by yours truly:
        if (gamepad1.y && (clawButton == ButtonPressState.UNPRESSED)) {
            if (!clawState) {
                claw!!.open()
                clawState = true
                clawButton = ButtonPressState.PRESSED_GOOD
            } else {
                claw!!.close()
                clawState = false
                clawButton = ButtonPressState.PRESSED_GOOD
            }
        } else if (!gamepad1.y && (clawButton == ButtonPressState.PRESSED_GOOD)) {
            clawButton = ButtonPressState.UNPRESSED
        }


        if (gamepad1.a && (cRotatorButton == ButtonPressState.UNPRESSED) && !cRotatorAtDrop) {
            cRotatorButton = ButtonPressState.PRESSED_GOOD
            clawRotator!!.toDrop()
            cRotatorAtDrop = true
        } else if (gamepad1.a && (cRotatorButton == ButtonPressState.UNPRESSED) && cRotatorAtDrop) {
            cRotatorButton = ButtonPressState.PRESSED_GOOD
            clawRotator!!.toPick()
            cRotatorAtDrop = false
        } else if (!(gamepad1.a) && (cRotatorButton == ButtonPressState.PRESSED_GOOD)) {
            cRotatorButton = ButtonPressState.UNPRESSED
        }

        //        if (gamepad1.y) {
//            clawRotator.toDrop();
//            cRotatorAtDrop = true;
//        } else if (gamepad1.a) {
//            clawRotator.toPick();
//            cRotatorAtDrop = false;
//        }
        // THE ULTIMATE BUTTON
        if (gamepad1.dpad_up) {
            //retract slides
            //rotate slides
            //latch on
            //extend slides

            val slidesRunnable = Runnable {
                while (slides.getEncoder() <= -100) {
                    slides!!.retract()
                }
                //TODO: tune
                while (slides.getRotatorEncoder() <= 470) {
                    slides!!.rotateRight()
                }

                latch!!.latchOn()
                while (slides.getEncoder() >= -3000) {
                    slides!!.extend()
                }
            }
            val slidesThread = Thread(slidesRunnable)
            slidesThread.start()
        } else if (gamepad1.dpad_down) {
            //retract slides
            //rotate slides
            //latch off
            //extend slides

            val slidesRunnable = Runnable {
                while (slides.getEncoder() <= -100) {
                    slides!!.retract()
                }
                //TODO: tune
                while (slides.getRotatorEncoder() >= 200) {
                    slides!!.rotateLeft()
                }

                latch!!.latchOn()
                while (slides.getEncoder() >= -300) {
                    slides!!.extend()
                }
            }
            val slidesThread = Thread(slidesRunnable)
            slidesThread.start()
        }
    }

    fun move(x: Float, y: Float, turn: Float) {
        // if the stick movement is negligible, set STICK_MARGIN to 0
        var x = x
        var y = y
        var turn = turn
        if (abs(x.toDouble()) <= STICK_MARGIN) x = .0f
        if (abs(y.toDouble()) <= STICK_MARGIN) y = .0f
        if (abs(turn.toDouble()) <= STICK_MARGIN) turn = .0f

        //Notation of a ? b : c means if a is true do b, else do c.
        val multiplier = normalPower
        drive!!.move(-x * multiplier, y * multiplier, turn * multiplier)
    }
}
