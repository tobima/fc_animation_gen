/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.c3ma.animation.color;

import java.awt.Color;

/**
 *
 * @author tobi
 */
public class RainbowColor {
    public static Color mapRainbowColor(float value, float red_value, float blue_value)
    {
        // Convert into a value between 0 and 1023.
        int int_value = (int)(1023 * (value - red_value) / (blue_value - red_value));

        // Map different color bands.
        if (int_value < 256)
        {
            // Red to yellow. (255, 0, 0) to (255, 255, 0).
            return new Color(255, int_value, 0);
        }
        else if (int_value < 512)
        {
            // Yellow to green. (255, 255, 0) to (0, 255, 0).
            int_value -= 256;
            return new Color(255 - int_value, 255, 0);
        }
        else if (int_value < 768)
        {
            // Green to aqua. (0, 255, 0) to (0, 255, 255).
            int_value -= 512;
            return new Color(0, 255, int_value);
        }
        else
        {
            // Aqua to blue. (0, 255, 255) to (0, 0, 255).
            int_value -= 768;
            return new Color(0, 255 - int_value, 255);
        }
    
    }
}
