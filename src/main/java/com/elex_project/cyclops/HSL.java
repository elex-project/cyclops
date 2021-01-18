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
public class HSL implements Color {
	private float hue, saturation, lightness;

	private HSL(float hue, float saturation, float lightness) {
		this.hue = hue;
		this.saturation = saturation;
		this.lightness = lightness;
	}

	public static HSL of(float hue, float saturation, float lightness) {
		return new HSL(hue, saturation, lightness);
	}

	/**
	 * Blend between {@code this} and {@code color} using the given ratio. This will interpolate
	 * the hue using the shortest angle.
	 *
	 * <p>A blend ratio of 0.0 will result in {@code hsl1}, 0.5 will give an even blend,
	 * 1.0 will result in {@code hsl2}.</p>
	 *
	 * @param ratio the blend ratio of {@code hsl1} to {@code hsl2} 0.0 ~1.0
	 */
	public void blend(HSL color, float ratio) {
		final float inverseRatio = 1 - ratio;
		// Since hue is circular we will need to interpolate carefully
		hue = Utils.circularInterpolate(this.hue, color.hue, ratio);
		saturation = this.saturation * inverseRatio + color.saturation * ratio;
		lightness = this.lightness * inverseRatio + color.lightness * ratio;
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
		return toRGB().toHSV();
	}

	@Override
	public LAB toLAB() {
		return toRGB().toLAB();
	}

	/**
	 * Convert HSL (hue-saturation-lightness) components to a RGB color.
	 * <ul>
	 * <li>hsl[0] is Hue [0 .. 360)</li>
	 * <li>hsl[1] is Saturation [0...1]</li>
	 * <li>hsl[2] is Lightness [0...1]</li>
	 * </ul>
	 * If hsv values are out of range, they are pinned.
	 *
	 * @return the resulting RGB color
	 */
	@Override
	public RGB toRGB() {
		//When 0 ≤ H < 360, 0 ≤ S ≤ 1 and 0 ≤ L ≤ 1:
		while (hue < 0 || hue >= 360) {
			if (hue < 0) {
				hue += 360;
			} else if (hue >= 360) {
				hue -= 360;
			}
		}

		float c = (1 - Math.abs(2 * lightness - 1)) * saturation;
		float x = c * (1 - Math.abs((hue / 60) % 2 - 1));
		float m = lightness - c / 2;

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
