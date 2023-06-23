# InsuranceSystem

보험사 프로그램 TUI로 구현
DDL 파일 참고하여 MySQL을 통한 로컬 서버 필요

dao폴더 내 Dao.java 파일 중 아래 부분

 connect = DriverManager.getConnection(url,  "root", "");

이중 getConnection의 파라미터의 2번짼 서버명 3번짼 비밀번호이니 사용자에 맞게 수정 필요
    
