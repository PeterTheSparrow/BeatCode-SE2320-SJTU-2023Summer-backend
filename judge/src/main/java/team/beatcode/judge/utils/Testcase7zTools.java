package team.beatcode.judge.utils;

import java.io.*;

/**
 * 工具<br/>
 * 获取压缩文件并解压到指定位置
 * 迁移自QBank
 */
public class Testcase7zTools {

    //**********************************************配置

    /**
     * 7z运行文件的地址
     */
    private static final String executable7zPath =
            "D:/MyBox/7-Zip/7z.exe"
            .replace("/", File.separator);

    /**
     * 压缩包下载到的文件夹，不是最终的地址
     */
    private static final String testcaseDownloadDirPath =
            "D:/Test/TempDownload"
                    .replace("/", File.separator);

    /**
     * 压缩包解压到的文件夹，工作位置
     */
    private static final String testcaseWorkingDirPath =
            "D:/Test/JudgeWorking"
                    .replace("/", File.separator);

    /**
     * @param pid 题号
     * @return 题目压缩包下载位置
     */
    public static String testcaseDownloadPath(int pid) {
        return String.format("%s%s%d.bin",
                // 不带后缀名会导致极其傻逼的权限冲突
                testcaseDownloadDirPath, File.separator, pid);
    }

    /**
     * @param pid 题号
     * @return 测试数据工作位置
     */
    public static String testcaseWorkingPath(int pid) {
        return String.format("%s%s%d",
                testcaseWorkingDirPath, File.separator, pid);
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

    private static boolean cleansePath(String path) {
        File dest = new File(path);
        if (dest.isFile())
            if (!dest.delete()) {
                System.out.println("Can't delete file " + path);
                return false;
            }
        else if (dest.isDirectory())
            if (!recursiveRemoveDir(dest)) {
                System.out.println("Can't delete dir " + path);
                return false;
            }

        return true;
    }

    //**********************************************业务

    /**
     * 解压收到的临时文件，目标位置会被自动清理
     * @param pid 题号
     * @return 是否解压成功
     */
    public static boolean extract(int pid) {
        try {
            if (!cleansePath(testcaseWorkingPath(pid))) {
                System.out.println("Can't clean " + testcaseWorkingPath(pid));
                return false;
            }

            String[] command = {
                    executable7zPath,
                    "x",
                    testcaseDownloadPath(pid),
                    "-o" + testcaseWorkingPath(pid),
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
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 确保压缩包下载位置为空，用于下载前清理和扫尾工作
     * @param pid 题号
     * @return 如果成功删除或没有需要删除的内容则返回true
     */
    public static boolean cleanse(int pid) {
        return cleansePath(testcaseDownloadPath(pid));
    }
}
