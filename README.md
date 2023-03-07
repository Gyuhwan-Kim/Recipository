# Recipository
1. [개요](#개요)
2. [팀원 및 작업 기간](#팀원-및-작업-기간)
3. [개발 환경](#개발-환경)
4. [디자인 시안](#디자인-시안)
5. [페이지 및 주요 기능](#페이지-및-주요-기능)
6. [앞으로의 작업 계획](#앞으로의-작업-계획)

## 개요
1. 반찬을 구매하기보다 직접 만들어 먹겠다는 생각에 요리의 빈도가 높아진 상황. 
요리 과정이 혼동되어 참고했던 유튜브 영상을 반복적으로 찾는 스스로의 모습에 요리 재료와 과정 및 주의사항, 참고한 유튜브 영상 링크 등을 기록할 개인 저장고의 필요성을 느낌.
(작업을 시작하고 ‘만 개의 레시피’ 라는 사이트의 존재를 알게 됨.)

2. Spring Boot와 Spring Security, Spring Data JPA를 공부하기 위하여 단순한 게시판의 역할을 할 수 있는 작업을 해보고자 시작한 개인 프로젝트.

3. 요리에 필요한 재료, 요리 과정, 주의사항, 참고한 유튜브 영상 링크나 기타 참조 링크, 완성된 요리 사진을 기록할 수 있는 게시판 역할의 페이지.

## 팀원 및 작업 기간
- 총원 : 1명 (개인 프로젝트)
- Front-End : 김규환
- Back-End : 김규환
- 작업 기간 : 2022.09.03. ~

## 개발 환경
- 개발 환경(IDE) : IntelliJ IDEA Community Edition 2022.1.2

- 서버 : Apache Tomcat ver. 9.0.65

- DB : MySQL ver. 8.0.30

- 버전 관리 : Github

- Front-end : html5, css, javascript, bootstrap, thymeleaf ver. 2.7.3

- Back-end : java 11, spring framework boot ver. 2.7.3, 
                  spring security ver. 2.7.4, spring data jpa

## 디자인 시안
![image](https://user-images.githubusercontent.com/83267231/223382195-ea7ceed6-9d15-4e06-9f46-649cd35f56dd.png)
작업을 시작하기 전에 화면 구성을 어떻게 할 것인지에 대한 대략적인 작업. 실제 작업이 저렇지는 않음.


## 페이지 및 주요 기능
<details>
  <summary>인덱스 페이지 이미지</summary>
  <div markdown="1"> 
  
  ![image](https://user-images.githubusercontent.com/83267231/223384061-d43a7965-85e0-4762-a7a2-46383b48b379.png)
  ![image](https://user-images.githubusercontent.com/83267231/223385206-50205dfc-cd46-4b94-a509-fc613fc68de0.png)

  </div>
</details>

### 1. 인덱스 페이지
- 게시글 검색 기능 (제목, 작성자, 내용 키워드 필터)
- 로그인 페이지로의 이동
- 회원 가입 페이지로의 이동
- 게시글 목록 확인 (with. pagination)
- 게시글 상세 정보 페이지로의 이동
- 마이 페이지로의 이동 (로그인 이후)
- 로그 아웃 (로그인 이후)
- 게시글 작성 (로그인 이후)

---
<details>
  <summary>회원가입 페이지 이미지</summary>
  <div markdown="1"> 
  
  ![image](https://user-images.githubusercontent.com/83267231/223385852-a530c083-8a6e-458b-a2fb-93494316be2f.png)

  </div>
</details>

### 2. 회원 가입 페이지
- 다음 세 가지 항목에 대한 정보를 작성
    - 이메일 : 로그인에 사용할 정보
    - 닉네임 : 게시글이나 댓글의 작성자 위치에 노출되는 정보
    - 비밀번호 : 로그인에 사용할 정보
- 이메일, 닉네임 중복 확인
- 유효성 검사를 통한 회원 가입

---
<details>
  <summary>로그인 페이지 이미지</summary>
  <div markdown="1"> 
  
  ![image](https://user-images.githubusercontent.com/83267231/223386302-18217c67-1b61-4f07-9a1c-21a616496322.png)

  </div>
</details>

### 3. 로그인 페이지
- 회원가입 시 입력했던 이메일과 비밀번호로 로그인

---
<details>
  <summary>게시글 작성 페이지 이미지</summary>
  <div markdown="1"> 
  
  ![image](https://user-images.githubusercontent.com/83267231/223387574-dedf6c37-f7b3-4254-820d-5404b09f3ab7.png)

  </div>
</details>

### 4. 게시글 작성 페이지
- 다음 다섯 가지 항목에 대해 게시글을 작성
    - 제목 : 게시글 목록과 상세 내용에 노출되는 게시글의 제목
    - 내용 : 게시글 상세보기 페이지에서 확인할 수 있는 상세 내용
    - 이미지 업로드 : 이미지 업로드 칸을 클릭하여 이미지 선택, 썸네일 확인 가능
    - 참고 링크 : 참고했던 영상 링크나 기타 링크
    - 게시글 공개 여부 : 게시글 목록에 게시글이 노출될지 여부를 결정할 수 있는 항목
    
---
<details>
  <summary>게시글 상세 정보 이미지</summary>
  <div markdown="1"> 
  
  ![image](https://user-images.githubusercontent.com/83267231/223399582-cc4dbfcf-c5ec-4dc6-b992-d5e5ed7216e5.png)

  </div>
</details>

### 5. 게시글 상세 정보 페이지
- 게시글 정보 조회
    게시글 작성 항목 중 제목, 내용, 이미지, 참조 링크에 더해 다음의 네 가지 항목 확인  
    - 작성자 : 게시글 작성자 정보
    - 작성 일시 : 게시글 최초 작성 일시
    - 수정 일시 : 게시글 수정 일시
    - 조회수 : 게시글에 대한 조회수
- 게시글 수정/삭제 (게시글 작성자)
- 게시글의 댓글 조회 (with. pagination)
- 댓글/답글 작성 (로그인 이후)
- 댓글/답글 삭제 (댓글/답글 작성자)

---
<details>
  <summary>게시글 수정 페이지 이미지</summary>
  <div markdown="1"> 
  
  ![image](https://user-images.githubusercontent.com/83267231/223396845-1034e381-a72b-4dfc-9d8b-f077cd8b6afa.png)

  </div>
</details>

### 6. 게시글 수정 페이지
- 작성한 게시글 정보 수정
- 게시글 작성 form과 동일한 구성

---
<details>
  <summary>마이 페이지 이미지</summary>
  <div markdown="1"> 
  
  ![image](https://user-images.githubusercontent.com/83267231/223397779-5afa17d4-a2f6-457b-a61a-678406cc1b77.png)
  ![image](https://user-images.githubusercontent.com/83267231/223397920-0a381593-b39d-4efa-97fc-d4a5cc3a1c1b.png)
  ![image](https://user-images.githubusercontent.com/83267231/223398074-fad687cd-17c0-4e8f-bd5c-558cbb7493f7.png)
  ![image](https://user-images.githubusercontent.com/83267231/223398509-296036e3-3ce9-47c3-8746-984ece79f8d9.png)
  ![image](https://user-images.githubusercontent.com/83267231/223398528-19e66ec0-2afe-46ff-8a0b-4ee95acd8cd4.png)

  </div>
</details>

### 7. 마이 페이지
- 작성한 게시글 조회 (with. pagination)
- 작성한 게시글 일괄 삭제
- 프로필 변경
    - 프로필 정보 변경
    - 비밀번호 변경
- 회원 탈퇴

## 앞으로의 작업 계획
- 가장 기초적인 게시판으로의 역할을 할 수 있다고 판단하여 포트폴리오를 작성하기 위해 현재까지의 작업에 대한 내용을 정리했지만, 원래 계획했던 모든 것을 구현한 상황은 아니다. 게다가 현재 가지고 있는 문제점도 존재한다. 다음의 항목들로 정리할 수 있겠다.
    - 기존에 계획했던 사항들
        - **좋아요 기능** : 조회수는 정상적으로 노출되지만, 좋아요 기능에 대한 작업을 하지 않았다. 아직 방식조차 생각하지 못한 상태이다.
        - **게시글 카테고리** : 게시글에는 카테고리 항목이 더해질 예정이었다. 현재는 관리자 계정만이 카테고리 정보를 형성할 수 있고, 그렇게 만들어진 카테고리 항목만이 게시글 작성 시 선택할 수 있도록 하는 방식으로 구상하고 있다. 또한 카테고리에 맞게 인덱스 페이지에 노출할 수 있도록 하는 기능 또한 계획했지만 현재 미구현 상태이다.
        - **게시글 sorting** : 인덱스 페이지에서의 게시글 sorting 작업을 하지 않았다. 조회수와 좋아요 수, 오래된 순서 등으로 sorting 할 계획이었는데, 아직 좋아요 기능에 대한 작업을 마무리하지 않아서 일단 sorting 기능을 작업하지 않은 상태이다.
        - **게시글 퍼가기 기능** : 게시글 퍼가기 기능을 작업하고 싶었다. 원하는 레시피가 작성되어 있었다가 비공개로 전환되는 바람에 볼 수 없어진 경우에 해당 게시글을 복사하여 사용자의 마이 페이지에서만 조회할 수 있도록 하는 기능이다.
        - **AWS** : 모든 작업을 계획한 것까지 마친 후에 AWS에 올리는 것까지가 작업을 시작한 목표였다. 따라서 아직 구현되지 않은 항목까지 작업을 마친 후 수행해야 할 것이다.
    - 현재 가지고 있는 문제점
        - **로그인 url** : 현재 로그인 페이지로의 url 이 loginform과 login-form 두 가지로 나타나는 경우가 존재한다. 이는 spring security의 default 설정에 의한 것이라고 생각할 수 있는데, 이는 loginform으로의 통합으로 해결될 수 있을 것이다.
        - **로그인 후 기존 페이지로** : 현재 로그인 버튼을 눌러 로그인을 하는 경우에는 무조건적으로 인덱스 페이지로 이동하게 되는데, 원래 있던 페이지로 이동할 수 있도록 할 생각이다. 아이디어는 로그인 버튼을 눌렀을 때의 url 요청에서는 interceptor에서 요청을 가로채서 그 동작을 handling 할 수 있도록 하는 것이다.
        - **게시글 비공개 관련** : 현재 게시글을 비공개 설정하더라도, url 상에 비공개인 게시글의 번호를 기억하고 있다가 작성하게 되면 게시글 정보를 확인할 수 있게 된다. 이 또한 마찬가지로 interceptor를 활용해서 해결할 수 있을 것이다.
    
     조금씩 시간을 들여 위의 항목들중에 우선 현재 가지고 있는 문제점을 우선적으로 해결하고, 처음 작업을 시작할 때 구상했지만 아직 구현하지 못한 기능들에 대한 작업을 이어서 할 생각이다.

## 관련 볼거리 (Readme 기반)
1. [프로젝트를 정리한 notion 링크](https://alike-brook-60d.notion.site/Recipository-24b9a56c3b3c42c78da9e90512df59ef)
