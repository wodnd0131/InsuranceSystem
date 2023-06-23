import business.OperationPolicy;
import business.OperationPolicyList;
import business.SellGroup;
import business.SellGroupList;
import businessEducation.Education;
import businessEducation.EducationList;
import businessEducation.EducationStudent;
import businessEducation.EducationStudentList;
import contract.*;
import contractManagement.ContractManagementPolicy;
import contractManagement.ContractManagementPolicyList;
import customer.CounselingState;
import customer.Customer;
import customer.CustomerCounseling;
import customer.CustomerCounselingList;
import customerManagement.CustomerManagementList;
import dao.*;
import exception.CCounselingNotFoundException;
import exception.CIllegalArgumentException;
import exception.CInsuranceNotFoundException;
import exception.CustomException;
import exception.SOPPolicyNotFoundException;
import insurance.Insurance;
import insurance.InsuranceList;
import insurance.InsuranceState;
import insurance.InsuranceType;
import java.time.LocalDate;
import java.time.LocalDateTime;

import customer.CustomerList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import marketingPlanning.CampaignProgram;
import marketingPlanning.CampaignProgramList;
import marketingPlanning.CampaignState;
import outerActor.OuterActor;
import reward.Reward;
import reward.RewardList;
import teams.BusinessEducationTeam;
import teams.BusinessTeam;
import teams.ContractManagementTeam;
import teams.CustomerManagementTeam;
import teams.InsuranceDevelopmentTeam;
import teams.MarketingPlanningTeam;
import teams.RewardTeam;
import teams.SellGroupTeam;
import teams.UnderwritingTeam;
import undewriting.AssumePolicy;

import undewriting.AssumePolicyList;
import userPersona.UserPersona;
import userPersona.UserPersonaList;
import util.Constants;
import util.Constants.Crud;
import util.Constants.Gender;
import util.Constants.PayWay;
import util.Constants.Result;
import util.Constants.Target;
import util.TuiReader;
import util.TimeChecker;

public class Main {
    private static InsuranceList insuranceList;
    private static AssumePolicyList assumePolicyList;
    private static CampaignProgramList campaignProgramList;
    private static ContractList contractList;
    private static CustomerList customerList;
    private static UserPersonaList userPersonaList;
    private static CustomerCounselingList customerCounselingList;
    private static CustomerManagementList customerManagementList;
    private static SellGroupList sellGroupList;
    private static PaymentList paymentList;
    private static OperationPolicyList operationPolicyList;
    private static EducationList educationList;
    private static EducationStudentList studentList;
    private static AdviceNoteList adviceNoteList;
    private static RewardList rewardList;
    private static ContractManagementPolicyList contractManagementPolicyList;

    private static InsuranceDevelopmentTeam insuranceDevelopmentTeam;
    private static RewardTeam rewardTeam;
    private static UnderwritingTeam underwritingTeam;
    private static MarketingPlanningTeam marketingPlanningTeam;
    private static CustomerManagementTeam customerManagementTeam;
    private static SellGroupTeam sellGroupTeam;
    private static BusinessTeam businessTeam;
    private static BusinessEducationTeam businessEducationTeam;
    private static ContractManagementTeam contractManagementTeam;
    private static int customerID;

