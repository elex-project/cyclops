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
public class LUV implements Color {
	private float L, u, v;

	public static LUV of(float L, float u, float v) {
		return new LUV(L, u, v);
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
		return toXYZ().toLAB();
	}

	@Override
	public RGB toRGB() {
		return toXYZ().toRGB();
	}

	@Override
	public XYZ toXYZ() {
		float var_Y = (L + 16) / 116f;
		if (Math.pow(var_Y, 3) > 0.008856) {
			var_Y = (float) Math.pow(var_Y, 3);
		} else {
			var_Y = (var_Y - 16 / 116f) / 7.787f;
		}

		float ref_X = 95.047f;      //Observer= 2°, Illuminant= D65
		float ref_Y = 100.000f;
		float ref_Z = 108.883f;

		float ref_U = (4 * ref_X) / (ref_X + (15 * ref_Y) + (3 * ref_Z));
		float ref_V = (9 * ref_Y) / (ref_X + (15 * ref_Y) + (3 * ref_Z));

		float var_U = u / (13 * L) + ref_U;
		float var_V = v / (13 * L) + ref_V;

		float Y = var_Y * 100;
		float X = -(9 * Y * var_U) / ((var_U - 4) * var_V - var_U * var_V);
		float Z = (9 * Y - (15 * var_V * Y) - (var_V * X)) / (3 * var_V);

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
		return toXYZ().toLCH();
	}

	@Override
	public LUV toLUV() {
		return new LUV(L, u, v);
	}

	@Override
	public YXY toYXY() {
		return toXYZ().toYXY();
	}
}
