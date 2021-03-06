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
public class CMYK implements Color {
	private float cyan, magenta, yellow, black;

	public static CMYK of(float cyan, float magenta, float yellow, float black) {
		return new CMYK(cyan, magenta, yellow, black);
	}

	@Override
	public CMYK toCMYK() {
		return new CMYK(cyan, magenta, yellow, black);
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
		return toCMY().toRGB();
	}

	@Override
	public XYZ toXYZ() {
		return toRGB().toXYZ();
	}

	@Override
	public CMY toCMY() {
		//CMYK and CMY values from 0 to 1

		float C = (cyan * (1 - black) + black);
		float M = (magenta * (1 - black) + black);
		float Y = (yellow * (1 - black) + black);
		return CMY.of(C, M, Y);
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
		return toRGB().toYXY();
	}
}
