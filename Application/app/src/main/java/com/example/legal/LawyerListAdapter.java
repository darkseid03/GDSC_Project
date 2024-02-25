package com.example.legal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LawyerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Lawyer> lawyersList;

    public LawyerListAdapter(Context context, ArrayList<Lawyer> lawyersList) {
        this.context = context;
        this.lawyersList = lawyersList;
    }

    @Override
    public int getCount() {
        return lawyersList.size();
    }

    @Override
    public Object getItem(int position) {
        return lawyersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_lawyer, null);
        }

        TextView nameTextView = convertView.findViewById(R.id.lawyerNameTextView);
        TextView moneyTextView = convertView.findViewById(R.id.lawyerMoneyTextView);
        TextView address= convertView.findViewById(R.id.AddTextView);
        TextView type = convertView.findViewById(R.id.TypeTextView);


        ImageView lawyerPhotoImageView = convertView.findViewById(R.id.lawyerPhotoImageView);


        Lawyer lawyer = lawyersList.get(position);
        if (lawyer != null) {
            nameTextView.setText(lawyer.getName());
            moneyTextView.setText(lawyer.getMoney());
            address.setText(lawyer.getOfficeAddress());
            type.setText(lawyer.getLawyerType());
            String photoUrl = lawyer.getPhotoUrl();
            if (photoUrl != null && !photoUrl.isEmpty()) {
                Picasso.get().load(photoUrl).placeholder(R.drawable.avtar_png).into(lawyerPhotoImageView);
            } else {

                lawyerPhotoImageView.setImageResource(R.drawable.avtar_png);
            }

        }

        return convertView;
    }
}
