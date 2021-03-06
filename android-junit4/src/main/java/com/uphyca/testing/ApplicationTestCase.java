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
package com.uphyca.testing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

/**
 * @see android.test.ApplicationTestCase
 */
public abstract class ApplicationTestCase<T extends Application> {

    private final ApplicationTester<T> _tester;

    /**
     * @param applicationClass
     * @see android.test.ApplicationTestCase#ApplicationTestCase(Class)
     */
    public ApplicationTestCase(Class<T> applicationClass) {
        _tester = new ApplicationTester<T>(this, applicationClass);
    }

    /**
     * @throws Exception
     * @see android.test.ApplicationTestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        _tester.setUp();
    }

    /**
     * @throws Exception
     * @see android.test.ApplicationTestCase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        _tester.tearDown();
    }

    @Test
    public void testAndroidTestCaseSetupProperly() {
        _tester.testAndroidTestCaseSetupProperly();
    }

    @Test
    public final void testApplicationTestCaseSetUpProperly() throws Exception {
        _tester.testApplicationTestCaseSetUpProperly();
    }

    /**
     * @param context
     * @see android.test.AndroidTestCase#setContext(android.content.Context)
     */
    public void setContext(Context context) {
        _tester.setContext(context);
    }

    /**
     * @return
     * @see android.test.AndroidTestCase#getContext()
     */
    public Context getContext() {
        return _tester.getContext();
    }

    /**
     * @param packageName
     * @param className
     * @param permission
     * @see android.test.AndroidTestCase#assertActivityRequiresPermission(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void assertActivityRequiresPermission(String packageName,
                                                 String className,
                                                 String permission) {
        AndroidTestCaseMethodInvoker invoker = AndroidTestCaseMethodInvoker.getInstance();
        invoker.assertActivityRequiresPermission(_tester,
                                                 packageName,
                                                 className,
                                                 permission);
    }

    /**
     * @param uri
     * @param permission
     * @see android.test.AndroidTestCase#assertReadingContentUriRequiresPermission(android.net.Uri,
     *      java.lang.String)
     */
    public void assertReadingContentUriRequiresPermission(Uri uri,
                                                          String permission) {
        AndroidTestCaseMethodInvoker invoker = AndroidTestCaseMethodInvoker.getInstance();
        invoker.assertReadingContentUriRequiresPermission(_tester,
                                                          uri,
                                                          permission);
    }

    /**
     * @param uri
     * @param permission
     * @see android.test.AndroidTestCase#assertWritingContentUriRequiresPermission(android.net.Uri,
     *      java.lang.String)
     */
    public void assertWritingContentUriRequiresPermission(Uri uri,
                                                          String permission) {
        AndroidTestCaseMethodInvoker invoker = AndroidTestCaseMethodInvoker.getInstance();
        invoker.assertWritingContentUriRequiresPermission(_tester,
                                                          uri,
                                                          permission);
    }

    /**
     * @return
     * @see android.test.ApplicationTestCase#getApplication()
     */
    public T getApplication() {
        return _tester.getApplication();
    }

    /**
     * @return
     * @see android.test.ApplicationTestCase#getSystemContext()
     */
    public Context getSystemContext() {
        return _tester.getSystemContext();
    }

    /**
     * @param testCaseClass
     * @throws IllegalAccessException
     * @see com.uphyca.testing.ApplicationTester#scrubClass(java.lang.Class)
     */
    protected void scrubClass(Class<?> testCaseClass) throws IllegalAccessException {
        _tester.scrubClass(testCaseClass);
    }
}
