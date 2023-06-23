package util;

public class Constants {	 
	public enum Crud{ CREATE, READ, UPDATE, DELETE; }
	public enum Target{
		INSURANCE,						// 상품
		BUSINESS_PLAN,					// 운영 계획
		BUSINESS_PERSONA,				// 운영 유저 페르소나
		CONSULT,						// 상담
		OPERATION_POLICY,				// 운영 방침
		CONTRACT_MANAGEMENT_POLICY,		// 계약 관리 정책
		PAYMENT,						// 납부
		EXPIRATION_CONTRACT,			// 만기 계약
		CONTRACT,						// 계약
		CONTRACT_STATISTIC,				// 계약 통계
		EDUCATION,						// 교육
		EDUCATION_STUDENT,				// 교육 수료자
		CAMPAIGN_PROGRAM,				// 캠페인
		ASSUME_POLICY,					// 인수 정책
		CUSTOMER,						// 고객
		REWARD,							// 보상
		SELL_GROUP,						// 판매 그룹
		ADVICE_NOTE						// 안내장
	}
	public enum PayWay{
		OFFLINE_FTF("직접 만나서 결제"),					// 오프라인 대면 결제
		ONLINE_AUTO("자동이체"),					// 온라인 자동 결제				
		ONLINE_MANUAL("온라인 이체"),					// 온라인 수동 결제
		OFFLINE_VISITING("직원 방문 결제");				// 오프라인 방문 결제
		private String payway;
		
		PayWay( String payway ){ this.payway = payway; }
		public String getString() { return this.payway; }
	}
	public enum Result{ 
		PROCESS( "PROCESS" ), 
		DENY( "DENY" ), 
		ACCEPT( "ACCEPT" );
		private String result;
		
		Result( String result ){ this.result = result; }
		public String getString() { return this.result; }
	}
	public enum Gender{
		MALE( "Male" ),
		FEMALE( "Female" );
		private String gender;

		Gender( String gender ){this.gender = gender;}
		public String getString() {return this.gender;}
	}
}
