package frc.robot.utilities;

//TODO - Make ports right
public enum Ports {

    XBOX_PORT(0), //xbox controller
    
    //drivetrain
    LEFT_DRIVE_0(20),
    LEFT_DRIVE_MAIN(21),
    LEFT_DRIVE_1(22),
    
    RIGHT_DRIVE_0(30),
    RIGHT_DRIVE_MAIN(31),
    RIGHT_DRIVE_1(32),

    //pneumatics
    PCM_1(1),
    DRIVE_SOLENOID_OPEN(1),
    DRIVE_SOLENOID_CLOSE(0),

    //sensors
    PIGEON_IMU(10),

    //turret
    TURRET(0),

    //conveyor
    CONVEYOR_RIGHT(33),
    CONVEYOR_LEFT(34),
    BREAKBEAM_ENTER(0),
    BREAKBEAM_EXIT(1);

    public int port;
    private Ports(int port) {
        this.port = port;
    }
}