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

package reyes.r.christopher.spenderbender.model;

import junit.framework.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Christopher R Reyes on 8/17/16.
 */
public class ExpenseModelTest {
    @Test
    public void testLoadConstructor() throws Exception {
        String name1 = "rent";
        Double amount1 = 3500.99;

        int yearIncurred1 = 2015;
        int monthIncurred1 = Calendar.MAY;
        int dayIncurred1 = 15;

        int yearCreated1 = 2013;
        int monthCreated1 = Calendar.JANUARY;
        int dayCreated1 = 15;

        long id1 = 15;

        ExpenseModel expenseModel1 = new ExpenseModel(
                name1,
                amount1,
                yearIncurred1,
                monthIncurred1,
                dayIncurred1,
                yearCreated1,
                monthCreated1,
                dayCreated1,
                id1
        );

        Calendar today = Calendar.getInstance();

        Assert.assertEquals("Name should be set appropriately", name1, expenseModel1.getName());
        Assert.assertEquals("Amount incurred should be set appropriately", amount1, expenseModel1.getAmount());
        Assert.assertEquals("Year incurred should be set appropriately", yearIncurred1, expenseModel1.getYearIncurred());
        Assert.assertEquals("Month incurred should be set appropriately", monthIncurred1, expenseModel1.getMonthIncurred());
        Assert.assertEquals("Day incurred should be set appropriately", dayIncurred1, expenseModel1.getDayIncurred());
        Assert.assertEquals("Year Created should be supplied year", yearCreated1, expenseModel1.getYearCreated());
        Assert.assertEquals("Month Created should be supplied Month", monthCreated1, expenseModel1.getMonthCreated());
        Assert.assertEquals("Day Created should be supplied Day", dayCreated1, expenseModel1.getDayCreated());
        Assert.assertEquals("Id should be same as supplied Id", id1, expenseModel1.getId());

        // Make sure multiple instances of ExpenseModel can exist simultaneously
        String name2 = "parking";
        Double amount2 = 3.45;

        int yearIncurred2 = 2016;
        int monthIncurred2 = Calendar.APRIL;
        int dayIncurred2 = 25;

        int yearCreated2 = 2012;
        int monthCreated2 = Calendar.JUNE;
        int dayCreated2 = 1;

        long id2 = 4;

        ExpenseModel expenseModel2 = new ExpenseModel(
                name2,
                amount2,
                yearIncurred2,
                monthIncurred2,
                dayIncurred2,
                yearCreated2,
                monthCreated2,
                dayCreated2,
                id2
        );

        Assert.assertEquals("Name should be set appropriately", name2, expenseModel2.getName());
        Assert.assertEquals("Amount incurred should be set appropriately", amount2, expenseModel2.getAmount());
        Assert.assertEquals("Year incurred should be set appropriately", yearIncurred2, expenseModel2.getYearIncurred());
        Assert.assertEquals("Month incurred should be set appropriately", monthIncurred2, expenseModel2.getMonthIncurred());
        Assert.assertEquals("Day incurred should be set appropriately", dayIncurred2, expenseModel2.getDayIncurred());
        Assert.assertEquals("Year Created should be supplied year", yearCreated2, expenseModel2.getYearCreated());
        Assert.assertEquals("Month Created should be supplied Month", monthCreated2, expenseModel2.getMonthCreated());
        Assert.assertEquals("Day Created should be supplied Day", dayCreated2, expenseModel2.getDayCreated());
        Assert.assertEquals("Id should be same as supplied Id", id2, expenseModel2.getId());
    }

