package com.atguigu.yygh.common.handler;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

/**
 * @author Henry Guan
 * @description 全局异常处理类
 * @since 2023-03-16
 */
@ControllerAdvice // 凡是标记@ControllerAdvice的类都表示全局异常处理类
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public R error(Exception e){
        log.error(e.getMessage());
        return R.error();
    }

    /**
     * SQL异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public R error(SQLException e) {
        log.error(e.getMessage());
        return R.error().message("SQL执行异常");
    }

    /**
     * 数学处理异常
     * @param e
     * @return
     */
    @ExceptionHandler(ArithmeticException.class)
    public R error(ArithmeticException e){
        log.error(e.getMessage());
        return R.error().message("数学处理异常");
    }

    /**
     * 预约挂号业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(YyghException.class)
    public R error(YyghException e){
        log.error(e.getMessage());
        return R.error().code(e.getCode()).message(e.getMessage());
    }

}
