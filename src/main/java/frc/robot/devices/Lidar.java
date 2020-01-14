// package frc.robot.devices;

// import java.nio.ByteBuffer;

// import edu.wpi.first.hal.I2CJNI;
// import edu.wpi.first.wpilibj.I2C.Port;;

// public class Lidar{


//     public Lidar(){
//         m_port = (byte) Port.kOnboard.value;
// 		I2CJNI.i2CInitialize(m_port);
// 		startMeasuring();
//     }
	
// 	//magic stuff
//     private static final byte k_deviceAddress = 0x62;

// 	private final byte m_port;

// 	private final ByteBuffer m_buffer = ByteBuffer.allocateDirect(2);

// 	/**
// 	 * tels the lidar to start mesuring
// 	 * called automaticly in the contrtuctor so you also probably will never need to use it
// 	 */
// 	public void startMeasuring() {
// 		writeRegister(0x04, 0x08 | 32); // default plus bit 5
// 		//sets it to continusly mesure
// 		writeRegister(0x11, 0xff);
// 		//sets speed of mesurments 0x14 is the deafult value
// 		writeRegister(0x45, 0x14);
// 		//tels devise to take musurement and then it will continue to mesure beacuse of above command
// 		writeRegister(0x00, 0x04);
		
// 	}

// 	/**
// 	 * stops the lidar frfom continuing to take mesurements
// 	 * you should probably never use this
// 	 */
// 	public void stopMeasuring() {
// 		//tels device to set musurements per mesure command to 1 causing it to stop mesuring
// 		writeRegister(0x11, 0x00);
// 	}

// 	/**
// 	 * gets the mesurement from the lidar
// 	 * @return the distance the lidar reads in cm
// 	 */
// 	public int getDistance() {
// 		return readShort(0x8f);
// 	}

// 	//more magic
// 	private int writeRegister(int address, int value) {
// 		m_buffer.put(0, (byte) address);
// 		m_buffer.put(1, (byte) value);

// 		return I2CJNI.i2CWrite(m_port, k_deviceAddress, m_buffer, (byte) 2);
// 	}

// 	//no idea how this works
// 	private short readShort(int address) {
// 		m_buffer.put(0, (byte) address);
// 		I2CJNI.i2CWrite(m_port, k_deviceAddress, m_buffer, (byte) 1);
// 		I2CJNI.i2CRead(m_port, k_deviceAddress, m_buffer, (byte) 2);
//         return m_buffer.getShort(0);
//     }

// }

package frc.robot.devices;

import java.nio.ByteBuffer;

import edu.wpi.first.hal.I2CJNI;
import edu.wpi.first.wpilibj.I2C.Port;;

public class Lidar{


    public Lidar(){
        m_port = (byte) Port.kOnboard.value;
		I2CJNI.i2CInitialize(m_port);
    }
    
    private static final byte k_deviceAddress = 0x62;

	private final byte m_port;

	private final ByteBuffer m_buffer = ByteBuffer.allocateDirect(2);

	public void startMeasuring() {
		writeRegister(0x04, 0x08 | 32); // default plus bit 5
		writeRegister(0x11, 0xff);
		writeRegister(0x00, 0x04);
	}

	public void stopMeasuring() {
		writeRegister(0x11, 0x00);
	}

	public int getDistance() {
		return readShort(0x8f);
	}

	private int writeRegister(int address, int value) {
		m_buffer.put(0, (byte) address);
		m_buffer.put(1, (byte) value);

		return I2CJNI.i2CWrite(m_port, k_deviceAddress, m_buffer, (byte) 2);
	}

	private short readShort(int address) {
		m_buffer.put(0, (byte) address);
		I2CJNI.i2CWrite(m_port, k_deviceAddress, m_buffer, (byte) 1);
		I2CJNI.i2CRead(m_port, k_deviceAddress, m_buffer, (byte) 2);
        return m_buffer.getShort(0);
    }



}