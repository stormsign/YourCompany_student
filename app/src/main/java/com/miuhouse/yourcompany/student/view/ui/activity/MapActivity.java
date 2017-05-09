package com.miuhouse.yourcompany.student.view.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题：搜索获取的地址district属性包含省市区，而定位获取的地址是分省，市，区属性的,
 * 所以当使用搜素获取的地址返回district加街道信息，定位获取的地址返回省，市，区加街道信息
 * Created by kings on 8/3/2016.
 */
public class MapActivity extends BaseActivity implements TextWatcher,
        PoiSearch.OnPoiSearchListener, Inputtips.InputtipsListener, LocationSource, AMapLocationListener, AMap.OnMapClickListener {

    private AMap aMap;
    private AutoCompleteTextView searchText;// 输入搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条

    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private String city;
    private String locationCity;
    private String province;
    private String district;
    private String street;
    private String houseNumber;
    private double lon;
    private double lat;
    private List<String> listString;
    private List<Tip> tipLists = new ArrayList<>();
    //判断是搜到获取的地址还是定位获取的地址
//    private boolean isSearchAddress = false;
    private List<Marker> markers = new ArrayList<>();
    private GeocodeSearch geocoderSearch;
    private MapView map;
    private Bundle bundle;

    @Override
    protected String setTitle() {
        return "选择地址";
    }

    @Override
    protected String setRight() {
        return "保存";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        map = (MapView) findViewById(R.id.map);
        map.onCreate(bundle);
        aMap = this.map.getMap();
        setUpMap();

    }

    @Override
    protected void initViewAndEvents() {
        init();
    }

    /**
     * 设置页面监听
     */
    private void setUpMap() {

        searchText = (AutoCompleteTextView) findViewById(R.id.keyWord);
        searchText.addTextChangedListener(this);// 添加文本输入框监听事件

        aMap.setOnMapClickListener(this);

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));

        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                street = tipLists.get(position).getAddress() + tipLists.get(position).getName();
                lat = tipLists.get(position).getPoint().getLatitude();
                lon = tipLists.get(position).getPoint().getLongitude();
                L.i("TAG", "lat=" + lat + "lon=" + lon + "street=" + street + "address" + tipLists.get(position).getAddress() + "name" + tipLists.get(position).getName());
                district = tipLists.get(position).getDistrict();
                Log.i("TAG", "tipListe=" + district);
                city = null;
                province = null;
            }
        });
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

//    /**
//     * 开始进行poi搜索
//     */
//    protected void doSearchQuery() {
//        showProgressDialog();// 显示进度框
//        city = city != null ? city : "深圳";
//        currentPage = 0;
//        query = new PoiSearch.Query(keyWord, "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
//        query.setPageSize(10);// 设置每页最多返回多少条poiitem
//        query.setPageNum(currentPage);// 设置查第一页
//        query.setCityLimit(true);
//        poiSearch = new PoiSearch(this, query);
//        poiSearch.setOnPoiSearchListener(this);
//        poiSearch.searchPOIAsyn();
//    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }


    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        L.i("TAG", "tipList=" + tipList.size());
        if (rCode == 1000) {// 正确返回
            listString = new ArrayList<String>();
            tipLists.clear();
            tipLists.addAll(tipList);
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.item_address, listString);
            searchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();

        } else {
//            ToastUtil.showerror(PoiKeywordSearchActivity.this, rCode);
        }


