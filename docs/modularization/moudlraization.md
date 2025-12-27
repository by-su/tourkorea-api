## 멀티 모듈

### Gradle 멀티모듈 프로젝트의 기본 구조 
- root project: 전체 프로젝트를 관리하는 최상위 디렉토리
   ~~~groovy
   // 현재의 root 프로젝트와 앞으로 추가될 모든 서브 모듈에 대한 설정
   allprojects {
   sourceCompatibility = '17'
   targetCompatibility = '17'
   
       repositories {
           mavenCentral()
       }
   }
   ~~~ 
- subprojects: 각각 독립적인 기능을 가진 하위 모듈들
   ~~~groovy
   // 루트 제외한 전체 서브 모듈에 해당되는 설정
   // settings.gradle에 include된 전체 프로젝트에 대한 공통 사항을 명시함
   // root 프로젝트까지 적용하고 싶다면 allprojects에서 사용
   subprojects {
   
       apply plugin: 'java'
       apply plugin: 'org.springframework.boot'
       apply plugin: 'io.spring.dependency-management'
   
       // 모든 서브 모듈에서 사용될 공통 의존성들을 추가함
       dependencies {
           if (project.name != 'global-utils') {
               implementation project(':global-utils')
           }
   
           implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0'
           implementation "org.projectlombok:lombok"
           annotationProcessor "org.projectlombok:lombok"
       }
   
       ext {
           set('springCloudVersion', "2023.0.0")
           set('eurekaClientVersion', "4.1.0")
       }
   
       dependencyManagement {
           imports {
               mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
           }
       }
   
       // 모든 서브 모듈에서 Junit을 사용하기 위한 설정
       test {
           useJUnitPlatform()
       }
   }
   ~~~
- settings.gradle: 전체 프로젝트의 설정을 관리하는 파일
  - 다음과 같이 include를 통해 서브 모듈을 추가할 수 있다.
   ~~~groovy
   include 'postservice',
           'postservice-api',
           'postservice-bridge',
           'postservice-domain'
   ~~~

### Gradle을 이용한 빌드
- Gradle에서는 implementation과 api 등의 문법을 통해 의존성을 제어할 수 있다.

1. `implementation`
   - 선언된 종속성은 해당 모듈 내에서만 사용되며, 모듈의 API를 통해 소비하는 다른 모듈들에게는 노출되지 않는다.
   - implementation 종속성은 컴파일 클래스패스에만 포함되고, 런타임 클래스패스에서는 제외되기 때문에 빌드 시간과 성능에 긍정적인 영향을 미칠 수 있다.
   - 특정 모듈에서 사용하는 라이브러리가 다른 모듈에 영향을 미치지 않기 때문에 코드의 이식성이 높아진다.

2. `api(compile)`
   - 선언된 종속성은 해당 모듈을 의존하는 다른 모듈에도 전파된다. 
   - 라이브러리가 제공하는 API에 종속성이 포함되어야 하며, 그 라이브러리를 사용하는 다른 프로젝트에서도 해당 종속성이 필요한 경우 사용될 수 있다.

3. `compileOnly`
   - 빌드의 컴파일 단계에서만 사용되며, 런타임 또는 테스트 런타임 클래스패스에는 포함되지 않는다.
   - 런타임에 불필요한 라이브러리를 제거함으로써 애플리케이션의 런타임 성능을 향상시키고, 패키징 크기를 최소화할 수 있다.

4. `runtimeOnly`
   - 런타임 시에만 필요하고, 컴파일 시에는 필요 없는 종속성
   - 데이터베이스 드라이버나, 런타임에 로딩되는 특정 구현체 등 컴파일 시에는 불필요하지만 실행 시 필수적인 라이브러리를 추가할 때 사용.

5. `testImplementation`
    - 테스트 코드에서만 사용되는 종속성을 추가할 때 사용
    - `JUnit`, `Mockito` 등

6. `testRuntimeOnly`
   - 테스트 실행 시에만 필요하며 테스트 컴파일 시에는 포함되지 않는다.
   - 테스트 실행 시 필요한 특정 라이브러리나 도구를 추가할 때 사용.




### 참고
https://mangkyu.tistory.com/304
https://shorturl.at/fglp4