package sjtu.reins.web.utils;

public interface MessageEnumInterface {

    default int getStatus() {
        return 0;
    }
    default String getMsg() {
        return "Add Your Message Here!";
    }
}
