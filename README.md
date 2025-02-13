# CLD_Project_Team2 - 요리왕 Yee룡

## 📌 프로젝트 개요

"요리왕 Yee룡"은 레시피 공유 플랫폼 *만개의 레시피*를 참고하여 개발된 서비스입니다.

요리왕 비룡과 2조를 결합하여 탄생한 프로젝트명입니다! 🚀🔥

## 📌 주요 기능

- **레시피 등록, 수정, 삭제, 조회**
- **AWS S3 기반 이미지 업로드 및 미리보기**
- **레시피 좋아요 기능 및 좋아요 목록 조회**
- **ArgoCD 기반 CI/CD 환경 구축**
- **Grafana를 이용한 모니터링 시스템 구축**

## 🛠 기술 스택

### Back-end
- **Spring Boot 3.4.2**
- **Spring Security & OAuth2**
- **Spring Data JPA**
- **MySQL (AWS RDS)**
- **Redis (세션 클러스터링)**

### Front-end
- **Thymeleaf**
- **Bootstrap 5**

### Infra & DevOps
- **AWS EKS (Elastic Kubernetes Service)**
- **AWS S3 (이미지 저장소)**
- **AWS Route 53 (DNS 관리)**
- **AWS EC2 & Bastion**
- **ArgoCD (CI/CD 자동화)**
- **Grafana & Prometheus (모니터링)**
- **GitHub Actions (CI/CD)**

## 🏗️ 프로젝트 구조

```
📂 CLD_Project_Team2
 ├── 📂 backend
 │   ├── 📂 src
 │   │   ├── 📂 main
 │   │   │   ├── 📂 java/com/recipe
 │   │   │   ├── 📂 controller
 │   │   │   ├── 📂 service
 │   │   │   ├── 📂 repository
 │   │   │   ├── 📂 entity
 │   │   │   ├── 📂 config
 │   │   │   ├── 📂 security
 │   │   │   ├── 📂 dto
 │   │   │   ├── 📄 Application.java
 │   │   ├── 📂 resources
 │   │   │   ├── 📄 application.yml
 │   │   │   ├── 📄 schema.sql
 │   │   │   ├── 📄 data.sql
 │   ├── 📄 Dockerfile
 │   ├── 📄 build.gradle
 │   ├── 📄 README.md
 │
 ├── 📂 frontend
 │   ├── 📂 static
 │   ├── 📂 templates
 │   ├── 📄 index.html
 │   ├── 📄 login.html
 │   ├── 📄 register.html
 │
 ├── 📂 infra
 │   ├── 📄 eks-cluster.yaml
 │   ├── 📄 ingress.yaml
 │   ├── 📄 external-dns.yaml
 │   ├── 📄 argocd-app.yaml
 │
 ├── 📄 README.md
```

## 🚀 프로젝트 배포 과정

1. **GitHub Actions를 이용한 자동 빌드 및 ECR 푸시**
2. **ArgoCD를 이용한 EKS 클러스터 자동 배포**
3. **Ingress + Route 53을 이용한 도메인 설정**
4. **Grafana & Prometheus로 모니터링 환경 구축**

## 🔥 팀원 역할

| 역할 | 이름 | 담당 업무 |
|------|------|----------|
| 팀장 | 강성관 | AWS 클라우드 환경 구축 (Formation 배포 및 Bastion-EC2 세팅) |
| 백엔드 | 정수진 | RDS VPC 관리, Route 53 도메인 구매 후 세팅 |
| 백엔드 | 강수재 | RDS 생성 및 관리, VM 기반 테스트 환경 구축 및 테스트, GitHub Actions 작성 |
| 인프라 | 박한철 | manifest 클러스터 환경 구축 및 전반적인 이슈 디버깅 |

## 📄 ERD 설계

----

## 📌 API 명세서

-----

## 🏗️ CI/CD Pipeline

```
1. GitHub Actions → Docker Build & Push (ECR)
2. ArgoCD → Kubernetes Deployment
3. Route 53 → 도메인 설정
4. Grafana & Prometheus → 모니터링
```

## 🌟 이슈 및 해결 방안

### 1️⃣ STS & VM 환경에서 실행되지 않는 문제
**원인:** Spring Boot 3.2 이상에서 바이트코드 파싱 제거
**해결:** Spring Boot 3.1.x 버전으로 다운그레이드

### 2️⃣ Route 53 서브 도메인 자동 생성 안됨
**원인:** ExternalDNS 설정 문제
**해결:** `externaldns.yaml` 수정 후 환경변수 적용

### 3️⃣ 세션 유지 문제
**원인:** Pod 분산으로 인한 세션 공유 문제
**해결:** ALB Stickiness 설정 및 Redis 기반 세션 클러스터링

## 📜 라이선스

이 프로젝트는 MIT 라이선스를 따릅니다.


