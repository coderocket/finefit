package edu.mit.csail.sdg.alloy4compiler.translator;

import kodkod.instance.Bounds;

public class BoundsExtractor {
	Bounds bounds;

	public BoundsExtractor(A4Solution s) {
		bounds = s.getBounds();
	}

	public Bounds getBounds() {
		return bounds;
	}
}
