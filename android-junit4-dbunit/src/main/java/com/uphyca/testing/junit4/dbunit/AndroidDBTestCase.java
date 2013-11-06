/*
 * Copyright (C) 2012 uPhyca Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uphyca.testing.junit4.dbunit;

import java.io.File;
import java.io.InputStream;

import org.dbunit.DBTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.IOperationListener;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.xml.sax.InputSource;

import android.content.Context;
import android.content.res.Resources;
import android.test.IsolatedContext;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;

import com.uphyca.testing.AndroidTestCase;
import com.uphyca.testing.junit4.dbunit.AndroidSQLiteDatabaseTester;

public abstract class AndroidDBTestCase extends AndroidTestCase {
	private static final class DBTestCaseDelegate extends DBTestCase {
		private final AndroidDBTestCase owner;

		public DBTestCaseDelegate(final AndroidDBTestCase owner) {
			this.owner = owner;
		}

		@Override
		protected IDatabaseTester newDatabaseTester() throws Exception {
			return owner.newDatabaseTester();
		}

		@Override
		protected IDataSet getDataSet() throws Exception {
			return owner.getDataSet();
		}

		@Override
		public void setUpDatabaseConfig(final DatabaseConfig config) {
			super.setUpDatabaseConfig(config);
		}

		@Override
		public IDatabaseTester getDatabaseTester() throws Exception {
			return super.getDatabaseTester();
		}

		@Override
		public DatabaseOperation getSetUpOperation() throws Exception {
			return super.getSetUpOperation();
		}

		@Override
		public DatabaseOperation getTearDownOperation() throws Exception {
			return super.getTearDownOperation();
		}

		@Override
		public void setUp() throws Exception {
			super.setUp();
		}

		@Override
		public void tearDown() throws Exception {
			super.tearDown();
		}

		@Override
		public IOperationListener getOperationListener() {
			return super.getOperationListener();
		}

		public IDatabaseConnection callGetConnection() throws Exception {
			return getConnection();
		}
	}

	private class MockContext2 extends MockContext {
		@Override
		public Resources getResources() {
			return getContext().getResources();
		}

		@Override
		public File getDir(final String name, final int mode) {
			// name the directory so the directory will be separated from
			// one created through the regular Context
			return getContext().getDir("mockcontext2_" + name, mode);
		}

		@Override
		public Context getApplicationContext() {
			return this;
		}
	}

	private Context mDatabaseContext;
	private final DBTestCaseDelegate mDBTestCase;

	public AndroidDBTestCase() {
		mDBTestCase = new DBTestCaseDelegate(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dbunit.DatabaseTestCase#newDatabaseTester()
	 */
	protected IDatabaseTester newDatabaseTester() throws Exception {
		return new AndroidSQLiteDatabaseTester(getMockContext(), getDatabaseName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dbunit.DatabaseTestCase#getDataSet()
	 */
	protected abstract IDataSet getDataSet() throws Exception;

	/**
	 * Returns the test database name.
	 */
	protected abstract String getDatabaseName();

	/**
	 * Implements the code for create database.
	 * 
	 * @param context
	 */
	protected abstract void onCreateDatabase(Context context);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dbunit.DatabaseTestCase#setUpDatabaseConfig(org.dbunit.database. DatabaseConfig)
	 */
	protected void setUpDatabaseConfig(final DatabaseConfig config) {
		mDBTestCase.setUpDatabaseConfig(config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dbunit.DatabaseTestCase#getDatabaseTester()
	 */
	protected IDatabaseTester getDatabaseTester() throws Exception {
		return mDBTestCase.getDatabaseTester();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dbunit.DatabaseTestCase#getSetUpOperation()
	 */
	protected DatabaseOperation getSetUpOperation() throws Exception {
		return mDBTestCase.getSetUpOperation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dbunit.DatabaseTestCase#getTearDownOperation()
	 */
	protected DatabaseOperation getTearDownOperation() throws Exception {
		return mDBTestCase.getTearDownOperation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dbunit.DatabaseTestCase#setUp()
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		mDatabaseContext = getDatabaseContext();
		onCreateDatabase(mDatabaseContext);
		mDBTestCase.setUp();
	}

	protected Context getDatabaseContext() {
		return new IsolatedContext(new MockContentResolver(), new RenamingDelegatingContext(new MockContext2(), // The context that most methods are
				// delegated to
				getContext(), // The context that file methods are delegated to
				"test."));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dbunit.DatabaseTestCase#tearDown()
	 */
	@Override
	@After
	public void tearDown() throws Exception {
		mDBTestCase.tearDown();
		super.tearDown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dbunit.DatabaseTestCase#getOperationListener()
	 */
	protected IOperationListener getOperationListener() {
		return mDBTestCase.getOperationListener();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dbunit.DatabaseTestCase#getConnection()
	 */
	public IDatabaseConnection getConnection() throws Exception {
		final IDatabaseConnection conn = mDBTestCase.callGetConnection();
		// SQLite.JDBC only accepts fetch size 1.
		// @see http://www.dbunit.org/properties.html
		conn.getConfig().setProperty("http://www.dbunit.org/properties/fetchSize", 1);
		return conn;
	}

	protected IDataSet getFlatXmlDataSetFromRawResrouce(final int id) throws DataSetException {
		final InputStream in = getContext().getResources().openRawResource(id);
		final FlatXmlProducer producer = new FlatXmlProducer(new InputSource(in), false);
		return new FlatXmlDataSet(producer);
	}

	protected IDataSet getFlatXmlDataSetFromClasspathResrouce(final String file) throws DataSetException {
		final InputStream in = getContext().getClassLoader().getResourceAsStream(file);
		final FlatXmlProducer producer = new FlatXmlProducer(new InputSource(in), false);
		return new FlatXmlDataSet(producer);
	}

	/**
	 * Gets the {@link Context} created by {@link AndroidDBTestCase#getDatabaseContext()}.
	 * 
	 * @return The {@link Context} instance
	 */
	public Context getMockContext() {
		return mDatabaseContext;
	}
}
