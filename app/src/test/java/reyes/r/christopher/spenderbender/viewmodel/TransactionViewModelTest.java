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

import android.view.View;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Calendar;

import reyes.r.christopher.spenderbender.model.ExpenseModel;
import reyes.r.christopher.spenderbender.persistence.LocalDatabaseHandler;

/**
 * Created by Christopher R Reyes on 8/19/16.
 *
 * Tests the view model for adding transactions
 */

public class TransactionViewModelTest {
    private ExpenseModel validExpenseModel1;
    private ExpenseModel validExpenseModel2;

    @Before
    public void setUp() throws Exception {
        validExpenseModel1 = new ExpenseModel(
                "Sushi",
                35.92,
                2016,
                Calendar.AUGUST,
                19
        );

        validExpenseModel2 = new ExpenseModel(
                "Potatoes",
                5.67,
                2016,
                Calendar.AUGUST,
                19
        );
    }

    @After
    public void tearDown() throws Exception {
        validExpenseModel1 = null;
        validExpenseModel2 = null;
    }

    @Test
    public void resetFields() throws Exception {

    }

    @Test
    public void validateFields() throws Exception {
        TransactionViewModel viewModel = new TransactionViewModel(mock(LocalDatabaseHandler.class));

        setViewModelPrivateFields(viewModel, validExpenseModel1);

        Assert.assertTrue("Valid expense should return valid fields", viewModel.validateFields());

        ExpenseModel invalidExpense1NullName = new ExpenseModel(
                null,
                1.23,
                2015,
                Calendar.FEBRUARY,
                2
        );

        setViewModelPrivateFields(viewModel, invalidExpense1NullName);

        Assert.assertFalse("Null name should not be valid for saving expense", viewModel.validateFields());

        setViewModelPrivateFields(viewModel, validExpenseModel2);

        Assert.assertTrue("Valid expense should return valid fields", viewModel.validateFields());

        ExpenseModel invalidExpense2EmptyName = new ExpenseModel(
                "",
                12.34,
                2015,
                Calendar.FEBRUARY,
                2
        );

        Assert.assertTrue("Couldn't correctly initialize Expense with an empty name", invalidExpense2EmptyName.getName().isEmpty());

        setViewModelPrivateFields(viewModel, invalidExpense2EmptyName);

        Assert.assertFalse("Empty name should not be valid for saving expenses", viewModel.validateFields());

        ExpenseModel invalidExpense3InfiniteAmount = new ExpenseModel(
                "positive infinity",
                Double.POSITIVE_INFINITY,
                2015,
                Calendar.FEBRUARY,
                2
        );

        setViewModelPrivateFields(viewModel, invalidExpense3InfiniteAmount);

        Assert.assertFalse("Positive infinity is not a valid transaction amount", viewModel.validateFields());

        ExpenseModel invalidExpense4InfiniteAmount = new ExpenseModel(
                "positive infinity",
                Double.NEGATIVE_INFINITY,
                2015,
                Calendar.FEBRUARY,
                2
        );

        setViewModelPrivateFields(viewModel, invalidExpense4InfiniteAmount);

        Assert.assertFalse("Negative infinity is not a valid transaction amount", viewModel.validateFields());

        ExpenseModel invalidExpense5InvalidYear = new ExpenseModel(
                "Year before 1k",
                1.23,
                500,
                Calendar.FEBRUARY,
                2
        );

        setViewModelPrivateFields(viewModel, invalidExpense5InvalidYear);

        Assert.assertFalse("Expenses cannot be incurred prior to 1000 AD", viewModel.validateFields());

        ExpenseModel validExpense6ValidYear = new ExpenseModel(
                "AD 1000",
                1.23,
                1000,
                Calendar.FEBRUARY,
                2
        );

        setViewModelPrivateFields(viewModel, validExpense6ValidYear);

        Assert.assertTrue("Expenses should be permitted beginning in 1000 AD", viewModel.validateFields());

        ExpenseModel invalidExpense7InvalidMonth = new ExpenseModel(
                "Month before January",
                1.23,
                2010,
                Calendar.JANUARY - 1,
                2
        );

        setViewModelPrivateFields(viewModel, invalidExpense7InvalidMonth);

        Assert.assertFalse("Only allow exact months as defined in Calendar class", viewModel.validateFields());

        ExpenseModel invalidExpense8InvalidMonth = new ExpenseModel(
                "Month after December",
                1.23,
                2010,
                Calendar.DECEMBER + 1,
                2
        );

        setViewModelPrivateFields(viewModel, invalidExpense8InvalidMonth);

        Assert.assertFalse("Only allow exact months as defined in Calendar class", viewModel.validateFields());

        ExpenseModel invalidExpense9InvalidDay = new ExpenseModel(
                "Day before 1",
                1.23,
                2010,
                Calendar.DECEMBER,
                0
        );

        setViewModelPrivateFields(viewModel, invalidExpense9InvalidDay);

        Assert.assertFalse("Only allow days between 1 and 31", viewModel.validateFields());

        ExpenseModel invalidExpense10InvalidDay = new ExpenseModel(
                "Day after 31",
                1.23,
                2010,
                Calendar.DECEMBER,
                32
        );

        setViewModelPrivateFields(viewModel, invalidExpense10InvalidDay);

        Assert.assertFalse("Only allow days between 1 and 31", viewModel.validateFields());

        ExpenseModel invalidExpense11InvalidDate = new ExpenseModel(
                "February 29, non leap year",
                1.23,
                2013,
                Calendar.FEBRUARY,
                29
        );

        setViewModelPrivateFields(viewModel, invalidExpense11InvalidDate);

        Assert.assertFalse("Only allow leap days on leap years", viewModel.validateFields());

        ExpenseModel validExpense12ValidDate = new ExpenseModel(
                "February 29, leap year",
                1.23,
                2008,
                Calendar.FEBRUARY,
                29
        );

        setViewModelPrivateFields(viewModel, validExpense12ValidDate);

        Assert.assertTrue("Allow leap days on leap years", viewModel.validateFields());

        ExpenseModel invalidExpense13InvalidDate = new ExpenseModel(
                "February 30",
                1.23,
                2008,
                Calendar.FEBRUARY,
                30
        );

        setViewModelPrivateFields(viewModel, invalidExpense13InvalidDate);

        Assert.assertFalse("February 30 is not a real date, no matter the year", viewModel.validateFields());

        ExpenseModel invalidExpense14InvalidDate = new ExpenseModel(
                "April 31",
                1.23,
                2008,
                Calendar.APRIL,
                31
        );

        setViewModelPrivateFields(viewModel, invalidExpense14InvalidDate);

        Assert.assertFalse("April 31 is not a real date, no matter the year", viewModel.validateFields());
    }

