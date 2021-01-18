/*
 * Project Cyclops
 *
 * Copyright (c) 2021. Elex.
 * All Rights Reserved.
 */

package com.elex_project.cyclops;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LAB implements Color {
	private float L, a, b;

	/**
	 * @param L L component value [0...100)
	 * @param a A component value [-128...127)
	 * @param b B component value [-128...127)
	 */
	public static LAB of(float L, float a, float b) {
		return new LAB(L, a, b);
	}

	/**
	 * Blend between two CIE-LAB colors using the given ratio.
	 *
	 * <p>A blend ratio of 0.0 will result in {@code lab1}, 0.5 will give an even blend,
	 * 1.0 will result in {@code lab2}.</p>
	 *
	 * @param ratio the blend ratio of {@code lab1} to {@code lab2}
	 */
	public void blend(LAB color, float ratio) {

		final float inverseRatio = 1 - ratio;
		L = this.L * inverseRatio + color.L * ratio;
		a = this.a * inverseRatio + color.a * ratio;
		b = this.b * inverseRatio + color.b * ratio;
	}

	@Override
	public CMYK toCMYK() {
		return toXYZ().toCMYK();
	}

	@Override
	public HSL toHSL() {
		return toXYZ().toHSL();
	}

	@Override
	public HSV toHSV() {
		return toXYZ().toHSV();
	}

	@Override
	public LAB toLAB() {
		return new LAB(L, a, b);
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
		float var_Y = (L + 16) / 116f;
		float var_X = a / 500 + var_Y;
		float var_Z = var_Y - b / 200f;

		if (Math.pow(var_Y, 3) > 0.008856) {
			var_Y = (float) Math.pow(var_Y, 3);
		} else {
			var_Y = (var_Y - 16 / 116f) / 7.787f;
		}
		if (Math.pow(var_X, 3) > 0.008856) {
			var_X = (float) Math.pow(var_X, 3);
		} else {
			var_X = (var_X - 16 / 116f) / 7.787f;
		}
		if (Math.pow(var_Z, 3) > 0.008856) {
			var_Z = (float) Math.pow(var_Z, 3);
		} else {
			var_Z = (var_Z - 16 / 116f) / 7.787f;
		}

		float ref_X = 95.047f;     //Observer= 2°, Illuminant= D65
		float ref_Y = 100.000f;
		float ref_Z = 108.883f;

		float X = ref_X * var_X;
		float Y = ref_Y * var_Y;
		float Z = ref_Z * var_Z;

		return XYZ.of(X, Y, Z);
	}

	@Override
	public CMY toCMY() {
		return toXYZ().toCMY();
	}

	@Override
	public HunterLAB toHunterLAB() {
		return toXYZ().toHunterLAB();
	}

	@Override
	public LCH toLCH() {
		//float L = lab[0];
		//float a = lab[1];
		//float b = lab[2];

		float var_H = (float) Math.atan2(b, a);  //Quadrant by signs //TODO check this

		if (var_H > 0) {
			var_H = (float) ((var_H / Math.PI) * 180);
		} else {
			var_H = (float) (360 - (Math.abs(var_H) / Math.PI) * 180);
		}

		//CIE-L* = CIE-L*;
		float C = (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
		float H = var_H;

		return LCH.of(L, C, H);
	}

	@Override
	public LUV toLUV() {
		return toXYZ().toLUV();
	}

	@Override
	public YXY toYXY() {
		return toXYZ().toYXY();
	}
}
