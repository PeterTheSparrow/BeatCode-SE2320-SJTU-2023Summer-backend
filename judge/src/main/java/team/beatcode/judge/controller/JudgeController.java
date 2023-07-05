package team.beatcode.judge.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.judge.entity.Problem;
import team.beatcode.judge.entity.Result;
import team.beatcode.judge.entity.Submission;
import team.beatcode.judge.service.ProblemService;
import team.beatcode.judge.service.SubmissionService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class JudgeController {
    @Autowired
    SubmissionService submissionService;
    @Autowired
    ProblemService problemService;
    @Autowired
    @Qualifier("customResourceLoader")
    private ResourceLoader resourceLoader;

    @Value("${judge.judgeDirectory}")
    private String judgeDirectory;

    @RequestMapping("judge")
    public String judgeBySid(@RequestParam("sid") int sid) throws IOException, InterruptedException {
        Submission submission=submissionService.getSubmission(sid);
        int pid=submission.getProblem_id();
        Problem problem=problemService.getProblem(pid);


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
        String JudgerPath = resourceLoader.getResource("classpath:judger").getURI().getPath();
        File JudgerDirectory = new File(JudgerPath);
        copyDirectory(JudgerDirectory,WorkDirectory);

        /*------------------------------------------------------------------
        //creat new file(code,conf,data*)
        -------------------------------------------------------------------*/
        //answer.code
        String SubmissionCode=submission.getSubmission_code();
        String AnswerCode_FilePath=WorkPath+File.separator+"work"+File.separator+"answer.cpp";
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
        submission.setCase_n(problem.getCase_n());
        Submission resSubmission=ParseResult(submission,result);
        resSubmission.setFull_result(result);
        submissionService.saveSubmission(resSubmission);

        /*---------------------------------------------
        //delete directory
        ---------------------------------------------*/
        deleteDirectory(WorkDirectory);
        return result;
    }
    private void copyDirectory(File sourceDirectory, File targetDirectory) throws IOException {

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
        Pattern inPattern=Pattern.compile("<in>([\\s\\S]*?)</in>");
        Pattern outPattern=Pattern.compile("<out>([\\s\\S]*?)</out>");
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
            }
            Matcher timMatcher=timPattern.matcher(detail);
            if(timMatcher.find()){
                result.setTime(timMatcher.group(1));
            }
            Matcher memMatcher=memPattern.matcher(detail);
            if(memMatcher.find()){
                result.setMemory(memMatcher.group(1));
            }
            Matcher inMatcher=inPattern.matcher(detail);
            if(inMatcher.find()){
                result.setIn(inMatcher.group(1));
            }
            Matcher outMatcher=outPattern.matcher(detail);
            if(outMatcher.find()){
                result.setOut(outMatcher.group(1));
            }
            Matcher resMatcher=resPattern.matcher(detail);
            if(resMatcher.find()){
                result.setRes(resMatcher.group(1));
            }
            ResultDetails.add(result);
        }
        sub.setDetails(ResultDetails);
        return sub;
    }

}
