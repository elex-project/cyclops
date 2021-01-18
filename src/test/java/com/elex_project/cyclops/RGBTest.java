/*
 * Project Cyclops
 *
 * Copyright (c) 2021. Elex.
 * All Rights Reserved.
 */

package com.elex_project.cyclops;

import org.junit.jupiter.api.Test;

import java.awt.color.ColorSpace;

import static org.junit.jupiter.api.Assertions.*;

class RGBTest {

	@Test
	void red() {
		RGB color = RGB.of(255,0,0);

		assertEquals(HSV.of(0,1,1), color.toHSV());
		assertEquals(HSL.of(0,1,0.5f), color.toHSL());
		assertEquals(CMYK.of(0,1,1,0), color.toCMYK());
	}

	@Test
	void color1() {
		RGB color = RGB.of(124,38,62);

		assertEquals(HSV.of(343,0.694f,0.486f), color.toHSV());
		assertSame(HSL.of(343,0.531f,0.318f), color.toHSL());
		assertEquals(CMYK.of(0,0.6935f,0.5f,0.5137f), color.toCMYK());
	}
}
