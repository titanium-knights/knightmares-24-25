package org.firstinspires.ftc.teamcode.utilities

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator
import com.qualcomm.robotcore.hardware.HardwareMap

class IMU(hmap: HardwareMap) {
    var imu: BNO055IMU = hmap.get(BNO055IMU::class.java, "imu")
    fun initializeIMU(): String {
        val parameters = BNO055IMU.Parameters()
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
        parameters.calibrationDataFile =
            "BNO055IMUCalibration.json" // see the calibration sample opmode
        parameters.loggingEnabled = true
        parameters.loggingTag = "IMU"
        parameters.accelerationIntegrationAlgorithm = JustLoggingAccelerationIntegrator()
        imu.initialize(parameters)
        return "completed initialization"
    }

    val zAngle: Double
        //TODO: test these methods
        get() = ((-imu.angularOrientation.firstAngle).toDouble())
    val yAngle: Double
        get() = ((-imu.angularOrientation.secondAngle).toDouble())
    val xAngle: Double
        get() = ((-imu.angularOrientation.thirdAngle).toDouble())
}