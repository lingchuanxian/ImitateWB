package cn.fjlcx.android.imitatewb.utils;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * 重命名文件
	 * 
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static String renameFileName(String path, String fileName) {
		File file = new File(path +"/"+ fileName);// 获取要报存的文件路径
		System.out.println(StringUtil.getFileType(fileName));
		if (file.exists()
				&& (StringUtil.getFileType(fileName).equals("jpg")
						|| StringUtil.getFileType(fileName).equals("jpeg")
						|| StringUtil.getFileType(fileName).equals("png") || StringUtil
						.getFileType(fileName).equals("bmp")))// 如果文件已存在就重命名，不存在就直接返回
		{
			return System.currentTimeMillis()+"."+StringUtil.getFileType(fileName) ;
		}
		return fileName;
	}

	/**
	 * 判断字符串是否非空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNullOrEmpty(String s) {
		if (null == s || "".equals(s)) {
			return true;
		}
		return false;
	}

	/**
	 * 根据文件名获得文件类型
	 * 
	 * @param s
	 * @return
	 */
	public static String getFileType(String s) {
		return s.substring(s.lastIndexOf(".") + 1);
	}

	public static String getFileName(String s) {
		return s.substring(0, s.lastIndexOf("."));
	}
	
	
	public static boolean isNumber(String str){
		boolean flag = false;
		//判断正负数都可以
		//Pattern pattern = Pattern.compile("^[-]{0,1}[0-9]+$");
		Pattern pattern = Pattern.compile("^[1-9][0-9]*$");
		Matcher isNum = pattern.matcher(str);
		if(isNum.matches()){
			flag = true;
		}
		return flag;
	}
	
	public static String getPropValue(String key){
		// action配置文件路径  
		@SuppressWarnings("unused")
		String config_path = "WEB-INF/classes/config.properties";
		// 属性文件   
		Properties prop = new Properties();
		// 把文件读入文件输入流，存入内存中     
		InputStream fis;
		try {
			//fis = new FileInputStream(new File(path + config_path));
			fis =StringUtil.class.getClassLoader().getResourceAsStream("config.properties");
			//加载文件流的属性     
			prop.load(fis); 
			return prop.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}     
	}
	
	public static String getNewString(String str) throws UnsupportedEncodingException
    {
       return new String(str.getBytes("ISO-8859-1"),"UTF-8");
    }

	/**
	 * 定义script的正则表达式
	 */
	private static final String REGEX_SCRIPT = "<script[^>]*?>[\\s\\S]*?<\\/script>";
	/**
	 * 定义style的正则表达式
	 */
	private static final String REGEX_STYLE = "<style[^>]*?>[\\s\\S]*?<\\/style>";
	/**
	 * 定义HTML标签的正则表达式
	 */
	private static final String REGEX_HTML = "<[^>]+>";
	/**
	 * 定义空格回车换行符
	 */
	private static final String REGEX_SPACE = "\\s*|\t|\r|\n|nbsp;";
	public static String delHTMLTag(String htmlStr) {
		// 过滤script标签
		Pattern p_script = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll("");
		// 过滤style标签
		Pattern p_style = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll("");
		// 过滤html标签
		Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll("");
		// 过滤空格回车标签
		Pattern p_space = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll("");
		return htmlStr.trim(); // 返回文本字符串
	}

	/**
	 * 替换当前Html格式的换行符和空格符
	 */
	private static final String SPACE_N="\u0026nbsp;";
	private static final String TAB_N="\u003cbr\u003e";
	public static String replaceSpaceAndTab(String html){

		html=html.replaceAll(SPACE_N," ");
		html=html.replaceAll(TAB_N,"\r\n");
		return html;
	}

	/**
	 * 获取当前的年份
	 */
	public static int getYear(){
		Calendar a=Calendar.getInstance();
		return a.get(Calendar.YEAR);
	}

	/**
	 * 获取当前的时间
	 */
	public static String  getCurrentdate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		return sdf.format(date);
	}

	/**
	 * 获取数据大小
	 * @param var0
	 * @return
	 */
	public static String getDataSize(long var0) {
		DecimalFormat var2 = new DecimalFormat("###.00");
		return var0 < 1024L ? var0 + "B" : (var0 < 1048576L ? var2.format((double) ((float) var0 / 1024.0F))
				+ "KB" : (var0 < 1073741824L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F))
				+ "MB" : (var0 < 0L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F / 1024.0F))
				+ "GB" : "error")));
	}

	public static String getDate(String dateString){
		if(StringUtil.isNullOrEmpty(dateString)){
			return "";
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(new Date(Long.parseLong(dateString.replace("/Date(","").replace(")/",""))));
		}
	}
	public static String getDateformatter(String dateString){
		if(StringUtil.isNullOrEmpty(dateString)){
			return "";
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return sdf.format(new Date(Long.parseLong(dateString.replace("/Date(", "").replace(")/", ""))));
		}
	}

	public static String getFullDate(String dateString){
		if(StringUtil.isNullOrEmpty(dateString)){
			return "";
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(new Date(Long.parseLong(dateString.replace("/Date(", "").replace(")/", ""))));
		}
	}
}
