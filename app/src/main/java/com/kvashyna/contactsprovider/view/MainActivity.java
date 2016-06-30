package com.kvashyna.contactsprovider.view;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kvashyna.contactsprovider.R;
import com.kvashyna.contactsprovider.adapter.RecycleAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    @ViewById
    RecyclerView contacts;
    @Bean
    RecycleAdapter mCursorAdapter;

    @AfterViews
    void initContacts() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            loadContacts();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadContacts();
                } else {
                    showAlert(R.string.permission_denied);
                }
            }
        }
    }

    /**
     * Start loader
     */
    private void loadContacts() {
        getLoaderManager().initLoader(0, null, MainActivity.this).forceLoad();
    }

    private void showAlert(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(android.R.string.dialog_alert_title);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                initContacts();// request permission again
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void initRecycleView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        contacts.setHasFixedSize(true);
        contacts.setLayoutManager(mLayoutManager);
    }

    /**
     * CursorLoader for retrieving contacts list
     * @param i
     * @param bundle
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                this,
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        initRecycleView();
        mCursorAdapter.swapCursor(cursor);
        contacts.setAdapter(mCursorAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}
