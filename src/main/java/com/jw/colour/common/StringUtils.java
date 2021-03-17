package com.jw.colour.common;

import java.security.SecureRandom;
import java.util.Date;

/**
 * @author jw on 2021/3/3
 */
public class StringUtils {

    /**
    * 判断是否为空
    * @author jw
    * @date 2021/3/17
    * @param str str
    * @return: boolean
    **/
    public static boolean isBlank(String str) {
        return str != null && !str.equals("");
    }

    /**
    * 获取随机手机号
    * @author jw
    * @date 2021/3/17
    * @return: java.lang.String
    **/
    public static String getPhoneNum() {
        //给予真实的初始号段，号段是在百度上面查找的真实号段
        String[] start = {"133", "149", "153", "173", "177",
                "180", "181", "189", "199", "130", "131", "132",
                "145", "155", "156", "166", "171", "175", "176", "185", "186", "166", "134", "135",
                "136", "137", "138", "139", "147", "150", "151", "152", "157", "158", "159", "172",
                "178", "182", "183", "184", "187", "188", "198", "170", "171"};
        StringBuilder phoneNum = new StringBuilder();
        //随机出真实号段   使用数组的length属性，获得数组长度，
        //通过Math.random（）*数组长度获得数组下标，从而随机出前三位的号段
        SecureRandom random = new SecureRandom();
        phoneNum.append(start[random.nextInt(start.length)]);
        //随机出剩下的8位数
        //循环剩下的位数
        for (int i = 0; i < 8; i++) {
            //每次循环都从0~9挑选一个随机数
            phoneNum.append(random.nextInt(10));
        }
        return phoneNum.toString();
    }



}
