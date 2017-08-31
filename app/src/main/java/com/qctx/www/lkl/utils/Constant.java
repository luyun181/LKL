package com.qctx.www.lkl.utils;

/**
 *
 */
public interface Constant {
    /**
     * 二维码请求的type
     */
    public static final String REQUEST_SCAN_TYPE="type";
    /**
     * 普通类型，扫完即关闭
     */
    public static final int REQUEST_SCAN_TYPE_COMMON=0;
    /**
     * 服务商登记类型，扫描
     */
    public static final int REQUEST_SCAN_TYPE_REGIST=1;


    /**
     * 扫描类型
     * 条形码或者二维码：REQUEST_SCAN_MODE_ALL_MODE
     * 条形码： REQUEST_SCAN_MODE_BARCODE_MODE
     * 二维码：REQUEST_SCAN_MODE_QRCODE_MODE
     *
     */
    public static final String REQUEST_SCAN_MODE="ScanMode";
    /**
     * 条形码： REQUEST_SCAN_MODE_BARCODE_MODE
     */
    public static final int REQUEST_SCAN_MODE_BARCODE_MODE = 0X100;
    /**
     * 二维码：REQUEST_SCAN_MODE_ALL_MODE
     */
    public static final int REQUEST_SCAN_MODE_QRCODE_MODE = 0X200;
    /**
     * 条形码或者二维码：REQUEST_SCAN_MODE_ALL_MODE
     */
    public static final int REQUEST_SCAN_MODE_ALL_MODE = 0X300;

    /*
 * web service
 */
//	// name space
//	public static final String NAME_SPACE = "http://sharetime.com/lawyer/service/PoliceServiceSvc";
//	// param name space
    public static final String PARAM_NAME_SPACE = "http://oup.sharetime.com";
//	// end point

    public static final String SYSTEM = "";

    public static final String SERVICE_URL = "http://120.27.19.71:8089/server/services/baUserSvc";

    public static final String CHECK_USER_METHOD = "userCheckIn";
    public static final String ERROR = "暂无数据";

}
