package kr.co.mythings.shackit.core.common;

public interface ApiStatusCode {
    String OK = "0000";
    String OK_NODATA = "0001";

    String BAD_REQUEST = "1001";
    String UNAUTHORIZED_REQUEST = "1002";
    String INTERNAL_SERVER_ERROR = "1003";
    String NOT_FOUND = "1004";
    String OUT_OF_LENGTH_REQUEST = "1005";

    String INVALID_TOKEN = "2001";
    String INVALID_IDX = "2002";
    String INVALID_EMAIL = "2003";
    String INVALID_PASSWORD = "2004";
    String DUPLICATE_EMAIL = "2005";

    String DUPLICATE_NAME_AREA = "3001";
    String DUPLICATE_NAME_DEVICE = "3002";

    String RULE_IS_NOT_EXIST = "4001";
    String ALREADY_RUN_AUTOMODE = "4002";

    String BOARD_SERIAL_IS_NOT_REGISTED = "5001";

    String UNIQUE_ID_REGIST_WAITTING = "6001";
}