    @Test
    public void testCreateNewConstructor() throws Exception {
        String name1 = "rent";
        Double amount1 = 3500.99;

        int yearIncurred1 = 2015;
        int monthIncurred1 = Calendar.MAY;
        int dayIncurred1 = 15;

        ExpenseModel expenseModel1 = new ExpenseModel(name1, amount1, yearIncurred1, monthIncurred1, dayIncurred1);

        Calendar today = Calendar.getInstance();

        Assert.assertEquals("Name should be set appropriately", name1, expenseModel1.getName());
        Assert.assertEquals("Amount incurred should be set appropriately", amount1, expenseModel1.getAmount());
        Assert.assertEquals("Year incurred should be set appropriately", yearIncurred1, expenseModel1.getYearIncurred());
        Assert.assertEquals("Month incurred should be set appropriately", monthIncurred1, expenseModel1.getMonthIncurred());
        Assert.assertEquals("Day incurred should be set appropriately", dayIncurred1, expenseModel1.getDayIncurred());
        Assert.assertEquals("Year Created should be current year", today.get(Calendar.YEAR), expenseModel1.getYearCreated());
        Assert.assertEquals("Month Created should be current Month", today.get(Calendar.MONTH), expenseModel1.getMonthCreated());
        Assert.assertEquals("Day Created should be current Day", today.get(Calendar.DAY_OF_MONTH), expenseModel1.getDayCreated());
        Assert.assertEquals("Id should be unsaved Id", ExpenseModel.UNSAVED_EXPENSE, expenseModel1.getId());

        // Make sure multiple instances of ExpenseModel can exist simultaneously
        String name2 = "parking";
        Double amount2 = 3.45;

        int yearIncurred2 = 2016;
        int monthIncurred2 = Calendar.APRIL;
        int dayIncurred2 = 25;

        ExpenseModel expenseModel2 = new ExpenseModel(name2, amount2, yearIncurred2, monthIncurred2, dayIncurred2);

        Assert.assertEquals("Name should be set appropriately", name2, expenseModel2.getName());
        Assert.assertEquals("Amount incurred should be set appropriately", amount2, expenseModel2.getAmount());
        Assert.assertEquals("Year incurred should be set appropriately", yearIncurred2, expenseModel2.getYearIncurred());
        Assert.assertEquals("Month incurred should be set appropriately", monthIncurred2, expenseModel2.getMonthIncurred());
        Assert.assertEquals("Day incurred should be set appropriately", dayIncurred2, expenseModel2.getDayIncurred());
        Assert.assertEquals("Year Created should be current year", today.get(Calendar.YEAR), expenseModel2.getYearCreated());
        Assert.assertEquals("Month Created should be current Month", today.get(Calendar.MONTH), expenseModel2.getMonthCreated());
        Assert.assertEquals("Day Created should be current Day", today.get(Calendar.DAY_OF_MONTH), expenseModel2.getDayCreated());
        Assert.assertEquals("Id should be unsaved Id", ExpenseModel.UNSAVED_EXPENSE, expenseModel2.getId());
    }

    @Test
    public void getAmountAsString() throws Exception {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());

        String name = "testName";

        int yearIncurred = 2000;
        int monthIncurred = Calendar.MAY;
        int dayIncurred = 29;

        double amountToTest = 1;

        ExpenseModel expenseModel = new ExpenseModel(
                name,
                amountToTest,
                yearIncurred,
                monthIncurred,
                dayIncurred
        );

        Assert.assertEquals("Test set up incorrectly: amount is not equal", amountToTest, expenseModel.getAmount(), 0.001);

        Assert.assertEquals("Integer amounts are the same in string or double form", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = 1.0;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Whole number amounts are equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = 0.1;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Decimal amounts are equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = .1;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Decimal amounts are equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = 9.;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Decimal amounts are equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = -9.;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Negative decimal amounts are equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = -0.9;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Negative decimal amounts are equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = -0.0;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Zero is equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = 0.0;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Zero is equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = 0.;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Zero is equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = 0;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Zero is equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = .0;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Zero is equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = Double.MAX_VALUE;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Big amounts are equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = Double.MIN_VALUE;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Small amounts are equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = -1 * Double.MAX_VALUE;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Big negative amounts are equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);

        amountToTest = -1 * Double.MIN_VALUE;

        Whitebox.setInternalState(expenseModel, "amount", amountToTest);
        Assert.assertEquals("Small negative amounts are equivalent", amountToTest, formatter.parse(expenseModel.getAmountAsString()).doubleValue(), 0.001);
    }

}