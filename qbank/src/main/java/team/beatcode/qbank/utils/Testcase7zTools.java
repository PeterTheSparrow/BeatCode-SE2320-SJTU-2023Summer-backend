package team.beatcode.qbank.utils;

import org.springframework.web.multipart.MultipartFile;
import team.beatcode.qbank.entity.Problem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;

/**
 * 工具<br/>
 * 本地存储压缩版本的题目内容
 * 管理员上传新文件后，先下载到临时文件夹，解压、检查通过后更新数据库、应用新题目文件
 */
public class Testcase7zTools {

    //**********************************************配置

    /**
     * 7z运行文件的地址
     */
    private static final String executable7zPath =
            "D:/MyBox/7-Zip/7z.exe"
            .replace("/", File.separator);
    private static String configFilePath(String task) {
        return String.format("%s%s%s",
                testcaseTempUnzipPath(task), File.separator, "problem.conf");
    }

    /**
     * @param task 临时任务编号
     * @return 题目临时文件夹位置
     */
    private static String testcaseTempDirPath(String task) {
        return String.format("%s%s%s%s",
                Macros.testcaseDirectoryPath, File.separator, "tmp", task);
    }

    /**
     * @param task 临时任务编号
     * @return 题目预下载位置
     */
    private static String testcaseTempZippedPath(String task) {
        return String.format("%s%s%s",
                testcaseTempDirPath(task), File.separator, "data");
    }

    /**
     * @param task 临时任务编号
     * @return 题目预下载后的解压位置
     */
    private static String testcaseTempUnzipPath(String task) {
        return String.format("%s%s%s",
                testcaseTempDirPath(task), File.separator, "data");
    }

    //**********************************************内部工具函数

    /**
     * @param dir 要删除的文件夹
     * @return 成功为true
     */
    private static boolean recursiveRemoveDir(File dir) {
        File[] files = dir.listFiles();
        if (files == null) return false;
        for (File file : files) {
            if (file.isFile())
                if (!file.delete())
                    return false;
                else if (!recursiveRemoveDir(dir))
                    return false;
        }
        return dir.delete();
    }

    /**
     * 只是短一点的报错
     * @param e 异常
     */
    private static void reportException(Throwable e) {
        StackTraceElement traceElement = e.getStackTrace()[1];
        System.out.printf("%s-line %d: %s", traceElement.getFileName(),
                traceElement.getLineNumber(), traceElement.getClassName());
    }

    //**********************************************业务

    public static boolean downloadToTmp(String task, MultipartFile file) {
        try {
            InputStream stream = file.getInputStream();
            try (FileOutputStream outputStream =
                    new FileOutputStream(testcaseTempZippedPath(task))) {
                stream.transferTo(outputStream);
            }
            return true;
        } catch (IOException e) {
            reportException(e);
            System.out.printf(": %s\n", task);
            return false;
        }
    }

