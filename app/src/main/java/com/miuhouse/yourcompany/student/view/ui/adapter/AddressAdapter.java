package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.model.AddressBean;
import com.miuhouse.yourcompany.student.model.Folder;
import com.miuhouse.yourcompany.student.presenter.interf.IUpdateAddPresenter;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.view.ui.activity.ChooseAddressActivity;
import com.miuhouse.yourcompany.student.view.widget.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 9/19/2016.
 */
public class AddressAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<AddressBean> addressList = new ArrayList<>();
    private Context mContext;
    private int oldDefaultPosition = -1;

    public AddressAdapter(Context mContext) {
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = mContext;
    }

    /**
     * 设置数据集
     *
     * @param addressList
     */
    public void setData(List<AddressBean> addressList) {
        if (addressList != null && addressList.size() > 0) {
            this.addressList = addressList;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public AddressBean getItem(int position) {
        return addressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_list_address, parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AddressBean addressBean = addressList.get(position);

        viewHolder.tvAddress.setText(getAddress(addressBean));
        if (addressBean.getIsDefault().equals("1")) {
            oldDefaultPosition = position;
            viewHolder.imgDefault.setBackgroundResource(R.mipmap.combined_smell);
        } else {
            viewHolder.imgDefault.setBackgroundResource(R.mipmap.combined);
        }
        viewHolder.btnDelete.setOnClickListener(deleteAddressListener(position));
        viewHolder.btnEdit.setOnClickListener(editAddressListener(position));
        viewHolder.imgDefault.setOnClickListener(setDefaultAddress(position));
        return convertView;
    }

    /**
     * 详细地址由省，市，街道，门牌号组成
     *
     * @param addressBean
     * @return
     */
    private String getAddress(AddressBean addressBean) {

        StringBuilder address = new StringBuilder();
        if (addressBean.getProvince() != null) {
            address.append(addressBean.getProvince());
        }
        if (addressBean.getCity() != null) {
            address.append(addressBean.getCity());
        }
        if (addressBean.getDistrict() != null) {
            address.append(addressBean.getDistrict());
        }
        if (addressBean.getStreet() != null) {
            address.append(addressBean.getStreet());
        }
        if (addressBean.getHouseNumber() != null) {
            address.append(addressBean.getHouseNumber());
        }
        return address.toString();
    }

    private View.OnClickListener deleteAddressListener(final int position) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelDialog(position);
            }
        };
    }

    public View.OnClickListener editAddressListener(final int position) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChooseAddressActivity.class);
                intent.putExtra("address", addressList.get(position));
                mContext.startActivity(intent);
            }
        };
    }

    public void showDelDialog(final int position) {
        L.i("TAG", "position=" + position);
        new LovelyStandardDialog(mContext)
                .setButtonsColorRes(R.color.themeColor)
                .setMessage("确定删除这条地址吗？").goneView()
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (delAddressListener != null) {
                            delAddressListener.onDelAddressClick(position);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public View.OnClickListener setDefaultAddress(final int position) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (defaultAddressListener != null) {
                    defaultAddressListener.onDefaultClick(position);
                }
            }
        };

    }

    class ViewHolder {
        private TextView tvAddress;
        private Button btnDelete;
        private Button btnEdit;
        private ImageView imgDefault;

        ViewHolder(View view) {
            tvAddress = (TextView) view.findViewById(R.id.tv_address);
            btnDelete = (Button) view.findViewById(R.id.btn_delete);
            btnEdit = (Button) view.findViewById(R.id.btn_edit);
            imgDefault = (ImageView) view.findViewById(R.id.img_default);
            view.setTag(this);
        }
    }

    public int getOldDefaultPosition() {
        return oldDefaultPosition;
    }

    /**
     * 默认上课地址的监听接口
     */
    private OnClickDefaultAddressListener defaultAddressListener;

    /**
     * 删除地址监听接口
     */
    private OnDelAddressListener delAddressListener;


    public void setOnDefaultAddressClickListener(OnClickDefaultAddressListener listener) {
        defaultAddressListener = listener;
    }

    public void setOnDelAddressListener(OnDelAddressListener listener) {
        delAddressListener = listener;
    }

    public interface OnClickDefaultAddressListener {
        void onDefaultClick(int position);
    }

    ;

    public interface OnDelAddressListener {
        void onDelAddressClick(int position);
    }
}
