package user.enums;

public enum OrderType {
    BUY_ORDER(0), SELL_ORDER(1);

    Integer value;

    OrderType(Integer value){
        this.value = value;
    }

    public Integer getValue(){
        return value;
    }
}
