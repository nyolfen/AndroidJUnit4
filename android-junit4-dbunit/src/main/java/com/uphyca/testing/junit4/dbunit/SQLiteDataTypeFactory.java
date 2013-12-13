package com.uphyca.testing.junit4.dbunit;

import java.util.Arrays;
import java.util.Collection;

import org.dbunit.dataset.datatype.DefaultDataTypeFactory;

public class SQLiteDataTypeFactory extends DefaultDataTypeFactory {
	private static final Collection<String> DATABASE_PRODUCTS = Arrays.asList(new String[] { "SQLite" });

	//for ignore warning
	@Override
	public Collection<String> getValidDbProducts() {
		return DATABASE_PRODUCTS;
	}
}
