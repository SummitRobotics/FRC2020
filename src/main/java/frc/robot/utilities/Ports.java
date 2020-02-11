package frc.robot.utilities;

//TODO - Make ports right
public enum Ports {

    //OI
    XBOX_PORT(0),
    LAUNCHPAD_PORT(1),
    JOYSTICK_PORT(2),

    BUTTONBOX_PORT(1),
    
    // drivetrain
    LEFT_DRIVE_0(20),
    LEFT_DRIVE_MAIN(21),
    LEFT_DRIVE_1(22),
    
    RIGHT_DRIVE_0(30),
    RIGHT_DRIVE_MAIN(31),
    RIGHT_DRIVE_1(32),

    // pneumatics
    PCM_1(1),
    DRIVE_SOLENOID_OPEN(1),
    DRIVE_SOLENOID_CLOSE(0),

    // sensors
    PIGEON_IMU(10),

    //turret
    TURRET(40),

    // shooter
    SHOOTER(60),

    // conveyor
    CONVEYOR_RIGHT(33),
    CONVEYOR_LEFT(34),
    BREAKBEAM_ENTER(0),
    BREAKBEAM_EXIT(1),
    
    // intake arm
    INTAKE_ARM_INTAKE(0),
    INTAKE_ARM_PIVOT(0),

    // DIO
    TURRET_LIMIT_ONE(0),
    TURRET_LIMIT_TWO(1);

    public int port;
    private Ports(int port) {
        this.port = port;
    }
}