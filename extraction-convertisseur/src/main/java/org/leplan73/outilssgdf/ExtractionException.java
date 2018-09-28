package org.leplan73.outilssgdf;

public class ExtractionException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ExtractionException(String string) {
		super(string);
	}

	public ExtractionException(Exception e) {
		super(e);
	}

}
