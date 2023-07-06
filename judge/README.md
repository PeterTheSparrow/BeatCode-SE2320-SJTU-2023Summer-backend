# judge服务使用说明
以下为judge服务目前阶段工作原理，需要的参数以及实现的功能说明

#### 1. 使用前提
在*submission_mongo*数据库中对本次需要提交的记录有完整的信息。
*submission_mongo*中需求的字段包括：
 - submission_id
 - submission_code
 - problem_id
 - case_n
 - conf
 
 另外，也需要评测机本地的题目数据缓存目录中（路径位于"/usr/tmp/BeatCode/data"），有对应于*problem_id*的题目数据文件夹存在（评测功能不会自动更新评测机本地题目数据缓存）。
 
 *注：若环境本身未曾在/usr目录下新建/tmp目录，可能会出现权限不够的情况，需要在建立目录后执行指令：*
 >sudo chmod a=rwx /usr/tmp
 #### 2.使用过程
 在确保使用前提的情况下，只需向本服务运行的端口上发送
 > /judge?sid=(0-9+)
 
其中sid即*submission_id*。

发送后，服务端会执行对应编号的评测服务，一直等待评测完成，并最终将结果填入*submission_mongo*表中后，返回一个包含*result.txt*文件中所有内容的*String*。
#### 3.处理结果
若一切正常运行，则本服务将产生并储存以下内容：
 - result_score
 - result_time
 - result_memory
 - full_result
 - details
 	- num
    - score
    - info
    - time
    - memory
    - in
    - out
    - res

例如
![image](/api/codehub/v1/projects/7457897/uploads/a7148952-0190-448a-89df-6b812b4c12a1/1688541766766.png '1688541766766.png')

#### 4.其他说明和todo
1.题目数据文件更新逻辑
2.评测错误状态处理


*对于两个conf文件内容的说明*
>#### *problem.conf文件内容*
>例：
use_builtin_judger on
use_builtin_checker ncmp
n_tests 10
n_ex_tests 5
n_sample_tests 1
input_pre www
input_suf in
output_pre www
output_suf out
time_limit 1
memory_limit 256
output_limit 64
**测评类型**：*use_builtin_judger on*制定了使用内置的测评器。一般情况下还是使用内置测评器，自定义测评器实现可放在之后实现。
**输入输出文件**：题目数据的输入输出文件名为 "pre%d.suf"。若没有在conf文件中特定指定该类型，则默认输入输出文件为"input%d.txt"和"output%d.txt".
**额外数据，样例数据**：支持相关功能，但暂时没有实现的必要，故不作说明。因此conf里相关的行也可以不写。
**答案检查器(checker)**：数据配置文件中，use_builtin_checker ncmp 的意思是将本题的 checker 设置为 ncmp。checker 是指判断选手输出是否正确的答案检查器。一般来说，如果输出结果为整数序列，那么用 ncmp 就够了。ncmp 会比较标准答案的整数序列和选手输出的整数序列。如果是忽略所有空白字符，进行字符串序列的比较，可以用 wcmp。如果你想按行比较（不忽略行末空格，但忽略文末回车），可以使用 fcmp。
**时空限制**：time_limit 控制的是一个测试点的时间限制，单位为秒，可以是小数（至多三位小数，即最高精确到毫秒）。
memory_limit 控制的是一个测试点的空间限制，单位为 MB。output_limit 控制的是程序的输出长度限制，单位也为 MB。注意这两个限制都不能为小数。

>#### *submission.conf文件内容*
>例：
problem_id 1
language C++
目前仅需这两项即可