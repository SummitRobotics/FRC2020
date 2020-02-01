package frc.robot.devices;

import java.util.Arrays;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

import frc.robot.utilities.RollingAverage;

public class LidarV4{

	private I2C i2c;
	private int value;

	private RollingAverage rollingAverage;

    public LidarV4(int id){
		i2c = new I2C(Port.kOnboard, id);
		value = 0;

		rollingAverage = new RollingAverage(50);
	}

	private void readDistance() {
		byte[] status = new byte[1];

		//checks if there is a valid mesurment
		i2c.read(0x01, 1, status);
		if ((status[0] & 0x01) == 0) {
			byte[] low = new byte[1];
			byte[] high = new byte[1];

			//reads distance from lidar
			i2c.read(0x10, 1, low);
			i2c.read(0x11, 1, high);

			int out = high[0];

			//fixes java using signed bytes
			if(out < 0){
				out = ((high[0] & 0b01111111) + 128);
			}
			out = (out << 8);
			int out2 = low[0];
			if(out2 < 0){
				out2 = ((low[0] & 0b01111111) + 128);
			}
			out = out + out2;
			
			//tells lidar to take another measurement
			i2c.write(0x00, 0x04);

			//prevent bad values
			if (out < 1000){
				value = out;
			}
		}

		rollingAverage.update(value);
	}

	public int getDistance() {
		readDistance();
		return value;
	}

	public void changeId(int id){
		//enables flash
		i2c.write(0xEA, 0x11);
		//reads device id and saves it
		byte[] idBuffer = new byte[5];
		i2c.read(0x16, 4, idBuffer);
		idBuffer[4] = (byte) id;
		System.out.println(Arrays.toString(idBuffer));
		byte[] writeBuffer = new byte[6];
		writeBuffer[0] = 0x62;
		for (int i  = 1; i < 6; i++) {
			writeBuffer[i] = idBuffer[i-1];
		}
		//unlocks adress writing
		i2c.writeBulk(writeBuffer);
		//writes adress
		i2c.write(0x1B, 0x01);
		System.out.println("worked!");
	}

	public int getRollingAverage() {
		readDistance();
		return (int) rollingAverage.getAverage();
	}
}