/*
 * Project Cyclops
 *
 * Copyright (c) 2021. Elex.
 * All Rights Reserved.
 */

package com.elex_project.cyclops;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.color.ColorSpace;

import static org.junit.jupiter.api.Assertions.*;

class RGBTest {

	@Test @Disabled
	void red() {
		RGB color = RGB.of(255,0,0);

		assertEquals(HSV.of(0,1,1), color.toHSV());
		assertEquals(HSL.of(0,1,0.5f), color.toHSL());
		assertEquals(CMYK.of(0,1,1,0), color.toCMYK());
	}

	@Test @Disabled
	void navy() {
		RGB color = RGB.of(0,0,128);

		assertEquals(HSV.of(240/365f,1,0.5f), color.toHSV());
		assertEquals(HSL.of(240/365f,1,0.25f), color.toHSL());
		assertEquals(CMYK.of(1,1,0,0.498f), color.toCMYK());
	}

	@Test
	void convTest() {
		RGB color = RGB.of(124,38,62);
		System.out.println(color.toCMY());
		assertEquals(color, color.toCMY().toRGB());
		System.out.println(color.toCMYK());
		assertEquals(color, color.toCMYK().toRGB());
		System.out.println(color.toHSL());
		assertEquals(color, color.toHSL().toRGB());
		System.out.println(color.toHSV());
		assertEquals(color, color.toHSV().toRGB());
		System.out.println(color.toHunterLAB());
		assertEquals(color, color.toHunterLAB().toRGB());
		System.out.println(color.toLAB());
		assertEquals(color, color.toLAB().toRGB());
		System.out.println(color.toLCH());
		assertEquals(color, color.toLCH().toRGB());
		System.out.println(color.toLUV());
		assertEquals(color, color.toLUV().toRGB());
		System.out.println(color.toXYZ());
		assertEquals(color, color.toXYZ().toRGB());
		System.out.println(color.toYXY());
		assertEquals(color, color.toYXY().toRGB());

		System.out.println(color.toYXY().toRGB());
	}
}
