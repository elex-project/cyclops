/*
 * Project Cyclops
 *
 * Copyright (c) 2021. Elex.
 * All Rights Reserved.
 */

package com.elex_project.cyclops;

public class Harmony {
	//This is the typical configuration of three colors that are equally spaced from each other on the color wheel.
	public static int[] triadics(int color) {
		return harmonies(color, 120);
	}

	//This color scheme combines the two colors on either side of a color’s complement.
	public static int[] splitComplements(int color) {
		return harmonies(color, 150);
	}

	//Uses the colors of the same color temperature near each other on the wheel.
	public static int[] analogouses(int color) {
		return harmonies(color, 30);
	}

	//Colors from the same family on the wheel. This will include lighter, darker and differently saturated versions of the color.
	public static int[] monochromatics(int color) {//TODO more
		return null;	//H° + 0°	(C* will change)
	}

	//This is the color opposite on the color wheel.
	public static int complement(int color) {
		LCH col = RGB.of(color).toLCH();
		col.setL(wheel(col.getL(), 180));
		return col.toRGB().getColor();
	}

	private static int[] harmonies(int color, int degree) {
		int[] colors = new int[2];
		LCH lch = RGB.of(color).toLCH();
		colors[0] = LCH.of(wheel(lch.getL(), degree), lch.getC(), lch.getH()).toRGB().getColor();
		colors[1] = LCH.of(wheel(lch.getL(), -degree), lch.getC(), lch.getH()).toRGB().getColor();
		return colors;
	}
	private static float wheel(float hue, int deg) {
		float out = hue + deg;
		return out;
	}
}
