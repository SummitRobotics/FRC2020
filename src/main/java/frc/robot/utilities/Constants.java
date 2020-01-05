package frc.robot.utilities;

public final class Constants {

    public enum LoggerRelations {
        LEFT_MOTOR_POWER(0),
        RIGHT_MOTOR_POWER(1),
        LEFT_MOTOR_TARGET(2),
        RIGHT_MOTOR_TARGET(3),
        LEFT_MOTOR_POSISION(4),
        RIGHT_MOTOR_POSISION(5);

        public int value;
        private LoggerRelations(int value) {
            this.value = value;
        }
    }

    public final static int
    LOGGER_RATE = 1,
    
    //motors
    LEFT_DRIVE_0 = 20,
    LEFT_DRIVE_MAIN = 21,
    LEFT_DRIVE_1 = 22,
    
    RIGHT_DRIVE_0 = 30,
    RIGHT_DRIVE_MAIN = 31,
    RIGHT_DRIVE_1 = 32;

    public final static String
    LOG_FILE_PATH = "/home/admin/";
}
