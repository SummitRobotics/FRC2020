package frc.robot.utilities;

//TODO - Make ports right
public class Ports {

    public static int

    // OI
    XBOX_PORT = 0,
    LAUNCHPAD_PORT = 1,
    JOYSTICK_PORT = 2,
    
    // drivetrain
    LEFT_DRIVE_0 = 31,
    LEFT_DRIVE_MAIN = 32,
    LEFT_DRIVE_1 = 30,
    
    RIGHT_DRIVE_0 = 21,
    RIGHT_DRIVE_MAIN = 22,
    RIGHT_DRIVE_1 = 20,

    SHIFT_SOLENOID_UP = 1,
    SHIFT_SOLENOID_DOWN = 0,

    // pneumatics
    PCM_1 = 16,

    // sensors
    PIGEON_IMU = 10,

    // turret
    TURRET = 50,

    // shooter
    SHOOTER = 52,

    // conveyor
    CONVEYOR = 60,
    BREAKBEAM_ENTER = 4,
    BREAKBEAM_EXIT = 5,
    
    // intake
    INTAKE_ARM_INTAKE = 62,
    INTAKE_ARM_PIVOT = 61,
    INTAKE_LOCK = 5,
    UPPER_LIMIT = 0, //TODO - fix

    // DIO
    TURRET_LIMIT_ONE = 2,
    TURRET_LIMIT_TWO = 3,

    // climber
    LEFT_ARM_MOTOR = 41,
    RIGHT_ARM_MOTOR = 40,
    EXTEND_CLIMB = 4,

    // buddy climb
    OPEN_BUDDY_CLIMB = 2,
    CLOSE_BUDDY_CLIMB = 3;
}