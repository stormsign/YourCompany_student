package com.miuhouse.yourcompany.student.view.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.model.AddressBean;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.presenter.UpdateAddressPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IUpdateAddPresenter;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IUpdateAddressView;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;

/**
 * Created by kings on 8/3/2016.
 */
public class ChooseAddressActivity extends BaseActivity implements IUpdateAddressView {

    private static final int REQUEST = 1;

    private TextView tvAddress;
    private CheckBox checkboxIsDefault;
    private EditText edtHouseNumber;
    private ProgressDialog progDialog = null;// 搜索时进度条

    private AddressBean addressBean;

    private IUpdateAddPresenter updateAddPresenter;

    @Override
    protected String setTitle() {
        return "上课地址";
    }

    @Override
    protected String setRight() {
        return "保存";
    }

    @Override
    protected void initViewAndEvents() {
        if (getIntent() != null) {
            addressBean = (AddressBean) getIntent().getSerializableExtra("address");
        }
        tvAddress = (TextView) findViewById(R.id.tv_address);
        checkboxIsDefault = (CheckBox) findViewById(R.id.checkbox_isDefault);
        edtHouseNumber = (EditText) findViewById(R.id.edt_houseNumber);

        findViewById(R.id.linear_choose_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ChooseAddressActivity.this, MapActivity.class), REQUEST);
            }
        });
        updateAddPresenter = new UpdateAddressPresenter(this);
        if (addressBean != null) {
            initdata();
        }
    }

    private void initdata() {
        tvAddress.setText(detailAddress(addressBean));
        edtHouseNumber.setText(addressBean.getHouseNumber());
        if (addressBean.getIsDefault().equals("1")) {
            checkboxIsDefault.setChecked(true);
        } else {
            checkboxIsDefault.setChecked(false);
        }
    }

    //    private void
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_choose_address;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST) {
            String city = data.getStringExtra("city");
            String province = data.getStringExtra("province");
            String street = data.getStringExtra("street");
            String district = data.getStringExtra("district");
            double lon = data.getDoubleExtra("lon", 0);
            double lat = data.getDoubleExtra("lat", 0);
            L.i("TAG", "从地图界面返回的数据=" + city + "省=" + province + "区" + district + "街道=" + street + "lon=" + lon + "lat=" + lat);

            addressBean = new AddressBean();
            addressBean.setProvince(province);
            addressBean.setCity(city);
            addressBean.setDistrict(district);
            addressBean.setLat(lat);
            addressBean.setLon(lon);
            addressBean.setStreet(street);

            tvAddress.setText(detailAddress(addressBean));

        }
    }

    private String detailAddress(AddressBean addressBean) {

        StringBuffer address = new StringBuffer();
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
        return address.toString();
    }

    @Override
    public void onRightClick() {
        if (addressBean != null) {
            if (checkboxIsDefault.isChecked()) {
                addressBean.setIsDefault("1");
            } else {
                addressBean.setIsDefault("0");
            }
            addressBean.setHouseNumber(edtHouseNumber.getText().toString());
            showProgressDialog();
            updateAddPresenter.updateAddress(addressBean);
        }
    }

    @Override
    public void UpdateSuccess(BaseBean baseBean) {
        dissmissProgressDialog();
        if (baseBean.getCode() == 0) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void delAddressSuccess(BaseBean baseBean) {

    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this, R.style.LodingDialog);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("保存中...");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public void hideError() {
        Toast.makeText(this, getResources().getText(R.string.exception_no_network), Toast.LENGTH_LONG).show();
    }
}
