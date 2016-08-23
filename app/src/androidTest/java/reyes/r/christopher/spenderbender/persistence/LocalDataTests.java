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
import android.database.sqlite.SQLiteStatement;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;

import reyes.r.christopher.spenderbender.model.ExpenseModel;

import static org.junit.Assert.*;

/**
 * Created by Christopher R Reyes on 8/13/16.
 *
 * Tests various client-side data functionality.
 */
@RunWith(AndroidJUnit4.class)
public class LocalDataTests {
    private LocalDatabaseHandler databaseHandler;

    private LocalDatabaseHandler createTestDatabase(String prefix) throws Exception {
        Context testContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), prefix);

        File previousTestDatabaseIfExists = testContext.getDatabasePath(LocalDatabaseHandler.DatabaseName);

        // As part of the setup, backup the existing database (if it exists), and then delete it

        Assert.assertTrue("Make sure we're working with a test database", previousTestDatabaseIfExists.getName().matches("^\\s*" + prefix + ".*$"));
        Assert.assertNotEquals("Make sure we haven't accidentally modified our tests to make the real database the test database", LocalDatabaseHandler.DatabaseName.toUpperCase(Locale.getDefault()), previousTestDatabaseIfExists.getName().toUpperCase(Locale.getDefault()));

        if ( previousTestDatabaseIfExists.exists() ) {
            Assert.assertTrue("Delete failed for previous test database", previousTestDatabaseIfExists.delete());
        }

        Assert.assertFalse("Prior database file should not exist before we start testing with it", previousTestDatabaseIfExists.exists());

        return new LocalDatabaseHandler(testContext);
    }

    @Before
    public void setUp() throws Exception {
        String testPrefix = "localDataTest_";

        this.databaseHandler = createTestDatabase(testPrefix);
    }

    @After
    public void tearDown() throws Exception {
        this.databaseHandler.close();
        this.databaseHandler = null;
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("reyes.r.christopher.spenderbender", appContext.getPackageName());
    }

    @Test
    public void databaseCreationTests() throws Exception {

        String testPrefix = "localDataTest_creation_";

        LocalDatabaseHandler testDbh = createTestDatabase(testPrefix);

        SQLiteDatabase testDb = testDbh.getReadableDatabase();

        // TODO: 8/18/16 This is bad form. I wanted to modularize the creation of a test database so I could save code between here and the setUp method, but that means I have to manually re-create the database path name here to run the next test
        // Should we just write that test database creation code twice?
        // The reason this is bad is that it assumes something about how the createTestDatabase method is implemented (namely that it's using this RenamingDelegatingContext)
        Context testContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), testPrefix);

        File previousTestDatabaseIfExists = testContext.getDatabasePath(LocalDatabaseHandler.DatabaseName);

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

    @Test
    public void canSaveExpense() {
        SQLiteDatabase db = this.databaseHandler.getWritableDatabase();

        SQLiteStatement countStar = db.compileStatement("Select count(*) from " + TransactionContract.TableName);

        Assert.assertEquals("Should start with 0 rows", 0, countStar.simpleQueryForLong());

        ExpenseModel expenseModel1 = new ExpenseModel(
                "Korean BBQ",       // name
                21.22,              // amount
                2016,               // year incurred
                Calendar.AUGUST,    // month incurred
                22                  // day incurred
        );

        long expense1Id = this.databaseHandler.saveExpense(expenseModel1, db);

        Assert.assertNotEquals("Can save Expense Model to database", -1, expense1Id);

        Assert.assertEquals("Saving expense creates a new row in the transaction table", 1, countStar.simpleQueryForLong());

        ExpenseModel expenseModel2 = new ExpenseModel(
                "Lumiere Restaurant",   // name
                201.39,                 // amount
                2014,                   // year incurred
                Calendar.FEBRUARY,      // month incurred
                22                      // day incurred
        );

        long expense2Id = this.databaseHandler.saveExpense(expenseModel2, db);

        Assert.assertNotEquals("Can save other expense to database", -1, expense2Id);

        Assert.assertEquals("Saving expense creates another new row in the transaction table", 2, countStar.simpleQueryForLong());

        // Check that rows in the database match the expenses we tried to save

        Cursor expenses = db.query(TransactionContract.TableName, null, null, null, null, null, null);

        Assert.assertTrue("Cursor moves to first", expenses.moveToFirst());

        while ( ! expenses.isAfterLast() ) {
            long primaryKey = expenses.getLong(
                    expenses.getColumnIndexOrThrow(
                            TransactionContract.PrimaryKey.getName()
                    )
            );

            String name = expenses.getString(
                    expenses.getColumnIndexOrThrow(
                            TransactionContract.TransactionName.getName()
                    )
            );

            double amount = expenses.getDouble(
                    expenses.getColumnIndexOrThrow(
                            TransactionContract.Amount.getName()
                    )
            );

            int yearIncurred = expenses.getInt(
                    expenses.getColumnIndexOrThrow(
                            TransactionContract.YearIncurred.getName()
                    )
            );

            int monthIncurred = expenses.getInt(
                    expenses.getColumnIndexOrThrow(
                            TransactionContract.MonthIncurred.getName()
                    )
            );

            int dayIncurred = expenses.getInt(
                    expenses.getColumnIndexOrThrow(
                            TransactionContract.DayIncurred.getName()
                    )
            );

            int yearCreated = expenses.getInt(
                    expenses.getColumnIndexOrThrow(
                            TransactionContract.YearCreated.getName()
                    )
            );

            int monthCreated = expenses.getInt(
                    expenses.getColumnIndexOrThrow(
                            TransactionContract.MonthCreated.getName()
                    )
            );

            int dayCreated = expenses.getInt(
                    expenses.getColumnIndexOrThrow(
                            TransactionContract.DayCreated.getName()
                    )
            );

            Assert.assertTrue("Row ID matches expense 1 or 2", (( primaryKey == expense1Id ) || ( primaryKey == expense2Id )));

            ExpenseModel modelToCompare;

            if(
                    expense1Id == expenses.getLong(
                        expenses.getColumnIndex(
                                TransactionContract.PrimaryKey.getName()
                        )
                    )
            ) {
                modelToCompare = expenseModel1;
            }
            else {
                modelToCompare = expenseModel2;
            }

            Assert.assertEquals("Saved expense name correctly", modelToCompare.getName(), name);
            Assert.assertEquals("Saved expense amount correctly", modelToCompare.getAmount(), amount, 0.001);
            Assert.assertEquals("Saved expense Year Incurred correctly", modelToCompare.getYearIncurred(), yearIncurred);
            Assert.assertEquals("Saved expense Month Incurred correctly", modelToCompare.getMonthIncurred(), monthIncurred);
            Assert.assertEquals("Saved expense Day Incurred correctly", modelToCompare.getDayIncurred(), dayIncurred);
            Assert.assertEquals("Saved expense Year Created correctly", modelToCompare.getYearCreated(), yearCreated);
            Assert.assertEquals("Saved expense Month Created correctly", modelToCompare.getMonthCreated(), monthCreated);
            Assert.assertEquals("Saved expense Day Created correctly", modelToCompare.getDayCreated(), dayCreated);

            expenses.moveToNext();
        }

        countStar.close();
        expenses.close();
        db.close();
    }

}