# Wellness Coach_BE
<img width="811" alt="스크린샷 2024-09-27 오후 3 48 18" src="https://github.com/user-attachments/assets/63696940-0687-4f45-9218-d275d1bc6541">

**생성형 AI 기반 저속노화 식단 관리 서비스, Wellness Coach 백엔드 레포지토리입니다.**
- 배포 URL : https://wellness-coach.vercel.app

## 주요 기술 스택
<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=OpenJDK&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white"> <br>
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat&logo=jpa&logoColor=white"> <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/OAuth 2.0-4285F4?style=flat&logo=oauth&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=springsecurity&logoColor=white">
<br/>

## 🧠 AI Interaction Process

**Wellness Coach의 핵심 AI는 ChatGPT API를 기반으로 하여, 사용자와 상호작용하며, 다음과 같은 과정을 통해 개인 맞춤형 식단을 추천합니다.**

1.	사용자 입력 : 사용자는 자신이 섭취한 식사를 입력합니다.
2.	식단 분석: Wellness Coach는 입력된 식단을 분석하여 가속노화/유의/저속 단계를 판단하고, 문제가 되는 요소(단순당, 정제곡물 등)를 식별합니다.
3.	대체 재료 추천: 가속 또는 유의 상태일 경우, AI는 저속노화에 도움이 되는 대체 재료와 그 이유를 제안합니다.
4.	쇼핑 링크 연결: 저속 노화에 도움이 되는 대체 재료의 온라인 쇼핑 링크를 네이버 쇼핑 API를 통해 제공합니다.
5.	결과 출력: 사용자에게 분석된 결과와 대체 재료 정보를 제공합니다.

## ERD
<img width="550" alt="스크린샷 2024-09-27 오후 3 35 42" src="https://github.com/user-attachments/assets/6ef3874a-8004-45df-87cb-56a3c331266a">

## API 명세서
[API 명세서 바로가기](https://flying-aunt-8cc.notion.site/API-5265fe7125744481a7daf76ce2787fe4?pvs=4)

## 개발자
| 정연재 |
|:------:|
| <img src="https://github.com/Yeonjae37.png" alt="정연재" width="100"> | 
| BE |
| [Yeonjae37](https://github.com/Yeonjae37)
