# Tour-Korea Backend
## Spring Cloud를 이용한 MSA 연습 레포

## Guide
### 실행 방법
1. `discovery-service`, `gateway-service`의 실행은 필수이며 `Edit Configuration`에서 `local`로 실행합니다.
2. Database 설정이 어렵다면 `docker/docker-compose.yml`에 있는 `user-mysql`, `post-mysql`을 실행하면 쉽게 설정 가능합니다.

## Documentation

| 문서 제목                   | 링크                                                          | 버전  | 날짜         | 
|---------------------------|-------------------------------------------------------------|------|------------|
| 모듈화                    | [모듈화.md](docs%2Fmodularization%2Fmodule_system.md)          | v1.0 | 2025/12/27 |
| Gradle 멀티 모듈           | [Gradle 멀티 모듈](docs%2Fmodularization%2Fmoudlraization.md)   | v1.0 | 2024/12/27 |

## 프로젝트 실행 시
- VM Options에 아래와 같이 실행하고 싶은 환경의 properties 파일을 지정해야 한다.
```VM Options
-DPROPERTY_PATH=classpath:properties/article-local.properties
```

- application.yml에 값을 추가하는 경우, 운영 서버의 env도 추가해주어야 한다. 