package team.beatcode.judge.service;

public interface VersionService {
    /**
     * 本地文件检查更新逻辑。会自动向QBank发送请求。
     * @param pid 题号
     * @return 出错时返回false。如果返回true，文件必然是最新的。
     */
    boolean checkVersion(int pid);
}
