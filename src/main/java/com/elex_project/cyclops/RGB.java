/*
 * Project Cyclops
 *
 * Copyright (c) 2021. Elex.
 * All Rights Reserved.
 */

package com.elex_project.cyclops;

import lombok.*;

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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RGB implements Color {
	private int color;

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

	@Override
	public String toString() {
		return "RGB(r=" + red() + ", g=" + green() + ", blue=" + blue() + ")";
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
		return toCMY().toCMYK();
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

		float var_R = (red() / 255f);                     //RGB from 0 to 255
		float var_G = (green() / 255f);
		float var_B = (blue() / 255f);

		float var_Min = min(var_R, var_G, var_B);    //Min. value of RGB
		float var_Max = max(var_R, var_G, var_B);    //Max. value of RGB
		float del_Max = var_Max - var_Min;            //Delta RGB value

		float L = (var_Max + var_Min) / 2f;

		float H = 0, S = 0;
		if (del_Max == 0) {                    //This is a gray, no chroma...
			H = 0;                                //HSL results from 0 to 1
			S = 0;
		} else {                                   //Chromatic data...
			if (L < 0.5) {
				S = del_Max / (var_Max + var_Min);
			} else {
				S = del_Max / (2 - var_Max - var_Min);
			}

			float del_R = (((var_Max - var_R) / 6f) + (del_Max / 2f)) / del_Max;
			float del_G = (((var_Max - var_G) / 6f) + (del_Max / 2f)) / del_Max;
			float del_B = (((var_Max - var_B) / 6f) + (del_Max / 2f)) / del_Max;

			if (var_R == var_Max) {
				H = del_B - del_G;
			} else if (var_G == var_Max) {
				H = (1 / 3f) + del_R - del_B;
			} else if (var_B == var_Max) {
				H = (2 / 3f) + del_G - del_R;
			}

			if (H < 0) H += 1;
			if (H > 1) H -= 1;
		}
		return HSL.of(H, S, L);
	}

	@Override
	public HSV toHSV() {

		float var_R = (red() / 255f);                 //RGB from 0 to 255
		float var_G = (green() / 255f);
		float var_B = (blue() / 255f);

		float var_Min = min(var_R, var_G, var_B);   //Min. value of RGB
		float var_Max = max(var_R, var_G, var_B);    //Max. value of RGB
		float del_Max = var_Max - var_Min;          //Delta RGB value

		float V = var_Max;
		float H = 0, S = 0;
		if (del_Max == 0) {                     //This is a gray, no chroma...
			H = 0;                                //HSV results from 0 to 1
			S = 0;
		} else {                                  //Chromatic data...
			S = del_Max / var_Max;

			float del_R = (((var_Max - var_R) / 6) + (del_Max / 2)) / del_Max;
			float del_G = (((var_Max - var_G) / 6) + (del_Max / 2)) / del_Max;
			float del_B = (((var_Max - var_B) / 6) + (del_Max / 2)) / del_Max;

			if (var_R == var_Max) {
				H = del_B - del_G;
			} else if (var_G == var_Max) {
				H = (1 / 3f) + del_R - del_B;
			} else if (var_B == var_Max) {
				H = (2 / 3f) + del_G - del_R;
			}

			if (H < 0) H += 1;
			if (H > 1) H -= 1;
		}
		// H를 360으로	// 0~360, 0~1, 0~1
		//H *=360;
		return HSV.of(H, S, V);
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
		float var_R = (red() / 255f);        //R from 0 to 255
		float var_G = (green() / 255f);        //G from 0 to 255
		float var_B = (blue() / 255f);        //B from 0 to 255

		if (var_R > 0.04045)
			var_R = (float) Math.pow((var_R + 0.055) / 1.055, 2.4);
		else
			var_R = var_R / 12.92f;
		if (var_G > 0.04045)
			var_G = (float) Math.pow((var_G + 0.055) / 1.055, 2.4);
		else
			var_G = var_G / 12.92f;
		if (var_B > 0.04045)
			var_B = (float) Math.pow((var_B + 0.055) / 1.055, 2.4);
		else
			var_B = var_B / 12.92f;

		var_R = var_R * 100;
		var_G = var_G * 100;
		var_B = var_B * 100;

		//Observer. = 2°, Illuminant = D65
		float X = (float) (var_R * 0.4124 + var_G * 0.3576 + var_B * 0.1805);
		float Y = (float) (var_R * 0.2126 + var_G * 0.7152 + var_B * 0.0722);
		float Z = (float) (var_R * 0.0193 + var_G * 0.1192 + var_B * 0.9505);

		return XYZ.of(X, Y, Z);
	}

	@Override
	public CMY toCMY() {
		float R = red();
		float G = green();
		float B = blue();

		float C = 1 - (R / 255);
		float M = 1 - (G / 255);
		float Y = 1 - (B / 255);
		return CMY.of(C, M, Y);
	}

	@Override
	public HunterLAB toHunterLAB() {
		return toXYZ().toHunterLAB();
	}

	@Override
	public LCH toLCH() {
		return toLAB().toLCH();
	}

	@Override
	public LUV toLUV() {
		return toXYZ().toLUV();
	}

	@Override
	public YXY toYXY() {
		return toXYZ().toYXY();
	}
}
