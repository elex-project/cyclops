/*
 * Project Cyclops
 *
 * Copyright (c) 2021. Elex.
 * All Rights Reserved.
 */

package com.elex_project.cyclops;

import org.jetbrains.annotations.NotNull;

public class Utils {
	static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
	static final int MIN_ALPHA_SEARCH_PRECISION = 1;

	private Utils() {
	}

	/**
	 * Returns the euclidean distance between two LAB colors.
	 */
	public static double distanceEuclidean(@NotNull double[] labX, @NotNull double[] labY) {
		return Math.sqrt(Math.pow(labX[0] - labY[0], 2)
				+ Math.pow(labX[1] - labY[1], 2)
				+ Math.pow(labX[2] - labY[2], 2));
	}

	static float constrain(float amount, float low, float high) {
		return amount < low ? low : (amount > high ? high : amount);
	}

	static int constrain(int amount, int low, int high) {
		return amount < low ? low : (amount > high ? high : amount);
	}

	static float circularInterpolate(float a, float b, float f) {
		if (Math.abs(b - a) > 180) {
			if (b > a) {
				a += 360;
			} else {
				b += 360;
			}
		}
		return (a + ((b - a) * f)) % 360;
	}

	static float min(float... args) {
		float min = args[0];
		for (float val : args) {
			if (val < min) min = val;
		}
		return min;
	}

	static float max(float... args) {
		float max = args[0];
		for (float val : args) {
			if (val > max) max = val;
		}
		return max;
	}

	public static int alpha(int color) {
		return color >> 24 & 0xFF;
	}

	public static int red(int color) {
		return color >> 16 & 0xFF;
	}

	public static int green(int color) {
		return color >> 8 & 0xFF;
	}

	public static int blue(int color) {
		return color & 0xFF;
	}

	public static int rgb(int r, int g, int b) {
		int c = 0xff000000;
		c |= r << 16;
		c |= g << 8;
		c |= b;
		return c;
	}

	public static int argb(int a, int r, int g, int b) {
		int c = 0x00000000;
		c |= a << 24;
		c |= r << 16;
		c |= g << 8;
		c |= b;
		return c;
	}

	/**
	 * Set the alpha component of {@code color} to be {@code alpha}.
	 *
	 * @param alpha [0..255]
	 */
	protected static int setAlphaComponent(int color, int alpha) {
		if (alpha < 0 || alpha > 255) {
			throw new IllegalArgumentException("alpha must be between 0 and 255.");
		}
		return (color & 0x00ffffff) | (alpha << 24);
	}

	/**
	 * Composite two potentially translucent colors over each other and returns the result.
	 */
	public static int compositeColors(int foreground, int background) {
		int bgAlpha = alpha(background);
		int fgAlpha = alpha(foreground);
		int a = compositeAlpha(fgAlpha, bgAlpha);

		int r = compositeComponent(red(foreground), fgAlpha,
				red(background), bgAlpha, a);
		int g = compositeComponent(green(foreground), fgAlpha,
				green(background), bgAlpha, a);
		int b = compositeComponent(blue(foreground), fgAlpha,
				blue(background), bgAlpha, a);

		return argb(a, r, g, b);
	}

	private static int compositeAlpha(int foregroundAlpha, int backgroundAlpha) {
		return 0xFF - (((0xFF - backgroundAlpha) * (0xFF - foregroundAlpha)) / 0xFF);
	}

	private static int compositeComponent(int fgC, int fgA, int bgC, int bgA, int a) {
		if (a == 0) return 0;
		return ((0xFF * fgC * fgA) + (bgC * bgA * (0xFF - fgA))) / (a * 0xFF);
	}

	private static double calculateLuminance(int color) {
		//return colorToXYZ(color).y() / 100;
		return RGB.of(color).luminance();
	}

	/**
	 * Returns the contrast ratio between {@code foreground} and {@code background}.
	 * {@code background} must be opaque.
	 * <p>
	 * Formula defined
	 * <a href="http://www.w3.org/TR/2008/REC-WCAG20-20081211/#contrast-ratiodef">here</a>.
	 */
	public static double calculateContrast(int foreground, int background) {
		if (alpha(background) != 255) {
			throw new IllegalArgumentException("background can not be translucent: #"
					+ Integer.toHexString(background));
		}
		if (alpha(foreground) < 255) {
			// If the foreground is translucent, composite the foreground over the background
			foreground = compositeColors(foreground, background);
		}

		final double luminance1 = calculateLuminance(foreground) + 0.05;
		final double luminance2 = calculateLuminance(background) + 0.05;

		// Now return the lighter luminance divided by the darker luminance
		return Math.max(luminance1, luminance2) / Math.min(luminance1, luminance2);
	}

