package net.lab1024.sa.base.common.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 校验数据是否为空的包装类
 *
 * @Date 2020/10/16 21:06:11
 */
@Data
public class ValidateData<T> {

    @NotNull(message = "数据不能为空哦")
    private T data;
}