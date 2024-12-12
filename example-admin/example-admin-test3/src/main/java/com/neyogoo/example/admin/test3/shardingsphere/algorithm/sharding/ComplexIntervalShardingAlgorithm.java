package com.neyogoo.example.admin.test3.shardingsphere.algorithm.sharding;


import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Range;
import org.apache.shardingsphere.infra.exception.core.external.sql.type.generic.UnsupportedSQLOperationException;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * 注意：
 * 使用该算法，新增的时候。如果一条sql多个分片键最终路由的分片不是一个。则当前的这条数据会被路由到全部命中的分片或者随机。开发者需要注意
 */
public class ComplexIntervalShardingAlgorithm implements ComplexKeysShardingAlgorithm<Comparable<?>> {
    private static final String SHARDING_SUFFIX_FORMAT_KEY = "sharding-suffix-pattern";
    private static final String SNOWFLAKE_SUFFIX_LEN_KEY = "snowflake-suffix-len";
    public static final int ID_LEN = 17;
    public static final int ID_YEAR_INDEX = 0;
    private Properties props;
    private String shardingPrefix;
    private String shardingSuffix;
    private int snowflakeSuffixLen;

    /**
     * 分片算法
     *
     * @param availableTargetNames 可用分片名集合
     * @param shardingValue        分片值集合
     * @return 最终的分片路由
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<Comparable<?>> shardingValue) {
        //
        this.initShardingPrefix(availableTargetNames);
        //
        Collection<String> targetNames = new ArrayList<>();
        //
        Map<String, Range<Comparable<?>>> columnNameAndRangeValuesMap = shardingValue.getColumnNameAndRangeValuesMap();
        //
        if (!columnNameAndRangeValuesMap.isEmpty())
            this.handlerColumnNameAndRangeValuesMap(availableTargetNames, targetNames, columnNameAndRangeValuesMap);
        //
        Map<String, Collection<Comparable<?>>> columnNameAndShardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();
        //
        if (!columnNameAndShardingValuesMap.isEmpty())
            this.handlerColumnNameAndShardingValuesMap(availableTargetNames, targetNames, columnNameAndShardingValuesMap);
        //
        return targetNames;
    }

    /**
     * 处理分排片名和范围分片值集合
     *
     * @param availableTargetNames        可用分片名集合
     * @param targetNames                 分片集合
     * @param columnNameAndRangeValuesMap 分排片名和范围分片值集合
     */
    private void handlerColumnNameAndRangeValuesMap(Collection<String> availableTargetNames, Collection<String> targetNames, Map<String, Range<Comparable<?>>> columnNameAndRangeValuesMap) {
        //
        for (Range<Comparable<?>> range : columnNameAndRangeValuesMap.values()) {
            //
            String upperTargetName = null, lowerTargetName = null;
            //
            if (range.hasUpperBound()) upperTargetName = this.getShardingName(range.upperEndpoint());
            //
            if (range.hasLowerBound()) lowerTargetName = this.getShardingName(range.lowerEndpoint());
            //
            if (!Strings.isNullOrEmpty(upperTargetName) && !Strings.isNullOrEmpty(lowerTargetName)) {
                //
                boolean begin = false;
                //
                for (String availableTargetName : availableTargetNames) {
                    //
                    if (Objects.equals(availableTargetName, lowerTargetName)) begin = true;
                    //
                    if (begin) this.addTargetNames(targetNames, availableTargetName);
                    //
                    if (Objects.equals(availableTargetName, upperTargetName)) break;
                }
            }
            //
            else if (!Strings.isNullOrEmpty(lowerTargetName)) {
                //
                boolean begin = false;
                //
                for (String availableTargetName : availableTargetNames) {
                    //
                    if (Objects.equals(availableTargetName, lowerTargetName)) begin = true;
                    //
                    if (begin) this.addTargetNames(targetNames, availableTargetName);
                }
            }
            //
            else if (!Strings.isNullOrEmpty(upperTargetName)) {
                //
                for (String availableTargetName : availableTargetNames) {
                    //
                    this.addTargetNames(targetNames, availableTargetName);
                    //
                    if (Objects.equals(availableTargetName, upperTargetName)) break;
                }
            }
        }
    }

    /**
     * 处理分排片名和分片值集合
     *
     * @param availableTargetNames           可用分片名集合
     * @param targetNames                    分片集合
     * @param columnNameAndShardingValuesMap 分排片名和分片值集合
     */
    private void handlerColumnNameAndShardingValuesMap(Collection<String> availableTargetNames, Collection<String> targetNames, Map<String, Collection<Comparable<?>>> columnNameAndShardingValuesMap) {
        //
        for (Collection<Comparable<?>> collection : columnNameAndShardingValuesMap.values()) {
            //
            for (Comparable<?> comparable : collection) {
                //
                String shardingName = this.getShardingName(comparable);
                //
                if (availableTargetNames.contains(shardingName)) {
                    //
                    this.addTargetNames(targetNames, shardingName);
                } else {
                    //
                    throw new UnsupportedSQLOperationException(String.format("未查询到分片%s信息", shardingName));
                }
            }
        }
    }

