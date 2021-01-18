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
public class CMY implements Color {

	private float cyan, magenta, yellow;

	public static CMY of(float cyan, float magenta, float yellow) {
		return new CMY(cyan, magenta, yellow);
	}

	@Override
	public CMYK toCMYK() {
		float C = cyan;
		float M = magenta;
		float Y = yellow;
		float var_K = 1;

		if (C < var_K) var_K = C;
		if (M < var_K) var_K = M;
		if (Y < var_K) var_K = Y;
		if (var_K == 1) { //Black
			C = 0;
			M = 0;
			Y = 0;
		} else {
			C = (C - var_K) / (1 - var_K);
			M = (M - var_K) / (1 - var_K);
			Y = (Y - var_K) / (1 - var_K);
		}
		float K = var_K;
		return CMYK.of(C, M, Y, K);
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
		//CMY values from 0 to 1
		//RGB results from 0 to 255
		int R = Math.round((1 - cyan) * 255);
		int G = Math.round((1 - magenta) * 255);
		int B = Math.round((1 - yellow) * 255);

		return RGB.of(R, G, B);
	}

	@Override
	public XYZ toXYZ() {
		return toRGB().toXYZ();
	}

	@Override
	public CMY toCMY() {
		return new CMY(cyan, magenta, yellow);
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
