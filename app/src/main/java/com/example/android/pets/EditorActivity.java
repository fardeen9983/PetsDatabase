
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.pets.data.PetsContract.petsTable;
import com.example.android.pets.data.PetsDBManager;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    private Cursor cursor;
    private PetsDBManager petsDBManager;
    private SQLiteDatabase db;
    private final String TAG = getClass().getSimpleName();
    private int id = -1;
    private ContentValues values;
    /**
     * EditText field to enter the pet's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the pet's breed
     */
    private EditText mBreedEditText;

    /**
     * EditText field to enter the pet's weight
     */
    private EditText mWeightEditText;

    /**
     * EditText field to enter the pet's gender
     */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = 0;
    private PetDetail petDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.edit_pet_name);
        mBreedEditText = findViewById(R.id.edit_pet_breed);
        mWeightEditText = findViewById(R.id.edit_pet_weight);
        mGenderSpinner = findViewById(R.id.spinner_gender);

        setupSpinner();

        petDetail = (PetDetail) getIntent().getSerializableExtra("pet");
        if (petDetail != null) {
            mNameEditText.setText(petDetail.getName());
            mBreedEditText.setText(petDetail.getBreed());
            mWeightEditText.setText(petDetail.getWeight() + "");
            getSupportActionBar().setTitle("Edit Details");
            id = petDetail.get_id();
            switch (petDetail.getGender()){
                case 0  : mGenderSpinner.setSelection(0);
                        break;
                case 1  : mGenderSpinner.setSelection(1);
                        break;
                case 2  : mGenderSpinner.setSelection(2);
            }
        }
        petsDBManager = new PetsDBManager(this);

    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = 1; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = 2; // Female
                    } else {
                        mGender = 0; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Do nothing for now
                if (id == -1)
                    insertRowIntoDB();
                else
                    updateRow();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                if (id != -1) {
                    db = petsDBManager.getWritableDatabase();
                    db.delete(petsTable.TABLE_NAME,petsTable.COLUMN_PET_ID + " =?", new String[]{id + ""});
                   // db.execSQL("DELETE FROM " + petsTable.TABLE_NAME + " WHERE " + petsTable.COLUMN_PET_ID + " IS " + id + " ;");
                    changeCursor(db);
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertRowIntoDB() {
        values = new ContentValues();
        values.put(petsTable.COLUMN_PET_NAME, mNameEditText.getText().toString().trim());
        values.put(petsTable.COLUMN_PET_BREED, mBreedEditText.getText().toString().trim());
        values.put(petsTable.COLUMN_PET_GENDER, mGender);
        String weight = mWeightEditText.getText().toString().trim();
        int Weight = 0;
        if (weight == "" || weight == null) {
            mWeightEditText.setError("Weight cannot be left empty ");
            return;
        } else {
            try {
                Weight = Integer.parseInt(weight);
            } catch (NumberFormatException e) {
                Log.v(TAG, "Weight is empty ", e);
            }
        }
        values.put(petsTable.COLUMN_PET_WEIGHT, Weight);

        db = petsDBManager.getWritableDatabase();
        long id = db.insert(petsTable.TABLE_NAME, null, values);

        if (id != -1) {
            Toast.makeText(this, "New Pet details added with id : " + id, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Error with insertion", Toast.LENGTH_SHORT).show();
        changeCursor(db);
        NavUtils.navigateUpFromSameTask(this);
    }

    private static void changeCursor(SQLiteDatabase db) {
        CatalogActivity.petDetailArrayAdapter.changeCursor(db.query(petsTable.TABLE_NAME, null, null, null, null, null, null));
        CatalogActivity.petDetailArrayAdapter.notifyDataSetChanged();
    }

    private void updateRow() {
        db = petsDBManager.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(petsTable.COLUMN_PET_NAME, mNameEditText.getText().toString());
        values.put(petsTable.COLUMN_PET_BREED, mBreedEditText.getText().toString());
        String y = mWeightEditText.getText().toString().trim();
        if (y == "" || y == null) {
            mWeightEditText.setError("Weight cannot be left empty");
            return;
        } else {
            int x = Integer.parseInt(y);
            values.put(petsTable.COLUMN_PET_GENDER, mGender);
            values.put(petsTable.COLUMN_PET_WEIGHT,x);
            db.update(petsTable.TABLE_NAME, values, petsTable.COLUMN_PET_ID + " =?", new String[]{id + ""});
            changeCursor(db);
            NavUtils.navigateUpFromSameTask(this);
        }
    }
}