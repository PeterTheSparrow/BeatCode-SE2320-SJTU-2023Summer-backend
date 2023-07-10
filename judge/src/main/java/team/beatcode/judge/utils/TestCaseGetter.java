package team.beatcode.judge.utils;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 工具：得到与解压test-case文件<br/>
 * 设计逻辑：得到压缩包与解压分开，允许分别指定下载压缩包的位置与解压的位置
 */
public class TestCaseGetter {
    private static String getTestCaseFilePath(int pid, String dir) {
        return String.format("%s%s%d.zip", dir, File.separator, pid);
    }
    private static String getTestCaseDirPath(int pid, String dir) {
        return String.format("%s%s%d", dir, File.separator, pid);
    }

    private static boolean rmdir_r(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除文件夹失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子文件夹
        File[] files = dirFile.listFiles();
        if (files != null)
            for (File file : files) {
                // 删除子文件
                if (file.isFile()) {
                    flag = file.delete();
                    if (!flag) break;
                }
                // 递归删除子文件夹
                else if (file.isDirectory()) {
                    flag = rmdir_r(file.getAbsolutePath());
                    if (!flag) break;
                }
            }
        if (!flag) {
            System.out.println("删除文件夹失败！");
            return false;
        }
        // 删除当前文件夹
        if (dirFile.delete()) {
            System.out.println("删除文件夹" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }//blog.csdn.net/yh991314/article/details/107342086

    private static File ensureCleanDir(String dir) {
        File destFolder = new File(dir);
        if (destFolder.exists() && !rmdir_r(dir)) {
            System.out.printf("rmdir fail: %s\n", dir);
            return null;
        }
        if (!destFolder.mkdirs()) {
            System.out.printf("mkdirs fail: %s\n", dir);
            return null;
        }
        return destFolder;
    }

    /**
     * 从题库下载zip格式的压缩包
     * @param restTemplate 记得用@LoadBalanced包装，这里Feign用不了
     * @param pid 题目的题号
     * @param dir 将下载的压缩包放到这个目录，同名文件会被覆盖
     * @return 出错则返回false，懒得区分不同错误原因
     */
    public static boolean downloadTestCasesZip(RestTemplate restTemplate, int pid, String dir) {
        String url_r = String.format("http://question-bank/GetTestCase?pid=%d", pid);
        String url_l = getTestCaseFilePath(pid, dir);

        ResponseEntity<Resource> response = restTemplate.getForEntity(url_r, Resource.class);
        Resource resource = response.getBody();
        if (resource == null) return false;

        try (InputStream inputStream = resource.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(url_l)) {
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 从题库下载zip格式的压缩包<br/>
     * 举例：<br/>
     * 源压缩包被下载到D:/foo/1.zip<br/>
     * 源压缩包的内容为<br/>
     * 1.zip<br/>
     * &emsp;&emsp;bar<br/>
     * &emsp;&emsp;&emsp;&emsp;input1.txt<br/>
     * &emsp;&emsp;&emsp;&emsp;output1.txt<br/>
     * &emsp;&emsp;conf.txt<br/>
     * 调用updateLocalTestCase(1, D:/foo, D:/bar)后D:/bar中出现<br/>
     * 1<br/>
     * &emsp;&emsp;bar<br/>
     * &emsp;&emsp;&emsp;&emsp;input1.txt<br/>
     * &emsp;&emsp;&emsp;&emsp;output1.txt<br/>
     * &emsp;&emsp;conf.txt<br/>
     * 警 告 ！如 果 D:/bar 中 此 前 存 在 文 件 夹 /1，文 件 夹 内 的 内 容 会 被 清 空
     * @param pid 题目的题号
     * @param zd 源压缩包所在的本地文件路径
     * @param fd 解压到的文件夹
     * @return 出错则返回false，懒得区分不同错误原因
     */
    public static boolean updateLocalTestCase(int pid, String zd, String fd) {
        // 找到读取压缩包文件
        File srcZip = new File(getTestCaseFilePath(pid, zd));

        // 创建ZipInputStream对象
        try (ZipInputStream zipInputStream =
                     new ZipInputStream(new FileInputStream(srcZip))) {
            String destPath = getTestCaseDirPath(pid, fd);

            // 清理并创建目标文件夹
            File destFolder = ensureCleanDir(destPath);
            if (destFolder == null) return false;

            // 逐个解压ZIP文件中的条目
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String entryName = zipEntry.getName();
                String filePath = destPath + File.separator + entryName;

                // 如果条目是文件夹，则创建对应的文件夹
                if (zipEntry.isDirectory()) {
                    if (ensureCleanDir(filePath) == null)
                        return false;
                } else {
                    // 如果条目是文件，则写入文件
                    try (FileOutputStream fos = new FileOutputStream(filePath)) {
                        zipInputStream.transferTo(fos);
                    }
                }

                zipInputStream.closeEntry();
                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (FileNotFoundException e) {
            System.out.printf("src fail: %s\n", srcZip);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
