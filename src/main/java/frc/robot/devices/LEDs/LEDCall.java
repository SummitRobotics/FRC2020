package frc.robot.devices.LEDs;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;

public class LEDCall {

    private int priority;
    private LEDRange range;

    private Color8Bit defaultColor;

    private int startLoop = 0;


    public LEDCall(int priority, LEDRange range) {
        this.priority = priority;
        this.range = range;

        defaultColor = new Color8Bit(Color.kBlack);

        startLoop = 0;
    }

    public int getPriority() {
        return priority;
    }

    public LEDRange getRange() {
        return range;
    }

    public Color8Bit getColor(int loop, int led) {
        return defaultColor;
    }

    public LEDCall solid(Color8Bit color) {
        return new LEDCall(priority, range) {
            @Override
            public Color8Bit getColor(int loop, int led) {
                return color;
            }
        };
    }

    public LEDCall flashing(Color8Bit onColor, Color8Bit offColor) {
        return new LEDCall(priority, range) {
            @Override
            public Color8Bit getColor(int loop, int led) {
                int time = loop % 40;
                if (time < 20) {
                    return onColor;

                } else {
                    return offColor;
                }    
            }
        };
    }

    public LEDCall ffh(Color8Bit onColor, Color8Bit offColor) {
        return new LEDCall(priority, range) {
            @Override
            public Color8Bit getColor(int loop, int led) {
                if(startLoop == 0){
                    startLoop = loop;
                }
                int time = loop - startLoop;
                if (time <= 10) {
                    return onColor;

                } else if (time <= 20) {
                    return offColor;

                } else if(time <= 30) {
                    return onColor;

                } else if(time <= 40) {
                    return offColor;

                } else {

                    return onColor;
                }
            }
        };
    }

    public LEDCall sine(Color8Bit color) {
        return new LEDCall(priority, range) {
            private final double waveLength = 6.068;

            @Override
            public Color8Bit getColor(int loop, int led) {
                double scaler = (Math.cos((((led * Math.PI) / waveLength) - (loop/4)) + 3)+1) * 0.25;
                return new Color8Bit(
                    (int) ((color.red * scaler) + 0.5),
                    (int) ((color.green * scaler) + 0.5),
                    (int) ((color.blue * scaler) +.5)
                );
            }
        };
    }
}