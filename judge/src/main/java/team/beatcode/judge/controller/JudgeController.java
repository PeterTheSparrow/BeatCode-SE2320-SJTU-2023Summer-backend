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
import team.beatcode.judge.entity.Testcase;
import team.beatcode.judge.service.ProblemService;
import team.beatcode.judge.service.SubmissionService;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

    @Value("${judge.baseDirectory}")
    private String baseDirectory;

    @RequestMapping("judge")
    public String judgeBySid(@RequestParam("sid") int sid) throws IOException, InterruptedException {
        Submission submission=submissionService.getSubmission(sid);
        int pid=submission.getProblem_id();
        Problem problem=problemService.getProblem(pid);


        /*------------------------------------------------------------------
        //Creat work path and directory
        ------------------------------------------------------------------*/
        String WorkPath = baseDirectory + File.separator + sid;
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
        String DataPath=WorkPath+File.separator+"data"+File.separator+problem.getProblem_id();
        File DataDirectory=new File(DataPath);
        if (!DataDirectory.exists() || !DataDirectory.isDirectory()) {
            if(!DataDirectory.mkdirs()){System.err.println(DataDirectory);}
        }
        int case_n=problem.getCase_n();
        for (int i = 0; i < case_n; i++) {
            Testcase testcase=problem.getTest_cases().get(i);
            Files.write(new File(DataPath+File.separator+"input"+(i+1)+".txt").toPath(), testcase.input.getBytes());
            Files.write(new File(DataPath+File.separator+"output"+(i+1)+".txt").toPath(), testcase.output.getBytes());
        }
        String ProblemConf=problem.getConf();
        String proConfPath=DataPath+File.separator+"problem.conf";
        Files.write(new File(proConfPath).toPath(),ProblemConf.getBytes());
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
        submission=ParseResultToSubmission(submission,result);
        return result;

//        /*---------------------------------------------
//        //delete directory
//        ---------------------------------------------*/
//        deleteDirectory(WorkDirectory);
//        return problem.getConf();
    }
    private void copyDirectory(File sourceDirectory, File targetDirectory) throws IOException{
        if (sourceDirectory.isDirectory()) {
            if (!targetDirectory.exists()) {
                if(!targetDirectory.mkdirs()){System.err.println(targetDirectory);}
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

    private Submission ParseResultToSubmission(Submission sub,String res)
    {
        List<Result> ResultDetails = new ArrayList<Result>();

        // Pattern for matching lines with attribute-value pairs
        Pattern attributePattern = Pattern.compile("(\\w+)\\s+(.*)");

        // Split the text into lines
        String[] lines = res.split("\\r?\\n");

        // Process each line
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            // Check if line matches attribute-value pattern
            Matcher matcher = attributePattern.matcher(line);
            if (matcher.matches()) {
                String attribute = matcher.group(1);
                String value = matcher.group(2);

                // Assign values to corresponding submission properties
                if ("score".equals(attribute)) {
                    sub.setResult_score(value);
                } else if ("time".equals(attribute)) {
                    sub.setResult_time(value);
                } else if ("memory".equals(attribute)) {
                    sub.setResult_memory(value);
                } else if ("test".equals(attribute)) {
                    // Process test details
                    Result result = parseResultDetails(lines, i);
                    ResultDetails.add(result);
                }
            }
        }

        // Assign details to submission
        sub.setDetails(ResultDetails);

        return sub;

    }
    private Result parseResultDetails(String[] lines, int startIndex) {
        Result result = new Result();

        // Pattern for matching attribute-value pairs in test details
        Pattern detailAttributePattern = Pattern.compile("<(\\w+)>(.*)</\\w+>");

        // Process lines starting from startIndex
        for (int i = startIndex; i < lines.length; i++) {
            String line = lines[i].trim();

            // Check if line matches attribute-value pattern
            Matcher matcher = detailAttributePattern.matcher(line);
            if (matcher.matches()) {
                String attribute = matcher.group(1);
                String value = matcher.group(2);

                // Assign values to corresponding result properties
                if ("num".equals(attribute)) {
                    result.setNum(value);
                } else if ("score".equals(attribute)) {
                    result.setScore(value);
                } else if ("info".equals(attribute)) {
                    result.setInfo(value);
                } else if ("time".equals(attribute)) {
                    result.setTime(value);
                } else if ("memory".equals(attribute)) {
                    result.setMemory(value);
                } else if ("in".equals(attribute)) {
                    result.setIn(value);
                } else if ("out".equals(attribute)) {
                    result.setOut(value);
                } else if ("res".equals(attribute)) {
                    result.setRes(value);
                }
            } else if (line.equals("</test>")) {
                // Reached end of test details
                break;
            }
        }

        return result;
    }

}
