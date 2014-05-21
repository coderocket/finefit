package com.finefit.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CsvFile {

	/**
	 * Csv file name
	 */
	String value();

}
