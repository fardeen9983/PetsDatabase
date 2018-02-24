package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetsContract.petsTable;
import com.example.android.pets.data.PetsDBManager;

public class PetAdpater extends CursorAdapter {

    private PetDetail petDetail;
    private Cursor cursor;
    private Context context;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onContentChanged() {
        super.onContentChanged();
    }

    public PetAdpater(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.cursor = c;
        this.context = context;
    }

    @Override
    public Object getItem(int position) {
        PetsDBManager petsDBManager = new PetsDBManager(context);
        SQLiteDatabase db = petsDBManager.getReadableDatabase();
        cursor = db.query(petsTable.TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToPosition(position);
        return new PetDetail(cursor.getInt(cursor.getColumnIndex(petsTable.COLUMN_PET_ID))
                , cursor.getString(cursor.getColumnIndex(petsTable.COLUMN_PET_NAME))
                , cursor.getString(cursor.getColumnIndex(petsTable.COLUMN_PET_BREED))
                , cursor.getInt(cursor.getColumnIndex(petsTable.COLUMN_PET_GENDER))
                , cursor.getInt(cursor.getColumnIndex(petsTable.COLUMN_PET_WEIGHT)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.pet_layout, parent, false);
        Log.v(TAG, "new View created");
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.v(TAG, "view binded");
        if (view == null)
            Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show();
        TextView petName = view.findViewById(R.id.pet_name);
        TextView petBreed = view.findViewById(R.id.pet_breed);

        petBreed.setText(cursor.getString(cursor.getColumnIndex(petsTable.COLUMN_PET_BREED)));
        petName.setText(cursor.getString(cursor.getColumnIndex(petsTable.COLUMN_PET_NAME)));
    }
}