    /**
     * 初始化分片前缀
     *
     * @param availableTargetNames 可用分片名集合
     */
    private void initShardingPrefix(Collection<String> availableTargetNames) {
        //
        availableTargetNames.stream()
                .findFirst()
                .ifPresent(availableTargetName -> {
                    int length = availableTargetName.length();
                    this.shardingPrefix = availableTargetName.substring(0, length - shardingSuffix.length());
                });
    }

    /**
     * 添加分片 去重
     *
     * @param targetNames 分片集合
     * @param targetName  当前分片
     */
    private void addTargetNames(Collection<String> targetNames, String targetName) {
        //
        if (targetNames.contains(targetName)) {
            return;
        }
        //
        targetNames.add(targetName);
    }

    /**
     * 根据规则计算出分片名
     *
     * @param comparable 规则字段
     * @return 分片名
     */
    private String getShardingName(Comparable<?> comparable) {
        //
        if (comparable instanceof LocalDate) {
            //
            return this.getShardingNameByLocalDate((LocalDate) comparable);
        }
        //
        if (comparable instanceof LocalDateTime) {
            //
            return this.getShardingNameByLocalDateTime((LocalDateTime) comparable);
        }
        //
        if (comparable instanceof Timestamp) {
            //
            return this.getShardingNameByTimestamp((Timestamp) comparable);
        }
        //
        if (comparable instanceof Long) {
            //
            return this.getShardingNameBySnowflakeId((Long) comparable);
        }
        //
        throw new UnsupportedSQLOperationException(String.format("暂不支持%s类型的分片键.", comparable.getClass()));
    }

    /**
     * 根据雪花算法获取分片
     *
     * @param id 雪花算法标识
     * @return 分片
     */
    private String getShardingNameBySnowflakeId(Long id) {
        //
        String snowflakeId = String.valueOf(id);
        //
//        int snowflakeIdLen = ID_LEN + this.snowflakeSuffixLen;
//        //
//        if (snowflakeId.length() != snowflakeIdLen) {
//            //
//            throw new UnsupportedSQLOperationException(String.format("暂不支持非%d位的雪花算法id.", snowflakeIdLen));
//        }
        //
        return this.shardingPrefix + snowflakeId.substring(
                ID_YEAR_INDEX, ID_YEAR_INDEX + this.snowflakeSuffixLen);
    }

    /**
     * 根据LocalDate算法获取分片
     *
     * @param localDate 时间
     * @return 分片
     */
    private String getShardingNameByLocalDate(LocalDate localDate) {
        //
        return this.shardingPrefix + localDate.format(DateTimeFormatter.ofPattern(this.shardingSuffix));
    }

    /**
     * 根据LocalDateTime算法获取分片
     *
     * @param localDate 时间
     * @return 分片
     */
    private String getShardingNameByLocalDateTime(LocalDateTime localDate) {
        //
        return this.shardingPrefix + localDate.format(DateTimeFormatter.ofPattern(this.shardingSuffix));
    }

    /**
     * 根据时间戳算法获取分片
     *
     * @param timestamp 时间戳
     * @return 分片
     */
    private String getShardingNameByTimestamp(Timestamp timestamp) {
        //
        return this.shardingPrefix + new SimpleDateFormat(this.shardingSuffix).format(timestamp);
    }

/*    @Override
    public Properties getProps() {
        return this.props;
    }*/

    @Override
    public void init(Properties props) {
        this.props = props;
        this.shardingSuffix = this.getShardingSuffix(props);
        this.snowflakeSuffixLen = this.getSnowflakeSuffixLen(props);
    }

    /**
     * 获取分偏键后缀
     *
     * @param props 算法属性
     * @return 分偏键后缀
     */
    private String getShardingSuffix(final Properties props) {
        Preconditions.checkArgument(props.containsKey(SHARDING_SUFFIX_FORMAT_KEY), "%s can not be null.", SHARDING_SUFFIX_FORMAT_KEY);
        return props.getProperty(SHARDING_SUFFIX_FORMAT_KEY);
    }

    /**
     * 获取雪花算法时间后缀长度
     *
     * @param props 算法属性
     * @return 雪花算法时间后缀长度
     */
    private int getSnowflakeSuffixLen(final Properties props) {
        Preconditions.checkArgument(props.containsKey(SNOWFLAKE_SUFFIX_LEN_KEY), "%s can not be null.", SNOWFLAKE_SUFFIX_LEN_KEY);
        String property = props.getProperty(SNOWFLAKE_SUFFIX_LEN_KEY, "2");
        return Integer.parseInt(property);
    }

    @Override
    public String getType() {
        return "COMPLEX_INTERVAL";
    }
}