    @Test
    public void setStringAmount() throws Exception {
    }

    @Test
    public void setName() throws Exception {
        TransactionViewModel viewModel = new TransactionViewModel(mock(LocalDatabaseHandler.class));

        boolean expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setName(null);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertTrue("Name cannot be null", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setName("");
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Name can be the empty string", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setName("foo");
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Name can be a non-empty string", expectedExceptionThrown);

        // TODO: 8/21/16 Add tests for special characters, like emojis or hebrew or other writing languages
    }

    @Test
    public void setYearIncurred() throws Exception {
        TransactionViewModel viewModel = new TransactionViewModel(mock(LocalDatabaseHandler.class));

        boolean expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(999);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertTrue("Year cannot be before 1000 AD", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(0);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertTrue("Year cannot be before 1000 AD", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(1);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertTrue("Year cannot be before 1000 AD", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(-1);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertTrue("Year cannot be before 1000 AD", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(1000);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Year 1000 AD is valid", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(1001);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Years after 1000 AD are valid", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(1900);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Years after 1000 AD are valid", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(1901);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Years after 1000 AD are valid", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(1100);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Years after 1000 AD are valid", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(1101);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Years after 1000 AD are valid", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(1999);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Years after 1000 AD are valid", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(2000);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Years after 1000 AD are valid", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(20000);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Years after 1000 AD are valid", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setYearIncurred(Integer.MAX_VALUE);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Years after 1000 AD are valid", expectedExceptionThrown);
    }

    @Test
    public void setMonthIncurred() throws Exception {
        TransactionViewModel viewModel = new TransactionViewModel(mock(LocalDatabaseHandler.class));

        boolean expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.JANUARY-1);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertTrue("Month cannot be before January", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.JANUARY);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("January is a valid Month", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.FEBRUARY);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("February is a valid Month", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.MARCH);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("March is a valid Month", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.APRIL);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("April is a valid Month", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.MAY);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("May is a valid Month", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.JUNE);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("June is a valid Month", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.JULY);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("July is a valid Month", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.AUGUST);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("August is a valid Month", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.SEPTEMBER);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("September is a valid Month", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.OCTOBER);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("October is a valid Month", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.NOVEMBER);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("November is a valid Month", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.DECEMBER);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("December is a valid Month", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setMonthIncurred(Calendar.DECEMBER+1);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertTrue("Month cannot be after December", expectedExceptionThrown);
    }

    @Test
    public void setDayIncurred() throws Exception {
        TransactionViewModel viewModel = new TransactionViewModel(mock(LocalDatabaseHandler.class));

        boolean expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setDayIncurred(0);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertTrue("Day cannot be less than 1", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setDayIncurred(-1);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertTrue("Day cannot be less than 1", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setDayIncurred(1);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Day can be 1", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setDayIncurred(32);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertTrue("Day cannot be greater than 31", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setDayIncurred(31);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Day can be 31", expectedExceptionThrown);

        expectedExceptionThrown = Boolean.FALSE;
        try {
            viewModel.setDayIncurred(15);
        } catch ( IllegalArgumentException e ) {
            expectedExceptionThrown = Boolean.TRUE;
        } catch ( Exception e ) {
            Assert.assertFalse("Caught unexpected exception: " + e.getMessage(), Boolean.TRUE);
        }

        Assert.assertFalse("Day can be 15", expectedExceptionThrown);
    }

    @Test
    public void addExpenseWithArgs() {
        MockDatabaseHandler testDbh = new MockDatabaseHandler();

        TransactionViewModel viewModel = new TransactionViewModel(testDbh);

        viewModel.addExpense(
                validExpenseModel1.getName(),
                validExpenseModel1.getAmount(),
                validExpenseModel1.getYearIncurred(),
                validExpenseModel1.getMonthIncurred(),
                validExpenseModel1.getDayIncurred()
        );

        ExpenseModel savedExpense = testDbh.getLastSavedExpense();

        Assert.assertEquals("Saved the correct expense name", validExpenseModel1.getName(), savedExpense.getName());
        Assert.assertEquals("Saved the correct expense amount", validExpenseModel1.getAmount(), savedExpense.getAmount(), 0.001);
        Assert.assertEquals("Saved the correct expense year", validExpenseModel1.getYearIncurred(), savedExpense.getYearIncurred());
        Assert.assertEquals("Saved the correct expense month", validExpenseModel1.getMonthIncurred(), savedExpense.getMonthIncurred());
        Assert.assertEquals("Saved the correct expense day", validExpenseModel1.getDayIncurred(), savedExpense.getDayIncurred());

        viewModel.addExpense(
                validExpenseModel2.getName(),
                validExpenseModel2.getAmount(),
                validExpenseModel2.getYearIncurred(),
                validExpenseModel2.getMonthIncurred(),
                validExpenseModel2.getDayIncurred()
        );

        savedExpense = testDbh.getLastSavedExpense();

        Assert.assertEquals("Saved the correct expense name", validExpenseModel2.getName(), savedExpense.getName());
        Assert.assertEquals("Saved the correct expense amount", validExpenseModel2.getAmount(), savedExpense.getAmount(), 0.001);
        Assert.assertEquals("Saved the correct expense year", validExpenseModel2.getYearIncurred(), savedExpense.getYearIncurred());
        Assert.assertEquals("Saved the correct expense month", validExpenseModel2.getMonthIncurred(), savedExpense.getMonthIncurred());
        Assert.assertEquals("Saved the correct expense day", validExpenseModel2.getDayIncurred(), savedExpense.getDayIncurred());
    }

    @Test
    public void addExpenseWithoutArgs() throws Exception {
        TransactionViewModel viewModel = mock(TransactionViewModel.class);

        final ArrayList<ExpenseModel> savedExpenses = new ArrayList<>();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                savedExpenses.add(new ExpenseModel(
                        (String) args[0],
                        (Double) args[1],
                        (Integer) args[2],
                        (Integer) args[3],
                        (Integer) args[4]
                ));
                return null;
            }
        }).when(viewModel).addExpense(anyString(), anyDouble(), anyInt(), anyInt(), anyInt());

        doCallRealMethod().when(viewModel).addExpense();
        when(viewModel.validateFields()).thenReturn(Boolean.TRUE);

        setViewModelPrivateFields(viewModel, validExpenseModel1);
        viewModel.addExpense();

        ExpenseModel savedExpense = savedExpenses.get(savedExpenses.size() - 1);

        Assert.assertEquals("Saved the correct expense name", validExpenseModel1.getName(), savedExpense.getName());
        Assert.assertEquals("Saved the correct expense amount", validExpenseModel1.getAmount(), savedExpense.getAmount(), 0.001);
        Assert.assertEquals("Saved the correct expense year", validExpenseModel1.getYearIncurred(), savedExpense.getYearIncurred());
        Assert.assertEquals("Saved the correct expense month", validExpenseModel1.getMonthIncurred(), savedExpense.getMonthIncurred());
        Assert.assertEquals("Saved the correct expense day", validExpenseModel1.getDayIncurred(), savedExpense.getDayIncurred());

        setViewModelPrivateFields(viewModel, validExpenseModel2);
        viewModel.addExpense();

        savedExpense = savedExpenses.get(savedExpenses.size() - 1);

        Assert.assertEquals("Saved the correct expense name", validExpenseModel2.getName(), savedExpense.getName());
        Assert.assertEquals("Saved the correct expense amount", validExpenseModel2.getAmount(), savedExpense.getAmount(), 0.001);
        Assert.assertEquals("Saved the correct expense year", validExpenseModel2.getYearIncurred(), savedExpense.getYearIncurred());
        Assert.assertEquals("Saved the correct expense month", validExpenseModel2.getMonthIncurred(), savedExpense.getMonthIncurred());
        Assert.assertEquals("Saved the correct expense day", validExpenseModel2.getDayIncurred(), savedExpense.getDayIncurred());

        boolean threwExpectedException = Boolean.FALSE;
        when(viewModel.validateFields()).thenReturn(Boolean.FALSE);

        // Should throw exception when it has invalid fields
        try {
            viewModel.addExpense();
        } catch (IllegalStateException e) {
            threwExpectedException = Boolean.TRUE;
        } catch (Exception e) {
            threwExpectedException = Boolean.FALSE;
        }

        Assert.assertTrue("Should not allow saving with invalid fields", threwExpectedException);
    }

    private void setViewModelPrivateFields(TransactionViewModel viewModel, ExpenseModel expense) {
        Whitebox.setInternalState(viewModel, "name", expense.getName());
        Whitebox.setInternalState(viewModel, "amount", expense.getAmount());
        Whitebox.setInternalState(viewModel, "yearIncurred", expense.getYearIncurred());
        Whitebox.setInternalState(viewModel, "monthIncurred", expense.getMonthIncurred());
        Whitebox.setInternalState(viewModel, "dayIncurred", expense.getDayIncurred());
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
