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
public class CMYK implements Color {
	private float cyan, magenta, yellow, black;

	private CMYK(float cyan, float magenta, float yellow, float black) {
		this.cyan = cyan;
		this.magenta = magenta;
		this.yellow = yellow;
		this.black = black;
	}

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
		//The R,G,B values are given in the range of 0..255.
		//The red (R) color is calculated from the cyan (C) and black (K) colors:
		int r = Math.round(255 * (1 - cyan) * (1 - black));
		//The green color (G) is calculated from the magenta (M) and black (K) colors:
		int g = Math.round(255 * (1 - magenta) * (1 - black));
		//The blue color (B) is calculated from the yellow (Y) and black (K) colors:
		int b = Math.round(255 * (1 - yellow) * (1 - black));

		return RGB.of(r, g, b);
	}

	@Override
	public XYZ toXYZ() {
		return toRGB().toXYZ();
	}
}
