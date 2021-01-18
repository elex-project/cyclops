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
public class XYZ implements Color {
	static final double XYZ_WHITE_REFERENCE_X = 95.047;
	static final double XYZ_WHITE_REFERENCE_Y = 100;
	static final double XYZ_WHITE_REFERENCE_Z = 108.883;
	static final double XYZ_EPSILON = 0.008856;
	static final double XYZ_KAPPA = 903.3;

	private float x, y, z;

	/**
	 * @param x X component value [0...95.047)
	 * @param y Y component value [0...100)
	 * @param z Z component value [0...108.883)
	 */
	public static XYZ of(float x, float y, float z) {
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
		//float X = x;
		//float Y = y;
		//float Z = z;

		float ref_X = 95.047f;
		float ref_Y = 100.000f;
		float ref_Z = 108.883f;
		float var_X = x / ref_X;          //ref_X =  95.047   Observer= 2°, Illuminant= D65
		float var_Y = y / ref_Y;          //ref_Y = 100.000
		float var_Z = z / ref_Z;          //ref_Z = 108.883

		if (var_X > 0.008856) {
			var_X = (float) Math.pow(var_X, 1 / 3f);
		} else {
			var_X = (float) ((7.787 * var_X) + (16 / 116));
		}
		if (var_Y > 0.008856) {
			var_Y = (float) Math.pow(var_Y, 1 / 3f);
		} else {
			var_Y = (float) ((7.787 * var_Y) + (16 / 116));
		}
		if (var_Z > 0.008856) {
			var_Z = (float) Math.pow(var_Z, 1 / 3f);
		} else {
			var_Z = (float) ((7.787 * var_Z) + (16 / 116));
		}

		float L = (116 * var_Y) - 16;
		float a = 500 * (var_X - var_Y);
		float b = 200 * (var_Y - var_Z);

		return LAB.of(L, a, b);
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
		float var_X = x / 100f;        //X from 0 to  95.047      (Observer = 2°, Illuminant = D65)
		float var_Y = y / 100f;        //Y from 0 to 100.000
		float var_Z = z / 100f;        //Z from 0 to 108.883

		float var_R = var_X * 3.2406f + var_Y * -1.5372f + var_Z * -0.4986f;
		float var_G = var_X * -0.9689f + var_Y * 1.8758f + var_Z * 0.0415f;
		float var_B = var_X * 0.0557f + var_Y * -0.2040f + var_Z * 1.0570f;

		if (var_R > 0.0031308) {
			var_R = (float) (1.055f * Math.pow(var_R, 1 / 2.4f) - 0.055f);
		} else {
			var_R = 12.92f * var_R;
		}
		if (var_G > 0.0031308) {
			var_G = (float) (1.055f * Math.pow(var_G, 1 / 2.4f) - 0.055f);
		} else {
			var_G = 12.92f * var_G;
		}
		if (var_B > 0.0031308) {
			var_B = (float) (1.055f * Math.pow(var_B, 1 / 2.4f) - 0.055f);
		} else {
			var_B = 12.92f * var_B;
		}

		int R = Math.round(var_R * 255);
		int G = Math.round(var_G * 255);
		int B = Math.round(var_B * 255);

		return RGB.of(R, G, B);
	}

	@Override
	public XYZ toXYZ() {
		return new XYZ(x, y, z);
	}

	@Override
	public CMY toCMY() {
		return toRGB().toCMY();
	}

	@Override
	public HunterLAB toHunterLAB() {
		float L = (float) (10 * Math.sqrt(y));
		float a = (float) (17.5 * (((1.02 * x) - y) / Math.sqrt(y)));
		float b = (float) (7 * ((y - (0.847 * z)) / Math.sqrt(y)));

		return HunterLAB.of(L, a, b);
	}

	@Override
	public LCH toLCH() {
		return toRGB().toLCH();
	}

	@Override
	public LUV toLUV() {
		//float X = xyz[0]; float Y = xyz[1]; float Z = xyz[2];

		float var_U = (4 * x) / (x + (15 * y) + (3 * z));
		float var_V = (9 * y) / (x + (15 * y) + (3 * z));

		float var_Y = y / 100f;
		if (var_Y > 0.008856) {
			var_Y = (float) Math.pow(var_Y, 1 / 3f);
		} else {
			var_Y = (float) ((7.787 * var_Y) + (16 / 116f));
		}

		float ref_X = 95.047f;        //Observer= 2°, Illuminant= D65
		float ref_Y = 100.000f;
		float ref_Z = 108.883f;

		float ref_U = (4 * ref_X) / (ref_X + (15 * ref_Y) + (3 * ref_Z));
		float ref_V = (9 * ref_Y) / (ref_X + (15 * ref_Y) + (3 * ref_Z));

		float L = (116 * var_Y) - 16;
		float u = 13 * L * (var_U - ref_U);
		float v = 13 * L * (var_V - ref_V);

		return LUV.of(L, u, v);
	}

	@Override
	public YXY toYXY() {
		//float X = xyz[0]; float Y = xyz[1]; float Z = xyz[2];

		//X from 0 to 95.047       Observer. = 2°, Illuminant = D65
		//Y from 0 to 100.000
		//Z from 0 to 108.883

		//float Y = Y;
		float X = x / (x + y + z);
		float Y = y / (x + y + z);

		return YXY.of(y, X, Y);
	}
}
