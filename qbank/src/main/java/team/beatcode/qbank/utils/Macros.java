package team.beatcode.qbank.utils;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.Arrays;

public class Macros {
    @Value("${utils.fileVersion.storage}")
    private static String testcaseDirectoryPathYml;

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

    public static class CheckerType {
        public static final String IntegerSequence = "ncmp";
        public static final String StringIgnoringBlank = "wcmp";
        public static final String LineIgnoringEnter = "fcmp";
    }

    public static class JudgerConfDefaults {
        public static final String input_prefix = "input";
        public static final String output_prefix = "output";
        public static final String suffix = "txt";
    }

    /**
     题库文件存放在这个文件夹中
     */
    public static final String testcaseDirectoryPath =
            testcaseDirectoryPathYml.replace("/", File.separator);
    /**
     * 注：7z可以自动识别文件的压缩方式和格式，不需要带后缀名。后缀名与实际格式不符会报警
     * @param pid 题号
     * @return 题目压缩文件位置
     */
    public static String testcaseZippedPath(int pid) {
        return String.format("%s%s%d",
                testcaseDirectoryPath, File.separator, pid);
    }
}
