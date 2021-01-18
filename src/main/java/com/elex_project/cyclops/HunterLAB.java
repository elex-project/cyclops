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
public class HunterLAB implements Color {
	private float L, a, b;

	public static HunterLAB of(final float lightness, final float a, final float b) {
		return new HunterLAB(lightness, a, b);
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
		float var_Y = L / 10f;
		float var_X = a / 17.5f * L / 10f;
		float var_Z = b / 7f * L / 10f;

		float Y = (float) Math.pow(var_Y, 2);
		float X = ( var_X + Y ) / 1.02f;
		float Z = -( var_Z - Y ) / 0.847f;

		return XYZ.of(X, Y, Z);
	}

	@Override
	public CMY toCMY() {
		return toXYZ().toCMY();
	}

	@Override
	public HunterLAB toHunterLAB() {
		return new HunterLAB(L, a, b);
	}

	@Override
	public LCH toLCH() {
		return toXYZ().toLCH();
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
