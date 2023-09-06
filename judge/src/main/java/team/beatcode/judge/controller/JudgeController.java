package team.beatcode.judge.controller;


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.judge.entity.Result;
import team.beatcode.judge.entity.Submission;
import team.beatcode.judge.feign.SubmitFeign;
import team.beatcode.judge.service.VersionService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class JudgeController {
    @Autowired
    VersionService versionService;

    @Autowired
    SubmitFeign submitFeign;

    @Value("${judge.judgeDirectory}")
    private String judgeDirectory;
    @Value("${judge.judgerDirectory}")
    private String judgerDirectory;

    @RequestMapping("Judge")
    public String Judge(@RequestBody Submission submission) throws IOException, InterruptedException {
        System.out.println(submission);
        // 版本管理
        // todo 改数据库数据类型
        try {
            versionService.checkVersion(Integer.parseInt(submission.getProblemId()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return e.toString();
        }

        // 执行评测
        String sid = submission.get_id();
System.out.println("starting judge "+sid);


        /*------------------------------------------------------------------
        //Creat work path and directory
        ------------------------------------------------------------------*/
        String WorkPath = judgeDirectory + File.separator + sid;
        File WorkDirectory = new File(WorkPath);
        if(!WorkDirectory.mkdirs())
        {System.err.println(WorkDirectory);}


        /*------------------------------------------------------------------
        //Copy the judging executable files to work path
        -------------------------------------------------------------------*/
        String JudgerPath = judgerDirectory;
        File JudgerDirectory = new File(JudgerPath);
        copyDirectory(JudgerDirectory,WorkDirectory);

        /*------------------------------------------------------------------
        //creat new file(code,conf,data*)
        -------------------------------------------------------------------*/
        //answer.code
        String SubmissionCode=submission.getSubmission_code();
        String AnswerCode_FilePath = WorkPath +File.separator+"work"+File.separator+"answer.code";
        Files.write(new File(AnswerCode_FilePath).toPath(),SubmissionCode.getBytes());

        //submission.conf
        String SubmissionConf=submission.getConf();
        String Conf_FilePath=WorkPath+File.separator+"work"+File.separator+"submission.conf";
        Files.write(new File(Conf_FilePath).toPath(),SubmissionConf.getBytes());

        //data*
//        String DataPath=WorkPath+File.separator+"data"+File.separator+problem.getProblem_id();
//        File DataDirectory=new File(DataPath);
//        if (!DataDirectory.exists() || !DataDirectory.isDirectory()) {
//            if(!DataDirectory.mkdirs()){System.err.println(DataDirectory);}
//        }
//        int case_n=problem.getCase_n();
//        for (int i = 0; i < case_n; i++) {
//            Testcase testcase=problem.getTest_cases().get(i);
//            Files.write(new File(DataPath+File.separator+"input"+(i+1)+".txt").toPath(), testcase.input.getBytes());
//            Files.write(new File(DataPath+File.separator+"output"+(i+1)+".txt").toPath(), testcase.output.getBytes());
//        }
//        String ProblemConf=problem.getConf();
//        String proConfPath=DataPath+File.separator+"problem.conf";
//        Files.write(new File(proConfPath).toPath(),ProblemConf.getBytes());
        /*---------------------------------------------
        //execute judger and wait
        ---------------------------------------------*/
        String JudgerFilePath = WorkPath + File.separator + "main_judger";
        Process Judger = new ProcessBuilder(JudgerFilePath).directory(new File(WorkPath)).start();
        Judger.waitFor();

        /*---------------------------------------------
        //get result
        ---------------------------------------------*/
        String resultFilePath = WorkPath + File.separator + "result"+File.separator+"result.txt";
        String result = Files.readString(new File(resultFilePath).toPath());
        Submission resSubmission=ParseResult(submission,result);
        // submissionService.saveSubmission(resSubmission);

        /*---------------------------------------------
        //delete directory
        ---------------------------------------------*/
//        deleteDirectory(WorkDirectory);

        // 结束评测，包括保存结果在内的收尾工作都交给submission服务
        submitFeign.WindUp(resSubmission);
        return result;
    }

    private void copyDirectory(File sourceDirectory, File targetDirectory) throws IOException {
        System.out.println("copying "+sourceDirectory.toPath()+" to "+targetDirectory.toPath());
        if (Files.isSymbolicLink(sourceDirectory.toPath())) {
            Files.copy(sourceDirectory.toPath(), targetDirectory.toPath(), LinkOption.NOFOLLOW_LINKS);
        } else {
            if (!targetDirectory.exists()) {
                if (!targetDirectory.mkdirs()) {
                    System.err.println(targetDirectory);
                }
            }

            File[] files = sourceDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    File targetFile = new File(targetDirectory, file.getName());
                    if (file.isDirectory()) {
                        copyDirectory(file, targetFile);
                    } else {
                        Files.copy(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    if(!file.delete()){System.err.println(file);}
                }
            }
        }
        if(!directory.delete()){System.err.println(directory);}
    }

    private Submission ParseResult(Submission sub,String res)
    {
        Pattern resultPattern=Pattern.compile("([\\s\\S]*)details");
        Matcher resultMatcher=resultPattern.matcher(res);
        if(resultMatcher.find())
        {
            sub.setFull_result(resultMatcher.group(1));
            Pattern errorPattern=Pattern.compile("<error>([\\s\\S]*)</error>");
            Matcher errorMatcher=errorPattern.matcher(res);
            if(resultMatcher.group(1).contains("error Judgment Failed"))
            {
                sub.setState("Judgement Failed");
                sub.setResult_score("0");
                sub.setResult_time("0");
                sub.setResult_memory("0");
                if(errorMatcher.find()){
                    sub.setError(errorMatcher.group(1));
                }
                return sub;
            }
            if(resultMatcher.group(1).contains("error Compile Error"))
            {
                sub.setState("Compile Error");
                sub.setResult_score("0");
                sub.setResult_time("0");
                sub.setResult_memory("0");
                if(errorMatcher.find()){
                    String errorMessage=errorMatcher.group(1);
                    String prefix1 = "/usr/tmp/BeatCode/judge/1/\\w+/work/answer\\.\\w+:";
                    String prefix2 = "answer\\.\\w+:";

                    sub.setError(errorMessage.replaceAll(prefix1,"").replaceAll(prefix2,""));
                }
                return sub;
            }
        }

        String stateFlag="Accepted";
        List<Result> ResultDetails = new ArrayList<>();

        Pattern scorePattern=Pattern.compile("score\\s+(.*)");
        Pattern timePattern=Pattern.compile("time\\s+(.*)");
        Pattern memoryPattern=Pattern.compile("memory\\s+(.*)");
        Matcher scoreMatcher=scorePattern.matcher(res);
        Matcher timeMatcher=timePattern.matcher(res);
        Matcher memoryMatcher=memoryPattern.matcher(res);
        if(scoreMatcher.find())
        {
            System.out.println(scoreMatcher.group(0));
            sub.setResult_score(scoreMatcher.group(1));
        }
        if(timeMatcher.find())
        {
            System.out.println(timeMatcher.group(0));
            sub.setResult_time(timeMatcher.group(1));
        }
        if(memoryMatcher.find())
        {
            System.out.println(memoryMatcher.group(0));
            sub.setResult_memory(memoryMatcher.group(1));
        }

        Pattern testsPattern=Pattern.compile("<tests>([\\s\\S]*)</tests>");
        Matcher testsMatcher=testsPattern.matcher(res);
        String DetailsRes="";
        if(testsMatcher.find())
        {
            DetailsRes = (testsMatcher.group(1));
        }

        Pattern testPattern=Pattern.compile("<test([\\s\\S]*?)</test>");
        Matcher testMatcher=testPattern.matcher(DetailsRes);

        Pattern numPattern=Pattern.compile("num=\"([^\"]*)\"");
        Pattern scoPattern=Pattern.compile("score=\"([^\"]*)\"");
        Pattern infPattern=Pattern.compile("info=\"([^\"]*)\"");
        Pattern timPattern=Pattern.compile("time=\"([^\"]*)\"");
        Pattern memPattern=Pattern.compile("memory=\"([^\"]*)\"");
//        Pattern inPattern=Pattern.compile("<in>([\\s\\S]*?)</in>");
//        Pattern outPattern=Pattern.compile("<out>([\\s\\S]*?)</out>");
        Pattern resPattern=Pattern.compile("<res>([\\s\\S]*?)</res>");
        while(testMatcher.find())
        {
            String detail=testMatcher.group(1);
            Result result=new Result();

            Matcher numMatcher=numPattern.matcher(detail);
            if(numMatcher.find()){
                result.setNum(numMatcher.group(1));
            }
            Matcher scoMatcher=scoPattern.matcher(detail);
            if(scoMatcher.find()){
                result.setScore(scoMatcher.group(1));
            }
            Matcher infMatcher=infPattern.matcher(detail);
            if(infMatcher.find()){
                result.setInfo(infMatcher.group(1));
                if(!Objects.equals(infMatcher.group(1), "Accepted") && !Objects.equals(infMatcher.group(1), "Extra Test Passed") &&
                        Objects.equals(stateFlag, "Accepted"))
                    stateFlag=infMatcher.group(1);
            }
            Matcher timMatcher=timPattern.matcher(detail);
            if(timMatcher.find()){
                result.setTime(timMatcher.group(1));
            }
            Matcher memMatcher=memPattern.matcher(detail);
            if(memMatcher.find()){
                result.setMemory(memMatcher.group(1));
            }
//            Matcher inMatcher=inPattern.matcher(detail);
//            if(inMatcher.find()){
//                result.setIn(inMatcher.group(1));
//            }
//            Matcher outMatcher=outPattern.matcher(detail);
//            if(outMatcher.find()){
//                result.setOut(outMatcher.group(1));
//            }
            Matcher resMatcher=resPattern.matcher(detail);
            if(resMatcher.find()){
                result.setRes(resMatcher.group(1));
            }
            ResultDetails.add(result);
        }
        sub.setDetails(ResultDetails);
        sub.setState(stateFlag);
        return sub;
    }

}
