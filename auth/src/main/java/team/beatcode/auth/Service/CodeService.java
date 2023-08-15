package team.beatcode.auth.Service;

public interface CodeService {
    /**
     * create a new verification code
     *
     * @param email the email address
     * @return the verification code
     */
    String createCode(String email);

    /**
     * check if the code is valid
     * @param email the email address
     * @param code the verification code
     * @return 0: valid, 1: invalid, 2: expired
     */
    Integer checkCode(String email, String code);
}
