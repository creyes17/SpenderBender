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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.core.Local;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.security.InvalidKeyException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import reyes.r.christopher.spenderbender.model.ExpenseModel;
import reyes.r.christopher.spenderbender.persistence.LocalDatabaseHandler;

import static org.mockito.Mockito.*;

/**
 * Created by Christopher R Reyes on 8/19/16.
 *
 * Tests the view model for adding transactions
 */

public class TransactionViewModelTest {
    @Test
    public void addExpense() {
        MockDatabaseHandler testDbh = new MockDatabaseHandler();

        TransactionViewModel viewModel = new TransactionViewModel(testDbh);

        ExpenseModel expenseModel1 = new ExpenseModel(
                "Sushi",
                35.92,
                2016,
                Calendar.AUGUST,
                19
        );

        viewModel.addExpense(
                expenseModel1.getName(),
                expenseModel1.getAmount(),
                expenseModel1.getYearIncurred(),
                expenseModel1.getMonthIncurred(),
                expenseModel1.getDayIncurred()
        );

        ExpenseModel savedExpense = testDbh.getLastSavedExpense();

        Assert.assertEquals("Saved the correct expense name", expenseModel1.getName(), savedExpense.getName());
        Assert.assertEquals("Saved the correct expense amount", expenseModel1.getAmount(), savedExpense.getAmount(), 0.001);
        Assert.assertEquals("Saved the correct expense year", expenseModel1.getYearIncurred(), savedExpense.getYearIncurred());
        Assert.assertEquals("Saved the correct expense month", expenseModel1.getMonthIncurred(), savedExpense.getMonthIncurred());
        Assert.assertEquals("Saved the correct expense day", expenseModel1.getDayIncurred(), savedExpense.getDayIncurred());

        ExpenseModel expenseModel2 = new ExpenseModel(
                "Potatos",
                5.67,
                2016,
                Calendar.AUGUST,
                19
        );

        viewModel.addExpense(
                expenseModel2.getName(),
                expenseModel2.getAmount(),
                expenseModel2.getYearIncurred(),
                expenseModel2.getMonthIncurred(),
                expenseModel2.getDayIncurred()
        );

        savedExpense = testDbh.getLastSavedExpense();

        Assert.assertEquals("Saved the correct expense name", expenseModel2.getName(), savedExpense.getName());
        Assert.assertEquals("Saved the correct expense amount", expenseModel2.getAmount(), savedExpense.getAmount(), 0.001);
        Assert.assertEquals("Saved the correct expense year", expenseModel2.getYearIncurred(), savedExpense.getYearIncurred());
        Assert.assertEquals("Saved the correct expense month", expenseModel2.getMonthIncurred(), savedExpense.getMonthIncurred());
        Assert.assertEquals("Saved the correct expense day", expenseModel2.getDayIncurred(), savedExpense.getDayIncurred());
    }

    private class MockDatabaseHandler extends LocalDatabaseHandler {

        private ExpenseModel lastSavedExpense;

        private MockDatabaseHandler() {
            super(null);
        }

        @Override
        public Long saveExpense(ExpenseModel expense) {
            this.lastSavedExpense = expense;

            return Long.MAX_VALUE;
        }

        private ExpenseModel getLastSavedExpense() {
            return lastSavedExpense;
        }
    }
}
