package com.whenling.castle.json;

public interface FV {

	interface None {
	}

	interface Simple {
	}

	interface Audit {
	}

	interface Detail extends Simple {
	}

	interface Full extends Detail, Audit {
	}

}
