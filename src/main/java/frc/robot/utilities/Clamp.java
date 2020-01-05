package frc.robot.utilities;

public class Clamp{


/**
 * clamps a double
 * @param in the input value to clamp
 * @param max the maximum you want it to be
 * @param min the minimum for it to be
 * @return the clamped double
 */    
public static double clampDouble(double in, double max, double min){
    if(in>max){
        return max;
    }
    else if(in<min){
        return min;
    }
    else{
        return in;
    }
}

}