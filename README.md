# 📈 주식 배당금 서비스

- 미국 주식 배당금 정보를 제공하는 API 서비스를 개발합니다.

---

## 🛠 1. 중요 사항
-  웹 페이지를 분석하고 스크래핑 기법을 활용하여 필요한 데이터를 추출/저장합니다.
-  사용자별 데이터를 관리하고 예상 배당금 액수를 계산할 수 있습니다.
-  서비스에서 캐시의 필요성을 이해하고 캐시 서버를 구성합니다.

---

## 🚀 2. 기술 스택
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=spring-boot&logoColor=white&style=for-the-badge)

![JPA](https://img.shields.io/badge/JPA-007396?style=for-the-badge)
![H2](https://img.shields.io/badge/H2%20Database-1F6C9A?logo=h2&logoColor=white&style=for-the-badge)

![Redis](https://img.shields.io/badge/Redis-DC382D?logo=redis&logoColor=white&style=for-the-badge)

![Jsoup](https://img.shields.io/badge/Jsoup-FFD700?style=for-the-badge)

![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white&style=for-the-badge)

---

## 📋 3. 최종 구현 API 리스트

### 1) GET - `/finance/dividend/{companyName}`
- 회사 이름을 인풋으로 받아서 해당 회사의 메타 정보와 배당금 정보를 반환
- 잘못된 회사명이 입력으로 들어온 경우 `400` status 코드와 에러메시지 반환

### 2) GET - `/company/autocomplete`
- 자동완성 기능을 위한 API
- 검색하고자 하는 prefix 를 입력으로 받고, 해당 prefix 로 검색되는 회사명 리스트 중 10개 반환

### 3) GET - `/company`
- 서비스에서 관리하고 있는 모든 회사 목록을 반환
- 반환 결과는 `Page 인터페이스` 형태

### 4) POST - `/company`
- 새로운 회사 정보 추가
- 추가하고자 하는 회사의 ticker 를 입력으로 받아 해당 회사의 정보를 스크래핑하고 저장
- 이미 보유하고 있는 회사의 정보일 경우 `400` status 코드와 에러 메시지 반환
- 존재하지 않는 회사 ticker 일 경우 `400` status 코드와 에러 메시지 반환

### 5) DELETE - `/company/{ticker}`
- ticker 에 해당하는 회사 정보 삭제
- 삭제 시 회사의 배당금 정보와 캐시도 모두 삭제되어야 함

### 6) POST - `/auth/signup`
- 회원가입 API
- 중복 ID 는 허용하지 않음
- 패스워드는 암호화된 형태로 저장

### 7) POST - `/auth/signin`
- 로그인 API
- 회원가입이 되어있고, 아이디/패스워드 정보가 옳은 경우 `JWT` 발급

---

## 🔥 4. 프로젝트 진행에 꼭 챙겨야 하는 부분들!

- [x] Spring Boot 로 프로젝트 세팅하기
- [x] H2 인메모리 데이터베이스 연동하기
- [x] Yahoo 파이낸스 데이터 스크래핑하기
- [x] DB Table 모델링 및 연관관계 매핑하기
- [x] 스크래핑한 데이터를 적절한 형태로 DB에 저장하기
- [x] POST `/company` API 구현
- [x] GET `/finance/dividend/{companyName}` API 구현
- [x] GET `/company/autocomplete` API 구현
- [x] GET `/company` API 구현
- [x] DELETE `/company/{ticker}` API 구현
- [ ] Redis 서버 구성하기
- [ ] Redis에 데이터 캐싱/삭제하기
- [x] 적합한 로그레벨로 필요한 로그 남기기
- [x] `ControllerAdvice`에서 에러 처리하기

---

## 🌐 참고 사이트
### [Yahoo Finance](https://finance.yahoo.com/lookup)