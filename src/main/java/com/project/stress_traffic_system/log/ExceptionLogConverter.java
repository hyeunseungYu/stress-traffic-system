/*
package com.project.stress_traffic_system.log;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;

public class ExceptionLogConverter extends ThrowableProxyConverter {

    //IThrowableProxy는 예외 객체에 대한 정보를 가지는 것이라고 함.
    //ThrowableProxyConverter는 예외 객체를 어떻게 표현할 지에 대한 것.
    //예외 객체들을 문자열로 바꾸는 작업을 함.
    @Override
    protected String throwableProxyToString(IThrowableProxy tp) {
        return this.extractException(tp);
    }

    private String extractException(IThrowableProxy tp) {
        StringBuffer sb = new StringBuffer();
        sb.append(tp.getClassName());
        sb.append(" : ");
        sb.append(tp.getMessage());
        //어플리케이션 실행 중 오류가 발생했을 때, 오류 발생 원인을 찾는 스택 트레이스에 대한 설정
        //하기 설정으로 "com.project.stress_traffic_system"이 포함되어 있는 경우에만 추출하여 최종적으로 추출한 예외 정보를 문자열 형태로 반환.
        Stream.of(tp.getStackTraceElementProxyArray())
                .forEach(v -> {
                    if (StringUtils.indexOf(v.getSTEAsString(), "com.project.stress_traffic_system") > 0) {
                        sb.append("\n");
                        sb.append(v.getSTEAsString());
                    }
                });
        return sb.toString();
    }
}
*/
