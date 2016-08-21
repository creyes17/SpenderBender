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

    public TransactionViewModel(LocalDatabaseHandler dbh) {
        this.dbh = dbh;

        moneyFormat.setRoundingMode(RoundingMode.CEILING);
        this.amount = 1.23;
        this.stringAmount = moneyFormat.format(this.amount);
        this.name = "Chris";
        this.yearIncurred = 1990;
        this.monthIncurred = Calendar.MAY;
        this.dayIncurred = 15;
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

    public void addExpense() {
        addExpense(this.name, this.amount, this.yearIncurred, this.monthIncurred, this.dayIncurred);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        this.stringAmount = moneyFormat.format(amount);
        notifyPropertyChanged(BR.stringAmount);
    }

    @Bindable
    public String getStringAmount() {
        return stringAmount;
    }

    public void setStringAmount(String amount) {
        if (this.stringAmount.isEmpty()) {
            this.amount = 0;
        }
        else {
            this.amount = Double.valueOf(amount);
        }
        this.stringAmount = moneyFormat.format(this.amount);
        notifyPropertyChanged(BR.stringAmount);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getYearIncurred() {
        return yearIncurred;
    }

    public void setYearIncurred(int yearIncurred) {
        this.yearIncurred = yearIncurred;
        notifyPropertyChanged(BR.yearIncurred);
    }

    @Bindable
    public int getMonthIncurred() {
        return monthIncurred;
    }

    public void setMonthIncurred(int monthIncurred) {
        this.monthIncurred = monthIncurred;
        notifyPropertyChanged(BR.monthIncurred);
    }

    @Bindable
    public int getDayIncurred() {
        return dayIncurred;
    }

    public void setDayIncurred(int dayIncurred) {
        this.dayIncurred = dayIncurred;
        notifyPropertyChanged(BR.dayIncurred);
    }
}
