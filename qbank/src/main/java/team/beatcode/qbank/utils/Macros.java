package team.beatcode.qbank.utils;

import java.util.Arrays;

public class Macros {
    public static final String PARAM_PAGE = "pageIndex";
    public static final String PARAM_PAGE_SIZE = "pageSize";
    public static final String PARAM_TITLE_KEY = "titleContains";
    public static final String PARAM_HARD_LEVEL = "hardLevel";
    public static final String PARAM_USER_ID = "user_id";

    private static final String[] HARD_LEVEL_LIST = {
            "简单", "中等", "困难"
    };
    public static boolean correctHardLevel(String hardLevel) {
        if (hardLevel == null)
            return false;
        if (hardLevel.equals(""))
            return true;
        return Arrays.asList(HARD_LEVEL_LIST).contains(hardLevel);
    }

    public static final String TEST_CASE_DIRECTORY =
            "D:\\Work\\Software-School-Projects\\BeatCode-test-case";

    public static class ProblemType {
        public static final String CONVENTIONAL = "conventional";
    }

    public static class CheckerType {
        public static final String IntegerSequence = "ncmp";
        public static final String StringIgnoringBlank = "wcmp";
        public static final String LineIgnoringEnter = "fcmp";
    }

    public static String getTestCaseFilePath(int pid) {
        return String.format("%s%d.zip", TEST_CASE_DIRECTORY, pid);
    }
}
