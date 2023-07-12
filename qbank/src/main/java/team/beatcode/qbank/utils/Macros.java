package team.beatcode.qbank.utils;

import java.util.Arrays;

public class Macros {
    public static final String PARAM_PAGE = "pageIndex";
    public static final String PARAM_PAGE_SIZE = "pageSize";
    public static final String PARAM_TITLE_KEY = "titleContains";
    public static final String PARAM_HARD_LEVEL = "hardLevel";

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

    public static String getTestCaseFilePath(int pid) {
        return String.format("%s%d.zip", TEST_CASE_DIRECTORY, pid);
    }
}
