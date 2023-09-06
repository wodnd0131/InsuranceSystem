# InsuranceSystem
2023년 1학기 분산 프로그래밍 프로젝트
보험사 프로그램 (강의 내용에 따라 TUI로만 구현 )

사용 언어 : JAVA
DB : MY SQL

# 참고 자료

1차 요구사항 분석 보고서
[분산 프로그래밍 중간 보고서.pdf](https://github.com/wodnd0131/InsuranceSystem/files/12530592/default.pdf)

최종 발표 PPT 및 Usecase / Class diagram 등 포함한 EA 파일
https://drive.google.com/drive/folders/1WpPPf1UkY8GTA8UdDoKbqWnUiPMa4LuM?usp=sharing

DB Relation 구조
![image](https://github.com/wodnd0131/InsuranceSystem/assets/62841992/137194c1-455a-4b52-a5c4-3516df0f513a)


# 실행 시 초기 설정

DDL 파일 참고하여 MySQL을 통한 로컬 서버 필요

dao폴더 내 Dao.java 파일 중 아래 부분

 connect = DriverManager.getConnection(url,  "root", "");

이중 getConnection의 파라미터의 2번짼 서버명 3번짼 비밀번호이니 사용자에 맞게 수정 필요
    
