package org.yaukie.api.constant;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zhang.nan
 */
@ApiModel(value = "分页结果类")
public class PageResult<T> {
    /**
     * 合计（总记录数）
     */
    @ApiModelProperty(value = "合计")
    public   long total;

    /**
     * 列表
     */
    @ApiModelProperty(value = "列表")
    public T rows;

    public PageResult() {
        this.total = 0;
        this.rows = null;
    }

    public PageResult(PageInfo pageInfo) {
        this.total = pageInfo.getTotal();
        this.rows = (T) pageInfo.getList();
    }

    public PageResult(long total, T rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }
}
