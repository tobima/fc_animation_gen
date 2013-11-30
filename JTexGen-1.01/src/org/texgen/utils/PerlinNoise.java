/*
 * Texture Generator Library by Andy Gibson
 *
 * This software is distributed under the terms of the FSF Lesser
 * Gnu Public License. It is provided as-is, without any express
 * or implied warranty, or guarantee of any kind.
 *
 */
package org.texgen.utils;

/**
 * Class for creating noise values based on Ken Perlin's idea for Perlin Noise.
 * Original noise class used my own method for creating noise, but I switched it
 * to use Ken's reference implementation. However, for now, I still use this
 * wrapper for calling it. At some point I will try and optimize the function
 * for 2 dimensions since we use it most and it performs calculates on the
 * unnecessary z element.
 * 
 * @author Andy Gibson
 */
public class PerlinNoise {

	/**
	 * Returns a noise value by interpolating between two values based on the
	 * floating point part of the input.
	 * 
	 * @param x
	 *            Value to derive the noise from.
	 * @return Noise value
	 */
	public double noise1(double x) {
		return (ImprovedNoise.noise(x, 0, 0) + 1) * 0.5;
	}

	/**
	 * Returns a noise value by interpolating between two values based on the
	 * floating point part of the input.
	 * 
	 * @param x
	 *            Value to derive the noise from.
	 * @param y
	 *            Value to derive the noise from.
	 * @return Noise value
	 */
	public double noise2(double x, double y) {
		return (ImprovedNoise.noise(x, y, 0) + 1) * 0.5;
	}

	/**
	 * Returns a noise value by interpolating between two values based on the
	 * floating point part of the input.
	 * 
	 * @param x
	 *            Value to derive the noise from.
	 * @param y
	 *            Value to derive the noise from.
	 * @param z
	 *            Value to derive the noise from.
	 * @return Noise value
	 */
	public double noise3(double x, double y, double z) {
		return (ImprovedNoise.noise(x, y, z) + 1) * 0.5;
	}

	public double fbmNoise(double x, int octaves) {
		double res = 0;
		double amp = 1;
		double div = 0;
		for (int i = 0; i < octaves; i++) {
			div = div + amp;
			res += (noise1(x) * amp);
			x = x * 2;
			amp = amp / 2;
		}
		return res / div;
	}

	public double fbmNoise2(double x, double y, int octaves) {
		double res = 0;
		double amp = 1;
		double div = 0;
		for (int i = 0; i < octaves; i++) {
			div = div + amp;
			res += (noise2(x, y) * amp);
			x = x * 2;
			y = y * 2;
			amp = amp / 2;
		}
		return res / div;
	}

	public double fbmNoise3(double x, double y, double z, int octaves) {
		double res = 0;
		double amp = 1;
		double div = 0;
		for (int i = 0; i < octaves; i++) {
			div = div + amp;
			res += (noise3(x, y, z) * amp);
			x = x * 2;
			y = y * 2;
			z = z * 2;
			amp = amp / 2;
		}
		return res / div;
	}

	/**
	 * Takes a sine wave and perturbs it using noise. Repeats for muliple
	 * octaves
	 * 
	 * @param u
	 *            u value to generate the wave from
	 * @param v
	 *            v value to generate the wave from
	 * @param sineScale
	 *            Speed of the sine wave over the u,v values
	 * @param speed
	 *            Speed of the noise over the u,v values
	 * @param octaves
	 *            Number of frequencies to use
	 * @return The noisy sine value
	 */
	public double noisySine(double u, double v, double sineScale, double speed,
			int octaves) {

		double noiseValue = (fbmNoise2(u * speed, v * speed, octaves)) + u;

		double texture = Math.sin(u + (noiseValue * sineScale));

		texture = 1 - Math.abs(texture);

		return texture;
	}
}
