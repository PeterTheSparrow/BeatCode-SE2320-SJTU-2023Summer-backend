package team.beatcode.qbank.utils;

public class Macros {
    public static final String TEST_CASE_DIRECTORY =
            "D:\\Work\\Software-School-Projects\\BeatCode-test-case";

    public static String getTestCases(int pid) {
        return String.format("%s%d.zip", TEST_CASE_DIRECTORY, pid);
    }
}
