package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.lists.Colors;
import frc.robot.utilities.Functions;
import frc.robot.utilities.lists.Ports;
import frc.robot.utilities.lists.StatusPrioritys;
import frc.robot.oi.shufhellboardwidgets.StatusDisplayWidget;

/**
 * Subsystem to control the shooter
 */
public class Shooter extends SubsystemBase {

    private TalonFX shooterMotor;
    private TalonFXSensorCollection shooterEncoder;
    private DoubleSolenoid coolerSolenoid;

    private NetworkTableEntry speed;
    private NetworkTableEntry temp;
    private StatusDisplayWidget status;
    private boolean overTempStatus;

    public Shooter(NetworkTableEntry speed, NetworkTableEntry temp, StatusDisplayWidget status) {
        this.speed = speed;
        this.temp = temp;
        this.status = status;

        shooterMotor = new TalonFX(Ports.SHOOTER);
        shooterEncoder = new TalonFXSensorCollection(shooterMotor);
        coolerSolenoid = new DoubleSolenoid(Ports.PCM_1, Ports.COOLER_OPEN, Ports.COOLER_CLOSE);

        overTempStatus = false;

        shooterMotor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 60, 60, 4));
        coolerSolenoid.set(Value.kReverse);
        shooterMotor.setInverted(true);
    }

    /**
     * Sets the shooter power
     * @param power the new power
     */
    public void setPower(double power) {
        power = Functions.clampDouble(power, 1, -1);
        shooterMotor.set(ControlMode.PercentOutput, power);
    }

    /**
     * Stops the motor
     */
    public void stop() {
        shooterMotor.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Gets the velocity of the shooter
     * @return the velocity
     */
    public double getShooterRPM() {
        return (shooterEncoder.getIntegratedSensorVelocity()*600)/2048;
    }

    public double getShooterTemperature() {
        return shooterMotor.getTemperature();
    }

    public double getShooterCurrentDraw() {
        return shooterMotor.getSupplyCurrent();
    }

    public void setCoolerSolenoid(boolean value) {
        coolerSolenoid.set(value ? Value.kForward : Value.kReverse);
    }

    @Override
    public void periodic() {
        double tempVal = getShooterTemperature();
        temp.forceSetNumber(tempVal);
        speed.forceSetNumber(getShooterRPM());
        if(tempVal > 75 && !overTempStatus){
            overTempStatus = true;
            status.addStatus("shooterOvertemp", "shooter over 75c", Colors.Red, StatusPrioritys.shooterOver75);
        }
        else if(tempVal <= 75 && overTempStatus){
            status.removeStatus("shooterOvertemp");
            overTempStatus = false;
        }
    }
   
}