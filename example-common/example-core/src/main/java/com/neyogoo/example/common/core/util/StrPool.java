package com.neyogoo.example.common.core.util;

import cn.hutool.core.util.RandomUtil;

/**
 * 常用字符串
 */
public interface StrPool {

    String AMPERSAND = "&";
    String AND = "and";
    String AT = "@";
    String ASTERISK = "*";
    String STAR = "*";
    String BACK_SLASH = "\\";
    String COLON = ":";
    String COMMA = ",";
    String DASH = "-";
    String DOLLAR = "$";
    String DOT = ".";
    String DOTDOT = "..";
    String DOT_CLASS = ".class";
    String DOT_JAVA = ".java";
    String DOT_XML = ".xml";
    String EMPTY = "";
    String EQUALS = "=";
    String FALSE = "false";
    String SLASH = "/";
    String HASH = "#";
    String HAT = "^";
    String LEFT_BRACE = "{";
    String BRACE = "{}";
    String LEFT_BRACKET = "(";
    char LEFT_BRACKET_CHAR = '(';
    String LEFT_CHEV = "<";
    String NEWLINE = "\n";
    String N = "n";
    String NO = "no";
    String NO_CH = "否";
    String NULL = "null";
    String PERCENT = "%";
    String PIPE = "|";
    String PLUS = "+";
    String QUESTION_MARK = "?";
    String EXCLAMATION_MARK = "!";
    String QUOTE = "\"";
    String RETURN = "\r";
    String TAB = "\t";
    String RIGHT_BRACE = "}";
    String RIGHT_BRACKET = ")";
    String RIGHT_CHEV = ">";
    String SEMICOLON = ";";
    String SINGLE_QUOTE = "'";
    String BACKTICK = "`";
    String SPACE = " ";
    String TILDA = "~";
    String LEFT_SQ_BRACKET = "[";
    String RIGHT_SQ_BRACKET = "]";
    String TRUE = "true";
    String UNDERSCORE = "_";
    String US_ASCII = "US-ASCII";
    String Y = "y";
    String YES = "yes";
    String YES_CH = "是";
    String ONE = "1";
    String ZERO = "0";
    String DOLLAR_LEFT_BRACE = "${";
    String HASH_LEFT_BRACE = "#{";
    String CRLF = "\r\n";
    String HTML_NBSP = "&nbsp;";
    String HTML_AMP = "&amp";
    String HTML_QUOTE = "&quot;";
    String HTML_LT = "&lt;";
    String HTML_GT = "&gt;";
    String MONGO_ID = "_id";
    String STRING_TYPE_NAME = "java.lang.String";
    String LONG_TYPE_NAME = "java.lang.Long";
    String BYTE_TYPE_NAME = "java.lang.Byte";
    String CHARACTER_TYPE_NAME = "java.lang.Character";
    String INTEGER_TYPE_NAME = "java.lang.Integer";
    String SHORT_TYPE_NAME = "java.lang.Short";
    String DOUBLE_TYPE_NAME = "java.lang.Double";
    String FLOAT_TYPE_NAME = "java.lang.Float";
    String BOOLEAN_TYPE_NAME = "java.lang.Boolean";
    String SET_TYPE_NAME = "java.util.Set";
    String LIST_TYPE_NAME = "java.util.List";
    String COLLECTION_TYPE_NAME = "java.util.Collection";
    String DATE_TYPE_NAME = "java.util.Date";
    String LOCAL_DATE_TIME_TYPE_NAME = "java.time.LocalDateTime";
    String LOCAL_DATE_TYPE_NAME = "java.time.LocalDate";
    String LOCAL_TIME_TYPE_NAME = "java.time.LocalTime";
    String JAVA_TEMP_DIR = "java.io.tmpdir";

    String ARRAY = "Array";
    String INTEGER = "Integer";
    String FLOAT = "Float";
    String DATETIME = "DateTime";
    String DATE = "Date";
    String TIME = "Time";
    String BOOLEAN = "Boolean";


    String HTTPS_PREFIX = "https://";
    String HTTP_PREFIX = "http://";
    String HTTPS = "https";
    String HTTP = "http";
    String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    String CONTENT_TYPE_FORM = "multipart/form-data";

    /**
     * 用于随机选的字符
     */
    String BASE_UPPER_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 用于随机选的字符和数字
     */
    String BASE_ALL_CHAR_NUMBER = BASE_UPPER_CHAR + RandomUtil.BASE_CHAR + RandomUtil.BASE_NUMBER;


    String TEST_TOKEN = "Bearer test";
    String TEST = "test";
    String PROD = "prod";


    String UNKNOWN = "unknown";
}
