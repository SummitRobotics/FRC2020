package frc.robot.logging;

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

    //sensors
    IMU_X_GFORCE(24),
    IMU_Y_ANGLE(25),
    IMU_HEADING(26),

    //pneumatics
    SHIFT_STATE(27),
    
    //lemonlight
    LEMONLIGHT_HAS_TARGET(28),
    LEMONLIGHT_X_OFF(29),
    LEMONLIGHT_Y_OFF(30),
    
    //turret
    TURRET(31),

    //Conveyor
    CONVEYOR_RIGHT(32),
    CONVEYOR_LEFT(33),
    CONVEYOR_BREAKBEAM_ENTER(34),
    CONVEYOR_BREAKBEAM_EXIT(35);

    public int value;
    private LoggerRelations(int value) {
        this.value = value;
    }
}