    /**
     * 解压收到的临时文件
     * @param task 临时任务编号
     * @return 是否解压成功
     */
    public static boolean unzipToTmp(String task) {
        try {
            String[] command = {
                    executable7zPath,
                    "x",
                    testcaseTempZippedPath(task),
                    "-o" + testcaseTempUnzipPath(task),
                    // 直接覆盖模式
                    "-aoa"
            };

            ProcessBuilder proc = new ProcessBuilder(command);
            Process process = proc.start();

            InputStream errorStream = process.getErrorStream();

            try (ByteArrayOutputStream listener = new ByteArrayOutputStream()) {
                errorStream.transferTo(listener);

                // Wait for the process to complete
                int exitCode = process.waitFor();
                String error = listener.toString();
                switch (exitCode) {
                    case 0 -> {
                        return true;
                    }
                    case 1 -> {
                        System.out.printf("7z Exit 1. Warning: \n%s\n", error);
                        return true;
                    }
                    case 2, 7, 8, 255 -> {
                        System.out.printf("7z Exit %d. Info: \n%s\n", exitCode, error);
                        return false;
                    }
                    default -> {
                        System.out.printf("7z Unknown Exit %d. \n%s\n", exitCode, error);
                        return false;
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            reportException(e);
            System.out.printf(": %s\n", task);
            return false;
        }
    }

    public static class AnalyzeReport {
        public String log;
        public Problem.Config config;

        public AnalyzeReport() {
            config = new Problem.Config();
            log = "";
        }

        public void log_no_conf() {
            log += "Lack file: problem.conf\n";
        }

        public void log_no_n_tests() {
            log += "Lack property: n_tests\n";
        }

        public void log_unmatched_n_tests(String file) {
            log += String.format("Lack testcase file: %s\n", file);
        }

        public void log_no_t_limits() {
            log += "Lack property: time_limit\n";
        }

        public void log_t_limits_format() {
            log += "Format: time_limit must match ^\\d+(\\.\\d{1,3})?$";
        }

        public void log_no_o_limits() {
            log += "Lack property: output_limit\n";
        }

        public void log_no_m_limits() {
            log += "Lack property: memory_limit\n";
        }
    }
    /**
     * 获取题目配置信息
     * @param task 临时任务编号
     * @return 配置文件内容，失败则返回null
     */
    public static AnalyzeReport analyze(String task) {
        AnalyzeReport report = new AnalyzeReport();

        File conf = new File(configFilePath(task));
        Map<String, String> conf_kv = new HashMap<>();
        // 解析配置文件内容
        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     new FileInputStream(conf)))){
            // 按行读取检测
            Stream<String> lines = reader.lines();
            lines.forEach(str -> {
                String[] parts = str.split(" ");
                if (parts.length == 2)
                    conf_kv.put(parts[0], parts[1]);
            });
        } catch (FileNotFoundException e) {
            // 要求文件存在，正常情况下可能不存在
            report.log_no_conf();
            return report;
        } catch (IOException e) {
            reportException(e);
            System.out.printf(": %s\n", task);
            return null;
        }
        // 检查测试集数
        String s_testcaseCount = conf_kv.get("n_tests");
        if (s_testcaseCount == null)
            report.log_no_n_tests();
        else try {
            Integer n_test = Integer.decode(s_testcaseCount);
            report.config.setTests(n_test);
            if (n_test < 0) report.log_no_n_tests();
            else {
                // 检查是否匹配
                String i_pre = conf_kv.get("input_pre");
                if (i_pre == null) i_pre = Macros.JudgerConfDefaults.input_prefix;
                String i_suf = conf_kv.get("input_suf");
                if (i_suf == null) i_suf = Macros.JudgerConfDefaults.suffix;
                String o_pre = conf_kv.get("output_pre");
                if (o_pre == null) o_pre = Macros.JudgerConfDefaults.output_prefix;
                String o_suf = conf_kv.get("output_suf");
                if (o_suf == null) o_suf = Macros.JudgerConfDefaults.suffix;

                File holder = new File(testcaseTempUnzipPath(task));
                String[] f = holder.list();
                if (f == null) {
                    System.out.printf("Unzipped file missing: %s\n", task);
                    return null;
                }
                Set<String> files = new HashSet<>(Arrays.asList(f));

                for (int i = n_test; i > 0; i--) {
                    String inp = String.format("%s%d.%s", i_pre, i, i_suf);
                    if (!files.contains(inp))
                        report.log_unmatched_n_tests(inp);
                    String oup = String.format("%s%d.%s", o_pre, i, o_suf);
                    if (!files.contains(oup))
                        report.log_unmatched_n_tests(oup);
                }
            }
        } catch (NumberFormatException e) {
            report.log_no_n_tests();
        }
        // 读取Limits配置
        String tl = conf_kv.get("time_limit");
        String ol = conf_kv.get("output_limit");
        String ml = conf_kv.get("memory_limit");

        if (tl == null)
            report.log_no_t_limits();
        else try {
            if (!tl.matches("^\\d+(\\.\\d{1,3})?$"))
                report.log_t_limits_format();
            float tl_f = Float.parseFloat(tl) * 1000f;
            report.config.setTLimit((int) tl_f);
        } catch (NumberFormatException e) {
            report.log_t_limits_format();
        }
        if (ol == null)
            report.log_no_o_limits();
        else try {
            report.config.setOLimit(Integer.decode(ol));
        } catch (NumberFormatException e) {
            report.log_no_o_limits();
        }
        if (ml == null)
            report.log_no_m_limits();
        else try {
            report.config.setMLimit(Integer.decode(ml));
        } catch (NumberFormatException e) {
            report.log_no_m_limits();
        }

        return report;
    }

    /**
     * 应用新文件，更新题目文件
     * @param task 临时任务编号
     * @param pid 题号
     */
    public static void update(String task, int pid) {
        // 最简捷的方法：将下载的压缩包直接移动并替换原文件
        try {
            Files.move(
                    Path.of(testcaseTempZippedPath(task)),
                    Path.of(Macros.testcaseZippedPath(pid)),
                    StandardCopyOption.REPLACE_EXISTING
                    );
        } catch (IOException e) {
            reportException(e);
            System.out.printf(": %s -> %d\n", task, pid);
        }
    }

    /**
     * 清除临时文件
     * @param task 临时任务编号
     */
    public static void cleanse(String task) {
        recursiveRemoveDir(
                new File(testcaseTempUnzipPath(task)));
    }
}
