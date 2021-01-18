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
public class LCH implements Color {
	private float L, c, h;

	public static LCH of(float L, float c, float h) {
		return new LCH(L, c, h);
	}

	@Override
	public CMYK toCMYK() {
		return toLAB().toCMYK();
	}

	@Override
	public HSL toHSL() {
		return toLAB().toHSL();
	}

	@Override
	public HSV toHSV() {
		return toLAB().toHSV();
	}

	@Override
	public LAB toLAB() {
		//CIE-H° from 0 to 360°

		//L = L;
		float a = (float) ((Math.cos(Math.PI / 180f * h)) * c);
		float b = (float) ((Math.sin(Math.PI / 180f * h)) * c);

		return LAB.of(L, a, b);
	}

	@Override
	public RGB toRGB() {
		return toLAB().toRGB();
	}

	@Override
	public XYZ toXYZ() {
		return toLAB().toXYZ();
	}

	@Override
	public CMY toCMY() {
		return toLAB().toCMY();
	}

	@Override
	public HunterLAB toHunterLAB() {
		return toLAB().toHunterLAB();
	}

	@Override
	public LCH toLCH() {
		return new LCH(L, c, h);
	}

	@Override
	public LUV toLUV() {
		return toLAB().toLUV();
	}

	@Override
	public YXY toYXY() {
		return toLAB().toYXY();
	}
}
