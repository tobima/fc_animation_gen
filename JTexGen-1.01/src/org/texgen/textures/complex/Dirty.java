/*
 * Texture Generator Library by Andy Gibson
 *
 * This software is distributed under the terms of the FSF Lesser
 * Gnu Public License. It is provided as-is, without any express
 * or implied warranty, or guarantee of any kind.
 *
 */
package org.texgen.textures.complex;

import org.texgen.textures.AbstractTexture;
import org.texgen.textures.Texture;
import org.texgen.textures.color.SolidTexture;
import org.texgen.textures.composite.MergedTexture;
import org.texgen.utils.RGBAColor;

/**
 * Takes an input texture and color (default black) and dirties the source using
 * a noisy texture
 * 
 * @author Andy Gibson
 */
public class Dirty extends AbstractTexture {

	private final MergedTexture texture;

	/**
	 * Dirties the source texture with a solid black color
	 * 
	 * @param source
	 *            The texture to have dirty put over it
	 * 
	 * Defaults the dirt to black with a speed of 0.5, a size of 10 and 10
	 * octaves
	 */
	public Dirty(Texture source) {
		this(source, RGBAColor.black());

	}

	/**
	 * Creates a texture which is black dirt over the source color.
	 * 
	 * @param sourceCol
	 *            Background color which is to be dirtied.
	 */
	public Dirty(RGBAColor sourceCol) {
		this(new SolidTexture(sourceCol), RGBAColor.black());

	}

	/**
	 * Creates a texture by dirtying a solid background of the given color using
	 * noise created using the noise parameters.
	 * 
	 * @param sourceCol
	 *            Background color to dirty.
	 * @param speed
	 *            Rate at which the noise moves across the texture.
	 * @param octaves
	 *            Number of frequencies to use when generating the fractal
	 *            noise.
	 * @param size
	 *            Value to scale the final dirt value by
	 */
	public Dirty(RGBAColor sourceCol, double speed, int octaves, double size) {
		this(new SolidTexture(sourceCol), RGBAColor.black(), speed, octaves,
				size);

	}

	/**
	 * 
	 * Dirties the source texture with the specified color
	 * 
	 * @param source
	 *            The texture to be dirtied
	 * @param color
	 *            The color of the dirt
	 */
	public Dirty(Texture source, RGBAColor color) {
		this(source, color, 10);
	}

	/**
	 * 
	 * @param source
	 *            The texture to be dirtied
	 * @param color
	 *            The color of the dirty
	 * @param speed
	 *            How fast the dirt changes across the surface (default 10)
	 */
	public Dirty(Texture source, RGBAColor color, double speed) {
		this(source, new SolidTexture(color), speed, 10, 0.5);
	}

	/**
	 * Dirties the source texture with the specified color using a noise
	 * function created from the parameters
	 * 
	 * @param source
	 *            The texture to be dirtied
	 * @param color
	 *            The solid color to use to color the dirt
	 * @param speed
	 *            Level of dirtying (default 0.5)
	 * @param size
	 *            Rate that the noise moves across the texture
	 * @param octaves
	 *            Number of octaves used to generate the noise
	 */
	public Dirty(Texture source, RGBAColor color, double speed, int octaves,
			double size) {
		this(source, new SolidTexture(color), speed, octaves, size);
	}

	/**
	 * Dirties the source texture with the specified color using a noise
	 * function created from the parameters 
	 * 
	 * @param source
	 *            The color to be dirtied
	 * @param color
	 *            The solid color to use to color the dirt
	 * @param speed
	 *            Level of dirtying (default 0.5)
	 * @param size
	 *            Rate that the noise moves across the texture
	 * @param octaves
	 *            Number of octaves used to generate the noise
	 */
	public Dirty(RGBAColor sourceColor, RGBAColor color, double speed,
			int octaves, double size) {
		this(new SolidTexture(sourceColor), new SolidTexture(color), speed,
				octaves, size);
	}

	/**
	 * Dirties the source texture with the specified color based on the
	 * parameters used. Throws an exception if the source texture is null.
	 * 
	 * @param source
	 *            The texture to be dirtied
	 * @param dirtTexture
	 *            The texture to use to color the dirt
	 * @param speed
	 *            Level of dirtying (default 0.5)
	 * @param octaves
	 *            Octaves used to generate the noise
	 * @param size
	 *            Rate that the noise moves across the texture
	 */
	public Dirty(Texture source, Texture dirtTexture, double speed,
			int octaves, double size) {
		if (source == null) {
			throw new IllegalArgumentException("Source texture cannot be null");
		}
		NoisyTexture dirt = new NoisyTexture(dirtTexture, speed, octaves, size);
		texture = new MergedTexture(source, dirt);
	}

	public void getColor(double u, double v, RGBAColor value) {
		calculateColorFromTexture(u, v, texture, value);
	}
}
