package frc.robot.utilities;

public final class Constants {

    public enum LoggerRelations {
        LEFT_MOTOR(0),
        RIGHT_MOTOR(1);

        public int value;
        private LoggerRelations(int value) {
            this.value = value;
        }
    }

    public final static int
    LOGGER_RATE = 1;
    
    public final static String
    LOG_FILE_PATH = "/home/admin/";
}
