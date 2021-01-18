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
public class HSV implements Color {
	private float hue, saturation, value;

	public static HSV of(float hue, float saturation, float value) {
		return new HSV(hue, saturation, value);
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
		return new HSV(hue, saturation, value);
	}

	@Override
	public LAB toLAB() {
		return toRGB().toLAB();
	}

	@Override
	public RGB toRGB() {
		int R = 0;                      //RGB results from 0 to 255
		int G = 0;
		int B = 0;
		if (saturation == 0) {                      //HSV from 0 to 1
			R = Math.round(value * 255);
			G = Math.round(value * 255);
			B = Math.round(value * 255);
		} else {
			float var_h = hue * 6;
			if (var_h == 6) var_h = 0;      //H must be < 1
			int var_i = (int) (var_h);             //Or ... var_i = floor( var_h )
			float var_1 = value * (1 - saturation);
			float var_2 = value * (1 - saturation * (var_h - var_i));
			float var_3 = value * (1 - saturation * (1 - (var_h - var_i)));

			float var_r, var_g, var_b;
			switch (var_i) {
				case 0:
					var_r = value;
					var_g = var_3;
					var_b = var_1;
					break;
				case 1:
					var_r = var_2;
					var_g = value;
					var_b = var_1;
					break;
				case 2:
					var_r = var_1;
					var_g = value;
					var_b = var_3;
					break;
				case 3:
					var_r = var_1;
					var_g = var_2;
					var_b = value;
					break;
				case 4:
					var_r = var_3;
					var_g = var_1;
					var_b = value;
					break;
				default:
					var_r = value;
					var_g = var_1;
					var_b = var_2;
			}

			R = Math.round(var_r * 255);                  //RGB results from 0 to 255
			G = Math.round(var_g * 255);
			B = Math.round(var_b * 255);
		}

		return RGB.of(R, G, B);
	}

	@Override
	public XYZ toXYZ() {
		return toRGB().toXYZ();
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
		return toRGB().toYXY();
	}
}
