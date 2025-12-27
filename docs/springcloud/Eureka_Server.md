# 유레카 서버

## Eureka Self Preservation
### 유레카 서버가 클라이언트 인스턴스 레지스트리를 유지하는 방법
- 클라이언트가 시작될 때 유레카 서버에 REST 호출을 통해 자신을 등록합니다.
- `gradeful shutdown` (정상 종료)가 발생하면 클라이언트는 서버에게 관련된 데이터를 지울 수 있도록 REST 호출을 합니다.
- `ungradeful shutdown` (비정상 종료)를 인지하기 위해서 서버는 클라이언트로부터 특정 시간 간격마다 `heartbeats`를 받습니다. 이런 과정을 `renewal`이라고 합니다.
- 만약 특정 시간 동안 `heartbeat`를 받지 못하면 서버는 클라이언트를 `evict`합니다.

- 심장 박동이 예상 임계값보다 낮을 때 인스턴스 퇴거를 중지하는 메커니즘을 자기 보존이라고 합니다.
  - 서버는 켜져있지만 네트워크 문제가 있을 때
  - 갑작스러운 종료로 도달할 수 없을 때 


### Eureka Config

| 설정 속성                                                    | 설명                                                                                                        | 기본값             |
|------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|------------------|
| `eureka.server.enable-self-preservation`                   | 자체 보존 기능을 비활성화하는 설정                                                                              | `true`           |
| `eureka.server.expected-client-renewal-interval-seconds`   | 이 속성으로 설정된 간격에 따라 서버가 클라이언트의 하트비트를 기대함                                             | `30`초           |
| `eureka.instance.lease-expiration-duration-in-seconds`     | 유레카 서버가 마지막 하트비트를 받은 후 클라이언트를 등록 목록에서 제거하기까지 기다리는 시간(초)                      | `90`초           |
| `eureka.server.eviction-interval-timer-in-ms`              | 이 빈도로 유레카 서버가 만료된 클라이언트를 제거하는 작업을 실행하도록 지시함                                       | `60`초           |
| `eureka.server.renewal-percent-threshold`                  | 이 속성에 기반하여 서버가 모든 등록된 클라이언트로부터 예상되는 분당 하트비트 수를 계산함                               | `0.85`           |
| `eureka.server.renewal-threshold-update-interval-ms`       | 이 빈도로 유레카 서버가 작업을 실행하여 모든 클라이언트로부터 이 시간에 예상되는 하트비트 수를 계산하도록 지시함         | `15`분           |

