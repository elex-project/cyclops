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
public class YXY implements Color {
	private float Y, x, y;

	public static YXY of(float Y, float x, float y) {
		return new YXY(Y, x, y);
	}

	@Override
	public CMYK toCMYK() {
		return toCMY().toCMYK();
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
		return toRGB().toLAB();
	}

	@Override
	public RGB toRGB() {
		return toXYZ().toRGB();
	}

	@Override
	public XYZ toXYZ() {
		//Y from 0 to 100
		//x from 0 to 1
		//y from 0 to 1

		float X = x * (Y / y);
		//Y = Y;
		float Z = (1 - x - y) * (Y / y);

		return XYZ.of(X, Y, Z);
	}

	@Override
	public CMY toCMY() {
		return toRGB().toCMY();
	}

	@Override
	public HunterLAB toHunterLAB() {
		return toRGB().toHunterLAB();
	}

	@Override
	public LCH toLCH() {
		return toRGB().toLCH();
	}

	@Override
	public LUV toLUV() {
		return toRGB().toLUV();
	}

	@Override
	public YXY toYXY() {
		return new YXY(Y, x, y);
	}
}
