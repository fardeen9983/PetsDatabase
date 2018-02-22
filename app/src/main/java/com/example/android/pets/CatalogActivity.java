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
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    private ArrayAdapter<PetDetail> petAdapter;
    private TextView textView;
    private ArrayList<PetDetail> petDetails;
    private String selection = petsTable.COLUMN_PET_ID + " >?";
    private String[] selectionArgs = {"3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        petsDBManager = new PetsDBManager(this);
        petDBR = petsDBManager.getReadableDatabase();
        petDBW = petsDBManager.getWritableDatabase();
        listView = findViewById(R.id.list_view);
        textView = findViewById(R.id.text_view_pet);
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        init();
        displayDatabaseInfo();
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
        Cursor cursor = petDBR.query(petsTable.TABLE_NAME, null, null, null, null, null, null);
        StringBuilder builder = new StringBuilder();
        int i, j, col = cursor.getColumnCount(), count = cursor.getCount();

        for (j = 0; j < col; j++)
            builder.append(cursor.getColumnName(j) + " ");
        if (cursor.moveToFirst()) {
            do {
                i = cursor.getPosition();
                builder.append("\n");
                for (j = 0; j < col; j++)
                    builder.append(cursor.getString(j) + " ");
            } while (cursor.moveToNext());
        }

        textView.setText("Current rows in the " + petsTable.TABLE_NAME + " table : " + cursor.getCount() + "\n" + builder.toString());
        cursor.close();
    }

    private void deleteAllData() {
        PetsDBManager.deleteAllData(petDBW);
        displayDatabaseInfo();
    }

    public void init() {
        Cursor cursor = petDBW.rawQuery("SELECT * FROM " + petsTable.TABLE_NAME + ";", null);
        int i = cursor.getCount();
        for (int j = 0; j < i; j++) {
        }
        cursor.close();
    }
}