    public static void initialize() {
        insuranceList = new InsuranceDao();
        assumePolicyList = new AssumePolicyDao();
        campaignProgramList = new CampaignProgramDao();
        contractList = new ContractDao();
        userPersonaList = new UserPersonaDao();
        customerList = new CustomerDao();
        customerCounselingList = new CustomerCounselingDao();
        customerManagementList = new CustomerManagementDao();
        operationPolicyList = new OperationPolicyDao();
        sellGroupList = new SellGroupDao();
        paymentList = new PaymentDao();
        adviceNoteList = new AdviceNoteDao();
        educationList = new EducationDao();
        studentList = new EducationStudentDao();
        rewardList = new RewardDao();
        contractManagementPolicyList = new ContractManagementPolicyDao();
        insuranceDevelopmentTeam = new InsuranceDevelopmentTeam(insuranceList);
        marketingPlanningTeam = new MarketingPlanningTeam(campaignProgramList);
        rewardTeam = new RewardTeam( rewardList, contractList, customerList, insuranceList );
        underwritingTeam = new UnderwritingTeam(assumePolicyList);
        customerManagementTeam = new CustomerManagementTeam(customerManagementList, customerList);
        sellGroupTeam = new SellGroupTeam(sellGroupList, insuranceList);
        businessTeam = new BusinessTeam(operationPolicyList, sellGroupList);
        businessEducationTeam = new BusinessEducationTeam( educationList, studentList );
        contractManagementTeam = new ContractManagementTeam(contractList, insuranceList, customerList,
            contractManagementPolicyList, paymentList, adviceNoteList);
    }
    public static void main(String[] args) {
        initialize();
        while (true) {
            try {
                loginPage();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private static void loginPage(){
        System.out.println("*********************  로그인  *********************");
        System.out.println(" 1. 고객 로그인");
        System.out.println(" 2. 회원가입");
        System.out.println(" 3. 직원 로그인");
        System.out.println(" 0. 종료");

        int choice = TuiReader.choice(0, 3);
        switch (choice) {
            case 1 :
                System.out.println("ID:");
                String userId = TuiReader.readInputCorrect();
                System.out.println("PassWord:");
                String password = TuiReader.readInputCorrect();
                customerID = customerManagementTeam.login(userId,password);
                System.out.println("로그인 성공");
                customerNotice();
                customerMenu();
                break;
            case 2 :
                System.out.println("ID:");
                userId = TuiReader.readInputCorrect();
                System.out.println("PassWord:");
                password = TuiReader.readInputCorrect();
                boolean isContinue = false;
                String[] customerInf = null;
                while (!isContinue) {
                    try {
                        System.out.println("고객정보를 '/'로 구분하여 입력해주세요 : " +
                            "address/age/sex/job/name/phoneNumber/registrationNumber/incomeLevel/accountNumber/accountPassword");
                        customerInf = TuiReader.readInput("다시 입력해주세요.").split("/");
                        if (customerInf.length != 10) {
                            System.out.println("다시 입력해주세요.");
                            continue;
                        }
                        isContinue = true;
                    } catch (Exception e) {
                        System.out.println("다시 입력해주세요.");
                    }
                }
                customerManagementTeam.register(userId,password,customerInf);
                System.out.println("회원가입 성공");
                break;
            case 3 :
                customerID=-1;
                employeeMenu();
                break;
            case 0 :
                System.exit(0);
                break;
            default:
                throw new CIllegalArgumentException("잘못된 입력입니다.");
        }
    }
    private static void customerMenu() {
        boolean isContinue = true;
        while (isContinue) {
            try {
                System.out.println("********************* MENU *********************");
                System.out.println(" 1. 상담 접수");
                System.out.println(" 2. 보험 가입");
                System.out.println(" 3. 보험금 신청");
                System.out.println(" 0. 종료");
                int choice = TuiReader.choice(0, 3);
                switch (choice) {
                    case 1:
                        counselingApply();
                        break;
                    case 2:
                        registerInsurance();
                        break;
                    case 3:
                        applyReward();
                        break;
                    case 0:
                        isContinue = false;
                        break;
                    default:
                        throw new CIllegalArgumentException("잘못된 입력입니다.");
                }
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private static void customerNotice() {
    	if( customerID != -1 ) {
            List<AdviceNote> adviceList = contractManagementTeam.getAllAdviceNote();
            List<AdviceNote> myAdviceNote = new ArrayList<AdviceNote>();
            for( AdviceNote advice : adviceList ) {
                if( advice.getCustomerID()==customerID ) myAdviceNote.add( advice );
            }
            if( myAdviceNote.size()!=0 ) {
                System.out.println("안내문이 있습니다.");
                for( AdviceNote advice : adviceList ) {
                    Contract expireContract = contractManagementTeam.getContract( advice.getContractID() );
                    Insurance expireInsurance = contractManagementTeam.getInsurance( expireContract.getInsuranceID() );
                    System.out.println( expireInsurance.getInsuranceName() + " 상품에 대한 계약이 만기되었습니다." );
                    System.out.println("1. 재계약   2. 확인 ");
                    int choice = TuiReader.choice( 1,  2 );
                    if( choice==1 ) {
                    	if( advice.getResult().getString().equals("ACCEPT") ) {
                    		System.out.println("이미 재계약 신청이 되어있습니다.");
                    	} else {
                    		advice.setResult( Result.ACCEPT );
                            contractManagementTeam.setAdviceNote( advice );
                            contractManagementTeam.manage( Target.ADVICE_NOTE, Crud.UPDATE );
                            System.out.println("재계약 신청이 완료되었습니다.");
                    	}
                    } else if( choice==2 ) {
                        advice.setResult( Result.DENY );
                        contractManagementTeam.setAdviceNote( advice );
                        contractManagementTeam.manage( Target.ADVICE_NOTE, Crud.UPDATE );
                    }
                }
            }
    	}
    }
    private static void printEmployeeMenu() {
        System.out.println("********************* MENU *********************");
        System.out.println(" 1. 상품개발");
        System.out.println(" 2. 인수정책");
        System.out.println(" 3. 인수심사");
        System.out.println(" 4. 캠페인 프로그램");
        System.out.println(" 5. 영업 활동");
        System.out.println(" 6. 보상 처리");
        System.out.println(" 7. 분납/수금 관리");
        System.out.println(" 8. 교육 수료자 관리");
        System.out.println(" 9. 교육 추가");
        System.out.println(" 10. 만기 계약 관리");
        System.out.println(" 11. 계약 관리 지침");
        System.out.println(" 12. 계약 통계 관리");
        System.out.println(" 13. 영업 조직 관리");
        System.out.println(" 0. 종료 ");
    }
    private static void employeeMenu() {
        boolean isContinue = true;
        while (isContinue) {
            try {
                printEmployeeMenu();
                int choice = TuiReader.choice(0, 13);
                switch (choice) {
                    case 1:
                        createInsurance();
                        break;
                    case 2:
                        uwPolicy();
                        break;
                    case 3:
                        underWriting();
                        break;
                    case 4:
                        campaignProgramMenu();
                        break;
                    case 5:
                        processSales();
                        break;
                    case 6:
                        processReward();
                        break;
                    case 7:
                    	managePayment();
                    	break;
                    case 8:
                    	manageStudent();
                    	break;
                    case 9:
                    	manageEducation();
                    	break;
                    case 10:
                    	manageExpireContract();
                    	break;
                    case 11:
                    	addContractManagementPolicy();
                    	break;
                    case 12:
                    	manageContractAnalysis();
                    	break;
                    case 13:
                        businessTeamManage();
                        break;
                    case 0:
                        isContinue = false;
                        break;
                    default:
                        System.out.println("잘못된 입력입니다.");
                        break;
                }
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private static void createInsurance() {
        System.out.println("********************* 상품 개발 *********************");
        System.out.println(" 1. 상품 기획 버튼");
        System.out.println(" 2. 상품 개발 버튼");
        System.out.println(" 3. 상품 인가 버튼");
        int choice = TuiReader.choice(1, 3);
        switch (choice) {
            case 1:
                createInsurancePlan();
                break;
            case 2:
                Insurance insurance = designInsurance();
                estimateProfit(insurance);
                System.out.println("저장 완료되었습니다. 요율 분석 버튼을 눌러 자동으로 이동합니다.");
                analyzeInsuranceRate(insurance);
                break;
            case 3:
                authorizeInsurance();
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }
    }
    private static void processSales() {
        System.out.println("********************* 영업 활동 *********************");
        System.out.println(" 1. 영업 활동 계획");
        System.out.println(" 2. 상담 일정 수립");
        System.out.println(" 3. 대면 상담");
        int choice = TuiReader.choice(1, 3);
        switch (choice) {
            case 1:
                salesPlan();
                break;
            case 2:
                setConsultationSchedule();
                break;
            case 3:
                faceToFaceConsultation();
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }
    }
    private static void createInsurancePlan() {
        String insurancePlanView = TimeChecker.viewNotResponseCheck(() -> {
            Thread.sleep(12000);
            return "********************* 상품 기획 *********************\n" +
                " 1. 새상품 기획\n" +
                " 2. 기존 상품 관리";
        });
        System.out.println(insurancePlanView);
        int choice = TuiReader.choice(1, 2);
        switch (choice) {
            case 1:
                String insurancePlanCreateView = TimeChecker.viewNotResponseCheck(() ->
                    "고객니즈 및 경쟁사의 동향 분석 보고서를 작성하세요: "
                );
                System.out.println(insurancePlanCreateView);
                insuranceDevelopmentTeam.plan(Target.INSURANCE, Crud.CREATE);
                System.out.println("새 상품 기획안을 저장하였습니다.");
                break;
            case 2:
                System.out.println("********************* 보험 기획안 *********************");
                List<Insurance> insurances = insuranceList.retrieveAll();
                if (insurances.size() == 0) {
                    throw new CInsuranceNotFoundException("수정하거나 삭제할 보험 기획안이 없습니다.");
                }
                System.out.println("수정하거나 삭제할 보험 기획안의 번호를 입력해주세요.");
                for (int i = 0; i < insurances.size(); i++) {
                    System.out.println(i + ". " + insurances.get(i).getPlanReport());
                }
                int choice2 = TuiReader.choice(0, insurances.size()-1);
                System.out.println("수정: 1, 삭제: 2");
                int choice3 = TuiReader.choice(1, 2);
                Insurance findInsurance = insurances.get(choice2);
                if (choice3 == 1) {
                    System.out.println("기존 기획안: " + insurances.get(choice2).getPlanReport());
                    System.out.println("수정할 기획안을 입력해주세요.");
                    String report = TuiReader.readInput("보고서가 올바른 형식이 아닙니다.");
                    findInsurance.setPlanReport(report);
                    insuranceList.update(findInsurance);
                    System.out.println("상품 기획안을 수정하였습니다.");
                } else if (choice3 == 2) {
                    if (findInsurance.getInsuranceState() != InsuranceState.PLANED) {
                        throw new CInsuranceNotFoundException("이미 설계를 시작한 상품입니다.");
                    } else {
                        insuranceList.delete(findInsurance.getInsuranceID());
                        System.out.println("해당 상품 기획안을 삭제하였습니다.");
                    }
                }
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }
    }
    private static Insurance designInsurance() {
        List<Insurance> insurances = insuranceList.retrieveAll()
            .stream()
            .filter(insurance -> insurance.getInsuranceState() == InsuranceState.PLANED)
            .collect(Collectors.toList());
        if (insurances.size() == 0) {
            throw new CInsuranceNotFoundException("설계할 보험 기획안이 없습니다.");
        }
        String designInsuranceView = TimeChecker.viewNotResponseCheck(() ->
            "********************* 상품 개발 ********************* \n" +
                "설계할 보험 기획안의 번호를 입력해주세요."
        );
        System.out.println(designInsuranceView);
        for (int i = 0; i < insurances.size(); i++) {
            System.out.println(i + ". " + insurances.get(i).getPlanReport());
        }
        int choiceId = TuiReader.choice(0, insurances.size()-1);
        String designInsuranceInputView = TimeChecker.viewNotResponseCheck(() ->
            "설계할 보험 기획안: " + insurances.get(choiceId).getPlanReport() + "\n" +
                "설계할 보험의 이름, 상품 종류, 판매 대상, 가입 조건, 보험료, 보장 내용, 개발 예상 비용을 / 로 구분하여 입력해주세요."
        );
        System.out.println(designInsuranceInputView);
        boolean isSuccessInput = false;
        Insurance insurance = insurances.get(choiceId);
        while (!isSuccessInput) {
            try {
                String[] input = TuiReader.readInput("다시 입력하세요.").split("/");
                if (input.length != 7) {
                    System.out.println("다시 입력하세요");
                    continue;
                }
                insurance.setInsuranceName(input[0]);
                insurance.setInsuranceType(InsuranceType.valueOf(input[1]));
                insurance.setSalesTarget(input[2]);
                insurance.setCanRegistTarget(input[3]);
                insurance.setPayment(Integer.parseInt(input[4]));
                insurance.setGuarantee(input[5]);
                insurance.setEstimatedDevelopment(Integer.parseInt(input[6]));
                isSuccessInput = true;
            } catch (Exception e) {
                System.out.println("다시 입력하세요");
            }
        }
        insurance.setInsuranceState(InsuranceState.DESIGNED);
        insuranceList.update(insurance);
        return insurance;
    }
    private static void estimateProfit(Insurance insurance) {
        System.out.println("********************* 예상 손익 분석 *********************");
        System.out.println(" 예상 손익 분석을 원하시면 1, 아니면 2를 입력하세요.");
        int choice = TuiReader.choice(1, 2);
        if (choice == 1) {
            System.out.println("예상 손익률 분석값을 입력해주세요.");
            boolean isSuccessInput = false;
            while (!isSuccessInput) {
                try {
                    String input = TuiReader.readInput("다시 입력하세요.");
                    insurance.setEstimatedProfitRate(Float.parseFloat(input));
                    isSuccessInput = true;
                } catch (Exception e) {
                    System.out.println("다시 입력하세요.");
                }
            }
        } else if (choice == 2) {
            insurance.setEstimatedProfitRate(-1f);
            System.out.println("예상 손익 분석을 건너뛰었습니다.");
        }
    }
    private static void analyzeInsuranceRate(Insurance insurance) {
        String analyzeInsuranceRateView = TimeChecker.viewNotResponseCheck(() ->
            "********************* 요율 분석 *********************\n" +
                "위험도를 입력해주세요. 입력시 자동으로 요율 검증이 진행됩니다."
        );
        System.out.println(analyzeInsuranceRateView);
        boolean isSuccessInput = false;
        while (!isSuccessInput) {
            try {
                String input = TuiReader.readInput("다시 입력하세요.");
                insurance.setRiskDegree(Integer.parseInt(input));
                isSuccessInput = true;
            } catch (Exception e) {
                System.out.println("다시 입력하세요.");
            }
        }
        Float rate = TimeChecker.actorNotResponseCheck(
            OuterActor.calcInsuranceRate(insurance.getPayment(), insurance.getRiskDegree()),
            2, "요율검증부서의 응답이 없습니다.");
        insurance.setRate(rate);
        System.out.println("홍길동 검수인이 검수한 결과 " + rate * 100 + "%가 산출되었습니다.");
        System.out.println("1. 설계 완료 버튼");
        TuiReader.choice(1, 1);
        if (insurance.getRate() == 0.0f) {
            throw new CustomException("아직 요율검증결과 합격되지 않은 상품입니다. 요율검증을 시도하세요!");
        }
        insuranceList.update(insurance);
        System.out.println("해당 상품 설계를 완료했습니다.");
    }
    private static void authorizeInsurance() {
        String authorizationInsuranceView = TimeChecker.viewNotResponseCheck(() ->
            "********************* 상품 인가 ********************* \n" +
                "인가할 보험의 번호를 입력해주세요."
        );
        System.out.println(authorizationInsuranceView);
        List<Insurance> insurances = insuranceList.retrieveAll()
            .stream().filter(insurance -> insurance.getInsuranceState() == InsuranceState.DESIGNED)
            .collect(Collectors.toList());
        if (insurances.size() == 0) {
            throw new CInsuranceNotFoundException("인가할 보험이 없습니다.");
        }
        for (int i = 0; i < insurances.size(); i++) {
            System.out.println(i + ". " + insurances.get(i).getInsuranceName());
        }
        int choice = TuiReader.choice(0, insurances.size()-1);
        Insurance insurance = insurances.get(choice);
        LocalDateTime authorizedDate;
        authorizedDate = TimeChecker.actorNotResponseCheck(
            OuterActor.authorizedInsurance(insurance), 2, "인가를 실패했습니다.");
        if (insurance.getInsuranceState() == InsuranceState.AUTHORIZED) {
            insurance.setDuration(8);
            insurance.setResultAnalysis(10);
            insurance.setRewardAmount(20);
            insurance.setSalesPerformance(30);
            insuranceList.update(insurance);
            System.out.println(
                authorizedDate.getMonth().getValue() + "월 " + authorizedDate.getDayOfMonth() + "일에 합격 되었습니다");
        } else {
            System.out.println("불합격되었습니다.");
        }
    }
    private static void registerInsurance() {
        List<Insurance> registList = insuranceList.retrieveAll()
            .stream()
            .filter(insurance -> insurance.getInsuranceState() == InsuranceState.AUTHORIZED)
            .toList();
        String registerInsuranceView = TimeChecker.viewNotResponseCheck(() -> {
            String view = "********************* 보험 가입 *********************\n";
                for (int i = 0; i < registList.size(); i++) {
                        view += i + ". 이름: " + registList.get(i).getInsuranceName() +
                                ", 종류: " + registList.get(i).getInsuranceType()
                                + ", 가격: " + registList.get(i).getPayment()
                                + ", 기간: " + registList.get(i).getDuration() + "\n";
                }
                return view.substring(0, view.length() - 1);
            }
        );
        System.out.println(registerInsuranceView);
        int registChoice = TuiReader.choice(0, registList.size() - 1);
        Insurance selectedInsurance = registList.get(registChoice);
        List<Contract> contractList = contractManagementTeam.getAllContract();
        for( Contract contract : contractList ) {
        	if( contract.getCustomerID()==customerID && contract.getInsuranceID()==selectedInsurance.getInsuranceID() ) {
        		if( contract.getContractRunState().equals( ContractRunState.READY) ) {
        			System.out.println("이미 신청 대기중인 상품입니다.");
        		} else if( contract.getContractRunState().equals( ContractRunState.FINISH ) ) {
        			System.out.println("이미 해당 상품에 가입되어 있습니다.");
        		}
        		return;
        	}
        }
        String selectInsuranceView = TimeChecker.viewNotResponseCheck(() ->
            "보험 이름: " + registList.get(registChoice).getInsuranceName()
                + ", 기간: " + registList.get(registChoice).getDuration()
                + ", 월 납입료: " + registList.get(registChoice).getPayment() + "\n"
                + "1. 신청\n" + "2. 취소"
        );
        System.out.println(selectInsuranceView);
        int contractChoice = TuiReader.choice(1, 2);
        switch(contractChoice) {
            case 1:
                Customer rgCustomer = customerList.retrieve(customerID);
                Contract contract = new Contract();
                if (rgCustomer.getIncomeLevel() > 5) {
                    contract.setContractUWState(ContractUWState.BASIC);
                    contract.setCustomerID(customerID);
                    contract.setInsuranceID(registList.get(registChoice).getInsuranceID());
                    contract.setContractDate(LocalDate.now());
                    contract.setContractState(ContractState.ONLINE);
                    contract.setContractRunState(ContractRunState.READY);
                    contractManagementTeam.setContract(contract);
                    contractManagementTeam.manage( Target.CONTRACT, Crud.CREATE );
                    Payment payment = new Payment();
                    Contract thisContract = contractManagementTeam.getContractByInsuranceAndCustomerID(selectedInsurance.getInsuranceID(), customerID);
                    payment.setContractID( thisContract.getContractID() );
                    payment.setDuration(0);
                    payment.setContractDuration(0);
                    payment.setContent("특이사항 없음");
                    LocalDate expireDate = LocalDate.now().plusMonths( selectedInsurance.getDuration() );
                    payment.setExpireDate(expireDate);
                    payment.setAmount( selectedInsurance.getPayment() );
                    payment.setAccidentCount(0);
                    payment.setPayway(PayWay.ONLINE_AUTO);
                    payment.setResult(true);
                    contractManagementTeam.setPayment( payment );
                    contractManagementTeam.manage( Target.PAYMENT, Crud.CREATE );
                    System.out.println("보험 가입 신청이 완료되었습니다.");
                } else if (rgCustomer.getIncomeLevel() <= 5) {
                    contract.setContractUWState(ContractUWState.COLLABORATIVE);
                    contract.setCustomerID(customerID);
                    contract.setInsuranceID(registList.get(registChoice).getInsuranceID());
                    contract.setContractDate(LocalDate.now());
                    contract.setContractState(ContractState.ONLINE);
                    contract.setContractRunState(ContractRunState.READY);
                    contractManagementTeam.setContract(contract);
                    contractManagementTeam.manage( Target.CONTRACT, Crud.CREATE );
                    Payment payment = new Payment();
                    Contract thisContract = contractManagementTeam.getContractByInsuranceAndCustomerID(selectedInsurance.getInsuranceID(), customerID);
                    payment.setContractID( thisContract.getContractID() );
                    payment.setDuration(0);
                    payment.setContractDuration(0);
                    payment.setContent("특이사항 없음");
                    LocalDate expireDate = LocalDate.now().plusMonths( selectedInsurance.getDuration() );
                    payment.setExpireDate(expireDate);
                    payment.setAmount( selectedInsurance.getPayment() );
                    payment.setAccidentCount(0);
                    payment.setPayway(PayWay.ONLINE_AUTO);
                    payment.setResult(true);
                    contractManagementTeam.setPayment( payment );
                    contractManagementTeam.manage( Target.PAYMENT, Crud.CREATE );
                    System.out.println("보험 가입 신청이 완료되었습니다.");
                }
                break;
            case 2:
                break;
        }
    }
    private static void uwPolicy() {
        String uwPolicyView = TimeChecker.viewNotResponseCheck(() ->
            "********************* 인수 정책 ********************* \n" +
                " 1. 인수 정책 열람\n" +
                " 2. 인수 정책 수립"
        );
        System.out.println(uwPolicyView);
        int uwchoice = TuiReader.choice(1, 2);
        switch (uwchoice) {
            case 1:
                retrieveUWPolicy();
                break;
            case 2:
                createUWPolicy();
                break;
            default:
                TuiReader.readInput("잘못된 입력입니다.");
                break;
        }
    }
    private static void createUWPolicy() {
        String createUWPolicyView = TimeChecker.viewNotResponseCheck(() ->
            "********************* 인수 정책 수립 ********************* \n" +
                "인수 정책 제목 / 인수 정책 내용 / 인수 정책 기준 : "
        );
        System.out.println(createUWPolicyView);
        underwritingTeam.establishPolicy(Constants.Target.ASSUME_POLICY, Constants.Crud.CREATE);
        System.out.println("인수 정책이 저장되었습니다.");
    }
    private static void retrieveUWPolicy() {
        System.out.println("********************* 인수 정책 열람 *********************");
        List<AssumePolicy> policyList = assumePolicyList.retreiveAll();
        if(policyList != null) {
            for (int i = 0; i < policyList.size(); i++) {
                System.out.print(i + ". 정책 이름: " + policyList.get(i).getName());
                System.out.print(" 정책 내용: " + policyList.get(i).getContent());
                System.out.println(" 정책 종류: " + policyList.get(i).getPolicyType());
            }
        } else {
            System.out.println("현재 인수 정책이 존재하지 않습니다.");
        }
    }
    private static void underWriting() {
        String underWritingView = TimeChecker.viewNotResponseCheck(() ->
            "********************* 인수 심사 ********************* \n" +
                " 1. 인수 심사 접수\n" +
                " 2. 공동 인수 접수"
        );
        System.out.println(underWritingView);
        int choiceUWC = TuiReader.choice(1, 2);
        switch (choiceUWC) {
            case 1:
                basicUW();
                break;
            case 2:
                collaborateUW();
                break;
        }
    }
    private static void basicUW() {
        List<Contract> registList = contractList.retrieveAll()
                .stream()
                .filter(contract ->  contract.getContractRunState() == ContractRunState.READY
                        && contract.getContractUWState() == ContractUWState.BASIC)
                .toList();
        if(!registList.isEmpty()) {
            List<Customer> customers = customerList.retrieveAll();
            List<Insurance> insurances = insuranceList.retrieveAll();
            List<String> joinList = registList.stream()
                .map(contract -> {
                    int customerID = contract.getCustomerID();
                    int insuranceID = contract.getInsuranceID();
                    String customerName = customers.stream()
                        .filter(customer -> customer.getCustomerID() == customerID)
                        .map(Customer::getName)
                        .findFirst()
                        .orElse("고객 정보 없음");
                    String insuranceName = insurances.stream()
                        .filter(insurance -> insurance.getInsuranceID() == insuranceID)
                        .map(Insurance::getInsuranceName)
                        .findFirst()
                        .orElse("보험 정보 없음");
                    String insuranceType = insurances.stream()
                        .filter(insurance -> insurance.getInsuranceID() == insuranceID)
                        .map(insurance -> insurance.getInsuranceType().toString())
                        .findFirst()
                        .orElse("보험 정보 없음");
                    return "고객 이름: " + customerName + ", 보험 이름: " + insuranceName + ", 보험 종류: " + insuranceType;
                }).toList();
            String basicUWView = TimeChecker.viewNotResponseCheck(() -> {
                String view = "";
                for (int i = 0; i < joinList.size(); i++) {
                    view += i + ". " + joinList.get(i) + "\n";
                }
                return view.substring(0, view.length() - 1);
            });
            System.out.println(basicUWView);
            int inChoice = TuiReader.choice(0, registList.size() - 1);
            if(registList.get(inChoice).getContractID() != 0) {
                registList.get(inChoice).setContractRunState(ContractRunState.FINISH);
                contractList.update(registList.get(inChoice));
                System.out.println("해당 고객의 보험 가입 신청을 처리했습니다.");
            }
        } else {
            System.out.println("현재 인수 심사 대상이 존재하지 않습니다.");
        }
    }
    private static void collaborateUW() {
        List<Contract> registCList = contractList.retrieveAll()
                .stream()
                .filter(contract -> contract.getContractRunState() == ContractRunState.READY
                        && contract.getContractUWState() == ContractUWState.COLLABORATIVE)
                .toList();
        if(!registCList.isEmpty()) {
            List<Customer> customers = customerList.retrieveAll();
            List<Insurance> insurances = insuranceList.retrieveAll();
            List<String> joinList = registCList.stream()
                .map(contract -> {
                    int customerID = contract.getCustomerID();
                    int insuranceID = contract.getInsuranceID();
                    String customerName = customers.stream()
                        .filter(customer -> customer.getCustomerID() == customerID)
                        .map(Customer::getName)
                        .findFirst()
                        .orElse("고객 정보 없음");
                    String insuranceName = insurances.stream()
                        .filter(insurance -> insurance.getInsuranceID() == insuranceID)
                        .map(Insurance::getInsuranceName)
                        .findFirst()
                        .orElse("보험 정보 없음");
                    String insuranceType = insurances.stream()
                        .filter(insurance -> insurance.getInsuranceID() == insuranceID)
                        .map(insurance -> insurance.getInsuranceType().toString())
                        .findFirst()
                        .orElse("보험 정보 없음");
                    int incomeLevel = customers.stream()
                        .filter(customer -> customer.getCustomerID() == customerID)
                        .map(Customer::getIncomeLevel)
                        .findFirst()
                        .orElse(-1);
                    return "고객 이름: " + customerName + ", 보험 이름: " + insuranceName
                        + ", 보험 종류: " + insuranceType + ", 소득 분위: " + incomeLevel;
                }).toList();
            String collaborateUWView = TimeChecker.viewNotResponseCheck(() -> {
                String view = "";
                for (int i = 0; i < joinList.size(); i++) {
                    view += i + ". " + joinList.get(i) + "\n";
                }
                return view.substring(0, view.length() - 1);
            });
            System.out.println(collaborateUWView);
            int inChoice = TuiReader.choice(0, registCList.size() - 1);
            Contract contract = registCList.get(inChoice);
            if(contract.getContractID() != 0) {
                int incomeLevel = customerList.retrieve(contract.getCustomerID()).getIncomeLevel();
                boolean deny = OuterActor.collaborateUW(registCList.get(inChoice), incomeLevel);
                if (deny) {
                    contractList.update(registCList.get(inChoice));
                    System.out.println("해당 고객의 보험 가입 신청을 처리했습니다.");
                } else {
                    contractList.update(registCList.get(inChoice));
                    contractList.delete(registCList.get(inChoice).getContractID());
                    System.out.println("고객님은 해당 보험에 가입하실 수 없습니다.");
                }
            }
        } else {
            System.out.println("현재 인수 심사 대상이 존재하지 않습니다.");
        }
    }
    private static void campaignProgramMenu() {
        System.out.println(" 1. 새로운 캠페인 프로그램 기획");
        System.out.println(" 2. 캠페인 프로그램 열람");
        int campaignChoice = TuiReader.choice(1, 2);
        switch(campaignChoice) {
            case 1:
                campaignPlan();
                break;
            case 2:
                retrieveCampaignProgram();
                break;
        }
    }
    private static void campaignPlan() {
        System.out.println("********************* 새로운 캠페인 프로그램 기획 *********************");
        List<Insurance> registList = insuranceList.retrieveAll()
                .stream()
                .filter(insurance -> insurance.getInsuranceState() == InsuranceState.AUTHORIZED)
                .toList();
        for(int i=0; i < registList.size(); i++) {
            System.out.println(i + ". 보험 이름: " + registList.get(i).getInsuranceName());
        }
        int insChoice = TuiReader.choice(0, registList.size()-1);
        Insurance insurance = registList.get(insChoice);
        boolean isCorrect = false;
        while (!isCorrect) {
            try {
                System.out.println("캠페인 이름 / 마케팅 대상 / 마케팅 기간 / 마케팅 장소 / 캠페인 수단 / 마케팅 예산 / 예상 손익률 : ");
                CampaignProgram campaignProgram = new CampaignProgram();
                String campaignPlan = util.TuiReader.readInput("캠페인 프로그램의 내용이 형식과 맞지 않습니다.");
                String[] campaignPlanSplit = campaignPlan.split("/");
                if (campaignPlanSplit.length != 7) {
                    System.out.println("입력 형식이 잘못되었습니다.");
                    continue;
                }
                campaignProgram.setInsuranceID(insurance.getInsuranceID());
                campaignProgram.setCampaignName(campaignPlanSplit[0]);
                campaignProgram.setCampaignTarget(campaignPlanSplit[1]);
                campaignProgram.setDuration(Integer.valueOf(campaignPlanSplit[2]));
                campaignProgram.setPlace(campaignPlanSplit[3]);
                campaignProgram.setCampaignWay(campaignPlanSplit[4]);
                campaignProgram.setBudget(Integer.valueOf((campaignPlanSplit[5])));
                campaignProgram.setExResult(Integer.valueOf(campaignPlanSplit[6]));
                campaignProgram.setProgramState(CampaignState.PLAN);
                campaignProgramList.add(campaignProgram);
                isCorrect = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("새로운 캠페인 프로그램 기획이 저장되었습니다!");
    }

    private static void retrieveCampaignProgram() {
        System.out.println("********************* 캠페인 프로그램 열람 *********************");
        System.out.println(" 1. 현재 진행 중인 캠페인 & 캠페인 종료");
        System.out.println(" 2. 지난 캠페인");
        System.out.println(" 3. 새로운 캠페인 실행");
        int rtCampaignChoice = TuiReader.choice(1, 3);
        switch (rtCampaignChoice) {
            case 1:
                runningCampaign();
                break;
            case 2:
                endCampaign();
                break;
            case 3:
                runCampaign();
                break;
        }
    }
    private static void runningCampaign() {
        System.out.println("********************* 실행 중인 캠페인 & 캠페인 종료 페이지 *********************");
        System.out.println("종료할 프로그램을 선택하세요.");
        List<CampaignProgram> campaignPrograms = campaignProgramList.retrieveAll()
                .stream()
                .filter(campaignProgram -> campaignProgram.getProgramState() == CampaignState.RUN)
                .collect(Collectors.toList());
        if(!campaignPrograms.isEmpty()) {
            for (int i = 0; i < campaignPrograms.size(); i++) {
                System.out.println(i + ". 캠페인 이름: " + campaignPrograms.get(i).getCampaignName()
                        + ", 대상 보험: " + campaignPrograms.get(i).getInsuranceID() + ", 캠페인 대상: " + campaignPrograms.get(i).getCampaignTarget()
                        + ", 기간: " + campaignPrograms.get(i).getDuration() + ", 장소: " + campaignPrograms.get(i).getPlace()
                        + ", 예산: " + campaignPrograms.get(i).getBudget() + ", 예상 손익률: " + campaignPrograms.get(i).getExResult());
            }
            int inChoice = TuiReader.choice(0, campaignPrograms.size() - 1);
            System.out.println("선택한 프로그램: " + campaignPrograms.get(inChoice).getCampaignName());
            System.out.println("1. 종료");
            System.out.println("2. 취소");
            int removeChoice = TuiReader.choice(0, 2);
            switch (removeChoice) {
                case 1:
                    campaignPrograms.get(inChoice).setProgramState(CampaignState.END);
                    campaignProgramList.update(campaignPrograms.get(inChoice));
                    break;
                case 2:
                    break;
            }
        } else {
            System.out.println("현재 진행중인 캠페인 프로그램 대상이 존재하지 않습니다.");
        }

    }
    private static void endCampaign() {
        System.out.println("********************* 지난 캠페인 페이지 *********************");
        List<CampaignProgram> campaignPrograms = campaignProgramList.retrieveAll()
                .stream()
                .filter(campaignProgram -> campaignProgram.getProgramState() == CampaignState.END)
                .collect(Collectors.toList());
        if(!campaignPrograms.isEmpty()) {
        for(int i = 0; i<campaignPrograms.size(); i++) {
            CampaignProgram campaignProgram = campaignPrograms.get(i);
            System.out.println(i + ". 캠페인 이름: " + campaignProgram.getCampaignName()
                    + ", 대상 보험: " + campaignProgram.getInsuranceID() + ", 캠페인 대상: " + campaignProgram.getCampaignTarget()
                    + ", 기간: " + campaignProgram.getDuration() + ", 장소: " + campaignProgram.getPlace()
                    + ", 예산: " + campaignProgram.getBudget() + ", 예상 손익률: " + campaignProgram.getExResult()
            );
        }
            int endCampaignChoice = TuiReader.choice(0, campaignPrograms.size()-1);
            CampaignProgram campaignProgram = campaignPrograms.get(endCampaignChoice);
            float endResult = campaignProgram.getEndResult();
            campaignProgram.setEndResult(endResult);
            campaignProgramList.update(campaignProgram);
            System.out.println("종료된 캠페인의 실제 손익률 저장이 완료되었습니다!");

    }}
    private static void runCampaign() {
        System.out.println("********************* 캠페인 프로그램 실행 *********************");
        List<CampaignProgram> campaignPrograms = campaignProgramList.retrieveAll()
                .stream().filter(campaignProgram -> campaignProgram.getProgramState() == CampaignState.PLAN)
                .toList();
        for(int i = 0; i < campaignPrograms.size(); i++) {
            CampaignProgram campaignProgram = campaignPrograms.get(i);
            System.out.println(i + ". " + campaignProgram.getDuration() + " " + campaignProgram.getCampaignTarget()
                    + " " + campaignProgram.getPlace() + " " + campaignProgram.getOutTeam());
        }
        System.out.println("실행할 캠페인 프로그램을 선택하세요.");
        int campaignChoice = TuiReader.choice(0, campaignPrograms.size() - 1);

        CampaignProgram campaignProgram = campaignPrograms.get(campaignChoice);
        OuterActor.runProgram(campaignProgram);
        campaignProgramList.update(campaignProgram);
        System.out.println("해당 프로그램이 실행됩니다.");
    }
    private static void applyReward() {
    	List<Contract> assignedContract = rewardTeam.getCustomerContract( customerID );
    	List<Insurance> assignedInsurances = rewardTeam.getCustomerInsurance( customerID );
    	if( assignedInsurances.size() == 0 ) {
    		System.out.println( "보험금을 신청할 수 있는 보험이 없습니다" );
    		return;
    	}
        String applyRewardView = TimeChecker.viewNotResponseCheck(() -> {
                String view = "현재 가입된 보험 목록입니다. 보험금 신청을 원하는 보험의 번호를 입력하세요\n" +
                    "---------------------------\n";
                for( int i=0; i<assignedInsurances.size(); i++ ) {
                    view += i + ": " + assignedInsurances.get(i).getInsuranceName() + "\n";
                }
                return view + "---------------------------";
            });
        System.out.println(applyRewardView);
    	int selectedContract = TuiReader.choice(0, assignedInsurances.size()-1);
        String applyConditionView = TimeChecker.viewNotResponseCheck(() ->
            "신청을 위해서 다음 정보가 필요합니다.\n" +
            "사고 증빙 서류: 사고 증명서, 사진 등\n" +
            "본인 증빙 서류: 주민등록증 사본, 가족관계증명서 등" +
            "위 내용을 확인하셨다면 '확인'버튼을 누르세요.\n1. 확인");
    	System.out.println(applyConditionView);
    	int choice = TuiReader.choice(1, 1);
    	System.out.println("신청 내용을 입력하고 증빙 서류를 첨부해주세요.(입력은 내용/사고증빙서류/본인증빙서류 형태로 입력해주세요)");
    	String[] inputList;
    	while( true ) {
    		String userInput = TuiReader.readInput("정확히 입력해주세요");	
    		inputList = userInput.split("/");
    		if( inputList.length!=3 ) {
        		System.out.println("증빙 서류 제출은 필수입니다." );
        	} else {
        		break;
        	}
    	}
    	System.out.println( "1. 신청     2. 취소");
    	choice = TuiReader.choice(1, 2);
    	if( choice==2 ) return;
    	System.out.println( "신청이 완료되었습니다" );
    	Reward applyReward = new Reward();
    	applyReward.setContent( inputList[0] );
    	applyReward.setAccidentProfile( inputList[1] );
    	applyReward.setIdentifyProfile( inputList[2] );
    	applyReward.setAppliResult( Result.PROCESS );
    	applyReward.setAppliDate( LocalDate.now() );
    	applyReward.setReward(0);
    	applyReward.setContractID( assignedContract.get( selectedContract ).getContractID() );
    	Customer tmpCustomer = rewardTeam.getCustomerInformation( customerID );
    	applyReward.setCustomerName( tmpCustomer.getName() );
    	rewardTeam.setReward( applyReward );
    	rewardTeam.manage( Target.REWARD, Crud.CREATE );
    }
    private static void processReward() {
        List<Reward> rewardList = rewardTeam.getAllReward();
        if( rewardList.size() == 0 ) { System.out.println( "접수된 보상 요청이 없습니다" );}
        else {
            String processRewardView = TimeChecker.viewNotResponseCheck(() -> {
                String view = "------------------------------\n";
                for (int i = 0; i < rewardList.size(); i++) {
                    view += i + " " + rewardList.get(i).getCustomerName() + ": " +
                        rewardList.get(i).getAppliResult().getString() +
                        " " + rewardList.get(i).getContent() +
                        " " + rewardList.get(i).getAppliDate().getMonthValue() + "월 " +
                        rewardList.get(i).getAppliDate().getDayOfMonth() + "일 " + "\n";
                }
                return view + "------------------------------";
            });
            System.out.println(processRewardView);
            int select = TuiReader.choice(0, rewardList.size()-1);
            Reward selectedReward = rewardList.get( select );
            String selectCustomerInfoView = TimeChecker.viewNotResponseCheck(() ->
                selectedReward.getCustomerName() + " - " + selectedReward.getContent()
            + "\n해당 보험에 대해 승인하시겠습니까?\n1. 보상 지급   2. 요청 거절   3. 취소");
            System.out.println(selectCustomerInfoView);
            int resultSelect = TuiReader.choice(1, 3);
            if( resultSelect == 1 ) {
                System.out.println( selectedReward.getCustomerName() );
                System.out.println( "지급 금액: " + Integer.toString( selectedReward.getReward() ) + "원" );
                System.out.println( "책임자 이름: 사용자" );
                System.out.println( "내용: " + selectedReward.getContent() );
                System.out.println( "--------- 해당 요청에 대한 보상을 지급합니다. -----------");
                rewardTeam.rewardResult( selectedReward.getRewardID(), Constants.Result.ACCEPT );
            } else if( resultSelect == 2 || resultSelect==3 ) {
                System.out.print( "보상 거절 사유를 입력하세요: ");
                String reason = TuiReader.readInput( "정확한 사유를 입력하세요" );
                selectedReward.setContent( reason );
                selectedReward.setAppliResult( Result.DENY );
                rewardTeam.setReward( selectedReward );
                rewardTeam.manage( Target.REWARD, Crud.UPDATE );
                System.out.println("보상 요청이 거절되었습니다.");
                OuterActor.sendSMStoCustomer(reason);
            } else {
            }
        }
    }
    private static void salesPlan() {
        List<Insurance> insurances = insuranceList.retrieveAll()
            .stream().filter(insurance -> insurance.getInsuranceState() == InsuranceState.AUTHORIZED)
            .collect(Collectors.toList());
        if (insurances.size() == 0) {
            throw new CInsuranceNotFoundException("인가된 보험이 없습니다.");
        }
        String salesPlan = TimeChecker.viewNotResponseCheck(() -> {
            String view = "********************* 영업 활동 계획 수립 *********************\n";
            for (int i = 0; i < insurances.size(); i++) {
                view += i + ". " + insurances.get(i).getInsuranceName() + "\n";
            }
            return view.substring(0, view.length() - 1);
        });
        System.out.println(salesPlan);
        int choice1 = TuiReader.choice(0, insurances.size()-1);
        Insurance insurance = insurances.get(choice1);
        Optional<CampaignProgram> findCampaign = campaignProgramList.retrieveAll().stream()
            .filter(campaignProgram -> campaignProgram.getInsuranceID() == insurance.getInsuranceID()
                && campaignProgram.getProgramState() == CampaignState.END)
            .findFirst();
        if (findCampaign.isEmpty()) {
            throw new CInsuranceNotFoundException("해당 상품의 캠페인 프로그램이 종료되지 않았습니다.");
        }
        System.out.println("캠페인 이름: " + findCampaign.get().getCampaignName());
        int choice2;
        boolean isSuccessInput = false;
        do {
            List<UserPersona> userPersonas = userPersonaList.retrieveAll()
                .stream()
                .filter(userPersona -> userPersona.getInsuranceId() == insurance.getInsuranceID())
                .collect(Collectors.toList());
            System.out.println("-----유저 퍼소나 리스트-----");
            for (UserPersona userPersona : userPersonas) {
                System.out.print("성별: " + userPersona.getSex() + " / ");
                System.out.print("나이: " + userPersona.getAge() + " / ");
                System.out.print("직업: " + userPersona.getJob() + " / ");
                System.out.println("소득수준: " + userPersona.getIncomeLevel());
            }
            System.out.println("--------------------------");
            System.out.println("1. 유저 퍼소나 추가");
            System.out.println("2. 영업 활동 계획 수립");
            choice2 = TuiReader.choice(1, 2);
            while (choice2 == 1 && !isSuccessInput) {
                try {
                    System.out.println("유저 퍼소나 성별, 나이, 직업, 소득수준을 /로 구분하여 입력하세요.");
                    String[] userPersonaInfo = TuiReader.readInput("유저 퍼소나 입력란에 정확히 입력하세요.")
                        .split("/");
                    if (userPersonaInfo.length != 4) {
                        System.out.println("유저 퍼소나 입력란에 정확히 입력하세요.");
                        continue;
                    }
                    UserPersona userPersona = new UserPersona();
                    userPersona.setInsuranceId(insurance.getInsuranceID());
                    userPersona.setSex(Gender.valueOf(userPersonaInfo[0]));
                    userPersona.setAge(Integer.parseInt(userPersonaInfo[1]));
                    userPersona.setJob(userPersonaInfo[2]);
                    userPersona.setIncomeLevel(Integer.parseInt(userPersonaInfo[3]));
                    userPersonaList.add(userPersona);
                    System.out.println("유저 퍼소나가 저장되었습니다.");
                    isSuccessInput = true;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            isSuccessInput = false;
        } while (choice2 == 1);
        isSuccessInput = false;
        while (!isSuccessInput) {
            try {
                System.out.println("영업 활동 시작일(20**-**-**), 종료일(20**-**-**), 예상 목표 인원, 판매 방식을 /로 구분하여 입력하세요.");
                String[] salesPlanInfo = TuiReader.readInput("영업 활동 계획란에 정확히 입력하세요.")
                    .split("/");
                if (salesPlanInfo.length != 4) {
                    System.out.println("영업 활동 계획란에 정확히 입력하세요.");
                    continue;
                }
                insurance.setSalesStartDate(LocalDate.parse(salesPlanInfo[0]));
                insurance.setSalesEndDate(LocalDate.parse(salesPlanInfo[1]));
                insurance.setGoalPeopleNumber(Integer.parseInt(salesPlanInfo[2]));
                insurance.setSalesMethod(salesPlanInfo[3]);
                insuranceList.update(insurance);
                System.out.println("영업 활동 계획을 수립하였습니다.");
                isSuccessInput = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private static void setConsultationSchedule() {
        List<CustomerCounseling> counselingList = customerCounselingList.retrieveAll()
            .stream()
            .filter(counseling -> counseling.getCounselingState() == CounselingState.APPLIED)
            .collect(Collectors.toList());
        if (counselingList.isEmpty()) {
            throw new CCounselingNotFoundException("대면 상담 요청된 고객이 없습니다.");
        }
        List<Customer> counselingCustomerList = counselingList.stream()
            .map(CustomerCounseling::getCustomerId)
            .distinct()
            .map(customerId -> customerList.retrieve(customerId))
            .collect(Collectors.toList());

        boolean isChoiceCustomer = false;
        while (!isChoiceCustomer) {
            String setConsultationScheduleView = TimeChecker.viewNotResponseCheck(
                () -> {
                    String view = "*************** 상담 일정 ***************\n";
                    for (int i = 0; i < counselingCustomerList.size(); i++) {
                        view += i + ". " + counselingCustomerList.get(i).getName() + "\n";
                    }
                    return view.substring(0, view.length() - 1);
                });
            System.out.println(setConsultationScheduleView);
            int choiceCustomer = TuiReader.choice(0, counselingCustomerList.size() - 1);
            Customer customer = counselingCustomerList.get(choiceCustomer);
            List<CustomerCounseling> choiceCustomerCounselingList = counselingList.stream()
                .filter(counseling -> counseling.getCustomerId() == customer.getCustomerID())
                .collect(Collectors.toList());
            System.out.println("0. 취소");
            for (int i = 0; i < choiceCustomerCounselingList.size(); i++) {
                CustomerCounseling counseling = choiceCustomerCounselingList.get(i);
                System.out.println(i+1 + ". 상담 희망 장소: " + counseling.getCounselingPlace()
                    + " 상담 희망일: "+ counseling.getCounselingTime().getMonthValue() + "월 "
                    + counseling.getCounselingTime().getDayOfMonth() + "일 "
                    + "상담 시간: " + counseling.getCounselingTime().getHour() + "시 "
                    + counseling.getCounselingTime().getMinute() + "분");
            }
            int choiceCounseling = TuiReader.choice(0, choiceCustomerCounselingList.size());
            if (choiceCounseling == 0) {
                continue;
            }
            isChoiceCustomer = true;
            CustomerCounseling counseling = choiceCustomerCounselingList.get(choiceCounseling - 1);
            counseling.setCounselingState(CounselingState.ACCEPTED_APPLY);
            customerCounselingList.update(counseling);
            try {
                OuterActor.sendSMStoCustomer("상담 일정이 잡혔습니다.");
            } catch (Exception e) {
                throw new CustomException(e);
            }
            System.out.println("상담 일정이 잡혔습니다.");
        }
    }
    private static void faceToFaceConsultation() {
        List<CustomerCounseling> counselingList = customerCounselingList.retrieveAll()
            .stream()
            .filter(counseling -> counseling.getCounselingState() == CounselingState.ACCEPTED_APPLY)
            .collect(Collectors.toList());
        if (counselingList.isEmpty()) {
            throw new CCounselingNotFoundException("대면 상담 수락된 고객이 없습니다.");
        }
        List<Customer> counselingCustomerList = counselingList.stream()
            .map(counseling -> customerList.retrieve(counseling.getCustomerId()))
            .collect(Collectors.toList());
        String faceToFaceConsultationView = TimeChecker.viewNotResponseCheck(() -> {
            String view = "*************** 대면 상담 ***************\n고객 리스트\n";
            for (int i = 0; i < counselingCustomerList.size(); i++) {
                view += i + ". " + counselingCustomerList.get(i).getName() + "\n";
            }
            return view.substring(0, view.length() - 1);
        });
        System.out.println(faceToFaceConsultationView);
        int choiceCustomer = TuiReader.choice(0, counselingCustomerList.size() - 1);
        CustomerCounseling counseling = counselingList.get(choiceCustomer);
        LocalDateTime counselingTime = counseling.getCounselingTime();
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        if (counselingTime.isBefore(startTime) || counselingTime.isAfter(endTime)) {
            throw new CCounselingNotFoundException("해당 고객은 상담 시간이 아닙니다.");
        }
        Customer customer = customerList.retrieve(counseling.getCustomerId());
        boolean isSuccessInput = false;
        while (!isSuccessInput) {
            try {
                String writeCustomerInformationView = TimeChecker.viewNotResponseCheck(() ->
                    "고객 이름, 나이, 성별, 연락처, 소득 수준, 계좌번호, 계좌 비밀번호를 /로 구분하여 입력하세요.");
                System.out.println(writeCustomerInformationView);
                String[] input = TuiReader.readInput("고객 정보를 정확히 입력해주세요").split("/");
                if (input.length != 7) {
                    System.out.println("고객 정보를 정확히 입력해주세요");
                    continue;
                }
                customer.setName(input[0]);
                customer.setAge(Integer.parseInt(input[1]));
                customer.setSex(Gender.valueOf(input[2]));
                customer.setPhoneNumber(input[3]);
                customer.setIncomeLevel(Integer.parseInt(input[4]));
                customer.setAccountNumber(input[5]);
                customer.setAccountPassword(input[6]);
                isSuccessInput = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        customerList.update(customer);
        boolean isContinue = true;
        while (isContinue) {
            System.out.println("추천 보험 리스트");
            List<Insurance> recommendInsurances = sellGroupTeam.recommendInsurance(customer);
            for (int i = 0; i < recommendInsurances.size(); i++) {
                Insurance insurance = recommendInsurances.get(i);
                System.out.println(i + ". " + insurance.getInsuranceName());
            }
            int choiceInsurance = TuiReader.choice(0, recommendInsurances.size() - 1);
            Insurance insurance = recommendInsurances.get(choiceInsurance);
            System.out.println("추천 보험 상품: " + insurance.getInsuranceName());
            System.out.println(" 보험료 가계산: " + sellGroupTeam.calculateInsuranceFee(insurance, customer) + "원");
            System.out.println(" 보험 추천 이유: " + sellGroupTeam.recommendInsuranceReason(insurance, customer));
            System.out.println("1. 보험 선택, 2. 보험 판매 실패, 3. 뒤로 가기");
            int choice = TuiReader.choice(1, 3);
            if (choice == 1) {
                contract(insurance, customer);
                isContinue = false;
            } else if (choice == 2) {
                System.out.println("보험 판매에 실패했습니다.");
                isContinue = false;
            }
        }
    }
    private static void contract(Insurance insurance, Customer customer) {
        System.out.println("*************** 청약서 작성 ***************");
        String contractFile = TuiReader.readInput(" 저장에 실패했습니다.");
        Contract contract = new Contract();
        contract.setInsuranceID(insurance.getInsuranceID());
        contract.setCustomerID(customer.getCustomerID());
        contract.setContractFile(contractFile);
        contract.setContractDate(LocalDate.now());
        contract.setContractState(ContractState.OFFLINE);
        contract.setContractRunState(ContractRunState.READY);
        ContractUWState contractUWState = customer.getIncomeLevel() <= 5 ?
            ContractUWState.COLLABORATIVE : ContractUWState.BASIC;
        contract.setContractUWState(contractUWState);
        int contractID = contractList.add(contract);
        Payment payment = new Payment();
        Contract thisContract = contractManagementTeam.getContractByInsuranceAndCustomerID(contract.getInsuranceID(), customer.getCustomerID());
        payment.setContractID( thisContract.getContractID() );
        payment.setDuration(0);
        payment.setContractDuration(0);
        payment.setContent("특이사항 없음");
        LocalDate expireDate = LocalDate.now().plusMonths( insurance.getDuration() );
        payment.setExpireDate(expireDate);
        payment.setAmount( insurance.getPayment() );
        payment.setAccidentCount(0);
        payment.setPayway(PayWay.ONLINE_AUTO);
        payment.setResult(true);
        contractManagementTeam.setPayment( payment );
        contractManagementTeam.manage( Target.PAYMENT, Crud.CREATE );
        System.out.println("청약서 저장이 완료됐습니다.");
        System.out.println("1. 인수 심사 버튼");
        int choice = TuiReader.choice(1, 1);
        if (choice == 1) {
            underWriting();
        }
        Contract findContract = contractList.retrieve(contractID);
        try {
            if (findContract.getContractRunState() == ContractRunState.FINISH) {
                OuterActor.sendSMStoCustomer("인수 심사 결과 통과되었습니다.");
            } else if (findContract.getContractRunState() == ContractRunState.DENY) {
                OuterActor.sendSMStoCustomer("인수 심사 결과 불합격하였습니다.");
            }
        } catch (Exception e) {
            throw new CustomException("SMS 전송에 실패했습니다.");
        }
    }
    private static void counselingApply() {
        System.out.println("*************** 상담 신청 ***************");
        boolean isSuccessInput = false;
        while (!isSuccessInput) {
            try {
                System.out.println("상담 희망 장소, 상담 희망일(20**-**-**), 상담 희망 시간(**:**:**)을 /로 구분하여 입력하세요.");
                String[] counselingInfo = TuiReader.readInput("상담 신청란에 정확히 입력하세요.").split("/");
                if (counselingInfo.length != 3) {
                    System.out.println("상담 신청란에 정확히 입력하세요.");
                    continue;
                }
                CustomerCounseling counseling = new CustomerCounseling();
                counseling.setCustomerId(customerID);
                counseling.setCounselingPlace(counselingInfo[0]);
                counseling.setCounselingTime(LocalDateTime.parse(counselingInfo[1] + "T" + counselingInfo[2]));
                counseling.setCounselingState(CounselingState.APPLIED);
                customerCounselingList.add(counseling);
                isSuccessInput = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("접수 완료되었습니다.");
    }
    private static void manageStudent() {
    	List<Education> educationList = businessEducationTeam.getAllEducation();
    	if( educationList.size()==0 )System.out.println("아직 수행된 교육이 없습니다.");
    	else {
	    	System.out.println("--------------------교육 수료자를 관리할 교육을 선택하세요 -----------------------");
	    	for( int i=0; i < educationList.size(); i++ ) {
	    		System.out.println( i + ": " + educationList.get(i).getName() + "( " + educationList.get(i).getTeacherName()  + ") " );
	    	}
	    	System.out.println("----------------------------------------------------------------------");
	    	int select = TuiReader.choice(0, educationList.size() - 1);
	    	int selectedEducationID = educationList.get(select).getEducationID();
	    	List<EducationStudent> studentList = businessEducationTeam.getAllStudent();
	    	List<EducationStudent> educatedStudentList = new ArrayList<EducationStudent>();
	    	for( EducationStudent student : studentList ) {
	    		if( student.getEducationID()==selectedEducationID ) educatedStudentList.add( student );
	    	}
	    	if( educatedStudentList.size()==0 ) {
	    		System.out.println("해당 교육을 수료한 사람이 없습니다.");
	    	}
	    	System.out.println("수행할 작업을 선택하세요");
	    	System.out.println("1. 학생 추가 2. 학생 정보 수정 3. 학생 조회");
	    	select = TuiReader.choice( 1,  3 );
	    	switch( select ) {
	    	case 1:
	    		System.out.println("수료자의 이름, 나이, 전화번호, 평가, 점수, 성별을 입력하세요");
	    		EducationStudent student = new EducationStudent();
	    		String[] studentSplit;
                String studentString = TuiReader.readInput("정확한 정보를 입력하세요");
                studentSplit = studentString.split("/");
                student.setName( studentSplit[0] );
                student.setAge( Integer.parseInt( studentSplit[1] ) );
                student.setPhone( studentSplit[2] );
                student.setExamination( studentSplit[3] );
	    		student.setStudentScore( Integer.parseInt( studentSplit[4] ) );
	    		if( studentSplit[5].equals("M") ) student.setGender( Gender.MALE );
	    		else if( studentSplit[5].equals("F") ) student.setGender( Gender.FEMALE );
	    		student.setEducationID( selectedEducationID );
	    		businessEducationTeam.setStudent( student );
	    		businessEducationTeam.manage( Target.EDUCATION_STUDENT, Crud.CREATE );
	    		System.out.println("저장이 완료되었습니다");
	    		break;
	    	case 2:
	    		if( educatedStudentList.size()==0 ) {
		    		System.out.println("해당 교육을 수료한 사람이 없습니다.");
		    	}
	    		else {
		    		for( int i=0; i<educatedStudentList.size(); i++ ) {
		    			EducationStudent tmpStudent = educatedStudentList.get(i);
		    			System.out.println( i + ": " + tmpStudent.getName() + " " + tmpStudent.getAge() );
		    		}
		    		System.out.println("수정하고자 하는 학생을 선택하세요.");
		    		select = TuiReader.choice( 0, educatedStudentList.size()-1 );
		    		EducationStudent selectedStudent = educatedStudentList.get( select );
		    		System.out.println("수료자의 평가, 점수를 입력하세요");
		    		String tmpString = TuiReader.readInput("정확한 정보를 입력하세요");
		    		String[] tmpSplit = tmpString.split("/");
		    		selectedStudent.setExamination( tmpSplit[0] );
		    		selectedStudent.setStudentScore( Integer.parseInt( tmpSplit[1] ) );
		    		businessEducationTeam.setStudent( selectedStudent );
		    		businessEducationTeam.manage( Target.EDUCATION_STUDENT, Crud.UPDATE );
		    		System.out.println("수정이 완료되었습니다.");
	    		}
	    		break;
	    	case 3:
	    		if( educatedStudentList.size()==0 ) {
		    		System.out.println("해당 교육을 수료한 사람이 없습니다.");
		    	} else {
		    		for( int i=0; i<educatedStudentList.size(); i++ ) {
		    			EducationStudent tmpStudent = educatedStudentList.get(i);
		    			System.out.println( i + ": " + tmpStudent.getName() + " " + tmpStudent.getAge() );
		    		}
		    	}
	    		break;
	    	}
    	}
    }
    private static void manageEducation() {
    	System.out.println("추가할 교육 이름, 교육 기간, 교육 장소, 강사 이름, 강사 전화번호, 교육 예산, 교육 내용을 입력하세요.");
    	String[] educationSplit;
        String educationString = TuiReader.readInput("정확한 정보를 입력하세요");
        educationSplit = educationString.split("/");
    	Education education = new Education();
    	education.setName( educationSplit[0] );
    	education.setDuration( Integer.parseInt( educationSplit[1] ) );
    	education.setPlace( educationSplit[2] );
    	education.setTeacherName( educationSplit[3] );
    	education.setTeacherPhoneNumber( educationSplit[4] );
    	education.setBudget( Integer.parseInt( educationSplit[5] ) );
    	education.setContent( educationSplit[6] );
    	System.out.println( "입력하신 정보가 올바르게 입력되었다면 확인을 눌러주세요.");
    	System.out.println( education.getName() + ": " + education.getTeacherName() + " " + education.getContent() );
    	System.out.println("1. 확인  2. 취소");
    	int choice = TuiReader.choice( 1,  2 );
    	if( choice==1 ) {
    		businessEducationTeam.setEducation( education );
        	businessEducationTeam.manage( Target.EDUCATION, Crud.CREATE );
        	System.out.println("새로운 교육이 추가되었습니다.");
    	}
    }
    private static void addContractManagementPolicy() {
    	List<ContractManagementPolicy> policyList = contractManagementTeam.getAllPolicy();
    	if( policyList.size()== 0 ) {
    		System.out.println("아직 수립된 정책이 없습니다.");
    	} else {
    		System.out.println("-----------현재 수립되어있는 정책 목록입니다. ------------------");
        	for( int i=0; i<policyList.size(); i++ ) {
        		System.out.println( i + ": " + policyList.get(i).getName() );
        	}
        	System.out.println("-------------------------------------------------------");
    	}
    	System.out.println("1. 확인  2. 지침 수립");
    	int choice = TuiReader.choice( 1,  2 );
    	if( choice==2 ) {
    		while( true ) {
    			System.out.println("지침 명칭, 지침 세부내용을 입력하세요.");
    			String policyString;
    			String[] policySplit;
    			ContractManagementPolicy newPolicy = new ContractManagementPolicy();
        		while( true ) {
        			policyString = TuiReader.readInput("");
            		policySplit = policyString.split("/");
            		newPolicy.setName( policySplit[0] );
            		newPolicy.setContent( policySplit[1] );
            		int cnt = 0;
            		for( ContractManagementPolicy alreadyPolicy: policyList ) {
            			if( alreadyPolicy.getName().equals( newPolicy.getName() ) ) {
            				System.out.println("이미 같은 명칭을 가진 정책이 존재합니다.");
            				cnt=0;	
            				break;
            			} else {
            				cnt++;
            			}
            		}
            		if( cnt==policyList.size() ) break;
        		}
        		System.out.println("------------입력한 정보가 올바른지 확인해주세요.-----------------");
        		System.out.println("지침 명칭: " + newPolicy.getName() );
        		System.out.println("지침 내용: " + newPolicy.getContent() );
        		System.out.println("1. 확인  2. 취소");
        		choice = TuiReader.choice( 1,  2 );
        		if( choice==1 ) {
        			contractManagementTeam.setPolicy( newPolicy );
        			contractManagementTeam.manage( Target.CONTRACT_MANAGEMENT_POLICY, Crud.CREATE );
        			break;
        		}
    		}
    	}
    }
    private static void manageContractAnalysis() {
    	List<Insurance> insuranceList = contractManagementTeam.getAllInsurance();
    	if( insuranceList.size()==0 ) {
    		System.out.println("보험 상품 내역을 불러올 수 없습니다");
    	} else {
    		System.out.println("------------------ 현재 생성되어있는 보험 목록 -------------------------");
        	for( int i=0; i<insuranceList.size(); i++ ) {
        		Insurance tempInsurance = insuranceList.get(i);
        		List<Contract> contractListAboutInsurance = contractManagementTeam.getContractByInsuranceID( tempInsurance.getInsuranceID() );
        		int signedCustomerCount = contractListAboutInsurance.size();
        		System.out.println( i + ": " + tempInsurance.getInsuranceName() + " " + tempInsurance.getPayment() + " " + tempInsurance.getGuarantee() +
        				" " + tempInsurance.getRewardAmount() + " " + signedCustomerCount + "명의 가입자" );
        	}
        	System.out.println("----------------------------------------------------------------");
        	System.out.println("자세히 보고 싶은 보험을 선택하세요");
        	int choice = TuiReader.choice( 0, insuranceList.size() - 1 );
        	Insurance selectedInsurance = insuranceList.get( choice );
        	int signedCustomerCount=0;
        	int amountResult=0;
        	if( selectedInsurance.getSalesPerformance() == 0 ) {
        		System.out.println("아직 해당 상품에 대한 사용자의 평가가 존재하지 않습니다.");
        		return;
        	} else {
        		List<Contract> contractListAboutInsurance = contractManagementTeam.getContractByInsuranceID( selectedInsurance.getInsuranceID() );
        		signedCustomerCount = contractListAboutInsurance.size();
        		amountResult = signedCustomerCount * selectedInsurance.getPayment();
        	}
        	if( selectedInsurance.getResultAnalysis()==0 ) {
        		System.out.println("아직 효율 데이터를 계산할 수 없습니다.");
        		return;
        	}
        	System.out.println( "--------------" + selectedInsurance.getInsuranceName() + "에 대한 통계입니다. ---------------------" );
        	System.out.println( "예상 손익률: " + selectedInsurance.getEstimatedProfitRate() );
        	System.out.println( "목표 가입자수: " + selectedInsurance.getGoalPeopleNumber() );
        	System.out.println( "판매 실적: " + amountResult );
        	System.out.println( "분석 결과: " + selectedInsurance.getResultAnalysis() );
        	System.out.println("-----------------------------------------------------------------------------------------------");
    	}
    }
    private static void managePayment() {
    	List<Payment> paymentList = contractManagementTeam.getAllPayment();
    	if( paymentList.size()==0 ) {
    		System.out.println("현재 상품에 가입된 고객이 없습니다");
    		return;
    	}
    	int tmp = 0;
    	for( Payment payment : paymentList ) {
    		if( !payment.getResult() ) break;
    		else tmp++;
    	}
    	if( tmp==paymentList.size() ) {
    		System.out.println("현재 미납 대상자가 존재하지 않습니다.");
    		return;
    	}
    	System.out.println("----------------- 납부 대상자들 목록입니다 ----------------------");
    	int num = 0;
    	for( Payment payment : paymentList ) {
    		Contract tempContract = contractManagementTeam.getContract( payment.getContractID() );
    		Customer tempCustomer = contractManagementTeam.getCustomerInformation( tempContract.getCustomerID() );
    		Insurance tempInsurance = contractManagementTeam.getInsurance( tempContract.getInsuranceID() );
    		String payState;
    		if( payment.getResult() ) payState="납부";
    		else payState="미납부";
    		if( tempCustomer.getName().equals("") || tempCustomer.getName().equals(null) || tempCustomer.getAge()==0 ) {
    			System.out.println("비어있는 정보가 있습니다.");
    			return;
    		}
    		if( tempInsurance.getInsuranceName().equals("") || tempInsurance.getInsuranceName().equals(null) ) {
    			System.out.println("비어있는 정보가 있습니다.");
    			return;
    		}
    		if( payment.getAmount()==0 || payment.getPayway().equals(null) || payment.getContractDuration()==0 || payState.equals(null) ) {
    			System.out.println("비어있는 정보가 있습니다.");
    			return;
    		}
    		System.out.println( num + " " + tempCustomer.getName() + " " + tempCustomer.getAge() + " " +
    				tempInsurance.getInsuranceName() + " " + payment.getAmount() + "원 " + payment.getPayway().getString() + " " +
    				payment.getContractDuration() + " " + payState );
    		num++;
    	}
    	System.out.println( "---------------------------------------------------");
    	System.out.println("원하시는 고객을 선택하세요");
    	int choice = TuiReader.choice( 0, num-1 );
    	Payment selectedPayment = paymentList.get( choice );
    	Contract selectedContract = contractManagementTeam.getContract( selectedPayment.getContractID() );
    	Customer selectedCustomer = contractManagementTeam.getCustomerInformation( selectedContract.getCustomerID() );
		Insurance selectedInsurance = contractManagementTeam.getInsurance( selectedContract.getInsuranceID() );
		System.out.println( "------------------ 해당 고객 정보입니다 -----------------------");
		System.out.println("고객 번호: " + selectedCustomer.getCustomerID() );
		System.out.println("고객 성명: " + selectedCustomer.getName() );
		System.out.println("고객 나이: " + selectedCustomer.getAge() );
		System.out.println("해당 상품: " + selectedInsurance.getInsuranceName() );
		int unPayedMonth = selectedPayment.getContractDuration() - selectedPayment.getDuration();
		int unPayed = unPayedMonth * selectedPayment.getAmount();
		System.out.println("미납 금액: " + unPayed );
		System.out.println("수납 방법: " + selectedPayment.getPayway().getString() );
		System.out.println("가입 기간: " + selectedPayment.getContractDuration() );
		System.out.println("미납 기간: " + unPayedMonth );
		System.out.println("미납 안내: " + selectedPayment.getContent() );
		System.out.println("----------------------------------------------------------");
		System.out.println("1. 미납 공지  2. 확인");
		choice = TuiReader.choice(1,  2 );
		if( choice==1 ) {
			String unpayedWarning = "현재 보험료가 미납되었습니다. 빠른 시일 내에 납부 부탁드립니다.";
			selectedPayment.setContent(unpayedWarning);
			contractManagementTeam.setPayment(selectedPayment);
			contractManagementTeam.manage( Target.PAYMENT, Crud.UPDATE );
			System.out.println("미납 공지가 완료되었습니다.");
		}
    }
    private static void manageExpireContract() {
    	List<Contract> contractList = contractManagementTeam.getAllContract();
    	if( contractList.size()==0 ) {
    		System.out.println("현재 상품에 가입된 사용자가 없습니다.");
    		return;
    	}
    	LocalDate today = LocalDate.now();
    	List<Contract> expireList = new ArrayList<Contract>();
    	for( Contract contract : contractList ) {
    		Insurance tempInsurance = contractManagementTeam.getInsurance( contract.getInsuranceID() );
    		if( today.isAfter( contract.getContractDate().plusMonths( tempInsurance.getDuration() ) ) ) expireList.add( contract );
    	}
    	if( expireList.size()==0 ) {
    		System.out.println("만기 대상자가 없습니다");
    		return;
    	}
        String expiredCustomerView = TimeChecker.viewNotResponseCheck(() -> {
            String view = "---------------------계약이 만기된 고객 목록입니다.-------------------------\n";
            int i = 0;
            for( Contract contract: expireList ) {
                Customer tmpCustomer = contractManagementTeam.getCustomerInformation( contract.getCustomerID() );
                Insurance tmpInsurance = contractManagementTeam.getInsurance( contract.getInsuranceID() );
                view += i + " " + tmpCustomer.getName() + " " + tmpInsurance.getInsuranceName() + " " + contract.getContractDate()+ " " +
                    tmpInsurance.getDuration() + "달 " + "\n";
                i++;
            }
            return view + "--------------------------------------------------------------------";
        });
    	System.out.println(expiredCustomerView);
    	int choice = TuiReader.choice( 0, expireList.size() - 1 );
    	Contract selectedContract = expireList.get( choice );
    	System.out.println("1. 안내장 발송  2. 확인");
    	choice = TuiReader.choice(1,  2 );
    	if( choice==1 ) {
    		AdviceNote adviceNote = new AdviceNote();
    		int newID = contractManagementTeam.getAllAdviceNote().size();
    		adviceNote.setAdviceNoteID( newID );
    		adviceNote.setResult( Result.PROCESS );
    		adviceNote.setCustomerID( selectedContract.getCustomerID() );
    		adviceNote.setContractID( selectedContract.getContractID() );
    		Insurance tmpInsurance = contractManagementTeam.getInsurance( selectedContract.getInsuranceID() );
    		String content = tmpInsurance.getInsuranceName() + "에 대한 계약이 만료되었습니다. 재계약 여부를 확인해주세요. ";
    		adviceNote.setContent( content );
    		contractManagementTeam.setAdviceNote(adviceNote);
    		contractManagementTeam.manage( Target.ADVICE_NOTE, Crud.CREATE );
    		System.out.println("안내장 발송이 완료되었습니다.");
    	}
    }
    private static void businessTeamManage() {
        System.out.println("********************* 영업 조직 관리 *********************");
        System.out.println(" 1. 운영 방침");
        System.out.println(" 2. 성과 평가");
        int campaignChoice = TuiReader.choice(1, 2);
        switch(campaignChoice) {
            case 1:
                operationPolicy();
                break;
            case 2:
                evaluationMenu();
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }
    }
    private static void operationPolicy() {
        System.out.println("********************* 운영 방침 *********************");
        System.out.println(" 1. 운영 방침 열람");
        System.out.println(" 2. 운영 방침 건의");
        System.out.println(" 3. 운영 방침 평가");
        System.out.println(" 4. 운영 방침 수립");
        System.out.println(" 5. 메인 메뉴로 돌아가기");
        int choice = TuiReader.choice(1, 5);
        switch (choice) {
            case 1:
                showOPPolicy();
                operationPolicy();
                break;
            case 2:
                suggestOPPolicy();
                operationPolicy();
                break;
            case 3:
                evaluateOPPolicy();
                operationPolicy();
                break;
            case 4:
                makeOPPolicy();
                operationPolicy();
                break;
            case 5:
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }
    }
    private static void suggestOPPolicy() {
        System.out.println("********************* 운영 방침 건의 *********************");
        System.out.println("'운영 방침 제목 / 운영 방침 내용'를 입력해주세요.");
        businessTeam.establishPolicy(Target.OPERATION_POLICY, Crud.CREATE);
        System.out.println("인수 정책이 건의되었습니다.");
    }
    private static void evaluateOPPolicy() {
        System.out.println("********************* 건의된 운영 방침*********************");
        List<OperationPolicy> policyList = businessTeam.getAllPolicy();
        boolean isShown = false;
        if(!policyList.isEmpty()) {
            for (int i = 0; i < policyList.size(); i++) {
                if(policyList.get(i).getPass() ==0) {
                    System.out.println((i+1) + ". " + policyList.get(i).getName());
                    System.out.println("운영 방침 내용: " + policyList.get(i).getContent());
                    System.out.println("운영 방침 추천 수: " + policyList.get(i).getRating());
                    isShown=true;
                }
            }
            if(isShown){
                System.out.println("********************* 운영 방침 평가 *********************");
                System.out.println("추천할 운영방침의 번호를 입력해주세요.");
                businessTeam.manage(Target.OPERATION_POLICY, Crud.UPDATE);
                System.out.println("평가 내용을 저장되었습니다.");
            }else{System.out.println("현재 건의된 운영 방침이 존재하지 않습니다.");}
        } else {
            System.out.println("현재 건의된 운영 방침이 존재하지 않습니다.");
        }
    }
    private static void makeOPPolicy() {
        System.out.println("********************* 건의된 운영 방침*********************");
        List<OperationPolicy> policyList = businessTeam.getAllPolicy();
        boolean isShown = false;
        if(!policyList.isEmpty()) {
            for (int i = 0; i < policyList.size(); i++) {
                if(policyList.get(i).getPass() ==0){
                System.out.println((i+1) + ". " + policyList.get(i).getName());
                System.out.println("운영 방침 내용: " + policyList.get(i).getContent());
                System.out.println("운영 방침 추천 수: " + policyList.get(i).getRating());
                    isShown= true;
                }
            }
            if(isShown){
            System.out.println("********************* 운영 방침 수립 *********************");
            System.out.println("수립할 운영방침의 번호를 입력해주세요.");
                int choice = TuiReader.choice(1, policyList.size()) - 1;
                OperationPolicy operationPolicy = operationPolicyList.retrieve(policyList.get(choice).getPolicyID());
            if(operationPolicy.getPass()==0){
                operationPolicy.setPass(1);
                operationPolicyList.update(operationPolicy);
            }else {
                throw new SOPPolicyNotFoundException("해당 번호의 건의된 운영 방침이 없습니다.");
            }
            System.out.println("인수 정책이 저장되었습니다.");
            }else{System.out.println("현재 건의된 운영 방침이 존재하지 않습니다.");}
        } else {
            System.out.println("현재 건의된 운영 방침이 존재하지 않습니다.");
        }
    }
    private static void showOPPolicy() {
        System.out.println("********************* 운영 방침 목록 *********************");
        List<OperationPolicy> policyList = businessTeam.getAllPolicy();
        boolean isShown = false;
        if(!policyList.isEmpty()) {
            for (int i = 0; i < policyList.size(); i++) {
                if(policyList.get(i).getPass() !=0){
                    System.out.println((i+1) + ". " + policyList.get(i).getName());
                    System.out.println("운영 방침 내용: " + policyList.get(i).getContent());
                    isShown = true;
                }
            }
            if(!isShown){System.out.println("현재 운영 방침이 존재하지 않습니다.");}
        } else {
            System.out.println("현재 운영 방침이 존재하지 않습니다.");
        }
    }
    private static void evaluationMenu() {
        System.out.println("********************* 성과 평가 *********************");
        System.out.println(" 1. 판매 조직 목록");
        System.out.println(" 2. 성과 평가");
        System.out.println(" 3. 메인 메뉴로 돌아가기");
        int choice = TuiReader.choice(1, 3);
        switch (choice) {
            case 1:
                showEvaluation();
                evaluationMenu();
                break;
            case 2:
                Evaluate();
                evaluationMenu();
                break;
            case 3:
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }
    }
    private static void Evaluate() {
        System.out.println("********************* 성과 평가 *********************");
        System.out.println("'팀 번호 / 성과'를 입력해주세요.");
        businessTeam.evaluateResult();
        System.out.println("평가 내용이 저장되었습니다.");
    }
    private static void showEvaluation() {
        System.out.println("********************* 판매 조직 조회 *********************");
        List<SellGroup> sellGroupList = sellGroupTeam.getAllGroup();
        if(!sellGroupList.isEmpty()) {
            for (int i = 0; i < sellGroupList.size(); i++) {
                System.out.println(sellGroupList.get(i).getGroupID() + " . " +
                    sellGroupList.get(i).getExResult()+" / "+
                    sellGroupList.get(i).getName()+" / "+
                    sellGroupList.get(i).getRepresentative()+" / "+
                    sellGroupList.get(i).getRepresentativePhoneNumber());
            }
        } else {
            System.out.println("현재 판매 조직이 존재하지 않습니다.");
        }
    }
}
