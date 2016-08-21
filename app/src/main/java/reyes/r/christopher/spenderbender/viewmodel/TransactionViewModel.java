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
import android.databinding.Bindable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import reyes.r.christopher.spenderbender.BR;
import reyes.r.christopher.spenderbender.model.ExpenseModel;
import reyes.r.christopher.spenderbender.persistence.LocalDatabaseHandler;

/**
 * Created by Christopher R Reyes on 8/19/16.
 *
 * ViewModel for manipulating Transactions
 */

public class TransactionViewModel extends BaseObservable {
    private final LocalDatabaseHandler dbh;

    private double amount;
    private String stringAmount;
    private String name;
    private int yearIncurred;
    private int monthIncurred;
    private int dayIncurred;

    private final DecimalFormat moneyFormat = new DecimalFormat("#.##");
    private final HashSet<Integer> validMonths = new HashSet<>();

    public TransactionViewModel(LocalDatabaseHandler dbh) {
        this.dbh = dbh;

        moneyFormat.setRoundingMode(RoundingMode.CEILING);

        validMonths.add(GregorianCalendar.JANUARY);
        validMonths.add(GregorianCalendar.FEBRUARY);
        validMonths.add(GregorianCalendar.MARCH);
        validMonths.add(GregorianCalendar.APRIL);
        validMonths.add(GregorianCalendar.MAY);
        validMonths.add(GregorianCalendar.JUNE);
        validMonths.add(GregorianCalendar.JULY);
        validMonths.add(GregorianCalendar.AUGUST);
        validMonths.add(GregorianCalendar.SEPTEMBER);
        validMonths.add(GregorianCalendar.OCTOBER);
        validMonths.add(GregorianCalendar.NOVEMBER);
        validMonths.add(GregorianCalendar.DECEMBER);

        // Default the date incurred to today
        Calendar today = GregorianCalendar.getInstance();
        setDateIncurred(today.get(GregorianCalendar.YEAR), today.get(GregorianCalendar.MONTH), today.get(GregorianCalendar.DAY_OF_MONTH));
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

    public void addExpense() throws IllegalStateException {
        if (validateFields()) {
            addExpense(this.name, this.amount, this.yearIncurred, this.monthIncurred, this.dayIncurred);
        }
        else {
            throw new IllegalStateException("Cannot save expense to database because current fields are not valid");
        }
    }

    /**
     * Validates the current stored fields for the purpose of the addExpense() method
     * @return true if the current stored fields are valid for saving to the database
     */
    public boolean validateFields() {
        // Just need to ensure that every field is specified
        return (
                isValidName(this.name) &&
                        isValidAmount(this.amount) &&
                        isValidYear(this.yearIncurred) &&
                        isValidMonth(this.monthIncurred) &&
                        isValidDay(this.dayIncurred) &&
                        isValidDate(this.yearIncurred, this.monthIncurred, this.dayIncurred)
        );
    }

    public double getAmount() {
        return amount;
    }

    /**
     * Sets our double representation of the amount of this transaction
     * Remains synchronized with our String representation as well
     * @param amount
     */
    public void setAmount(double amount) throws IllegalArgumentException {
        if (! isValidAmount(amount)) {
            throw new IllegalArgumentException("Amount [" + String.valueOf(amount) + "] is not a valid amount");
        }
        this.amount = amount;
        this.stringAmount = moneyFormat.format(amount);
        notifyPropertyChanged(BR.stringAmount);
    }

    @Bindable
    public String getStringAmount() {
        return stringAmount;
    }

    /**
     * Sets our String representation of the current amount of this transaction
     * Remains synchronized with our actual double representation of that amount
     * @param amount
     */
    public void setStringAmount(String amount) throws IllegalArgumentException {
        String safeAmount = amount;
        if (safeAmount.isEmpty()) {
            safeAmount = String.valueOf((double) 0);
        }

        if (! isValidAmount(safeAmount)) {
            throw new IllegalArgumentException("Amount [" + safeAmount + "] is not a valid amount");
        }

        this.amount = Double.valueOf(safeAmount);

        this.stringAmount = moneyFormat.format(this.amount);

        notifyPropertyChanged(BR.stringAmount);
    }

    /**
     * Validates that the amount of the transaction is valid
     * Pretty much any finite amount is valid
     * @param amount
     * @return true if the amount is valid
     */
    private boolean isValidAmount(double amount) {
        return (! Double.isInfinite(amount));
    }

    /**
     * Validates that the amount of the transaction is valid
     * Also checks the conversion from String to double
     * @param amount
     * @return
     */
    private boolean isValidAmount(String amount) {
        double formattedAmount;
        try {
            formattedAmount = Double.valueOf(amount);
        } catch (NumberFormatException e) {
            return Boolean.FALSE;
        }
        return isValidAmount(formattedAmount);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Name must be neither null nor empty");
        }
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    /**
     * Checks whether or not the name is a valid, non-empty String
     * @param name
     * @return
     */
    private boolean isValidName(String name) {
        return ( name != null && ! name.isEmpty() );
    }

    @Bindable
    public int getYearIncurred() {
        return this.yearIncurred;
    }

    public void setYearIncurred(int yearIncurred) throws IllegalArgumentException {
        if (! isValidYear(yearIncurred)) {
            throw new IllegalArgumentException("Must supply a valid year in which the expense was incurred");
        }
        this.yearIncurred = yearIncurred;
        notifyPropertyChanged(BR.yearIncurred);
    }

    /**
     * Determines if the specified year is a valid year for the transaction.
     * Unfortunately, years before 1000 AD are not supported
     * @param year
     * @return true if year is valid for this transaction
     */
    private boolean isValidYear(int year) {
        // Require that years be at least 4 digits for the purpose of validating specific dates.
        // Sorry, that means no ancient transactions for you!
        return year >= 1000;
    }

    @Bindable
    public int getMonthIncurred() {
        return monthIncurred;
    }

    public void setMonthIncurred(int monthIncurred) throws IllegalArgumentException {
        if ( ! isValidMonth(monthIncurred) ) {
            throw new IllegalArgumentException("Month incurred must be an integer between " + String.valueOf(GregorianCalendar.JANUARY) + " (for January) and " + String.valueOf(GregorianCalendar.DECEMBER) + " (for December).");
        }

        this.monthIncurred = monthIncurred;
        notifyPropertyChanged(BR.monthIncurred);
    }

    /**
     * Checks whether the supplied month is valid or not
     * @param month
     * @return
     */
    private boolean isValidMonth(int month) {
        return (this.validMonths.contains(month));
    }

    @Bindable
    public int getDayIncurred() {
        return dayIncurred;
    }

    public void setDayIncurred(int dayIncurred) throws IllegalArgumentException {
        if(! isValidDay(dayIncurred)) {
            throw new IllegalArgumentException("Must specify a valid day of the month in which the expense was incurred");
        }
        this.dayIncurred = dayIncurred;
        notifyPropertyChanged(BR.dayIncurred);
    }

    /**
     * Does a brief check to see if the day could be a valid day of the month
     * @param day - The day to check
     * @return true if the day could be a valid day for some month
     */
    private boolean isValidDay(int day) {
        // Do a gut-check that the day is valid.
        // If we want to know if the day is actually valid for the given month, then use isValidDate
        return (day >= 1 && day <= 31);
    }

    /**
     * Sets year, month, and day simultaneously
     * @param yearIncurred
     * @param monthIncurred
     * @param dayIncurred
     * @throws IllegalArgumentException
     */
    private void setDateIncurred(int yearIncurred, int monthIncurred, int dayIncurred) throws IllegalArgumentException {
        if (isValidDate(yearIncurred, monthIncurred, dayIncurred)) {
            this.yearIncurred = yearIncurred;
            this.monthIncurred = monthIncurred;
            this.dayIncurred = dayIncurred;
        }
        else {
            throw new IllegalArgumentException("Must supply a valid date");
        }
    }

    /**
     * Validates that the specified date is a valid date
     * @param year
     * @param month
     * @param day
     * @return true if the date will ever exist or has already happened
     */
    private boolean isValidDate(int year, int month, int day) {
        GregorianCalendar testDate = new GregorianCalendar(year, month, day);
        testDate.setLenient(Boolean.FALSE);
        try {
            testDate.getTime();
        } catch (IllegalArgumentException e ) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
