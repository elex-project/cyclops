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
public class XYZ implements Color {
	static final double XYZ_WHITE_REFERENCE_X = 95.047;
	static final double XYZ_WHITE_REFERENCE_Y = 100;
	static final double XYZ_WHITE_REFERENCE_Z = 108.883;
	static final double XYZ_EPSILON = 0.008856;
	static final double XYZ_KAPPA = 903.3;

	private double x, y, z;

	/**
	 * @param x X component value [0...95.047)
	 * @param y Y component value [0...100)
	 * @param z Z component value [0...108.883)
	 */
	private XYZ(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static XYZ of(double x, double y, double z) {
		return new XYZ(x, y, z);
	}

	private static double pivotXyzComponent(double component) {
		return component > XYZ_EPSILON
				? Math.pow(component, 1 / 3.0)
				: (XYZ_KAPPA * component + 16) / 116;
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

	/**
	 * Converts a color from CIE XYZ to CIE LAB representation.
	 *
	 * <p>This method expects the XYZ representation to use the D65 illuminant and the CIE
	 * 2° Standard Observer (1931).</p>
	 *
	 * <ul>
	 * <li>outLab[0] is L [0 ...1)</li>
	 * <li>outLab[1] is a [-128...127)</li>
	 * <li>outLab[2] is b [-128...127)</li>
	 * </ul>
	 */
	@Override
	public LAB toLAB() {
		x = pivotXyzComponent(x / XYZ_WHITE_REFERENCE_X);
		y = pivotXyzComponent(y / XYZ_WHITE_REFERENCE_Y);
		z = pivotXyzComponent(z / XYZ_WHITE_REFERENCE_Z);

		return LAB.of(
				Math.max(0, 116 * y - 16),
				500 * (x - y),
				200 * (y - z)
		);
	}

	/**
	 * Converts a color from CIE XYZ to its RGB representation.
	 *
	 * <p>This method expects the XYZ representation to use the D65 illuminant and the CIE
	 * 2° Standard Observer (1931).</p>
	 *
	 * @return int containing the RGB representation
	 */
	@Override
	public RGB toRGB() {
		double r = (x * 3.2406 + y * -1.5372 + z * -0.4986) / 100;
		double g = (x * -0.9689 + y * 1.8758 + z * 0.0415) / 100;
		double b = (x * 0.0557 + y * -0.2040 + z * 1.0570) / 100;

		r = r > 0.0031308 ? 1.055 * Math.pow(r, 1 / 2.4) - 0.055 : 12.92 * r;
		g = g > 0.0031308 ? 1.055 * Math.pow(g, 1 / 2.4) - 0.055 : 12.92 * g;
		b = b > 0.0031308 ? 1.055 * Math.pow(b, 1 / 2.4) - 0.055 : 12.92 * b;

		return RGB.of(
				Utils.constrain((int) Math.round(r * 255), 0, 255),
				Utils.constrain((int) Math.round(g * 255), 0, 255),
				Utils.constrain((int) Math.round(b * 255), 0, 255)
		);
	}

	@Override
	public XYZ toXYZ() {
		return new XYZ(x, y, z);
	}
}
