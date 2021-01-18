/*
 * Project Cyclops
 *
 * Copyright (c) 2021. Elex.
 * All Rights Reserved.
 */

package com.elex_project.cyclops;

public interface Color {

	public CMYK toCMYK();

	public HSL toHSL();

	public HSV toHSV();

	public LAB toLAB();

	public RGB toRGB();

	public XYZ toXYZ();

	public CMY toCMY();

	public HunterLAB toHunterLAB();

	public LCH toLCH();

	public LUV toLUV();

	public YXY toYXY();
}
