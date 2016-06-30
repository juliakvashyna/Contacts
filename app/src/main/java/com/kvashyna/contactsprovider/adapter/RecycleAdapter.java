package com.kvashyna.contactsprovider.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kvashyna.contactsprovider.R;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by bigdrop on 6/22/16.
 */
@EBean
public class RecycleAdapter extends RecyclerViewCursorAdapter<RecycleAdapter.ViewHolder> {
    @RootContext
    Context mContext;

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        final String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        holder.mTextView.setText(name);
        final String photo = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
        if (photo != null) {
            Uri photoUri = Uri.parse(photo);
            holder.mImageView.setImageURI(photoUri);
        } else {
            holder.mImageView.setImageDrawable(null);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.contact_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    //TODO bind
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(View convertView) {
            super(convertView);
            mTextView = (TextView) convertView.findViewById(R.id.name);
            mImageView = (ImageView) convertView.findViewById(R.id.icon);
        }
    }
}
