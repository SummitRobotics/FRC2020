package frc.robot.devices;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;


public class LidarV4{

	private I2C i2c = new I2C(Port.kOnboard, 0x62);
	private int value;

    public LidarV4(){
       value = 0;
	}

	public int getDistance() {
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
			if(out < 1000){
				value = out;
			}
		}
		return value;
	}
}