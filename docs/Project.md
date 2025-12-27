# 모듈화 프로그래밍을 하면서 겪은 문제

## 스프링 부트의 컴포넌트 스캔
core-service의 config파일을 읽지 못해서 cors문제를 core-service, user-service에 모두 만들어야 했다.
추후에 security 필터를 적용하는 과정에서 이 문제를 인식했고 컴포넌트 스캔을 설정해주었따. (UserServiceApplication.java)

## 서비스 간 통신
 api gateway를 통해야 할까?
 서비스 간에 통신을 그대로 해도 될까?


https://stackoverflow.com/questions/50163525/feign-client-dynamic-authorization-header



## 모듈화 할 때 특정 의존성 특정 모듈에만 적용하기 
Configuration -> implementation.exclude 