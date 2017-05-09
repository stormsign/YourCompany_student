package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.utils.Util;

import java.util.List;

/**
 * Created by kings on 7/12/2016.
 */
public class ChoiceAdapter extends ArrayAdapter<String> {
    private String selectedStr;

    public ChoiceAdapter(Context context, List<String> list, String selectedStr) {
        super(context, 0, list);
        this.selectedStr = selectedStr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_choice_genders, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvDescription.setText(getItem(position));
        if (!Util.isEmpty(selectedStr)) {
            if (selectedStr.equals(getItem(position))) {
                vh.radioButton.setChecked(true);
            } else {
                vh.radioButton.setChecked(false);
            }
        }

        return convertView;
    }

    private static final class ViewHolder {
        TextView tvDescription;
        RadioButton radioButton;

        public ViewHolder(View view) {
            tvDescription = (TextView) view.findViewById(R.id.tv_description);
            radioButton = (RadioButton) view.findViewById(R.id.radio_genders);
        }

        ;
    }

}
