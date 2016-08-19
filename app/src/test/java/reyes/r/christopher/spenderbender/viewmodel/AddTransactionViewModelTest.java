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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.core.Local;

import reyes.r.christopher.spenderbender.model.ExpenseModel;
import reyes.r.christopher.spenderbender.persistence.LocalDatabaseHandler;

import static org.mockito.Mockito.*;

/**
 * Created by Christopher R Reyes on 8/19/16.
 *
 * Tests the view model for adding transactions
 */

public class AddTransactionViewModelTest {
    private LocalDatabaseHandler testDbh;

    @Before
    public void setUp() throws Exception {
        this.testDbh = new MockLocalDatabaseHandler();
    }

    @Test
    public void addExpense() {
        // Get our view model

    }

    private class MockLocalDatabaseHandler extends LocalDatabaseHandler {
        private ExpenseModel lastSavedExpense;
        private long currentId = 0;

        MockLocalDatabaseHandler() {
            super(null);
        }

        @Override
        public Long saveExpense(ExpenseModel expense, SQLiteDatabase db) {
            this.lastSavedExpense = expense;
            currentId++;

            return currentId;
        }
    }
}
