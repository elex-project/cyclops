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

import static com.elex_project.cyclops.Utils.*;

/**
 * RGB color
 *
 * <li>alpha [0..255]</li>
 * <li>red [0..255]</li>
 * <li>green [0..255]</li>
 * <li>blue [0..255]</li>
 *
 * @author Elex
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RGB implements Color {
	private int color;

	private RGB(int color) {
		this.color = color;
	}

	public static RGB of(int a, int r, int g, int b) {
		return new RGB(argb(a, r, g, b));
	}

	public static RGB of(String hex) {
		return new RGB(Integer.parseInt(hex, 16));
	}

	public static RGB of(int r, int g, int b) {
		return new RGB(rgb(r, g, b));
	}

	public static RGB of(int color) {
		return new RGB(color);
	}

	public String toHexString() {
		return Integer.toHexString(color);
	}

	public int alpha() {
		return color >> 24 & 0xFF;
	}

	public int red() {
		return color >> 16 & 0xFF;
	}

	public int green() {
		return color >> 8 & 0xFF;
	}

	public int blue() {
		return color & 0xFF;
	}

	public void setColor(int r, int g, int b) {
		this.color = rgb(r, g, b);
	}

	public void setAlpha(int alpha) {
		color = setAlphaComponent(color, alpha);
	}

	/**
	 * Returns the luminance of a color as a float between {@code 0.0} and {@code 1.0}.
	 * <p>Defined as the Y component in the XYZ representation of {@code color}.</p>
	 *
	 * @return 0.0~1.0
	 */
	public double luminance() {
		// Luminance is the Y component
		return toXYZ().getY() / 100;
	}

	public void blend(RGB c2, float ratio) {
		this.color = blendARGB(this.color, c2.color, ratio);
	}

	@Override
	public CMYK toCMYK() {
		// The R,G,B values are divided by 255 to change the range from 0..255 to 0..1:
		float r = red() / 255f;
		float g = green() / 255f;
		float b = blue() / 255f;
		//The black key (K) color is calculated from the red (R'), green (G') and blue (B') colors:
		float k = 1 - Utils.max(r, g, b);
		//
		float c, m, y;
		if (k == 1) {
			c = m = y = 0;
		} else {
			// The cyan color (C) is calculated from the red (R') and black (K) colors:
			c = (1 - r - k) / (1 - k);
			//The magenta color (M) is calculated from the green (G') and black (K) colors:
			m = (1 - g - k) / (1 - k);
			//The yellow color (Y) is calculated from the blue (B') and black (K) colors:
			y = (1 - b - k) / (1 - k);
		}

		return CMYK.of(c, m, y, k);
	}

	/**
	 * Convert RGB components to HSL (hue-saturation-lightness).
	 * <ul>
	 * <li>outHsl[0] is Hue [0 .. 360)</li>
	 * <li>outHsl[1] is Saturation [0...1]</li>
	 * <li>outHsl[2] is Lightness [0...1]</li>
	 * </ul>
	 *
	 * @return 3-element array which holds the resulting HSL components
	 */
	@Override
	public HSL toHSL() {
		final float rf = red() / 255f;
		final float gf = green() / 255f;
		final float bf = blue() / 255f;

		final float max = Utils.max(rf, gf, bf);
		final float min = Utils.min(rf, gf, bf);
		final float deltaMaxMin = max - min;

		float h, s;
		float l = (max + min) / 2f;

		if (max == min) {
			// Monochromatic
			h = s = 0f;
		} else {
			if (max == rf) {
				h = ((gf - bf) / deltaMaxMin) % 6f;
			} else if (max == gf) {
				h = ((bf - rf) / deltaMaxMin) + 2f;
			} else {
				h = ((rf - gf) / deltaMaxMin) + 4f;
			}

			s = deltaMaxMin / (1f - Math.abs(2f * l - 1f));
		}

		h = (h * 60f) % 360f;
		if (h < 0) {
			h += 360f;
		}

		return HSL.of(
				Utils.constrain(h, 0f, 360f),
				Utils.constrain(s, 0f, 1f),
				Utils.constrain(l, 0f, 1f));
	}

	@Override
	public HSV toHSV() {
		// The R,G,B values are divided by 255 to change the range from 0..255 to 0..1:
		float r = red() / 255f;
		float g = green() / 255f;
		float b = blue() / 255f;

		float cMax = Utils.max(r, g, b);
		float cMin = Utils.min(r, g, b);
		float delta = cMax - cMin;

		float h, s, v;
		if (delta == 0) {
			h = 0;
		} else {
			//Hue calculation:
			if (cMax == r) {
				h = 60 * (((g - b) / delta) % 6);
			} else if (cMax == g) {
				h = 60 * (((b - r) / delta) + 2);
			} else {
				h = 60 * (((r - g) / delta) + 4);
			}
		}
		//
		if (h < 0) {
			h += 360;
		} else if (h > 359) {
			h -= 360;
		}
		//Saturation calculation:
		if (cMax == 0) {
			s = 0;
		} else {
			s = delta / cMax;
		}
		//Value calculation:
		v = cMax;

		return HSV.of(h, s, v);
	}

	/**
	 * Convert the ARGB color to its CIE LAB representative components.
	 */
	@Override
	public LAB toLAB() {
		return toXYZ().toLAB();
	}

	@Override
	public RGB toRGB() {
		return new RGB(color);
	}

	/**
	 * Convert the ARGB color to its CIE XYZ representative components.
	 *
	 * <p>The resulting XYZ representation will use the D65 illuminant and the CIE
	 * 2° Standard Observer (1931).</p>
	 *
	 * <ul>
	 * <li>outXyz[0] is X [0 ...95.047)</li>
	 * <li>outXyz[1] is Y [0...100)</li>
	 * <li>outXyz[2] is Z [0...108.883)</li>
	 * </ul>
	 * The alpha component is ignored
	 */
	@Override
	public XYZ toXYZ() {
		double sr = red() / 255.0;
		sr = sr < 0.04045 ? sr / 12.92 : Math.pow((sr + 0.055) / 1.055, 2.4);
		double sg = green() / 255.0;
		sg = sg < 0.04045 ? sg / 12.92 : Math.pow((sg + 0.055) / 1.055, 2.4);
		double sb = blue() / 255.0;
		sb = sb < 0.04045 ? sb / 12.92 : Math.pow((sb + 0.055) / 1.055, 2.4);

		return XYZ.of(
				100 * (sr * 0.4124 + sg * 0.3576 + sb * 0.1805),
				100 * (sr * 0.2126 + sg * 0.7152 + sb * 0.0722),
				100 * (sr * 0.0193 + sg * 0.1192 + sb * 0.9505)
		);
	}
}
