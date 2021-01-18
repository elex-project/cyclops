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
public class HSV implements Color {
	private float hue, saturation, value;

	private HSV(float hue, float saturation, float value) {
		this.hue = hue;
		this.saturation = saturation;
		this.value = value;
	}

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
		//When 0 ≤ H < 360, 0 ≤ S ≤ 1 and 0 ≤ V ≤ 1:
		while (hue < 0 || hue >= 360) {
			if (hue < 0) {
				hue += 360;
			} else if (hue >= 360) {
				hue -= 360;
			}
		}

		float c = value * saturation;
		float x = c * (1 - Math.abs((hue / 60) % 2 - 1));
		float m = value - c;
		float r, g, b;
		if (hue < 60) {
			r = c;
			g = x;
			b = 0;
		} else if (hue < 120) {
			r = x;
			g = c;
			b = 0;
		} else if (hue < 180) {
			r = 0;
			g = c;
			b = x;
		} else if (hue < 240) {
			r = 0;
			g = x;
			b = c;
		} else if (hue < 300) {
			r = x;
			g = 0;
			b = c;
		} else {
			r = c;
			g = 0;
			b = x;
		}

		return RGB.of(Math.round((r + m) * 255), Math.round((g + m) * 255), Math.round((b + m) * 255));
	}

	@Override
	public XYZ toXYZ() {
		return toRGB().toXYZ();
	}
}
