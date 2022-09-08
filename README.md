# RoutineMaker

## **용인대학교 컴퓨터과학과 19학번 졸업작품 프로젝트**
## 주제 : **Google ML kit**을 활용한 루틴 관리 안드로이드 어플리케이션
### 팀원 : 주종훈, 나연주, 곽시현

----------
# 1. 프로젝트 소개

## Q. **APP 소개**
### + RoutineMaker는 원하는 요일을 지정하여 매일 반복되는 알람을 설정할 수 있습니다.
### + 또한, 할일 Fragment로 To-do List를 작성할 수도 있습니다.
### + To-do List는 바탕화면에 위젯을 추가하여 따로 관리할 수 있습니다.
### + 알람이 수신되었을 경우에, 알람 푸시알림을 클릭하면 카메라 Activity가 열립니다.
### + 카메라 액티비티의 PreviewView를 통해 실시간으로 글자를 인식합니다.
### + 인식한 글자와 내가 설정한 루틴의 제목이 일치한다면, 루틴 인증 성공!
### + 매달 루틴 인증을 매일 수행하여 챌린지 100%를 달성하세요!

## Q. **왜 만들었나요?**
### +
### +
### +

## Q. **개발 기간은 얼마나 되었나요?**
### + 기획 포함 6주간 제작하였습니다.

----------
## **사용한 에디터**

+Android Studio

## **사용한 기술 스택**

### Gradle
+안드로이드 스튜디오의 공식 빌드 시스템인 Gradle로 빌드 자동화

### Kotlin, Java
+안드로이드 개발에 주요 사용되는 Kotlin과 Java 프로그래밍 언어를 사용하였습니다.

### Xml
+안드로이드 어플리케이션의 레이아웃 파일을 xml로 구현하였습니다.

### SQLite, Room DB
+보다 쉽게 내부저장소 데이터베이스를 사용하기 위해 Room DB를 사용하였습니다.
+Room 지속성 라이브러리는 SQLite를 완벽히 활용하면서 원활한 데이터베이스 액세스가 가능하도록 SQLite에 추상화 계층을 제공합니다.

### Google Machine-Learning Kit (https://developers.google.com/ml-kit)
+Google ML-Kit를 사용하여 높은 인식률의 글자인식을 수행하도록 구현하였습니다.

### CameraX(Android jetpack)
+ML-kit를 사용하기 위해 카메라 동작을 일관되게 유지하고, 방향, 회전을 고려하는 라이브러리인 CameraX를 사용하였습니다.

### Git Branch
+Git Branch를 사용하여 독립적인 저장소에서 각자의 역할을 수행할 수 있는 협업 경험을 쌓았습니다

------
## Class Diagram
<img width="647" alt="KakaoTalk_Image_2022-09-08-17-35-08" src="https://user-images.githubusercontent.com/85150438/189075865-c099ab76-2b4f-496c-a5ba-c15b42459959.png">

------
## 소스코드 마인드맵

<img width = "80%" src ="https://github.com/YONGIN-UNIV-19-graduation-project-team/graduationproject/files/9523414/Mind.Map.pdf"/>
------
# 루틴 인증 영상

<img src="https://user-images.githubusercontent.com/85150438/188340107-a5a9700f-b0c6-467d-96c4-7591318d17af.gif"/>


