package team.beatcode.qbank.utils;

public class Macros {
    public static final String PARAM_PAGE = "pageIndex";
    public static final String PARAM_PAGE_SIZE = "pageSize";
    public static final String PARAM_SEARCH_TYPE = "searchIndex";
    public static final String PARAM_KEYWORD = "searchKeyWord";

    public static final String TEST_CASE_DIRECTORY =
            "D:\\Work\\Software-School-Projects\\BeatCode-test-case";

    public static String getTestCaseFilePath(int pid) {
        return String.format("%s%d.zip", TEST_CASE_DIRECTORY, pid);
    }
}
