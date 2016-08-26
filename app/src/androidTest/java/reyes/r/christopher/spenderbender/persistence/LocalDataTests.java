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
import java.util.List;
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

        // As part of the setup, delete the existing test database if it exists

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

        Assert.assertEquals("Before saving, expense has UNSAVED id", ExpenseModel.UNSAVED_EXPENSE, expenseModel1.getId());

        long expense1Id = this.databaseHandler.saveExpense(expenseModel1, db);

        Assert.assertNotEquals("Can save Expense Model to database", -1, expense1Id);

        Assert.assertEquals("Saving expense creates a new row in the transaction table", 1, countStar.simpleQueryForLong());

        Assert.assertEquals("Saving expense sets it's Id field", expense1Id, expenseModel1.getId());

        ExpenseModel expenseModel2 = new ExpenseModel(
                "Lumiere Restaurant",   // name
                201.39,                 // amount
                2014,                   // year incurred
                Calendar.FEBRUARY,      // month incurred
                22                      // day incurred
        );

        Assert.assertEquals("Before saving, expense has UNSAVED id", ExpenseModel.UNSAVED_EXPENSE, expenseModel2.getId());

        long expense2Id = this.databaseHandler.saveExpense(expenseModel2, db);

        Assert.assertNotEquals("Can save other expense to database", -1, expense2Id);

        Assert.assertEquals("Saving expense creates another new row in the transaction table", 2, countStar.simpleQueryForLong());

        Assert.assertEquals("Saving expense sets it's Id field", expense2Id, expenseModel2.getId());

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

            Assert.assertTrue("Row ID matches expense 1 or 2", (( primaryKey == expenseModel1.getId() ) || ( primaryKey == expenseModel2.getId() )));

            ExpenseModel modelToCompare;

            if( primaryKey == expenseModel1.getId() ) {
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

        ExpenseModel expenseModel3 = new ExpenseModel(
                "Lumiere Restaurant",   // name
                201.39,                 // amount
                2014,                   // year incurred
                Calendar.FEBRUARY,      // month incurred
                22,                     // day incurred
                2015,                   // year created
                Calendar.OCTOBER,       // month created
                31,                     // day created
                55                      // Id
        );

        boolean caughtCorrectException = Boolean.FALSE;

        try {
            databaseHandler.saveExpense(expenseModel3);
        } catch ( IllegalArgumentException e ) {
            caughtCorrectException = Boolean.TRUE;
        }

        Assert.assertTrue("Cannot update an expense with an existing Id", caughtCorrectException);

        ExpenseModel expenseModel4 = new ExpenseModel(
                "Lumiere Restaurant",           // name
                201.39,                         // amount
                2014,                           // year incurred
                Calendar.FEBRUARY,              // month incurred
                22,                             // day incurred
                2015,                           // year created
                Calendar.OCTOBER,               // month created
                31,                             // day created
                ExpenseModel.UNSAVED_EXPENSE    // Id
        );

        caughtCorrectException = Boolean.FALSE;

        long id4 = ExpenseModel.UNSAVED_EXPENSE;

        try {
            id4 = databaseHandler.saveExpense(expenseModel4);
        } catch ( IllegalArgumentException e ) {
            caughtCorrectException = Boolean.TRUE;
        }

        Assert.assertFalse("Can update an expense with an 'UNSAVED' Id", caughtCorrectException);

        Assert.assertNotEquals("Doesn't save UNSAVED Id to database", ExpenseModel.UNSAVED_EXPENSE, id4);
        Assert.assertEquals("Id becomes set when saving expense", id4, expenseModel4.getId());

        countStar.close();
        expenses.close();
        db.close();
    }

    @Test
    public void canGetAllExpenses() {
        // Note: We assume that the saveExpense method has already been tested and works
        Assert.assertEquals("When there are no expenses, getAllExpenses return an empty list", 0, this.databaseHandler.getAllExpenses().size());

        ExpenseModel expenseModel1 = new ExpenseModel(
                "thing1",
                1.23,
                2015,
                Calendar.DECEMBER,
                22
        );

        // Again, we assume that the saveExpense method has already been tested, and that this will add a row to the database
        long expenseModel1Id = this.databaseHandler.saveExpense(expenseModel1);

        List<ExpenseModel> allExpenses = this.databaseHandler.getAllExpenses();
        Assert.assertEquals("With one expense saved, getAllExpenses returns that one expense", 1, allExpenses.size());

        ExpenseModel modelToCompare = allExpenses.get(0);

        Assert.assertEquals("Able to retrieve single expense name correctly", expenseModel1.getName(), modelToCompare.getName());
        Assert.assertEquals("Able to retrieve single expense amount correctly", expenseModel1.getAmount(), modelToCompare.getAmount(), 0.001);
        Assert.assertEquals("Able to retrieve single expense yearIncurred correctly", expenseModel1.getYearIncurred(), modelToCompare.getYearIncurred());
        Assert.assertEquals("Able to retrieve single expense monthIncurred correctly", expenseModel1.getMonthIncurred(), modelToCompare.getMonthIncurred());
        Assert.assertEquals("Able to retrieve single expense dayIncurred correctly", expenseModel1.getDayIncurred(), modelToCompare.getDayIncurred());
        Assert.assertEquals("Able to retrieve single expense yearCreated correctly", expenseModel1.getYearCreated(), modelToCompare.getYearCreated());
        Assert.assertEquals("Able to retrieve single expense monthCreated correctly", expenseModel1.getMonthCreated(), modelToCompare.getMonthCreated());
        Assert.assertEquals("Able to retrieve single expense dayCreated correctly", expenseModel1.getDayCreated(), modelToCompare.getDayCreated());
        Assert.assertEquals("Able to retrieve single expense id correctly", expenseModel1Id, modelToCompare.getId());

        allExpenses = this.databaseHandler.getAllExpenses();
        Assert.assertEquals("getAllExpenses always returns the correct number of results", 1, allExpenses.size());

        ExpenseModel expenseModel2 = new ExpenseModel(
                "thing2",
                4.56,
                2016,
                Calendar.JANUARY,
                3
        );

        long expenseModel2Id = this.databaseHandler.saveExpense(expenseModel2);

        allExpenses = this.databaseHandler.getAllExpenses();
        Assert.assertEquals("getAllExpenses always returns the correct number of results", 2, allExpenses.size());

        for ( ExpenseModel expense: allExpenses) {

            ExpenseModel matchingModel;

            Assert.assertTrue("Each expense matches expense ID that we saved", (
                    expense.getId() == expenseModel1Id ||
                            expense.getId() == expenseModel2Id
            ));

            if (expense.getId() == expenseModel1Id) {
                matchingModel = expenseModel1;
            }
            else {
                matchingModel = expenseModel2;
            }

            Assert.assertEquals("Able to retrieve expense name correctly", matchingModel.getName(), expense.getName());
            Assert.assertEquals("Able to retrieve expense amount correctly", matchingModel.getAmount(), expense.getAmount(), 0.001);
            Assert.assertEquals("Able to retrieve expense yearIncurred correctly", matchingModel.getYearIncurred(), expense.getYearIncurred());
            Assert.assertEquals("Able to retrieve expense monthIncurred correctly", matchingModel.getMonthIncurred(), expense.getMonthIncurred());
            Assert.assertEquals("Able to retrieve expense dayIncurred correctly", matchingModel.getDayIncurred(), expense.getDayIncurred());
            Assert.assertEquals("Able to retrieve expense yearCreated correctly", matchingModel.getYearCreated(), expense.getYearCreated());
            Assert.assertEquals("Able to retrieve expense monthCreated correctly", matchingModel.getMonthCreated(), expense.getMonthCreated());
            Assert.assertEquals("Able to retrieve expense dayCreated correctly", matchingModel.getDayCreated(), expense.getDayCreated());
        }
    }
}