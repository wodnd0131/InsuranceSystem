package undewriting;

public enum PolicyType {
    BASIC("Basic"),// 일반 인수 관련 인수 정책
    COLLABORATIVE("Collaborative"); // 공동 인수 관련 인수 정책
    

    private String policyType;
    PolicyType( String policyType ){
        this.policyType = policyType;
    }

    public String getString() {
        return this.policyType;
    }
    
}
