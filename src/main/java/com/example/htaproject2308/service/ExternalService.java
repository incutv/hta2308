package com.example.htaproject2308.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class ExternalService {

    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "fallbackResponse")
    public String callExternalApi() {
        // 외부 API 호출 시뮬레이션
        System.out.println("외부 API 호출 시도 중...");
        throw new RuntimeException("API 호출 실패");  // 실패 시뮬레이션
    }

    // 폴백 메서드: 서킷 오픈 시 호출됨
    public String fallbackResponse(Throwable t) {
        System.out.println("폴백 메서드 호출됨: " + t.getMessage());
        return "외부 서비스가 현재 사용할 수 없습니다. 나중에 다시 시도해주세요.";
    }
}

