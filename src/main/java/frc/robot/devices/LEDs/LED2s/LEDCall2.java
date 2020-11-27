package frc.robot.devices.LEDs.LED2s;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;

public class LEDCall2 {

    private int priority;
    private LEDRange2 range;

    private Color8Bit defaultColor;

    private int startLoop;


    public LEDCall2(int priority, LEDRange2 range) {
        this.priority = priority;
        this.range = range;

        defaultColor = new Color8Bit(Color.kBlack);

        startLoop = 0;
    }

    public int getPriority() {
        return priority;
    }

    public LEDRange2 getRange() {
        return range;
    }

    public void init(int loop) {
        startLoop = loop;
    }

    public Color8Bit getColor(int loop, int led) {
        return defaultColor;
    }

    public LEDCall2 solid(Color8Bit color) {
        return new LEDCall2(priority, range) {
            @Override
            public Color8Bit getColor(int loop, int led) {
                return color;
            }
        };
    }

    public LEDCall2 flashing(Color8Bit onColor, Color8Bit offColor) {
        return new LEDCall2(priority, range) {
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

    public LEDCall2 ffh(Color8Bit onColor, Color8Bit offColor) {
        return new LEDCall2(priority, range) {
            @Override
            public Color8Bit getColor(int loop, int led) {
                int time = loop - startLoop;
                if (time <= 5) {
                    return onColor;

                } else if (time <= 10) {
                    return offColor;

                } else if(time <= 15) {
                    return onColor;

                } else if(time <= 20) {
                    return offColor;

                } else {

                    return onColor;
                }
            }
        };
    }

    public LEDCall2 sine(Color8Bit color) {
        return new LEDCall2(priority, range) {
            private final double waveLength = 6.068;

            @Override
            public Color8Bit getColor(int loop, int led) {
                double scaler = (Math.cos((((led * Math.PI) / waveLength) - loop) + 3)) * 0.25;
                return new Color8Bit(
                    (int) ((color.red * scaler) + 0.5),
                    (int) ((color.green * scaler) + 0.5),
                    (int) ((color.blue * scaler) +.5)
                );
            }
        };
    }
}