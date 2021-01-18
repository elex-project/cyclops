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
public class HSL implements Color {
	private float hue, saturation, lightness;

	public static HSL of(float hue, float saturation, float lightness) {
		return new HSL(hue, saturation, lightness);
	}

	private static float hueToRgb(float v1, float v2, float vH) {
		if (vH < 0) vH += 1;
		if (vH > 1) vH -= 1;
		if ((6 * vH) < 1)
			return (v1 + (v2 - v1) * 6 * vH);
		if ((2 * vH) < 1)
			return (v2);
		if ((3 * vH) < 2)
			return (v1 + (v2 - v1) * ((2 / 3f) - vH) * 6);
		return (v1);
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
		int R = 0;                      //RGB results from 0 to 255
		int G = 0;
		int B = 0;
		if (saturation == 0) {                       //HSL from 0 to 1
			R = Math.round(lightness * 255);                      //RGB results from 0 to 255
			G = Math.round(lightness * 255);
			B = Math.round(lightness * 255);
		} else {
			float var_2;
			if (lightness < 0.5) {
				var_2 = lightness * (1 + saturation);
			} else {
				var_2 = (lightness + saturation) - (saturation * lightness);
			}
			float var_1 = 2 * lightness - var_2;

			R = Math.round(255 * hueToRgb(var_1, var_2, hue + (1 / 3f)));
			G = Math.round(255 * hueToRgb(var_1, var_2, hue));
			B = Math.round(255 * hueToRgb(var_1, var_2, hue - (1 / 3f)));
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
