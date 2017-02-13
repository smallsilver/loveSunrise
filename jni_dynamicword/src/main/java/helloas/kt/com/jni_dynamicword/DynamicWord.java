package helloas.kt.com.jni_dynamicword;

/**
 * Created by Administrator on 2016/5/17.
 */
public class DynamicWord {
    /**
     * 产生一个新的动态密码<br>
     * @param key 服务器的KEY值
     * @param letters 服务器获取的数字表
     * @param now 当前时间
     * @param dxTime 差值
     * @param c2sTime 客户端与服务器时间差值
     * @return 却说态密码
     */
    public native String createWord(String key, String letters, long now, long dxTime, long c2sTime);

    /**
     * 获取下一个动态密码的时间
     * @param now 当前时间
     * @param dxTime 差值
     * @param c2sTime 客户端与服务器时间差值
     * @return 下一次动态密码产生的时间
     */
    public native int nextWordLeaveInterval(long now, long dxTime, long c2sTime);

    /**
     * 加密数据
     * @param params 加密前的字符串
     * @return  加密后的字符串
     */
    public native String encryptData(String params);

    /**
     * 解密字符串
     * @param params 解密前的字符串
     * @return  解密后的字符串
     */
    public native String decryptData(String params);
}
