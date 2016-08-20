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

package reyes.r.christopher.spenderbender.viewmodel;

import android.databinding.BaseObservable;

import reyes.r.christopher.spenderbender.model.ExpenseModel;
import reyes.r.christopher.spenderbender.persistence.LocalDatabaseHandler;

/**
 * Created by Christopher R Reyes on 8/19/16.
 *
 * ViewModel for manipulating Transactions
 */

public class TransactionViewModel extends BaseObservable {
    private final LocalDatabaseHandler dbh;

    public TransactionViewModel(LocalDatabaseHandler dbh) {
        this.dbh = dbh;
    }

    public void addExpense(String name, double amount, int yearIncurred, int monthIncurred, int dayIncurred) {

        ExpenseModel expense = new ExpenseModel(
                name,
                amount,
                yearIncurred,
                monthIncurred,
                dayIncurred
        );

        dbh.saveExpense(expense);
    }
}
