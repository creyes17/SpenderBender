/*
 * SpenderBender: A free offline budget tracking tool.
 * Copyright (c) 2016 Christopher R Reyes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package reyes.r.christopher.spenderbender.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Created by Christopher R Reyes on 8/13/16.
 *
 * Tests various client-side data functionality.
 */
@RunWith(AndroidJUnit4.class)
public class LocalDataTests {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("reyes.r.christopher.spenderbender", appContext.getPackageName());
    }

    @Test
    public void databaseCreationTests() throws Exception {

        String testPrefix = "localDataTest_";
        Context testContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), testPrefix);

        // XXX Placeholder
        Assert.assertTrue(Boolean.TRUE);

        File previousTestDatabaseIfExists = testContext.getDatabasePath(LocalDatabaseHandler.DatabaseName);

        // As part of the setup, backup the existing database (if it exists), and then delete it

        Assert.assertTrue("Make sure we're working with a test database", previousTestDatabaseIfExists.getName().matches("^\\s*" + testPrefix + ".*$"));

        if ( previousTestDatabaseIfExists.exists() ) {
            Assert.assertTrue("Delete failed for previous test database", previousTestDatabaseIfExists.delete());
        }

        Assert.assertFalse("Prior database file should not exist before we start testing onCreate", previousTestDatabaseIfExists.exists());

        LocalDatabaseHandler testDbh = new LocalDatabaseHandler(testContext);

        SQLiteDatabase testDb = testDbh.getReadableDatabase();

        // After creating the database, a new database file should exist with the right file name

        Assert.assertTrue("Database is created at the expected file", previousTestDatabaseIfExists.exists());

        // The created database should have a transaction table

        Cursor results =  testDb.rawQuery("PRAGMA table_info('" + TransactionContract.TableName + "');", null);

        Assert.assertTrue( "Transaction table should exist", results.moveToFirst() );

        Assert.assertEquals( "Transaction table should have the same number of columns as the schema definition", TransactionContract.AllColumns.size(), results.getCount() );

        // The table has the same number of columns as the schema, and in SQLite, each column must have a unique name
        // Therefore, as long as each column name matches a column name from the schema, all columns have been created successfully

        HashSet<String> validColumnNames = new HashSet<>();

        for (SQLiteColumnDefinition column :
                TransactionContract.AllColumns) {
            validColumnNames.add(column.getName());
        }

        int name = results.getColumnIndexOrThrow("name");

        while ( ! results.isAfterLast() ) {
            String columnName = results.getString(name);

            Assert.assertTrue("Column [" + columnName + "] is a valid column", validColumnNames.contains(columnName));

            results.moveToNext();
        }

        results.close();

        // Test idempotency of onCreate()

        testDbh.onCreate(testDb);

        results =  testDb.rawQuery("PRAGMA table_info('" + TransactionContract.TableName + "');", null);

        Assert.assertTrue( "Transaction table should still exist", results.moveToFirst() );

        Assert.assertEquals( "Transaction table should still have the same number of columns as the schema definition", TransactionContract.AllColumns.size(), results.getCount() );

        // The table has the same number of columns as the schema, and in SQLite, each column must have a unique name
        // Therefore, as long as each column name matches a column name from the schema, all columns have been created successfully

        name = results.getColumnIndexOrThrow("name");

        while ( ! results.isAfterLast() ) {
            String columnName = results.getString(name);

            Assert.assertTrue("Column [" + columnName + "] is a valid column", validColumnNames.contains(columnName));

            results.moveToNext();
        }

        results.close();

        testDb.close();
        testDbh.close();
    }

}