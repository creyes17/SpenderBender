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

package reyes.r.christopher.spenderbender;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import reyes.r.christopher.spenderbender.databinding.ActivityRecordExpenseBinding;
import reyes.r.christopher.spenderbender.persistence.LocalDatabaseHandler;
import reyes.r.christopher.spenderbender.viewmodel.TransactionViewModel;

public class RecordExpenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up main activity
        LocalDatabaseHandler dbh = new LocalDatabaseHandler(this);
        TransactionViewModel viewModel = new TransactionViewModel(dbh);

        final ActivityRecordExpenseBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_record_expense);
        binding.setViewModel(viewModel);

        // Set up Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            //toolbar.setTitle(getString(R.string.app_name));
            setSupportActionBar(toolbar);
        }

        // Set onClick handlers
        Button submit = (Button) findViewById(R.id.submit);
        assert submit != null;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionViewModel vm = binding.getViewModel();
                String toastMessage = getString(R.string.record_expense_submit_success);
                if (vm.validateFields()) {
                    vm.addExpense();
                    vm.resetFields();
                }
                else {
                    toastMessage = getString(R.string.record_expense_submit_validation_failed);
                }
                Toast.makeText(RecordExpenseActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.record_expense_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.toolbar_list_expenses) {
            startActivity(new Intent(this, ViewExpenseListActivity.class));
        }
        return true;
    }
}
