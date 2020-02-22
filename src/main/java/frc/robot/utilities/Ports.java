package frc.robot.utilities;

//TODO - Make ports right
public class Ports {

    public static int

    // OI
    XBOX_PORT = 0,
    LAUNCHPAD_PORT = 1,
    JOYSTICK_PORT = 2,
    
    // drivetrain
    LEFT_DRIVE_0 = 20,
    LEFT_DRIVE_MAIN = 21,
    LEFT_DRIVE_1 = 22,
    
    RIGHT_DRIVE_0 = 30,
    RIGHT_DRIVE_MAIN = 31,
    RIGHT_DRIVE_1 = 32,
        
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
    BREAKBEAM_ENTER = 0,
    BREAKBEAM_EXIT = 1,
    
    // intake
    INTAKE_ARM_INTAKE = 62,
    INTAKE_ARM_PIVOT = 61,
    UPPER_LIMIT = 6, //TODO - fix

    // DIO
    TURRET_LIMIT_ONE = 0,
    TURRET_LIMIT_TWO = 1,

    // climber
    LEFT_ARM_MOTOR = 41,
    RIGHT_ARM_MOTOR = 40,
    EXTEND_CLIMB = 4,
    CLOSE_BREAK = 5,

    // buddy climb
    OPEN_CLAMP = 2,
    CLOSE_CLAMP = 3;
}