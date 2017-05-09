package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.model.AddressBean;
import com.miuhouse.yourcompany.student.model.AddressListBean;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.presenter.AddressPresenter;
import com.miuhouse.yourcompany.student.presenter.UpdateAddressPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IAddressPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IUpdateAddPresenter;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IAddressView;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IUpdateAddressView;
import com.miuhouse.yourcompany.student.view.ui.adapter.AddressAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址管理 Created by kings on 8/3/2016.
 */
public class AddressActivity extends BaseActivity
    implements IAddressView, IUpdateAddressView, AddressAdapter.OnClickDefaultAddressListener,
    AddressAdapter.OnDelAddressListener {

  private static final int REQUEST = 1;
  private IAddressPresenter addressPresenter;

  private TextView tvDefaultAddress;
  private LinearLayout linearContent;

  /**
   * list 和 addressList 的区别是list保存的是地址字符串，addressList保存的是地址对象
   */
  private List<AddressBean> addressList = new ArrayList<>();
  private AddressAdapter adapter;
  private IUpdateAddPresenter updateAddPresenter;

  /**
   * 被删除的position
   */
  private int delPosition;

  /**
   * 在列表中选择默认上课地址index
   */
  private int position;

  @Override
  protected String setTitle() {
    return "地址管理";
  }

  @Override
  protected String setRight() {
    return "添加地址";
  }

  @Override
  protected void initViewAndEvents() {
    ListView addressListView = (ListView) findViewById(R.id.lv_address);
    View view = this.getLayoutInflater().inflate(R.layout.list_head_layout, null);
    tvDefaultAddress = (TextView) view.findViewById(R.id.tv_default_address);
    linearContent = (LinearLayout) findViewById(R.id.linear_content);
    addressListView.addHeaderView(view);

    addressPresenter = new AddressPresenter(this);
    updateAddPresenter = new UpdateAddressPresenter(this);

    adapter = new AddressAdapter(this);
    adapter.setOnDefaultAddressClickListener(this);
    adapter.setOnDelAddressListener(this);
    addressListView.setAdapter(adapter);
  }

  @Override
  protected int getContentLayoutId() {
    return R.layout.activity_address;
  }

  @Override
  protected View getOverrideParentView() {
    return linearContent;
  }

  @Override
  public void onRightClick() {
    startActivityForResult(new Intent(this, ChooseAddressActivity.class), REQUEST);
  }

  /**
   * 请求地址列表,并且viewOverrideManager已经实例化了吗，
   */
  @Override
  public void request() {
    addressPresenter.getAddressList();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (resultCode == RESULT_OK && requestCode == REQUEST) {
      addressPresenter.getAddressList();
    }
  }

  /**
   * 返回地址列表
   */
  @Override
  public void result(AddressListBean addressListBean) {
    addressList.clear();
    addressList.addAll(addressListBean.getAddressList());
    for (int i = 0; i < addressListBean.getAddressList().size(); i++) {
      AddressBean addressBean = addressListBean.getAddressList().get(i);

      //给默认地址TextView赋值
      if (addressBean.getIsDefault().equals("1")) {
        tvDefaultAddress.setText(getDefaultAddress(addressBean));
      }
    }
    adapter.setData(addressList);
    //假如没有设置默认的接单地址，给一个提示
    if (Util.isEmpty(tvDefaultAddress.getText().toString())) {
      //            tvDefaultAddress.setHint(getResources().getText(R.string.default_address));
    }
  }

  private String getDefaultAddress(AddressBean addressBean) {

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

  /**
   * 修改默认地址成功的返回函数
   */
  @Override
  public void UpdateSuccess(BaseBean baseBean) {
    if (baseBean.getCode() == 0) {

      if (position != adapter.getOldDefaultPosition()) {
        for (int i = 0; i < addressList.size(); i++) {
          if (i == position) {
            addressList.get(position).setIsDefault("1");
            tvDefaultAddress.setText(getDefaultAddress(addressList.get(position)));
          }
          if (i == adapter.getOldDefaultPosition()) {
            addressList.get(adapter.getOldDefaultPosition()).setIsDefault("0");
          }
        }
      }
      adapter.notifyDataSetChanged();
    }
  }

  /**
   * 删除地址成功返回函数
   */
  @Override
  public void delAddressSuccess(BaseBean baseBean) {
    if (baseBean.getCode() == 0) {
      if (adapter.getItem(delPosition).getIsDefault().equals("1")) {
        tvDefaultAddress.setText("");
      }
      addressList.remove(delPosition);
      adapter.notifyDataSetChanged();
    }

    Toast.makeText(this, baseBean.getMsg(), Toast.LENGTH_LONG).show();
  }

  @Override
  public void showError(int type) {
    if (type == ViewOverrideManager.NO_ADDRESS) {
      viewOverrideManager.showLoading(type, null);
    }
  }

  @Override
  public void showLoading(String msg) {
    if (viewOverrideManager == null) {
      viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
    }
    viewOverrideManager.showLoading(msg, true);
  }

  @Override
  public void onDefaultClick(int position) {
    this.position = position;
    AddressBean addressBean = adapter.getItem(position);
    addressBean.setIsDefault("1");
    updateAddPresenter.updateAddress(addressBean);
  }

  @Override
  public void onDelAddressClick(int position) {
    Log.i("TAG", "position=" + position);
    this.delPosition = position;
    updateAddPresenter.delAddress(adapter.getItem(position).getId());
  }

  @Override
  public void onBackClick() {
    //        super.onBackClick();
    if (getIntent().getIntExtra("tag", 0) == 1) {
      startActivity(new Intent(this, MainActivity.class));
      finish();
    } else {
      super.onBackClick();
    }
  }
}
