<div align="center">
<img src="https://github.com/codestates-seb/seb39_main_015/assets/41741221/c30102a7-02d1-4472-b907-0cad9a5fc19d" width=320>
<h3 align="center">바쁘다 바빠 현대 사회 속, 걸으며 생각할 수 있는 여유를 제공합니다.<br>본격 산책 어플리케이션</h3>
<img width="160" alt="qrcode" src="https://github.com/codestates-seb/seb39_main_015/assets/41741221/2b69220b-5264-48f1-81c0-46272748f51b">
</div>

<br>

- **팀 명 :** 걸어볼래
- **프로젝트 명 :** 걸어볼래
- **프로젝트 기간 :** 2023.04.28 - 2023.05.25
- **한줄 소개 :** 바쁘다 바빠 현대 사회 속, 걸으며 생각할 수 있는 여유를 제공하는 산책 기록 서비스
- **팀원 :** 허상범(팀장), 김승철(부팀장), 김성수, 박설화, 이재환, 하종승
- **배포 링크 :** https://would-you-walk.com/

<br>

# 서비스 소개

> [서비스 소개 자세히 보기](https://codestates.notion.site/caefda618e7046e590c7772eb48196cf)

### 기획배경

세상은 점점 빨라지고 있습니다.
<br>
유튜브에는 호기심을 자극하는 썸네일이 하루에도 몇십개씩 보이고,
걸어서 20분이면 갈 거리인데도 우리는 시간을 아낀다는 이유로 킥보드를 빌립니다.
<br>
SNS에 누가누가 1km를 빨리 달렸나 자랑하는 피드는 많이 보이지만,
천천히 걷다가 우연히 발견한 벽의 낙서나 신기하게 생긴 나무의 사진은 찾아보기 힘듭니다.
<br>
<br>
저희는 사용자들이 여유롭게 걸으며 생각할 수 있는 시간을 제공하고자 합니다.<br>
**이렇게나 빠른 세상에서 걸어볼래의 서비스가 여러분들에게 보다 느리고 소중한 시간이 되기를 바랍니다.**

<br>

### 서비스 안내

산책을 하며 바라보는 풍경을 사진으로 담고, 떠오른 생각을 기록할 수 있습니다.<br>
그날의 산책을 마치면, 걸은 경로와 남긴 순간들은 모두 자신의 기록 탭에 남게 됩니다.<br>
피드 탭에서는 전체 공개로 설정되어 있는 다른 유저의 기록들도 확인할 수 있습니다.

<br>

# 팀 소개

| ![허상범](https://avatars.githubusercontent.com/u/41741221?v=4) | ![박설화](https://avatars.githubusercontent.com/u/120469477?v=4) | ![하종승](https://avatars.githubusercontent.com/u/114554291?v=4) | ![김승철](https://avatars.githubusercontent.com/u/119999208?s=400&v=4) | ![김성수](https://avatars.githubusercontent.com/u/40792205?s=64&v=4) | ![이재환](https://avatars.githubusercontent.com/u/97214060?v=4) |
| :-------------------------------------------------------------: | :--------------------------------------------------------------: | :--------------------------------------------------------------: | :--------------------------------------------------------------------: | :------------------------------------------------------------------: | :-------------------------------------------------------------: |
|            [허상범](https://github.com/sangbeomheo)             |               [박설화](https://github.com/iberis2)               |              [하종승](https://github.com/hajongon)               |                [김승철](https://github.com/kim-ksp7331)                |           [김성수](https://github.com/HeWillGoTillTheEnd)            |              [이재환](https://github.com/NOTE111)               |
|                            FE (팀장)                            |                                FE                                |                                FE                                |                              BE (부팀장)                               |                                  BE                                  |                               BE                                |

<details>
<summary><h3>담당 파트</h3></summary>

### `FE`

**`허상범`**

- 기능 외

  - Vercel 배포 환경 설정/관리
  - 회의 진행 및 회의록 작성
  - (프로젝트 기획, 데일리 스크럼 등)
  - User Flow Chart 작성
  - 화면 정의서(UI) 작성
  - 프로젝트 주제 기획
  - 서비스명 선정

- 기능
  - Map 컴포넌트 구현 (현재 위치 주소 표기, 현재위치-시작위치 마커, 경로)
  - PWA(Progressive Web Application) 구현
  - Router 객체 관리 / 공통 레이아웃 구현
  - SCSS 디자인 시스템 작성
  - Sprite SVG Icon 컴포넌트 구현
  - 랜딩페이지 구현
  - 걷기 중 페이지 / 걷기 종료 페이지 구현
  - 걷기 기능 구현 (걷기 시작 ~ 종료)

<br>

**`박설화`**

- 기능 외

  - 프로젝트 주제 기획
  - 서비스명 선정
  - 서비스 매뉴얼 슬라이드 작성

- 기능
  - 걷는 중 기록을 남기는 글, 사진 작성 페이지
  - 카메라 또는 앨범에 접근하여 사진 첨부하기
  - 기록 리스트에서 내가 남긴 전체 기록을 최신순, 월별, 일자별 조회
  - 달력 구현
  - 기록 상세 페이지 RUD
  - 사진 클릭시 원본 크기의 사진 모달 조회
  - 공개설정 드롭다운
  - date-fns로 한국 시간 구현
  - 로딩 중 화면 skeleton css 적용

<br>

**`하종승`**

- 기능 외

  - 프로젝트 아이디어 제안
  - 프로젝트 주제 기획
  - 서비스명 선정
  - PT 영상 편집
  - 앱 캐릭터 디자인

- 기능
  - 구글 맵 렌더링 및 걸은 경로 그리기 테스트
  - 로그인 / 회원가입 / 로그아웃 / 회원탈퇴
  - 마이페이지 레이아웃
  - 유저 정보 CRUD 구현
  - 비밀번호 변경, 비밀번호 찾기 기능 구현
  - 헤더, 모달, 드롭다운 컴포넌트 구현
  - 피드 페이지 레이아웃, 걸은 기록 READ
  - canvas를 이용해 걸은 경로를 그려주는 리액트 훅 구현
  - 페이지 이동 및 새로고침 시에 실행되는 Authorization 구현
  - 모바일 환경에 맞춘 디자인 (safe-area, status-bar, user-scalable 등)

<br>

### `BE`

**`김승철`**

- 기능 외

  - 프로젝트 주제 기획
  - 서비스명 선정
  - 테이블 명세서, ERD 다이어그램 작성
  - github action을 이용한 CI/CD 파이프라인 설정
  - API 계층, 서비스 계층에 대한 슬라이스 테스트 코드 작성
  - Spring Rest Docs를 활용한 API 문서화

- 기능
  - 웹소켓을 이용한 좌표 전송 기능
  - 걷기 중 순간기록 CRUD
  - JWT를 이용한 회원 인증 기능
  - Refresh Token을 이용한 자동 로그인 기능
  - AWS S3를 이용한 이미지 업로드 기능
  - 비회원 걷기 기록 관련 API 구현

<br>

**`김성수`**

- 기능 외

  - 프로젝트 주제 기획
  - 서비스명 선정
  - Route53, EC2를 이용한 프론트 및 백엔드 도메인 설정
  - ERD, API명세서, 테이블 명세서 작성
  - Spring Rest Docs를 이용한 API문서화
  - Github Action을 이용한 CI/CD 파이프라인 구축
  - API 계층, 서비스 계층에 대한 슬라이스 테스트 코드 작성
  - EC2, RDS연결
  - 효율적인 쿼리 로그 조회를 위한 p6spy 적용 및 N+1 문제 해결

- 기능
  - 걷기기록 년,월,일별 조회 API구현
  - 걷기 기록 CRUD 구현

<br>

**`이재환`**

- 기능 외

  - 프로젝트 주제 기획
  - 서비스명 선정
  - 테이블 명세서, ERD 다이어그램 작성
  - github action을 이용한 CI/CD 파이프라인 설정
  - API 계층, 서비스 계층에 대한 슬라이스 테스트 코드 작성
  - Spring Rest Docs를 활용한 API 문서화
  - AWS IAM 유저설정 및 권한 추가 설정
  - 배포 레포지토리 synk fork를 통한 정기적 업데이트

- 기능
  - 회원 CRUD
  - 회원 임시비밀번호 이메일 전송 기능
  - 임시 비밀번호 관련 이메일 템플릿 .html 작성
  - 예외 처리
  - 프로필, 순간기록 이미지 용량 4MB 제한
  - 회원 자동 삭제 기능

</div>
</details>

<br>

# 기술 스택

![image](https://github.com/codestates-seb/seb39_main_015/assets/41741221/d502943b-43d7-4909-bbd7-420c11a41354)

<br>

# 기술발표영상

[![팀 걸어볼래 발표 영상](http://img.youtube.com/vi/5vPsaEICJC4/0.jpg)](https://youtu.be/5vPsaEICJC4)

> [기술발표영상 유튜브 보러가기](https://youtu.be/5vPsaEICJC4)

- 주제
  - PWA - `하종승`
  - 리액트 쿼리 Infinite Query 활용기 - `박설화`
  - 리액트 훅으로 재사용하는 구글 맵 만들기 - `허상범`
  - 멤버쉽 - `이재환`
  - CI/CD파이프라인 및 도메인연결 - `김성수`
  - 웹소켓 - `김승철`

<br>

# 문서

#### [서비스 메뉴얼](https://codestates.notion.site/caefda618e7046e590c7772eb48196cf)

#### [API 명세서](https://was.would-you-walk.com/docs/index.html)

#### [요구사항 정의서](https://www.notion.so/codestates/d2ffdd92cf6a41f49497bf67e29b0b08?pvs=4)

#### [유저플로우](https://www.figma.com/file/9KSLIkZ5L67nCljFu7GBAC/%EA%B1%B8%EC%96%B4%EB%B3%BC%EB%9E%98---%EC%9C%A0%EC%A0%80-%ED%94%8C%EB%A1%9C%EC%9A%B0?type=whiteboard&node-id=0:1&t=H7Gvbs7MqCC9K4fH-1)

#### [화면정의서](https://www.figma.com/file/bwCfktWSuEbLPsBljeMKhF/%EA%B1%B8%EC%96%B4%EB%B3%BC%EB%9E%98-%EB%8B%A4%EC%9E%90%EC%9D%B8?type=design&node-id=0:1&t=9X6LWmj4u4iwCYin-1)

<br>

# ERD 다이어그램

![image](https://github.com/codestates-seb/seb39_main_015/assets/41741221/a911e948-c521-4406-a10f-687dc52fb36b)

<br>

# 브랜치 전략 & 깃 컨벤션

### 브랜치 구조

- `main`: 서비스 운영 브랜치입니다.
- `fe-dev`: 프론트 개발 환경(CI) 브랜치. 이슈 또는 기능 단위로 작업했던 내용을 합치고 검토합니다.
- `be-dev`: 백엔드 개발 환경(CI) 브랜치. 이슈 또는 기능 단위로 작업했던 내용을 합치고 검토합니다.
- `feat`: FE, BE 세부 작업 브랜치입니다.

<br>

#### 기능 브랜치 네이밍 컨벤션

```
{이슈넘버}-{팀}-{태그}-{브랜치명}

ex) 171-be-chore-개발환경설정
```

<br>

### 깃 컨벤션

| Tag                 | Title                                                    |
| ------------------- | -------------------------------------------------------- |
| `Feat`              | 새로운 기능 추가                                         |
| `Fix`               | 버그 수정                                                |
| `Docs`              | 문서 수정                                                |
| `Formatting`        | 세미콜론 누락 등. 코드 자체에는 변경이 없는 경우         |
| `Refactor`          | 코드 리팩토링                                            |
| `Chore`             | 패키지 매니저 수정 및 기타 수정. ex) `.gitignore`        |
| `Design`            | styled-components 등 UI 디자인 수정                      |
| `Rename`            | 파일 또는 폴더 명을 수정하거나 옮기는 작업만 수행한 경우 |
| `Remove`            | 파일을 수정하는 작업만 수행한 경우                       |
| `Comment`           | 필요한 주석 추가 및 변경                                 |
| `Setting`           | 기본 세팅 변경의 경우(ESLint 등)                         |
| `Test`              | 테스트 코드, 리팩토링 테스트 코드 추가                   |
| `!HOTFIX`           | 급하게 치명적인 버그를 고치는 경우                       |
| `!BREAKING CHANGE`  | API 변경을 크게 한 경우                                  |
