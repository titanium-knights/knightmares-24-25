package org.firstinspires.ftc.teamcode.utilities

import com.acmerobotics.dashboard.config.Config

@Config
object CONFIG {
    //drivetrain wheels
    var FRONT_LEFT: String = "fl" //ch 0
    var FRONT_RIGHT: String = "fr" //ch 2
    var BACK_LEFT: String = "bl" //ch 3
    var BACK_RIGHT: String = "br" //ch 1

    //slide motor
    var slide: String = "slides"
    var slideRot: String = "slidesRot"

    var pullUpMotor1: String = "pl1"
    var pullUpMotor2: String = "pl2"

    //claw servo
    // public static String clawMotor = "cm";
    var claw: String = "claw"

    //latch servo
    var latch: String = "latch"
    var clawRotator: String = "clawRotator"
}