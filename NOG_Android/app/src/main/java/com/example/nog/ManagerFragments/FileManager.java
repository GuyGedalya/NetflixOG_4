package com.example.nog.ManagerFragments;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FileManager {
    private final Context context;
    public FileManager(Context context){
        this.context = context;
    }

    // Getting path from Uri
    public String getPathFromUri(Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }
}