	/**
	 * Calculates the minimum alpha value which can be applied to {@code background} so that would
	 * have a contrast value of at least {@code minContrastRatio} when alpha blended to
	 * {@code foreground}.
	 *
	 * @param foreground       the foreground color
	 * @param background       the background color, opacity will be ignored
	 * @param minContrastRatio the minimum contrast ratio
	 * @return the alpha value in the range 0-255, or -1 if no value could be calculated
	 */
	public static int calculateMinimumBackgroundAlpha(int foreground, int background, float minContrastRatio) {
		// Ignore initial alpha that the background might have since this is
		// what we're trying to calculate.
		background = setAlphaComponent(background, 255);
		final int leastContrastyColor = setAlphaComponent(foreground, 255);
		return binaryAlphaSearch(foreground, background, minContrastRatio, (fg, bg, alpha) -> {
			int testBackground = blendARGB(leastContrastyColor, bg, alpha / 255f);
			// Float rounding might set this alpha to something other that 255,
			// raising an exception in calculateContrast.
			testBackground = setAlphaComponent(testBackground, 255);
			return calculateContrast(fg, testBackground);
		});
	}

	/**
	 * Calculates the minimum alpha value which can be applied to {@code foreground} so that would
	 * have a contrast value of at least {@code minContrastRatio} when compared to
	 * {@code background}.
	 *
	 * @param foreground       the foreground color
	 * @param background       the opaque background color
	 * @param minContrastRatio the minimum contrast ratio
	 * @return the alpha value in the range 0-255, or -1 if no value could be calculated
	 */
	public static int calculateMinimumAlpha(int foreground, int background,
	                                        float minContrastRatio) {
		if (alpha(background) != 255) {
			throw new IllegalArgumentException("background can not be translucent: #"
					+ Integer.toHexString(background));
		}

		Utils.ContrastCalculator contrastCalculator = (fg, bg, alpha) -> {
			int testForeground = setAlphaComponent(fg, alpha);
			return calculateContrast(testForeground, bg);
		};

		// First lets check that a fully opaque foreground has sufficient contrast
		double testRatio = contrastCalculator.calculateContrast(foreground, background, 255);
		if (testRatio < minContrastRatio) {
			// Fully opaque foreground does not have sufficient contrast, return error
			return -1;
		}
		foreground = setAlphaComponent(foreground, 255);
		return binaryAlphaSearch(foreground, background, minContrastRatio, contrastCalculator);
	}

	/**
	 * Calculates the alpha value using binary search based on a given contrast evaluation function
	 * and target contrast that needs to be satisfied.
	 *
	 * @param foreground       the foreground color
	 * @param background       the opaque background color
	 * @param minContrastRatio the minimum contrast ratio
	 * @param calculator       function that calculates contrast
	 * @return the alpha value in the range 0-255, or -1 if no value could be calculated
	 */
	private static int binaryAlphaSearch(int foreground, int background,
	                                     float minContrastRatio, Utils.ContrastCalculator calculator) {
		// Binary search to find a value with the minimum value which provides sufficient contrast
		int numIterations = 0;
		int minAlpha = 0;
		int maxAlpha = 255;

		while (numIterations <= Utils.MIN_ALPHA_SEARCH_MAX_ITERATIONS &&
				(maxAlpha - minAlpha) > Utils.MIN_ALPHA_SEARCH_PRECISION) {
			final int testAlpha = (minAlpha + maxAlpha) / 2;

			final double testRatio = calculator.calculateContrast(foreground, background,
					testAlpha);
			if (testRatio < minContrastRatio) {
				minAlpha = testAlpha;
			} else {
				maxAlpha = testAlpha;
			}

			numIterations++;
		}

		// Conservatively return the max of the range of possible alphas, which is known to pass.
		return maxAlpha;
	}

	/**
	 * @param color1
	 * @param color2
	 * @param ratio  0.0 ~1.0
	 * @return color
	 */
	protected static int blendARGB(int color1, int color2, float ratio) {
		final float inverseRatio = 1 - ratio;
		float a = alpha(color1) * inverseRatio + alpha(color2) * ratio;
		float r = red(color1) * inverseRatio + red(color2) * ratio;
		float g = green(color1) * inverseRatio + green(color2) * ratio;
		float b = blue(color1) * inverseRatio + blue(color2) * ratio;
		return argb((int) a, (int) r, (int) g, (int) b);
	}

	interface ContrastCalculator {
		double calculateContrast(int foreground, int background, int alpha);
	}
}
