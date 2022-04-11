package com.tkn.cloud.mall.practice.common.util;

import cn.hutool.crypto.digest.MD5;
import com.tkn.cloud.mall.practice.common.common.Constant;

/**
 * @description: md5工具类
 * @author: lz
 * @time: 2022/3/2 17:37
 */
public class MD5Utils {
    /**
     *  md5加密
     * @param passWord
     * @return
     */
    public static String getMD5Str(String passWord,String icode) {
        return MD5.create().digestHex(passWord+ Constant.SALT+icode);
    }

}
