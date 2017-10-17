package cn.fjlcx.android.imitatewb.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * @author smlcx
 * @version 创建时间：2016-11-30 下午10:54:51
 * 
 */
public class SPUtils {  
    private static final String ShareName = "shareFile";

	/** 
     * 向SharedPreferences中写入int类型数据 
     * @param context 上下文
     * @param key 键
     * @param value 值
     */  
    public static void putValue(Context context,String key,
								int value) {
        Editor sp = getEditor(context);
        sp.putInt(key, value);  
        sp.commit();  
    }  
      
    /** 
     * 向SharedPreferences中写入boolean类型的数据
     *  
     * @param context 上下文
     * @param key 键
     * @param value 值
     */  
    public static void putValue(Context context, String key,
								boolean value) {
        Editor sp = getEditor(context);
        sp.putBoolean(key, value);  
        sp.commit();  
    }  
      
    /** 
     * 向SharedPreferences中写入String类型的数据
     *  
     * @param context 上下文
     * @param key 键
     * @param value 值
     */  
    public static void putValue(Context context, String key,
								String value) {
        Editor sp = getEditor(context);
        sp.putString(key, value);  
        sp.commit();  
    }  
      
    /** 
     * 向SharedPreferences中写入float类型的数据
     *  
     * @param context 上下文
     * @param key 键
     * @param value 值
     */  
    public static void putValue(Context context, String key,
								float value) {
        Editor sp = getEditor(context);
        sp.putFloat(key, value);  
        sp.commit();  
    }  
  
    /** 
     * 向SharedPreferences中写入long类型的数据
     *  
     * @param context 上下文
     * @param key 键
     * @param value 值
     */  
    public static void putValue(Context context, String key,
								long value) {
        Editor sp = getEditor(context);
        sp.putLong(key, value);  
        sp.commit();  
    }  
      
    /** 
     * 从SharedPreferences中读取int类型的数据
     *  
     * @param context 上下文
     * @param key 键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的int
     */  
    public static int getValue(Context context, String key,
							   int defValue) {
        SharedPreferences sp = getSharedPreferences(context);
        int value = sp.getInt(key, defValue);  
        return value;  
    }  
      
    /** 
     * 从SharedPreferences中读取boolean类型的数
     *
     * @param context 上下文环
     * @param key
     * @param defValue 如果读取不成功则使用默认
     * @return 返回读取的�?? 
     */  
    public static boolean getValue(Context context, String key,
								   boolean defValue) {
        SharedPreferences sp = getSharedPreferences(context);
        boolean value = sp.getBoolean(key, defValue);  
        return value;  
    }  
      
    /** 
     * 从SharedPreferences中读取String类型的数
     *  
     * @param context 上下文环
     * @param key
     * @param defValue 如果读取不成功则使用默认
     * @return 返回读取的
     */  
    public static String getValue(Context context,  String key,
								  String defValue) {
        SharedPreferences sp = getSharedPreferences(context);
        String value = sp.getString(key, defValue);
        return value;  
    }  
      
    /** 
     * 从SharedPreferences中读取float类型的数
     *  
     * @param context 上下文
     * @param key
     * @param defValue 如果读取不成功则使用默认
     * @return 返回读取的
     */  
    public static float getValue(Context context, String key,
								 float defValue) {
        SharedPreferences sp = getSharedPreferences(context);
        float value = sp.getFloat(key, defValue);  
        return value;  
    }  
      
    /** 
     * 从SharedPreferences中读取long类型的数
     *  
     * @param context 上下文环
     * @param key
     * @param defValue 如果读取不成功则使用默认
     * @return 返回读取的
     */  
    public static long getValue(Context context,  String key,
								long defValue) {
        SharedPreferences sp = getSharedPreferences(context);
        long value = sp.getLong(key, defValue);  
        return value;  
    }

    /**
     * 针对复杂类型存储<对象>
     *
     * @param key
     * @param object
     */
    public static void setObject(Context context,String key, Object object) {
        SharedPreferences sp = getSharedPreferences(context);

        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //创建字节对象输出流
        ObjectOutputStream out = null;
        try {
            //然后通过将字对象进行64转码，写入key值为key的sp中
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            Editor editor = sp.edit();
            editor.putString(key, objectVal);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObject(Context context,String key, Class<T> clazz) {
        SharedPreferences sp = getSharedPreferences(context);
        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            //一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //获取Editor实例  
    private static Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }  
  
    //获取SharedPreferences实例  
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(ShareName, Context.MODE_PRIVATE);
    }  
    
    
}  
