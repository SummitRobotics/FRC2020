package frc.robot.utilities;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    /**
     * contains all the index arrays for eah pice of data that is logged
     */
    public enum LoggerRelations {
        //controller -must start at 0 or modifications need to oi.java
        RIGHT_TRIGGER(0),
        LEFT_TRIGGER(1),
        RIGHT_BUMPER(2),
        LEFT_BUMPER(3),
        LEFT_STICK_X(4),
        LEFT_STICK_Y(5),
        RIGHT_STICK_X(6),
        RIGHT_STICK_Y(7),
        BTN_A(8),
        BTN_B(9),
        BTN_X(10),
        BTN_Y(11),
        BTN_START(12),
        BTN_BACK(13),
        DPAD_UP(14),
        DPAD_DOWN(15),
        DPAD_LEFT(16),
        DPAD_RIGHT(17),

        //motors
        LEFT_MOTOR_POWER(18),
        RIGHT_MOTOR_POWER(19),
        LEFT_MOTOR_TARGET(20),
        RIGHT_MOTOR_TARGET(21),
        LEFT_MOTOR_POSITION(22),
        RIGHT_MOTOR_POSITION(23),
        TURRET(24),

        //sensors
        IMU_X_GFORCE(25),
        IMU_Y_ANGLE(26),
        IMU_HEADING(27),

        //pneumatics
        SHIFT_STATE(28);
        

        public int value;
        private LoggerRelations(int value) {
            this.value = value;
        }
    }

    public enum DPadValues {
        UP(0, 45, 315),
        DOWN(180, 135, 225),
        LEFT(90, 45, 135),
        RIGHT(270, 225, 315);

        public int[] values;
        private DPadValues(int... values) {
            this.values = values;
        }

        public boolean isEqual(int value) {
            for (int element : values) {
                if (value == element) {
                    return true;
                }
            }
            return false;
        }
    }

    //all the values used in the robot such as can addressed
    public final static int
    //controllers
    XBOX_PORT = 0, //xbox controller

    //ect
    LOGGER_RATE = 1,
    
    //motors
    LEFT_DRIVE_0 = 20,
    LEFT_DRIVE_MAIN = 21,
    LEFT_DRIVE_1 = 22,
    
    RIGHT_DRIVE_0 = 30,
    RIGHT_DRIVE_MAIN = 31,
    RIGHT_DRIVE_1 = 32,

    TURRET = 40,

    //pneumatics
    PCM_1 = 1,
    DRIVE_SOLENOID_OPEN = 1,
    DRIVE_SOLENOID_CLOSE = 0,

    //sensors
    PIGEON_IMU = 10;
    
    //PID Values
    public final static double
    DRIVETRAIN_P = .2,
    DRIVETRAIN_I = 0.0,
    DRIVETRAIN_D = 0.01,

    GYRO_P = 0.05,
    GYRO_I = 0,
    GYRO_D = 0,

    TURRET_P = .2,
    TURRET_I = 0.0,
    TURRET_D = 0.01;

    public final static String
    LOG_FILE_PATH = "/home/admin/";
}
