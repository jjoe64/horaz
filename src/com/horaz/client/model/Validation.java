package com.horaz.client.model;

/**
 * predefined validation types
 */
public enum Validation {
	/**
	 * the field must not be empty
	 */
	IS_NOT_EMPTY(".+");

	final private String regex;
	private Validation(String regex) {
		this.regex = regex;
	}

	/**
	 * runs the validation
	 * @param value
	 * @return false if failed
	 */
	public boolean validate(Object value) {
		String v = value==null?"":value.toString();
		return v.matches(regex);
	}
}
