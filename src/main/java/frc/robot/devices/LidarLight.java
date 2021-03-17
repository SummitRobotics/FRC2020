package frc.robot.devices;

import frc.robot.utilities.Functions;

public class LidarLight {
    
    //TODO - make good
	private final double acceptableLidarVSLimelightDiscrepancy = 20;


    public Lemonlight limelight;
    public Lidar lidar;

    private int lidarBadDistanceReading = 0;
    
    public LidarLight(Lemonlight limelight, Lidar lidar) {
        this.limelight = limelight;
        this.lidar = lidar;
    }

	public double getBestDistance(){
		double lidarDistance = lidar.getCompensatedLidarDistance(lidar.getAverageDistance());
		double limeLightDistance = limelight.getLimelightDistanceEstimate(limelight.getVerticalOffset());
		// if the lidar is to far from the limelight distance we use the limelight estimate beacuse it should be more reliable but less acurate
		if (Functions.isWithin(lidarDistance, limeLightDistance, acceptableLidarVSLimelightDiscrepancy)) {
			lidarBadDistanceReading = 0;
			return lidarDistance;
		} else {
			lidarBadDistanceReading++;
			return limeLightDistance;
		}
    }
    
    public boolean isLidarBeingBad() {
        return lidarBadDistanceReading > 20;
    }

    public double getHorizontalOffset() {
        return limelight.getHorizontalOffset();
    }

    public boolean hasTarget() {
        return limelight.hasTarget();
    }
}