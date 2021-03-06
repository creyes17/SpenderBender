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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import reyes.r.christopher.spenderbender.model.ExpenseModel;

/**
 * Created by Christopher R Reyes on 8/11/16.
 *
 * Manages the local database(s) on the device
 */

public class LocalDatabaseHandler extends SQLiteOpenHelper {

    static final String DatabaseName = "SpenderBenderSQLiteDB";
    private static final int DatabaseVersion = 1;

    public LocalDatabaseHandler(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStatement =  TransactionContract.schema.toString();

        db.execSQL(createStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database Version 1. Nothing to do on update because there is no prior version yet.
    }

    public Long saveExpense(ExpenseModel expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long result = this.saveExpense(expense, db);
        db.close();
        return result;
    }

    /**
     * Saves an expense to the given database. Modifies expense by setting its Id field!
     * @param expense   The expense to save. Will be modified!
     * @param db        The SQLite database into which to save the expense
     * @return          The new ID of the expense
     */
    public Long saveExpense(ExpenseModel expense, SQLiteDatabase db) {
        if(expense.getId() != ExpenseModel.UNSAVED_EXPENSE) {
            throw new IllegalArgumentException("Updating existed expenses is not yet supported!");
        }

        ContentValues formattedExpense = new ContentValues();

        formattedExpense.put(TransactionContract.TransactionName.getName(), expense.getName());
        formattedExpense.put(TransactionContract.Amount.getName(), expense.getAmount());
        formattedExpense.put(TransactionContract.YearIncurred.getName(), expense.getYearIncurred());
        formattedExpense.put(TransactionContract.MonthIncurred.getName(), expense.getMonthIncurred());
        formattedExpense.put(TransactionContract.DayIncurred.getName(), expense.getDayIncurred());
        formattedExpense.put(TransactionContract.YearCreated.getName(), expense.getYearCreated());
        formattedExpense.put(TransactionContract.MonthCreated.getName(), expense.getMonthCreated());
        formattedExpense.put(TransactionContract.DayCreated.getName(), expense.getDayCreated());

        long newId = db.insert(TransactionContract.TableName, null, formattedExpense);

        expense.setId(newId);

        return newId;
    }

    public List<ExpenseModel> getAllExpenses() {
        List<ExpenseModel> allExpenses = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor expenses = db.query(TransactionContract.TableName, null, null, null, null, null, null);

        expenses.moveToFirst();
        while (! expenses.isAfterLast()) {
            ExpenseModel next = new ExpenseModel(
                    expenses.getString( expenses.getColumnIndexOrThrow(TransactionContract.TransactionName.getName())),
                    expenses.getDouble( expenses.getColumnIndexOrThrow(TransactionContract.Amount.getName())),
                    expenses.getInt( expenses.getColumnIndexOrThrow(TransactionContract.YearIncurred.getName())),
                    expenses.getInt( expenses.getColumnIndexOrThrow(TransactionContract.MonthIncurred.getName())),
                    expenses.getInt( expenses.getColumnIndexOrThrow(TransactionContract.DayIncurred.getName())),
                    expenses.getInt( expenses.getColumnIndexOrThrow(TransactionContract.YearCreated.getName())),
                    expenses.getInt( expenses.getColumnIndexOrThrow(TransactionContract.MonthCreated.getName())),
                    expenses.getInt( expenses.getColumnIndexOrThrow(TransactionContract.DayCreated.getName())),
                    expenses.getLong( expenses.getColumnIndexOrThrow(TransactionContract.PrimaryKey.getName()))
            );
            allExpenses.add(next);
            expenses.moveToNext();
        }

        expenses.close();
        db.close();
        return allExpenses;
    }
}
