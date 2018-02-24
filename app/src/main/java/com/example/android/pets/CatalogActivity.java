/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetsDBManager;
import com.example.android.pets.data.PetsContract.petsTable;

import java.util.ArrayList;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    private SQLiteDatabase petDBR, petDBW;
    private PetsDBManager petsDBManager;
    private ListView listView;
    private TextView empty_listview;
    private String[] projection = {petsTable.COLUMN_PET_ID, petsTable.COLUMN_PET_NAME, petsTable.COLUMN_PET_BREED};
    private String selection = null;
    private String[] selectionArgs = null;
    private final String TAG = getClass().getSimpleName();

    //List
    private PetDetail petDetail;
    private ArrayList<PetDetail> petDetails;
    public static PetAdpater petDetailArrayAdapter;

    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        Log.v(TAG, "onCreate called");
        petDetail = new PetDetail();

        petsDBManager = new PetsDBManager(this);
        petDBR = petsDBManager.getReadableDatabase();
        petDBW = petsDBManager.getWritableDatabase();
        listView = findViewById(R.id.list_view);
        empty_listview = findViewById(R.id.text_view_pet);
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        displayDatabaseInfo();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                petDetail = (PetDetail) petDetailArrayAdapter.getItem(position);
                intent.putExtra("pet", petDetail);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_delete_all_entries:
                deleteAllData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayDatabaseInfo() {
        Log.v(TAG, "display function called");
        PetAsynLoadDetails petAsynLoadDetails = new PetAsynLoadDetails();
        petAsynLoadDetails.execute();
        Log.v(TAG, "blah blah");
    }

    private void deleteAllData() {
        PetsDBManager.deleteAllData(petDBW);
        petDetails.clear();
        petDetailArrayAdapter.notifyDataSetChanged();
        displayDatabaseInfo();
    }

    private class PetAsynLoadDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            cursor = petDBR.query(petsTable.TABLE_NAME, null, null, null, null, null, null);
            petDetails = new ArrayList<>();
            int i = 0, j;
            if (cursor.moveToFirst()) {
                do {
                    petDetail = new PetDetail();
                    i = 0;
                    petDetail.set_id(cursor.getInt(i++));
                    petDetail.setName(cursor.getString(i++));
                    petDetail.setBreed(cursor.getString(i++));
                    petDetail.setGender(cursor.getInt(i++));
                    petDetail.setWeight(cursor.getInt(i));

                    petDetails.add(petDetail);

                } while (cursor.moveToNext());
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            petDetailArrayAdapter = new PetAdpater(CatalogActivity.this, cursor, true);
            listView.setAdapter(petDetailArrayAdapter);
        }
    }
}
