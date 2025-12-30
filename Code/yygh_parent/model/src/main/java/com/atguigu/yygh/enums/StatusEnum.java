package com.atguigu.yygh.enums;

/**
 * @author Henry Guan
 * @description 状态枚举类
 * @since 2023-04-27
 */

public enum StatusEnum {
    LOCK(0,"锁定"),
    NORMAL(1,"正常"),
    ;
    private Integer status;
    private String name;

    StatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public static String getNameByStatus(Integer status) {
        StatusEnum[] arrObj = StatusEnum.values();
        for (StatusEnum obj : arrObj) {
            if (obj.getStatus().intValue() == status.intValue()) {
                return obj.getName();
            }
        }
        return "";
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