//    }
    }


    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框

        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    PoiResult poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    L.i("TAG", "poiItems=" + poiItems.get(0).getLatLonPoint().getLatitude());
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
//                        ToastUtil.show(PoiKeywordSearchActivity.this,
//                                R.string.no_result);
                    }
                }
            } else {
//                ToastUtil.show(PoiKeywordSearchActivity.this,
//                        R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(PoiKeywordSearchActivity.this, rCode);
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!IsEmptyOrNullString(newText)) {
            locationCity = locationCity != null ? locationCity : "深圳";
            InputtipsQuery inputquery = new InputtipsQuery(newText, locationCity);

            Inputtips inputTips = new Inputtips(MapActivity.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    public boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 地位
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {

//                isSearchAddress = false;

                mListener.onLocationChanged(aMapLocation); //
                city = aMapLocation.getCity();
                locationCity = aMapLocation.getCityCode();
                province = aMapLocation.getProvince();
                district = aMapLocation.getDistrict();
                street = aMapLocation.getStreet() + aMapLocation.getAoiName();
                lon = aMapLocation.getLongitude();
                lat = aMapLocation.getLatitude();
                String address = aMapLocation.getAddress();
                houseNumber = aMapLocation.getStreetNum();
                searchText.setHint(province + city + district + street);
                Log.i("TAG", "address=" + city + "province=" + province + "district=" + district + "street=" + street + "address=" + address + "houseNumber=" + houseNumber + "poiName=" + aMapLocation.getPoiName() + "aoiName" + aMapLocation.getAoiName());
                deactivate();


                LatLng latLng = new LatLng(aMapLocation.getLatitude(),
                        aMapLocation.getLongitude());
                CameraPosition currentCameraPosition = aMap.getCameraPosition();
                float zoom = currentCameraPosition.zoom;
                zoom = 20;
                CameraPosition cameraPosition = new CameraPosition(latLng, zoom, 0, 0);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                aMap.animateCamera(cameraUpdate, 500, new AMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
//                        searchAddress();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                L.i("TAG", "errText=" + errText);
                Toast.makeText(this, errText, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();

            mlocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onRightClick() {
        if (lon == 0 || lat == 0) {
            Toast.makeText(this, "获取地址失败", Toast.LENGTH_LONG).show();
            return;
        }
//        if (!isSearchAddress) {
        Intent intent = new Intent();
        L.i("TAG", "定位获取的数据");
        intent.putExtra("city", city);
        intent.putExtra("province", province);
        intent.putExtra("district", district);
        intent.putExtra("street", street);
        intent.putExtra("lon", lon);
        intent.putExtra("lat", lat);

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        lon = latLng.longitude;
        lat = latLng.latitude;
        for (Marker markerInfo : markers) {
            markerInfo.remove();
        }
        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng));
        markers.add(marker);

        initGeocoder(lat, lon);


    }

    /**
     * 根据经纬度获取详细地址信息
     *
     * @param lat
     * @param lon
     */
    private void initGeocoder(final double lat, final double lon) {
        if (geocoderSearch == null) {
            geocoderSearch = new GeocodeSearch(this);
        }
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

                String address = getDetailAddress(regeocodeResult);
                searchText.setText(address);

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });

        LatLonPoint latLonPoint = new LatLonPoint(lat, lon);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 100, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    private void save(RegeocodeResult regeocodeResult, double lon, double lat) {
        getDetailAddress(regeocodeResult);
        Intent intent = new Intent();
        intent.putExtra("city", city);
        intent.putExtra("province", province);
        intent.putExtra("houseNumber", houseNumber);
        intent.putExtra("district", district);
        intent.putExtra("street", street);
        intent.putExtra("lon", lon);
        intent.putExtra("lat", lat);
        setResult(RESULT_OK, intent);
        finish();
    }

    private String getDetailAddress(RegeocodeResult regeocodeResult) {
        dissmissProgressDialog();
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        city = regeocodeAddress.getCity();
        province = regeocodeAddress.getProvince();
        district = regeocodeAddress.getDistrict();
        street = regeocodeAddress.getStreetNumber().getStreet();
        StringBuffer strBuffer = new StringBuffer();
        L.i("TAG", "根据经纬度获取的详细地址=" + city + "省=" + province + "区" + district + "街道=" + street);

        return strBuffer.append(province).append(city).append(district).append(street).toString();
    }

    private void addressShowText() {

    }
}
