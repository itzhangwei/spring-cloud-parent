package com.mihoyo.center.permission.injector;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Objects;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;

/**
 * mybatis 批量插入，根据 list 中的首个 对象的字段，判断是是否为null，生成不为null字段的，insert（字段）<br/>
 * 如果list中的对象，存在相同字段，有的是null有的不是null。那么此方法就会有问题
 *
 * @author wei.zhang05
 * @projectName mihoyo-center
 * @title InsertBatchMethod
 * @package com.mihoyo.center.permission.injector
 * @date 2021/5/30 17:05
 */
public class InsertBatchMethod extends AbstractMethod {

    private Predicate<TableFieldInfo> predicate;

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = new NoKeyGenerator();
        SqlMethod sqlMethod = SqlMethod.INSERT_ONE;

        String insertSqlColumn = SqlScriptUtils.convertTrim(this.getAllInsertSqlColumnMaybeIf(tableInfo, "et."),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        // 使用 foreach 来生成表的字段 sql, 用foreach 中的 index 来控制生成的字段值循环一次
        insertSqlColumn = SqlScriptUtils.convertIf(insertSqlColumn, String.format("%s == 0", "index"),
                false);
//        String columnScript = "(" + insertSqlColumn + ")";
        String columnScript = SqlScriptUtils.convertForeach(insertSqlColumn, "list", "index", "et", ",");

        String insertSqlProperty = SqlScriptUtils.convertTrim(this.getAllInsertSqlPropertyMaybeIf(tableInfo, "et."),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);

        String valuesScript = SqlScriptUtils.convertForeach(insertSqlProperty, "list", null, "et", ",");
        String keyProperty = null;
        String keyColumn = null;
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                keyGenerator = new Jdbc3KeyGenerator();
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else if (null != tableInfo.getKeySequence()) {
                keyGenerator = TableInfoHelper.genKeyGenerator(this.getMethod(sqlMethod), tableInfo, this.builderAssistant);
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            }
        }

        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), columnScript, valuesScript);
        SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, this.getMethod(sqlMethod), sqlSource, keyGenerator, keyProperty, keyColumn);
    }

    @Override
    public String getMethod(SqlMethod sqlMethod) {
        return "insertBatch";
    }

    public InsertBatchMethod setPredicate(final Predicate<TableFieldInfo> predicate) {
        this.predicate = predicate;
        return this;
    }

    /**
     * 获取所有 insert 时候插入值 sql 脚本片段
     * <p>insert into table (字段) values (值)</p>
     * <p>位于 "值" 部位</p>
     *
     * <li> 自动选部位,根据规则会生成 if 标签 </li>
     * @param tableInfo 表对象
     * @param prefix 前缀
     * @return sql 脚本片段
     */
    private String getAllInsertSqlPropertyMaybeIf(TableInfo tableInfo, final String prefix) {
        final String newPrefix = prefix == null ? "" : prefix;
        return tableInfo.getKeyInsertSqlProperty(newPrefix, true) + tableInfo.getFieldList().stream()
                .map(i -> {
                    String sqlScript = i.getInsertSqlProperty(prefix);
                    if (i.isWithInsertFill()) {
                        return sqlScript;
                    }
                    return convertIf(sqlScript, prefix + i.getProperty(), i.getInsertStrategy(), i.isCharSequence());
                }).filter(Objects::nonNull).collect(joining(TableInfo.NEWLINE));
    }

    /**
     * 转换成 if 标签的脚本片段
     *
     * @param sqlScript      sql 脚本片段
     * @param property       字段名
     * @param fieldStrategy  验证策略
     * @param isCharSequence 是否是字符串
     * @return if 脚本片段
     */
    private String convertIf(final String sqlScript, final String property, final FieldStrategy fieldStrategy, boolean isCharSequence) {
        if (fieldStrategy == FieldStrategy.NEVER) {
            return null;
        }
        if (fieldStrategy == FieldStrategy.IGNORED) {
            return sqlScript;
        }
        if (fieldStrategy == FieldStrategy.NOT_EMPTY && isCharSequence) {
            return SqlScriptUtils.convertIf(sqlScript, String.format("%s != null and %s != ''", property, property),
                    false);
        }
        return SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", property), false);
    }

    /**
     * 获取 insert 时候字段 sql 脚本片段
     * <p>insert into table (字段) values (值)</p>
     * <p>位于 "字段" 部位</p>
     *
     * <li> 自动选部位,根据规则会生成 if 标签 </li>
     *
     * @param tableInfo 表信息对象
     * @param prefix if语句中字段前缀
     * @return sql 脚本片段
     */
    public String getAllInsertSqlColumnMaybeIf(TableInfo tableInfo, String prefix) {
        return tableInfo.getKeyInsertSqlColumn(true) + tableInfo.getFieldList().stream().map(t -> {
            final String sqlScript = t.getInsertSqlColumn();
            if (t.isWithInsertFill()) {
                return sqlScript;
            }
            return this.convertIf(sqlScript, prefix + t.getProperty(), t.getInsertStrategy(), t.isCharSequence());
        }).filter(Objects::nonNull).collect(joining(NEWLINE));
    }
}
