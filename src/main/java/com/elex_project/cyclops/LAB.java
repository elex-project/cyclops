/*
 * Project Cyclops
 *
 * Copyright (c) 2021. Elex.
 * All Rights Reserved.
 */

package com.elex_project.cyclops;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class LAB implements Color {
	private double lightness, a, b;

	/**
	 * @param lightness L component value [0...100)
	 * @param a         A component value [-128...127)
	 * @param b         B component value [-128...127)
	 */
	private LAB(double lightness, double a, double b) {
		this.lightness = lightness;
		this.a = a;
		this.b = b;
	}

	public static LAB of(double lightness, double a, double b) {
		return new LAB(lightness, a, b);
	}

	/**
	 * Blend between two CIE-LAB colors using the given ratio.
	 *
	 * <p>A blend ratio of 0.0 will result in {@code lab1}, 0.5 will give an even blend,
	 * 1.0 will result in {@code lab2}.</p>
	 *
	 * @param ratio the blend ratio of {@code lab1} to {@code lab2}
	 */
	public void blend(LAB color, double ratio) {

		final double inverseRatio = 1 - ratio;
		lightness = this.lightness * inverseRatio + color.lightness * ratio;
		a = this.a * inverseRatio + color.a * ratio;
		b = this.b * inverseRatio + color.b * ratio;
	}

	@Override
	public CMYK toCMYK() {
		return toRGB().toCMYK();
	}

	@Override
	public HSL toHSL() {
		return toRGB().toHSL();
	}

	@Override
	public HSV toHSV() {
		return toRGB().toHSV();
	}

	@Override
	public LAB toLAB() {
		return new LAB(lightness, a, b);
	}

	@Override
	public RGB toRGB() {
		return toXYZ().toRGB();
	}

	/**
	 * Converts a color from CIE LAB to CIE XYZ representation.
	 *
	 * <p>The resulting XYZ representation will use the D65 illuminant and the CIE
	 * 2° Standard Observer (1931).</p>
	 *
	 * <ul>
	 * <li>outXyz[0] is X [0 ...95.047)</li>
	 * <li>outXyz[1] is Y [0...100)</li>
	 * <li>outXyz[2] is Z [0...108.883)</li>
	 * </ul>
	 */
	@Override
	public XYZ toXYZ() {
		final double fy = (lightness + 16) / 116;
		final double fx = a / 500 + fy;
		final double fz = fy - b / 200;

		double tmp = Math.pow(fx, 3);
		final double xr = tmp > XYZ.XYZ_EPSILON ? tmp : (116 * fx - 16) / XYZ.XYZ_KAPPA;
		final double yr = lightness > XYZ.XYZ_KAPPA * XYZ.XYZ_EPSILON ? Math.pow(fy, 3) : lightness / XYZ.XYZ_KAPPA;

		tmp = Math.pow(fz, 3);
		final double zr = tmp > XYZ.XYZ_EPSILON ? tmp : (116 * fz - 16) / XYZ.XYZ_KAPPA;

		return XYZ.of(
				xr * XYZ.XYZ_WHITE_REFERENCE_X,
				yr * XYZ.XYZ_WHITE_REFERENCE_Y,
				zr * XYZ.XYZ_WHITE_REFERENCE_Z
		);
	}
}
