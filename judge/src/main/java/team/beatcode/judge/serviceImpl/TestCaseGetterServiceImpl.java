package team.beatcode.judge.serviceImpl;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.beatcode.judge.service.TestCaseGetterService;

import java.io.*;

import java.util.zip.*;

@Service
public class TestCaseGetterServiceImpl implements TestCaseGetterService {
    private String localZip(int pid, String dir) {
        return String.format("%s/%d.zip", dir, pid);
    }

    public static boolean rmdir_r(String dir) {
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

    @Override
    public boolean downloadTestCasesZip(RestTemplate restTemplate, int pid, String dir) {
        String url_r = String.format("http://question-bank/GetTestCase?pid=%d", pid);
        String url_l = localZip(pid, dir);

        ResponseEntity<Resource> response = restTemplate.getForEntity(url_r, Resource.class);
        Resource resource = response.getBody();
        if (resource == null) return false;

        try (InputStream inputStream = resource.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(url_l)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean updateLocalTestCase(int pid, String zd, String fd) {
        // 找到读取压缩包文件
        File srcZip = new File(localZip(pid, zd));

        // 清理并创建目标文件夹
        File destFolder = ensureCleanDir(fd);
        if (destFolder == null) return false;

        // 创建ZipInputStream对象
        try (ZipInputStream zipInputStream =
                     new ZipInputStream(new FileInputStream(srcZip))) {
            // 逐个解压ZIP文件中的条目
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String entryName = zipEntry.getName();
                String filePath = fd + File.separator + entryName;

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
